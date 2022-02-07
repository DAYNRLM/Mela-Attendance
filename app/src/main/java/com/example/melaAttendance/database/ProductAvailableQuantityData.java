package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ProductAvailableQuantityData {


    @Id(autoincrement = true)
    Long auto_gen_available_quantity;
    String product_id;
    String product_name;
    String available_quantity;
    @Generated(hash = 1130817709)
    public ProductAvailableQuantityData(Long auto_gen_available_quantity,
            String product_id, String product_name, String available_quantity) {
        this.auto_gen_available_quantity = auto_gen_available_quantity;
        this.product_id = product_id;
        this.product_name = product_name;
        this.available_quantity = available_quantity;
    }
    @Generated(hash = 278635590)
    public ProductAvailableQuantityData() {
    }
    public String getProduct_id() {
        return this.product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return this.product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getAvailable_quantity() {
        return this.available_quantity;
    }
    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }
    public Long getAuto_gen_available_quantity() {
        return this.auto_gen_available_quantity;
    }
    public void setAuto_gen_available_quantity(Long auto_gen_available_quantity) {
        this.auto_gen_available_quantity = auto_gen_available_quantity;
    }
}
