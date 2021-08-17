package com.cm.cmrl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.admin.AdminStationList;
import com.cm.cmrl.arraylist.Adminstationlist;

import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.interfaces.Callback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by Iddinesh.
 */

public class AdminDepartmentAdapter extends RecyclerView.Adapter<AdminDepartmentAdapter.MyViewHolder> {

    private List<Adminstationlist> continentList;
    private List<Adminstationlist> originalList;
    private FragmentActivity fragmentActivity;
    private String id, tabs;
    private Callback callback;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView heartImageview, unHeartImageview;
        private LinearLayout layout;
        TextView stationNameCustomFontTextView;
        TextView openCountCustomFontTextView, codeCustomFontTextView;


        public MyViewHolder(View view) {
            super(view);

            stationNameCustomFontTextView =  view.findViewById(R.id.department_nmae);
            openCountCustomFontTextView =  view.findViewById(R.id.count_text);
            codeCustomFontTextView =  view.findViewById(R.id.department_code);
            heartImageview =  view.findViewById(R.id.heart);
            unHeartImageview =  view.findViewById(R.id.un_heart);
            layout =  view.findViewById(R.id.layout);


        }
    }

    public AdminDepartmentAdapter(Callback callback, FragmentActivity fragmentActivity, ArrayList<Adminstationlist> adminStationListArrayList, String id, String tabs) {

        this.fragmentActivity = fragmentActivity;
        this.continentList = adminStationListArrayList;
        this.id = id;
        this.tabs = tabs;
        this.callback = callback;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public AdminDepartmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_department_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onBindViewHolder(final AdminDepartmentAdapter.MyViewHolder holder, final int position) {
        String TAG = "AdminDepartmentAdapter";
        Log.w(TAG,"onBindViewHolder-->");
        final Adminstationlist movie = continentList.get(position);
        holder.stationNameCustomFontTextView.setText(continentList.get(position).getName());
        holder.openCountCustomFontTextView.setText(movie.getOpen_ticket_count());
        holder.codeCustomFontTextView.setText(movie.getCode());
        if (continentList.get(position).getFavourite().equalsIgnoreCase("0")) {
            holder.heartImageview.setVisibility(View.GONE);
            holder.unHeartImageview.setVisibility(View.VISIBLE);
        } else if (continentList.get(position).getFavourite().equalsIgnoreCase("1")) {
            holder.heartImageview.setVisibility(View.VISIBLE);
            holder.unHeartImageview.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences("Station", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Station", continentList.get(position).getName());
            editor.commit();
            Intent intent = new Intent(fragmentActivity, AdminStationList.class);
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("code", continentList.get(position).getCode());
            intent.putExtra("type", continentList.get(position).getStationType());
            intent.putExtra("TabSelects", tabs);
            intent.putExtra("TypeFrom", "1");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.unHeartImageview.setOnClickListener(v -> Ion.with(fragmentActivity)
                .load(ApiCall.API_URL + "favourite_tickets.php?user_id=" + id + "&station_id=" + continentList.get(position).getCode() + "&action=add")
                .asString()
                .setCallback((e, result) -> {
                    // do stuff with the result or error
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("favourite");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                holder.heartImageview.setVisibility(View.VISIBLE);
                                holder.unHeartImageview.setVisibility(View.GONE);
                                String message = jsonObject.getString("message");
                                 Toasty.success(fragmentActivity, ""+message, Toast.LENGTH_SHORT, true).show();

                               // Toast.makeText(fragmentActivity, "" + message, Toast.LENGTH_SHORT).show();
                                callback.CallbackList(tabs);
                            } else {
                                String message = jsonObject.getString("message");
                                Toasty.success(fragmentActivity, ""+message, Toast.LENGTH_SHORT, true).show();

                                //Toast.makeText(fragmentActivity, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }));
        holder.heartImageview.setOnClickListener(v -> Ion.with(fragmentActivity)
                .load(ApiCall.API_URL + "favourite_tickets.php?user_id=" + id + "&station_id=" + continentList.get(position).getCode() + "&action=remove")
                .asString()
                .setCallback((e, result) -> {
                    // do stuff with the result or error
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("favourite");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                holder.heartImageview.setVisibility(View.GONE);
                                holder.unHeartImageview.setVisibility(View.VISIBLE);
                                String message = jsonObject.getString("message");
                                Toasty.info(fragmentActivity, ""+message, Toast.LENGTH_SHORT, true).show();
                                // Toast.makeText(fragmentActivity, "" + message, Toast.LENGTH_SHORT).show();
                                callback.CallbackList(tabs);
                            } else {
                                String message = jsonObject.getString("message");
                                Toasty.success(fragmentActivity, ""+message, Toast.LENGTH_SHORT, true).show();

                                //Toast.makeText(fragmentActivity, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return continentList.size();
    }


    public void filterData(String query) {
        // TODO Auto-generated method stub
        query = query.toLowerCase(Locale.getDefault());
        continentList.clear();
        if (query.length() == 0) {
            continentList.addAll(originalList);
        } else {
            for (Adminstationlist wp : originalList) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(query) || wp.getCode().toLowerCase(Locale.getDefault())
                        .contains(query)) {
                    continentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }


}

