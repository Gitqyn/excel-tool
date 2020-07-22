package com.eu.util;

import com.eu.annoation.ColumnName;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 自动生成配置文件工具
 *
 * @author fuyangrong
 * @date 2017/12/1
 */
public class GenerateConfigXmlHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateConfigXmlHelper.class);

    /**
     * 生成配置文件
     *
     * @param c        实体类
     * @param filePath 存放路径
     */
    public static void createConfigXml(Class c, String filePath) {
        // 建立document对象，操作xml
        Document doc = DocumentHelper.createDocument();
        // 设置xml编码
        doc.setXMLEncoding("utf-8");
        // 设置根节点
        Element root = doc.addElement("class");
        // 添加根节点属性
        root.addAttribute("name", c.getName());
        // 获取类所声明的所有属性
        Field[] fields = c.getDeclaredFields();
        // 添加类属性和excel表格列映射的子节点
        for (Field field : fields) {
            ColumnName columnName = field.getDeclaredAnnotation(ColumnName.class);
            if (columnName != null) {
                Element e = root.addElement("property");
                e.addAttribute("name", field.getName());
                e.addAttribute("column", columnName.des());
                e.addAttribute("javaType", field.getType().getName());
                e.addAttribute("enable", "true");
            }
        }
        try {
            OutputFormat xmlFormat = new OutputFormat();
            //换行
            xmlFormat.setNewlines(true);
            //缩进
            xmlFormat.setIndent(true);
            xmlFormat.setIndent("   ");
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filePath + File.separator + c.getSimpleName().toLowerCase() + "-to-excel.xml")), xmlFormat);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("自动生成配置文件成功");
        }
    }

}
