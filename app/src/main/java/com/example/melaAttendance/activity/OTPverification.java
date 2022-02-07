package com.example.melaAttendance.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class OTPverification extends AppCompatActivity {
    Button getOTP;
    private EditText enterOtpET;
    private String enteredOTP;
    private String mNumber;
    private Bundle bundle;
    private Context context;
    private String selfGeneratedOTP;
    private int shgRegistrationId;
    private String SHG_DETAILS_URL="https://nrlm.gov.in/nrlmwebservice/services/mela/data?shgRegId=3&mobile=9034930094";
    private List<AttendanceFlagData> attendanceFlagData;
    private List<ShgMemberDetailsData> memberDataList;
    String participantStatus;
    String userName;
    int melaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        context=getBaseContext();
        enterOtpET=(EditText)findViewById(R.id.enter_otpET);
        getOTP = (Button)findViewById(R.id.btnGetOtp);
        bundle=getIntent().getExtras();
        shgRegistrationId=bundle.getInt(PreferenceManager.getShgRegistrationId());
        AppUtility.getInstance().showLog("shgregid"+shgRegistrationId,OTPverification.class);
        mNumber=bundle.getString("mNumber");
        AppUtility.getInstance().showLog("mNumber"+mNumber,OTPverification.class);
        selfGeneratedOTP = ProjectPrefrences.getInstance().getSharedPrefrencesData(mNumber, context);
        Toast.makeText(OTPverification.this, "Your 4 digit OTP is:-"+selfGeneratedOTP, Toast.LENGTH_LONG).show();
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OTPverification.this, ""+selfGeneratedOTP, Toast.LENGTH_LONG).show();
                String FINAL_SHG_DETAILS_URL="https://nrlm.gov.in/nrlmwebservice/services/mela/data?shgRegId="+shgRegistrationId+"&mobile="+mNumber;
                enteredOTP = enterOtpET.getText().toString().trim();
                Toast.makeText(OTPverification.this, ""+selfGeneratedOTP, Toast.LENGTH_LONG).show();
                if (enteredOTP.equalsIgnoreCase(selfGeneratedOTP)) {
                    getShgAndMemberDataFromServer();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData("usernamemm",userName,OTPverification.this);

                } else {
                    Toast.makeText(OTPverification.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getShgAndMemberDataFromServer()
    {
        ProgressDialog progressDialog=new ProgressDialog(OTPverification.this);
        progressDialog.setMessage("Loading...");
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

                            ProjectPrefrences.getInstance().removeSharedPrefrencesData(mNumber, context);
                            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyLoginDone(), "ok", context);

                            getCrpShgAndMembersDataFromLocalDB();
                            progressDialog.dismiss();
                            Intent intent = new Intent(OTPverification.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtras(bundle);
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
                Toast.makeText(OTPverification.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
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
                        attendanceFlagData2.setParticipantSataus("MAIN");
                        attendanceFlagData2.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                        attendanceFlagData2.setDateOfAttendanceFlagData(loginDate);

                        SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData2);
                    }

                    for (int k = 0; k < memberDataList.size(); k++) {
                        AttendanceFlagData attendanceFlagData3 = new AttendanceFlagData();
                        attendanceFlagData3.setUserName(memberDataList.get(k).getAss_name());
                        attendanceFlagData3.setParticipantFlag("NA");
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
                            attendanceFlagData2.setParticipantSataus("MAIN");
                            attendanceFlagData2.setShgRegIdAttendanceFlagData(String.valueOf(shgRegistrationId));
                            attendanceFlagData2.setDateOfAttendanceFlagData(loginDate);

                            SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().insert(attendanceFlagData2);
                        }

                        for (int k = 0; k < memberDataList.size(); k++) {
                            AttendanceFlagData attendanceFlagData3 = new AttendanceFlagData();
                            attendanceFlagData3.setUserName(memberDataList.get(k).getAss_name());
                            attendanceFlagData3.setParticipantFlag("NA");
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();
        Intent intent=new Intent(OTPverification.this,LoginActivity.class);
        startActivity(intent);
    }
}
