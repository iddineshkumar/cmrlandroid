package com.cm.cmrl.ups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.UpsNameAdapter;
import com.cm.cmrl.arraylist.MonitorUpsList;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class UpsBank extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    UpsNameAdapter upsNameAdapter;
    ArrayList<MonitorUpsList> monitorUpsListArrayList =new ArrayList<>();
    TextView emptyCustomFontTextView,titleCustomFontTextView;
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    LinearLayout signOutLayout;
    LinearLayout sidemenuLayout,dateLayout;
    ImageView emptyImageView;
    SessionManager sessionManager;
    Button retryButton;
    String networkStatus = "", empid = "", station_code = "";
    SearchView searchView;

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.monitor_ups_list, container,
                false);

        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(sessionManager.KEY_EMPID);
        station_code = hashMap.get(sessionManager.KEY_STATION_CODE);
        sessionManager = new SessionManager(getActivity());

        sidemenuLayout = view.findViewById(R.id.back_layout);
        signOutLayout = view.findViewById(R.id.sign_out);
        dateLayout = view.findViewById(R.id.date_layout);

        toolbar = view.findViewById(R.id.toolbar);

        recyclerView = view.findViewById(R.id.recycler_view);

        searchView = view.findViewById(R.id.search);

        emptyCustomFontTextView = view.findViewById(R.id.empty_text);
        titleCustomFontTextView = view.findViewById(R.id.title);

        emptyImageView = view.findViewById(R.id.empty_image);

        retryButton = view.findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);

        floatingActionButton = view.findViewById(R.id.fab_createevent);

        dateLayout.setVisibility(View.GONE);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);

        titleCustomFontTextView.setText("Maintenance");

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), new DepartmentListClass.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        // Call Schedule or Home screen.

                        Bundle bundle =new Bundle();
                        bundle.putString("name",monitorUpsListArrayList.get(position).getName());
                        UpsBankUpdateTwo orderManagement = new UpsBankUpdateTwo();
                        orderManagement.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();

                    }
                })
        );

        sidemenuLayout.setOnClickListener(view1 -> UpsActivity.drawerLayout.openDrawer(UpsActivity.nvDrawer));


        signOutLayout.setOnClickListener(view12 -> {


            Upscode orderManagement = new Upscode();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();


        });
        retryButton.setOnClickListener(view13 -> {

            networkStatus = ConnectionDetector.getConnectivityStatusString(getActivity());

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
        prepareAlbums();
        networkStatus = ConnectionDetector.getConnectivityStatusString(getActivity());

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
            emptyCustomFontTextView.setVisibility(View.INVISIBLE);

            upsNameAdapter = new UpsNameAdapter( monitorUpsListArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(upsNameAdapter);

        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(), UpsActivity.class);
                    startActivity(intent);

                    return true;
                }
            }
            return false;
        });
        return view;

    }
    private void prepareAlbums() {

        MonitorUpsList a = new MonitorUpsList("equipment id 1", "UPS BANK 1");
        monitorUpsListArrayList.add(a);

        a = new MonitorUpsList("equipment id 1", "UPS BANK 2");
        monitorUpsListArrayList.add(a);



    }


    @Override
    public void onRefresh() {

        refresh();
    }

    private void refresh() {
        new Handler().postDelayed(() -> {
            mWaveSwipeRefreshLayout.setRefreshing(false);

        }, 3000);
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
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



}

