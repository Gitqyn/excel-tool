package com.eu.util;

import com.eu.model.Model;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * 解析配置文件工具类
 * @auther fuyangrong
 * @create 2018/01/29
 */
public class ResolveConfigXmlUtil {

    /**
     * 解析配置文件
     * @param file 配置文件
     * @return Map<String,Object> 包含className和list<Model>
     * @throws Exception
     */
    public static Map<String, Object> getAssociation(File file) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Model> list = new ArrayList<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        String className = root.attributeValue("name");
        map.put("className", className);

        for (Iterator<Element> it = root.elementIterator("property"); it.hasNext(); ) {
            Element property = it.next();
            Model model = new Model();
            String name = property.attributeValue("name");
            String colum = property.attributeValue("colum");
            String javaType = property.attributeValue("javaType");
            model.setFieldName(name);
            model.setColunmName(colum);
            model.setJavaType(javaType);
            list.add(model);
        }
        map.put("list", list);

        return map;
    }
}
