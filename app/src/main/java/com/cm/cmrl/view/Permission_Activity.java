package com.cm.cmrl.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;

import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class Permission_Activity extends AppCompatActivity {

    Button permission_btn;

    Intent thisIntent;

    String TAG = "Permission_Activity";

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    String[] permissionString = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            "check"};

    SessionManager sessionManager;

    String user_level="";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.w(TAG,"onCreate");

        sessionManager = new SessionManager(Permission_Activity.this);

        setContentView(R.layout.permission_layout);

        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        user_level = hashMap.get(SessionManager.KEY_USER_LEVEL);
        Log.w(TAG,"user_level : "+user_level);

        thisIntent = getIntent();

        permission_btn = findViewById(R.id.permission_btn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Window window = Permission_Activity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Permission_Activity.this.getResources().getColor(R.color.colorPrimaryDark));
        }

        insertmappermission();

        permission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG,"permission_btn--->");
                insertmappermission();
            }
        });

    }

    private void insertmappermission() {
        Log.w(TAG,"insertmappermission --->");


        int haslocationpermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.w(TAG,"insertmappermission if--->");


            haslocationpermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) | checkSelfPermission(Manifest.permission.CAMERA) | checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (haslocationpermission != PackageManager.PERMISSION_GRANTED) {

                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) | !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) | !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    showMessageOKCancel(
                            (dialog, which) -> requestPermissions(permissionString,
                                    REQUEST_CODE_ASK_PERMISSIONS));
                    return;
                }
                requestPermissions(permissionString,
                        REQUEST_CODE_ASK_PERMISSIONS);

                Log.w(TAG,"if OOO --->");

            }else{
                Log.w(TAG,"else OOO --->");

            }


        }else{
            Log.w(TAG,"insertmappermission else--->");

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(Permission_Activity.this)
                .setMessage("Allow permission to access this device location and External Storage ?")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Log.w(TAG,"onRequestPermissionsResult : "+"Permission Granted");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            } else {
                // Permission Denied
                Log.w(TAG,"onRequestPermissionsResult : "+"Permission Denied");



            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}