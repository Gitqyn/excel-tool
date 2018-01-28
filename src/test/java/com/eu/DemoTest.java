package com.eu;

import com.eu.model.Demo;
import com.eu.util.ExcelImportUtil;
import com.eu.util.GenerateConfigXmlHelper;
import org.junit.Test;

import java.io.File;
import java.util.List;


public class DemoTest {


    @Test
    public void importExcel(){
        File file= new File("F:"+File.separator+"demo.xlsx");
        File configXml = new File(System.getProperty("user.dir") +File.separator+ "src/main/resource/"+"demo-to-excel.xml");
        List<Demo> list = null;
        try{
            list = ExcelImportUtil.convertToClass(file,configXml);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(list.toString());

    }

    @Test
    public void createConfigXMLTest(){
        GenerateConfigXmlHelper.createConfigXML(Demo.class,System.getProperty("user.dir") +File.separator+ "src/main/resource");

    }






}
