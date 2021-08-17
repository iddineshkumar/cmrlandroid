package com.cm.cmrl.maintain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.cm.cmrl.R;
import com.cm.cmrl.ups.UpsActivity;
import com.cm.cmrl.ups.UpsTimeFragment;
import com.cm.cmrl.view.PointsOverlayView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class decodeScnner extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, QRCodeReaderView.OnQRCodeReadListener{

        private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    private ViewGroup mainLayout;

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private PointsOverlayView pointsOverlayView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.activity_decode, container,
                false);
        LinearLayout sidemenuLayout = view.findViewById(R.id.back_layout);
        mainLayout = view.findViewById(R.id.main_layout);
        sidemenuLayout.setVisibility(View.INVISIBLE);
        sidemenuLayout.setOnClickListener(view1 -> MaintainFragmentActivity.drawerLayout.openDrawer(MaintainFragmentActivity.nvDrawer));

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(),UpsActivity.class);
                    startActivity(intent);

                    return true;
                }
            }
            return false;
        });
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        resultTextView.setText(text);

        Bundle bundle =new Bundle();
        bundle.putString("name","SKO-UPS 1");
        UpsTimeFragment orderManagement = new UpsTimeFragment();
        orderManagement.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();
        }

        pointsOverlayView.setPoints(points);
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", view -> ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA)).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void initQRCodeReaderView() {
        View content = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);

        qrCodeReaderView =  content.findViewById(R.id.qrdecoderview);
        resultTextView =  content.findViewById(R.id.result_text_view);
        CheckBox flashlightCheckBox = content.findViewById(R.id.flashlight_checkbox);
        CheckBox enableDecodingCheckBox = content.findViewById(R.id.enable_decoding_checkbox);
        pointsOverlayView =  content.findViewById(R.id.points_overlay_view);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        flashlightCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> qrCodeReaderView.setTorchEnabled(isChecked));
        enableDecodingCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> qrCodeReaderView.setQRDecodingEnabled(isChecked));
        qrCodeReaderView.startCamera();
    }
}
