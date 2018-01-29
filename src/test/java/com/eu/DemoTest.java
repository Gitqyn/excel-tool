package com.eu;

import com.eu.util.ExcelImportUtil;
import com.eu.util.GenerateConfigXmlHelper;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * 测试类
 * @auther fuyangrong
 * @create 2018/01/29
 */
public class DemoTest {

    /**
     * 生成配置文件
     */
    @Test
    public void createConfigXMLTest(){
        GenerateConfigXmlHelper.createConfigXML(Demo.class,System.getProperty("user.dir") +File.separator+ "src/test/resource");

    }

    /**
     * 导入excel
     */
    @Test
    public void importExcel(){
        long start = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        File file= new File(System.getProperty("user.dir") +"/doc/demo.xlsx");
        File configXml = new File(System.getProperty("user.dir") +File.separator+ "src/test/resource/"+"demo-to-excel.xml");
        List<Demo> list = null;
        try{
            list = ExcelImportUtil.convertToClass(file,configXml);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("总数："+list.size());
        System.out.println(list.toString());
        long end = System.currentTimeMillis();
        System.out.println("总耗时："+(end-start));

    }






}
