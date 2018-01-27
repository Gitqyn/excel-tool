package com.eu.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

import static org.apache.poi.ss.usermodel.Cell.*;


/**
 * excel导入工具类
 *
 * @auther fuyangrong
 * @create 2017/12/4
 */
public class ExcelImportUtil {


    private static HSSFWorkbook readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        return new HSSFWorkbook(fis);

    }

    public void convertToClass(String fileName) throws IOException {

        HSSFWorkbook wb = ExcelImportUtil.readFile(fileName);

        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            HSSFSheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows
                    + " row(s).");
            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                System.out.println("\nROW " + row.getRowNum() + " has " + row.getPhysicalNumberOfCells() + " cell(s).");
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    HSSFCell cell = row.getCell(c);
                    formatCellValue(cell);

                }
            }
        }
    }

    @Deprecated
    private static String formatCellValue(Cell cell){
        String value ="";
        if (cell != null) {
            switch (cell.getCellType()) {
                case CELL_TYPE_FORMULA:
                    value = cell.getCellFormula();
                    break;

                case CELL_TYPE_NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;

                case CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;

                case CELL_TYPE_BLANK:
                    value = "";
                    break;

                case CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;

                case CELL_TYPE_ERROR:
                    value = String.valueOf(cell.getErrorCellValue());
                    break;

                default:
                    value = "UNKNOWN value of type " + cell.getCellType();
            }
        }
        return value;
    }

}
