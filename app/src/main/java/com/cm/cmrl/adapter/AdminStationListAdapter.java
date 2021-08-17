package com.cm.cmrl.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.arraylist.Adminstationlist;

import com.cm.cmrl.R;
import com.cm.cmrl.view.DepartmentStatusListClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class AdminStationListAdapter extends RecyclerView.Adapter<AdminStationListAdapter.MyViewHolder> {

    private List<Adminstationlist> continentList;
    private List<Adminstationlist> originalList;
    private FragmentActivity fragmentActivity;

    private String code;

    private String TAG ="AdminStationListAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout openLayout, inprogressLayout, pendingLayout, completeLayout, closeLayout;
         TextView stationNameCustomFontTextView;
         TextView openCountCustomFontTextView, pendingCountCustomFontTextView;
         TextView inProgressCustomFontTextView, completeCustomFontTextView, closeCountCustomFontTextView;


        public MyViewHolder(View view) {
            super(view);

            stationNameCustomFontTextView =  view.findViewById(R.id.name_text);
            openCountCustomFontTextView =  view.findViewById(R.id.open_count);
            pendingCountCustomFontTextView =  view.findViewById(R.id.pending_count);
            inProgressCustomFontTextView = view.findViewById(R.id.inprogress_count);
            completeCustomFontTextView =  view.findViewById(R.id.completed_count);
            closeCountCustomFontTextView =  view.findViewById(R.id.close_count);
            closeLayout =  view.findViewById(R.id.back5);
            openLayout =  view.findViewById(R.id.back1);
            inprogressLayout =  view.findViewById(R.id.back2);
            pendingLayout =  view.findViewById(R.id.back3);
            completeLayout =  view.findViewById(R.id.back4);
        }
    }

    public AdminStationListAdapter(FragmentActivity fragmentActivity, List<Adminstationlist> moviesList, String code) {
        this.fragmentActivity = fragmentActivity;
        this.continentList = moviesList;
        this.code = code;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public AdminStationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adminstation_list_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdminStationListAdapter.MyViewHolder holder, final int position) {

        final Adminstationlist movie = continentList.get(position);
        holder.stationNameCustomFontTextView.setText(movie.getName());
        holder.openCountCustomFontTextView.setText(movie.getOpen_ticket_count());
        holder.pendingCountCustomFontTextView.setText(movie.getPending_ticket_count());
        holder.inProgressCustomFontTextView.setText(movie.getInprogress_ticket_count());
        holder.completeCustomFontTextView.setText(movie.getCompleted_ticket_count());
        holder.closeCountCustomFontTextView.setText(movie.getClosed_ticket_count());
        if (movie.getClosed_ticket_count().equalsIgnoreCase("0")) {
            holder.closeLayout.setAlpha(0.2f);
            holder.closeLayout.setEnabled(false);
            Log.w(TAG,"CloseTicketCountIfPart-->"+movie.getClosed_ticket_count());
        }
        else{
            holder.closeLayout.setAlpha(1.0f);
            Log.w(TAG,"Else CloseTicketCountIfPart-->"+movie.getClosed_ticket_count());
            holder.closeLayout.setEnabled(true);

        }
        if (movie.getCompleted_ticket_count().equalsIgnoreCase("0")) {
            holder.completeLayout.setAlpha(0.2f);
            holder.completeLayout.setEnabled(false);
        }else{
            holder.completeLayout.setAlpha(1.0f);
            holder.completeLayout.setEnabled(true);
        }
        if (movie.getInprogress_ticket_count().equalsIgnoreCase("0")) {
            holder.inprogressLayout.setAlpha(0.2f);
            holder.inprogressLayout.setEnabled(false);
        }else{
            holder.inprogressLayout.setAlpha(1.0f);
            holder.inprogressLayout.setEnabled(true);
        }
        if (movie.getOpen_ticket_count().equalsIgnoreCase("0")) {
            holder.openLayout.setAlpha(0.2f);
            holder.openLayout.setEnabled(false);
            Log.w(TAG,"OpenTicketCountIfPart-->"+movie.getOpen_ticket_count());
        }else{
            holder.openLayout.setAlpha(1.0f);
            holder.openLayout.setEnabled(true);
            Log.w(TAG,"OpenTicketCountElsePart-->"+movie.getOpen_ticket_count());
        }
        if (movie.getPending_ticket_count().equalsIgnoreCase("0")) {
            holder.pendingLayout.setAlpha(0.2f);
            holder.pendingLayout.setEnabled(false);
        }else{
            holder.pendingLayout.setAlpha(1.0f);
            holder.pendingLayout.setEnabled(true);
        }
        holder.openLayout.setOnClickListener(v -> {

            Log.w(TAG,"code :-->"+code+"station_code :"+continentList.get(position).getCode());
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "1");
            intent.putExtra("ticket", "Open Issues");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Open");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.inprogressLayout.setOnClickListener(v -> {
            Log.w(TAG,"code :-->"+code+"station_code :"+continentList.get(position).getCode());
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "2");
            intent.putExtra("ticket", "Inprogress Issues");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Inprogress");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.completeLayout.setOnClickListener(v -> {
            Log.w(TAG,"code :-->"+code+"station_code :"+continentList.get(position).getCode());
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "4");
            intent.putExtra("ticket", "Completed Issues");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Completed");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.pendingLayout.setOnClickListener(v -> {
            Log.w(TAG,"code :-->"+code+"station_code :"+continentList.get(position).getCode());
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "3");
            intent.putExtra("ticket", "Pending Issues");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Pending");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.closeLayout.setOnClickListener(v -> {
            Log.w(TAG,"code :-->"+code+"station_code :"+continentList.get(position).getCode());
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "5");
            intent.putExtra("ticket", "Close Issues");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Close");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
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
                        .contains(query) || wp.getStationType().toLowerCase(Locale.getDefault())
                        .contains(query)) {
                    continentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }


}

