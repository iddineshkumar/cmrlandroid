package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.cm.cmrl.R;
import com.cm.cmrl.adapter.IssueTwoAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.IssueList;
import com.cm.cmrl.photoview.PhotoView;
import com.cm.cmrl.session.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class  DepartmentStatusDetail extends AppCompatActivity {

    String image = "", date = "", status = "", decription = "", createdby = "", title = "", locationName = "", priorityName = "", station_name = "";
    String name = "", ticketId = "", departmentCode = "", station_code = "",  userLevel = "", stationLocation = "", screenStatus = "";

    CollapsingToolbarLayout collapsingToolbar;

    TextView dateCustomFontTextView;
    TextView statusCustomFontTextView;
    TextView decriptionCustomFontTextView;
    TextView createdbyCustomFontTextView;
    TextView priorityCustomFontTextView, locationCustomFontTextView, createdIdCustomFontTextView;
    TextView inprogressStatusCustomFontTextView, inprogressDescriptionCustomFontTextView;
    TextView pendingCustomFontTextView, pendingDescriptionCustomFontTextView;
    TextView completedCustomFontTextView, completedDescriptionCustomFontTextView, ticketCustomFontTextView;
    TextView closedCustomFontTextView, closedDescription, timeCustomFontTextView, stationNameCustomFontTextView;

    TextView tvfault_title,tvfault_type,tvtrainid,tvtrain_id,tvreport_datetime;

    CardView inprogressCardView, pendingCardView, completedCardView, closeCardView;

    ImageView imageView;

    GridView gridView;

    IssueList issueList;

    Dialog alertDialog;

    ArrayList<IssueList> issueListArrayList = new ArrayList<>();

    SessionManager sessionManager;

    FloatingActionButton floatingActionButton;

    ViewPager viewPager;

    int postion = 0;
    private String updateAt;

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_status_detail);

        sessionManager = new SessionManager(DepartmentStatusDetail.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        station_name = hashMap.get(SessionManager.KEY_STATION_NAME);
        image = Objects.requireNonNull(getIntent().getExtras()).getString("image");
        title = getIntent().getExtras().getString("title");
        date = getIntent().getExtras().getString("date");
        status = getIntent().getExtras().getString("status");
        name = getIntent().getExtras().getString("name");
        decription = getIntent().getExtras().getString("description");
        createdby = getIntent().getExtras().getString("createdby");
        locationName = getIntent().getExtras().getString("locationName");
        priorityName = getIntent().getExtras().getString("priorityName");
        ticketId = getIntent().getExtras().getString("ticketId");
        departmentCode = getIntent().getExtras().getString("code");
        stationLocation = getIntent().getExtras().getString("stationLocation");
        screenStatus = getIntent().getExtras().getString("screenStatus");
        String station_id = getIntent().getExtras().getString("stationid");
        String fault_title = getIntent().getExtras().getString("faulttitle");
        String fault_type = getIntent().getExtras().getString("faulttype");
        String trainid = getIntent().getExtras().getString("trainid");
        String train_id = getIntent().getExtras().getString("train_id");
        String report_datetime = getIntent().getExtras().getString("report_datetime");

        String TAG = "DepartmentStatusDetail";
        Log.w(TAG,"userLevel :"+" "+userLevel+" "+"station_id :"+ station_id +" "+"fault_title :"+ fault_title +" "+"fault_type :"+ fault_type +" "+"trainid :"+ trainid +" "+"train_id :"+ train_id +" "+"report_datetime :"+ report_datetime);

        imageView = findViewById(R.id.backdrop);
        gridView = findViewById(R.id.add_image_grid_view);

        inprogressCardView = findViewById(R.id.inprogrss_card);
        pendingCardView = findViewById(R.id.pending_card);
        completedCardView = findViewById(R.id.completd_card);
        closeCardView = findViewById(R.id.close_card);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        dateCustomFontTextView = findViewById(R.id.date_text);
        statusCustomFontTextView = findViewById(R.id.status_text);
        decriptionCustomFontTextView = findViewById(R.id.describtio_text);
        createdbyCustomFontTextView = findViewById(R.id.create_text);
        createdIdCustomFontTextView = findViewById(R.id.create_text_id);
        timeCustomFontTextView = findViewById(R.id.time_text);
        stationNameCustomFontTextView = findViewById(R.id.station_text_name);
        ticketCustomFontTextView = findViewById(R.id.ticket_id);

        floatingActionButton = findViewById(R.id.fab);

        priorityCustomFontTextView = findViewById(R.id.priority_text);
        locationCustomFontTextView = findViewById(R.id.location_text);

        inprogressStatusCustomFontTextView = findViewById(R.id.inprogress_text);
        inprogressDescriptionCustomFontTextView = findViewById(R.id.inprogress_describtio_text);

        pendingCustomFontTextView = findViewById(R.id.pending_text);
        pendingDescriptionCustomFontTextView = findViewById(R.id.pending_describtio_text);

        completedCustomFontTextView = findViewById(R.id.completed_text);
        completedDescriptionCustomFontTextView = findViewById(R.id.completed_describtio_text);
        closedCustomFontTextView = findViewById(R.id.closed_text);
        closedDescription = findViewById(R.id.closed_describtio_text);


        tvfault_title = findViewById(R.id.tvfault_title);
        tvfault_type = findViewById(R.id.tvfault_type);
        tvtrainid = findViewById(R.id.tvtrainid);
        tvtrain_id = findViewById(R.id.tvtrain_id);
        tvreport_datetime = findViewById(R.id.tvreport_datetime);

        SimpleDateFormat sdfoutputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm", Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date reporteddate = null;
        try {
            if(report_datetime != null && !report_datetime.isEmpty()){
                reporteddate = sdf.parse(report_datetime);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }



          if (userLevel.equalsIgnoreCase("4")){
                Log.w(TAG,"userLevel--->"+userLevel);
                tvfault_title.setVisibility(View.GONE);
                tvfault_type.setVisibility(View.GONE);
                tvreport_datetime.setVisibility(View.GONE);
                tvtrainid.setVisibility(View.GONE);
                tvtrain_id.setVisibility(View.GONE);


            }
          else if (userLevel.equalsIgnoreCase("5") && (station_id.equalsIgnoreCase("OCC"))){
                Log.w(TAG,"userLevel--->"+userLevel+" "+"Fault Title "+" "+ fault_type +" "+"Fault Type"+" "+ fault_title);
                tvfault_title.setVisibility(View.VISIBLE);
                tvfault_type.setVisibility(View.VISIBLE);
                tvreport_datetime.setVisibility(View.VISIBLE);
                tvfault_title.setText("Fault Title :"+" "+ fault_type);
                tvfault_type .setText("Fault Type :"+" "+ fault_title);

                Log.w(TAG,"Fault Title "+" "+ fault_type +" "+"Fault Type"+" "+ fault_title);

                if (reporteddate != null) {
                    tvreport_datetime.setText("Reported Date :"+" "+sdfoutputFormat.format(reporteddate));
                }
                if (fault_type != null) {
                    if(fault_type.equalsIgnoreCase("train related faults")){
                        tvtrainid.setVisibility(View.VISIBLE);
                        tvtrain_id.setVisibility(View.VISIBLE);
                        tvtrainid.setText("Train ID :"+" "+ trainid);
                        tvtrain_id.setText("Train Name :"+" "+ train_id);
                    }else{
                        tvtrainid.setVisibility(View.GONE);
                        tvtrain_id.setVisibility(View.GONE);
                    }
                }

            }
          else if (userLevel.equalsIgnoreCase("6") && (station_id.equalsIgnoreCase("OCC"))){
                Log.w(TAG,"userLevel--->"+userLevel+" "+"Fault Title "+" "+ fault_type +" "+"Fault Type"+" "+ fault_title);
                tvfault_title.setVisibility(View.VISIBLE);
                tvfault_type.setVisibility(View.VISIBLE);
                tvreport_datetime.setVisibility(View.VISIBLE);
                tvfault_title.setText("Fault Title :"+" "+ fault_type);
                tvfault_type .setText("Fault Type :"+" "+ fault_title);

                Log.w(TAG,"Fault Title "+" "+ fault_type +" "+"Fault Type"+" "+ fault_title);

                if (reporteddate != null) {
                    tvreport_datetime.setText("Reported Date :"+" "+sdfoutputFormat.format(reporteddate));
                }
                if (fault_type != null) {
                    if(fault_type.equalsIgnoreCase("train related faults")){
                        tvtrainid.setVisibility(View.VISIBLE);
                        tvtrain_id.setVisibility(View.VISIBLE);
                        tvtrainid.setText("Train ID :"+" "+ trainid);
                        tvtrain_id.setText("Train Name :"+" "+ train_id);
                    }else{
                        tvtrainid.setVisibility(View.GONE);
                        tvtrain_id.setVisibility(View.GONE);
                    }
                }

            }
          else{
                tvfault_title.setVisibility(View.GONE);
                tvfault_type.setVisibility(View.GONE);
                tvreport_datetime.setVisibility(View.GONE);
                tvtrainid.setVisibility(View.GONE);
                tvtrain_id.setVisibility(View.GONE);
            }







        inprogressCardView.setVisibility(View.GONE);
        pendingCardView.setVisibility(View.GONE);
        completedCardView.setVisibility(View.GONE);
        closeCardView.setVisibility(View.GONE);

        //Split the image string to show the first position of the image in ImageView.
        String[] ImageString = image.split(",");


        if (image.equalsIgnoreCase("")) {
                Log.w(TAG,""+image);
        } else {

            if (ImageString.length >= 2) {

                // Display the image from server to ImageView
                Glide.with(DepartmentStatusDetail.this)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + ImageString[0])
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

            } else {

                // Display the image from server to ImageView
                Glide.with(DepartmentStatusDetail.this)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + image)
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }

        // check the status is open, pending ,in progress, complete or close
        if (status.equalsIgnoreCase("1")) {

            statusCustomFontTextView.setText("Status: Open");
            createdbyCustomFontTextView.setText("Created by: " + name);
            createdIdCustomFontTextView.setText("Emp id: " + createdby);

        } else if (status.equalsIgnoreCase("2")) {

            statusCustomFontTextView.setText("Status: In progress");
            createdbyCustomFontTextView.setText("Updated by: " + name);
            createdIdCustomFontTextView.setText("Emp id: " + createdby);

        } else if (status.equalsIgnoreCase("3")) {

            statusCustomFontTextView.setText("Status: Pending");
            createdbyCustomFontTextView.setText("Updated by: " + name);
            createdIdCustomFontTextView.setText("Emp id: " + createdby);

        } else if (status.equalsIgnoreCase("4")) {

            statusCustomFontTextView.setText("Status: Completed");
            createdbyCustomFontTextView.setText("Updated by: " + name);
            createdIdCustomFontTextView.setText("Emp id: " + createdby);

        } else if (status.equalsIgnoreCase("5")) {

            statusCustomFontTextView.setText("Status: Closed");
            createdbyCustomFontTextView.setText("Closed by: " + name);
            createdIdCustomFontTextView.setText("Emp id: " + createdby);

        }

        decriptionCustomFontTextView.setText(decription);
        priorityCustomFontTextView.setText("Priority: " + priorityName);
        locationCustomFontTextView.setText("Location: " + locationName);
        ticketCustomFontTextView.setText("Ticket id: " + ticketId);

        // whether user is station controller or department member
//        if (userLevel.equalsIgnoreCase("4")) {
        if (userLevel.equalsIgnoreCase("6")) {
            SharedPreferences sharedPreferences = getSharedPreferences("Station", MODE_PRIVATE);
            stationNameCustomFontTextView.setText("Station: " + sharedPreferences.getString("Station", null));
        } else {
            stationNameCustomFontTextView.setText("Station: " + station_name);
        }


        collapsingToolbar.setTitle(title);
        SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = spf.parse(date);
            if (date1 != null) {
                updateAt = outputFormat.format(date1);
            }
            String[] time = updateAt.split(" ");
            String outTime = time[1];
            if (date1 != null) {
                dateCustomFontTextView.setText("Date: " + daySimpleDateFormat.format(date1));
            }
            timeCustomFontTextView.setText("Time: " + outTime + " " + time[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] array = image.split(",");

        if (array.length == 0) {
            Log.w(TAG,""+array.length);
        } else {

            for (String s : array) {

                issueList = new IssueList(s);
                issueListArrayList.add(issueList);
            }

            // Calculate height and width for horizontal GridView
            int size = issueListArrayList.size();
            gridView.setNumColumns(size);
            int Imagewith = size * 100;
            final float Image_COL_WIDTH = DepartmentStatusDetail.this.getResources().getDisplayMetrics().density * Imagewith;
            int Image_width = Math.round(Image_COL_WIDTH);

            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(Image_width, ViewGroup.LayoutParams.MATCH_PARENT);
            gridView.setLayoutParams(lpp);

            IssueTwoAdapter issueAdapter = new IssueTwoAdapter(DepartmentStatusDetail.this, issueListArrayList);
            gridView.setAdapter(issueAdapter);

        }

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {


            Glide.with(DepartmentStatusDetail.this)
                    .load(ApiCall.BASE_URL+"assets/uploads/" + issueListArrayList.get(i).getImageUrl())
                    .placeholder(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            postion = i;

        });

        imageView.setOnClickListener(view -> {

            alertDialog = new Dialog(DepartmentStatusDetail.this, R.style.DialogTheme);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            alertDialog.setContentView(R.layout.detail_image_view);

            viewPager = alertDialog.findViewById(R.id.psger);

            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(DepartmentStatusDetail.this, issueListArrayList);
            viewPager.setAdapter(customPagerAdapter);
            viewPager.setCurrentItem(postion);

            viewPager.setOnClickListener(view1 -> alertDialog.dismiss());

            alertDialog.show();

        });

        floatingActionButton.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(view -> {

            Intent intent = new Intent(DepartmentStatusDetail.this, UpdateStatusActivity.class);
            intent.putExtra("ticketId", ticketId);
            intent.putExtra("departmentCode", departmentCode);
            intent.putExtra("stationLocation", stationLocation);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);

        });

    }

    public class CustomPagerAdapter extends PagerAdapter {

        ArrayList<IssueList> searchListArrayList;

        FragmentActivity mContext;

        LayoutInflater mLayoutInflater;

        PhotoView imageVieww;

        CustomPagerAdapter(FragmentActivity context, ArrayList<IssueList> searchListArrayList) {
            mContext = context;
            this.searchListArrayList = searchListArrayList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return searchListArrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int i) {

            View itemView = mLayoutInflater.inflate(R.layout.full_screen_popup_layout, container, false);

            imageVieww = itemView.findViewById(R.id.iv_photo);

            Glide.with(DepartmentStatusDetail.this)
                    .load(ApiCall.BASE_URL+"assets/uploads/" + issueListArrayList.get(i).getImageUrl())
                    .placeholder(R.drawable.no_image)
                    .into(imageVieww);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);

        }

        public CharSequence getPageTitle(int position) {

            return issueListArrayList.get(position).getImageUrl();
        }

        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


