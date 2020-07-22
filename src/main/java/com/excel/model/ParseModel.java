package com.excel.model;

/**
 * 配置文件实体类
 *
 * @author  fuyangrong
 * @date  2017/01/29
 */
public class ParseModel {
    /**
     * 属性名
     */
    private String fieldName;

    /**
     * 列名
     */
    private String columnName;

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
     * columnName在excel行中的下标
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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    @Override
    public String toString() {
        return "Model{" +
                "fieldName='" + fieldName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", javaType='" + javaType + '\'' +
                ", validate='" + validate + '\'' +
                ", validateMessage='" + validateMessage + '\'' +
                ", index=" + index +
                ", enable=" + enable +
                '}';
    }
}
