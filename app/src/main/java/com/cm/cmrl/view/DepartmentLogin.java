package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.materialeditext.MaterialEditText;
import com.cm.cmrl.materialspinner.MaterialSpinner;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.cm.cmrl.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class DepartmentLogin extends AppCompatActivity {

    MaterialEditText employeeMaterialEditText, userNameMaterialEditText, passwordMaterialEditText;

    MaterialSpinner mainMaterialSpinner;

    LinearLayout forgotLinearLayout, loginMainLinearLayout;

    String networkStatus = "";
    String status = "", message = "", user_level = "", role = "", station_code = "", station_name = "", empid = "", name = "", username = "", mobile;

    Dialog  dialog;
    RequestQueue requestQueue;
    Button loginButton;

    SessionManager sessionManager;

    private String TAG ="DepartmentLogin";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_login);

        Log.w(TAG,"onCreate--->");

        sessionManager = new SessionManager(getApplicationContext());

        mainMaterialSpinner =  findViewById(R.id.spinner);

        loginButton =  findViewById(R.id.loginnnn_button);

        employeeMaterialEditText =  findViewById(R.id.employee_id);
        userNameMaterialEditText =  findViewById(R.id.user_name);
        passwordMaterialEditText =  findViewById(R.id.password);

        loginMainLinearLayout =  findViewById(R.id.login_main_layout);
        forgotLinearLayout =  findViewById(R.id.forgot_layout);

        employeeMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            employeeMaterialEditText.setFocusableInTouchMode(true);
            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        userNameMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            employeeMaterialEditText.setFocusableInTouchMode(true);
            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        passwordMaterialEditText.setOnTouchListener((view, motionEvent) -> {
            employeeMaterialEditText.setFocusableInTouchMode(true);
            userNameMaterialEditText.setFocusableInTouchMode(true);
            passwordMaterialEditText.setFocusableInTouchMode(true);
            return false;
        });

        loginMainLinearLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(loginMainLinearLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            passwordMaterialEditText.setFocusable(false);
            employeeMaterialEditText.setFocusable(false);
            userNameMaterialEditText.setFocusable(false);
        });

        // check whether internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Snackbar snackbar = Snackbar
                    .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", view -> {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    });

            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

        loginButton.setOnClickListener(view -> {

            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                Snackbar snackbar = Snackbar
                        .make(loginMainLinearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", view1 -> {

                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        });

                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                if (Objects.requireNonNull(employeeMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter employee id")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();

                } else if (Objects.requireNonNull(userNameMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter user name")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();

                } else if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter password")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();
                } else {


                    LoginUrl(ApiCall.API_URL + "login_access.php?empid=" + employeeMaterialEditText.getText().toString().replace(" ", "%20") + "&username=" + userNameMaterialEditText.getText().toString().replace(" ", "%20") + "&password=" + passwordMaterialEditText.getText().toString().replace(" ", "%20") + "&user_level=5");

                }
            }


        });

        forgotLinearLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(forgotLinearLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            passwordMaterialEditText.setFocusable(false);
            employeeMaterialEditText.setFocusable(false);
            userNameMaterialEditText.setFocusable(false);

            Intent intent = new Intent(DepartmentLogin.this, ForgotPassword.class);
            intent.putExtra("status", "1");
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
    }

    public void onBackPressed() {

        Intent intent = new Intent(DepartmentLogin.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.new_right, R.anim.new_left);


    }

    /**
     * @param url call the api to pass the login param to server
     */
    public void LoginUrl(String url) {

        dialog = new Dialog(DepartmentLogin.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(DepartmentLogin.this);

        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("login");
                        Log.w(TAG,"Response-->"+ja);
                        sessionManager.createDepartmentLoginSession();

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            status = jsonObject.getString("status");
                            message = jsonObject.getString("message");
                            Log.w(TAG,"status-->"+status);
                            Log.w(TAG,"message-->"+message);


                            if (status.equalsIgnoreCase("1")) {

                                message = jsonObject.getString("message");
                                user_level = jsonObject.getString("user_level");
                                station_code = jsonObject.getString("department_code");
                                station_name = jsonObject.getString("department_name");
//                                    role = jsonObject.getString("role");
                                empid = jsonObject.getString("empid");
                                name = jsonObject.getString("name");
                                username = jsonObject.getString("username");
                                mobile = jsonObject.getString("mobile");
                            } else if (status.equalsIgnoreCase("2")) {

                                message = jsonObject.getString("message");

                            } else if (status.equalsIgnoreCase("3")) {

                                message = jsonObject.getString("message");

                            } else if (status.equalsIgnoreCase("4")) {

                                message = jsonObject.getString("message");

                            } else if (status.equalsIgnoreCase("0")) {

                                message = jsonObject.getString("message");
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    if (status.equalsIgnoreCase("1")) {

                        new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Logged in successfully")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();

                                    if (station_code.equalsIgnoreCase("AFC")) {

                                        sessionManager.createLoginSession(message, user_level, station_code, role, station_name, empid, name, username, mobile);
                                        Intent intent = new Intent(DepartmentLogin.this, MAinfragmentActivty.class);
                                        //  Intent intent = new Intent(DepartmentLogin.this, DepartmentSelection.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                                    } else if (station_code.equalsIgnoreCase("TEL")) {

                                        sessionManager.createLoginSession(message, user_level, station_code, role, station_name, empid, name, username, mobile);
                                        Intent intent = new Intent(DepartmentLogin.this, MAinfragmentActivty.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                    } else {

                                        sessionManager.createLoginSession(message, user_level, station_code, role, station_name, empid, name, username, mobile);
                                        Intent intent = new Intent(DepartmentLogin.this, MAinfragmentActivty.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                    }

                                })
                                .show();
                    } else if (status.equalsIgnoreCase("2")) {
                        new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct password")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();


                    } else if (status.equalsIgnoreCase("3")) {
                        new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct user name")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();

                    } else if (status.equalsIgnoreCase("4")) {
                        new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct employee id")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();
                    } else if (status.equalsIgnoreCase("0")) {
                        new SweetAlertDialog(DepartmentLogin.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();
                    }
                },

                error -> {
                    Log.e("Volley", "Error");
                    dialog.dismiss();
                }
        );
        jor.setRetryPolicy(new DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jor);

    }
}


