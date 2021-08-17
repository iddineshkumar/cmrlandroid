package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.cm.cmrl.R;
import com.cm.cmrl.alerter.Alerter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.materialeditext.MaterialEditText;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class ChangepawwordActivity   extends AppCompatActivity {

    MaterialEditText passwordMaterialEditText,confirmPasswordMaterialEditText;

    LinearLayout wholeLayout;

    Button submitButton;

    Dialog  dialog;

    RequestQueue requestQueue;

    String status  = "",empid="";
    String message  = "",userLevel="";

    SessionManager sessionManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        sessionManager = new SessionManager(ChangepawwordActivity.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        passwordMaterialEditText = findViewById(R.id.password);
        confirmPasswordMaterialEditText = findViewById(R.id.confirm_password);

        submitButton =  findViewById(R.id.change_pass_button);
        wholeLayout = findViewById(R.id.whole_layout);

        passwordMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            passwordMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });

        confirmPasswordMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            confirmPasswordMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });

        wholeLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(wholeLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });

        submitButton.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(submitButton.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if (Objects.requireNonNull(confirmPasswordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("") && Objects.requireNonNull(passwordMaterialEditText.getText()).toString().trim().equalsIgnoreCase("")) {

                confirmPasswordMaterialEditText.setError("Enter strong password");
                passwordMaterialEditText.setError("Enter strong old password");


            }else if (confirmPasswordMaterialEditText.getText().toString().equalsIgnoreCase("") && Objects.requireNonNull(passwordMaterialEditText.getText()).toString().equalsIgnoreCase("")) {

                confirmPasswordMaterialEditText.setError("Enter password");
                passwordMaterialEditText.setError("Enter old password");


            } else if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().equalsIgnoreCase("")) {

                passwordMaterialEditText.setError("Enter old password");

            } else if (confirmPasswordMaterialEditText.getText().toString().equalsIgnoreCase("")) {

                confirmPasswordMaterialEditText.setError("Enter password");
            }else if (passwordMaterialEditText.getText().toString().trim().equalsIgnoreCase("")) {

                passwordMaterialEditText.setError("Enter strong old password");

            } else if (confirmPasswordMaterialEditText.getText().toString().trim().equalsIgnoreCase("")) {

                confirmPasswordMaterialEditText.setError("Enter strong password");
            }
            else {

                ChangePassword(ApiCall.API_URL+"change_password.php?empid="+empid+"&old_pwd="+passwordMaterialEditText.getText().toString().replace(" ","%20")+"&new_pwd="+confirmPasswordMaterialEditText.getText().toString().replace(" ","%20"));
            }
        });


    }



    public void ChangePassword(String url) {


        dialog = new Dialog(ChangepawwordActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(ChangepawwordActivity.this);

        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("response");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            status = jsonObject.getString("status");
                            message = jsonObject.getString("message");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    if(status.equalsIgnoreCase("1")){

                        new SweetAlertDialog(ChangepawwordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();

                                    if(userLevel.equalsIgnoreCase("4")){

                                        Intent intent = new Intent(ChangepawwordActivity.this, DepartmentListClass.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                                    }else if(userLevel.equalsIgnoreCase("5")){

                                        Intent intent = new Intent(ChangepawwordActivity.this, MAinfragmentActivty.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.new_right, R.anim.new_left);

                                    }
                                })
                                .show();
                    }
                    else {

                        Alerter.create(ChangepawwordActivity.this)
                                .setTitle("CMRL")
                                .setText(message)
                                .setBackgroundColor(R.color.colorAccent)
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

