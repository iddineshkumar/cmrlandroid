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
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.DeparmentSpinnerList;
import com.cm.cmrl.arraylist.IssueList;
import com.cm.cmrl.arraylist.LocationList;
import com.cm.cmrl.arraylist.PriorityList;
import com.cm.cmrl.materialspinner.MaterialSpinner;
import com.cm.cmrl.photoview.PhotoView;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.cm.cmrl.adapter.IssueAdapter;
import com.cm.cmrl.adapter.IssueTwoAdapter;

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

public class StationEditLayout extends AppCompatActivity implements View.OnClickListener {

    GridView injuryGridView;
    IssueAdapter issueAdapter;

    IssueList issueList;
    DeparmentSpinnerList deparmentSpinnerList;
    PriorityList priorityList;
    LocationList locationList;

    ArrayList<IssueList> issueListArrayList = new ArrayList<>();
    ArrayList<DeparmentSpinnerList> deparmentSpinnerListArrayList = new ArrayList<>();
    ArrayList<PriorityList> priorityListArrayList = new ArrayList<>();
    ArrayList<LocationList> locationListArrayList = new ArrayList<>();

    CharSequence[] items;

    ProgressDialog dialogg;

    String imagepath = "", departmentString = "", priorityString = "", locationString = "", status = "";
    String message = "";
    String empid = "", station_code = "", title = "";

    Dialog alertDialog, dialog;

    int serverResponseCode = 0;

    LinearLayout addUploadLinearLayout, backLayout, wholeLayout;

    int position = 0;

    RequestQueue requestQueue;

    TextView departmentCustomFontTextView, priorityCustomFontTextView;

    EditText locationCustomFontTextView;
    MaterialSpinner departmentMaterialSpinner, priorityMaterialSpinner, locationMaterialSpinner;

    Typeface boldTypeface, normalTypeface;

    EditText titleEditText, mesasageEditText;

    Button addButton;

    SessionManager sessionManager;

    LinearLayout clearButton;
    private String TAG ="StationEditLayout";

    String image = "", departmentIdd = "", descc = "", prioriy = "", locationn = "", ticketId = "", ticketStatus = "", thishistoryId = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.w(TAG,"onCreate-->");
        setContentView(R.layout.station_edit_layout);

        title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        prioriy = getIntent().getExtras().getString("priority");
        priorityString = getIntent().getExtras().getString("priorityId");
        locationn = getIntent().getExtras().getString("location");
        descc = getIntent().getExtras().getString("description");
        departmentIdd = getIntent().getExtras().getString("departmenttt");
        image = getIntent().getExtras().getString("image");
        ticketId = getIntent().getExtras().getString("ticketId");
        departmentString = getIntent().getExtras().getString("code");
        ticketStatus = getIntent().getExtras().getString("ticketStatus");
        thishistoryId = getIntent().getExtras().getString("Tickethistory_id");


        Log.e("departmentIdd", "" + departmentIdd);
        sessionManager = new SessionManager(StationEditLayout.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);

        boldTypeface = Typeface
                .createFromAsset(StationEditLayout.this.getAssets(), "fonts/bolod_gothici.TTF");
        normalTypeface = Typeface
                .createFromAsset(StationEditLayout.this.getAssets(), "fonts/regular_gothici.TTF");

        addUploadLinearLayout = findViewById(R.id.add_upload_image_layout);
        backLayout = findViewById(R.id.back_layout);
        wholeLayout = findViewById(R.id.main_layout);

        titleEditText = findViewById(R.id.issue_edit);
        mesasageEditText = findViewById(R.id.complanit_edit);

        addButton = findViewById(R.id.add_button);
        clearButton = findViewById(R.id.clear_button);

        departmentMaterialSpinner = findViewById(R.id.department_spinner);
        priorityMaterialSpinner = findViewById(R.id.priority_spinner);
        locationMaterialSpinner = findViewById(R.id.location_spinner);

        departmentCustomFontTextView = findViewById(R.id.department_text);
        priorityCustomFontTextView = findViewById(R.id.priority_text);
        locationCustomFontTextView =  findViewById(R.id.location_text);

        injuryGridView =  findViewById(R.id.add_image_grid_view);
        items = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};

        titleEditText.setTypeface(normalTypeface);
        mesasageEditText.setTypeface(normalTypeface);
        locationCustomFontTextView.setTypeface(normalTypeface);

        titleEditText.setFocusable(false);
        mesasageEditText.setFocusable(false);
        locationCustomFontTextView.setFocusable(false);
        titleEditText.setText(title);
        mesasageEditText.setText(descc);
        locationCustomFontTextView.setText(locationn);
        titleEditText.setOnTouchListener((view, motionEvent) -> {

            titleEditText.setFocusableInTouchMode(true);

            return false;
        });
        locationCustomFontTextView.setOnTouchListener((view, motionEvent) -> {

            locationCustomFontTextView.setFocusableInTouchMode(true);

            return false;
        });

        mesasageEditText.setOnTouchListener((view, motionEvent) -> {

            mesasageEditText.setFocusableInTouchMode(true);

            return false;
        });
        departmentMaterialSpinner.setOnClickListener(view -> {

            titleEditText.setFocusable(false);
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(departmentMaterialSpinner.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });

      /*  locationMaterialSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleEditText.setFocusable(false);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(locationMaterialSpinner.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });*/
        priorityMaterialSpinner.setOnClickListener(view -> titleEditText.setFocusable(false));

        departmentMaterialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {

            departmentCustomFontTextView.setText(deparmentSpinnerListArrayList.get(position).getName());
            departmentString = deparmentSpinnerListArrayList.get(position).getCode();
            departmentMaterialSpinner.setText("");
            priorityMaterialSpinner.setText("");
            locationMaterialSpinner.setText("");

        });


        priorityMaterialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {

            priorityCustomFontTextView.setText(priorityListArrayList.get(position).getName());
            priorityString = priorityListArrayList.get(position).getId();
            departmentMaterialSpinner.setText("");
            priorityMaterialSpinner.setText("");
            locationMaterialSpinner.setText("");
        });

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
            final float Image_COL_WIDTH = StationEditLayout.this.getResources().getDisplayMetrics().density * Imagewith;
            int Image_width = Math.round(Image_COL_WIDTH);

            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
            injuryGridView.setLayoutParams(lpp);

            IssueTwoAdapter issueAdapter = new IssueTwoAdapter(StationEditLayout.this, issueListArrayList);
            injuryGridView.setAdapter(issueAdapter);

            Log.e("issueListArrayList", "" + issueListArrayList.size());

        }


        injuryGridView.setOnItemClickListener((adapterView, v, i, l) -> {

            items = new CharSequence[]{"Remove Image", "View Image", "Cancel"};

            selectImage(i);
        });

        wholeLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(wholeLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });

        backLayout.setOnClickListener(view -> onBackPressed());

        addUploadLinearLayout.setOnClickListener(view -> {

            items = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            selectImage(position);

        });

        clearButton.setOnClickListener(this);


        addButton.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(addButton.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            StringBuilder issuseImages = new StringBuilder();
            if (!issueListArrayList.isEmpty()) {

                for (int i = 0; i < issueListArrayList.size(); i++) {

                    issuseImages.append(",").append(issueListArrayList.get(i).getImageUrl().replace(ApiCall.BASE_URL + "assets/uploads/", ""));
                }

                issuseImages = new StringBuilder(issuseImages.substring(1));
            }

            if (departmentCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please select department")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (priorityCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please select priority")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (locationCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter location")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (titleEditText.getText().toString().trim().equalsIgnoreCase("")) {
                new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter title")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (mesasageEditText.getText().toString().trim().equalsIgnoreCase("")) {
                new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter message")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else {

                locationString = locationCustomFontTextView.getText().toString();

                String title = mesasageEditText.getText().toString().replace("\n", "").replace("\r", "").replace(" ", "%20");

                UpdateIssuse(ApiCall.API_URL + "thistoryupdate_bysc.php?empid=" + empid + "&ticket_id=" + ticketId + "&tickethistory_id=" + thishistoryId + "&ticket_title=" + titleEditText.getText().toString().replace(" ", "%20") + "&dept_code=" + departmentString.replace(" ", "%20") + "&priority=" + priorityString.replace(" ", "%20") + "&location=" + locationString.replace(" ", "%20") + "&description=" + title + "&ticket_status=" + ticketStatus + "&photo=" + issuseImages);
            }
        });


        DepartmentPriortiy();
    }

    String departmentStatuss = "";
    String departmentMessagee = "";
    String departmentId = "";
    String departmentCode = "";
    String departmentName = "";

    String priorityStatuss = "";
    String priorityMessagee = "";
    String priorityId = "";
    String priorityName = "";

    String locationStatuss = "";
    String locationMessagee = "";
    String locationId = "";
    String locationName = "";

    int i = 0;
    int j = 0;


    /**
     * Call api for get department and location list
     */
    public void DepartmentPriortiy() {

        dialog = new Dialog(StationEditLayout.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        String reasonListUrl = ApiCall.API_URL + "dept_priority_location";

        requestQueue = Volley.newRequestQueue(StationEditLayout.this);


        Log.e("reasonListUrl", "" + reasonListUrl);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, reasonListUrl, null,
                response -> {
                    final ArrayList<String> departmentStrings = new ArrayList<>();
                    final ArrayList<String> priorityStrings = new ArrayList<>();
                    final ArrayList<String> locationStrings = new ArrayList<>();

                    try {

                        JSONArray ja = response.getJSONArray("dlist");
                        JSONArray priortiyJsonArray = response.getJSONArray("plist");
                        JSONArray locationJsonArray = response.getJSONArray("llist");
                        if (j == 0) {

                            priorityStatuss = "";
                            priorityMessagee = "";
                            priorityId = "";
                            priorityName = "Select priority";

                            priorityStrings.add(priorityName);
                            priorityList = new PriorityList(priorityStatuss, priorityMessagee, priorityId, priorityName);
                            priorityListArrayList.add(priorityList);

                        }

                        for (int j = 0; j < priortiyJsonArray.length(); j++) {


                            JSONObject jsonObject = priortiyJsonArray.getJSONObject(j);

                            Log.e("priortiyjsonObject", "" + jsonObject);
                            priorityStatuss = jsonObject.getString("status");
                            priorityMessagee = jsonObject.getString("message");
                            priorityId = jsonObject.getString("id");
                            priorityName = jsonObject.getString("name");

                            priorityStrings.add(priorityName);
                            priorityList = new PriorityList(priorityStatuss, priorityMessagee, priorityId, priorityName);
                            priorityListArrayList.add(priorityList);

                        }


                        if (i == 0) {

                            locationStatuss = "";
                            locationMessagee = "";
                            locationId = "";
                            locationName = "Select location";

                            locationStrings.add(locationName);

                            locationList = new LocationList(locationStatuss, locationMessagee, locationId, locationName);
                            locationListArrayList.add(locationList);

                        }

                        for (int i = 0; i < locationJsonArray.length(); i++) {


                            JSONObject jsonObject = locationJsonArray.getJSONObject(i);
                            locationStatuss = jsonObject.getString("status");
                            locationMessagee = jsonObject.getString("message");
                            locationId = jsonObject.getString("id");
                            locationName = jsonObject.getString("name");

                            locationStrings.add(locationName);
                            locationList = new LocationList(locationStatuss, locationMessagee, locationId, locationName);
                            locationListArrayList.add(locationList);


                        }

                        Log.e("priortiyJsonArray", "" + priortiyJsonArray);
                        if (i == 0) {

                            departmentStatuss = "";
                            departmentMessagee = "";
                            departmentId = "";
                            departmentCode = "";
                            departmentName = "Select department";

                            departmentStrings.add(departmentName);
                            deparmentSpinnerList = new DeparmentSpinnerList(departmentStatuss, departmentMessagee, departmentId, departmentCode, departmentName);
                            deparmentSpinnerListArrayList.add(deparmentSpinnerList);

                        }
                        for (int i = 0; i < ja.length() + 1; i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            departmentStatuss = jsonObject.getString("status");
                            departmentMessagee = jsonObject.getString("message");
                            departmentId = jsonObject.getString("id");
                            departmentCode = jsonObject.getString("code");
                            departmentName = jsonObject.getString("name");

                            departmentStrings.add(departmentName);
                            deparmentSpinnerList = new DeparmentSpinnerList(departmentStatuss, departmentMessagee, departmentId, departmentCode, departmentName);
                            deparmentSpinnerListArrayList.add(deparmentSpinnerList);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    dialog.dismiss();
                    departmentMaterialSpinner.setTextColor(Color.parseColor("#000000"));
                    departmentMaterialSpinner.setItems(departmentStrings);

                    priorityMaterialSpinner.setTextColor(Color.parseColor("#000000"));
                    priorityMaterialSpinner.setItems(priorityStrings);

                    locationMaterialSpinner.setTextColor(Color.parseColor("#000000"));
                    locationMaterialSpinner.setItems(locationStrings);

                    departmentMaterialSpinner.setText("");
                    priorityMaterialSpinner.setText("");
                    locationMaterialSpinner.setText("");


                    priorityCustomFontTextView.setText(prioriy);
                    departmentCustomFontTextView.setText(departmentString);

                    for (int m = 0; m < deparmentSpinnerListArrayList.size(); m++) {

                        if (deparmentSpinnerListArrayList.get(m).getName().equalsIgnoreCase(departmentIdd)) {
                            departmentString = deparmentSpinnerListArrayList.get(m).getCode();
                            Log.e("idValue", "" + deparmentSpinnerListArrayList.get(m).getId());

                        }

                    }


                },

                error -> {
                    Log.e("Volley", "Error");
                    dialog.dismiss();
                }
        );

        requestQueue.add(jor);

    }

    /**
     * @param bitmap convert file to bitmap and compress the image size
     */
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
            MediaScannerConnection.scanFile(StationEditLayout.this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
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
            dialogg = ProgressDialog.show(StationEditLayout.this, "", "Uploading Image...", true);
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
              //  Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, 120, 120, false);
                dialogg = ProgressDialog.show(StationEditLayout.this, "", "Uploading Image...", true);
                bitmapConvertToFile(bmp);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = StationEditLayout.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void cameraIntent() {
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Intent install = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkURI = FileProvider.getUriForFile(
                StationEditLayout.this,
                StationEditLayout.this.getApplicationContext()
                        .getPackageName() + ".provider", file);
        install.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);

        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        StationEditLayout.this.startActivityForResult(install, 2);
      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 2);*/
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void selectImage(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StationEditLayout.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                cameraIntent();

            } else if (items[item].equals("Choose from Gallery")) {
                galleryIntent();

            } else if (items[item].equals("Cancel")) {

                dialog.dismiss();

            } else if (items[item].equals("Remove Image")) {

                issueListArrayList.remove(position);

                IssueTwoAdapter issueAdapter = new IssueTwoAdapter(StationEditLayout.this, issueListArrayList);
                injuryGridView.setAdapter(issueAdapter);


            } else if (items[item].equals("View Image")) {


                alertDialog = new Dialog(StationEditLayout.this, R.style.DialogTheme);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.setContentView(R.layout.full_screen_popup_layout);

                PhotoView photoView = alertDialog.findViewById(R.id.iv_photo);
                Glide.with(StationEditLayout.this)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + issueListArrayList.get(position).getImageUrl())
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

        //sourceFileUri.replace(sourceFileUri, "ashifaq");
        //

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
        String tag = name + ".jpg";
        final String fileName = sourceFileUri.replace(sourceFileUri, tag);

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

            Log.e("uploadFile", "Source File not exist :" + imagepath);

            runOnUiThread(() -> {

                Toast.makeText(StationEditLayout.this, "Source File not exist :" + imagepath, Toast.LENGTH_LONG).show();
                // messageText.setText("Source File not exist :"+ imagepath);
            });

        } else {
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

                if (serverResponseCode == 200) {

                    runOnUiThread(() -> {

                        Log.e("ImageUrl", "" + fileName);

                        issueList = new IssueList(fileName);
                        issueListArrayList.add(issueList);

                        int size = issueListArrayList.size();
                        injuryGridView.setNumColumns(size);
                        int Imagewith = size * 100;
                        final float Image_COL_WIDTH = StationEditLayout.this.getResources().getDisplayMetrics().density * Imagewith;
                        int Image_width = Math.round(Image_COL_WIDTH);


                        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
                        injuryGridView.setLayoutParams(lpp);


                        IssueTwoAdapter issueAdapter = new IssueTwoAdapter(StationEditLayout.this, issueListArrayList);
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

                runOnUiThread(() -> {
                    //  messageText.setText("MalformedURLException Exception : check script url.");
                    Toast.makeText(StationEditLayout.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialogg.dismiss();
                e.printStackTrace();

                runOnUiThread(() -> {
                    //  messageText.setText("Got Exception : see logcat ");
                    Toast.makeText(StationEditLayout.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialogg.dismiss();

        }
    }



    @Override
    public void onClick(View view) {

         if (view.getId() == R.id.clear_button) {
            departmentCustomFontTextView.setText("");
            priorityCustomFontTextView.setText("");
            locationCustomFontTextView.setText("");
            titleEditText.setText("");
            mesasageEditText.setText("");

            issueListArrayList.clear();

            int size = issueListArrayList.size();
            injuryGridView.setNumColumns(size);
            int Imagewith = size * 100;
            final float Image_COL_WIDTH = StationEditLayout.this.getResources().getDisplayMetrics().density * Imagewith;
            int Image_width = Math.round(Image_COL_WIDTH);


            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
            injuryGridView.setLayoutParams(lpp);


            issueAdapter = new IssueAdapter(StationEditLayout.this, issueListArrayList);
            issueAdapter = new IssueAdapter(StationEditLayout.this, issueListArrayList);
            injuryGridView.setAdapter(issueAdapter);

         }
    }


    public void UpdateIssuse(String url) {


        dialog = new Dialog(StationEditLayout.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(StationEditLayout.this);

        Log.e("url", "" + url);
        Log.w(TAG,"URL"+url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("response");

                        Log.w(TAG,"Response--->"+ja);


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

                        new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();
                                    Intent intent = new Intent(StationEditLayout.this, ViewTickets.class);
                                    intent.putExtra("ticketId", ticketId);
                                    intent.putExtra("departmentCode", DepartmentStatusListClass.departmentCode);
                                    intent.putExtra("stationLocation", DepartmentStatusListClass.locName);
                                    intent.putExtra("ticket_status", DepartmentStatusListClass.ticketStatus);
                                    intent.putExtra("title", title);
                                    intent.putExtra("ticketupdate","ticketupdate");
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();
                    } else {

                        new SweetAlertDialog(StationEditLayout.this, SweetAlertDialog.WARNING_TYPE)
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

