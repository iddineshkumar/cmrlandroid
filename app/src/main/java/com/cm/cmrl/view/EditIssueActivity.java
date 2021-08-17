package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cm.cmrl.R;
import com.cm.cmrl.alerter.Alerter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.IssueList;
import com.cm.cmrl.materialspinner.MaterialSpinner;
import com.cm.cmrl.photoview.PhotoView;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;

import com.cm.cmrl.adapter.IssueTwoAdapter;
import com.cm.cmrl.utils.ConnectionDetector;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class EditIssueActivity extends AppCompatActivity {

    CharSequence[] statusString;
    CharSequence[] items;

    EditText issuseEditText;


    MaterialSpinner statusMaterialSpinner;

    Typeface normalTypeface,boldTypeface;

    TextView statusCustomFontTextView;

    Button submitButton;

    SessionManager sessionManager;

    String empid="",status="",ticketId="",message="",ticketStatus="",thishistoryId="";
    String imagepath="",userLevel="",title="";
    String is_status_changed="",updated_date="",urlstatus;
    String networkStatus = "";

    String image="",descrition="";

    Dialog alertDialog, dialog;

    RequestQueue requestQueue;

    GridView injuryGridView;

    int position = 0;
    int serverResponseCode = 0;

    ProgressDialog dialogg;

    IssueList issueList;

    ArrayList<IssueList> issueListArrayList=new ArrayList<>();
    LinearLayout backLayout,addUploadLinearLayout;
    LinearLayout wholeLayout;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_issse_layout);

        sessionManager = new SessionManager(EditIssueActivity.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        ticketStatus= Objects.requireNonNull(getIntent().getExtras()).getString("ticketStatus");
        descrition=getIntent().getExtras().getString("description");
        ticketId=getIntent().getExtras().getString("ticketId");
        image = getIntent().getExtras().getString("photo");
        image = getIntent().getExtras().getString("photo");
        thishistoryId =getIntent().getExtras().getString("Tickethistory_id");
        title=getIntent().getExtras().getString("title");
        boldTypeface = Typeface
                .createFromAsset(EditIssueActivity.this.getAssets(), "fonts/bolod_gothici.TTF");
        normalTypeface = Typeface
                .createFromAsset(EditIssueActivity.this.getAssets(), "fonts/regular_gothici.TTF");

        addUploadLinearLayout = findViewById(R.id.add_upload_image_layout);
        wholeLayout = findViewById(R.id.whole_layout);
        backLayout = findViewById(R.id.back_layout);

        issuseEditText = findViewById(R.id.complanit_edit);

        statusMaterialSpinner = findViewById(R.id.location_spinner);

        statusCustomFontTextView = findViewById(R.id.location_text);

        submitButton = findViewById(R.id.add_button);

        injuryGridView = findViewById(R.id.add_image_grid_view);

        issuseEditText.setTypeface(normalTypeface);



        if(userLevel.equalsIgnoreCase("4")){


            if(ticketStatus.equalsIgnoreCase("4")){

                statusString=new CharSequence[] {"Select status","Pending","Close"};

            }else  if(ticketStatus.equalsIgnoreCase("5")){

                statusString=new CharSequence[]{"Open","In progress","Pending","completed"};
            }else {

                statusString=new CharSequence[]{"Select status","In progress","Pending","completed"};
            }

        }else  if(userLevel.equalsIgnoreCase("5")){

            if(ticketStatus.equalsIgnoreCase("4")){

                statusString=new CharSequence[]{"Select status","In progress","Pending","completed"};


            }else  if(ticketStatus.equalsIgnoreCase("5")){

                statusString=new CharSequence[]{"Select status","In progress","Pending","completed"};

            }else {

                statusString=new CharSequence[]{"Select status","In progress","Pending","completed"};
            }

        }

        if(ticketStatus.equalsIgnoreCase("1")){

            statusCustomFontTextView.setText("Open");

        }else   if(ticketStatus.equalsIgnoreCase("2")){

            statusCustomFontTextView.setText("In progress");

        }else   if(ticketStatus.equalsIgnoreCase("3")){

            statusCustomFontTextView.setText("Pending");

        }else   if(ticketStatus.equalsIgnoreCase("4")){

            statusCustomFontTextView.setText("Completed");

        }else   if(ticketStatus.equalsIgnoreCase("5")){

            statusCustomFontTextView.setText("Closed");

        }



        statusMaterialSpinner.setText("");

        statusMaterialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(statusMaterialSpinner.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            statusMaterialSpinner.setText("");
            statusCustomFontTextView.setText(statusString[position]);

        });


        statusMaterialSpinner.setTextColor(Color.parseColor("#000000"));
        statusMaterialSpinner.setItems(statusString);
        statusMaterialSpinner.setText("");
        issuseEditText.setText(descrition);


        issuseEditText.setFocusable(false);

        issuseEditText.setOnTouchListener((view, motionEvent) -> {

            issuseEditText.setFocusableInTouchMode(true);

            return false;
        });

        backLayout.setOnClickListener(view -> onBackPressed());


        String[] array = image.split(",");

        if (array.length == 0) {
            Log.w("","");
        } else {

            for (String s : array) {

                issueList = new IssueList(s);
                issueListArrayList.add(issueList);
            }

            // Calculate height and width for horizontal GridView
            int size = issueListArrayList.size();
            injuryGridView.setNumColumns(size);
            int Imagewith = size * 100;
            final float Image_COL_WIDTH = EditIssueActivity.this.getResources().getDisplayMetrics().density * Imagewith;
            int Image_width = Math.round(Image_COL_WIDTH);

            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
            injuryGridView.setLayoutParams(lpp);

            IssueTwoAdapter issueAdapter = new IssueTwoAdapter(EditIssueActivity.this, issueListArrayList);
            injuryGridView.setAdapter(issueAdapter);

            Log.e("issueListArrayList",""+issueListArrayList.size());

        }

        addUploadLinearLayout.setOnClickListener(view -> {

            items = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            selectImage(position);

        });

        injuryGridView.setOnItemClickListener((adapterView, v, i, l) -> {

            items = new CharSequence[]{"Remove Image", "View Image", "Cancel"};
            selectImage(i);
        });


        // check whether internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            Snackbar snackbar = Snackbar
                    .make(wholeLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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

            networkStatus= ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if(networkStatus.equalsIgnoreCase("Not connected to Internet"))
            {
                Snackbar snackbar = Snackbar
                        .make(wholeLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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


            }else {
                String title;
                StringBuilder issuseImages= new StringBuilder();
                if (!issueListArrayList.isEmpty()) {

                    for (int i = 0; i < issueListArrayList.size(); i++) {

                        issuseImages.append(",").append(issueListArrayList.get(i).getImageUrl().replace(ApiCall.BASE_URL + "assets/uploads/", ""));

                    }
                    issuseImages = new StringBuilder(issuseImages.substring(1));
                }

                if(statusCustomFontTextView.getText().toString().trim().equalsIgnoreCase("")){

                    Alerter.create(EditIssueActivity.this)
                            .setTitle("CMRL")
                            .setText("Please select status")
                            .setBackgroundColor(R.color.colorAccent)
                            .show();

                }else if(statusCustomFontTextView.getText().toString().trim().equalsIgnoreCase("Select status")){

                    Alerter.create(EditIssueActivity.this)
                            .setTitle("CMRL")
                            .setText("Please select status")
                            .setBackgroundColor(R.color.colorAccent)
                            .show();

                }else  if(issuseEditText.getText().toString().trim().equalsIgnoreCase("")){

                    Alerter.create(EditIssueActivity.this)
                            .setTitle("CMRL")
                            .setText("Please enter message")
                            .setBackgroundColor(R.color.colorAccent)
                            .show();
                }
                else {

                    title=issuseEditText.getText().toString().replace("\n", "").replace("\r", "").replace(" ","%20");


                    AddIssuseURL(ApiCall.API_URL+"thistoryupdate_bydc.php?empid="+empid+"&ticket_id="+ticketId+"&tickethistory_id="+thishistoryId+"&remarks="+title+"&ticket_status="+status+"&photo="+ issuseImages.toString().replace(" ","%20"));
                }

            }
        });

    }
    @SuppressLint("SimpleDateFormat")
    public void bitmapConvertToFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        File bitmapFile;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory("image_crop_sample"), "");
            if (!file.exists()) {
                file.mkdir();
            }

            bitmapFile = new File(file, "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg");
            fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            MediaScannerConnection.scanFile(EditIssueActivity.this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(final String path, final Uri uri) {

                    new Thread(() -> uploadFile(path)).start();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception ignored) {
                }
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
            //addImagLinearLayout.setImageBitmap(bitmap);
            dialogg = ProgressDialog.show(EditIssueActivity.this, "", "Uploading Image...", true);
            bitmapConvertToFile(bitmap);

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : Objects.requireNonNull(f.listFiles())) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inSampleSize = 2;
                options.inJustDecodeBounds = false;
                options.inTempStorage = new byte[16 * 1024];

                Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                dialogg = ProgressDialog.show(EditIssueActivity.this, "", "Uploading Image...", true);
                bitmapConvertToFile(bmp);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = EditIssueActivity.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void cameraIntent() {

        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Intent install = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri apkURI = FileProvider.getUriForFile(
                EditIssueActivity.this,
                EditIssueActivity.this.getApplicationContext()
                        .getPackageName() + ".provider", file);
        install.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);

        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        EditIssueActivity.this.startActivityForResult(install,2);

    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void selectImage(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditIssueActivity.this);
        builder.setTitle("ADD PHOTO");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                cameraIntent();

            } else if (items[item].equals("Choose from Gallery")) {
                galleryIntent();

            } else if (items[item].equals("Cancel")) {

                dialog.dismiss();

            } else if (items[item].equals("Remove Image")) {

                issueListArrayList.remove(position);

                IssueTwoAdapter issueAdapter = new IssueTwoAdapter(EditIssueActivity.this, issueListArrayList);
                injuryGridView.setAdapter(issueAdapter);

            } else if (items[item].equals("View Image")) {

                alertDialog = new Dialog(EditIssueActivity.this, R.style.DialogTheme);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.setContentView(R.layout.full_screen_popup_layout);

                PhotoView photoView = alertDialog.findViewById(R.id.iv_photo);
                Glide.with(EditIssueActivity.this)
                        .load(ApiCall.BASE_URL+"assets/uploads/"+issueListArrayList.get(position).getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.no_image)
                        .into(photoView);
                alertDialog.show();

                photoView.setOnClickListener(view -> alertDialog.cancel());


            }
        });
        builder.show();
    }
    public void uploadFile(String sourceFileUri) {

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name=(hour+""+minute+""+second+""+day+""+(month+1)+""+year);
        String tag=name+".jpg";
        final String fileName = sourceFileUri.replace(sourceFileUri,tag);

        HttpURLConnection conn;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialogg.dismiss();

            Log.e("uploadFile", "Source File not exist :"+imagepath);

            runOnUiThread(() -> Toast.makeText(EditIssueActivity.this,"Source File not exist :"+ imagepath,Toast.LENGTH_LONG).show());

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(ApiCall.API_URL+"image_upload");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(() -> {

                        // messageText.setText(msg);
                        Log.e("ImageUrl", "" + fileName);

                        issueList = new IssueList(fileName);
                        issueListArrayList.add(issueList);

                        // Calculate width and height of the horizontal GridView
                        int size = issueListArrayList.size();
                        injuryGridView.setNumColumns(size);
                        int Imagewith = size * 100;
                        final float Image_COL_WIDTH = EditIssueActivity.this.getResources().getDisplayMetrics().density * Imagewith;
                        int Image_width = Math.round(Image_COL_WIDTH);

                        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
                        injuryGridView.setLayoutParams(lpp);

                        IssueTwoAdapter issueAdapter = new IssueTwoAdapter(EditIssueActivity.this, issueListArrayList);
                        injuryGridView.setAdapter(issueAdapter);
                    });
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialogg.dismiss();
                ex.printStackTrace();

                runOnUiThread(() -> Toast.makeText(EditIssueActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show());

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialogg.dismiss();
                e.printStackTrace();

                runOnUiThread(() -> Toast.makeText(EditIssueActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show());
                Log.e("Upload Exception", "Exception : "  + e.getMessage(), e);
            }
            dialogg.dismiss();

        }
    }
    public void AddIssuseURL(String url) {
        dialog = new Dialog(EditIssueActivity.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(EditIssueActivity.this);

        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("response");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            urlstatus = jsonObject.getString("status");
                            message = jsonObject.getString("message");
                            is_status_changed = jsonObject.getString("message");
                            updated_date = jsonObject.getString("message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if(urlstatus.equalsIgnoreCase("1")){

                        new SweetAlertDialog(EditIssueActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();

                                    Intent intent = new Intent(EditIssueActivity.this, ViewTickets.class);
                                    intent.putExtra("ticketId", ticketId);
                                    intent.putExtra("departmentCode", DepartmentStatusListClass.departmentCode);
                                    intent.putExtra("stationLocation", DepartmentStatusListClass.locName);
                                    intent.putExtra("ticket_status", DepartmentStatusListClass.ticketStatus);
                                    intent.putExtra("title", title);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();
                    }
                    else {

                        Alerter.create(EditIssueActivity.this)
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

    protected void onResume() {

        super.onResume();
        networkStatus= ConnectionDetector.getConnectivityStatusString(getApplicationContext());
        if(networkStatus.equalsIgnoreCase("Not connected to Internet"))
        {

            Snackbar snackbar = Snackbar
                    .make(wholeLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}

