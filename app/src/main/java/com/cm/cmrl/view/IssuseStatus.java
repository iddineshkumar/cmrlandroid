package com.cm.cmrl.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.IssueStatusCountList;
import com.cm.cmrl.arraylist.IssueStatusList;
import com.cm.cmrl.session.SessionManager;

import com.cm.cmrl.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class IssuseStatus extends AppCompatActivity {

    String[] issueList = {"Open", "In Progress", "Pending", "Completed", "Closed"};

    String empid = "", station_code = "", code = "", name = "", userLevel = "", s_code;
    String networkStatus = "";

    IssueStatusList issueStatusList;
    IssueStatusCountList issueStatusCountList;

    ArrayList<IssueStatusList> issueStatusLists = new ArrayList<>();
    ArrayList<IssueStatusCountList> issueStatusCountListArrayList = new ArrayList<>();

    RecyclerView recyclerView;

    IssuseStatusAdapter issuseStatusAdapter;

    SessionManager sessionManager;

    Dialog  dialog;

    RequestQueue requestQueue;

    ImageView emptyImageView;

    Button retryButton;

    TextView emptyCustomFontTextView;
    TextView titleCustomFontTextView;
    private String TAG ="IssuseStatus";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_status_layout);

        sessionManager = new SessionManager(IssuseStatus.this);
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(SessionManager.KEY_EMPID);
        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);

        Log.w(TAG,"empid :"+empid+"station_code :"+station_code+"userLevel :"+userLevel);


        if(station_code.isEmpty()){
            station_code = Objects.requireNonNull(getIntent().getExtras()).getString("stationcode");
            Log.w(TAG,"station_code"+station_code);
        }
        code = Objects.requireNonNull(getIntent().getExtras()).getString("code");


        if (userLevel.equalsIgnoreCase("6")) {
            station_code = getIntent().getExtras().getString("station_code");
        } else if (userLevel.equalsIgnoreCase("4")) {
            s_code = getIntent().getExtras().getString("s_code");
        } else if (userLevel.equalsIgnoreCase("5")) {
            s_code = getIntent().getExtras().getString("s_code");
        }
        name = getIntent().getExtras().getString("name");

        Log.w(TAG,"empid "+empid);
        Log.w(TAG,"station_code" + station_code);
        Log.w(TAG,"code"+ code);
        Log.w(TAG,"userLevel" + userLevel);

        emptyCustomFontTextView = findViewById(R.id.empty_text);
        titleCustomFontTextView = findViewById(R.id.titlre_text);

        emptyImageView = findViewById(R.id.empty_image);

        retryButton = findViewById(R.id.retry_button);

        recyclerView = findViewById(R.id.recycler_view);


        issueStatusLists.clear();

        for (int i = 0; i < issueList.length; i++) {

            issueStatusList = new IssueStatusList();

            issueStatusLists.add(issueStatusList);
        }
        if (userLevel.equalsIgnoreCase("4")) {
            titleCustomFontTextView.setText(s_code + " - " + code);
        } else if (userLevel.equalsIgnoreCase("5")) {
            titleCustomFontTextView.setText(s_code + " - " + code);
        } else if (userLevel.equalsIgnoreCase("6")) {
            titleCustomFontTextView.setText(code + " - " + station_code);
        }

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(IssuseStatus.this, (view, position) -> {
                    // TODO Handle item click

                    if (!issueStatusCountListArrayList.get(position).getTickets_count().equalsIgnoreCase("0")) {
                        Intent intent = new Intent(IssuseStatus.this, DepartmentStatusListClass.class);
                        intent.putExtra("ticket_status", issueStatusCountListArrayList.get(position).getTicket_status());
                        intent.putExtra("ticket", issueList[position] + " Issues");
                        intent.putExtra("code", code);
                        intent.putExtra("station_code", s_code);
                        intent.putExtra("stationcode",station_code);
                        Log.w(TAG,"code---> :"+code);
                        Log.w(TAG,"s_code :"+station_code);
                        Log.w(TAG,"station_code :"+station_code);
                        intent.putExtra("name", name);
                        intent.putExtra("issues", issueList[position]);
                        startActivity(intent);
                        overridePendingTransition(R.anim.new_right, R.anim.new_left);
                    }

                })
        );

        retryButton.setOnClickListener(view -> {

            networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

            } else {

                if (userLevel.equalsIgnoreCase("4")) {

                    issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + code.replace(" ", "%20"));


                } else if (userLevel.equalsIgnoreCase("5")) {

                    issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + code.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));

                } else if (userLevel.equalsIgnoreCase("6")) {

                    issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + code.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));


                }
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        // check whether internet is on or not.
        networkStatus = ConnectionDetector.getConnectivityStatusString(getApplicationContext());

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");

        } else {
            if (userLevel.equalsIgnoreCase("4")) {

                issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + station_code.replace(" ", "%20") + "&department_code=" + code.replace(" ", "%20"));


            } else if (userLevel.equalsIgnoreCase("5")) {

                issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + code.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));

            } else if (userLevel.equalsIgnoreCase("6")) {

                issuseStatus(ApiCall.API_URL  + "ticketmenu.php?empid=" + empid + "&station_code=" + code.replace(" ", "%20") + "&department_code=" + station_code.replace(" ", "%20"));
            }
        }
    }



    /**
     * @param Url call the api to  get the issue status
     */
    public void issuseStatus(String Url) {

        dialog = new Dialog(IssuseStatus.this, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        Log.e("ISSueURL", "" + Url);

        requestQueue = Volley.newRequestQueue(IssuseStatus.this);

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {

                    try {

                        JSONArray ja = response.getJSONArray("tickets");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String ticket_status = jsonObject.getString("ticket_status");
                            String tickets_count = jsonObject.getString("tickets_count");

                            issueStatusCountList = new IssueStatusCountList(status, message, ticket_status, tickets_count);
                            issueStatusCountListArrayList.add(issueStatusCountList);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if (issueStatusCountListArrayList.isEmpty()) {

                        emptyImageView.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.VISIBLE);

                        emptyImageView.setImageResource(R.mipmap.empty_icon);
                        emptyCustomFontTextView.setText("No issues");

                    } else {

                        emptyImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.GONE);

                        issuseStatusAdapter = new IssuseStatusAdapter(issueStatusCountListArrayList);

                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(IssuseStatus.this, 2);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(issuseStatusAdapter);


                    }

                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }

    public class IssuseStatusAdapter extends RecyclerView.Adapter<IssuseStatusAdapter.MyViewHolder> {

        private List<IssueStatusCountList> albumList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
             TextView title, count;
            RelativeLayout mainRelativeLayout;

            public MyViewHolder(View view) {
                super(view);
                title =  view.findViewById(R.id.title);
                count =  view.findViewById(R.id.count);
                mainRelativeLayout =  view.findViewById(R.id.main_relative);
            }
        }


        IssuseStatusAdapter(List<IssueStatusCountList> albumList) {
            this.albumList = albumList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.issue_grid_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            holder.count.setText(issueStatusCountListArrayList.get(position).getTickets_count());

            if (issueStatusCountListArrayList.get(position).getTickets_count().equalsIgnoreCase("0")) {

                holder.mainRelativeLayout.setVisibility(View.VISIBLE);
            } else {

                holder.mainRelativeLayout.setVisibility(View.GONE);
            }
            if (position == 0) {

                holder.title.setText("Open");
            } else if (position == 1) {
                holder.title.setText("In progress");


            } else if (position == 2) {

                holder.title.setText("Pending");


            } else if (position == 3) {
                holder.title.setText("Completed");


            } else if (position == 4) {
                holder.title.setText("Closed");

            }


        }


        /**
         * Click listener for popup menu items
         */


        @Override
        public int getItemCount() {
            return albumList.size();
        }

    }

    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.


        if (userLevel.equalsIgnoreCase("4")) {

            Intent intent = new Intent(IssuseStatus.this, StationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);


        } else if (userLevel.equalsIgnoreCase("5")) {

            Intent intent = new Intent(IssuseStatus.this, MAinfragmentActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);


        } else if (userLevel.equalsIgnoreCase("6")) {
            super.onBackPressed();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG,"onResume-->");
        Log.w(TAG,"station_code onresume "+station_code);
        if(station_code.isEmpty()){
            station_code = Objects.requireNonNull(getIntent().getExtras()).getString("stationcode");
            s_code =  getIntent().getExtras().getString("stationcode");
            Log.w(TAG,"station_code---> :"+station_code);
            Log.w(TAG,"s_code---> :"+s_code);
        }
         s_code = station_code;
        if (userLevel.equalsIgnoreCase("4")) {
            titleCustomFontTextView.setText(s_code + " - " + code);
            Log.w(TAG,"s_code :"+s_code+"code :"+code);
        } else if (userLevel.equalsIgnoreCase("5")) {
            titleCustomFontTextView.setText(s_code + " - " + code);
            Log.w(TAG,"s_code :"+s_code+"code :"+code);
        } else if (userLevel.equalsIgnoreCase("6")) {
            titleCustomFontTextView.setText(code + " - " + station_code);
            Log.w(TAG,"s_code :"+s_code+"code :"+code);

        }


    }
}
