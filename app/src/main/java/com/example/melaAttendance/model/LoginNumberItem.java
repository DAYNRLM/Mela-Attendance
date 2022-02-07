package com.example.melaAttendance.model;

public class LoginNumberItem {

    private int shgRegistrationId;
    private String shgCode;
    private String shgName;
    private int loginMobilenumber;
    private int melaId;

    public int getShgRegistrationId() {
        return shgRegistrationId;
    }

    public void setShgRegistrationId(int shgRegistrationId) {
        this.shgRegistrationId = shgRegistrationId;
    }

    public String getShgCode() {
        return shgCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }

    public String getShgName() {
        return shgName;
    }

    public void setShgName(String shgName) {
        this.shgName = shgName;
    }

    public int getLoginMobilenumber() {
        return loginMobilenumber;
    }

    public void setLoginMobilenumber(int loginMobilenumber) {
        this.loginMobilenumber = loginMobilenumber;
    }

    public int getMelaId() {
        return melaId;
    }

    public void setMelaId(int melaId) {
        this.melaId = melaId;
    }
}
