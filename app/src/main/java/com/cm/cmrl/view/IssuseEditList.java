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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.cm.cmrl.R;
import com.cm.cmrl.adapter.IssueEditAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.UpdateHistoryList;
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

public class IssuseEditList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

 Toolbar toolbar;

    RequestQueue requestQueue;

    private RecyclerView recyclerView;

    UpdateHistoryList deapartmentStatusList;

    public static ArrayList<UpdateHistoryList> departmentListArrayList = new ArrayList<>();

    IssueEditAdapter departmentStatusAdapter;

    TextView emptyCustomFontTextView;

    SwipeRefreshLayout mWaveSwipeRefreshLayout;

    Dialog  dialog;

    SessionManager sessionManager;

    public static String empid = "", station_code = "", departmentCode = "", ticketStatus = "", userLevel = "";
    String thishistoryId = "", title;
    String networkStatus = "";

    LinearLayout backLinearLayout;

    ImageView emptyImageView;

    Button retryButton;

    private String TAG ="IssuseEditList";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issuse_edit_list);
        thishistoryId = Objects.requireNonNull(getIntent().getExtras()).getString("Tickethistory_id");
        title = getIntent().getExtras().getString("title");
        sessionManager = new SessionManager(IssuseEditList.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        departmentCode = ViewTickets.departmentCode;
        ticketStatus = ViewTickets.ticketStatus;
        //locName = getIntent().getExtras().getString("name");

        toolbar =  findViewById(R.id.toolbar);

        recyclerView =  findViewById(R.id.recycler_view);
        backLinearLayout =  findViewById(R.id.back_layout);

        emptyCustomFontTextView =  findViewById(R.id.empty_text);

        mWaveSwipeRefreshLayout =  findViewById(R.id.main_swipe);

        emptyCustomFontTextView = findViewById(R.id.empty_text);

        emptyImageView = findViewById(R.id.empty_image);

        retryButton =  findViewById(R.id.retry_button);
        TextView customFontTextView = findViewById(R.id.name);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        backLinearLayout.setOnClickListener(view -> onBackPressed());
        customFontTextView.setText(title);
        emptyCustomFontTextView.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(IssuseEditList.this, (view, position) -> {
                    // TODO Handle item click

                    Intent intent = new Intent(IssuseEditList.this, DepartmentStatusDetail.class);
                    intent.putExtra("image", departmentListArrayList.get(position).getPhotos());
                    intent.putExtra("name", departmentListArrayList.get(position).getUpdated_by_name());
                    intent.putExtra("title", departmentListArrayList.get(position).getTitle());
                    intent.putExtra("date", departmentListArrayList.get(position).getUpdated_at());
                    intent.putExtra("status", departmentListArrayList.get(position).getTicket_status());
                    intent.putExtra("description", departmentListArrayList.get(position).getDescription());
                    intent.putExtra("createdby", departmentListArrayList.get(position).getUpdated_by_id());
                    intent.putExtra("locationName", departmentListArrayList.get(position).getLocation());
                    intent.putExtra("priorityName", departmentListArrayList.get(position).getPriority_name());
                    intent.putExtra("ticketId", departmentListArrayList.get(position).getTicket_id());
                    intent.putExtra("stationLocation", ViewTickets.stationLocation);
                    intent.putExtra("code", ViewTickets.departmentCode);
                    intent.putExtra("screenStatus", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.new_right, R.anim.new_left);

                })
        );

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

                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);

                if (userLevel.equalsIgnoreCase("4")) {

                    IssueStatusList(ApiCall.API_URL  + "viewtickethistory_edit.php?tickethistory_id=" + thishistoryId);
                } else if (userLevel.equalsIgnoreCase("5")) {

                    IssueStatusList(ApiCall.API_URL  + "viewtickethistory_edit.php?tickethistory_id=" + thishistoryId);
                }
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        // check whetehr internet is on or not
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

        } else {

            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);

            if (userLevel.equalsIgnoreCase("4")) {

                IssueStatusList(ApiCall.API_URL  + "viewtickethistory_edit.php?tickethistory_id=" + thishistoryId);

            } else if (userLevel.equalsIgnoreCase("5")) {

                IssueStatusList(ApiCall.API_URL  + "viewtickethistory_edit.php?tickethistory_id=" + thishistoryId);
            }
        }

    }

    @Override
    public void onRefresh() {
        IssueStatusList(ApiCall.API_URL  + "viewtickethistory_edit.php?tickethistory_id=" + thishistoryId);
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

        dialog = new Dialog(IssuseEditList.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        Log.e("Url", "" + Url);
        departmentListArrayList.clear();
        requestQueue = Volley.newRequestQueue(IssuseEditList.this);

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    try {
                        JSONArray ja = response.getJSONArray("ticket");

                        Log.w(TAG,"Response-->"+ja);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String ticket_id = jsonObject.getString("ticket_id");
                            //String ticket_history_id = jsonObject.getString("ticket_history_id");
                            String ticket_status = jsonObject.getString("ticket_status");
                           // String station_id = jsonObject.getString("station_id");
                            String title = jsonObject.getString("title");
                            //String department_id = jsonObject.getString("department_id");
                            //String priority_id = jsonObject.getString("priority_id");
                            String priority_name = jsonObject.getString("priority_name");
                            String description = jsonObject.getString("description");
                            String location = jsonObject.getString("location");
                           // String location_name = jsonObject.getString("location_name");
                            String remarks = jsonObject.getString("remarks");
                            String photos = jsonObject.getString("photos");
                            String updated_by_id = jsonObject.getString("updated_by_id");
                            String updated_by_name = jsonObject.getString("updated_by_name");
                            String updated_at = jsonObject.getString("updated_at");

                            deapartmentStatusList = new UpdateHistoryList(status, message, ticket_id, ticket_status,
                                    remarks, updated_by_id, photos, updated_by_name, updated_at, title, priority_name,
                                    description, location);
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

                        departmentStatusAdapter = new IssueEditAdapter(IssuseEditList.this, departmentListArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(IssuseEditList.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(departmentStatusAdapter);

                    }
                },
                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }

}
