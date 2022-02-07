package com.example.melaAttendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.VolleyError;
import com.example.melaAttendance.R;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import com.example.melaAttendance.volley.VolleyResult;
import com.example.melaAttendance.volley.VolleyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtpVerificationActivity extends AppCompatActivity {
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout confirmPasswordInputLayout;

    @BindView(R.id.passwordEt)
    TextInputEditText passwordEt;

    @BindView(R.id.confirmPasswordEt)
    TextInputEditText confirmPasswordEt;

    @BindView(R.id.updateBtn)
    MaterialButton updateBtn;

    @BindView(R.id.otpEt)
    PinEntryEditText otpEt;

    @BindView(R.id.otpCardview)
    CardView otpCardview;

    AppUtility appUtility;
    VolleyService volleyService;
    DateFactory dateFactory;
    String otpStatus ="";
    String getOtp ="";
    String password ="";
    String confirmPassword ="";
    VolleyResult mResultCallBack=null;
    String resetPasswordUrl=AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/melaattend/resetPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
        dateFactory = DateFactory.getInstance();
        appUtility = AppUtility.getInstance();
        volleyService = VolleyService.getInstance(OtpVerificationActivity.this);
        getOtp= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getOTP(),OtpVerificationActivity.this);
        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(passwordEt.getText().toString().length()<6){
                        passwordInputLayout.setError("Enter 6 digit pasword");
                    }
                }
            }
        });
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInputLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String getPassword =passwordEt.getText().toString();
                String getConfirmPassword = confirmPasswordEt.getText().toString();

                if(getPassword.length()==6){
                    if(!getConfirmPassword.isEmpty()){
                        if(!getPassword.equalsIgnoreCase(getConfirmPassword)){
                            otpCardview.setVisibility(View.GONE);
                            passwordInputLayout.setError("Password is not Matched");
                        }else {
                            otpCardview.setVisibility(View.VISIBLE);
                            passwordInputLayout.setError(null);
                        }
                    }
                }
            }
        });

        confirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordInputLayout.setError(null);
                String getPassword =passwordEt.getText().toString();
                if(getPassword.equalsIgnoreCase("")||getPassword.isEmpty()){
                    passwordInputLayout.setError("Enter 6 digit password");
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                String getPassword =passwordEt.getText().toString();
                String getConfirmPassword = confirmPasswordEt.getText().toString();

                if(getConfirmPassword.length()==6){
                    if(!getPassword.equalsIgnoreCase(getConfirmPassword)){
                        otpCardview.setVisibility(View.GONE);
                        passwordInputLayout.setError("Password is not Matched");
                    }else {
                        otpCardview.setVisibility(View.VISIBLE);
                        passwordInputLayout.setError(null);
                    }
                }
            }
        });

        otpEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String getOtpFromEt = otpEt.getText().toString().trim();
                if(getOtpFromEt.length()==4){
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(getOtpFromEt.equalsIgnoreCase(getOtp)){
                                otpStatus ="1";
                                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getOTP(),"",OtpVerificationActivity.this);

                            }
                            else {
                                otpEt.setText(null);
                                Toast.makeText(OtpVerificationActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },500);
                }
            }
        });
    }


    @OnClick(R.id.updateBtn)
    public void updatePassword(){

        password = passwordEt.getText().toString();
        confirmPassword = confirmPasswordEt.getText().toString();

        if(password.equalsIgnoreCase("")||password.isEmpty()){
            passwordInputLayout.setError("Enter 6 digit password");
        }else if(confirmPassword.equalsIgnoreCase("")||confirmPassword.isEmpty()){
            confirmPasswordInputLayout.setError("Enter Confirm Password");
        }else if(otpStatus.equalsIgnoreCase("")){
            Toast.makeText(OtpVerificationActivity.this,"Please Enter OTP First....",Toast.LENGTH_SHORT).show();

        }else {
            callApiForUpdate();
        }


    }

    private void callApiForUpdate() {

        if(NetworkFactory.isInternetOn(OtpVerificationActivity.this)){
            ProgressDialog progressDialog =new ProgressDialog(OtpVerificationActivity.this);
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject masterUrlObject =new JSONObject();
            try {
                masterUrlObject.accumulate("mobile",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getOtpMobileNumber(),OtpVerificationActivity.this));
                masterUrlObject.accumulate("melaId", AppConstants.melaId);
                masterUrlObject.accumulate("password",confirmPassword);

            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("OTP json making exception:- "+e,LoginActivity.class);
            }

            mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    progressDialog.dismiss();
                    appUtility.showLog("get response:-" +response,LoginActivity.class);
                    //{"data":"","message":"Mobile Number Invalid","status":0}
                    //{"status":"Updated Successfully!!!"}
                    try{
                        if(response.has("status")){
                            String status =response.getString("status");
                            if(status.equalsIgnoreCase("Updated Successfully!!!")){
                                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Password Update Successfully");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(OtpVerificationActivity.this,"Login with new password",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                materialAlertDialogBuilder.show();
                            }else {
                                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Password Is not update try again..");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(OtpVerificationActivity.this,"Password Is not update",Toast.LENGTH_SHORT).show();
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
                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Server Error please try again");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(OtpVerificationActivity.this,"Password Is not update",Toast.LENGTH_SHORT).show();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            };
            volleyService.postDataVolley("otp_update_request",resetPasswordUrl,masterUrlObject,mResultCallBack);
        }
        else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
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
}