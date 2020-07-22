package com.excel.util;

import com.excel.model.ParseModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.*;


/**
 * excel导入工具类
 *
 * @author fuyangrong
 * @date 2017/12/4
 */
public class ExcelImportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportUtil.class);

    private static final String XLSX_SUFFIX = ".xlsx";
    private static final String XLS_SUFFIX = ".xls";


    private static Workbook readFile(File file) throws Exception {
        InputStream fis = new FileInputStream(file);
        Workbook workbook;
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
     *
     * @param excel
     * @param configXml
     * @return List<T>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> convertToClass(File excel, File configXml) throws Exception {
        List<T> list = new ArrayList<>();
        Workbook wb = ExcelImportUtil.readFile(excel);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Sheet {} \"{}\" has {} row(s).", k, wb.getSheetName(k), rows);
            }
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
                    ParseModel parseModel = (ParseModel) map.get(String.valueOf(i));
                    String fieldName = parseModel.getFieldName();
                    String javaType = parseModel.getJavaType();
                    Class c2 = Class.forName(javaType);
                    if (c2 == String.class && cellValue instanceof Double) {
                        Double doubleVal = (Double) cellValue;
                        cellValue = doubleVal.toString();
                    }
                    if (c2 == String.class && cellValue instanceof Integer) {
                        Integer integerVal = (Integer) cellValue;
                        cellValue = integerVal.toString();
                    }
                    if (c2 == String.class && cellValue instanceof Long) {
                        Long longVal = (Long) cellValue;
                        cellValue = longVal.toString();
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
                    value = evaluator.evaluateFormulaCell(cell);
                    break;
                case CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = simpleDateFormat.format(cell.getDateCellValue());
                    } else {
                        value = cell.getNumericCellValue();
                    }
                    break;
                case CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;

                case CELL_TYPE_BLANK:
                    value = "";
                    break;

                case CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
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


    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMapRelation(Row row, File configXml) throws Exception {
        if (row == null) {
            throw new Exception("导入的excel的首行不能为空");
        }
        Map<String, Object> map = new HashMap<>(row.getLastCellNum());
        Map<String, Object> rcxu = ParseMapperXmlUtil.getAssociation(configXml);
        map.put("className", rcxu.get("className").toString());
        List<ParseModel> list = (List<ParseModel>) rcxu.get("list");
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = cell.getStringCellValue();
            // TODO 忽略enable=false的行
            Optional<ParseModel> optional = list.stream().filter(parseModel -> cellValue.equals(parseModel.getColumnName())).findFirst();
            ParseModel m;
            if (optional.isPresent()) {
                m = optional.get();
            } else {
                throw new Exception("未找到属性[" + cellValue + "]");
            }
            m.setIndex(i);
            map.put(String.valueOf(i), m);
        }
        return map;
    }

}
