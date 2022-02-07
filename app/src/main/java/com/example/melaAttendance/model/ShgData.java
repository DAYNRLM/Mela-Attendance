package com.example.melaAttendance.model;

import java.io.Serializable;

public class ShgData implements Serializable {


    private static ShgData shgData=null;
    private String shgRegId;
    private String shgCode;
    private String shgName;
    private CrpData crpData;
    private ScData scData;
    private MpData mpData;
    private HelperData helperData;

    public static synchronized ShgData getInstance(){
        if (shgData==null){
            shgData=new ShgData();
        }
        return shgData;
    }

    public String getShgRegId() {
        return shgRegId;
    }

    public void setShgRegId(String shgRegId) {
        this.shgRegId = shgRegId;
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

    public CrpData getCrpData() {
        return crpData;
    }

    public void setCrpData(CrpData crpData) {
        this.crpData = crpData;
    }

    public ScData getScData() {
        return scData;
    }

    public void setScData(ScData scData) {
        this.scData = scData;
    }

    public MpData getMpData() {
        return mpData;
    }

    public void setMpData(MpData mpData) {
        this.mpData = mpData;
    }

    public HelperData getHelperData() {
        return helperData;
    }

    public void setHelperData(HelperData helperData) {
        this.helperData = helperData;
    }

    public static class CrpData{

        private int closing;
        private int opening;
        private String userName;
        private String mobile;
        private String participantStatus;
        private String dataStatus;

        public int getClosing() {
            return closing;
        }

        public void setClosing(int closing) {
            this.closing = closing;
        }

        public int getOpening() {
            return opening;
        }

        public void setOpening(int opening) {
            this.opening = opening;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getParticipantStatus() {
            return participantStatus;
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus = participantStatus;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }
    }
    public static class ScData{
        private int closing;
        private int opening;
        private String userName;
        private String mobile;
        private String participantStatus;
        private String dataStatus;

        public int getClosing() {
            return closing;
        }

        public void setClosing(int closing) {
            this.closing = closing;
        }

        public int getOpening() {
            return opening;
        }

        public void setOpening(int opening) {
            this.opening = opening;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }



        public String getParticipantStatus() {
            return participantStatus;
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus = participantStatus;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
    public static class MpData{
        private int closing;
        private int opening;
        private String userName;
        private String mobile;
        private String participantStatus;
        private String dataStatus;

        public int getClosing() {
            return closing;
        }

        public void setClosing(int closing) {
            this.closing = closing;
        }

        public int getOpening() {
            return opening;
        }

        public void setOpening(int opening) {
            this.opening = opening;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }


        public String getParticipantStatus() {
            return participantStatus;
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus = participantStatus;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
    public static class HelperData{
        private int closing;
        private int opening;
        private String userName;
        private String mobile;
        private String participantStatus;
        private String dataStatus;

        public int getClosing() {
            return closing;
        }

        public void setClosing(int closing) {
            this.closing = closing;
        }

        public int getOpening() {
            return opening;
        }

        public void setOpening(int opening) {
            this.opening = opening;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getParticipantStatus() {
            return participantStatus;
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus = participantStatus;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
