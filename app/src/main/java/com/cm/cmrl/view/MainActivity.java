package com.cm.cmrl.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.cm.cmrl.R;
import com.cm.cmrl.admin.AdminLogin;
import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;

/**
 * Created by Iddinesh.
 */

public class MainActivity extends AppCompatActivity {

    Button departmentButton,stationcontrollerButton,adminButton;
//    Button maintainButton;

    LinearLayout mainLayout,logoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String TAG = "MainActivity";
        Log.w(TAG,"onCreate-->");

        departmentButton = findViewById(R.id.department_member);
        stationcontrollerButton = findViewById(R.id.station_controller);
        adminButton = findViewById(R.id.admin);
//        maintainButton=(Button)findViewById(R.id.maintain);

        mainLayout = findViewById(R.id.main_layout);
        logoLayout = findViewById(R.id.logo_layout);

        // show the button with animation
        final PropertyAction fabAction = PropertyAction.newPropertyAction(stationcontrollerButton).scaleX(0).scaleY(0).duration(750).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction headerAction = PropertyAction.newPropertyAction(departmentButton).interpolator(new DecelerateInterpolator()).translationY(-200).duration(550).alpha(0.4f).build();
        final PropertyAction bottomAction = PropertyAction.newPropertyAction(adminButton).translationY(500).duration(750).alpha(0f).build();

        Player.init().
                animate(headerAction).
                then().
                animate(fabAction).
                then().
                animate(bottomAction).
                play();

        departmentButton.setOnClickListener(view -> {

           Intent intent = new Intent(MainActivity.this, DepartmentLogin.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        adminButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, AdminLogin.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        stationcontrollerButton.setOnClickListener(view -> {
             Intent intent = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });


        /*maintainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MaintainLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.new_right, R.anim.new_left);
            }
        });*/
    }

    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
