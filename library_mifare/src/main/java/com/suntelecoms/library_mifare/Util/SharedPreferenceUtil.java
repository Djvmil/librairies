package com.suntelecoms.library_mifare.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceUtil {

    public static String showboxNfcPreferenceName = "nfc_preference_name";
    public static enum PreferenceName {
        is_first_run,is_nfc_initialized
    }

    private static final String LOG_TAG = SharedPreferenceUtil.class.getSimpleName();

    public static String getPreference(Context _context, String preferenceName){
        // Editor for Shared preferences
        String preference = null;
        try{
            SharedPreferences pref = _context.getSharedPreferences(showboxNfcPreferenceName, 0);
            if(!pref.contains(preferenceName)){
                return null;
            }else{
                preference = pref.getString(preferenceName, null);
            }
        }catch(Exception ex){
            Log.i("Exception >> ", ex.getMessage());
        }
        return preference;
    }

    public static boolean setPreference (Context _context, String preferenceName, String preferenceValue){
        if(preferenceValue == null || preferenceName == null){
            return false;
        }
        // Editor for Shared preferences
        SharedPreferences.Editor editor;
        SharedPreferences pref = _context.getSharedPreferences(showboxNfcPreferenceName, 0);
        editor = pref.edit();

        Log.i(LOG_TAG,preferenceValue + "/" + preferenceName);
        editor.putString(preferenceName, preferenceValue);
        if(editor.commit()){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean getBooleanPreference(Context _context, String preferenceName){
        // Editor for Shared preferences
        Boolean preference = null;
        try{

            SharedPreferences pref = _context.getSharedPreferences(showboxNfcPreferenceName, 0);
            if(!pref.contains(preferenceName)){
                return null;
            }else{
                preference = pref.getBoolean(preferenceName, false);
            }

        }catch(Exception ex){
            Log.i("Exception >> ", ex.getMessage());
            preference = false;
        }
        return preference;

    }

    public static boolean setBooleanPreference (Context _context, String preferenceName, Boolean preferenceValue){

        if(preferenceValue == null){
            Log.e(LOG_TAG, "Parametre 'preferenceValue' NULL !");
            return false;
        }

        if(preferenceName == null){
            Log.e(LOG_TAG, "Parametre 'preferenceName' NULL !");
            return false;
        }

        // Editor for Shared preferences
        SharedPreferences.Editor editor;
        SharedPreferences pref = _context.getSharedPreferences(showboxNfcPreferenceName, 0);
        editor = pref.edit();

        Log.i(LOG_TAG,preferenceValue + "/" + preferenceName);
        editor.putBoolean(preferenceName, preferenceValue);
        if(editor.commit()){
            return true;
        }else{
            return false;
        }

    }
}
