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

import com.cm.cmrl.adapter.AdminFavoriteAdapter;
import com.cm.cmrl.api.ApiCall;

import com.cm.cmrl.arraylist.Adminstationlist;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.AdminActivity;
import com.cm.cmrl.view.DepartmentListClass;
import com.cm.cmrl.R;
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

public class AdminSelectedDepartmentScreen extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView, recyclerView1;
    private Adminstationlist adminStationList;
    private ArrayList<Adminstationlist> elevatedList = new ArrayList<>();
    private ArrayList<Adminstationlist> undergroundList = new ArrayList<>();
    private AdminFavoriteAdapter adminStationListAdapter;
    private TextView emptyCustomFontTextView;
    private SwipeRefreshLayout mWaveSwipeRefreshLayout, mWaveSwipeRefreshLayout1;
    private FloatingActionButton floatingActionButton;
    private Dialog dialog;
    private ImageView emptyImageView;
    private Button retryButton;
    private String networkStatus = "";
    private String id;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout elvalorLine, underLine;
    private boolean ele = false, und = false;

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.admin_selected_department_list, container,
                false);
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> hashMap = sessionManager.getUserDetails();
        id = hashMap.get(SessionManager.KEY_EMPID);
        recyclerView =  view.findViewById(R.id.recycler_view);
        recyclerView1 =  view.findViewById(R.id.recycler_view1);
        emptyCustomFontTextView =  view.findViewById(R.id.empty_text);
        emptyImageView =  view.findViewById(R.id.empty_image);
        mWaveSwipeRefreshLayout =  view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout1 =  view.findViewById(R.id.main_swipe1);
        floatingActionButton = view.findViewById(R.id.fab_createevent);
        retryButton =  view.findViewById(R.id.retry_button);
        LinearLayout sideMenuLayout = view.findViewById(R.id.back_layout);
        elvalorLine =  view.findViewById(R.id.elvator_line);
        LinearLayout elvatorLayout = view.findViewById(R.id.elvator_layout);
        LinearLayout underLayout = view.findViewById(R.id.under_layout);
        underLine =  view.findViewById(R.id.under_ground_line);
       // LinearLayout changePasswordLayout = view.findViewById(R.id.change_password);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout1.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout1.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout1.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout1.setOnRefreshListener(this);
        emptyCustomFontTextView.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        recyclerView.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), (view1, position) -> {
                    // TODO Handle item click
                    Intent intent = new Intent(getActivity(), AdminStationList.class);
                    intent.putExtra("name", elevatedList.get(position).getName());
                    intent.putExtra("code", elevatedList.get(position).getCode());
                    intent.putExtra("type", elevatedList.get(position).getStationType());
                    intent.putExtra("TabSelects", "0");
                    intent.putExtra("TypeFrom", "2");
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.new_right, R.anim.new_left);
                })
        );
        recyclerView1.addOnItemTouchListener(
                new DepartmentListClass.RecyclerItemClickListener(getActivity(), (view12, position) -> {
                    // TODO Handle item click
                    Intent intent = new Intent(getActivity(), AdminStationList.class);
                    intent.putExtra("name", undergroundList.get(position).getName());
                    intent.putExtra("code", undergroundList.get(position).getCode());
                    intent.putExtra("type", undergroundList.get(position).getStationType());
                    intent.putExtra("TypeFrom", "2");
                    intent.putExtra("TabSelects", "1");
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.new_right, R.anim.new_left);
                })
        );
        sideMenuLayout.setOnClickListener(view13 -> AdminActivity.drawerLayout.openDrawer(AdminActivity.nvDrawer));
        retryButton.setOnClickListener(view14 -> {
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
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
                DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
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
            emptyCustomFontTextView.setText(R.string.pleasecheckyourinternet);
            floatingActionButton.setVisibility(View.GONE);
        } else {
            if (AdminActivity.tabSelects.equalsIgnoreCase("0")) {
                ele = true;
                elvalorLine.setVisibility(View.VISIBLE);
                underLine.setVisibility(View.INVISIBLE);
                elevatedList.clear();
                mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mWaveSwipeRefreshLayout1.setVisibility(View.GONE);
                DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
            }
            else if (AdminActivity.tabSelects.equalsIgnoreCase("1")) {
                und = true;
                elvalorLine.setVisibility(View.INVISIBLE);
                underLine.setVisibility(View.VISIBLE);
                undergroundList.clear();
                mWaveSwipeRefreshLayout.setVisibility(View.GONE);
                mWaveSwipeRefreshLayout1.setVisibility(View.VISIBLE);
                DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
            }
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mWaveSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mWaveSwipeRefreshLayout1.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });
        elvatorLayout.setOnClickListener(view15 -> {
            ele = true;
            elvalorLine.setVisibility(View.VISIBLE);
            underLine.setVisibility(View.INVISIBLE);
            elevatedList.clear();
            mWaveSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mWaveSwipeRefreshLayout1.setVisibility(View.GONE);
            DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
        });
        underLayout.setOnClickListener(view16 -> {
            und = true;
            elvalorLine.setVisibility(View.INVISIBLE);
            underLine.setVisibility(View.VISIBLE);
            undergroundList.clear();
            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            mWaveSwipeRefreshLayout1.setVisibility(View.VISIBLE);
            DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
        });
        return view;
    }

    @Override
    public void onRefresh() {

        refresh();
    }



    private void refresh() {
        new Handler().postDelayed(() -> {
            DepaartmentUrl(ApiCall.API_URL+"favourite_list.php?user_id=" + id);
            mWaveSwipeRefreshLayout.setRefreshing(false);
        }, 3000);
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


        Log.e("Url", "" + Url);
        dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        @SuppressLint("SetTextI18n") JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                response -> {
                    try {

                        JSONArray ja = response.getJSONArray("favourite_list");

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
                            String code = jsonObject.getString("code");
                            if (station_type.equalsIgnoreCase("elevated")) {
                                adminStationList = new Adminstationlist(status, message, id, code, station_type, name, open_ticket_count, inprogress_ticket_count, pending_ticket_count,
                                        completed_ticket_count, closed_ticket_count, "");
                                elevatedList.add(adminStationList);
                            } else if (station_type.equalsIgnoreCase("underground")) {
                                adminStationList = new Adminstationlist(status, message, id, code, station_type, name, open_ticket_count, inprogress_ticket_count, pending_ticket_count,
                                        completed_ticket_count, closed_ticket_count, "");
                                undergroundList.add(adminStationList);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    if (ele) {
                        if (elevatedList.isEmpty()) {

                            emptyImageView.setVisibility(View.VISIBLE);
                            retryButton.setVisibility(View.GONE);
                            emptyCustomFontTextView.setVisibility(View.VISIBLE);

                            emptyImageView.setImageResource(R.mipmap.empty_icon);
                            emptyCustomFontTextView.setText("No departments");

                        } else {

                            emptyImageView.setVisibility(View.GONE);
                            retryButton.setVisibility(View.GONE);
                            emptyCustomFontTextView.setVisibility(View.GONE);

                            adminStationListAdapter = new AdminFavoriteAdapter(elevatedList);
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adminStationListAdapter);
                            ele = false;
                        }
                    } else if (und) {
                        if (undergroundList.isEmpty()) {

                            emptyImageView.setVisibility(View.VISIBLE);
                            retryButton.setVisibility(View.GONE);
                            emptyCustomFontTextView.setVisibility(View.VISIBLE);

                            emptyImageView.setImageResource(R.mipmap.empty_icon);
                            emptyCustomFontTextView.setText("No departments");

                        } else {

                            emptyImageView.setVisibility(View.GONE);
                            retryButton.setVisibility(View.GONE);
                            emptyCustomFontTextView.setVisibility(View.GONE);

                            adminStationListAdapter = new AdminFavoriteAdapter(undergroundList);
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(mLayoutManager);
                            recyclerView1.setItemAnimator(new DefaultItemAnimator());
                            recyclerView1.setAdapter(adminStationListAdapter);
                            und = false;
                        }
                    }
                },

                error -> dialog.dismiss()
        );

        requestQueue.add(jor);

    }
}

