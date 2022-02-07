package com.example.melaAttendance.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.view.ContextThemeWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AppUtility extends ContextThemeWrapper {
    public static AppUtility utilsInstance;
    private static boolean wantToShow = false;

    public void showLog(String logMsg, Class application) {
        if (wantToShow) {
            Log.d(application.getName(), logMsg);
        }
    }

    public static AppUtility getInstance(){
        if (utilsInstance==null){
            utilsInstance=new AppUtility();
        }
        return utilsInstance;
    }
    public String changeDateValue(String date) {
        String changedValue = "";
        Locale locale = new Locale("ENGLISH");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
        changedValue = sdf.format(new Date());
        return changedValue;
    }
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
    public String getRandomOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return "" + otp;
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connec.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    private String loadeDataList(String filName) {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(filName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
