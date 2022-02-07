package com.example.melaAttendance.preference;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class AbstractPreferenceManager {
    private static final String VERSION = "__prefs_version__";
    private final Context ctx ;
    private final SharedPreferences prefs;

    protected AbstractPreferenceManager(Context ctx, SharedPreferences prefs) {
        this.ctx = ctx;
        this.prefs = prefs;
    }
    protected Context getContext()
    {
        return ctx;
    }
    protected SharedPreferences getPreferences()
    {
        return prefs;
    }
    protected boolean clearPreferences()
    {
        return prefs.edit().clear().commit();
    }
    protected void onUpgrade(SharedPreferences prefs, int oldVersion, int newVersion)
    {
        prefs.edit().clear().apply();
    }
    protected boolean readBoolean(String key, Boolean value)
    {
        return prefs.getBoolean(key, value);
    }
    protected int readInt(String key, int value)
    {
        return prefs.getInt(key, value);
    }

}
