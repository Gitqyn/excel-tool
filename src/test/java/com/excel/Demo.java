package com.excel;

/**
 * @author fyr
 * @date 2020-07-24 024
 */
public class Demo {

    private String name;

    private String deptName;

    private Integer age;

    private Integer num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "name='" + name + '\'' +
                ", deptName='" + deptName + '\'' +
                ", age=" + age +
                ", num=" + num +
                '}';
    }
}
