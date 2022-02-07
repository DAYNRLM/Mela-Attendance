package com.example.melaAttendance.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.AttendanceActivity;
import com.example.melaAttendance.activity.HomeActivity;
import com.example.melaAttendance.activity.OTPverification;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.database.AttendanceFlagData;
import com.example.melaAttendance.database.AttendanceFlagDataDao;
import com.example.melaAttendance.database.ShgDetailsData;
import com.example.melaAttendance.database.ShgMemberDetailsData;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;
import com.example.melaAttendance.model.SelectAttedenceType;
import com.example.melaAttendance.model.ShgData;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.Cryptography;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.GPSTracker;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SelectShgAdapter extends RecyclerView.Adapter<SelectShgAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<ShgsDetailsOnLoginData> shgsDetailsOnLoginDataList;
    List<ShgsDetailsOnLoginData> shgDetailForSearch;              //List which is use to filter the SHG
    String mobileNumber,shgName;
    private List<AttendanceFlagData> attendanceFlagData;
    private List<ShgMemberDetailsData> memberDataList;
    String participantStatus;
    String userName,stallNo;
    int melaId,shgRegId;
    String latitude="";
    String longitude="";
    RecyclerView selectAttedenceRV;
    List<Object> selectAttedenceTypes=new ArrayList();

    public SelectShgAdapter(Context context, List<ShgsDetailsOnLoginData> shgsDetailsOnLoginDataList) {
        this.context = context;
        this.shgsDetailsOnLoginDataList = shgsDetailsOnLoginDataList;
        this.shgDetailForSearch=new ArrayList<>(shgsDetailsOnLoginDataList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shg_select_custom_layout, parent, false);
        return new SelectShgAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.shgName.setText(shgsDetailsOnLoginDataList.get(position).getShgName());
        holder.shgCode.setText(shgsDetailsOnLoginDataList.get(position).getShgCode());
        holder.shgMobile.setText(shgsDetailsOnLoginDataList.get(position).getLoginMobilenumber());
        /*holder.shgRegId.setText(""+shgsDetailsOnLoginDataList.get(position).getShgRegistrationId());*/
        holder.shgStallno.setText(shgsDetailsOnLoginDataList.get(position).getStallNo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=DialogFactory.getInstance().showProgressDialog(context);
                progressDialog.show();
                selectAttedenceTypes.clear();

                shgRegId= shgsDetailsOnLoginDataList.get(position).getShgRegistrationId();
                AppUtility.getInstance().showLog("shgRegId"+shgRegId,SelectShgAdapter.class);
                mobileNumber=shgsDetailsOnLoginDataList.get(position).getLoginMobilenumber();
                shgName=shgsDetailsOnLoginDataList.get(position).getShgName();
                stallNo=shgsDetailsOnLoginDataList.get(position).getStallNo();
                saveInPref(shgRegId,shgName);
                GPSTracker gpsTracker = new GPSTracker((context));
                if(!AppUtility.isInternetOn(context)){
                    DialogFactory.getInstance().showAlertDialog(context, R.drawable.ic_launcher_background, "Mela","Gps is not enabled", "Go to seeting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    }, "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    },false);
                }else {
                    gpsTracker.getLocation();
                    latitude = String.valueOf(gpsTracker.latitude);
                    longitude = String.valueOf(gpsTracker.longitude);

                    //  AddTrainingPojo.addTrainingPojo.setGpsLoation(latitude + "lat"+"," + longitude+"long");
                    AppUtility.getInstance().showLog("location" + latitude + "  " + longitude, SelectShgAdapter.class);

                }
               /* String SHG_DATA_URL=AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/nrlmwebservice/services/mela/data?melaId=3&shgRegId="+shgRegId;
                AppUtility.getInstance().showLog("SHG_DATA_URL"+SHG_DATA_URL, SelectAttedenceType.class);

                 StringRequest stringRequest=new StringRequest(Request.Method.GET, SHG_DATA_URL, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         AppUtility.getInstance().showLog("shgdata"+response,SelectShgAdapter.class);
                         parseData(response,progressDialog);
                         progressDialog.dismiss();

                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         progressDialog.dismiss();
                         AppUtility.getInstance().showLog("error"+error,SelectShgAdapter.class);
                         DialogFactory.getInstance().showServerErrorDialog(context,"Server Error!!!","OK");
                     }
                 });

                 RequestQueue requestQueue=Volley.newRequestQueue(context);
                 requestQueue.add(stringRequest);*/


                String SHG_DATA_URL=AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/mela/data";
                AppUtility.getInstance().showLog("SHG_DATA_URL"+SHG_DATA_URL, SelectAttedenceType.class);
                 JSONObject shgDataUrlInfo=new JSONObject();
                try {

                    shgDataUrlInfo.accumulate("melaId",AppConstants.melaId);
                    shgDataUrlInfo.accumulate("shgRegId",""+shgRegId);
                    shgDataUrlInfo.accumulate("mobile",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyShgMobile(),context));
                    shgDataUrlInfo.accumulate("imei_no",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyImei(),context));
                    shgDataUrlInfo.accumulate("device_name",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyDeviceInfo(),context));
                    shgDataUrlInfo.accumulate("location_coordinate",latitude+","+longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();
                    String encrypted=cryptography.encrypt(shgDataUrlInfo.toString());
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
                JsonObjectRequest memberData=new JsonObjectRequest(Request.Method.POST, SHG_DATA_URL,encryptedObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            AppUtility.getInstance().showLog("shgdata"+response,SelectShgAdapter.class);
                            Cryptography cryptography = null;
                            String objectResponse="";
                            if(response.has("data")){
                                objectResponse=response.getString("data");
                            }else {
                                return;
                            }
                            try {
                                cryptography = new Cryptography();
                                response = new JSONObject(cryptography.decrypt(objectResponse));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            parseData(response,progressDialog);
                            progressDialog.dismiss();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AppUtility.getInstance().showLog("error"+error,SelectShgAdapter.class);
                        DialogFactory.getInstance().showServerErrorDialog(context,"Server Error!!!","OK");
                    }
                });

                RequestQueue requestQueue=Volley.newRequestQueue(context);
                requestQueue.add(memberData);

              /*  clearingAllTableOfLocalDBExceptShgDetailsOnLoginData();
                getShgAndMemberDataFromServer();*/

            }
        });


    }

    private void parseData(JSONObject response,ProgressDialog progressDialog){
        ShgData shgData=ShgData.getInstance();
        ShgData.CrpData crpData=new ShgData.CrpData();
        ShgData.ScData scData=new ShgData.ScData();
        ShgData.MpData mpData=new ShgData.MpData();
        ShgData.HelperData helperData=new ShgData.HelperData();
        if (response.toString().equalsIgnoreCase("{}")){
            progressDialog.dismiss();
            DialogFactory.getInstance().showServerErrorDialog(context,"No Data Found!!!","OK");
        }else {

            try {

                shgData.setShgRegId(response.getString("shg_reg_id"));
                shgData.setShgCode(response.getString("shg_code"));
                shgData.setShgName(response.getString("shg_name"));

                JSONObject crpObject = response.getJSONObject("CRP_data");
                if (crpObject.has("status")) {
                    crpData.setDataStatus(crpObject.getString("status"));
                } else {
                    crpData.setClosing(crpObject.getInt("closing"));
                    crpData.setOpening(crpObject.getInt("opening"));
                    crpData.setMobile(crpObject.getString("CRP_mobile"));
                    crpData.setUserName(crpObject.getString("user_name"));
                    crpData.setParticipantStatus(crpObject.getString("participant_status"));
                    selectAttedenceTypes.add(crpData);
                }


                JSONObject scObject = response.getJSONObject("sc_data");
                if (scObject.has("status")) {
                    scData.setDataStatus(scObject.getString("status"));
                } else {
                    scData.setClosing(scObject.getInt("closing"));
                    scData.setOpening(scObject.getInt("opening"));
                    scData.setMobile(scObject.getString("CRP_mobile"));
                    scData.setUserName(scObject.getString("user_name"));
                    scData.setParticipantStatus(scObject.getString("participant_status"));
                    selectAttedenceTypes.add(scData);
                }
                JSONObject mainPData = response.getJSONObject("main_participant_data");
                if (mainPData.has("status")) {
                    mpData.setDataStatus(mainPData.getString("status"));
                } else {
                    mpData.setClosing(mainPData.getInt("closing"));
                    mpData.setOpening(mainPData.getInt("opening"));
                    mpData.setMobile(mainPData.getString("CRP_mobile"));
                    mpData.setUserName(mainPData.getString("user_name"));
                    mpData.setParticipantStatus(mainPData.getString("participant_status"));
                    selectAttedenceTypes.add(mpData);
                }


                JSONObject helperObject = response.getJSONObject("helper_data");
                if (helperObject.has("status")) {
                    helperData.setDataStatus(helperObject.getString("status"));
                } else {
                    helperData.setClosing(helperObject.getInt("closing"));
                    helperData.setOpening(helperObject.getInt("opening"));
                    helperData.setMobile(helperObject.getString("CRP_mobile"));
                    helperData.setUserName(helperObject.getString("user_name"));
                    helperData.setParticipantStatus(helperObject.getString("participant_status"));
                    selectAttedenceTypes.add(helperData);
                }

                openDialog(shgData);

            } catch (JSONException je) {
                AppUtility.getInstance().showLog("JSONexception" + je, SelectShgAdapter.class);
            }
        }

    }

    @Override
    public int getItemCount() {
        return shgsDetailsOnLoginDataList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShgsDetailsOnLoginData> filteredList = new ArrayList<>();
            if (constraint.toString().trim().toLowerCase().isEmpty()) {
                filteredList.addAll(shgDetailForSearch);
            } else {
                for (ShgsDetailsOnLoginData shg : shgDetailForSearch) {
                    if (shg.getShgName().toString().trim().toLowerCase()
                            .contains(constraint.toString().trim().toLowerCase())) {
                        filteredList.add(shg);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shgsDetailsOnLoginDataList.clear();
            shgsDetailsOnLoginDataList.addAll((Collection<? extends ShgsDetailsOnLoginData>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shgName,shgCode,shgMobile,shgRegId,shgStallno;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shgName = (TextView)itemView.findViewById(R.id.cardViewSelectShgName);
            shgCode = (TextView)itemView.findViewById(R.id.cardViewSelectShgCode);
            shgMobile = (TextView)itemView.findViewById(R.id.cardViewSelectShgMobile);
            shgRegId = (TextView)itemView.findViewById(R.id.cardViewSelectShgRegId);
            shgStallno = (TextView)itemView.findViewById(R.id.cardViewSelectStallNumber);
        }
    }

    private void openDialog(ShgData shgData) {
        getAttendanceData();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.attedenc_type_custom_dialog);
        selectAttedenceRV = (RecyclerView)dialog.findViewById(R.id. selectAttedenceRV );
        SelectAttedenceTypeAdapter selectAttedenceTypeAdapter = new SelectAttedenceTypeAdapter(selectAttedenceTypes,context,dialog,shgData.getShgRegId());
        selectAttedenceRV.setLayoutManager(new LinearLayoutManager(context));
        selectAttedenceRV.setAdapter(selectAttedenceTypeAdapter);
        selectAttedenceTypeAdapter.notifyDataSetChanged();
        dialog.show();
    }

    private void getAttendanceData() {
        /*selectAttedenceTypes*/
    }

    private void saveInPref(int shgRegId,String shgName) {
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyShgRegId(),shgRegId , context);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefShgNameForProfile(),shgName,context);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData("stall",stallNo, context);
    }
}
