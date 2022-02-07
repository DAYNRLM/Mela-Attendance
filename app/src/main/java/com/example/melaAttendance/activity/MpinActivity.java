package com.example.melaAttendance.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.database.AttendanceFlagData;
import com.example.melaAttendance.database.AttendanceFlagDataDao;
import com.example.melaAttendance.database.ShgDetailsData;
import com.example.melaAttendance.database.ShgMemberDetailsData;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MpinActivity extends AppCompatActivity {
    Button btnMpinProceed,
            submit_Fgd;
    PinEntryEditText pinEntryEditText,pinConfirmMpinEditText;
    String mPin,confirmMpin;
    final String TAG = "tag";
    private int shgRegistrationId;
    private String mNumber;
    String userName,idFgd,passswordFgd;
    private List<AttendanceFlagData> attendanceFlagData;
    private List<ShgMemberDetailsData> memberDataList;
    String participantStatus;
    int melaId;
    String getMpinFromPreference,checkMpin;
    LinearLayout verifieLinearLayout,topLineraLayout;
    TextView fgtpin;
    EditText id_Fgd,password_Fgd;
Handler handler;
Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);

        handler=new Handler();
        runnable=  new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();
        btnMpinProceed =(Button)findViewById(R.id.btnMpinProceed);
        pinEntryEditText  =(PinEntryEditText)findViewById(R.id.txt_pin_entry);
        pinConfirmMpinEditText = (PinEntryEditText)findViewById(R.id.txt_pin_entry_confirm);
        verifieLinearLayout = (LinearLayout)findViewById(R.id.verifieLinerLayout);
        topLineraLayout = (LinearLayout)findViewById(R.id.topSetLinearLayout);
        fgtpin=(TextView)findViewById(R.id.fgt_pin);
       // shgRegistrationId=bundle.getInt(PreferenceManager.getShgRegistrationId());

        shgRegistrationId = ProjectPrefrences.getInstance().getSharedPrefrencesIntegerData(PreferenceManager.getPrefKeyShgRegId(),MpinActivity.this);

        getMpinFromPreference = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyMpin(),MpinActivity.this);
       // Log.d("tag","prefrance value"+getMpinFromPreference);
        AppUtility.getInstance().showLog("shgregid"+shgRegistrationId,MpinActivity.class);
       // mNumber=bundle.getString("mNumber");

        Dialog dialog;
        dialog=new Dialog(MpinActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgot_mpin_dialog);
        id_Fgd=(EditText) dialog.findViewById(R.id.id_fgd);
        password_Fgd=(EditText) dialog.findViewById(R.id.passwd_fgd);
        submit_Fgd=(Button) dialog.findViewById(R.id.submit_fgd);

        fgtpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        submit_Fgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idFgd=id_Fgd.getText().toString();
                passswordFgd=password_Fgd.getText().toString();

                if (idFgd.length()<10){
                    id_Fgd.setError("Invalid number");
                }else if (passswordFgd.length()<3){
                    password_Fgd.setError("Invalid password.");
                }else {
                    if (idFgd.equalsIgnoreCase(ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefPhonenoForMpin(),MpinActivity.this))) {
                        if (ProjectPrefrences.getInstance().getSharedPrefrencesData("password", MpinActivity.this).equalsIgnoreCase(passswordFgd)) {
                            dialog.dismiss();
                            verifieLinearLayout.setVisibility(View.GONE);
                            topLineraLayout.setVisibility(View.VISIBLE);

                        }
                    }else Toast.makeText(getApplicationContext(),"Mobile number is not matched!!!",Toast.LENGTH_SHORT).show();
                            btnMpinProceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String pass = pinEntryEditText.getText().toString();
                                    String conf = pinConfirmMpinEditText.getText().toString();
                                    if (pass.length() == 0) {
                                        pinEntryEditText.setError("Please, Fill the password.");
                                    } else if (conf.length() == 0) {
                                        pinConfirmMpinEditText.setError("Please, Fill the confirm password.");
                                    } else {
                                        if (pass.equalsIgnoreCase(conf)) {

                                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyMpin(), conf, MpinActivity.this);

                                            Intent intent = new Intent(MpinActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                }

            }
        });

     mNumber = ProjectPrefrences.getInstance().getSharedPrefrencesData( PreferenceManager.getPrefKeyShgMobile(),MpinActivity.this);


     if((getMpinFromPreference!=null) &&(getMpinFromPreference!="")){
     verifieLinearLayout.setVisibility(View.VISIBLE);

     fgtpin.setPaintFlags(fgtpin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


     final PinEntryEditText pinEntry2 = findViewById(R.id.tvEnterMpin);
     if (pinEntry2 != null) {
     pinEntry2.setAnimateText(true);
     pinEntry2.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
     @Override
     public void onPinEntered(CharSequence str) {

                        checkMpin = str.toString();

                        if (checkMpin.equalsIgnoreCase(getMpinFromPreference)) {
                            Toast.makeText(MpinActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            btnMpinProceed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   // Toast.makeText(MpinActivity.this,"inside the button",Toast.LENGTH_LONG).show();
                                    if((checkMpin!=null)&& (checkMpin.equalsIgnoreCase(getMpinFromPreference))) {

                                        Intent intent = new Intent(MpinActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else {

                                        Toast.makeText(MpinActivity.this,"Please enter Mpin",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            pinEntry2.setError(true);
                            Toast.makeText(MpinActivity.this, "Please Enter Corrrect Mpin", Toast.LENGTH_SHORT).show();
                            checkMpin=null;
                            pinEntry2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pinEntry2.setText(null);
                                    checkMpin=null;
                                }
                            }, 1000);
                        }
                    }
                });

            }




        }else{

            topLineraLayout.setVisibility(View.VISIBLE);

        }



        final PinEntryEditText pinEntry = findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    mPin=str.toString();
                }
            });
        }

        final PinEntryEditText pinEntry2 = findViewById(R.id.txt_pin_entry_confirm);
        if (pinEntry2 != null) {
            pinEntry2.setAnimateText(true);
            pinEntry2.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    confirmMpin = str.toString();
                    if (confirmMpin.equalsIgnoreCase(mPin)) {
                        Toast.makeText(MpinActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                    } else {
                        pinEntry2.setError(true);
                        Toast.makeText(MpinActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pinEntry2.setText(null);
                                confirmMpin=null;
                            }
                        }, 1000);
                    }
                }
            });
        }
        btnMpinProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FINAL_SHG_DETAILS_URL="https://nrlm.gov.in/nrlmwebservice/services/mela/data?shgRegId="+shgRegistrationId+"&mobile="+mNumber;
                if((mPin!=null  && confirmMpin!=null) && (mPin.equalsIgnoreCase(confirmMpin)) )
                {

                    //getShgAndMemberDataFromServer();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyLoginDone(), "ok", MpinActivity.this);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyMpin(), confirmMpin, MpinActivity.this);

                    Intent intent = new Intent(MpinActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData("usernamemm",userName,MpinActivity.this);


                }

            }
        });

    }


    private void getShgAndMemberDataFromServer()
    {
        ProgressDialog progressDialog=new ProgressDialog(MpinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String FINAL_SHG_DETAILS_URL="https://nrlm.gov.in/nrlmwebservice/services/mela/data?shgRegId="+shgRegistrationId+"&mobile="+mNumber;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, FINAL_SHG_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //    progressDialog.dismiss();
                AppUtility.getInstance().showLog("response:"+s,OTPverification.class);
                try {
                    JSONArray mainArr = new JSONArray(s);
                    for(int i=0;i<mainArr.length();i++)
                    {
                        JSONObject obj=mainArr.getJSONObject(i);
                        String shgCode=obj.getString("shg_code");
                        String   user_nameR=obj.getString("user_name");
                        String stallNo=obj.getString("stall_no");
                        int shg_reg_idR=obj.getInt("shg_reg_id");
                        String  shg_main_participantR=obj.getString("shg_main_participant");
                        String  shg_reg_codeR=obj.getString("shg_reg_code");
                        int mela_idR=obj.getInt("mela_id");
                        String  shggstR=obj.getString("shggst");
                        String  entity_codeR=obj.getString("entity_code");
                        int user_idR=obj.getInt("user_id");
                        String shg_nameR=obj.getString("shg_name");
                        String   main_participant_mobileR=obj.getString("main_participant_mobile");
                        String  participant_statusR=obj.getString("participant_status");

                        //putting shg details in local db

                        ShgDetailsData shgDetailsData=new ShgDetailsData();

                        if((shgCode !=null) & !shgCode.isEmpty())
                            shgDetailsData.setShg_code(shgCode);
                        else shgDetailsData.setShg_code(AppConstants.NOT_AVAILABLE);

                        if((user_nameR !=null) && !user_nameR.isEmpty())
                            shgDetailsData.setUser_name(user_nameR);
                        else shgDetailsData.setUser_name(AppConstants.NOT_AVAILABLE);

                        if((stallNo !=null) && !stallNo.isEmpty())
                            shgDetailsData.setStallNo(stallNo);
                        else shgDetailsData.setStallNo(AppConstants.NOT_AVAILABLE);

                        shgDetailsData.setShg_reg_id(shg_reg_idR);

                        if((shg_main_participantR !=null) && !shg_main_participantR.isEmpty())
                            shgDetailsData.setShg_main_participant(shg_main_participantR);
                        else shgDetailsData.setShg_main_participant(AppConstants.NOT_AVAILABLE);

                        if((shg_reg_codeR !=null) && !shg_reg_codeR.isEmpty())
                            shgDetailsData.setShg_reg_code(shg_reg_codeR);
                        else shgDetailsData.setShg_reg_code(AppConstants.NOT_AVAILABLE);

                        shgDetailsData.setMela_id(mela_idR);

                        if(!(shggstR ==null) && !shggstR.isEmpty())
                            shgDetailsData.setShggst(shggstR);
                        else shgDetailsData.setShggst(AppConstants.NOT_AVAILABLE);


                        if(!(entity_codeR ==null) && !entity_codeR.isEmpty())
                            shgDetailsData.setEntity_code(entity_codeR);
                        else shgDetailsData.setEntity_code(AppConstants.NOT_AVAILABLE);

                        shgDetailsData.setUser_id(user_idR);

                        if(!(shg_nameR ==null) && !shg_nameR.isEmpty())
                            shgDetailsData.setShg_name(shg_nameR);
                        else shgDetailsData.setShg_name(AppConstants.NOT_AVAILABLE);

                        if(!(main_participant_mobileR ==null) && !main_participant_mobileR.isEmpty())
                            shgDetailsData.setMain_participant_mobile(main_participant_mobileR);
                        else shgDetailsData.setMain_participant_mobile(AppConstants.NOT_AVAILABLE);

                        if(!(participant_statusR==null) && !participant_statusR.isEmpty())
                            shgDetailsData.setParticipant_status(participant_statusR);
                        else shgDetailsData.setParticipant_status(AppConstants.NOT_AVAILABLE);

                        SplashScreen.getInstance().getDaoSession().getShgDetailsDataDao().insert(shgDetailsData);


                        JSONArray productArr=obj.getJSONArray("Product");
                        for(int j=0;j<productArr.length();j++) {
                            JSONObject productobj = productArr.getJSONObject(j);
                            String  category_nameR = productobj.getString("category_name");
                            String  shg_member_codeR = productobj.getString("shg_member_code");
                            String  ass_nameR = productobj.getString("ass_name");
                            String avialableQuantity=productobj.getString("available_quantity");
                            String mobileR = productobj.getString("mobile");
                            String subcategory_nameR = productobj.getString("subcategory_name");
                            String member_nameR = productobj.getString("member_name");
                            String product_nameR = productobj.getString("product_name");
                            String district_nameR = productobj.getString("district_name");
                            String Stringblock_nameR = productobj.getString("block_name");
                            String ass_mobileR = productobj.getString("ass_mobile");
                            String state_nameR = productobj.getString("state_name");
                            String village_nameR = productobj.getString("village_name");
                            String grampanchayat_nameR = productobj.getString("grampanchayat_name");
                            int category_idR = productobj.getInt("category_id");
                            int subcategory_idR = productobj.getInt("subcategory_id");
                            int product_idR = productobj.getInt("product_id");
                            ShgMemberDetailsData shgMemberDetailsData=new ShgMemberDetailsData();

                            if(!(category_nameR==null) && !category_nameR.isEmpty())
                                shgMemberDetailsData.setCategory_name(category_nameR);
                            else shgMemberDetailsData.setCategory_name(AppConstants.NOT_AVAILABLE);

                            if(!(shg_member_codeR==null) && !shg_member_codeR.isEmpty())
                                shgMemberDetailsData.setShg_member_code(shg_member_codeR);
                            else shgMemberDetailsData.setShg_member_code(AppConstants.NOT_AVAILABLE);

                            if(!(avialableQuantity==null) && !avialableQuantity.isEmpty())
                                shgMemberDetailsData.setAvialableQuantity(avialableQuantity);
                            else shgMemberDetailsData.setAvialableQuantity(AppConstants.NOT_AVAILABLE);

                            if(!(ass_nameR==null) && !ass_nameR.isEmpty())
                                shgMemberDetailsData.setAss_name(ass_nameR);
                            else shgMemberDetailsData.setAss_name(AppConstants.NOT_AVAILABLE);

                            if(!(mobileR==null) && !mobileR.isEmpty())
                                shgMemberDetailsData.setMobile(mobileR);
                            else shgMemberDetailsData.setMobile(AppConstants.NOT_AVAILABLE);

                            if(!(subcategory_nameR==null) && !subcategory_nameR.isEmpty())
                                shgMemberDetailsData.setSubcategory_name(subcategory_nameR);
                            else shgMemberDetailsData.setSubcategory_name(AppConstants.NOT_AVAILABLE);

                            if(!(member_nameR==null) && !member_nameR.isEmpty() )
                                shgMemberDetailsData.setMember_name(member_nameR);
                            else shgMemberDetailsData.setMember_name(AppConstants.NOT_AVAILABLE);

                            if(!(product_nameR==null) && !product_nameR.isEmpty())
                                shgMemberDetailsData.setProduct_name(product_nameR);
                            else shgMemberDetailsData.setProduct_name(AppConstants.NOT_AVAILABLE);

                            if(!(district_nameR==null) && !district_nameR.isEmpty())
                                shgMemberDetailsData.setDistrict_name(district_nameR);
                            else shgMemberDetailsData.setDistrict_name(AppConstants.NOT_AVAILABLE);

                            if(!(Stringblock_nameR==null) && !Stringblock_nameR.isEmpty())
                                shgMemberDetailsData.setBlock_name(Stringblock_nameR);
                            else shgMemberDetailsData.setBlock_name(AppConstants.NOT_AVAILABLE);

                            if(!(ass_mobileR==null) && !ass_mobileR.isEmpty())
                                shgMemberDetailsData.setAss_mobile(ass_mobileR);
                            else shgMemberDetailsData.setAss_mobile(AppConstants.NOT_AVAILABLE);

                            if(!(state_nameR==null) && !state_nameR.isEmpty())
                                shgMemberDetailsData.setState_name(state_nameR);
                            else shgMemberDetailsData.setState_name(AppConstants.NOT_AVAILABLE);

                            if(!(village_nameR==null) && !village_nameR.isEmpty())
                                shgMemberDetailsData.setVillage_name(village_nameR);
                            else shgMemberDetailsData.setVillage_name(AppConstants.NOT_AVAILABLE);

                            if(!(grampanchayat_nameR==null) && !grampanchayat_nameR.isEmpty())
                                shgMemberDetailsData.setGrampanchayat_name(grampanchayat_nameR);
                            else shgMemberDetailsData.setGrampanchayat_name(AppConstants.NOT_AVAILABLE);

                            shgMemberDetailsData.setCategory_id(category_idR);
                            shgMemberDetailsData.setSubcategory_id(subcategory_idR);
                            shgMemberDetailsData.setProduct_id(product_idR);


                            SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao().insert(shgMemberDetailsData);

                         //   ProjectPrefrences.getInstance().removeSharedPrefrencesData(mNumber, context);
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyLoginDone(), "ok", MpinActivity.this);
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyMpin(), confirmMpin, MpinActivity.this);

                            //save mpin in shared preference.....

                           // getCrpShgAndMembersDataFromLocalDB();
                            progressDialog.dismiss();
                            Intent intent = new Intent(MpinActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                Toast.makeText(MpinActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                AppUtility.getInstance().showLog("volleyError.getMessage()"+volleyError.getMessage(),OTPverification.class);
            }
        });
        //stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //  requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void getCrpShgAndMembersDataFromLocalDB() {

        attendanceFlagData = new ArrayList<>();
        String loginDate = DateFactory.getInstance().getTodayDate();

        List<ShgDetailsData> shgDetailsDataList = SplashScreen.getInstance().getDaoSession()
                .getShgDetailsDataDao()
                .queryBuilder()
                .build()
                .list();
        AppUtility.getInstance().showLog("crporsc"+shgDetailsDataList,OTPverification.class);
        for (int m = 0; m < shgDetailsDataList.size(); m++) {
            participantStatus = shgDetailsDataList.get(m).getParticipant_status();
            melaId = shgDetailsDataList.get(m).getMela_id();
            userName= shgDetailsDataList.get(m).getUser_name();

        }
        if (participantStatus.equalsIgnoreCase("CRP")) {

            QueryBuilder<AttendanceFlagData> attendanceFlagDataQueryBuilder = SplashScreen.getInstance()
                    .getDaoSession()
                    .getAttendanceFlagDataDao()
                    .queryBuilder();
            List<AttendanceFlagData> sameDayAttendanceCheckList = attendanceFlagDataQueryBuilder
                    .where(AttendanceFlagDataDao.Properties.ShgRegIdAttendanceFlagData.eq(String.valueOf(shgRegistrationId))
                            , attendanceFlagDataQueryBuilder.and(AttendanceFlagDataDao.Properties.ShgRegIdAttendanceFlagData.eq(String.valueOf(shgRegistrationId))
                                    , AttendanceFlagDataDao.Properties.DateOfAttendanceFlagData.eq(loginDate)))
                    .build()
                    .list();
            if (sameDayAttendanceCheckList.size() > 0) {
                return;
            } else {
                for (int n = 0; n < shgDetailsDataList.size(); n++) {
                    AttendanceFlagData attendanceFlagData4 = new AttendanceFlagData();
                    attendanceFlagData4.setUserName(userName);
                    attendanceFlagData4.setParticipantSataus(participantStatus);
                    attendanceFlagData4.setParticipantFlag("NA");
                    attendanceFlagData4.setMorningFlag("NA");
                    attendanceFlagData4.setEeningFlag("NA");
                    attendanceFlagData4.setDateOfAttendanceFlagData(loginDate);
                    attendanceFlagData4.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData4.setMelaId(String.valueOf(melaId));
                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData4);
                }
                memberDataList = SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao().queryBuilder().list();
                for (int j = 0; j < memberDataList.size(); j++) {
                    AttendanceFlagData attendanceFlagData2 = new AttendanceFlagData();
                    attendanceFlagData2.setMelaId(String.valueOf(melaId));
                    attendanceFlagData2.setUserName(memberDataList.get(j).getMember_name());
                    attendanceFlagData2.setParticipantFlag("NA");
                    attendanceFlagData2.setEeningFlag("NA");
                    attendanceFlagData2.setMorningFlag("NA");
                    attendanceFlagData2.setParticipantSataus("MAIN");
                    attendanceFlagData2.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData2.setDateOfAttendanceFlagData(loginDate);

                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData2);
                }

                for (int k = 0; k < memberDataList.size(); k++) {
                    AttendanceFlagData attendanceFlagData3 = new AttendanceFlagData();
                    attendanceFlagData3.setUserName(memberDataList.get(k).getAss_name());
                    attendanceFlagData3.setParticipantFlag("NA");
                    attendanceFlagData3.setMorningFlag("NA");
                    attendanceFlagData3.setEeningFlag("NA");
                    attendanceFlagData3.setParticipantSataus("HELPER");
                    attendanceFlagData3.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData3.setDateOfAttendanceFlagData(loginDate);
                    attendanceFlagData3.setMelaId(String.valueOf(melaId));

                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData3);
                }
                AppUtility.getInstance().showLog("getDataFromDataBase:Size..." + attendanceFlagData.size() + "---" + attendanceFlagData.toString(), OTPverification.class);
            }

        }
        if (participantStatus.equalsIgnoreCase("SC")){

            QueryBuilder<AttendanceFlagData> attendanceFlagDataQueryBuilder = SplashScreen.getInstance()
                    .getDaoSession()
                    .getAttendanceFlagDataDao()
                    .queryBuilder();
            List<AttendanceFlagData> sameDayAttendanceCheckList = attendanceFlagDataQueryBuilder
                    .where(AttendanceFlagDataDao.Properties.ShgRegIdAttendanceFlagData.eq(String.valueOf(shgRegistrationId))
                            , attendanceFlagDataQueryBuilder.and(AttendanceFlagDataDao.Properties.ShgRegIdAttendanceFlagData.eq(String.valueOf(shgRegistrationId))
                                    , AttendanceFlagDataDao.Properties.DateOfAttendanceFlagData.eq(loginDate)))
                    .build()
                    .list();
            if (sameDayAttendanceCheckList.size() > 0) {
                return;
            } else {
                for (int n = 0; n < shgDetailsDataList.size(); n++) {
                    AttendanceFlagData attendanceFlagData4 = new AttendanceFlagData();
                    attendanceFlagData4.setUserName(shgDetailsDataList.get(n).getUser_name());
                    attendanceFlagData4.setParticipantSataus(participantStatus);
                    attendanceFlagData4.setParticipantFlag("NA");
                    attendanceFlagData4.setMorningFlag("NA");
                    attendanceFlagData4.setEeningFlag("NA");
                    attendanceFlagData4.setDateOfAttendanceFlagData(loginDate);
                    attendanceFlagData4.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData4.setMelaId(String.valueOf(melaId));
                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData4);
                }

                memberDataList = SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao().queryBuilder().list();
                for (int j = 0; j < memberDataList.size(); j++) {
                    AttendanceFlagData attendanceFlagData2 = new AttendanceFlagData();
                    attendanceFlagData2.setMelaId(String.valueOf(melaId));
                    attendanceFlagData2.setUserName(memberDataList.get(j).getMember_name());
                    attendanceFlagData2.setParticipantFlag("NA");
                    attendanceFlagData2.setMorningFlag("NA");
                    attendanceFlagData2.setEeningFlag("NA");
                    attendanceFlagData2.setParticipantSataus("MAIN");

                    attendanceFlagData2.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData2.setDateOfAttendanceFlagData(loginDate);

                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData2);
                }

                for (int k = 0; k < memberDataList.size(); k++) {
                    AttendanceFlagData attendanceFlagData3 = new AttendanceFlagData();
                    attendanceFlagData3.setUserName(memberDataList.get(k).getAss_name());
                    attendanceFlagData3.setParticipantFlag("NA");
                    attendanceFlagData3.setEeningFlag("NA");
                    attendanceFlagData3.setMorningFlag("NA");
                    attendanceFlagData3.setParticipantSataus("HELPER");
                    attendanceFlagData3.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                    attendanceFlagData3.setDateOfAttendanceFlagData(loginDate);
                    attendanceFlagData3.setMelaId(String.valueOf(melaId));

                    SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData3);
                }
                AppUtility.getInstance().showLog("getDataFromDataBase:Size..." + attendanceFlagData.size() + "---" + attendanceFlagData.toString(), OTPverification.class);
            }
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
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();*/
       /* Intent intent=new Intent(MpinActivity.this,LoginActivity.class);
        startActivity(intent);*/
        finish();
    }
}
