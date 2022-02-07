package com.example.melaAttendance.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShgDetailsData {

    private String shg_code;
    private String user_name;
    private int shg_reg_id;
    private String shg_main_participant;
    private String shg_reg_code;
    private int mela_id;
    private String shggst;
    private String entity_code;
    private int user_id;
    private String shg_name;
    private String main_participant_mobile;
    private String participant_status;
    private String stallNo;
    @Generated(hash = 1740172118)
    public ShgDetailsData(String shg_code, String user_name, int shg_reg_id,
            String shg_main_participant, String shg_reg_code, int mela_id,
            String shggst, String entity_code, int user_id, String shg_name,
            String main_participant_mobile, String participant_status,
            String stallNo) {
        this.shg_code = shg_code;
        this.user_name = user_name;
        this.shg_reg_id = shg_reg_id;
        this.shg_main_participant = shg_main_participant;
        this.shg_reg_code = shg_reg_code;
        this.mela_id = mela_id;
        this.shggst = shggst;
        this.entity_code = entity_code;
        this.user_id = user_id;
        this.shg_name = shg_name;
        this.main_participant_mobile = main_participant_mobile;
        this.participant_status = participant_status;
        this.stallNo = stallNo;
    }
    @Generated(hash = 879931565)
    public ShgDetailsData() {
    }
    public String getShg_code() {
        return this.shg_code;
    }
    public void setShg_code(String shg_code) {
        this.shg_code = shg_code;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public int getShg_reg_id() {
        return this.shg_reg_id;
    }
    public void setShg_reg_id(int shg_reg_id) {
        this.shg_reg_id = shg_reg_id;
    }
    public String getShg_main_participant() {
        return this.shg_main_participant;
    }
    public void setShg_main_participant(String shg_main_participant) {
        this.shg_main_participant = shg_main_participant;
    }
    public String getShg_reg_code() {
        return this.shg_reg_code;
    }
    public void setShg_reg_code(String shg_reg_code) {
        this.shg_reg_code = shg_reg_code;
    }
    public int getMela_id() {
        return this.mela_id;
    }
    public void setMela_id(int mela_id) {
        this.mela_id = mela_id;
    }
    public String getShggst() {
        return this.shggst;
    }
    public void setShggst(String shggst) {
        this.shggst = shggst;
    }
    public String getEntity_code() {
        return this.entity_code;
    }
    public void setEntity_code(String entity_code) {
        this.entity_code = entity_code;
    }
    public int getUser_id() {
        return this.user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getShg_name() {
        return this.shg_name;
    }
    public void setShg_name(String shg_name) {
        this.shg_name = shg_name;
    }
    public String getMain_participant_mobile() {
        return this.main_participant_mobile;
    }
    public void setMain_participant_mobile(String main_participant_mobile) {
        this.main_participant_mobile = main_participant_mobile;
    }
    public String getParticipant_status() {
        return this.participant_status;
    }
    public void setParticipant_status(String participant_status) {
        this.participant_status = participant_status;
    }
    public String getStallNo() {
        return this.stallNo;
    }
    public void setStallNo(String stallNo) {
        this.stallNo = stallNo;
    }

}
