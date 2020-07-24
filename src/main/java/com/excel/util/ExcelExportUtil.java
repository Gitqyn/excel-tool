package com.excel.util;

import com.excel.model.ParseModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel导出工具类
 *
 * @author fuyangrong
 * @date 2017/01/29
 */
public class ExcelExportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtil.class);


    /**
     * 导出Excel
     *
     * @param list      导出数据
     * @param mapperXml 配置文件
     * @param sheetName sheet名称
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public static Workbook exportExcel(List<?> list, File mapperXml, String sheetName) throws Exception {
        List<ParseModel> parseModelList = ParseMapperXmlUtil.getAssociation(mapperXml);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(com.excel.util.StringUtil.isEmpty(sheetName) ? "sheet1" : sheetName);
        Map<String, Integer> rootMap = new HashMap<>(parseModelList.size());
        //表头
        Row rootRow = sheet.createRow(0);
        int index = 0;
        for (ParseModel m : parseModelList) {
            String columnName = m.getColumnName();
            Cell cell = rootRow.createCell(index);
            cell.setCellValue(columnName);
            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(style);
            Font font = wb.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            rootMap.put(columnName, index);
            index++;
        }
        int rowNum = 1;
        if (list != null && list.size() > 0) {
            for (Object o : list) {
                Row row = sheet.createRow(rowNum);
                for (ParseModel m : parseModelList) {
                    String fieldName = m.getFieldName();
                    String columName = m.getColumnName();
                    String javaType = m.getJavaType();
                    int num = rootMap.get(columName);
                    Class realClass = o.getClass();
                    Method method = realClass.getDeclaredMethod("get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1));
                    String cellValue;
                    if (Date.class.getName().equals(javaType)) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = dateFormat.format(method.invoke(o));
                    } else {
                        cellValue = String.valueOf(method.invoke(o));
                    }
                    Cell cell = row.createCell(num);
                    cell.setCellValue(cellValue);

                }
                rowNum++;
            }
            adjustColumnSize(sheet, parseModelList.size());
        }
        return wb;
    }

    /**
     * 导出Excel
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param list      导出数据
     * @param mapperXml 配置文件
     * @param sheetName sheet名称
     * @param bookName  导出文件名
     * @throws Exception 异常
     */
    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, List<?> list, File mapperXml, String sheetName, String bookName) throws Exception {
        Workbook workbook = exportExcel(list, mapperXml, sheetName);
        OutputStream output = null;
        try {
            String userAgent = request.getHeader("User-Agent");
            response.setHeader("Content-disposition", "attachment; filename=" + StringUtil.encodeFileName(bookName, userAgent));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            output = response.getOutputStream();
            workbook.write(output);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 自动调整列宽
     *
     * @param sheet     表格
     * @param columnNum 字段下标
     */
    private static void adjustColumnSize(Sheet sheet, int columnNum) {
        for (int i = 0; i < columnNum; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
