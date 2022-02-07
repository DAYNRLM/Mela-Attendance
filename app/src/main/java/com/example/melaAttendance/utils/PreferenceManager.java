package com.example.melaAttendance.utils;

public class PreferenceManager  {
    public static ProjectPrefrences preferenceManager=null;
    private static final String PREF_KEY_LOGIN_DONE = "Longin";
    private static final String PREF_KEY_IN_TIME = "inTime";
    private static final String PREF_KEY_DATE = "inDate";
    private static final String PREF_KEY_OUT_TIME = "outTime";
    private static final String PREF_KEY_IN_ADDRESS = "inAddress";
    private static final String PREF_KEY_OUT_ADDRESS = "outAddress";
    private static final String PREF_KEY_SET_FLAG_FOR_IN = "inFLAG";
    private static final String PREF_KEY_SET_FLAG_FOR_OUT= "outFLAG";
    private static final String PREF_KEY_SHG_CODE= "shgCode";
    private static final String PREF_KEY_SHG_NAME= "shgName";


    //Imei and device Info keys
    private static final String PREF_KEY_IMEI="imei";
    private static final String PREF_KEY_DEVICE_INFO="deviceInfo";


    private static final String PREF_KEY_NEXT_DAY_DATE= "nextDayDate";
    private static final String PREF_KEY_SHG_REG_ID= "prfShgRegID";
    private static final String PREF_KEY_MPIN= "prfMPIN";
    private static final String PREF_PHONENO_FOR_MPIN="ph_no_mpin";
    private static final String PARTICIPANT_MOBILE="pm";

    public static String getParticipantMobile() {
        return PARTICIPANT_MOBILE;
    }

    private  static final String PREF_SHG_NAME_FOR_PROFILE="shgProfileName";
    private static final String  PREF_SHG_REGISTRATION_CODE_FOR_PROFILE="regcode";
    private static final String PREF_KEY_SHG_MOBILE= "shgMobile";
    //-----

    private static final String PREF_KEY_SHG_TOTAL_AMOUNT= "generatedAmount";


    private static final String SHG_REGISTRATION_ID="shgRegId";
    private static final String DATE_FOR_SET_ATTENDANCE="setDateForAtt";
    // bundle data
    private static final String BUNDLE_USERNAME="userName";
    private static final String BUNDLE_FLAG_STATUS="flag";
    private static final String BUNDLE_AUTO_GENERATEDID="autoID";
    private static final String BUNDLE_PARTICIPANT_STATUS_FOR_SET_ATTENDANCE="participantStatus";
    private static final String BUNDLE_PARTICIPANT_SHGREGID_FOR_SET_ATTENDANCE="bundleParticipantShgRegId";
    private static final String BUNDLE_PARTICIPANT_MELAID_FOR_SET_ATTENDANCE="bundleParticipantMelaID";

    //key for get attedence detail from server
    private static final String BUNDLE_ATTEDENCE_OPENING_LOCATION="openingLocation";
    private static final String BUNDLE_ATTEDENCE_OPENING_TIME="openingTime";
    private static final String BUNDLE_ATTEDENCE_LAST_CLOSING_TIME="lastClosingTime";
    private static final String BUNDLE_ATTEDENCE_CLOSING_LOCATION="closingLocation";
    private static final String BUNDLE_ATTEDENCE_STATUS="attedenceStatus";
    private static final String BUNDLE_ATTEDENCE_MOBILE_NUMBER="attedenceMobile";
    private  static final String PRODUCT_ID_FOR_BARCODE="pdctIdBarcode";
    private static final String OTP_MOBILE_NUMBER="otpmobilenumber";
    private static final String OTP="otp";

    public static String getOTP() {
        return OTP;
    }
      public static String getOtpMobileNumber() {
        return OTP_MOBILE_NUMBER;
    }



    public static String getPrefPhonenoForMpin() {
        return PREF_PHONENO_FOR_MPIN;
    }

    public static String getPrefKeyImei() {
        return PREF_KEY_IMEI;
    }

    public static String getPrefKeyDeviceInfo() {
        return PREF_KEY_DEVICE_INFO;
    }

    public static String getProductIdForBarcode() {
        return PRODUCT_ID_FOR_BARCODE;
    }


    public static String getPrefKeyMpin() {
        return PREF_KEY_MPIN;
    }

    public static String getBundleAttedenceMobileNumber() {
        return BUNDLE_ATTEDENCE_MOBILE_NUMBER;
    }

    public static String getBundleAttedenceClosingLocation() {
        return BUNDLE_ATTEDENCE_CLOSING_LOCATION;
    }

    public static String getBundleAttedenceOpeningLocation() {
        return BUNDLE_ATTEDENCE_OPENING_LOCATION;
    }

    public static String getBundleAttedenceLastClosingTime() {
        return BUNDLE_ATTEDENCE_LAST_CLOSING_TIME;
    }

    public static String getBundleAttedenceOpeningTime() {
        return BUNDLE_ATTEDENCE_OPENING_TIME;
    }

    public static String getBundleAttedenceStatus() {
        return BUNDLE_ATTEDENCE_STATUS;
    }

    public static String getPrefKeyShgTotalAmount() {
        return PREF_KEY_SHG_TOTAL_AMOUNT;
    }

    public static String getPrefShgNameForProfile() {
        return PREF_SHG_NAME_FOR_PROFILE;
    }
    public static String getPrefShgRegistrationCodeForProfile() {
        return PREF_SHG_REGISTRATION_CODE_FOR_PROFILE;
    }
    public static String getBundleParticipantShgregidForSetAttendance() {
        return BUNDLE_PARTICIPANT_SHGREGID_FOR_SET_ATTENDANCE;
    }

    public static String getBundleParticipantMelaidForSetAttendance() {
        return BUNDLE_PARTICIPANT_MELAID_FOR_SET_ATTENDANCE;
    }

    public static String getBundleParticipantStatusForSetAttendance() {
        return BUNDLE_PARTICIPANT_STATUS_FOR_SET_ATTENDANCE;
    }

    public static String getPrefKeyShgRegId() {
        return PREF_KEY_SHG_REG_ID;
    }

    public static String getPrefKeyNextDayDate() {
        return PREF_KEY_NEXT_DAY_DATE;
    }

    public static String getDateForSetAttendance() {
        return DATE_FOR_SET_ATTENDANCE;
    }

    public static String getBundleUsername() {
        return BUNDLE_USERNAME;
    }

    public static String getBundleFlagStatus() {
        return BUNDLE_FLAG_STATUS;
    }

    public static String getBundleAutoGeneratedid() {
        return BUNDLE_AUTO_GENERATEDID;
    }

    public static String getShgRegistrationId() {
        return SHG_REGISTRATION_ID;
    }

    public static String getPrefKeyShgName() {
        return PREF_KEY_SHG_NAME;
    }

    public static String getPrefKeyShgMobile() {
        return PREF_KEY_SHG_MOBILE;
    }

    public static String getPrefKeyShgId() {
        return PREF_KEY_SHG_ID;
    }

    private static final String PREF_KEY_SHG_ID= "shgId";

    public static String getPrefKeySetFlagForIn() {
        return PREF_KEY_SET_FLAG_FOR_IN;
    }

    public static String getPrefKeySetFlagForOut() {
        return PREF_KEY_SET_FLAG_FOR_OUT;
    }

    public static String getPrefKeyShgCode() {
        return PREF_KEY_SHG_CODE;
    }

    public static ProjectPrefrences getInstance(){

        if(preferenceManager==null)
            preferenceManager=new ProjectPrefrences();
        return preferenceManager;
    }

    public static String getPrefKeyLoginDone() {
        return PREF_KEY_LOGIN_DONE;
    }

    public static ProjectPrefrences getPreferenceManager() {
        return preferenceManager;
    }

    public static void setPreferenceManager(ProjectPrefrences preferenceManager) {
        PreferenceManager.preferenceManager = preferenceManager;
    }

    public static String getPrefKeyInTime() {
        return PREF_KEY_IN_TIME;
    }

    public static String getPrefKeyOutTime() {
        return PREF_KEY_OUT_TIME;
    }

    public static String getPrefKeyInAddress() {
        return PREF_KEY_IN_ADDRESS;
    }

    public static String getPrefKeyOutAddress() {
        return PREF_KEY_OUT_ADDRESS;
    }

    public static String getPrefKeyDate() {
        return PREF_KEY_DATE;
    }
}
