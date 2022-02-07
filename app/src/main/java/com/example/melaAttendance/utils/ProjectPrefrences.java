package com.example.melaAttendance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.melaAttendance.BuildConfig;

public class ProjectPrefrences {

    public static ProjectPrefrences projectPrefrences=null;

    public static ProjectPrefrences getInstance(){

        if(projectPrefrences==null)
            projectPrefrences=new ProjectPrefrences();
        return projectPrefrences;
    }

    public  String getSharedPrefrencesData(String key, Context context){
        String value="";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            value = sharedPreferences.getString(key, "");
            return value;

        }catch (ClassCastException cce){
            AppUtility.getInstance().showLog(String.valueOf(cce),ProjectPrefrences.class);
        }

        return null;
    }
    public  int getSharedPrefrencesIntegerData(String key, Context context){
        int value=0;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            value = sharedPreferences.getInt(key, 0);
            return value;

        }catch (ClassCastException cce){
            AppUtility.getInstance().showLog(String.valueOf(cce),ProjectPrefrences.class);
        }

        return 0;
    }

    public boolean saveSharedPrefrecesData(String key,String value,Context context){
        SharedPreferences spref=context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor sprefEditor=spref.edit();
        sprefEditor.putString(key,value);
        boolean isDataSaved= sprefEditor.commit();
        sprefEditor.clear();
        return isDataSaved;
    }
    public boolean saveSharedPrefrecesData(String key,int value,Context context){
        SharedPreferences spref=context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor sprefEditor=spref.edit();
        sprefEditor.putInt(key,value);
        boolean isDataSaved= sprefEditor.commit();
        sprefEditor.clear();
        return isDataSaved;
    }

    public void removeSharedPrefrencesData(String key, Context context){

        SharedPreferences preferences=context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.remove(key);
        editor.commit();

    }
}
