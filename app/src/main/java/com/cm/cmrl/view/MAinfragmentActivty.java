package com.cm.cmrl.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cm.cmrl.R;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class MAinfragmentActivty extends AppCompatActivity {


    public static NavigationView nvDrawer;

    public static DrawerLayout drawerLayout;


    SessionManager sessionManager;

    String code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainfragment_layout);

        String TAG = "MAinfragmentActivty";
        Log.w(TAG,"onCreate--->");

        // Call Schedule or Home screen.
        StationListActivity orderManagement = new StationListActivity();

        sessionManager=new SessionManager(MAinfragmentActivty.this);

        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        code = hashMap.get(SessionManager.KEY_STATION_CODE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();
        Button logOutButton = findViewById(R.id.logout_button);


        nvDrawer = findViewById(R.id.nvView);

        drawerLayout = findViewById(R.id.main_content);
        logOutButton.setOnClickListener(view -> new SweetAlertDialog(MAinfragmentActivty.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Do you want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(Dialog::dismiss)
                .setConfirmClickListener(sDialog -> {

                    sDialog.dismiss();
                    sessionManager.logoutUser();
                })
                .show());
        // call method to set fragment
        setupDrawerContent(nvDrawer);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;

        Class fragmentClass;

        FragmentManager fragmentManager;

        switch (menuItem.getItemId()) {

            case R.id.home_fragment:

                fragmentClass = StationListActivity.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }

                fragmentManager = getSupportFragmentManager();
                if (fragment != null) {
                    fragmentManager.beginTransaction().addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
                }

                break;

            case R.id.change_password:

                fragmentClass = ChangePasswordForDepartmentFragment.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }

                fragmentManager = getSupportFragmentManager();
                if (fragment != null) {
                    fragmentManager.beginTransaction().addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
                }

                break;
            default:

        }

        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);

        // Set action bar title
        setTitle(menuItem.getTitle());

        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }
    public void onBackPressed() {


        if(code.equalsIgnoreCase("AFC")){

            Intent intent = new Intent(MAinfragmentActivty.this, DepartmentSelection.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        }else if(code.equalsIgnoreCase("TEL")){

            Intent intent = new Intent(MAinfragmentActivty.this, DepartmentSelection.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        }else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
    }
