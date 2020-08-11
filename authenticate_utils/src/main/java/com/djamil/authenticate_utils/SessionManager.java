package com.djamil.authenticate_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class SessionManager {
	// Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "authenticate_pref";
    public static final String KEY_USE_FINGERPRINT = "USE_FINGERPRINT";

    public SessionManager() {
        pref = Authenticate.activity.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor = pref.edit();
    }

    public boolean getKeyUseFingerprint(){
        return pref.getBoolean(KEY_USE_FINGERPRINT, false);
    }

    public void setUseFingerprint(boolean usFingerPrint) {
        editor.putBoolean(KEY_USE_FINGERPRINT, usFingerPrint);
        editor.apply();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}
