package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class ShgsDetailsOnLoginData {

    private int shgRegistrationId;
    private String shgCode;
    private String shgName;
    private String loginMobilenumber;
    private int melaId;
    private String stallNo;
    @Keep
    public ShgsDetailsOnLoginData(int shgRegistrationId, String shgCode,
            String shgName, String loginMobilenumber, int melaId) {
        this.shgRegistrationId = shgRegistrationId;
        this.shgCode = shgCode;
        this.shgName = shgName;
        this.loginMobilenumber = loginMobilenumber;
        this.melaId = melaId;
    }
    @Generated(hash = 189089314)
    public ShgsDetailsOnLoginData() {
    }
    @Generated(hash = 1044520063)
    public ShgsDetailsOnLoginData(int shgRegistrationId, String shgCode,
            String shgName, String loginMobilenumber, int melaId, String stallNo) {
        this.shgRegistrationId = shgRegistrationId;
        this.shgCode = shgCode;
        this.shgName = shgName;
        this.loginMobilenumber = loginMobilenumber;
        this.melaId = melaId;
        this.stallNo = stallNo;
    }
    public int getShgRegistrationId() {
        return this.shgRegistrationId;
    }
    public void setShgRegistrationId(int shgRegistrationId) {
        this.shgRegistrationId = shgRegistrationId;
    }
    public String getShgCode() {
        return this.shgCode;
    }
    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }
    public String getShgName() {
        return this.shgName;
    }
    public void setShgName(String shgName) {
        this.shgName = shgName;
    }
    public String getLoginMobilenumber() {
        return this.loginMobilenumber;
    }
    public void setLoginMobilenumber(String loginMobilenumber) {
        this.loginMobilenumber = loginMobilenumber;
    }
    public int getMelaId() {
        return this.melaId;
    }
    public void setMelaId(int melaId) {
        this.melaId = melaId;
    }
    public String getStallNo() {
        return this.stallNo;
    }
    public void setStallNo(String stallNo) {
        this.stallNo = stallNo;
    }
}
