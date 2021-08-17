package com.cm.cmrl.ups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.UpsNameAdapter;
import com.cm.cmrl.arraylist.MonitorUpsList;
import com.cm.cmrl.helper.DatabaseHandler;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class UpsTimeFragment  extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView recyclerView;
    UpsNameAdapter upsNameAdapter;
    List<MonitorUpsList> monitorUpsListArrayList = new ArrayList<>();
    TextView emptyCustomFontTextView,titleCustomFontTextView,dateCustomFontTextView;
    SwipeRefreshLayout mWaveSwipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    LinearLayout signOutLayout;
    LinearLayout sidemenuLayout,dateLayout;
    ImageView emptyImageView;
    SessionManager sessionManager;
    Button retryButton;
    String networkStatus = "", empid = "", station_code = "";
    SearchView searchView;
    String title="";
    DatabaseHandler db;

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.monitor_ups_list, container,
                false);

        db = new DatabaseHandler(getActivity());

        title =  getArguments().getString("name");

        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        empid = hashMap.get(sessionManager.KEY_EMPID);
        station_code = hashMap.get(sessionManager.KEY_STATION_CODE);
        sessionManager = new SessionManager(getActivity());

        sidemenuLayout =  view.findViewById(R.id.back_layout);
        signOutLayout =  view.findViewById(R.id.sign_out);
        dateLayout =  view.findViewById(R.id.date_layout);

        toolbar =  view.findViewById(R.id.toolbar);

        recyclerView =  view.findViewById(R.id.recycler_view);

        searchView =  view.findViewById(R.id.search);

        emptyCustomFontTextView =  view.findViewById(R.id.empty_text);
        titleCustomFontTextView =  view.findViewById(R.id.title);
        dateCustomFontTextView =  view.findViewById(R.id.date_text);

        emptyImageView =  view.findViewById(R.id.empty_image);

        retryButton =  view.findViewById(R.id.retry_button);

        mWaveSwipeRefreshLayout =  view.findViewById(R.id.main_swipe);

        floatingActionButton =  view.findViewById(R.id.fab_createevent);

        sidemenuLayout.setVisibility(View.INVISIBLE);
        signOutLayout.setVisibility(View.INVISIBLE);

        titleCustomFontTextView.setText(title);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy , hh:mm a");
        String formattedDate = df.format(c.getTime());

        dateCustomFontTextView.setText(formattedDate);

        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), (view1, position) -> {
                    // TODO Handle item click
                    // Call Schedule or Home screen.
                    Bundle bundle =new Bundle();
                    bundle.putString("name",title);
                    UpsMonitorUpdate orderManagement = new UpsMonitorUpdate();
                    orderManagement.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();

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
                    Upsmonitor orderManagement = new Upsmonitor();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();


                    return true;
                }
            }
            return false;
        });

        return view;

    }
    private void prepareAlbums() {

        monitorUpsListArrayList = db.getAllUpsTimeList();

        if(monitorUpsListArrayList.isEmpty()){

            db.addTimeList(new MonitorUpsList("equipment id 1", "6:00 AM t0 2:00 PM"));
            db.addTimeList(new MonitorUpsList("equipment id 2", "2:00 PM to 10:00 PM"));
            db.addTimeList(new MonitorUpsList("equipment id 3", "10:00 PM to 6:00 AM"));

        }
        else {



        }
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        monitorUpsListArrayList = db.getAllUpsTimeList();


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
            public void onItemClick(View view, int position);
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
