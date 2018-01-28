package com.eu.model;

public class Model {

    private String fieldName;

    private String colunmNam;

    private String javaType;

    private String validate;

    private String validateMessage;

    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColunmNam() {
        return colunmNam;
    }

    public void setColunmNam(String colunmNam) {
        this.colunmNam = colunmNam;
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

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
