package com.cm.cmrl.admin;

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

import com.cm.cmrl.adapter.AdminDepartmentAdapter;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.Adminstationlist;
import com.cm.cmrl.interfaces.Callback;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.AdminActivity;
import com.cm.cmrl.view.DepartmentListClass;
import com.cm.cmrl.view.ProfileActivity;
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

public class AdminDepartmentScreen extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    private RecyclerView recyclerView;
    private Adminstationlist adminStationList;
    private ArrayList<Adminstationlist> adminStationListArrayList = new ArrayList<>();
    private AdminDepartmentAdapter adminStationListAdapter;
    private TextView emptyCustomFontTextView;
    private SwipeRefreshLayout mWaveSwipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private Dialog  dialog;
    private ImageView emptyImageView;
    private Button retryButton;
    private String networkStatus = "", selectStatus = "0", tabSelects;
    private LinearLayout elvalorLine, underLine;
    private String id;
    private LinearLayoutManager mLayoutManager;

    private String TAG = "AdminDepartmentScreen";

    @SuppressLint("RestrictedApi")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {

        Log.w(TAG,"onCreateView--->");
        final View view = inflater.inflate(R.layout.admin_department_list, container,
                false);

        //private LinearLayout changePasswordLayout;
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        id = hashMap.get(SessionManager.KEY_EMPID);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyCustomFontTextView =  view.findViewById(R.id.empty_text);
        emptyImageView = view.findViewById(R.id.empty_image);
        elvalorLine =  view.findViewById(R.id.elvator_line);
        LinearLayout elvatorLayout = view.findViewById(R.id.elvator_layout);
        LinearLayout underLayout = view.findViewById(R.id.under_layout);
        underLine =  view.findViewById(R.id.under_ground_line);

        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);
        floatingActionButton =  view.findViewById(R.id.fab_createevent);
        LinearLayout profileLinearLayout = view.findViewById(R.id.profile_layout);
        retryButton =  view.findViewById(R.id.retry_button);
        LinearLayout sideMenuLayout = view.findViewById(R.id.back_layout);
        //changePasswordLayout =  view.findViewById(R.id.change_password);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        emptyCustomFontTextView.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);



        elvatorLayout.setOnClickListener(view1 -> {
            selectStatus = "0";
            tabSelects = "0";
            elvalorLine.setVisibility(View.VISIBLE);
            underLine.setVisibility(View.INVISIBLE);
            DepaartmentUrl(ApiCall.API_URL+"get_estationtickets_new.php?user_id="+id);

        });
        underLayout.setOnClickListener(view2 -> {
            selectStatus = "1";
            tabSelects = "1";
            elvalorLine.setVisibility(View.INVISIBLE);
            underLine.setVisibility(View.VISIBLE);
            DepaartmentUrl(ApiCall.API_URL+"get_ustationtickets_new.php?user_id=" + id);


        });
               /* changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(AdminDepartmentScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Do you want to change password?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismiss();
                                Intent intent = new Intent(AdminDepartmentScreen.this, ChangepawwordActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.new_right, R.anim.new_left);
                            }
                        })
                        .show();

            }
        });*/

        sideMenuLayout.setOnClickListener(view12 -> AdminActivity.drawerLayout.openDrawer(AdminActivity.nvDrawer));
        retryButton.setOnClickListener(view13 -> {
            networkStatus = ConnectionDetector.getConnectivityStatusString(getActivity());
            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                mWaveSwipeRefreshLayout.setVisibility(View.GONE);
                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText(R.string.pleasecheckyourinternet);
                floatingActionButton.setVisibility(View.GONE);
            } else {
                if (selectStatus.equalsIgnoreCase("0")) {
                    DepaartmentUrl(ApiCall.API_URL+"get_estationtickets_new.php?user_id=" + id);
                } else if (selectStatus.equalsIgnoreCase("1")) {
                    DepaartmentUrl(ApiCall.API_URL+"get_ustationtickets_new.php?user_id=" + id);
                }
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
            }

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
            emptyCustomFontTextView.setText(R.string.pleasecheckyourinternet);
            floatingActionButton.setVisibility(View.GONE);

        } else {
            if (AdminActivity.tabSelects.equalsIgnoreCase("0")) {
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
                tabSelects = "0";
                DepaartmentUrl(ApiCall.API_URL+"get_estationtickets_new.php?user_id=" + id);
                elvalorLine.setVisibility(View.VISIBLE);
                underLine.setVisibility(View.INVISIBLE);
            } else if (AdminActivity.tabSelects.equalsIgnoreCase("1")) {
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
                tabSelects = "1";
                DepaartmentUrl(ApiCall.API_URL+"get_ustationtickets_new.php?user_id=" + id);
                elvalorLine.setVisibility(View.INVISIBLE);
                underLine.setVisibility(View.VISIBLE);
            }


        }
        profileLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mWaveSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
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
            // Refresh the department list to call api again
            if (selectStatus.equalsIgnoreCase("0")) {
                DepaartmentUrl(ApiCall.API_URL+"get_estationtickets_new.php?user_id=" + id);
            } else {
                DepaartmentUrl(ApiCall.API_URL+"get_ustationtickets_new.php?user_id=" + id);
            }

            mWaveSwipeRefreshLayout.setRefreshing(false);
        }, 3000);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void CallbackList(String status) {
        if (status.equalsIgnoreCase("0")) {
            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
            dialog.dismiss();
            tabSelects = "0";
            DepaartmentUrl(ApiCall.API_URL+"get_estationtickets_new.php?user_id=" + id);
            elvalorLine.setVisibility(View.VISIBLE);
            underLine.setVisibility(View.INVISIBLE);
        } else if (status.equalsIgnoreCase("1")) {
            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
            dialog.dismiss();
            tabSelects = "1";
            DepaartmentUrl(ApiCall.API_URL+"get_ustationtickets_new.php?user_id=" + id);
            elvalorLine.setVisibility(View.INVISIBLE);
            underLine.setVisibility(View.VISIBLE);
        }
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

    /**
     * @param Url call the api to get the department list
     */
    private void DepaartmentUrl(String Url) {
        Log.w(TAG,"URL:"+"\t"+Url);
        Log.e("Url", "" + Url);
        dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    adminStationListArrayList.clear();
                    try {
                        dialog.dismiss();
                        JSONArray ja = response.getJSONArray("list");

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jsonObject = ja.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("id");
                            String station_type = jsonObject.getString("station_type");
                            String name = jsonObject.getString("station_name");
                            String open_ticket_count = jsonObject.getString("topen");
                            String inprogress_ticket_count = jsonObject.getString("tinprogress");
                            String pending_ticket_count = jsonObject.getString("tpending");
                            String completed_ticket_count = jsonObject.getString("tcompleted");
                            String closed_ticket_count = jsonObject.getString("tclosed");
                            String favourite_status = jsonObject.getString("favourite_status");
                            String code = jsonObject.getString("code");
                            adminStationList = new Adminstationlist(status, message, id, code, station_type, name, open_ticket_count, inprogress_ticket_count, pending_ticket_count,
                                    completed_ticket_count, closed_ticket_count, favourite_status);
                            adminStationListArrayList.add(adminStationList);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();

                    if (adminStationListArrayList.isEmpty()) {

                        emptyImageView.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.VISIBLE);

                        emptyImageView.setImageResource(R.mipmap.empty_icon);
                        emptyCustomFontTextView.setText("No departments");

                    } else {

                        emptyImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyCustomFontTextView.setVisibility(View.GONE);

                        adminStationListAdapter = new AdminDepartmentAdapter(AdminDepartmentScreen.this, getActivity(), adminStationListArrayList, id, tabSelects);
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(adminStationListAdapter);
                    }
                },

                error -> {
                    Log.w(TAG,"onErrorResponse"+error.toString());
                    dialog.dismiss();
                }
        );

        requestQueue.add(jor);

    }
}

