/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab6Part2;

import java.sql.Date;

/**
 *
 * @author HP
 */
public class Student {

    private int regId;
    private String name;
    private String address;
    private String parentName;
    private String phone;
    private int standID;
    private Date regDate;

    public Student() {
        
    }

    public Student(int regId, String name, String address, String parentName, String phone, int standID, Date regDate) {
        this.regId = regId;
        this.name = name;
        this.address = address;
        this.parentName = parentName;
        this.phone = phone;
        this.standID = standID;
        this.regDate = regDate;
    }

    public int getRegId() {
        return regId;
    }

    public void setRegId(int regId) {
        this.regId = regId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStandID() {
        return standID;
    }

    public void setStandID(int standID) {
        this.standID = standID;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

}
