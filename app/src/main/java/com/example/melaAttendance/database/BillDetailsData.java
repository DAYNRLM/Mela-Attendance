package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BillDetailsData {

    private Long billId;
    private String billNo;
    private String date;
    private String time;
    private int total_amount;
    private String shg_code;
    @Generated(hash = 1680213092)
    public BillDetailsData(Long billId, String billNo, String date, String time,
                           int total_amount, String shg_code) {
        this.billId = billId;
        this.billNo = billNo;
        this.date = date;
        this.time = time;
        this.total_amount = total_amount;
        this.shg_code = shg_code;
    }
    @Generated(hash = 422616339)
    public BillDetailsData() {
    }
    public Long getBillId() {
        return this.billId;
    }
    public void setBillId(Long billId) {
        this.billId = billId;
    }
    public String getBillNo() {
        return this.billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getTotal_amount() {
        return this.total_amount;
    }
    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
    public String getShg_code() {
        return this.shg_code;
    }
    public void setShg_code(String shg_code) {
        this.shg_code = shg_code;
    }

}
