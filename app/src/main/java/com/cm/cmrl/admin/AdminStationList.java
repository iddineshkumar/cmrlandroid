package com.cm.cmrl.admin;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm.cmrl.R;

import com.cm.cmrl.adapter.AdminStationListAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.Adminstationlist;
import com.cm.cmrl.session.SessionManager;

import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.AdminActivity;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class AdminStationList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    Toolbar toolbar;
    RequestQueue requestQueue;
    private RecyclerView recyclerView;
    Adminstationlist adminStationList;
    ArrayList<Adminstationlist> adminStationListArrayList = new ArrayList<>();
    AdminStationListAdapter adminStationListAdapter;
    TextView emptyCustomFontTextView;
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    Dialog dialog;
    SessionManager sessionManager;
    ImageView emptyImageView;
    Button retryButton;
    String networkStatus = "";
    LinearLayout back_layout;
    LinearLayout elvatorLayout;
    LinearLayout elvalorLine;
    private String code = "", type = "";
    private String tabSelects = "";
    private TextView textView;
    private String userLevel;
    private String typeFrom;

    private String TAG ="AdminStationList";

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminstation_list);

        Log.w(TAG,"onCreate--->");

        sessionManager = new SessionManager(AdminStationList.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        toolbar = findViewById(R.id.toolbar);

        recyclerView =  findViewById(R.id.recycler_view);
        emptyCustomFontTextView =  findViewById(R.id.empty_text);
        emptyImageView =  findViewById(R.id.empty_image);

        mWaveSwipeRefreshLayout =  findViewById(R.id.main_swipe);
        back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(view -> onBackPressed());

        elvalorLine =  findViewById(R.id.elvator_line);
        elvatorLayout =  findViewById(R.id.elvator_layout);

        floatingActionButton =  findViewById(R.id.fab_createevent);

        retryButton =  findViewById(R.id.retry_button);

        TextView customFontTextView = findViewById(R.id.department_name);
        textView =  findViewById(R.id.selected_text_name);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        String name = getIntent().getStringExtra("name");
        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        tabSelects = getIntent().getStringExtra("TabSelects");
        typeFrom = getIntent().getStringExtra("TypeFrom");
        customFontTextView.setText(name);

        retryButton.setOnClickListener(view -> {
            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                mWaveSwipeRefreshLayout.setVisibility(View.GONE);
                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText(R.string.pleasecheckyourinternet);
                // floatingActionButton.setVisibility(View.GONE);
            } else {
                if (tabSelects.equalsIgnoreCase("0")) {
                    DepaartmentUrl(ApiCall.API_URL+"get_edeptickets_new.php?stations_type=" + type + "&station_code=" + code);
                } else if (tabSelects.equalsIgnoreCase("1")) {
                    DepaartmentUrl(ApiCall.API_URL+"get_udeptickets_new.php?stations_type=" + type + "&station_code=" + code);
                }
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        // check whether internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText(R.string.pleasecheckyourinternet);
            //floatingActionButton.setVisibility(View.GONE);

        } else {
            if(tabSelects != null){
                if (tabSelects.equalsIgnoreCase("0")) {
                    textView.setText("Elevated");
                    DepaartmentUrl(ApiCall.API_URL+"get_edeptickets_new.php?stations_type=" + type + "&station_code=" + code);
                } else if (tabSelects.equalsIgnoreCase("1")) {
                    textView.setText("Underground");
                    DepaartmentUrl(ApiCall.API_URL+"get_udeptickets_new.php?stations_type=" + type + "&station_code=" + code);
                }
            }

        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (!recyclerView.canScrollVertically(1)) {

                }*/
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(AdminStationList.this, AdminActivity.class);
        intent.putExtra("Tabselects", tabSelects);
        intent.putExtra("TypeForm", typeFrom);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {

        refresh();
    }

    @SuppressLint("SetTextI18n")
    private void refresh() {
        new Handler().postDelayed(() -> {

            if (tabSelects.equalsIgnoreCase("0")) {
                textView.setText("Elevated");
                DepaartmentUrl(ApiCall.API_URL+"get_edeptickets_new.php?stations_type=" + type + "&station_code=" + code);
            } else if (tabSelects.equalsIgnoreCase("1")) {
                textView.setText("Underground");
                DepaartmentUrl(ApiCall.API_URL+"get_udeptickets_new.php?stations_type=" + type + "&station_code=" + code);
            }
            // Refresh the department list to call api again

            mWaveSwipeRefreshLayout.setRefreshing(false);
            adminStationListAdapter.notifyDataSetChanged();
        }, 3000);
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

    /**
     * @param Url call the api to get the department list
     */
    public void DepaartmentUrl(String Url) {

        Log.w(TAG,"URL :"+"\t"+Url);
        dialog = new Dialog(AdminStationList.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        requestQueue = Volley.newRequestQueue(AdminStationList.this);

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    adminStationListArrayList.clear();
                    try {

                        JSONArray ja = response.getJSONArray("list");
                        Log.w(TAG,"Response :"+"\t"+ja);


                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("id");
                            String code = jsonObject.getString("code");
                            String name = jsonObject.getString("name");
                            String open_ticket_count = jsonObject.getString("open_ticket_count");
                            String inprogress_ticket_count = jsonObject.getString("inprogress_ticket_count");
                            String pending_ticket_count = jsonObject.getString("pending_ticket_count");
                            String completed_ticket_count = jsonObject.getString("completed_ticket_count");
                            String closed_ticket_count = jsonObject.getString("closed_ticket_count");
                            adminStationList = new Adminstationlist(status, message, id, code, "", name, open_ticket_count, inprogress_ticket_count, pending_ticket_count,
                                    completed_ticket_count, closed_ticket_count, "");
                            adminStationListArrayList.add(adminStationList);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if (adminStationListArrayList.isEmpty()) {

                        emptyImageView.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.VISIBLE);

                        emptyImageView.setImageResource(R.mipmap.empty_icon);
                        emptyCustomFontTextView.setText("No departments");

                    } else {

                        emptyImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.GONE);

                        adminStationListAdapter = new AdminStationListAdapter(AdminStationList.this, adminStationListArrayList, code);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AdminStationList.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(adminStationListAdapter);



                    }
                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }
}

