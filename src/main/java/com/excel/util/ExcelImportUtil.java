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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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


    /**
     * 按excel读取文件
     *
     * @param file excel文件
     * @return Workbook
     * @throws Exception 异常
     */
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
            fis.close();
            throw new IllegalArgumentException("文件类型错误，必须是以“.xls”或“.xlsx”结尾的文件");
        }
        fis.close();
        return workbook;
    }

    /**
     * excel文件解析
     *
     * @param excel     excel
     * @param mapperXml 映射文件
     * @param <T>       JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, File mapperXml) throws Exception {
        return parseFile(excel, null, null, mapperXml);
    }

    /**
     * excel文件解析
     *
     * @param excel       excel
     * @param titleRowNum 标题行，默认0
     * @param mapperXml   映射文件
     * @param <T>         JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, Integer titleRowNum, File mapperXml) throws Exception {
        return parseFile(excel, null, titleRowNum, mapperXml);
    }

    /**
     * excel文件解析
     *
     * @param excel       excel
     * @param sheetIndex  表格在工作簿中的下标，默认0
     * @param titleRowNum 标题行，默认0
     * @param mapperXml   映射文件
     * @param <T>         JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, Integer sheetIndex, Integer titleRowNum, File mapperXml) throws Exception {
        Workbook wb = ExcelImportUtil.readFile(excel);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = wb.getSheetAt(sheetIndex == null ? 0 : sheetIndex);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sheet 0 \"{}\" has {} row(s).", wb.getSheetName(0), sheet.getPhysicalNumberOfRows());
        }
        Row firstRow = sheet.getRow(titleRowNum == null ? 0 : titleRowNum);
        List<ParseModel> fieldMapperList = getFieldMapperList(firstRow, mapperXml);
        return convertToJavaObject(sheet, evaluator, fieldMapperList, titleRowNum == null ? 1 : titleRowNum + 1);
    }

    /**
     * excel文件解析
     *
     * @param excel          excel
     * @param parseModelList 关系对象集合
     * @param <T>            JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, List<ParseModel> parseModelList) throws Exception {
        return parseFile(excel, null, parseModelList);
    }

    /**
     * excel文件解析
     *
     * @param excel          excel
     * @param startRowNum    开始解析行下标，默认1
     * @param parseModelList 关系对象集合
     * @param <T>            JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, Integer startRowNum, List<ParseModel> parseModelList) throws Exception {
        return parseFile(excel, null, startRowNum, parseModelList);
    }

    /**
     * excel文件解析
     *
     * @param excel          excel
     * @param sheetIndex     表格在工作簿中的下标,默认0
     * @param startRowNum    开始解析行下标，默认1
     * @param parseModelList 关系对象集合
     * @param <T>            JPO
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> parseFile(File excel, Integer sheetIndex, Integer startRowNum, List<ParseModel> parseModelList) throws Exception {
        Workbook wb = ExcelImportUtil.readFile(excel);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = wb.getSheetAt(sheetIndex == null ? 0 : sheetIndex);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sheet 0 \"{}\" has {} row(s).", wb.getSheetName(0), sheet.getPhysicalNumberOfRows());
        }
        return convertToJavaObject(sheet, evaluator, parseModelList, startRowNum == null ? 1 : startRowNum);
    }


    /**
     * 将sheet转换为需要的类对象集合
     *
     * @param sheet          表格
     * @param evaluator      公式执行
     * @param parseModelList 关系对象集合
     * @param startRowNum    开始解析的行
     * @param <T>            java object
     * @return List<T>
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> convertToJavaObject(Sheet sheet, FormulaEvaluator evaluator, List<ParseModel> parseModelList, Integer startRowNum) throws Exception {
        List<T> list = new ArrayList<>();
        int rows = sheet.getPhysicalNumberOfRows();
        String className = parseModelList.get(0).getClassName();
        Class<?> c = Class.forName(className);
        if (startRowNum == null) {
            startRowNum = 1;
        }
        for (int r = startRowNum; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            T object = (T) c.newInstance();
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                Object cellValue = formatCellValue(cell, evaluator);
                int finalI = i;
                Optional<ParseModel> first = parseModelList.stream().filter(it -> it.getIndex() != null && it.getIndex() == finalI).findFirst();
                if (first.isPresent()) {
                    ParseModel parseModel = first.get();
                    String fieldName = parseModel.getFieldName();
                    String javaType = parseModel.getJavaType();
                    Class<?> c2 = Class.forName(javaType);
                    if (c2 == String.class && (cellValue instanceof Integer || cellValue instanceof Long || cellValue instanceof Double)) {
                        cellValue = String.valueOf(cellValue);
                    }
                    if (c2 == Integer.class && cellValue instanceof Double) {
                        cellValue = ((Double) cellValue).intValue();
                    }
                    if (c2 == Long.class && cellValue instanceof Double) {
                        cellValue = ((Double) cellValue).longValue();
                    }
                    Method method = c.getDeclaredMethod("set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), c2);
                    method.invoke(object, cellValue);
                }
            }
            list.add(object);
        }
        return list;
    }

    private static Object formatCellValue(Cell cell, FormulaEvaluator evaluator) {
        Object value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case FORMULA:
                    value = evaluator.evaluateFormulaCell(cell);
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        value = cell.getDateCellValue();
                    } else {
                        value = cell.getNumericCellValue();
                    }
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case BLANK:
                    value = "";
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case ERROR:
                    value = String.valueOf(cell.getErrorCellValue());
                    break;
                case _NONE:
                    break;
                default:
                    throw new IllegalArgumentException("UNKNOWN value of type " + cell.getCellType());
            }
        }
        return value;
    }


    /**
     * 根据列名自动识别 java bean 中属性对应 excel 列的下标
     *
     * @param row       标题行
     * @param mapperXml 映射文件
     * @return List<ParseModel>
     * @throws Exception 异常
     */
    private static List<ParseModel> getFieldMapperList(Row row, File mapperXml) throws Exception {
        if (row == null) {
            throw new Exception("excel标题行不能为空");
        }
        List<ParseModel> modelList = ParseMapperXmlUtil.getAssociation(mapperXml);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = cell.getStringCellValue();
                Optional<ParseModel> optional = modelList.stream().filter(parseModel -> parseModel.getColumnName().equals(cellValue)).findFirst();
                ParseModel m;
                if (optional.isPresent()) {
                    m = optional.get();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("列[{}}]对应的属性[{}]", cellValue, m.getFieldName());
                    }
                    m.setIndex(i);
                } else {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("未找到列[{}]对应的属性", cellValue);
                    }
                }
            }
        }
        return modelList;
    }

}
