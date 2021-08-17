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
import com.cm.cmrl.adapter.DepartmentAdapter;
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

public class DepartmentListClass extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;

    private DepartmentList departmentList;

    private ArrayList<DepartmentList> departmentListArrayList = new ArrayList<>();

    private DepartmentAdapter departmentAdapter;

    private TextView emptyCustomFontTextView;

    private SwipeRefreshLayout mWaveSwipeRefreshLayout;

    private FloatingActionButton floatingActionButton;
    private Dialog  dialog;

    private ImageView emptyImageView;

    private Button retryButton;

    private String networkStatus = "";
    private String code;


    private String TAG = "DepartmentListClass";

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.department_list, container,
                false);

        Log.w(TAG,"onCreate--->");

        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        code = hashMap.get(SessionManager.KEY_STATION_CODE);
        String name = hashMap.get(SessionManager.KEY_STATION_NAME);
        //String userLevel = hashMap.get(SessionManager.KEY_USER_LEVEL);

        Log.w(TAG,"StationCode"+code);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyCustomFontTextView = view.findViewById(R.id.empty_text);
        emptyImageView = view.findViewById(R.id.empty_image);

        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);

        floatingActionButton = view.findViewById(R.id.fab_createevent);

        retryButton = view.findViewById(R.id.retry_button);
        LinearLayout sideMenuLayout = view.findViewById(R.id.back_layout);
        TextView customFontTextView = view.findViewById(R.id.station_name);
        LinearLayout profileLinearLayout = view.findViewById(R.id.profile_layout);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        emptyCustomFontTextView.setVisibility(View.GONE);
        customFontTextView.setText(name);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    if (floatingActionButton.isShown()) {
                        floatingActionButton.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!floatingActionButton.isShown()) {
                        floatingActionButton.show();
                    }
                }
            }
        });

        floatingActionButton.setOnClickListener(view1 -> {

            Intent intent = new Intent(getActivity(), AddIssueLayout.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.new_right, R.anim.new_left);

        });
        sideMenuLayout.setOnClickListener(view12 -> StationActivity.drawerLayout.openDrawer(StationActivity.nvDrawer));

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
                floatingActionButton.setVisibility(View.VISIBLE);


                DepaartmentUrl(ApiCall.API_URL + "departmentlist.php?" + "&station_code=" + code);
            }

        });
        profileLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);

        // check whether internet is on or not
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
            floatingActionButton.setVisibility(View.VISIBLE);

            DepaartmentUrl(ApiCall.API_URL + "departmentlist.php?" + "&station_code=" + code);
        }

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

            // Refresh the department list to call api again
            DepaartmentUrl(ApiCall.API_URL + "departmentlist.php?" + "&station_code=" + code);
            mWaveSwipeRefreshLayout.setRefreshing(false);
        }, 3000);
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
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
    private void DepaartmentUrl(String Url) {
        Log.e("", "" + Url);

        dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    departmentListArrayList.clear();
                    try {

                        JSONArray ja = response.getJSONArray("list");

                        Log.w(TAG,"Response"+ja);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("id");
                            String code = jsonObject.getString("code");
                            String name = jsonObject.getString("name");
                            String open_ticket_count = jsonObject.getString("uopen_count");
                            String inprogress_ticket_count = jsonObject.getString("uinprogress_count");
                            String pending_ticket_count = jsonObject.getString("upending_count");
                            String complete_ticket_count = jsonObject.getString("ucompleted_count");
                            String close_ticket_count = jsonObject.getString("uclosed_count");
                            departmentList = new DepartmentList(status, message, id, code, name, open_ticket_count, inprogress_ticket_count, pending_ticket_count, complete_ticket_count, close_ticket_count);
                            departmentListArrayList.add(departmentList);
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
                        emptyCustomFontTextView.setText("No departments");

                    } else {

                        emptyImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.GONE);

                        departmentAdapter = new DepartmentAdapter(getActivity(), departmentListArrayList, code);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(departmentAdapter);
                    }
                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }
}


