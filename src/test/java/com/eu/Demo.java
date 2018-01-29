package com.eu;

import com.eu.annoation.ColumName;

/**
 * demo
 *
 * @auther fuyangrong
 * @create 2017/12/1
 */
public class Demo {

    @ColumName(des="id")
    private Integer id;

    @ColumName(des="用户名")
    private String userName;

    @ColumName(des="部门")
    private String department;

    @ColumName(des="编号")
    private String userCode;

    @ColumName(des="年龄")
    private Integer age;

    public Demo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", department='" + department + '\'' +
                ", userCode='" + userCode + '\'' +
                ", age=" + age +
                '}';
    }
}
