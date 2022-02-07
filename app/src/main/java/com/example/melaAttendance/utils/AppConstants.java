package com.example.melaAttendance.utils;

public class AppConstants {
    public static final String quantity[]={"1","2","3","4","5","6","7","8","9","10","11"};
    public static final String BILL_GENERATE_URL = "https://nrlm.gov.in/nrlmwebservice/services/mela/bill?melaId=1&shgRegId=3&productId=1,2,4&productPrice=150,90,20&productQty=2,1,5";
    public static final String ATTENDENCE_URL = "https://nrlm.gov.in/nrlmwebservice/services/melaattandences/attendence";
    public static final String OTP_URL = " https://nrlm.gov.in/nrlmwebservice/services/login/message?mobileno=9812343583&message=9811";
    public static final String name[]={"CRP Attedence","STATE Coordinator Attedence", "SHG Attedence","Member 1","Member 2"};
    public static       String LOGIN_NUMBER_URL="https://nrlm.gov.in/nrlmwebservice/services/mela/user?melaId=1&mobile=8894227755";
    public static final String RESET_PASSWORD="https://nrlm.gov.in/nrlmwebservice/services/melaattend/resetPassword";
    // http://10.197.183.105:8080/nrlmwebservice/services/mela/users?melaId=3&mobile=9891047659&password=910476&deviceInfo=Xiaomi-cactus-Redmi
  // URL for live server
    public static final String HTTP_TYPE="https";
    public static final String IP_ADDRESS="nrlm.gov.in";
     public static final String API_TYPE="nrlmwebservice";

    //10.24.16.2:8080
//url for local
/*      public static final String HTTP_TYPE="http";
      public static final String IP_ADDRESS="10.197.183.105:8080";
      public static final String API_TYPE="nrlmwebservice";*/
        //  Demo server
    //  ip - 10.197.183.151:8080
   /* public static final String API_TYPE="nrlmwebservicedemo";
    public static final String HTTP_TYPE="https";
    public static final String IP_ADDRESS="nrlm.gov.in";*/

    public static final String NOT_AVAILABLE = "Not Available";
    public static final String melaId="6";
    public static final int NO_DATA=0;
}

