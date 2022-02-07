package com.example.melaAttendance.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.database.DaoMaster;
import com.example.melaAttendance.database.DaoSession;
import com.example.melaAttendance.utils.AppConstants;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.Cryptography;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import org.greenrobot.greendao.database.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SplashScreen extends AppCompatActivity {

    String login_status,version;
    private Context context;
    public static final int RequestPermissionCode = 1;
    private static SplashScreen instance = null;
    private String response=null,status;                      //status tells about the closing and opening of the mela
    public synchronized static SplashScreen getInstance() {
        if (instance==null)
            instance=new SplashScreen();
        return instance;
    }
    private DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }
    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = getApplicationContext();
        handleSSLHandshake();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mela0-db");
        Database db = helper.getWritableDb();
        if (db != null) {
            daoSession = new DaoMaster(db).newSession();
            SplashScreen.getInstance().setDaoSession(daoSession);
        }
        getVersionStatus();
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

    private void getVersionStatus() {

        if (!NetworkFactory.isInternetOn(SplashScreen.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setTitle("No Internet!");
            builder.setMessage("Please open your Internet Connection.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            builder.show();
        }else {
            String url = AppConstants.HTTP_TYPE+"://"+AppConstants.IP_ADDRESS+"/"+AppConstants.API_TYPE+"/services/mela/version";
            JSONObject versionPayload=new JSONObject();
            try {
                versionPayload.accumulate("version","0.0.0");
                versionPayload.accumulate("melaId",AppConstants.melaId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject encryptedObject =new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted=cryptography.encrypt(versionPayload.toString());
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
            JsonObjectRequest versionRequest = new JsonObjectRequest(Request.Method.POST, url, encryptedObject,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject s) {
                    //  response=s;
                    Cryptography cryptography = null;
                    String versionString="";
                    String objectResponse="";
                    if(s.has("data")){
                        try {
                            objectResponse=s.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        return;
                    }
                    try {
                        cryptography = new Cryptography();
                         versionString=cryptography.decrypt(objectResponse);
                   //     Log.d("TAG"+s,"data_value");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    responseParshing(versionString);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogFactory.getInstance().showServerErrorDialog(SplashScreen.this, "Server Error!", "OK");

                //    Log.d("TAG", "error" + error);
                }
            });
      /*      stringRequest.setRetryPolicy(new RetryPolicy() {

                @Override
                public int getCurrentTimeout() {
                    return DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
                }

                @Override
                public int getCurrentRetryCount() {
                    return DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                    if (NetworkFactory.isInternetOn(SplashScreen.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                        builder.setTitle("Poor Internet!");
                        builder.setMessage("Please connect your phone with high speed Internet Connection" + error);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            });*/
            RequestQueue requestQueue = Volley.newRequestQueue(SplashScreen.this);
            requestQueue.add(versionRequest);
        }
    }

    private void  responseParshing(String response){
        try {
            JSONArray mainArr = new JSONArray(response);

            for (int i = 0; i < mainArr.length(); i++) {
                JSONObject obj = mainArr.getJSONObject(i);
                version = obj.getString("version");
                if(version.equalsIgnoreCase("correct")) {             //because at incorrect version not receivinng the status of mela
                    status = obj.getString("status");
                }
            }
            AppUtility.getInstance().showLog(version, SplashScreen.class);

            if (version.equalsIgnoreCase("correct")) {
                if(status.equalsIgnoreCase("open")) {
                    loadNextScreenWithDelay();
                }else
                {
                    showDialogForMelaClose();
                }

            } else {
                showDialogForVersion();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showDialogForVersion() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle(getString(R.string.update_application_title));
        builder.setMessage(R.string.update_application_msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateApplication();
            }
        });
        builder.show();

    }
    private void updateApplication() {
        //  final String appPackageName = context.getPackageName();
        // String marketStores = "market://details?id=" + appPackageName;
        String nrlmLiveLocUri = "https://drive.google.com/drive/folders/1LjPskVPqontHuZRTKdqH1kcGTxwsI__C?usp=sharing";

        try {
            SplashScreen.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nrlmLiveLocUri)));
        } catch (android.content.ActivityNotFoundException anfe) {
            //((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            SplashScreen.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nrlmLiveLocUri)));
        }
        ((Activity) SplashScreen.this).finish();
    }
    private void showDialogForMelaClose()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.mela_close_msg));
        builder.setCancelable(false);
        builder.setPositiveButton("Yes" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(SplashScreen.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SplashScreen.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SplashScreen.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void loadNextScreenWithDelay() {
        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToNextAndClearPreviousScreens(LoginActivity.class);
            }
        }, 3000);
    }
    private void moveToNextAndClearPreviousScreens(Class<LoginActivity> loginActivityClass) {
        login_status = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyLoginDone(), context);
       // Log.d("TAG", "login_status----" + login_status);
        if (login_status.equalsIgnoreCase("ok")) {
            Intent intent = new Intent(SplashScreen.this, MpinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            // clearingAllTableOfLocalDB();
            Intent intent = new Intent(SplashScreen.this, loginActivityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
    private void  clearingAllTableOfLocalDB(){
        SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getBillDetailsDataDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getShgDetailsDataDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();
        SplashScreen.getInstance().getDaoSession().getSoldProductDetailsDataDao().deleteAll();
    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
