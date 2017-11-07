package nl.anwb.menoa.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedPreferenceUtil {

    private static final Logger log = LoggerFactory.getLogger("SharedPreferenceUtil");

    private SharedPreferenceUtil() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }

    public static boolean isPreferenceEmpty(Context context, String sharedPreferenceName) {
        log.info("isPreferenceEmpty() " + sharedPreferenceName);
        String value = getSharedPreferences(context).getString(sharedPreferenceName, null);
        return TextUtils.isEmpty(value);
    }

    public static String getPreferenceValue(Context context, String sharedPreferenceName) {
        log.info("getPreferenceValue() " + sharedPreferenceName);
        return getSharedPreferences(context).getString(sharedPreferenceName, null);
    }

    public static void setPreferenceValue(Context context, String sharedPreferenceName, String value) {
        log.info("setPreferenceValue() sharedPreferenceName: " + sharedPreferenceName + " Value: " + value);
        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(context).edit();
        sharedPreferencesEditor.putString(sharedPreferenceName, value);
        sharedPreferencesEditor.apply();
    }
}
