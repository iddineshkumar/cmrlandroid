package com.cm.cmrl.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cm.cmrl.R;
import com.cm.cmrl.api.APIInterface;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.api.RetrofitClient;
import com.cm.cmrl.arraylist.DeparmentSpinnerList;
import com.cm.cmrl.arraylist.IssueList;
import com.cm.cmrl.arraylist.LocationList;
import com.cm.cmrl.arraylist.PriorityList;
import com.cm.cmrl.materialspinner.MaterialSpinner;
import com.cm.cmrl.model.ImageUploadResponse;
import com.cm.cmrl.photopicker.activity.PickImageActivity;
import com.cm.cmrl.photoview.PhotoView;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.cm.cmrl.adapter.IssueAdapter;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Iddinesh.
 */

public class AddIssueLayout extends AppCompatActivity implements View.OnClickListener {

    GridView injuryGridView;
    IssueAdapter issueAdapter;
    IssueList issueList;
    DeparmentSpinnerList deparmentSpinnerList;
    PriorityList priorityList;
    LocationList locationList;
    ArrayList<IssueList> issueListArrayList = new ArrayList<>();
    List<DeparmentSpinnerList> deparmentSpinnerListArrayList = new ArrayList<>();
    ArrayList<PriorityList> priorityListArrayList = new ArrayList<>();
    ArrayList<LocationList> locationListArrayList = new ArrayList<>();

    CharSequence[] items;

    ProgressDialog dialogg;

    String departmentString = "", priorityString = "", locationString = "", status = "";
    String message = "";
    String empid = "", station_code = "";
    Dialog alertDialog, dialog;
    LinearLayout addUploadLinearLayout, backLayout, wholeLayout;
    int position = 0;
    RequestQueue requestQueue;
    TextView departmentCustomFontTextView, priorityCustomFontTextView;
    MaterialSpinner departmentMaterialSpinner, priorityMaterialSpinner, locationMaterialSpinner;
    Typeface boldTypeface, normalTypeface;
    EditText titleEditText, mesasageEditText, locationCustomFontTextView;
    Button addButton;
    SessionManager sessionManager;
    LinearLayout clearButton;
    private static final int READ_STORAGE_CODE = 1001;
    private static final int WRITE_STORAGE_CODE = 1002;
    private ArrayList<String> pathList = new ArrayList<>();

    private String TAG ="AddIssueLayout";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_issue_layout);

        sessionManager = new SessionManager(AddIssueLayout.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);

        boldTypeface = Typeface
                .createFromAsset(AddIssueLayout.this.getAssets(), "fonts/bolod_gothici.TTF");
        normalTypeface = Typeface
                .createFromAsset(AddIssueLayout.this.getAssets(), "fonts/regular_gothici.TTF");

        addUploadLinearLayout =  findViewById(R.id.add_upload_image_layout);
        backLayout =  findViewById(R.id.back_layout);
        wholeLayout =  findViewById(R.id.main_layout);

        titleEditText = findViewById(R.id.issue_edit);
        mesasageEditText =  findViewById(R.id.complanit_edit);

        addButton =  findViewById(R.id.add_button);
        clearButton =  findViewById(R.id.clear_button);

        departmentMaterialSpinner =  findViewById(R.id.department_spinner);
        priorityMaterialSpinner =  findViewById(R.id.priority_spinner);
        locationMaterialSpinner = findViewById(R.id.location_spinner);

        departmentCustomFontTextView =  findViewById(R.id.department_text);
        priorityCustomFontTextView =  findViewById(R.id.priority_text);
        locationCustomFontTextView =  findViewById(R.id.location_text);

        injuryGridView =  findViewById(R.id.add_image_grid_view);
        items = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        titleEditText.setTypeface(normalTypeface);
        mesasageEditText.setTypeface(normalTypeface);
        titleEditText.setFocusable(false);
        mesasageEditText.setFocusable(false);

        titleEditText.setOnTouchListener((view, motionEvent) -> {

            titleEditText.setFocusableInTouchMode(true);

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


        priorityMaterialSpinner.setOnClickListener(view -> titleEditText.setFocusable(false));

        departmentMaterialSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            departmentCustomFontTextView.setText(deparmentSpinnerListArrayList.get(position).getName());
            departmentString = deparmentSpinnerListArrayList.get(position).getCode();
            Log.w(TAG,"DepartmentSpinner:"+"DeptName:"+deparmentSpinnerListArrayList.get(position).getName()+"\t"+"DeptCode :"+deparmentSpinnerListArrayList.get(position).getCode());
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

        backLayout.setOnClickListener(view -> {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(wholeLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            Intent intent = new Intent(AddIssueLayout.this, StationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });

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
            locationString = locationCustomFontTextView.getText().toString();
            StringBuilder issuseImages = new StringBuilder();
            if (!issueListArrayList.isEmpty()) {

                for (int i = 0; i < issueListArrayList.size(); i++) {

                    issuseImages.append(",").append(issueListArrayList.get(i).getImageUrl().replace(ApiCall.BASE_URL + "assets/uploads/", ""));
                }

                issuseImages = new StringBuilder(issuseImages.substring(1));
            }

            if (departmentCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please select department")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (priorityCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please select priority")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (locationCustomFontTextView.getText().toString().equalsIgnoreCase("")) {
                new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter location")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (titleEditText.getText().toString().trim().equalsIgnoreCase("")) {
                new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter title")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else if (mesasageEditText.getText().toString().trim().equalsIgnoreCase("")) {
                new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Please enter message")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();

            } else {

                String title = mesasageEditText.getText().toString().replace("\n", "").replace("\r", "").replace(" ", "%20");
                String fileName =issuseImages.substring(issuseImages.lastIndexOf("/")+1);
                Log.w("issuseImages--->",fileName);

                AddIssuseURL(ApiCall.API_URL + "createticket.php?empid=" + empid + "&station_code=" + station_code + "&title=" + titleEditText.getText().toString().replace(" ", "%20") + "&department_code=" + departmentString.replace(" ", "%20") + "&priority=" + priorityString.replace(" ", "%20") + "&description=" + title + "&location=" + locationString.replace(" ", "%20") + "&photo=" + issuseImages.toString().replace(" ", "%20"));
            }
        });

        if(deparmentSpinnerListArrayList.isEmpty()){
            DepartmentPriortiy();
        }

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

        dialog = new Dialog(AddIssueLayout.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        String reasonListUrl = ApiCall.API_URL + "dept_priority_location";

        requestQueue = Volley.newRequestQueue(AddIssueLayout.this);


        Log.e("reasonListUrl", "" + reasonListUrl);
        Log.w(TAG,"reasonListUrl :"+reasonListUrl);
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
            MediaScannerConnection.scanFile(AddIssueLayout.this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(final String path, final Uri uri) {

                    new Thread(() -> {

                        //uploadFile(path);
                        uploadProfileImageRequest(path);

                    }).start();

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

    @SuppressLint("SimpleDateFormat")
    public void bitmapConvert(ArrayList<String> uri) {
        FileOutputStream fileOutputStream = null;
        File bitmapFile;
        for (int i = 0; i < uri.size(); i++) {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory("image_crop_sample"), "");
                if (!file.exists()) {
                    file.mkdir();
                }

                bitmapFile = new File(file, "IMG_" + i + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg");
                fileOutputStream = new FileOutputStream(bitmapFile);
                Bitmap bitmap = BitmapFactory.decodeFile(uri.get(i));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                MediaScannerConnection.scanFile(AddIssueLayout.this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
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

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("onActivityResult", String.valueOf(requestCode) + resultCode);
        Log.w("PICKER_REQUEST_CODE", String.valueOf(PickImageActivity.PICKER_REQUEST_CODE));

        if (resultCode == -1 && requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            Log.w("this.pathList", String.valueOf(this.pathList));
            Log.w("KEY_DATA_RESULT", PickImageActivity.KEY_DATA_RESULT);
            this.pathList = Objects.requireNonNull(data.getExtras()).getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (this.pathList != null && !this.pathList.isEmpty()) {
                ArrayList<String> uris = new ArrayList<>(pathList);
                dialogg = ProgressDialog.show(AddIssueLayout.this, "", "Uploading Image...", true);
                bitmapConvert(uris);
            }
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
                dialogg = ProgressDialog.show(AddIssueLayout.this, "", "Uploading Image...", true);
                bitmapConvertToFile(bmp);

            } catch (Exception e) {
                e.printStackTrace();
                dialogg.dismiss();
            }
        }
    }
    private void cameraIntent() {
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Intent install = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri apkURI = FileProvider.getUriForFile(
                AddIssueLayout.this,
                AddIssueLayout.this.getApplicationContext()
                        .getPackageName() + ".provider", file);
        install.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);

        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        AddIssueLayout.this.startActivityForResult(install, 2);

    }



    private void openImagePickerIntent() {

        if (isPermissionGranted()) {
            Intent mIntent = new Intent(this, PickImageActivity.class);
            mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 60);
            mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 3);
            startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
        } else {
            requestPermission();
        }

    }

    private void selectImage(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddIssueLayout.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                cameraIntent();

            } else if (items[item].equals("Choose from Gallery")) {
                openImagePickerIntent();

            } else if (items[item].equals("Cancel")) {

                dialog.dismiss();

            } else if (items[item].equals("Remove Image")) {

                issueListArrayList.remove(position);
                issueAdapter.notifyDataSetChanged();


            } else if (items[item].equals("View Image")) {


                alertDialog = new Dialog(AddIssueLayout.this, R.style.DialogTheme);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.setContentView(R.layout.full_screen_popup_layout);

                PhotoView photoView = alertDialog.findViewById(R.id.iv_photo);
                Glide.with(AddIssueLayout.this)
                        .load(issueListArrayList.get(position).getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.no_image)
                        .into(photoView);
                alertDialog.show();

                photoView.setOnClickListener(view -> alertDialog.cancel());


            }
        });
        builder.show();
    }


    private void uploadProfileImageRequest(final String path) {
        APIInterface ApiService = RetrofitClient.getApiService();
        Call<ImageUploadResponse> call = ApiService.getImageStroeResponse(getProfileImagePicMultipart(path));
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImageUploadResponse> call, @NonNull retrofit2.Response<ImageUploadResponse> response) {
                dialogg.dismiss();
                Log.w("path",path);
                Log.w(TAG,"ImageUploadResponse"+"--->" + new Gson().toJson(response.body()));
                if (response.body() != null && 1 == response.body().getResponse().get(0).getStatus()) {
                    /*int day, month, year;
                    int second, minute, hour;
                    GregorianCalendar date = new GregorianCalendar();

                    day = date.get(Calendar.DAY_OF_MONTH);
                    month = date.get(Calendar.MONTH);
                    year = date.get(Calendar.YEAR);

                    second = date.get(Calendar.SECOND);
                    minute = date.get(Calendar.MINUTE);
                    hour = date.get(Calendar.HOUR);*/
                   // String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
                    //String tag = name + i + ".jpg";
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    Log.w("fileName", fileName);
                    String ImageUrl = ApiCall.BASE_URL +"assets/uploads/" + fileName;
                    //String ImageUrl = response.body().getResponse().get(0).getFilepath();
                    Log.w(TAG,"ImageURL"+ImageUrl);
                    issueList = new IssueList(ImageUrl);
                    issueListArrayList.add(issueList);

                    int size = issueListArrayList.size();
                    injuryGridView.setNumColumns(size);
                    int Imagewith = size * 100;
                    final float Image_COL_WIDTH = AddIssueLayout.this.getResources().getDisplayMetrics().density * Imagewith;
                    int Image_width = Math.round(Image_COL_WIDTH);


                    LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
                    injuryGridView.setLayoutParams(lpp);


                    issueAdapter = new IssueAdapter(AddIssueLayout.this, issueListArrayList);
                    injuryGridView.setAdapter(issueAdapter);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageUploadResponse> call, @NonNull Throwable t) {
                dialogg.dismiss();

                Log.w("Profile", "On failure working"+t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void uploadFile(final String path) {
        APIInterface ApiService = RetrofitClient.getApiService();
        Call<ImageUploadResponse> call = ApiService.getImageStroeResponse(getProfileImagePicMultipart(path));
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImageUploadResponse> call, @NonNull retrofit2.Response<ImageUploadResponse> response) {
                dialogg.dismiss();
                Log.w(TAG,"ImageUploadResponse"+ "--->" + new Gson().toJson(response.body()));

                int status = 0;
                if (response.body() != null) {
                    status = response.body().getResponse().get(0).getStatus();
                }
                Log.w("Status :",""+status);
                if (response.body() != null && 1 == response.body().getResponse().get(0).getStatus()) {

                    Log.w(TAG,"ImageUploadResponse"+"--->" + new Gson().toJson(response.body()));
                        /*int day, month, year;
                        int second, minute, hour;
                        GregorianCalendar date = new GregorianCalendar();

                        day = date.get(Calendar.DAY_OF_MONTH);
                        month = date.get(Calendar.MONTH);
                        year = date.get(Calendar.YEAR);

                        second = date.get(Calendar.SECOND);
                        minute = date.get(Calendar.MINUTE);
                        hour = date.get(Calendar.HOUR);*/
                    // String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
                    // String tag = name + i + ".jpg";
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    Log.w("fileName", fileName);
/*
                    http://whitehousecbe.in/cmrlcms/assets/uploads/IMG_020200401191325.jpg
*/                   /* String ImageUrl = ApiCall.BASE_URL + "cmrlcms/assets/uploads/" + fileName;
*/

                    String ImageUrl = ApiCall.BASE_URL + "/assets/uploads/" + fileName;
                   // String ImageUrl = response.body().getResponse().get(0).getFilepath();
                    Log.w(TAG,"ImageURL-->"+ImageUrl);
                    issueList = new IssueList(ImageUrl);
                    issueListArrayList.add(issueList);

                    int size = issueListArrayList.size();
                    injuryGridView.setNumColumns(size);
                    int Imagewith = size * 100;
                    final float Image_COL_WIDTH = AddIssueLayout.this.getResources().getDisplayMetrics().density * Imagewith;
                    int Image_width = Math.round(Image_COL_WIDTH);

                    LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
                    injuryGridView.setLayoutParams(lpp);
                    issueAdapter = new IssueAdapter(AddIssueLayout.this, issueListArrayList);
                    injuryGridView.setAdapter(issueAdapter);


                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageUploadResponse> call, @NonNull Throwable t) {
                dialogg.dismiss();
                Log.w("Profile", "On failure working"+t.getMessage());
            }
        });

    }
    private MultipartBody.Part getProfileImagePicMultipart(String path) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), "");
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        }
        Log.w(TAG,"PATH:"+path+"requestFile :"+requestFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("uploaded_file", path, requestFile);
        Log.w("ImageStorepath", "getProfileMultiPartRequest: " + new Gson().toJson(filePart));

        return filePart;
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
            final float Image_COL_WIDTH = AddIssueLayout.this.getResources().getDisplayMetrics().density * Imagewith;
            int Image_width = Math.round(Image_COL_WIDTH);


            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
            injuryGridView.setLayoutParams(lpp);


            issueAdapter = new IssueAdapter(AddIssueLayout.this, issueListArrayList);
            issueAdapter = new IssueAdapter(AddIssueLayout.this, issueListArrayList);
            injuryGridView.setAdapter(issueAdapter);
        }
    }

    public void AddIssuseURL(String url) {
        dialog = new Dialog(AddIssueLayout.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();
        requestQueue = Volley.newRequestQueue(AddIssueLayout.this);

        Log.e("url", "" + url);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("tickets");


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

                        new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("CMRL")
                                .setContentText(message)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(sDialog -> {

                                    sDialog.dismiss();

                                    Intent intent = new Intent(AddIssueLayout.this, StationActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.new_right, R.anim.new_left);
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(AddIssueLayout.this, SweetAlertDialog.WARNING_TYPE)
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

    private boolean isPermissionGranted() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }


    //Requesting permission
    private void requestPermission() {

        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block
//Here you can explain why you need this permission
//Explain here why you need this permission
//And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AddIssueLayout.READ_STORAGE_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.w("Request code", String.valueOf(requestCode));
        //Checking the request code of our request
        if (requestCode == READ_STORAGE_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openImagePickerIntent();

            } else {

                AddIssueLayout.this.finish();
            }
        } else if (requestCode == WRITE_STORAGE_CODE) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG,""+grantResults.length);

            } else {

                AddIssueLayout.this.finish();
            }
        }
    }
}
