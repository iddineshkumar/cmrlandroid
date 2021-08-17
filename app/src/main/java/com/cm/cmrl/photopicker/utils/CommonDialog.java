package com.cm.cmrl.photopicker.utils;

import android.content.DialogInterface.OnClickListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CommonDialog {
    public static void showDialogConfirm(AppCompatActivity activity, int message, String yesText, String noText, OnClickListener onYes, OnClickListener onNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton((CharSequence) yesText, onYes);
        builder.setNegativeButton((CharSequence) noText, onNo);
        builder.create().show();
    }

    public static void showInfoDialog(AppCompatActivity activity, int message, String yesText, OnClickListener onYes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton((CharSequence) yesText, onYes);
        builder.create().show();
    }
}
