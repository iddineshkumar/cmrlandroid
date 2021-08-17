package com.cm.cmrl.ups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import com.cm.cmrl.R;
import com.cm.cmrl.adapter.UpsBankAdapter;
import com.cm.cmrl.arraylist.UpsBankUpdateList;
import com.cm.cmrl.session.SessionManager;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import com.cm.cmrl.utils.ConnectionDetector;
import com.cm.cmrl.view.DepartmentListClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Iddinesh.
 */

public class UpsBankUpdate extends Fragment {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    UpsBankAdapter upsNameAdapter;
    ArrayList<UpsBankUpdateList> monitorUpsListArrayList =new ArrayList<>();
    TextView emptyCustomFontTextView,titleCustomFontTextView,dateCustomFontTextView;
    FloatingActionButton floatingActionButton;
    LinearLayout signOutLayout;
    LinearLayout sidemenuLayout,dateLayout;
    ImageView emptyImageView;
    SessionManager sessionManager;
    Button retryButton;
    String networkStatus = "", empid = "", station_code = "";
    Button subButton,sync;
    LinearLayout cancel;

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.ups_bank_update, container,
                false);
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

        emptyCustomFontTextView =  view.findViewById(R.id.empty_text);
        titleCustomFontTextView =  view.findViewById(R.id.title);
        dateCustomFontTextView =  view.findViewById(R.id.date_text);

        emptyImageView = view.findViewById(R.id.empty_image);

        retryButton = view.findViewById(R.id.retry_button);

        floatingActionButton = view.findViewById(R.id.fab_createevent);

        subButton = view.findViewById(R.id.add_button);
        sync = view.findViewById(R.id.sync);

        cancel = view.findViewById(R.id.clear_button);
        sidemenuLayout.setVisibility(View.INVISIBLE);
        signOutLayout.setVisibility(View.INVISIBLE);
        titleCustomFontTextView.setText("Update");
        emptyCustomFontTextView.setVisibility(View.GONE);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy , hh:mm a");
        String formattedDate = df.format(c.getTime());

        dateCustomFontTextView.setText(formattedDate);


        subButton.setOnClickListener(view1 -> {

            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("CMRL")
                    .setContentText("Update confirmation")
                    .setConfirmText("Ok")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismiss();

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();


        });

        sync.setOnClickListener(view12 -> new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Update confirmation")
                .setConfirmText("Ok")
                .setCancelText("No")
                .setConfirmClickListener(sDialog -> sDialog.dismiss())
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show());


        retryButton.setOnClickListener(view13 -> {

            networkStatus = ConnectionDetector.getConnectivityStatusString(getActivity());

            if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {

                emptyCustomFontTextView.setVisibility(View.VISIBLE);
                emptyImageView.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                emptyImageView.setImageResource(R.mipmap.wifi);
                emptyCustomFontTextView.setText("Please check your internet connectivity and try again");
                floatingActionButton.setVisibility(View.GONE);
            } else {


                floatingActionButton.setVisibility(View.GONE);

            }

        });

        emptyImageView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        emptyCustomFontTextView.setVisibility(View.GONE);
        prepareAlbums();
        networkStatus = ConnectionDetector.getConnectivityStatusString(getActivity());

        if (networkStatus.equalsIgnoreCase("Not connected to Internet")) {


            emptyCustomFontTextView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            emptyImageView.setImageResource(R.mipmap.wifi);
            emptyCustomFontTextView.setText("Please check your internet connectivity and try again");
            floatingActionButton.setVisibility(View.GONE);

        } else {


            floatingActionButton.setVisibility(View.GONE);
            emptyCustomFontTextView.setVisibility(View.INVISIBLE);


            upsNameAdapter = new UpsBankAdapter( monitorUpsListArrayList);
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
                    UpsBank orderManagement = new UpsBank();
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

        UpsBankUpdateList a = new UpsBankUpdateList("Battery 1","","0");
        monitorUpsListArrayList.add(a);

        a = new UpsBankUpdateList("Battery 2","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 3","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 4","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 5","","0");
        monitorUpsListArrayList.add(a);

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
