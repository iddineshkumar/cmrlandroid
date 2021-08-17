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
import com.cm.cmrl.arraylist.StationList;
import com.cm.cmrl.materialeditext.MaterialEditText;
import com.cm.cmrl.materialspinner.MaterialSpinner;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.cm.cmrl.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class LoginScreen extends AppCompatActivity {

    MaterialEditText employeeMaterialEditText, userNameMaterialEditText, passwordMaterialEditText;

    MaterialSpinner mainMaterialSpinner;

    LinearLayout forgotLinearLayout, loginMainLinearLayout;

    RequestQueue requestQueue;

    StationList stationList;

    ArrayList<StationList> stationListArrayList = new ArrayList<>();
    TextView mainReasonCustomFontTextView;

    Button loginButton;

    String networkStatus = "", stationId = "";
    String status = "", message = "", user_level = "", station_code = "", station_name = "", empid = "", name = "", username = "", mobile;

    Dialog  dialog;

    SessionManager sessionManager;
    private String role = "";

    private String TAG ="LoginScreen";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Log.w(TAG,"onCreate-->");

        sessionManager = new SessionManager(getApplicationContext());

        mainMaterialSpinner =  findViewById(R.id.spinner);
        employeeMaterialEditText =  findViewById(R.id.employee_id);
        userNameMaterialEditText =  findViewById(R.id.user_name);
        passwordMaterialEditText =  findViewById(R.id.password);

        loginButton = findViewById(R.id.loginnnn_button);

        forgotLinearLayout = findViewById(R.id.forgot_layout);
        loginMainLinearLayout = findViewById(R.id.login_main_layout);

        mainReasonCustomFontTextView = findViewById(R.id.request_reason_text);

        mainMaterialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {

            mainReasonCustomFontTextView.setText(stationListArrayList.get(position).getName());
            stationId = stationListArrayList.get(position).getCode();

        });

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


        forgotLinearLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(forgotLinearLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            passwordMaterialEditText.setFocusable(false);
            employeeMaterialEditText.setFocusable(false);
            userNameMaterialEditText.setFocusable(false);

            Intent intent = new Intent(LoginScreen.this, ForgotPassword.class);
            intent.putExtra("status", "0");
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
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
                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter employee id")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();

                } else if (Objects.requireNonNull(userNameMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter user name")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();

                } else if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {
                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Enter password")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(Dialog::dismiss)
                            .show();
                } else {


                    LoginUrl(ApiCall.API_URL + "login_access.php?stationid=" + stationId.replace(" ", "%20") + "&empid=" + employeeMaterialEditText.getText().toString().replace(" ", "%20") + "&username=" + userNameMaterialEditText.getText().toString().replace(" ", "%20") + "&password=" + passwordMaterialEditText.getText().toString().replace(" ", "%20") + "&user_level=4");

                }
            }

        });

        StationListUrl();
    }

    // default back button action
    public void onBackPressed() {

        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.new_right, R.anim.new_left);
    }

    protected void onResume() {

        super.onResume();
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


        } else {

            StationListUrl();
        }
    }

    /**
     * @param url pass value to server
     */
    public void LoginUrl(String url) {

        dialog = new Dialog(LoginScreen.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(LoginScreen.this);


        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        JSONArray ja = response.getJSONArray("login");

                        Log.w(TAG,"Response-->"+ja);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                message = jsonObject.getString("message");
                                user_level = jsonObject.getString("user_level");
                                station_code = jsonObject.getString("station_code");

                                Log.w(TAG,"station_code-->"+station_code);
                                station_name = jsonObject.getString("station_name");
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

                        new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Logged in successfully")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {
                                    sDialog.dismiss();
                                    Log.w(TAG,"station_code-->"+station_code);
                                    sessionManager.createLoginSession(message, user_level, station_code, role, station_name, empid, name, username, mobile);

                                    Intent intent = new Intent(LoginScreen.this, StationActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();

                    } else if (status.equalsIgnoreCase("2")) {
                        new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct password")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();


                    } else if (status.equalsIgnoreCase("3")) {
                        new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct user name")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();

                    } else if (status.equalsIgnoreCase("4")) {
                        new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("CMRL")
                                .setContentText("Please enter correct employee id")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(Dialog::dismiss)
                                .show();
                    } else if (status.equalsIgnoreCase("0")) {
                        new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.WARNING_TYPE)
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

    String statuss = "";
    String messagee = "";
    String id = "";
    String code = "";
    String namee = "";
    int i = 0;

    public void StationListUrl() {

        String reasonListUrl = ApiCall.API_URL + "stationlist";

        requestQueue = Volley.newRequestQueue(LoginScreen.this);

        Log.e("reasonListUrl", "" + reasonListUrl);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, reasonListUrl, null,
                response -> {
                    final ArrayList<String> reasonStrings = new ArrayList<>();

                    try {

                        JSONArray ja = response.getJSONArray("list");

                        if (i == 0) {

                            statuss = "";
                            messagee = "";
                            id = "";
                            code = "";
                            namee = "Select location";

                            reasonStrings.add(namee);
                            stationList = new StationList(statuss, messagee, id, code, namee);
                            stationListArrayList.add(stationList);

                        }
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);

                            statuss = jsonObject.getString("status");
                            messagee = jsonObject.getString("message");
                            id = jsonObject.getString("id");
                            code = jsonObject.getString("code");
                            namee = jsonObject.getString("name");

                            reasonStrings.add(namee);
                            stationList = new StationList(statuss, messagee, id, code, namee);
                            stationListArrayList.add(stationList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mainMaterialSpinner.setTextColor(Color.parseColor("#000000"));
                    mainMaterialSpinner.setItems(reasonStrings);
                },

                error -> Log.e("Volley", "Error")
        );
        jor.setRetryPolicy(new DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jor);

    }
}


