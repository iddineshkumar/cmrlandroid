package com.cm.cmrl.maintain;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.cm.cmrl.arraylist.AllTimeList;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.AllFragmentTimeAdapter;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Iddinesh.
 */

public class MoniterFragmetTime extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<AllTimeList> allTimeListArrayList = new ArrayList<>();

    private TextView emptyCustomFontTextView;

   private SwipeRefreshLayout mWaveSwipeRefreshLayout;

    private FloatingActionButton floatingActionButton;
    private ImageView emptyImageView;

    private Button retryButton;

    private String networkStatus = "";

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.all_fragment_time, container,
                false);



        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        emptyCustomFontTextView = view.findViewById(R.id.empty_text);

        emptyImageView = view.findViewById(R.id.empty_image);

        retryButton = view.findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);

        floatingActionButton = view.findViewById(R.id.fab_createevent);


        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), (view1, position) -> {
                    // TODO Handle item click
                    MoniterFragmentDetail orderManagement = new MoniterFragmentDetail();
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();
                    }

                })
        );



        retryButton.setOnClickListener(view12 -> {

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

            AllFragmentTimeAdapter allFragmentTimeAdapter = new AllFragmentTimeAdapter(allTimeListArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(allFragmentTimeAdapter);

        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    MonitorFragment orderManagement = new MonitorFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();
                    }


                    return true;
                }
            }
            return false;
        });
        return view;

    }
    private void prepareAlbums() {

        AllTimeList a = new AllTimeList("7:30:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("0:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("2:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("4:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("6:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("8:00","09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("10:00", "09");
        allTimeListArrayList.add(a);

        a = new AllTimeList("12:00", "09");
        allTimeListArrayList.add(a);


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


}
