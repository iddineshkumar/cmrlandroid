package com.cm.cmrl.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;
import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class DepartmentSelection extends AppCompatActivity {

    Button afcButton,telButton;

    SessionManager sessionManager;

    String message="", user_level="", station_code="", station_name="", empid="", name="", username="",mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_selection_screen);

        sessionManager = new SessionManager(DepartmentSelection.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        message = hashMap.get(SessionManager.KEY_MESSAGE);
        user_level = hashMap.get(SessionManager.KEY_USER_LEVEL);
        station_name = hashMap.get(SessionManager.KEY_STATION_NAME);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        empid = hashMap.get(SessionManager.KEY_EMPID);
        name = hashMap.get(SessionManager.KEY_NAME);
        username = hashMap.get(SessionManager.KEY_USERNMAE);
        mobile = hashMap.get(SessionManager.KEY_USERNMAE);

        afcButton = findViewById(R.id.afc);
        telButton = findViewById(R.id.tel);

        afcButton.setOnClickListener(view -> {

            sessionManager.createLoginSession(message, user_level, "AFC", "",station_name, empid, name, username,mobile);

            Intent intent = new Intent(DepartmentSelection.this, MAinfragmentActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        });

        telButton.setOnClickListener(view -> {

            sessionManager.createLoginSession(message, user_level, "TEL", "",station_name, empid, name, username,mobile);

            Intent intent = new Intent(DepartmentSelection.this, MAinfragmentActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });

    }

    public void onBackPressed() {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


    }
    }
