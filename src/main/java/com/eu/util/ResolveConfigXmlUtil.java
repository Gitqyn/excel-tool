package com.eu.util;

import com.eu.model.Model;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 *
 */
public class ResolveConfigXmlUtil{

    public static Map<String,Object> getAssociation(File file) throws Exception{
        Map<String,Object> map = new HashMap<>();
        List<Model> list = new ArrayList<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        String className = root.attributeValue("name");
        map.put("className",className);

        for (Iterator<Element> it = root.elementIterator("property"); it.hasNext(); ) {
            Element property = it.next();
            Model model = new Model();
            String name = property.attributeValue("name");
            String colum = property.attributeValue("colum");
            String javaType = property.attributeValue("javaType");
            model.setFieldName(name);
            model.setColunmNam(colum);
            model.setJavaType(javaType);
            list.add(model);
        }
        map.put("list",list);

        return map;
    }
}
