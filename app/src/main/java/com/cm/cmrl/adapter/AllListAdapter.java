package com.cm.cmrl.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cm.cmrl.arraylist.AllList;
import com.cm.cmrl.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class AllListAdapter extends RecyclerView.Adapter<AllListAdapter.MyViewHolder>  {

    private List<AllList> continentList;
    private List<AllList> originalList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

         TextView equipmentNameCustomFontTextView, equipmentIdCustomFontTextView,timeCustomFontTextView;
         TextView equipmentSatus, compledCountCustomFontTextView,pendingCountCustomFontTextView;
         TextView upcomingCountCustomFontTextView;

        public MyViewHolder(View view) {
            super(view);

            equipmentNameCustomFontTextView =  view.findViewById(R.id.equipment_name);
            equipmentIdCustomFontTextView =  view.findViewById(R.id.equipment_id);
            timeCustomFontTextView =  view.findViewById(R.id.time_text);

            equipmentSatus =  view.findViewById(R.id.equipment_status);
            compledCountCustomFontTextView =  view.findViewById(R.id.completed_count);
            pendingCountCustomFontTextView =  view.findViewById(R.id.pending_count);
            upcomingCountCustomFontTextView =  view.findViewById(R.id.upcoming_count);


        }
    }

    public AllListAdapter(List<AllList> moviesList) {

        this.continentList = moviesList;

        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public AllListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_fragment_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllListAdapter.MyViewHolder holder, int position) {

        final AllList movie = continentList.get(position);
        holder.equipmentNameCustomFontTextView.setText(movie.getEquipmentName());
        holder.equipmentIdCustomFontTextView.setText(movie.getEquipmentId());
        holder.timeCustomFontTextView.setText(movie.getEquipmentTime());
        holder.equipmentSatus.setText(movie.getEquipmentType());
        holder.compledCountCustomFontTextView.setText(movie.getCompletedCount());
        holder.pendingCountCustomFontTextView.setText(movie.getPendingCount());
        holder.upcomingCountCustomFontTextView.setText(movie.getUpcomingCount());

    }
    @Override
    public int getItemCount() {
        return continentList.size();
    }


    public void filterData(String query){
        // TODO Auto-generated method stub
        query = query.toLowerCase(Locale.getDefault());
        continentList.clear();
        if (query.length() == 0) {
            continentList.addAll(originalList);
        } else {
            for (AllList wp : originalList) {
                if (wp.getEquipmentName().toLowerCase(Locale.getDefault())
                        .contains(query)||wp.getEquipmentId().toLowerCase(Locale.getDefault())
                        .contains(query)) {
                    continentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

}


