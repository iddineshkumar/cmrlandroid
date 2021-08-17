package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import com.cm.cmrl.adapter.TicketViewAdapter;
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

public class ViewTickets extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;

    RequestQueue requestQueue;

    private RecyclerView recyclerView;

    DeapartmentStatusList deapartmentStatusList;

    public static ArrayList<DeapartmentStatusList> departmentListArrayList = new ArrayList<>();



    TextView emptyCustomFontTextView, titleCustomFontTextView;

    SwipeRefreshLayout mWaveSwipeRefreshLayout;


    Dialog dialog;

    SessionManager sessionManager;

    public static String empid = "";
    public static String station_code = "";
    public static String departmentCode = "";
    public static String ticketStatus = "";
    public static String userLevel = "";
    public static String ticketId = "";
    public static String title = "";
    public static String ticket = "";


    LinearLayout backLinearLayout;
    ImageView emptyImageView;

    Button retryButton;

    public static String networkStatus = "", stationLocation;

    private String TAG ="ViewTickets";

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_status_list);
        Log.w(TAG,"onCreate---->");

        sessionManager = new SessionManager(ViewTickets.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);
        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);

        departmentCode = Objects.requireNonNull(getIntent().getExtras()).getString("departmentCode");

        ticketStatus = getIntent().getExtras().getString("ticket_status");
        ticket = getIntent().getExtras().getString("ticket");
        Log.w(TAG+"onCreate-->","ticketStatus :"+ticketStatus+"ticket :"+ticket);

        ticketId = getIntent().getExtras().getString("ticketId");
        title = getIntent().getExtras().getString("title");
        stationLocation = getIntent().getExtras().getString("stationLocation");

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        backLinearLayout = findViewById(R.id.back_layout);

        emptyCustomFontTextView = findViewById(R.id.empty_text);
        titleCustomFontTextView = findViewById(R.id.name);
        emptyImageView = findViewById(R.id.empty_image);

        retryButton = findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        titleCustomFontTextView.setText(title);
        backLinearLayout.setOnClickListener(view -> onBackPressed());

        emptyCustomFontTextView.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(ViewTickets.this, (view, position) -> {
                    // TODO Handle item click

                  /*  Intent intent = new Intent(ViewTickets.this, DepartmentStatusDetail.class);
                    intent.putExtra("image",departmentListArrayList.get(position).getPhotos());
                    intent.putExtra("name",departmentListArrayList.get(position).getUpdated_by_name());
                    intent.putExtra("title",departmentListArrayList.get(position).getTitle());
                    intent.putExtra("date",departmentListArrayList.get(position).getUpdated_at());
                    intent.putExtra("status",departmentListArrayList.get(position).getTicket_status());
                    intent.putExtra("description",departmentListArrayList.get(position).getDescription());
                    intent.putExtra("createdby",departmentListArrayList.get(position).getUpdated_by());
                    intent.putExtra("locationName",departmentListArrayList.get(position).getLocation_name());
                    intent.putExtra("priorityName",departmentListArrayList.get(position).getPriority_name());
                    intent.putExtra("ticketId",departmentListArrayList.get(position).getTicket_id());
                    intent.putExtra("stationLocation",stationLocation);
                    intent.putExtra("code",departmentCode);
                    intent.putExtra("screenStatus","1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.new_right, R.anim.new_left);*/
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

                    ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "20%"));

                } else if (userLevel.equalsIgnoreCase("5")) {

                    ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "20%"));
                }
                else if (userLevel.equalsIgnoreCase("6")) {

                    ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));

                }
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

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

            if (userLevel.equalsIgnoreCase("4")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));
            } else if (userLevel.equalsIgnoreCase("5")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));
            }
            else if (userLevel.equalsIgnoreCase("6")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));

            }
        }*/

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

    public void ViewTicketList(final String Url) {
        dialog = new Dialog(ViewTickets.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();
        Log.e("Url", "" + Url);
        Log.w(TAG,"URL -->"+Url);
        departmentListArrayList.clear();
        requestQueue = Volley.newRequestQueue(ViewTickets.this);

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("ticket");
                        Log.w(TAG,"URL -->"+Url);
                        Log.w(TAG,"Response-->"+ja);

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
                            String updated_by = jsonObject.getString("updated_by_id");
                            String updated_by_name = jsonObject.getString("updated_by_name");
                            String updated_at = jsonObject.getString("updated_at");
                            String remarks = jsonObject.getString("remarks");
                            String ticket_status = jsonObject.getString("ticket_status");
                            String priority_name = jsonObject.getString("priority_name");
                            String tickethistory_id = jsonObject.getString("tickethistory_id");

                            String fault_title  = jsonObject.getString("fault_title");
                            String fault_type  = jsonObject.getString("fault_type");
                            String trainid  = jsonObject.getString("trainid");
                            String train_id  = jsonObject.getString("train_id");
                            String report_datetime  = jsonObject.getString("report_datetime");



                            deapartmentStatusList = new DeapartmentStatusList(status, message, ticket_id, station_id, title,
                                    department_id, priority_id, description, photos, location, updated_by,
                                    updated_at, remarks, ticket_status, priority_name, updated_by_name, tickethistory_id,fault_title,fault_type,trainid,train_id,report_datetime);
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

                        TicketViewAdapter departmentStatusAdapter = new TicketViewAdapter(ViewTickets.this, departmentListArrayList,title);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ViewTickets.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(departmentStatusAdapter);
                    }
                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }

    @Override
    public void onBackPressed() {
//        Log.e("ticketStatus", "" + ticketStatus);
//        Intent intent = new Intent(ViewTickets.this, DepartmentStatusListClass.class);
//        intent.putExtra("ticket_status", ticketStatus);
//        intent.putExtra("ticketId", ticketId);
//        intent.putExtra("departmentCode", departmentCode);
//        intent.putExtra("stationLocation", stationLocation);
//        intent.putExtra("title", title);
//        startActivity(intent);
        super.onBackPressed();
        ViewTickets.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            if (userLevel.equalsIgnoreCase("4")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));
            } else if (userLevel.equalsIgnoreCase("5")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));
            }
            else if (userLevel.equalsIgnoreCase("6")) {
                ViewTicketList(ApiCall.API_URL + "viewticket.php?ticket_id=" + ticketId.replace(" ", "%20"));

            }
        }
    }
}

