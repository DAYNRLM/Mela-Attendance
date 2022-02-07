package com.example.melaAttendance.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.AttendanceActivity;
import com.example.melaAttendance.model.ShgData;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.Cryptography;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.GPSTracker;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SelectAttedenceTypeAdapter extends RecyclerView.Adapter<SelectAttedenceTypeAdapter.MyViewHolder> {
    List<Object> selectAttedenceTypes;
    Context context;
    Bundle bundle;
    ProgressDialog progressDialog;
    Dialog dialog;
    String participantStatus,shgMobileNumber,status,participantName,participantMobile;
    String shgRegistrationId;
   String latitude ="";
   String longitude="";

    public SelectAttedenceTypeAdapter(List<Object> selectAttedenceTypes, Context context, Dialog dialog, String shgRegId) {
        this.selectAttedenceTypes = selectAttedenceTypes;
        this.context = context;
        this.dialog = dialog;
        this.shgRegistrationId=shgRegId;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attedence_select_custom_layout, parent, false);
        return new SelectAttedenceTypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

              Object object= selectAttedenceTypes.get(position);
              AppUtility.getInstance().showLog("object"+object,SelectShgAdapter.class);

              if (object.getClass()== ShgData.CrpData.class){
                  ShgData.CrpData crpData=(ShgData.CrpData)object;
                  AppUtility.getInstance().showLog("crpData"+crpData,SelectShgAdapter.class);
                  if (!(crpData.getDataStatus()!=null && !crpData.getDataStatus().equalsIgnoreCase(""))){
                      holder.name.setText(crpData.getParticipantStatus()+":"+crpData.getUserName());
                      if (crpData.getOpening()!=0)
                          holder.openingDone.setVisibility(View.VISIBLE);
                      if (crpData.getClosing()!=0)
                          holder.closingingDoneRB.setVisibility(View.VISIBLE);
                  }
              }
              if (object.getClass()==ShgData.ScData.class){
                  ShgData.ScData scData=(ShgData.ScData)object;
                  AppUtility.getInstance().showLog("scData"+scData,SelectShgAdapter.class);
                  if (!(scData.getDataStatus()!=null && !scData.getDataStatus().equalsIgnoreCase(""))){
                      holder.name.setText(scData.getParticipantStatus()+":"+scData.getUserName());
                      if (scData.getOpening()!=0)
                          holder.openingDone.setVisibility(View.VISIBLE);
                      if (scData.getClosing()!=0)
                          holder.closingingDoneRB.setVisibility(View.VISIBLE);
                  }
              }
              if (object.getClass()==ShgData.MpData.class){
                  ShgData.MpData mpData=(ShgData.MpData)object;
                  AppUtility.getInstance().showLog("mpData"+mpData,SelectShgAdapter.class);
                  if (!(mpData.getDataStatus()!=null && !mpData.getDataStatus().equalsIgnoreCase(""))){
                      holder.name.setText(mpData.getParticipantStatus()+":"+mpData.getUserName());
                      if (mpData.getOpening()!=0)
                          holder.openingDone.setVisibility(View.VISIBLE);
                      if (mpData.getClosing()!=0)
                          holder.closingingDoneRB.setVisibility(View.VISIBLE);
                  }
              }
              if (object.getClass()==ShgData.HelperData.class){
                  ShgData.HelperData helperData=(ShgData.HelperData)object;
                  AppUtility.getInstance().showLog("helperData"+helperData,SelectShgAdapter.class);
                  if (!(helperData.getDataStatus()!=null && !helperData.getDataStatus().equalsIgnoreCase(""))){
                      holder.name.setText(helperData.getParticipantStatus()+":"+helperData.getUserName());
                      if (helperData.getOpening()!=0)
                          holder.openingDone.setVisibility(View.VISIBLE);
                      if (helperData.getClosing()!=0)
                          holder.closingingDoneRB.setVisibility(View.VISIBLE);
                  }
              }

         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantShgregidForSetAttendance(),shgRegistrationId,context);
                shgMobileNumber= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyShgMobile(),context);
                AppUtility.getInstance().showLog("shgMobileNumber:--"+shgMobileNumber,SelectAttedenceTypeAdapter.class);
                AppUtility.getInstance().showLog("shgRegistrationId:--"+shgRegistrationId,SelectAttedenceTypeAdapter.class);

                Object clickedObject=selectAttedenceTypes.get(position);
                if (clickedObject.getClass()== ShgData.CrpData.class){
                    ShgData.CrpData crpData=(ShgData.CrpData)clickedObject;
                    participantStatus= crpData.getParticipantStatus();
                    participantName=crpData.getUserName();
                    participantMobile=crpData.getMobile();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getParticipantMobile(),participantMobile,context);
                    AppUtility.getInstance().showLog("participantName:--"+participantName,SelectAttedenceTypeAdapter.class);
                    AppUtility.getInstance().showLog("participantStatus:--"+participantStatus,SelectAttedenceTypeAdapter.class);
                }
                else if (clickedObject.getClass()==ShgData.ScData.class){
                    ShgData.ScData scData=(ShgData.ScData)clickedObject;
                    participantStatus= scData.getParticipantStatus();
                    participantName=scData.getUserName();
                    participantMobile=scData.getMobile();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getParticipantMobile(),participantMobile,context);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleUsername(),participantName,context);
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantStatusForSetAttendance(),participantStatus,context);
                    AppUtility.getInstance().showLog("participantName:--"+participantName,SelectAttedenceTypeAdapter.class);
                    AppUtility.getInstance().showLog("participantStatus:--"+participantStatus,SelectAttedenceTypeAdapter.class);
                }
                else if (clickedObject.getClass()==ShgData.MpData.class){
                    ShgData.MpData mpData=(ShgData.MpData)clickedObject;
                    participantStatus= mpData.getParticipantStatus();
                    participantName=mpData.getUserName();
                    participantMobile=mpData.getMobile();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getParticipantMobile(),participantMobile,context);
                    AppUtility.getInstance().showLog("participantName:--"+participantName,SelectAttedenceTypeAdapter.class);
                    AppUtility.getInstance().showLog("participantStatus:--"+participantStatus,SelectAttedenceTypeAdapter.class);
                }
                else if (clickedObject.getClass()==ShgData.HelperData.class){
                    ShgData.HelperData helperData=(ShgData.HelperData)clickedObject;
                    participantStatus= helperData.getParticipantStatus();
                    participantName=helperData.getUserName();
                    participantMobile=helperData.getMobile();
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getParticipantMobile(),participantMobile,context);
                    AppUtility.getInstance().showLog("participantName:--"+participantName,SelectAttedenceTypeAdapter.class);
                    AppUtility.getInstance().showLog("participantStatus:--"+participantStatus,SelectAttedenceTypeAdapter.class);
                }
                else {
                    Toast.makeText(context,"No status Found.",Toast.LENGTH_SHORT).show();
                }
                /*String url = AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/nrlmwebservice/services/melaattandences/status?participantStatus="+participantStatus+"&shgRegId="+shgRegistrationId+"&melaId=3&mobile="+shgMobileNumber;
                AppUtility.getInstance().showLog("shgURL:--"+url,SelectAttedenceTypeAdapter.class);*/
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleUsername(),participantName,context);
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantStatusForSetAttendance(),participantStatus,context);
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleParticipantShgregidForSetAttendance(),shgRegistrationId,context);
                getAttedenceStatusFromServer( participantStatus,shgRegistrationId ,shgMobileNumber);
            }
        });
    }

    private void getAttedenceStatusFromServer(String staus,String id,String mobileNumber) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //String url = "https://nrlm.gov.in/nrlmwebservice/services/melaattandences/status?participantStatus="+staus+"&shgRegId="+id;
        String url = AppConstants.HTTP_TYPE + "://" + AppConstants.IP_ADDRESS +"/"+AppConstants.API_TYPE+"/services/melaattandences/status";
        AppUtility.getInstance().showLog("url" + url, SelectAttedenceTypeAdapter.class);

        JSONObject statusRequestPayload = new JSONObject();
        try {
            statusRequestPayload.accumulate("participantStatus", staus);
            statusRequestPayload.accumulate("shgRegId", id);
            statusRequestPayload.accumulate("melaId", AppConstants.melaId);
            statusRequestPayload.accumulate("mobile", mobileNumber);
            statusRequestPayload.accumulate("date", AppUtility.getInstance().changeDateValue(DateFactory.getInstance().getTodayDate()));
            statusRequestPayload.accumulate("imei_no", ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyImei(), context));
            statusRequestPayload.accumulate("device_name", ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyDeviceInfo(), context));
            statusRequestPayload.accumulate("location_coordinate", latitude + "," + longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject encryptedObject =new JSONObject();
        try {
            Cryptography cryptography = new Cryptography();
            String encrypted=cryptography.encrypt(statusRequestPayload.toString());
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

        JsonObjectRequest memberStatusRequest = new JsonObjectRequest(Request.Method.POST, url, encryptedObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject s) {
                AppUtility.getInstance().showLog("s" + s, SelectAttedenceTypeAdapter.class);
                progressDialog.dismiss();
                JSONArray   sa=null;
                try {
                    Cryptography cryptography = null;
                    String objectResponse="";
                    if(s.has("data")){
                        objectResponse=s.getString("data");
                    }else {
                        return;
                    }
                    try {
                        cryptography = new Cryptography();
                        String saa=cryptography.decrypt(objectResponse);
                        sa = new JSONArray(saa);
                        Log.d("TAG"+s,"data_value");

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    JSONArray mainArr = new JSONArray(sa.toString());
                    if (mainArr.length() == 0) {
                        Toast.makeText(context, "Server Error! Try after sometime.", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < mainArr.length(); i++) {
                            JSONObject obj = mainArr.getJSONObject(i);
                            if (obj.has("dateStatus")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Info");
                                builder.setMessage(obj.getString("dateStatus"));
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            } else {
                                String openingLocation = obj.getString("opening_location");
                                String lastClosingTime = obj.getString("last closing time");
                                String lastClosingLocation = obj.getString("closing_location");
                                String openingTime = obj.getString("opening time");
                                status = obj.getString("status");
                                AppUtility.getInstance().showLog("openingLocation--" + openingLocation + "lastClosingTime--" + lastClosingTime + "openingTime--" + openingTime + "status--" + status + "closing location--" + lastClosingLocation, SelectAttedenceTypeAdapter.class);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceOpeningLocation(), openingLocation, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceLastClosingTime(), lastClosingTime, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceClosingLocation(), lastClosingLocation, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceOpeningTime(), openingTime, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceStatus(), status, context);
                                Toast.makeText(context, "status" + status, Toast.LENGTH_SHORT).show();
                                if (status.equalsIgnoreCase("Opening") || status.equalsIgnoreCase("Closing")) {
                                    Intent intent = new Intent(context, AttendanceActivity.class);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Info");
                                    builder.setMessage(status);
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                          /*  Intent intent = new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);*/
                                    /* dialog.dismiss();*/
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogFactory.getInstance().showServerErrorDialog(context, "Server Error!!!", "OK");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(memberStatusRequest);

    }


 /*       StringRequest stringRequest=new StringRequest(Request.Method.GET,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                AppUtility.getInstance().showLog("s"+s,SelectAttedenceTypeAdapter.class);
                progressDialog.dismiss();

                try {
                    JSONArray mainArr=new JSONArray(s);
                    if(mainArr.length()==0){
                        Toast.makeText(context, "Server Error! Try after sometime.", Toast.LENGTH_SHORT).show();
                    }else {

                        for (int i = 0; i < mainArr.length(); i++) {
                            JSONObject obj = mainArr.getJSONObject(i);
                            if (obj.has("dateStatus")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Info");
                                builder.setMessage(obj.getString("dateStatus"));
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }else {
                                String openingLocation = obj.getString("opening_location");
                                String lastClosingTime = obj.getString("last closing time");
                                String lastClosingLocation = obj.getString("closing_location");
                                String openingTime = obj.getString("opening time");
                                status = obj.getString("status");
                                AppUtility.getInstance().showLog("openingLocation--" + openingLocation + "lastClosingTime--" + lastClosingTime + "openingTime--" + openingTime + "status--" + status + "closing location--" + lastClosingLocation, SelectAttedenceTypeAdapter.class);

                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceOpeningLocation(), openingLocation, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceLastClosingTime(), lastClosingTime, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceClosingLocation(), lastClosingLocation, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceOpeningTime(), openingTime, context);
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getBundleAttedenceStatus(), status, context);
                                Toast.makeText(context, "status" + status, Toast.LENGTH_SHORT).show();


                                if (status.equalsIgnoreCase("Opening") || status.equalsIgnoreCase("Closing")) {
                                    Intent intent = new Intent(context, AttendanceActivity.class);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Info");
                                    builder.setMessage(status);
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                          *//*  Intent intent = new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);*//*
                                    *//* dialog.dismiss();*//*
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogFactory.getInstance().showServerErrorDialog(context,"Server Error!!!","OK");
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);*/


    @Override
    public int getItemCount() {
        return selectAttedenceTypes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView openingDone,closingingDoneRB;
        CheckBox inTime,outTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tvAttedenceTypeName);
            openingDone=(ImageView)itemView.findViewById(R.id.openingDoneRB);
            closingingDoneRB = (ImageView)itemView.findViewById(R.id.closingingDoneRB);
        }
    }
}
