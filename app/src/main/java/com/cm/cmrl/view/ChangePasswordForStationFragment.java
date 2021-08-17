package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.interfaces.OnKeyboardVisibilityListener;
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

public class ChangePasswordForStationFragment extends Fragment implements OnKeyboardVisibilityListener {

    private MaterialEditText passwordMaterialEditText, confirmPasswordMaterialEditText, newPasswordMaterialEdittext;

    private LinearLayout wholeLayout;

    private Button submitButton;

    private Dialog  dialog;

    private String status = "", empid = "";
    private String message = "";

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.change_pass, container,
                false);

        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        //String userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);

        passwordMaterialEditText =  view.findViewById(R.id.password);
        confirmPasswordMaterialEditText =  view.findViewById(R.id.confirm_password);
        newPasswordMaterialEdittext =  view.findViewById(R.id.new_password);

        LinearLayout sidemenuLayout = view.findViewById(R.id.back_layout);
        wholeLayout =  view.findViewById(R.id.whole_layout);

        submitButton =  view.findViewById(R.id.change_pass_button);

        passwordMaterialEditText.setOnTouchListener((view1, motionEvent) -> {

            passwordMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });
        newPasswordMaterialEdittext.setOnTouchListener((view12, motionEvent) -> {

            newPasswordMaterialEdittext.setFocusableInTouchMode(true);

            return false;
        });
        confirmPasswordMaterialEditText.setOnTouchListener((view13, motionEvent) -> {

            confirmPasswordMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });

        sidemenuLayout.setOnClickListener(view14 -> StationActivity.drawerLayout.openDrawer(StationActivity.nvDrawer));
        wholeLayout.setOnClickListener(view15 -> {

            InputMethodManager in = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(wholeLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });

        submitButton.setOnClickListener(view16 -> {

            InputMethodManager in = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(submitButton.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }


            if (Objects.requireNonNull(passwordMaterialEditText.getText()).toString().length() == 0) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Enter current password")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();
            } else if (Objects.requireNonNull(newPasswordMaterialEdittext.getText()).toString().length() == 0) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Enter new password")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();
            } else if (Objects.requireNonNull(confirmPasswordMaterialEditText.getText()).toString().length() == 0) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Enter confirm password")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();
            } else if (!newPasswordMaterialEdittext.getText().toString().equalsIgnoreCase(confirmPasswordMaterialEditText.getText().toString())) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Confirm both new password and confirm password")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();
            } else {
                ChangePassword(ApiCall.API_URL + "change_password.php?empid=" + empid + "&old_pwd=" + passwordMaterialEditText.getText().toString().replace(" ", "%20") + "&new_pwd=" + confirmPasswordMaterialEditText.getText().toString());
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(), StationActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.new_right, R.anim.new_left);

                    return true;
                }
            }
            return false;
        });
        setKeyboardVisibilityListener(this);

        return view;
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }

    /**
     * @param url call the api to change the password
     */
    private void ChangePassword(String url) {


        dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
                    if (status.equalsIgnoreCase("1")) {

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();

                                    Intent intent = new Intent(getActivity(), StationActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();
                    } else {

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (!visible) {
            passwordMaterialEditText.setFocusable(false);
            confirmPasswordMaterialEditText.setFocusable(false);
        }
    }
}
