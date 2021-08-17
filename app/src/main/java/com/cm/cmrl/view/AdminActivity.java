package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cm.cmrl.R;
import com.cm.cmrl.admin.AdminDepartmentScreen;
import com.cm.cmrl.admin.AdminSelectedDepartmentScreen;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    public static NavigationView nvDrawer;
    public static DrawerLayout drawerLayout;
    SessionManager sessionManager;
    String code = "";
    private Fragment fragment;
    private Class fragmentClass;
    public static String type;
    public static String tabSelects;
    private FragmentTransaction transaction;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        String TAG = "AdminActivity";
        Log.w(TAG,"onCreate--->");
        sessionManager = new SessionManager(AdminActivity.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        code = hashMap.get(SessionManager.KEY_STATION_CODE);
        Intent intent = getIntent();
        if (intent.getStringExtra("Tabselects") != null) {
            tabSelects = intent.getStringExtra("Tabselects");
        } else {
            tabSelects = "0";
        }
        if (intent.getStringExtra("TypeForm") != null) {
            if (Objects.requireNonNull(intent.getStringExtra("TypeForm")).equalsIgnoreCase("1")) {
                fragmentClass = AdminDepartmentScreen.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
            } else if (Objects.requireNonNull(intent.getStringExtra("TypeForm")).equalsIgnoreCase("2")) {
                fragmentClass = AdminSelectedDepartmentScreen.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
            }
        } else {
            fragmentClass = AdminDepartmentScreen.class;

            try {

                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {

                e.printStackTrace();

            }
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
        }

        Button logOutButton = findViewById(R.id.logout_button);

        nvDrawer = findViewById(R.id.nvView);

        drawerLayout =  findViewById(R.id.main_content);
        logOutButton.setOnClickListener(view -> new SweetAlertDialog(AdminActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Do you want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(Dialog::dismiss)
                .setConfirmClickListener(sDialog -> {
                    getSharedPreferences("Station", 0).edit().clear().commit();
                    sessionManager.logoutUser();
                    sDialog.dismiss();
                })
                .show());
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

        switch (menuItem.getItemId()) {

            case R.id.home_fragment:
                fragmentClass = AdminDepartmentScreen.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }
                tabSelects = "0";
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
                break;
            case R.id.favorites_fragment:
                fragmentClass = AdminSelectedDepartmentScreen.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }
                tabSelects = "0";
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();
                break;
            case R.id.change_password:

                fragmentClass = ChangePasswordForAdminFragment.class;

                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {

                    e.printStackTrace();

                }

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, fragment).commit();

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

    @Override
    public void onBackPressed() {

        if (code.equalsIgnoreCase("AFC")) {

            Intent intent = new Intent(AdminActivity.this, DepartmentSelection.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        } else if (code.equalsIgnoreCase("TEL")) {

            Intent intent = new Intent(AdminActivity.this, DepartmentSelection.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
