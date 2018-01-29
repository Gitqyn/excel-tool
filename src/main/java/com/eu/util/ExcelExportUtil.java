package com.eu.util;

import com.eu.model.Model;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel导出工具类
 * @auther fuyangrong
 * @create 2017/01/29
 */
public class ExcelExportUtil {


    /**
     * 导出Excel
     * @param list  导出数据
     * @param configXml 配置文件
     * @param sheetName sheet名称
     * @param bookName 工作薄名称
     * @throws Exception
     */
    public static void exportExcel(List<?> list, File configXml, String sheetName, String bookName) throws Exception {
        Map<String, Object> map = ResolveConfigXmlUtil.getAssociation(configXml);
        String className = map.get("className").toString();
        Class c = Class.forName(className);
        List<Model> models = (List<Model>) map.get("list");
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(com.eu.util.StringUtil.isEmpty(sheetName) ? "sheet1" : sheetName);

        Map<String, Integer> rootMap = new HashMap<>();
        //表头
        Row rootRow = sheet.createRow(0);
        int index = 0;
        for (Model m : models) {
            String columName = m.getColunmName();
            rootRow.createCell(index).setCellValue(columName);
            rootMap.put(columName, index);
            index++;
        }
        int rowNum = 1;
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Row row = sheet.createRow(rowNum);
                for (Model m : models) {
                    String fieldName = m.getFieldName();
                    String columName = m.getColunmName();
                    String javaType = m.getJavaType();
                    Class cl = Class.forName(javaType);
                    int num = rootMap.get(columName);
                    Method method = c.getDeclaredMethod("get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), null);
                    String cellValue = String.valueOf(method.invoke(obj, null));
                    row.createCell(num).setCellValue(cellValue);
                }
                rowNum++;
            }
        }

        FileOutputStream fileOut = new FileOutputStream(com.eu.util.StringUtil.isEmpty(bookName) ? "workbook.xlsx" : bookName+".xlsx");
        wb.write(fileOut);
        fileOut.close();

    }

}
