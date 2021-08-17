package com.cm.cmrl.admin;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cm.cmrl.R;
import com.cm.cmrl.arraylist.IssueStatusList;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.view.DepartmentListClass;
import com.cm.cmrl.view.MAinfragmentActivty;
import java.util.ArrayList;

import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class ShowStationDash extends AppCompatActivity {

    String[] issueList = {"Open", "In Progress", "Pending", "Completed"};
    String userLevel="",stationName="";
    String open="",pending="",completed="",close="";
    IssueStatusList issueStatusList;
    ArrayList<IssueStatusList> issueStatusLists = new ArrayList<>();
    RecyclerView recyclerView;
    IssuseStatusAdapter issuseStatusAdapter;
    SessionManager sessionManager;
    ImageView emptyImageView;
    Button retryButton;
    TextView emptyCustomFontTextView;
    TextView titleCustomFontTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_status_layout);

        String TAG = "ShowStationDash";
        Log.w(TAG,"onCreate-->");

        sessionManager = new SessionManager(ShowStationDash.this);

        open= Objects.requireNonNull(getIntent().getExtras()).getString("open");
        pending=getIntent().getExtras().getString("pending");
        completed=getIntent().getExtras().getString("completed");
        close=getIntent().getExtras().getString("close");
        stationName=getIntent().getExtras().getString("stationName");


        emptyCustomFontTextView =  findViewById(R.id.empty_text);
        titleCustomFontTextView =  findViewById(R.id.titlre_text);

        emptyImageView =  findViewById(R.id.empty_image);

        retryButton =  findViewById(R.id.retry_button);

        recyclerView =  findViewById(R.id.recycler_view);


        issueStatusLists.clear();

        for (String s : issueList) {
            issueStatusList = new IssueStatusList();

            issueStatusLists.add(issueStatusList);
        }


        titleCustomFontTextView.setText(stationName);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(ShowStationDash.this, (view, position) -> {
                    // TODO Handle item click



                })
        );

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        issuseStatusAdapter = new IssuseStatusAdapter(issueStatusLists);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ShowStationDash.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(issuseStatusAdapter);


    }


    public class IssuseStatusAdapter extends RecyclerView.Adapter<IssuseStatusAdapter.MyViewHolder> {

        private ArrayList<IssueStatusList> albumList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count;
            RelativeLayout mainRelativeLayout;
            public MyViewHolder(View view) {
                super(view);
                title =  view.findViewById(R.id.title);
                count =  view.findViewById(R.id.count);
                mainRelativeLayout = view.findViewById(R.id.main_relative);
            }
        }


        IssuseStatusAdapter(ArrayList<IssueStatusList> issueStatusLists) {
            this.albumList = issueStatusLists;
        }

        @NonNull
        @Override
        public IssuseStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.issue_grid_layout, parent, false);

            return new IssuseStatusAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final IssuseStatusAdapter.MyViewHolder holder, int position) {
            if (position == 0) {

                holder.title.setText("Open");
                holder.count.setText(open);
                if(open.equalsIgnoreCase("0")){

                    holder.mainRelativeLayout.setVisibility(View.VISIBLE);
                }
                else {

                    holder.mainRelativeLayout.setVisibility(View.GONE);
                }
            } else if (position == 1) {
                holder.count.setText(pending);
                holder.title.setText("Pending");
                if(pending.equalsIgnoreCase("0")){

                    holder.mainRelativeLayout.setVisibility(View.VISIBLE);
                }
                else {

                    holder.mainRelativeLayout.setVisibility(View.GONE);
                }

            } else if (position == 2) {
                holder.title.setText("Completed");
                if(completed.equalsIgnoreCase("0")){

                    holder.mainRelativeLayout.setVisibility(View.VISIBLE);
                }
                else {

                    holder.mainRelativeLayout.setVisibility(View.GONE);
                }

                holder.count.setText(completed);


            } else if (position == 3) {
                holder.title.setText("Closed");
                if(close.equalsIgnoreCase("0")){

                    holder.mainRelativeLayout.setVisibility(View.VISIBLE);
                }
                else {

                    holder.mainRelativeLayout.setVisibility(View.GONE);
                }
                holder.count.setText(close);

            } /*else if (position == 4) {
                holder.title.setText("Closed");
                if(close.equalsIgnoreCase("0")){

                    holder.mainRelativeLayout.setVisibility(View.VISIBLE);
                }
                else {

                    holder.mainRelativeLayout.setVisibility(View.GONE);
                }
                holder.count.setText(close);
            }*/


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
        super.onBackPressed();

        if(userLevel.equalsIgnoreCase("4")){

            Intent intent = new Intent(ShowStationDash.this, DepartmentListClass.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);


        }else if(userLevel.equalsIgnoreCase("5")){

            Intent intent = new Intent(ShowStationDash.this, MAinfragmentActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.new_right, R.anim.new_left);


        }


    }
}

