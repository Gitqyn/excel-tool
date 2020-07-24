package com.excel;

import com.excel.util.ExcelImportUtil;
import com.excel.util.GenerateMapperXmlHelper;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author fyr
 * @date 2020-07-24 024
 */
public class ImportTest {

    @Test
    public void generateMapperXmlTest() throws Exception {
        // 生成映射文件
        GenerateMapperXmlHelper.createMapperXml(Demo.class, "D:\\IdeaProjects\\excel-tool\\src\\test\\resources");
    }

    @Test
    public void importTest() throws Exception {
        List<Object> objects = ExcelImportUtil.parseFile(new File("D:\\IdeaProjects\\excel-tool\\src\\test\\resources\\demo.xlsx"), new File("D:\\IdeaProjects\\excel-tool\\src\\test\\resources\\demo-to-excel.xml"));
    }
}
