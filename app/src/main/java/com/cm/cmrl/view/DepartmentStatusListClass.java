package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.DepartmentStatusAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.DeapartmentStatusList;
import com.cm.cmrl.session.SessionManager;

import com.cm.cmrl.utils.ConnectionDetector;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class DepartmentStatusListClass extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    Toolbar toolbar;

    RequestQueue requestQueue;

    private RecyclerView recyclerView;

    DeapartmentStatusList deapartmentStatusList;

    public static ArrayList<DeapartmentStatusList> departmentListArrayList = new ArrayList<>();

    DepartmentStatusAdapter departmentStatusAdapter;

    TextView emptyCustomFontTextView, titleCustomFontTextView;

    SwipeRefreshLayout mWaveSwipeRefreshLayout;

    Dialog  dialog;

    SessionManager sessionManager;

    public static String empid = "", station_code = "", departmentCode = "", ticketStatus = "", userLevel = "", locName, ticket, list;
    private String issues = "";
    String networkStatus = "";
    LinearLayout backLinearLayout;

    ImageView emptyImageView;

    Button retryButton;
    private LinearLayoutManager mLayoutManager;

    private String TAG = "DepartmentStatusListClass";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_status_list);

        Log.w(TAG,"onCreate----->");

        sessionManager = new SessionManager(DepartmentStatusListClass.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        Log.w(TAG,"station_code Session-->"+station_code);

        departmentCode = Objects.requireNonNull(getIntent().getExtras()).getString("station_code");
        Log.w(TAG,"departmentCode Session-->"+departmentCode);
        if(userLevel.equals("5")){
            departmentCode = getIntent().getExtras().getString("code");
            Log.w(TAG,"departmentCode 5 :"+departmentCode);
        }

        if(departmentCode == null || departmentCode.isEmpty()){
            departmentCode = getIntent().getExtras().getString("code");
        }

        ticketStatus = getIntent().getExtras().getString("ticket_status");
        ticket = getIntent().getExtras().getString("ticket");
        Log.w(TAG+"onCreate-->","ticketStatus :"+ticketStatus+"ticket :"+ticket);
        locName = getIntent().getExtras().getString("name");
        if(station_code.equals("") || station_code.isEmpty()){
            station_code = getIntent().getExtras().getString("stationcode");
            Log.w(TAG,"station_codeonresume"+station_code);
        }

        if(userLevel.equals("6")){
            station_code = getIntent().getExtras().getString("code");
        }

        issues = getIntent().getExtras().getString("issues");

        Log.w(TAG+"onCreate-->","userLevel :"+userLevel+"Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);

        Log.w(TAG+"onCreate-->","ticket :"+ticket+"locName :"+locName+"station_code :"+station_code+"issues :"+issues);

        toolbar = findViewById(R.id.toolbar);
        titleCustomFontTextView = findViewById(R.id.name);
        recyclerView = findViewById(R.id.recycler_view);
        backLinearLayout = findViewById(R.id.back_layout);

        emptyCustomFontTextView = findViewById(R.id.empty_text);

        mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);

        emptyCustomFontTextView = findViewById(R.id.empty_text);

        emptyImageView = findViewById(R.id.empty_image);

        retryButton = findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        if (userLevel.equalsIgnoreCase("4")) {
            list = departmentCode + " - " + station_code + " - " + issues+" "+"Issues";
            Log.w(TAG,"userLevel :"+userLevel+" "+"ticket :"+ticket);
            titleCustomFontTextView.setText(list);
        } else if (userLevel.equalsIgnoreCase("5")) {
            list = station_code + " - " + departmentCode + " - " + issues+" "+"Issues";
            Log.w(TAG,"userLevel :"+userLevel+" "+"ticket :"+issues);
            titleCustomFontTextView.setText(list);
        } else if (userLevel.equalsIgnoreCase("6")) {
            list = departmentCode + " - " + station_code + " - " + issues+" "+"Issues";
            Log.w(TAG,"userLevel :"+userLevel+" "+"ticket :"+ticket);
            titleCustomFontTextView.setText(list);
        }
        backLinearLayout.setOnClickListener(view -> onBackPressed());

        emptyCustomFontTextView.setVisibility(View.GONE);


        retryButton.setOnClickListener(view -> {
            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                mWaveSwipeRefreshLayout.setVisibility(View.GONE);
                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

            } else {
                Log.w(TAG+"Call Server--->","Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.w(TAG,"departmentCode -->"+departmentCode);
                Log.w(TAG,"station_code -->"+station_code);
                if (userLevel.equalsIgnoreCase("4")) {

                    IssueStatusList(ApiCall.API_URL + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));

                } else if (userLevel.equalsIgnoreCase("5")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + departmentCode.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));

                } else if (userLevel.equalsIgnoreCase("6")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));


                }
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        // check whetehr internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        /*if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

        }
        else {

            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            Log.w(TAG+"Call Server Two--->","Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);

            if(departmentCode != null){
                if (userLevel.equalsIgnoreCase("4")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));

                }
                else if (userLevel.equalsIgnoreCase("5")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + departmentCode.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));

                }
                else if (userLevel.equalsIgnoreCase("6")) {
                    Log.w(TAG+"Call Server Three--->","Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));
                }

            }


        }*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mWaveSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });
    }

    @Override
    public void onRefresh() {

        refresh();
    }

    private void refresh() {
        new Handler().postDelayed(() -> mWaveSwipeRefreshLayout.setRefreshing(false), 3000);
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private DepartmentListClass.RecyclerItemClickListener.OnItemClickListener mListener;

        public interface OnItemClickListener {
             void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, DepartmentListClass.RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    // Call the api to show the Issue status list
    public void IssueStatusList(String Url) {

        dialog = new Dialog(DepartmentStatusListClass.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();
        Log.w(TAG,Url);
        Log.e("Url", "" + Url);
        departmentListArrayList.clear();
        requestQueue = Volley.newRequestQueue(DepartmentStatusListClass.this);


        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                        try {
                        JSONArray ja = response.getJSONArray("ticket");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);

                            int status = jsonObject.getInt("status");
                            String message = jsonObject.getString("message");
                            String ticket_id = jsonObject.getString("ticket_id");
                            String station_id = jsonObject.getString("station_id");
                            String title = jsonObject.getString("title");
                            String department_id = jsonObject.getString("department_id");
                            String priority_id = jsonObject.getString("priority_id");
                            String description = jsonObject.getString("description");
                            String photos = jsonObject.getString("photos");
                            String location = jsonObject.getString("location");
                            String updated_by = jsonObject.getString("updated_by");
                            String updated_at = jsonObject.getString("updated_at");
                            String remarks = jsonObject.getString("remarks");
                            String ticket_status = jsonObject.getString("ticket_status");
                            //String tickets_count = jsonObject.getString("tickets_count");
                           // String location_name = jsonObject.getString("location_name");
                            String priority_name = jsonObject.getString("priority_name");
                            String tickethistory_id = "";
                            String updatedName = "";

                            deapartmentStatusList = new DeapartmentStatusList(status, message, ticket_id, station_id, title,
                                    department_id, priority_id, description, photos, location, updated_by,
                                    updated_at, remarks, ticket_status, priority_name, updatedName, tickethistory_id);
                            departmentListArrayList.add(deapartmentStatusList);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if (departmentListArrayList.isEmpty()) {

                        emptyImageView.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.VISIBLE);

                        emptyImageView.setImageResource(R.mipmap.empty_icon);
                        emptyCustomFontTextView.setText("No Issues");

                    } else {

                        emptyImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.GONE);

                        departmentStatusAdapter = new DepartmentStatusAdapter(DepartmentStatusListClass.this, departmentListArrayList, issues, list);
                        mLayoutManager = new LinearLayoutManager(DepartmentStatusListClass.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(departmentStatusAdapter);

                    }
                },
                error -> dialog.dismiss()
        );

        jor.setRetryPolicy(new DefaultRetryPolicy(
                18000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jor);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(userLevel.equals("5")){
            Intent intent = new Intent(DepartmentStatusListClass.this, IssuseStatus.class);
            intent.putExtra("code",departmentCode);
            intent.putExtra("name",locName);
            intent.putExtra("issues",issues);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);
            finish();
        }else{
            finish();
        }









    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG,"onResume-->");
        Log.w(TAG,"station_code onresume "+station_code);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

        }
        else {

            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            Log.w(TAG+"Call Server Two--->","Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);

            if(departmentCode != null){
                if (userLevel.equalsIgnoreCase("4")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));

                }
                else if (userLevel.equalsIgnoreCase("5")) {

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + departmentCode.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));

                }
                else if (userLevel.equalsIgnoreCase("6")) {
                    Log.w(TAG+"Call Server Three--->","Emp Id :"+empid+" ticketStatus :"+ ticketStatus+"station_code :"+station_code+" departmentCode:"+departmentCode);

                    IssueStatusList(ApiCall.API_URL  + "ticketlist.php?empid=" + empid + "&ticket_status=" + ticketStatus + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + departmentCode.replace(" ", "%20"));
                }

            }


        }

        if(station_code == null || station_code.equals("") || station_code.isEmpty()){
            station_code = Objects.requireNonNull(getIntent().getExtras()).getString("stationcode");
            Log.w(TAG,"station_codeonresume"+station_code);
        }
    }
}
