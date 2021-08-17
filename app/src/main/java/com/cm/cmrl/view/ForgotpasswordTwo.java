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
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class ForgotpasswordTwo extends AppCompatActivity {

    MaterialEditText emailMaterialEditText;

    LinearLayout wholeLayout;

    Button submitButton;

    Dialog dialog;

    RequestQueue requestQueue;

    String status = "", message = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        emailMaterialEditText = findViewById(R.id.mobile_number);

        wholeLayout =  findViewById(R.id.forgot_first_screen_layout);

        submitButton = findViewById(R.id.forgot_sumbit_layout);

        emailMaterialEditText.setOnTouchListener((view, motionEvent) -> {

            emailMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });

        wholeLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(wholeLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            emailMaterialEditText.setFocusable(false);

        });

        submitButton.setOnClickListener(view -> {


            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(submitButton.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            emailMaterialEditText.setFocusable(false);

            if (Objects.requireNonNull(emailMaterialEditText.getText()).toString().equalsIgnoreCase("")) {


                emailMaterialEditText.setError("Enter your mobile number");
            } else {


                ForgotPasswordUrl(ApiCall.API_URL+"forgot_password.php?mobile=" + emailMaterialEditText.getText().toString().replace(" ", "%20"));
            }

        });

    }

    /**
     * @param url for Department member
     */
    public void ForgotPasswordUrl(String url) {


        dialog = new Dialog(ForgotpasswordTwo.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(ForgotpasswordTwo.this);


        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                        try {

                        JSONArray ja = response.getJSONArray("response");


                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

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

                        new SweetAlertDialog(ForgotpasswordTwo.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();
                                    Intent intent = new Intent(ForgotpasswordTwo.this, DepartmentLogin.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();
                    } else if (status.equalsIgnoreCase("0")) {

                        Alerter.create(ForgotpasswordTwo.this)
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

