package com.cm.cmrl.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;
import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class LoadingScreen extends AppCompatActivity {

    /**
     * Session to check whether user is login or not.
     */
    SessionManager sessionManager;

    // user level
    String user_level;

    String TAG = "LoadingScreen";
    int haslocationpermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        Log.w(TAG,"onCreate -->");

        sessionManager = new SessionManager(LoadingScreen.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        user_level = hashMap.get(SessionManager.KEY_USER_LEVEL);

        Log.w(TAG,"user_level -->"+user_level);


        final GifMovieView gif1 = findViewById(R.id.gif1);

        gif1.setMovieResource(R.drawable.metroloder);

        //Log.e("tokenID", "" + FirebaseInstanceId.getInstance().getToken());

        Thread timerThread = new Thread() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {


                    // check the android sdk version for runtime permission
                    if (Build.VERSION.SDK_INT >= 23) {
                        Log.w(TAG,"IF"+sessionManager.isLoggedIn());


                        insertmappermission();

                    } else {

                        Log.w(TAG,"ELSE"+sessionManager.isLoggedIn());

                        // check whether user is logged in or not
                        if (sessionManager.isLoggedIn()) {

                            if (user_level.equalsIgnoreCase("4")) {


                                // user level is four show station controller (Department list)
                                Intent intent = new Intent(LoadingScreen.this, StationActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);

                            } else if (user_level.equalsIgnoreCase("5")) {

                                // user level is five show Department Member (Station list)
                                Intent intent = new Intent(LoadingScreen.this, MAinfragmentActivty.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);

                            } else if (user_level.equalsIgnoreCase("6")) {

                                // user level is five show Department Member (Station list)
                                Intent intent = new Intent(LoadingScreen.this, AdminActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);

                            }

                        } else {


                            Intent intent = new Intent(LoadingScreen.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.new_right, R.anim.new_left);

                        }


                    }

                }
            }
        };
        timerThread.start();
    }

    @SuppressLint("NewApi")
    private void insertmappermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            haslocationpermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) | checkSelfPermission(Manifest.permission.CAMERA) | checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (haslocationpermission != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG,"insertmappermission  if");

                Intent myIntent = new Intent(LoadingScreen.this, Permission_Activity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);
                finish();

            } else {
                Log.w(TAG,"insertmappermission  else"+sessionManager.isLoggedIn());
                Log.w(TAG,"insertmappermission  else"+" user_level : "+user_level);

                if (sessionManager.isLoggedIn()) {

                    if (user_level.equalsIgnoreCase("4")) {
                        // user level is four show station controller (Department list)
                        Intent intent = new Intent(LoadingScreen.this, StationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                    } else if (user_level.equalsIgnoreCase("5")) {

                        // user level is five show Department Member (Station list)
                        Intent intent = new Intent(LoadingScreen.this, MAinfragmentActivty.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                    } else if (user_level.equalsIgnoreCase("6")) {

                        // user level is five show Department Member (Station list)
                        Intent intent = new Intent(LoadingScreen.this, AdminActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                    }

                } else {


                    Intent intent = new Intent(LoadingScreen.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.new_right, R.anim.new_left);

                }
            }

        }else{
            Log.w(TAG,"insertmappermission else");
            Intent myIntent = new Intent(LoadingScreen.this, Permission_Activity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
            finish();
        }

    }
}

