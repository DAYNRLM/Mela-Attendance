package com.example.melaAttendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.melaAttendance.R;
import com.example.melaAttendance.adapter.SelectShgAdapter;
import com.example.melaAttendance.database.AttendanceFlagData;
import com.example.melaAttendance.database.AttendanceFlagDataDao;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.Cryptography;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.GPSTracker;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import com.google.gson.JsonObject;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class AttendanceActivity extends AppCompatActivity implements LocationListener {
    ImageView ivAttedenceProfile, imgBack;
    Button btnAttedenceTakePic, btnAttendence;
    TextView tvAttendenceDate, out_time, out_address, shg_nameTV;
    TextView tvAttendanceAddress, tvAttendenceInTime;
    Intent intent;
    Double lati, longi;
    Geocoder geocoder;
    List<Address> addresses;
    ProgressDialog pd;
    String address="", today_date, time, time_stamp, base_64;
    String out_time_stamp;
    String latitude = "";
    String longitude="";
    public static final int RequestPermissionCode = 1;
    ProgressDialog progressDialog;
    JSONObject responseobj;
    Bundle bundle;
    final String TAG = "tag";
    String userNameFromBundle, autoIDFromBundle, flagFromBundle, participantStatus, attedenceOpeningLocation, attedenceOpeningTime, attedenceLastClosingTime, attedenceStatus, attedenceLastClosingLocation, attedenceMobileNumber, attendanceAutoGeneratedId;
    int melaIdFromBundle, shgRegIdFromBundle;
    private CardView openingDetailsCV, closingDetailsCV;
    String URL = "http://10.24.16.2:8080/nrlmwebservice/services/mela/bill?melaId=3&shgRegId=3&productId=1,2,4&productPrice=150,90,20&productQty=2,1,5";
Handler handler;
Runnable runnable;
    // LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();
        /*vcs switched*/
        progressDialog = new ProgressDialog(AttendanceActivity.this);
        ivAttedenceProfile = (ImageView) findViewById(R.id.pictureIV);
        btnAttedenceTakePic = (Button) findViewById(R.id.take_pictureBtn);
        tvAttendanceAddress = (TextView) findViewById(R.id.in_locationTV);
        tvAttendenceDate = (TextView) findViewById(R.id.tvAttendenceDate);
        tvAttendenceInTime = (TextView) findViewById(R.id.in_timeTV);
        btnAttendence = (Button) findViewById(R.id.punch_attandanceBtn);
        shg_nameTV = (TextView) findViewById(R.id.shg_nameTV);
        out_time = (TextView) findViewById(R.id.out_timeTV);
        out_address = (TextView) findViewById(R.id.out_locationTV);
        openingDetailsCV = (CardView) findViewById(R.id.opening_detailsCV);
        closingDetailsCV = (CardView) findViewById(R.id.closing_detailsCV);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnAttendence.setEnabled(true);
        // EnableRuntimePermission();
        if (CheckingPermissionIsEnabledOrNot()) {
            //Toast.makeText(AttendanceActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        } else {
            RequestMultiplePermission();
        }
        melaIdFromBundle = Integer.parseInt(ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleParticipantMelaidForSetAttendance(),AttendanceActivity.this));
        shgRegIdFromBundle = Integer.parseInt(ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleParticipantShgregidForSetAttendance(),AttendanceActivity.this));

        userNameFromBundle= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleUsername(),AttendanceActivity.this);
        participantStatus=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleParticipantStatusForSetAttendance(),AttendanceActivity.this);
        attedenceMobileNumber = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getParticipantMobile(),AttendanceActivity.this);

        attedenceOpeningLocation= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleAttedenceOpeningLocation(),AttendanceActivity.this);
        attedenceLastClosingTime= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleAttedenceLastClosingTime(),AttendanceActivity.this);
        attedenceLastClosingLocation=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleAttedenceClosingLocation(),AttendanceActivity.this);
        attedenceOpeningTime=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleAttedenceOpeningTime(),AttendanceActivity.this);
        attedenceStatus= ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getBundleAttedenceStatus(),AttendanceActivity.this);

        AppUtility.getInstance().showLog("openingtime" + attedenceOpeningLocation + "attedenceOpeningTime" + attedenceOpeningTime + "attedenceLastClosingTime" + attedenceLastClosingTime + "attedenceStatus" + attedenceStatus, AttendanceActivity.class);

        shg_nameTV.setText(userNameFromBundle);
        String date = DateFactory.getInstance().getTodayDate();
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyNextDayDate(), date, AttendanceActivity.this);

        if (attedenceStatus.equalsIgnoreCase("Opening")) {

            closingDetailsCV.setVisibility(View.GONE);
            btnAttendence.setText("PUNCH IN");
            btnAttedenceTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckingPermissionIsEnabledOrNot()) {
                        // Toast.makeText(AttendanceActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        getLocation();
                        getCurrentTimeDate();
                    } else {
                        // RequestMultiplePermission();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                        builder.setTitle("Permitions Not Granted");
                        builder.setMessage("Please allow all permitions.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
        if (attedenceStatus.equalsIgnoreCase("Closing")) {
            openingDetailsCV.setVisibility(View.VISIBLE);
            btnAttendence.setText("PUNCH OUT");
            tvAttendenceInTime.setText(attedenceOpeningTime);
            tvAttendanceAddress.setText(attedenceOpeningLocation);

            btnAttedenceTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckingPermissionIsEnabledOrNot()) {
                        // Toast.makeText(AttendanceActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        getLocationOut();
                        getCurrentTimeDateOut();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                        builder.setTitle("Permitions Not Granted");
                        builder.setMessage("Please allow all permitions.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
            });
        }

        btnAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // btnAttendence.setEnabled(false);
                if (CheckingPermissionIsEnabledOrNot()) {
                    // Toast.makeText(AttendanceActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                    if (base_64 != null ) {
                        if (attedenceStatus.equalsIgnoreCase("Opening")) {
                            btnAttendence.setEnabled(false);
                            submitForInTime();

                        }
                        if (attedenceStatus.equalsIgnoreCase("Closing")) {
                            btnAttendence.setEnabled(false);
                            submitForOutTime();

                        }
                    } else {
                        Toast.makeText(AttendanceActivity.this,
                                "Please Wait until we get your current location", Toast.LENGTH_LONG).show();
                        getCurrentTimeDate();
                        getLocationOut();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                    builder.setTitle("Permitions Not Granted");
                    builder.setMessage("Please allow all permitions.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }
   @Override
    protected void onResume() {
        super.onResume();
        GPSTracker gpsTracker = new GPSTracker((AttendanceActivity.this));
        if(!AppUtility.isInternetOn(AttendanceActivity.this)){
            DialogFactory.getInstance().showAlertDialog(AttendanceActivity.this, R.drawable.ic_launcher_background, "Mela","Gps is not enabled", "Go to seeting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    AttendanceActivity.this.startActivity(intent);
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
    }
    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && GetLocationPermission && ReadExternalStoragePermission) {

                        Toast.makeText(AttendanceActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AttendanceActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            bitmapToByteArray(b);
            ivAttedenceProfile.setImageBitmap(bitmap);
            progressDialog = new ProgressDialog(AttendanceActivity.this);
            progressDialog.setMessage("Loading...");

        }
    }
    private void getCurrentTimeDateOut() {
        today_date = DateFactory.getInstance().getTodayDate();
        time = DateFactory.getInstance().getCurrentTime("HH:mm:ss");
        tvAttendenceDate.setText(today_date);
        out_time.setText(time);
        out_time_stamp =AppUtility.getInstance().changeDateValue( today_date) + " " + time + ".05369";
        Log.d("TAG", "TIME STAMP : " + time_stamp);
    }

    private void getLocationOut() {
        GPSTracker gpsTracker = new GPSTracker(AttendanceActivity.this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            lati = Double.parseDouble(stringLatitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            longi = Double.parseDouble(stringLongitude);
            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(lati, longi, 1);
                address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                out_address.setText(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void submitForOutTime() {
        pd = new ProgressDialog(AttendanceActivity.this);
        pd.setMessage("loading");
        if (!NetworkFactory.isInternetOn(AttendanceActivity.this)) {
            DialogFactory.getInstance().showNoInternetDialog(AttendanceActivity.this);
            return;
        }
        pd.show();
        new OutLogin().execute();
    }

    private class OutLogin extends AsyncTask<String, String, String> {
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/melaattandences/attendence");
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("melaid", melaIdFromBundle);
                jsonObject.accumulate("shg_reg_id", shgRegIdFromBundle);
                jsonObject.accumulate("opening", "NA");
                jsonObject.accumulate("closing", out_time_stamp);
                jsonObject.accumulate("opening_photo_name", "NA");
                jsonObject.accumulate("closing_photo_name", userNameFromBundle);
                jsonObject.accumulate("opening_photo", "NA");
                jsonObject.accumulate("closing_photo", base_64);
                jsonObject.accumulate("opening_location", "NA");
                jsonObject.accumulate("closing_location", address);
                jsonObject.accumulate("participant_status", participantStatus);
                jsonObject.accumulate("mobile", attedenceMobileNumber);
                jsonObject.accumulate("imei_no",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyImei(),AttendanceActivity.this));
                jsonObject.accumulate("device_name",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyDeviceInfo(),AttendanceActivity.this));
                jsonObject.accumulate("location_coordinate",latitude+","+longitude);
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();
                    String encrypted=cryptography.encrypt(jsonObject.toString());
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
                json = encryptedObject.toString();

                AppUtility.getInstance().showLog(json, AttendanceActivity.class);
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null)
                    result = AppUtility.convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
            }
            return result;
        }

        protected void onPostExecute(String response) {
            pd.dismiss();
            Toast.makeText(AttendanceActivity.this, "Your Closing Attedence Saved ", Toast.LENGTH_LONG).show();
           // updateEveningFlag();
            try {
                responseobj = new JSONObject(response);
                Cryptography cryptography = null;
                String objectResponse="";
                if(responseobj.has("data")){
                    objectResponse=responseobj.getString("data");
                }else {
                    return;
                }
                try {
                    cryptography = new Cryptography();
                    String saa=cryptography.decrypt(objectResponse);
                    responseobj = new JSONObject(saa);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void updateEveningFlag() {

        List<AttendanceFlagData> attendanceFlagData = SplashScreen.getInstance().
                getDaoSession().getAttendanceFlagDataDao().
                queryBuilder().where(AttendanceFlagDataDao.
                Properties.AutogeneratedFlagId.eq(autoIDFromBundle)).build().list();
        for (AttendanceFlagData attendanceFlagData1 : attendanceFlagData) {
            attendanceFlagData1.setEeningFlag("1");
            SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData1);

        }

        /*AttendanceFlagData attendanceFlagData1 = new AttendanceFlagData();
        attendanceFlagData1.setEeningFlag("1");
        SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData1);*/
    }

    private void submitForInTime() {
        pd = new ProgressDialog(AttendanceActivity.this);
        pd.setMessage("loading");
        if (!NetworkFactory.isInternetOn(AttendanceActivity.this)) {
            DialogFactory.getInstance().showNoInternetDialog(AttendanceActivity.this);
            return;
        }
        pd.show();
        new InLogin().execute();
    }

    private class InLogin extends AsyncTask<String, String, String> {
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/melaattandences/attendence");
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("melaid", melaIdFromBundle);
                jsonObject.accumulate("shg_reg_id", shgRegIdFromBundle);
                jsonObject.accumulate("opening", time_stamp);
                jsonObject.accumulate("closing", "NA");
                jsonObject.accumulate("opening_photo_name", userNameFromBundle);
                jsonObject.accumulate("closing_photo_name", "NA");
                jsonObject.accumulate("opening_photo", base_64);
                jsonObject.accumulate("closing_photo", "NA");
                jsonObject.accumulate("opening_location", address);
                jsonObject.accumulate("closing_location", "NA");
                jsonObject.accumulate("participant_status", participantStatus);
                jsonObject.accumulate("mobile", attedenceMobileNumber);
                jsonObject.accumulate("imei_no",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyImei(),AttendanceActivity.this));
                jsonObject.accumulate("device_name",ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyDeviceInfo(),AttendanceActivity.this));
                jsonObject.accumulate("location_coordinate",latitude+","+longitude);
                JSONObject encryptedObject =new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();
                    String encrypted=cryptography.encrypt(jsonObject.toString());
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
                json = encryptedObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null)
                    result = AppUtility.convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                pd.dismiss();
            }
            return result;
        }

        protected void onPostExecute(String response) {
            pd.dismiss();
            //updateAttendanceFlag();
          //  updateMorningAttendanceFlag();
            Toast.makeText(AttendanceActivity.this,
                    "Your Opening Attedence Saved ", Toast.LENGTH_LONG).show();
            try {
                responseobj = new JSONObject(response);
                Cryptography cryptography = null;
                String objectResponse="";
                if(responseobj.has("data")){
                    objectResponse=responseobj.getString("data");
                }else {
                    return;
                }
                try {
                    cryptography = new Cryptography();
                    String saa=cryptography.decrypt(objectResponse);
                    responseobj = new JSONObject(saa);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                saveDetailInSharedPrefrence();
                // progressDialog.dismiss();
            } catch (JSONException e) {
                pd.dismiss();
                e.printStackTrace();
            }

            Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    private void updateMorningAttendanceFlag() {
        List<AttendanceFlagData> attendanceFlagData = SplashScreen.getInstance()
                .getDaoSession().getAttendanceFlagDataDao()
                .queryBuilder().where(AttendanceFlagDataDao
                        .Properties.AutogeneratedFlagId.eq(autoIDFromBundle))
                .build().list();
        for (AttendanceFlagData attendanceFlagData1 : attendanceFlagData) {
            attendanceFlagData1.setMorningFlag("1");
            SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData1);
        }
        //  AttendanceFlagData attendanceFlagData1 = new AttendanceFlagData();
        //  SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData1);
    }

    private void saveDetailInSharedPrefrence() {
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyInTime(), time, AttendanceActivity.this);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyInAddress(), address, AttendanceActivity.this);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyDate(), today_date, AttendanceActivity.this);
    }

    private void updateAttendanceFlag() {
        List<AttendanceFlagData> attendanceFlagDataListForUpdateFlag = SplashScreen.getInstance()
                .getDaoSession()
                .getAttendanceFlagDataDao()
                .queryBuilder()
                .where(AttendanceFlagDataDao.Properties.AutogeneratedFlagId.eq(autoIDFromBundle))
                .build()
                .list();
        for (AttendanceFlagData attendanceFlagData : attendanceFlagDataListForUpdateFlag) {
            attendanceFlagData.setParticipantFlag("0");
            SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData);
        }
        List<AttendanceFlagData> attendanceFlagDataListForUpdateFlag1 = SplashScreen.getInstance()
                .getDaoSession()
                .getAttendanceFlagDataDao()
                .queryBuilder()
                .where(AttendanceFlagDataDao.Properties.ParticipantFlag.eq(autoIDFromBundle))
                .build()
                .list();
        for (AttendanceFlagData attendanceFlagData : attendanceFlagDataListForUpdateFlag1) {
            AppUtility.getInstance().showLog("updatedflags" + attendanceFlagData.getParticipantFlag(), AttendanceActivity.class);
        }
    }
    private void bitmapToByteArray(byte[] b) {
        base_64 = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("TAG", "base----" + base_64);
    }
    private void getCurrentTimeDate() {
        today_date = DateFactory.getInstance().getTodayDate();
        time = DateFactory.getInstance().getCurrentTime("HH:mm:ss");
        tvAttendenceDate.setText(today_date);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyInTime(), time, AttendanceActivity.this);
        String get_in_time = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyInTime(), AttendanceActivity.this);
        tvAttendenceInTime.setText(get_in_time);
        time_stamp = AppUtility.getInstance().changeDateValue(today_date) + " " + time + ".05369";
        Log.d("TAG", "TIME STAMP : " + time_stamp);
    }

    private void getLocation() {
//        pd = new ProgressDialog(AttendanceActivity.this);
//        pd.setMessage("loading");
//        pd.show();
        GPSTracker gpsTracker = new GPSTracker(AttendanceActivity.this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            lati = Double.parseDouble(stringLatitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            longi = Double.parseDouble(stringLongitude);
            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(lati, longi, 1);
                address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                addresses.get(0).getSubLocality();
                Log.d("location", knownName + "," + city + "," + country + "," + postalCode);
                tvAttendanceAddress.setText(address);
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyInAddress(), address, AttendanceActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings

            gpsTracker.showSettingsAlert();
        }

    }

    private boolean checkPermission() {
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{GET_ACCOUNTS, READ_PHONE_STATE, CAMERA, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AttendanceActivity.this,
                Manifest.permission.CAMERA)) {
            //  Toast.makeText(AttendanceActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AttendanceActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case RequestPermissionCode: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //    Toast.makeText(AttendanceActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//                } else {
//                 //  Toast.makeText(AttendanceActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

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
    public void onLocationChanged(Location location) {
        //  pd.show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
