package com.cm.cmrl.maintain;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Iddinesh.
 */

public class MaintainFragmentActivity extends AppCompatActivity {

    public static NavigationView nvDrawer;

    public static DrawerLayout drawerLayout;

    Button logOutButton;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_fragment_layout);

        // Call Schedule or Home screen.
        decodeScnner orderManagement = new decodeScnner();
        sessionManager=new SessionManager(MaintainFragmentActivity.this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();

        logOutButton =  findViewById(R.id.logout_button);

        nvDrawer =  findViewById(R.id.nvView);

        drawerLayout = findViewById(R.id.main_content);

        logOutButton.setOnClickListener(view -> new SweetAlertDialog(MaintainFragmentActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Do you want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(Dialog::dismiss)
                .setConfirmClickListener(Dialog::dismiss)
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

            case R.id.qr_fragment:

                fragmentClass = decodeScnner.class;

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

            case R.id.all_fragment:

                fragmentClass = AllFragment.class;

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

            case R.id.monitor_fragment:

                fragmentClass = MonitorFragment.class;

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

            case R.id.maintenance_fragment:
                fragmentClass = MaintenanceFragment.class;

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
            case R.id.checker:

                fragmentClass = CheckerFragment.class;

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
        Intent intent = new Intent(MaintainFragmentActivity.this, MaintainStationList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.new_right, R.anim.new_left);

    }
}
