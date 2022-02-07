package com.example.melaAttendance.model;

public class ProductPriceList implements java.io.Serializable{
    String productName;
    String quantity;
    String unitPrice;
    int amount;
    String product_id;
    String final_bill_no;

    public String getFinal_bill_no() {
        return final_bill_no;
    }

    public void setFinal_bill_no(String final_bill_no) {
        this.final_bill_no = final_bill_no;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
