package com.excel.model;

import java.util.Objects;

/**
 * 配置文件实体类
 *
 * @author  fuyangrong
 * @date  2017/01/29
 */
public class ParseModel {

    /**
     * 类名
     */
    private String className;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseModel that = (ParseModel) o;
        return enable == that.enable &&
                Objects.equals(className, that.className) &&
                Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(columnName, that.columnName) &&
                Objects.equals(javaType, that.javaType) &&
                Objects.equals(validate, that.validate) &&
                Objects.equals(validateMessage, that.validateMessage) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, fieldName, columnName, javaType, validate, validateMessage, index, enable);
    }

    @Override
    public String toString() {
        return "ParseModel{" +
                "className='" + className + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", javaType='" + javaType + '\'' +
                ", validate='" + validate + '\'' +
                ", validateMessage='" + validateMessage + '\'' +
                ", index=" + index +
                ", enable=" + enable +
                '}';
    }
}
