package com.eu.model;

/**
 * 配置文件实体类
 *
 * @auther fuyangrong
 * @create 2017/01/29
 */
public class Model {
    /**
     * //属性名
     */
    private String fieldName;
    /**
     * 列名
     */
    private String colunmName;
    /**
     * fieldName的java数据类型
     */
    private String javaType;
    /**
     * 验证规则
     */
    private String validate;
    /**
     * 验证提示信息
     */
    private String validateMessage;
    /**
     * colunmName在excel行中的下标
     */
    private Integer index;
    /**
     * 是否忽略
     */
    private boolean enable;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColunmName() {
        return colunmName;
    }

    public void setColunmName(String colunmName) {
        this.colunmName = colunmName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getValidateMessage() {
        return validateMessage;
    }

    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
