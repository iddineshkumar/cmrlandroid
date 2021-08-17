package com.cm.cmrl.photopicker.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cm.cmrl.R;
import com.cm.cmrl.photopicker.Constants;
import com.cm.cmrl.photopicker.adapter.AlbumAdapter;
import com.cm.cmrl.photopicker.adapter.ListAlbumAdapter;
import com.cm.cmrl.photopicker.model.ImageModel;
import com.cm.cmrl.photopicker.myinterface.OnAlbum;
import com.cm.cmrl.photopicker.myinterface.OnListAlbum;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Created by Iddinesh.
 */
public class PickImageActivity extends AppCompatActivity implements OnClickListener, OnAlbum, OnListAlbum {
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    public static final int PICKER_REQUEST_CODE = 1001;
    private AlbumAdapter albumAdapter;
    private ArrayList<ImageModel> dataAlbum = new ArrayList();
    private ArrayList<ImageModel> dataListPhoto = new ArrayList();
    private GridView gridViewAlbum;
    private GridView gridViewListAlbum;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout layoutListItemSelect;
    private int limitImageMax = 30;
    private int limitImageMin = 2;
    private ListAlbumAdapter listAlbumAdapter;
    private ArrayList<ImageModel> listItemSelect = new ArrayList();
    private int pWHBtnDelete;
    private int pWHItemSelected;
    private ArrayList<String> pathList = new ArrayList();
    private TextView txtTotalImage;
    private Handler mHandler;
    private ProgressDialog pd;
    private static final int READ_STORAGE_CODE = 1001;
    private static final int WRITE_STORAGE_CODE = 1002;
    private TextView showTitleTextview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.piclist_activity_album);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.limitImageMax = bundle.getInt(KEY_LIMIT_MAX_IMAGE, 30);
            this.limitImageMin = bundle.getInt(KEY_LIMIT_MIN_IMAGE, 2);
            if (this.limitImageMin > this.limitImageMax) {
                finish();
            }
            if (this.limitImageMin < 1) {
                finish();
            }
        }
        this.pWHItemSelected = (((int) ((((float) getDisplayInfo(this).heightPixels) / 100.0f) * 25.0f)) / 100) * 80;
        this.pWHBtnDelete = (this.pWHItemSelected / 100) * 25;
        //  this.txtTitle = (TextView) findViewById(R.id.txtTitle);

        this.gridViewListAlbum = (GridView) findViewById(R.id.gridViewListAlbum);
        this.txtTotalImage = (TextView) findViewById(R.id.txtTotalImage);

        ((TextView) findViewById(R.id.btnDone)).setOnClickListener(this);
        this.layoutListItemSelect = (LinearLayout) findViewById(R.id.layoutListItemSelect);
        this.horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        this.horizontalScrollView.getLayoutParams().height = this.pWHItemSelected;
        this.gridViewAlbum = (GridView) findViewById(R.id.gridViewAlbum);
        this.showTitleTextview = (TextView) findViewById(R.id.show_title_text);
        showTitleTextview.setText(R.string.text_title_activity_album);

        pd = new ProgressDialog(PickImageActivity.this);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };

        try {
            Collections.sort(this.dataAlbum, new Comparator<ImageModel>() {
                @Override
                public int compare(ImageModel lhs, ImageModel rhs) {
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.albumAdapter = new AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);

        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new GetItemAlbum().execute(new Void[0]);
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_CODE);
        }
        updateTxtTotalImage();

    }

    private boolean isPermissionGranted(String permission) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, permission);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    private void requestPermission(String permission, int code) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == READ_STORAGE_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new GetItemAlbum().execute(new Void[0]);
            } else {
                PickImageActivity.this.finish();
            }
        } else if (requestCode == WRITE_STORAGE_CODE) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }
    }

    private boolean Check(String a, ArrayList<String> list) {
        if (!list.isEmpty() && list.contains(a)) {
            return true;
        }
        return false;
    }

    void addItemSelect(final ImageModel item) {
        item.setId(this.listItemSelect.size());
        this.listItemSelect.add(item);
        updateTxtTotalImage();
        final View viewItemSelected = View.inflate(this, R.layout.piclist_item_selected, null);
        ImageView imageItem = (ImageView) viewItemSelected.findViewById(R.id.imageItem);
        ImageView btnDelete = (ImageView) viewItemSelected.findViewById(R.id.btnDelete);

        imageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //btnDelete.getLayoutParams().width = this.pWHBtnDelete;
        //btnDelete.getLayoutParams().height = this.pWHBtnDelete;
        Glide.with((AppCompatActivity)this).asBitmap().load(item.getPathFile()).placeholder(R.drawable.piclist_icon_default).into(imageItem);



        btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PickImageActivity.this.layoutListItemSelect.removeView(viewItemSelected);
                PickImageActivity.this.listItemSelect.remove(item);
                PickImageActivity.this.updateTxtTotalImage();
            }
        });

        PickImageActivity.this.layoutListItemSelect.addView(viewItemSelected);
        viewItemSelected.startAnimation(AnimationUtils.loadAnimation(PickImageActivity.this, R.anim.abc_fade_in));
        PickImageActivity.this.sendScroll();

    }

    void updateTxtTotalImage() {
        this.txtTotalImage.setText(String.format(getResources().getString(R.string.text_images), new Object[]{Integer.valueOf(this.listItemSelect.size())}));
    }

    private void sendScroll() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        PickImageActivity.this.horizontalScrollView.fullScroll(66);
                    }
                });
            }
        }).start();
    }

    void showListAlbum(String pathAlbum) {
        showTitleTextview.setText(new File(pathAlbum).getName());
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
        this.gridViewListAlbum.setVisibility(View.VISIBLE);
        new GetItemListAlbum(pathAlbum).execute(new Void[0]);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            ArrayList<String> listString = getListString(this.listItemSelect);
            Log.w("listItemSelect:",""+listItemSelect);
            done(listString);
//            if (listString.size() >= this.limitImageMin) {
              //  done(listString);
//            } else {
//                Toast.makeText(this, "Please select at lease " + this.limitImageMin + " images", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void done(ArrayList<String> listString) {
        Log.w("donelistString:",""+listString);
        Intent mIntent = new Intent();
        setResult(AppCompatActivity.RESULT_OK, mIntent);
        mIntent.putStringArrayListExtra(KEY_DATA_RESULT, listString);
        finish();
    }

    ArrayList<String> getListString(ArrayList<ImageModel> listItemSelect) {
        ArrayList<String> listString = new ArrayList();
        for (int i = 0; i < listItemSelect.size(); i++) {
            listString.add(((ImageModel) listItemSelect.get(i)).getPathFile());
        }
        return listString;
    }

    private boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;
        for (int k = 0; k < Constants.FORMAT_IMAGE.size(); k++) {
            if (name.endsWith((String) Constants.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    public void onBackPressed() {
        if (this.gridViewListAlbum.getVisibility() == View.VISIBLE) {
            this.dataListPhoto.clear();
            this.listAlbumAdapter.notifyDataSetChanged();
            this.gridViewListAlbum.setVisibility(View.GONE);
            //   this.txtTitle.setText(getResources().getString(R.string.text_title_activity_album));

            showTitleTextview.setText(getResources().getString(R.string.text_title_activity_album));
            return;
        }
        super.onBackPressed();
    }

    public static DisplayMetrics getDisplayInfo(AppCompatActivity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public void OnItemAlbumClick(int position) {
        showListAlbum(((ImageModel) this.dataAlbum.get(position)).getPathFolder());
    }

    public void OnItemListAlbumClick(ImageModel item) {
        if (this.listItemSelect.size() < this.limitImageMax) {
            addItemSelect(item);
        } else {
            Toast.makeText(this, "Limit " + this.limitImageMax + " images", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetItemAlbum extends AsyncTask<Void, Void, String> {
        private GetItemAlbum() {
        }

        protected String doInBackground(Void... params) {
            Cursor cursor = PickImageActivity.this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            if (cursor != null) {
                int column_index_data = cursor.getColumnIndexOrThrow("_data");
                while (cursor.moveToNext()) {
                    String pathFile = cursor.getString(column_index_data);
                    File file = new File(pathFile);
                    if (file.exists()) {
                        boolean check = PickImageActivity.this.checkFile(file);
                        if (!PickImageActivity.this.Check(file.getParent(), PickImageActivity.this.pathList) && check) {
                            PickImageActivity.this.pathList.add(file.getParent());
                            PickImageActivity.this.dataAlbum.add(new ImageModel(file.getParentFile().getName(), pathFile, file.getParent()));
                        }
                    }
                }
                cursor.close();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            PickImageActivity.this.gridViewAlbum.setAdapter(PickImageActivity.this.albumAdapter);
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        String pathAlbum;

        GetItemListAlbum(String pathAlbum) {
            this.pathAlbum = pathAlbum;
        }

        protected String doInBackground(Void... params) {
            File file = new File(this.pathAlbum);
            if (file.isDirectory()) {
                for (File fileTmp : file.listFiles()) {
                    if (fileTmp.exists()) {
                        boolean check = PickImageActivity.this.checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            PickImageActivity.this.dataListPhoto.add(new ImageModel(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress(new Void[0]);
                        }
                    }
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                Collections.sort(PickImageActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                    @Override
                    public int compare(ImageModel item, ImageModel t1) {
                        File fileI = new File(item.getPathFolder());
                        File fileJ = new File(t1.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return 1;
                        }
                        return 0;
                    }
                });
            } catch (Exception e) {
            }
            PickImageActivity.this.listAlbumAdapter.notifyDataSetChanged();
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }
}
