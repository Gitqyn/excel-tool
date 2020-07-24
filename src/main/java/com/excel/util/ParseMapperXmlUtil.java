package com.excel.util;

import com.excel.model.ParseModel;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 解析配置文件工具类
 *
 * @author fuyangrong
 * @date 2018/01/29
 */
public class ParseMapperXmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseMapperXmlUtil.class);

    /**
     * 解析配置文件
     *
     * @param mapper 配置文件
     * @return List<ParseModel>
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public static List<ParseModel> getAssociation(File mapper) throws Exception {
        List<ParseModel> list = new ArrayList<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(mapper);
        Element root = document.getRootElement();
        String className = root.attributeValue("name");
        for (Iterator<Element> it = root.elementIterator("property"); it.hasNext(); ) {
            Element property = it.next();
            ParseModel parseModel = new ParseModel();
            String name = property.attributeValue("name");
            String column = property.attributeValue("column");
            String javaType = property.attributeValue("javaType");
            String enable = property.attributeValue("enable");
            String index = property.attributeValue("index");
            String validate = property.attributeValue("validate");
            String validateMessage = property.attributeValue("validateMessage");
            boolean isEnable = enable.isEmpty() || Boolean.parseBoolean(enable);
            if (isEnable) {
                parseModel.setClassName(className);
                parseModel.setFieldName(name);
                parseModel.setJavaType(javaType);
                parseModel.setColumnName(column);
                if (!StringUtil.isEmpty(index)) {
                    parseModel.setIndex(Integer.parseInt(index));
                }
                parseModel.setValidate(validate);
                parseModel.setValidateMessage(validateMessage);
                list.add(parseModel);
            }
        }
        return list;
    }
}
