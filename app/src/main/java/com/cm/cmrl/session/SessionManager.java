package com.cm.cmrl.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.cm.cmrl.view.MainActivity;

import java.util.HashMap;
/**
 * Created by Iddinesh.
 */
public class SessionManager {


    private SharedPreferences pref;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context context;
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // User name (make variable public to access from outside)
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_USER_LEVEL = "userlevel";
    public static final String KEY_STATION_CODE = "stationcode";
    public static final String KEY_STATION_NAME = "stationname";
    public static final String KEY_EMPID = "empid";
    public static final String KEY_NAME = "name";
    public static final String KEY_ROLE = "role";
    public static final String KEY_USERNMAE = "usernmae";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEEPDEPARTMENTLOGIN = "true";





    private String TAG ="SessionManager";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context applicationContext) {

        // TODO Auto-generated constructor stub

        this.context = applicationContext;
        // Shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public HashMap<String, String> getUserDetails() {
        // TODO Auto-generated method stub

        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_MESSAGE, pref.getString(KEY_MESSAGE, null));
        user.put(KEY_USER_LEVEL, pref.getString(KEY_USER_LEVEL, null));
        user.put(KEY_STATION_CODE, pref.getString(KEY_STATION_CODE, null));
        user.put(KEY_STATION_NAME, pref.getString(KEY_STATION_NAME, null));
        user.put(KEY_EMPID, pref.getString(KEY_EMPID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_USERNMAE, pref.getString(KEY_USERNMAE, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        Log.w(TAG,"KEY_STATION_CODE :"+KEY_STATION_CODE);
        Log.e("user", "" + user);

        return user;

    }

    public boolean checkLogin() {
        // TODO Auto-generated method stub
        if (!this.isLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);

    }

    public boolean isLoggedIn() {
        // TODO Auto-generated method stub
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createLoginSession(String message, String user_level, String station_code, String role, String station_name, String empid, String name, String username, String mobile) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_MESSAGE, message);
        editor.putString(KEY_USER_LEVEL, user_level);
        editor.putString(KEY_STATION_CODE, station_code);
        Log.w(TAG,"KEY_STATION_CODE Editor :"+KEY_STATION_CODE);
        editor.putString(KEY_STATION_NAME, station_name);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_EMPID, empid);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNMAE, username);
        editor.putString(KEY_MOBILE, mobile);
        editor.commit();

    }

    public void createDepartmentLoginSession() {

        editor.putBoolean(KEEPDEPARTMENTLOGIN, true);

        editor.commit();

    }


}
