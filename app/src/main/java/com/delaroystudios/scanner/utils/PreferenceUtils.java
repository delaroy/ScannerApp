package com.delaroystudios.scanner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static boolean saveMessage(Boolean message, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(Constant.MESSAGE, message);
        prefsEditor.apply();
        return true;
    }

    public static Boolean getMessage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constant.MESSAGE, true);
    }

}
