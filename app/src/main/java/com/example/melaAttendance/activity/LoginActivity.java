package com.example.melaAttendance.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;
import com.example.melaAttendance.database.ShgsDetailsOnLoginDataDao;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.Cryptography;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.GPSTracker;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import com.example.melaAttendance.volley.VolleyResult;
import com.example.melaAttendance.volley.VolleyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class LoginActivity extends AppCompatActivity {
    boolean status=false;
    private Button login;
    private EditText mobileNo,password;
    private MaterialBetterSpinner Shg_id;
    private ArrayList<String> regno;
    public static final String SHARED_Bill = "BILL";
    private Context context;
    private String otp;
    private ProgressDialog progressDialog;
    private String encodedMsg;
    private String mobileNumber;
    private TextView forgotPassword;
    VolleyService volleyService;
    String passwordlogin;
    String otpMobileNumber;
    AppUtility appUtility;
     //  public static String LOGIN_NUMBER_URL = "https://nrlm.gov.in/nrlmwebservice/services/mela/users?melaId=3&mobile=";
   // public static String LOGIN_NUMBER_URL = AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/nrlmwebservice/services/mela/users?melaId=3&mobile=";
    public static String LOGIN_NUMBER_URL = AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/mela/users";
    private List<ShgsDetailsOnLoginData> shgsNameListForSpinner;
    private List<String> showSpninnerList;
    private String selectedShgnameFromSpinner;
    private int shgRegistrationIdFromLocal;
    private String shgRegistrationCodeFromLocal, shgRegistrationNameFromLocal, shgRegisterationMobileFromLocal,shgStallNumber;
    public static final int RequestPermissionCode = 1;
    TelephonyManager telephonyManager;
    int melaId=0;
    private Handler handler;
    private Runnable runnable;
    String generatedOTP ="";
    String SEND_OTP_MESSAGE_URL=AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/melaattend/forgot";
    //String URL_DATA = "https://smsgw.sms.gov.in/failsafe/HttpLink?username=mnrlm.otp&pin=Ty%40%2312Qa&&message=Hello&mnumber=9034930094&signature=DAYSMS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getBaseContext();
        login = (Button) findViewById(R.id.btnLogin);
        mobileNo = (EditText) findViewById(R.id.registn_num);
        forgotPassword=(TextView)findViewById(R.id.forgotPasswordTv);
     /*  Shg_id = findViewById(R.id.shg_nameMBS);
        Shg_id.setVisibility(View.GONE);*/
        volleyService =VolleyService.getInstance(LoginActivity.this);
        regno = new ArrayList<String>();
        password=(EditText)findViewById(R.id.passwrd);
        password.setGravity(Gravity.CENTER_HORIZONTAL);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();

        if(CheckingPermissionIsEnabledOrNot())
        {
        }
        else {
            RequestMultiplePermission();
        }
   //     handleSSLHandshake();
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                passwordlogin=password.getText().toString().trim();
                mobileNumber = mobileNo.getText().toString().trim();
                if ((mobileNumber == null) || (mobileNumber.length() < 10)) {
                    mobileNo.setError("Invalid Mobile Number.");
                } else if (passwordlogin == null ||passwordlogin.length()<6 ){
                    password.setError("Invalid Password.");
                } else {
                    SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        fetchingLoginDataFromServer(mobileNumber,passwordlogin,getDeviceInfo(),getIMEINo1());
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }


    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getIMEINo1() {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount() > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    AppUtility.getInstance().showLog("imeiNo1=" + imeiNo1, LoginActivity.class);
                }else if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    imeiNo1 = telephonyManager.getDeviceId(0);
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
      //  return "867130042454246";
        return imeiNo1;
    }

    private String getIMEINo2() {
        String imeiNo2 = "";
        if (getSIMSlotCount() > 1) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                imeiNo2 = telephonyManager.getDeviceId(1);
                AppUtility.getInstance().showLog("imeiNo2=" + imeiNo2, LoginActivity.class);
            }

        } else imeiNo2 = telephonyManager.getDeviceId();

        return imeiNo2;
    }

    public void forgetPassword(){

        MaterialButton cancelBtn,sendOTPBtn;
        TextInputEditText otpMobileEt;
        TextInputLayout mobInputLayout;

        if(NetworkFactory.isInternetOn(LoginActivity.this)){
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
            View customLayout = getLayoutInflater().inflate(R.layout.forgot_password_custom_dialog, null);
            materialAlertDialogBuilder.setView(customLayout);
            materialAlertDialogBuilder.setCancelable(false);
            androidx.appcompat.app.AlertDialog cusDialog =materialAlertDialogBuilder.show();

            cancelBtn =customLayout.findViewById(R.id.cancelBtn);
            sendOTPBtn =customLayout.findViewById(R.id.sendOTPBtn);
            mobInputLayout =customLayout.findViewById(R.id.mobInputLayout);
            otpMobileEt =customLayout.findViewById(R.id.otpMobileEt);

            otpMobileEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mobInputLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cusDialog.dismiss();
                }
            });
            sendOTPBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otpMobileNumber = otpMobileEt.getText().toString().trim();
                    if(otpMobileNumber.isEmpty()||otpMobileNumber.length()<10){
                        mobInputLayout.setError("Enter a Valid Mobile Number");
                    }else {
                        appUtility=AppUtility.getInstance();
                        generatedOTP =appUtility.getRandomOtp();
                        callOTPApi();
                        cusDialog.dismiss();
                       /* appSharedPreferences.setMobile(otpMobileNumber);
                        appSharedPreferences.setOtp(generatedOTP);
                        Intent intent =new Intent(LoginActivity.this,OtpVerificationActivity.class);
                        startActivity(intent);*/

                    }
                }
            });

        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please enable your internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            materialAlertDialogBuilder.show();
        }

    }

    private void callOTPApi() {
        if(NetworkFactory.isInternetOn(LoginActivity.this)){
            ProgressDialog progressDialog =new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject masterUrlObject =new JSONObject();
            try {
                String msg =generatedOTP+" is One Time Password(OTP) for the Mela Attendance application. Do not share it with anyone.";
                masterUrlObject.accumulate("mobile",otpMobileNumber);
                masterUrlObject.accumulate("melaId",AppConstants.melaId);
                masterUrlObject.accumulate("otpMessage",msg);

            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("OTP json making exception:- "+e,LoginActivity.class);
            }

          VolleyResult mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    progressDialog.dismiss();
                    appUtility.showLog("get response:-" +response,LoginActivity.class);
                    //{"data":"","message":"Mobile Number Invalid","status":0}
                    try{
                        if(response.has("message")){
                            String status =response.getString("message");
                            if(status.equalsIgnoreCase("message send successfully")){
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getOTP(),generatedOTP,LoginActivity.this);
                                 ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getOtpMobileNumber(),otpMobileNumber,LoginActivity.this);
                                 Intent intent =new Intent(LoginActivity.this,OtpVerificationActivity.class);
                                startActivity(intent);

                            }else {
                                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Mobile Number does not exist...");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                materialAlertDialogBuilder.show();
                            }
                        }
                    }catch (Exception e){
                        appUtility.showLog("OTP get response Expection:-" +e,LoginActivity.class);
                    }
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    progressDialog.dismiss();
                    appUtility.showLog("volley error:-" +error,LoginActivity.class);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Server Error please try again");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            };
            volleyService.postDataVolley("otp_request",SEND_OTP_MESSAGE_URL,masterUrlObject,mResultCallBack);
        }
        else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please On Your internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            materialAlertDialogBuilder.show();
        }

    }

    private String getDeviceInfo() {
        String deviceInfo = "";
        deviceInfo = Build.MANUFACTURER + "-" + Build.DEVICE + "-" + Build.MODEL;
        AppUtility.getInstance().showLog("deviceInfo=" + deviceInfo, LoginActivity.class);
        if (deviceInfo.equalsIgnoreCase(""))
            return "";
       // return "Xiaomi-cactus-Redmi 6A";
        return deviceInfo;
    }

    private int getSIMSlotCount() {
        int getPhoneCount = 0;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPhoneCount = telephonyManager.getPhoneCount();
        }
        return getPhoneCount;
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_COARSE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        READ_PHONE_STATE
                }, RequestPermissionCode);
    }

    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult==PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED&&
                FourthPermissionResult==PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission&&GetLocationPermission&&ReadExternalStoragePermission&&ReadPhoneStatePermission) {

                        Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void getLoginDetailsFromLocalDB(String mobileNumber) {

        shgsNameListForSpinner = SplashScreen.getInstance().getDaoSession()
                .getShgsDetailsOnLoginDataDao()
                .queryBuilder()
                .where(ShgsDetailsOnLoginDataDao.Properties.LoginMobilenumber.eq(mobileNumber))
                .build()
                .list();

        AppUtility.getInstance().showLog("shgnamelistfrospinner" + shgsNameListForSpinner, LoginActivity.class);

    }
    private void setUpOTPApi() {
            Intent intent = new Intent(LoginActivity.this, MpinActivity.class);
            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyShgMobile(), mobileNumber, LoginActivity.this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
       /* setUpOTP(mobileNumber);
        String url = " https://nrlm.gov.in/nrlmwebservice/services/login/message?mobileno=" + mobileNumber + "&message=" + otp;
        // String url = "https://smsgw.sms.gov.in/failsafe/HttpLink?username=mnrlm.otp&pin=Ty%40%2312Qa&&message="+message+"&mnumber="+mobilenum+"&signature=DAYSMS";
     ProgressDialog progressDialogss=DialogFactory.getInstance().showProgressDialog(LoginActivity.this);
        if (!NetworkFactory.isInternetOn(LoginActivity.this)) {
            DialogFactory.getInstance().showNoInternetDialog(LoginActivity.this);
            return;
        } else {
            progressDialogss.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String s) {
                progressDialogss.dismiss();
                Log.d("TAG", "Response : " + s);
                try {
                    Intent intent = new Intent(LoginActivity.this, MpinActivity.class);
                    bundle = new Bundle();
                    bundle.putInt(PreferenceManager.getShgRegistrationId(), shgRegistrationIdFromLocal);
                    bundle.putString("mNumber", mobileNumber);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyShgMobile(), mobileNumber, LoginActivity.this);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        try {
            stringRequest.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }*/
    }
    public void setUpOTP(String mNumber) {
        otp = getRandomOtp();
        encodedMsg = getEncodedMsg(otp);
        AppUtility.getInstance().showLog(""+encodedMsg,LoginActivity.class);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(mNumber, otp, context);
    }

    private String getEncodedMsg(String otp) {
        String hexString = "";
        for (char ch : otp.trim().toCharArray()) {
            hexString = hexString.trim().concat(String.format("%04x", (int) ch));
        }
        return hexString;
    }
    public String getRandomOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9999);
        // Toast.makeText(context, "OTP is: " + otp + "", Toast.LENGTH_LONG).show();
        return "" + otp;
    }
    public boolean fetchingLoginDataFromServer(String mobileNumber,String passwordlogin,String deviceInfo, String imei) {
         ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyImei(),imei,context);
         ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyDeviceInfo(),deviceInfo,context);
        ProgressDialog progressDialog=DialogFactory.getInstance().showProgressDialog(LoginActivity.this);
       if (!NetworkFactory.isInternetOn(LoginActivity.this)) {
            DialogFactory.getInstance().showNoInternetDialog(LoginActivity.this);
            return status =false;
        } else {
           progressDialog.show();
       }
        //String FINAL_LOGIN_NUMBER_URL = LoginActivity.LOGIN_NUMBER_URL + mobileNumber+"&password="+passwordlogin+"&deviceInfo="+deviceInfo+"&imei="+imei;
        String FINAL_LOGIN_NUMBER_URL = LoginActivity.LOGIN_NUMBER_URL ;
        JSONObject masterUrlJsonObj=new JSONObject();
        try {
            masterUrlJsonObj.accumulate("mobile",mobileNumber );//"9958648812" //mobileNumber

            masterUrlJsonObj.accumulate("melaId",AppConstants.melaId);
            masterUrlJsonObj.accumulate("password",passwordlogin);//"586488" //passwordlogin
            masterUrlJsonObj.accumulate("deviceInfo",deviceInfo);//"vivo-2027-V2027" //deviceInfo
            masterUrlJsonObj.accumulate("imei",imei);//"71dd99745641efdf" //imei
            masterUrlJsonObj.accumulate("app_login_time", DateFactory.getInstance().getDateTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*******make json object is encrypted and *********/
        JSONObject encryptedObject =new JSONObject();
        try {
            Cryptography cryptography = new Cryptography();
             String encrypted=cryptography.encrypt(masterUrlJsonObj.toString());
            encryptedObject.accumulate("data",encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /***********************************************/
        AppUtility.getInstance().showLog("LoginUrl"+FINAL_LOGIN_NUMBER_URL,LoginActivity.class);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, FINAL_LOGIN_NUMBER_URL,encryptedObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject s) {
                try {
                    AppUtility.getInstance().showLog("response"+s,LoginActivity.class);
                    Cryptography cryptography = null;
                    JSONArray    jsonArr=null;
                    String objectResponse="";
                    if(s.has("data")){
                        objectResponse=s.getString("data");
                    }else {
                        return;
                    }
                    try {
                        cryptography = new Cryptography();
                           jsonArr = new JSONArray(cryptography.decrypt(objectResponse));
                           AppUtility.getInstance().showLog("LoginResponse"+jsonArr,LoginActivity.class);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    }catch (Exception e)
                    {
                          e.printStackTrace();
                    }
                    if (jsonArr.toString().equalsIgnoreCase("[]")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Info");
                        builder.setMessage("No Data Found.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    JSONArray mainArr = null;
                    try {
                        mainArr = jsonArr;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    for (int i = 0; i < mainArr.length(); i++) {
                            JSONObject obj = mainArr.getJSONObject(i);


                        if (obj.has("status")) {
                                DialogFactory.getInstance().showServerErrorDialog(LoginActivity.this, obj.getString("status"), "OK");
                            }else if(obj.has("login_attempt") || obj.has("reason"))
                            {
                            if(i==0) {

                                if(obj.has("login_attempt")) {
                                    builder.setTitle(obj.getInt("login_attempt") + " " + "Invalid login attempt");
                                    continue;
                                }else{
                                    builder.setTitle("Info");
                                }
                            }
                                builder.setMessage(obj.getString("reason"));
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }

                                else {
                                String shgCode = obj.getString("shg_code");
                                int shgRegId = obj.getInt("shg_reg_id");
                                String shgName = obj.getString("shg_name");
                                long loginMobileNumber = obj.getLong("mobile");
                                melaId = obj.getInt("mela_id");
                                String stallNo = obj.getString("stall_no");

                                ShgsDetailsOnLoginData shgsDetailsOnLoginData = new ShgsDetailsOnLoginData();

                                if (shgCode != null && !shgCode.isEmpty()) {
                                    shgsDetailsOnLoginData.setShgCode(shgCode);
                                } else {
                                    shgsDetailsOnLoginData.setShgCode(AppConstants.NOT_AVAILABLE);
                                }
                                shgsDetailsOnLoginData.setShgRegistrationId(shgRegId);

                                if (shgName != null && !shgName.isEmpty()) {
                                    shgsDetailsOnLoginData.setShgName(shgName);
                                } else {
                                    shgsDetailsOnLoginData.setShgCode(AppConstants.NOT_AVAILABLE);
                                }
                                if (stallNo != null && !stallNo.isEmpty()) {
                                    shgsDetailsOnLoginData.setStallNo(stallNo);
                                } else {
                                    shgsDetailsOnLoginData.setStallNo(AppConstants.NOT_AVAILABLE);
                                }
                                shgsDetailsOnLoginData.setLoginMobilenumber(String.valueOf(loginMobileNumber));
                                shgsDetailsOnLoginData.setMelaId(melaId);
                                ShgsDetailsOnLoginDataDao shgsDetailsOnLoginDataDao = SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao();
                                shgsDetailsOnLoginDataDao.insert(shgsDetailsOnLoginData);

                            }
                        }
                        progressDialog.dismiss();
                        password.setVisibility(View.VISIBLE);
                        if (SplashScreen.getInstance()
                                .getDaoSession()
                                .getShgsDetailsOnLoginDataDao()
                                .queryBuilder()
                                .build()
                                .list().size() != 0) {
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantMelaidForSetAttendance(), String.valueOf(melaId), LoginActivity.this);
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefPhonenoForMpin(), mobileNumber, LoginActivity.this);
                            AppUtility.getInstance().showLog("password" + password, LoginActivity.class);
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData("password", passwordlogin, LoginActivity.this);
                            setUpOTPApi();
                        }
                } catch (JSONException e) {
                    AppUtility.getInstance().showLog("ex"+e,LoginActivity.class);
                    status=false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                status=false;
                DialogFactory.getInstance().showServerErrorDialog(LoginActivity.this,"Server Error!!!","OK");
                Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                AppUtility.getInstance().showLog("LoginVolleyError0"+volleyError.getMessage(),LoginActivity.class);
            }
        });
        //stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //  requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        return status;
    }
 /*   String FINAL_LOGIN_NUMBER_URL = LoginActivity.LOGIN_NUMBER_URL + mobileNumber+"&password="+passwordlogin+"&deviceInfo="+deviceInfo+"&imei="+imei;
        AppUtility.getInstance().showLog("LoginUrl"+FINAL_LOGIN_NUMBER_URL,LoginActivity.class);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, FINAL_LOGIN_NUMBER_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                AppUtility.getInstance().showLog("response"+s,LoginActivity.class);
                if (s.equalsIgnoreCase("[]")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("No Data Found.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                JSONArray mainArr = new JSONArray(s);
                for (int i = 0; i < mainArr.length(); i++) {
                    JSONObject obj = mainArr.getJSONObject(i);
                    if (obj.has("status")) {
                        DialogFactory.getInstance().showServerErrorDialog(LoginActivity.this, obj.getString("status"), "OK");
                    } else {
                        String shgCode = obj.getString("shg_code");
                        int shgRegId = obj.getInt("shg_reg_id");
                        String shgName = obj.getString("shg_name");
                        long loginMobileNumber = obj.getLong("mobile");
                        melaId = obj.getInt("mela_id");
                        String stallNo = obj.getString("stall_no");

                        ShgsDetailsOnLoginData shgsDetailsOnLoginData = new ShgsDetailsOnLoginData();

                        if (shgCode != null && !shgCode.isEmpty()) {
                            shgsDetailsOnLoginData.setShgCode(shgCode);
                        } else {
                            shgsDetailsOnLoginData.setShgCode(AppConstants.NOT_AVAILABLE);
                        }
                        shgsDetailsOnLoginData.setShgRegistrationId(shgRegId);

                        if (shgName != null && !shgName.isEmpty()) {
                            shgsDetailsOnLoginData.setShgName(shgName);
                        } else {
                            shgsDetailsOnLoginData.setShgCode(AppConstants.NOT_AVAILABLE);
                        }
                        if (stallNo != null && !stallNo.isEmpty()) {
                            shgsDetailsOnLoginData.setStallNo(stallNo);
                        } else {
                            shgsDetailsOnLoginData.setStallNo(AppConstants.NOT_AVAILABLE);
                        }
                        shgsDetailsOnLoginData.setLoginMobilenumber(String.valueOf(loginMobileNumber));
                        shgsDetailsOnLoginData.setMelaId(melaId);
                        ShgsDetailsOnLoginDataDao shgsDetailsOnLoginDataDao = SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao();
                        shgsDetailsOnLoginDataDao.insert(shgsDetailsOnLoginData);

                    }
                }
                progressDialog.dismiss();
                password.setVisibility(View.VISIBLE);
                if (SplashScreen.getInstance()
                        .getDaoSession()
                        .getShgsDetailsOnLoginDataDao()
                        .queryBuilder()
                        .build()
                        .list().size() != 0) {

                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantMelaidForSetAttendance(), String.valueOf(melaId), LoginActivity.this);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefPhonenoForMpin(), mobileNumber, LoginActivity.this);
                    AppUtility.getInstance().showLog("password" + password, LoginActivity.class);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData("password", passwordlogin, LoginActivity.this);
                    setUpOTPApi();
                }

            } catch (JSONException e) {
                AppUtility.getInstance().showLog("ex"+e,LoginActivity.class);
                status=false;
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
            status=false;
            DialogFactory.getInstance().showServerErrorDialog(LoginActivity.this,"Server Error!!!","OK");
            Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            AppUtility.getInstance().showLog("ex"+volleyError.getMessage(),LoginActivity.class);
        }
    });
    //stringRequest.setShouldCache(false);
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    //  requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        return status;
}*/
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();
    }
    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(LoginActivity.this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(runnable);
    }
    public void startHandler() {
        handler.postDelayed(runnable, 30*60*1000);
    }

}
