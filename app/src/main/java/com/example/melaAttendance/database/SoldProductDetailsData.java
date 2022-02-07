package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SoldProductDetailsData {
    @Id(autoincrement = true)
    private Long product_id;
    private String product_name;
    private String getProduct_quantity_sold;
    private int unit_price;
    private int total_amount;
    private String bill_invoice;
    private String sold_product_id;
    @Generated(hash = 923856773)
    public SoldProductDetailsData(Long product_id, String product_name, String getProduct_quantity_sold,
            int unit_price, int total_amount, String bill_invoice, String sold_product_id) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.getProduct_quantity_sold = getProduct_quantity_sold;
        this.unit_price = unit_price;
        this.total_amount = total_amount;
        this.bill_invoice = bill_invoice;
        this.sold_product_id = sold_product_id;
    }
    @Generated(hash = 576226230)
    public SoldProductDetailsData() {
    }
    public Long getProduct_id() {
        return this.product_id;
    }
    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return this.product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getGetProduct_quantity_sold() {
        return this.getProduct_quantity_sold;
    }
    public void setGetProduct_quantity_sold(String getProduct_quantity_sold) {
        this.getProduct_quantity_sold = getProduct_quantity_sold;
    }
    public int getUnit_price() {
        return this.unit_price;
    }
    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }
    public int getTotal_amount() {
        return this.total_amount;
    }
    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
    public String getBill_invoice() {
        return this.bill_invoice;
    }
    public void setBill_invoice(String bill_invoice) {
        this.bill_invoice = bill_invoice;
    }
    public String getSold_product_id() {
        return this.sold_product_id;
    }
    public void setSold_product_id(String sold_product_id) {
        this.sold_product_id = sold_product_id;
    }
}
