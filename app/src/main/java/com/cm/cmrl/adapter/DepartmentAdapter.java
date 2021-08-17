package com.cm.cmrl.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.R;
import com.cm.cmrl.arraylist.DepartmentList;
import com.cm.cmrl.view.DepartmentStatusListClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {

    private List<DepartmentList> continentList;
    private List<DepartmentList> originalList;
    private FragmentActivity fragmentActivity;
    private String code;
    private String TAG = "DepartmentAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fromCustomFontTextView;
        ImageView editImageView;
        LinearLayout editLinearLayout;
        TextView openCountCustomFontTextView, pendingCountCustomFontTextView;
        TextView inProgressCustomFontTextView, completeCustomFontTextView, closeCountCustomFontTextView;
        private LinearLayout openLayout, inprogressLayout, pendingLayout, completeLayout, closeLayout;

        public MyViewHolder(View view) {
            super(view);
            fromCustomFontTextView =  view.findViewById(R.id.from_date_text);
            editImageView =  view.findViewById(R.id.edit_image);
            editLinearLayout = view.findViewById(R.id.edit_layout);
            openCountCustomFontTextView = view.findViewById(R.id.open_count);
            pendingCountCustomFontTextView =  view.findViewById(R.id.pending_count);
            inProgressCustomFontTextView =  view.findViewById(R.id.inprogress_count);
            completeCustomFontTextView =  view.findViewById(R.id.completed_count);
            closeCountCustomFontTextView = view.findViewById(R.id.close_count);
            closeLayout = view.findViewById(R.id.back5);
            openLayout =  view.findViewById(R.id.back1);
            inprogressLayout =  view.findViewById(R.id.back2);
            pendingLayout =  view.findViewById(R.id.back3);
            completeLayout =  view.findViewById(R.id.back4);
        }
    }

    public DepartmentAdapter(FragmentActivity fragmentActivity, List<DepartmentList> moviesList, String code) {

        this.fragmentActivity = fragmentActivity;
        this.continentList = moviesList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
        this.code = code;
    }

    @NonNull
    @Override
    public DepartmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_grid_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DepartmentAdapter.MyViewHolder holder, final int position) {



        final DepartmentList movie = continentList.get(position);
        holder.fromCustomFontTextView.setText(movie.getName());
        holder.openCountCustomFontTextView.setText(movie.getOpen_ticket_count());
        holder.pendingCountCustomFontTextView.setText(movie.getPending_ticket_count());
        holder.inProgressCustomFontTextView.setText(movie.getInprogress_ticket_count());
        holder.completeCustomFontTextView.setText(movie.getCompleted_ticket_count());
        holder.closeCountCustomFontTextView.setText(movie.getClose_ticket_count());
        if (movie.getClose_ticket_count().equalsIgnoreCase("0")) {
            holder.closeLayout.setAlpha(0.2f);
            holder.closeLayout.setEnabled(false);
        }else{
            holder.closeLayout.setAlpha(1f);
            holder.closeLayout.setEnabled(true);
        }
        if (movie.getCompleted_ticket_count().equalsIgnoreCase("0")) {
            holder.completeLayout.setAlpha(0.2f);
            holder.completeLayout.setEnabled(false);
        }else{
            holder.completeLayout.setAlpha(1f);
            holder.completeLayout.setEnabled(true);
        }
        if (movie.getInprogress_ticket_count().equalsIgnoreCase("0")) {
            holder.inprogressLayout.setAlpha(0.2f);
            holder.inprogressLayout.setEnabled(false);
        }else{
            holder.inprogressLayout.setAlpha(1f);
            holder.inprogressLayout.setEnabled(true);
        }
        if (movie.getOpen_ticket_count().equalsIgnoreCase("0")) {
            holder.openLayout.setAlpha(0.2f);
            holder.openLayout.setEnabled(false);
        }else{
            holder.openLayout.setAlpha(1f);
            holder.openLayout.setEnabled(true);
        }
        if (movie.getPending_ticket_count().equalsIgnoreCase("0")) {
            holder.pendingLayout.setAlpha(0.2f);
            holder.pendingLayout.setEnabled(false);
        }else{
            holder.pendingLayout.setAlpha(1f);
            holder.pendingLayout.setEnabled(true);
        }

        Log.w(TAG,"station_code :"+continentList.get(position).getCode());
        Log.w(TAG,"code :"+code);
        holder.openLayout.setOnClickListener(v -> {
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "1");
            intent.putExtra("ticket", "Open Issues");
            Log.w(TAG,"TicketStatus :"+"1"+"Ticket :"+"Open");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Open");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.inprogressLayout.setOnClickListener(v -> {
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "2");
            intent.putExtra("ticket", "Inprogress Issues");
            Log.w(TAG,"TicketStatus :"+"2"+"Ticket :"+"Inprogress");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Inprogress");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.completeLayout.setOnClickListener(v -> {
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
            Intent intent = new Intent(fragmentActivity, DepartmentStatusListClass.class);
            intent.putExtra("ticket_status", "3");
            intent.putExtra("ticket", "Pending Issues");
            Log.w(TAG,"TicketStatus :"+"3"+"Ticket :"+"Pending");
            intent.putExtra("code", code);
            intent.putExtra("station_code", continentList.get(position).getCode());
            intent.putExtra("name", continentList.get(position).getName());
            intent.putExtra("issues", "Pending");
            fragmentActivity.startActivity(intent);
            fragmentActivity.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.closeLayout.setOnClickListener(v -> {
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
            for (DepartmentList wp : originalList) {
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

