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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm.cmrl.R;
import com.cm.cmrl.adapter.StationlistAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.DepartmentList;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.utils.ConnectionDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class StationListActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;

    private DepartmentList departmentList;

    private ArrayList<DepartmentList> departmentListArrayList = new ArrayList<>();

    private StationlistAdapter stationlistAdapter;

    private TextView emptyCustomFontTextView;

    private SwipeRefreshLayout mWaveSwipeRefreshLayout;

    private FloatingActionButton floatingActionButton;

    private Dialog dialog;


    private ImageView emptyImageView;

    private Button retryButton;

    private String networkStatus = "";
    private String station_code = "";

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.station_list_layout, container,
                false);

        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();

        station_code = hashMap.get(SessionManager.KEY_STATION_CODE);
        String name = hashMap.get(SessionManager.KEY_STATION_NAME);

        LinearLayout sidemenuLayout = view.findViewById(R.id.back_layout);

        recyclerView = view.findViewById(R.id.recycler_view);

        emptyCustomFontTextView = view.findViewById(R.id.empty_text);

        emptyImageView = view.findViewById(R.id.empty_image);

        retryButton = view.findViewById(R.id.retry_button);
        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);
        LinearLayout profileLinearLayout = view.findViewById(R.id.profile_layout);
        floatingActionButton = view.findViewById(R.id.fab_createevent);
        TextView customFontTextView = view.findViewById(R.id.name);


        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);
        customFontTextView.setText(name);
        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), (view1, position) -> {
                    // TODO Handle item click

                    Intent intent = new Intent(getActivity(), IssuseStatus.class);
                    intent.putExtra("code", departmentListArrayList.get(position).getCode());
                    intent.putExtra("name", departmentListArrayList.get(position).getName());
                    intent.putExtra("s_code", station_code);

                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.new_right, R.anim.new_left);
                })
        );


        sidemenuLayout.setOnClickListener(view12 -> MAinfragmentActivty.drawerLayout.openDrawer(MAinfragmentActivty.nvDrawer));

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

                DepaartmentUrl(ApiCall.API_URL+"stationlist.php?department_code=" + station_code);
            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

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
            DepaartmentUrl(ApiCall.API_URL + "stationlist.php?department_code=" + station_code);
        }
        profileLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),ProfileActivity.class);
            startActivity(intent);
        });

        return view;

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
        new Handler().postDelayed(() -> {


            DepaartmentUrl(ApiCall.API_URL + "stationlist.php?department_code=" + station_code);
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
        public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void DepaartmentUrl(String Url) {

        dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        Log.e("StationUrl", "" + Url);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    departmentListArrayList.clear();
                    try {
                        JSONArray ja = response.getJSONArray("list");


                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("id");
                            String code = jsonObject.getString("code");
                            String name = jsonObject.getString("name");
                            String open_ticket_count = jsonObject.getString("open_ticket_count");
                            departmentList = new DepartmentList(status, message, id, code, name, open_ticket_count, "", "", "", "");
                            departmentListArrayList.add(departmentList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if (departmentListArrayList.isEmpty()) {

                        emptyCustomFontTextView.setVisibility(View.VISIBLE);
                        emptyCustomFontTextView.setText("No time duration list");

                    } else {

                        emptyCustomFontTextView.setVisibility(View.INVISIBLE);

                        stationlistAdapter = new StationlistAdapter( departmentListArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(stationlistAdapter);

                    }
                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);
    }

}
