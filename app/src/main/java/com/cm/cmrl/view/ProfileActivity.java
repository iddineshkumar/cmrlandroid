package com.cm.cmrl.view;

import android.os.Bundle;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;

import java.util.HashMap;
/**
 * Created by Iddinesh.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SessionManager sessionManager = new SessionManager(ProfileActivity.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        String id = hashMap.get(SessionManager.KEY_EMPID);
        String name = hashMap.get(SessionManager.KEY_NAME);
        String mobile = hashMap.get(SessionManager.KEY_MOBILE);
        RelativeLayout profileRela = findViewById(R.id.profile_relative_back);
        TextView employeeID = findViewById(R.id.employee_text);
        TextView employeeName = findViewById(R.id.employee_name_text);
        TextView employeeMobile = findViewById(R.id.employee_mobile_text);
        employeeID.setText(id);
        employeeName.setText(name);
        employeeMobile.setText(mobile);
        profileRela.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
