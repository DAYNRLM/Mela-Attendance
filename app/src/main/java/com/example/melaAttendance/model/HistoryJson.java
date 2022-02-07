package com.example.melaAttendance.model;

public class HistoryJson {
    private String payment_ModeR,invoice_No,bill_No,totalAmount,createdDate;
    private int shg_reg_Id,mela_Id;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayment_ModeR() {
        return payment_ModeR;
    }

    public void setPayment_ModeR(String payment_ModeR) {
        this.payment_ModeR = payment_ModeR;
    }

    public String getInvoice_No() {
        return invoice_No;
    }

    public void setInvoice_No(String invoice_No) {
        this.invoice_No = invoice_No;
    }

    public String getBill_No() {
        return bill_No;
    }

    public void setBill_No(String bill_No) {
        this.bill_No = bill_No;
    }

    public int getShg_reg_Id() {
        return shg_reg_Id;
    }

    public void setShg_reg_Id(int shg_reg_Id) {
        this.shg_reg_Id = shg_reg_Id;
    }

    public int getMela_Id() {
        return mela_Id;
    }

    public void setMela_Id(int mela_Id) {
        this.mela_Id = mela_Id;
    }
}
