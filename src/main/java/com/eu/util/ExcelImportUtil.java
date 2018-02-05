package com.eu.util;

import com.eu.model.Model;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.*;


/**
 * excel导入工具类
 * @auther fuyangrong
 * @create 2017/12/4
 */
public class ExcelImportUtil {

    private static final String XLSX_SUFFIX = ".xlsx";
    private static final String XLS_SUFFIX = ".xls";


    private static Workbook readFile(File file) throws Exception {
        InputStream fis = new FileInputStream(file);
        Workbook workbook = null;
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (XLSX_SUFFIX.equals(suffix)) {
            workbook = new XSSFWorkbook(fis);
        } else if (XLS_SUFFIX.equals(suffix)) {
            workbook = new HSSFWorkbook(fis);
        } else {
            throw new Exception("文件类型错误，必须是以“.xls”或“xlsx”结尾的文件");
        }
        fis.close();
        return workbook;

    }

    /**
     * 将excel行转换为需要的类对象集合
     * @param excel
     * @param configXml
     * @return List<T>
     * @throws Exception
     */
    public static <T> List<T> convertToClass(File excel, File configXml) throws Exception {
        List<T> list = new ArrayList<>();
        Workbook wb = ExcelImportUtil.readFile(excel);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows
                    + " row(s).");
            Row firstRow = sheet.getRow(0);
            Map<String, Object> map = getMapRelation(firstRow, configXml);
            Class c = Class.forName(map.get("className").toString());
            for (int r = 1; r < rows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                T object = (T) c.newInstance();
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    Object cellValue = formatCellValue(cell, evaluator);
                    Model model = (Model) map.get(String.valueOf(i));
                    String fieldName = model.getFieldName();
                    String javaType = model.getJavaType();
                    Class c2 = Class.forName(javaType);
                    if (c2 == String.class && cellValue instanceof Double) {
                        double dnum = (Double) formatCellValue(cell, evaluator);
                        cellValue = String.valueOf((int) dnum);
                    } else if (c2 == Integer.class) {
                        double dnum = (Double) formatCellValue(cell, evaluator);
                        cellValue = new Integer((int) dnum);
                    }
                    Method method = c.getDeclaredMethod("set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), c2);
                    method.invoke(object, cellValue);
                }
                list.add(object);
            }
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    private static Object formatCellValue(Cell cell, FormulaEvaluator evaluator) throws Exception {
        Object value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case CELL_TYPE_FORMULA:
                    value = new Integer(evaluator.evaluateFormulaCell(cell));
                    break;
                case CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = simpleDateFormat.format(cell.getDateCellValue());
                    } else {
                        value = new Double(cell.getNumericCellValue());
                    }
                    break;

                case CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;

                case CELL_TYPE_BLANK:
                    value = "";
                    break;

                case CELL_TYPE_BOOLEAN:
                    value = new Boolean(cell.getBooleanCellValue());
                    break;

                case CELL_TYPE_ERROR:
                    value = String.valueOf(cell.getErrorCellValue());
                    break;

                default:
                    throw new Exception("UNKNOWN value of type " + cell.getCellType());
            }
        }
        return value;
    }

    private static Map<String, Object> getMapRelation(Row row, File configXml) throws Exception {
        Map<String, Object> map = new HashMap();
        if (row == null) {
            throw new Exception("导入的excel的首行不能为空");
        }
        Map<String, Object> rcxu = ResolveConfigXmlUtil.getAssociation(configXml);
        map.put("className", rcxu.get("className").toString());
        List<Model> list = (List<Model>) rcxu.get("list");

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = cell.getStringCellValue();
            Model m = list.stream().filter(model -> cellValue.equals(model.getColunmName())).findFirst().get();
            m.setIndex(i);
            map.put(String.valueOf(i), m);
        }
        return map;
    }

}
