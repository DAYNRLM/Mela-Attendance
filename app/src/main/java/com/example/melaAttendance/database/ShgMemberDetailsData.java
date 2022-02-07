package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShgMemberDetailsData {

    private String category_name;
    private String shg_member_code;
    private String ass_name;
    private String mobile;
    private String subcategory_name;
    private String member_name;
    private String product_name;
    private int subcategory_id;
    private String district_name;
    private String block_name;
    private String ass_mobile;
    private int category_id;
    private String state_name;
    private int product_id;
    private String village_name;
    private String grampanchayat_name;
    String avialableQuantity;
    @Generated(hash = 1640836307)
    public ShgMemberDetailsData(String category_name, String shg_member_code,
            String ass_name, String mobile, String subcategory_name,
            String member_name, String product_name, int subcategory_id,
            String district_name, String block_name, String ass_mobile,
            int category_id, String state_name, int product_id, String village_name,
            String grampanchayat_name, String avialableQuantity) {
        this.category_name = category_name;
        this.shg_member_code = shg_member_code;
        this.ass_name = ass_name;
        this.mobile = mobile;
        this.subcategory_name = subcategory_name;
        this.member_name = member_name;
        this.product_name = product_name;
        this.subcategory_id = subcategory_id;
        this.district_name = district_name;
        this.block_name = block_name;
        this.ass_mobile = ass_mobile;
        this.category_id = category_id;
        this.state_name = state_name;
        this.product_id = product_id;
        this.village_name = village_name;
        this.grampanchayat_name = grampanchayat_name;
        this.avialableQuantity = avialableQuantity;
    }
    @Generated(hash = 361685632)
    public ShgMemberDetailsData() {
    }
    public String getCategory_name() {
        return this.category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public String getShg_member_code() {
        return this.shg_member_code;
    }
    public void setShg_member_code(String shg_member_code) {
        this.shg_member_code = shg_member_code;
    }
    public String getAss_name() {
        return this.ass_name;
    }
    public void setAss_name(String ass_name) {
        this.ass_name = ass_name;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getSubcategory_name() {
        return this.subcategory_name;
    }
    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }
    public String getMember_name() {
        return this.member_name;
    }
    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
    public String getProduct_name() {
        return this.product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public int getSubcategory_id() {
        return this.subcategory_id;
    }
    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }
    public String getDistrict_name() {
        return this.district_name;
    }
    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
    public String getBlock_name() {
        return this.block_name;
    }
    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }
    public String getAss_mobile() {
        return this.ass_mobile;
    }
    public void setAss_mobile(String ass_mobile) {
        this.ass_mobile = ass_mobile;
    }
    public int getCategory_id() {
        return this.category_id;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public String getState_name() {
        return this.state_name;
    }
    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
    public int getProduct_id() {
        return this.product_id;
    }
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
    public String getVillage_name() {
        return this.village_name;
    }
    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }
    public String getGrampanchayat_name() {
        return this.grampanchayat_name;
    }
    public void setGrampanchayat_name(String grampanchayat_name) {
        this.grampanchayat_name = grampanchayat_name;
    }
    public String getAvialableQuantity() {
        return this.avialableQuantity;
    }
    public void setAvialableQuantity(String avialableQuantity) {
        this.avialableQuantity = avialableQuantity;
    }
}
