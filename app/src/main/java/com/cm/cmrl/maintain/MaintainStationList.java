package com.cm.cmrl.maintain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.cm.cmrl.arraylist.MainList;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.MaintainAdapter;
import com.cm.cmrl.ups.UpsActivity;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

/**
 * Created by Iddinesh.
 */

public class MaintainStationList  extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    ArrayList<MainList> mainListArrayList = new ArrayList<>();
    MaintainAdapter maintainAdapter;
    TextView emptyCustomFontTextView;
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    LinearLayout signOutLayout;
    ImageView emptyImageView;
    Button retryButton;
    String networkStatus = "";
    SearchView searchView;


    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_station_list);

        toolbar = findViewById(R.id.toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        searchView = findViewById(R.id.search);

        emptyCustomFontTextView = findViewById(R.id.empty_text);

        emptyImageView = findViewById(R.id.empty_image);

        retryButton = findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);

        floatingActionButton = findViewById(R.id.fab_createevent);

        signOutLayout = findViewById(R.id.sign_out);


        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(MaintainStationList.this, (view, position) -> {
                    // TODO Handle item click

                    Intent intent = new Intent(MaintainStationList.this, UpsActivity.class);
                    intent.putExtra("code", mainListArrayList.get(position).getCode());
                    intent.putExtra("name", mainListArrayList.get(position).getName());

                    startActivity(intent);
                    MaintainStationList.this.overridePendingTransition(R.anim.new_right, R.anim.new_left);
                })
        );




        retryButton.setOnClickListener(view -> {

            networkStatus = ConnectionDetector.getConnectivityStatusString(MaintainStationList.this);

            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                mWaveSwipeRefreshLayout.setVisibility(View.GONE);
                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText("Please check your internet connectivity and try again");
                floatingActionButton.setVisibility(View.GONE);
            } else {

                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);

            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        networkStatus = ConnectionDetector.getConnectivityStatusString(MaintainStationList.this);

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");
            floatingActionButton.setVisibility(View.GONE);
        } else {

            prepareAlbums();
            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
            if (mainListArrayList.isEmpty()) {

                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyCustomFontTextView.setText("No station list");

            } else {

                emptyCustomFontTextView.setVisibility(View.INVISIBLE);
                maintainAdapter = new MaintainAdapter(mainListArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MaintainStationList.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setAdapter(maintainAdapter);

                search(searchView);

            }

        }


    }
    private void prepareAlbums() {
        MainList a = new MainList("0", "success","1", "SDF 001", "Station name 1");
        mainListArrayList.add(a);

        a = new MainList("0", "success","1", "SDF 001", "Station name 1");
        mainListArrayList.add(a);

        a = new MainList("0", "success","2", "SDF 002", "Station name 2");
        mainListArrayList.add(a);

        a = new MainList("0", "success","3", "SDF 003", "Station name 3");
        mainListArrayList.add(a);

        a = new MainList("0", "success","4", "SDF 004", "Station name 4");
        mainListArrayList.add(a);

        a = new MainList("0", "success","5", "SDF 005", "Station name 5");
        mainListArrayList.add(a);

        a = new MainList("0", "success","6", "SDF 006", "Station name 6");
        mainListArrayList.add(a);

        a = new MainList("0", "success","7", "SDF 007", "Station name 7");
        mainListArrayList.add(a);


    }
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public void onRefresh() {

        refresh();
    }

    private void refresh() {
        new Handler().postDelayed(() -> mWaveSwipeRefreshLayout.setRefreshing(false), 3000);
    }

    /**
     * RecyclerItemClickListener class
     */
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


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                maintainAdapter.filterData(newText);


                return true;
            }
        });

    }
}
