package com.cm.cmrl.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.arraylist.Adminstationlist;
import com.cm.cmrl.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class AdminFavoriteAdapter extends RecyclerView.Adapter<AdminFavoriteAdapter.MyViewHolder> {

    private List<Adminstationlist> continentList;
    private List<Adminstationlist> originalList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView stationNameCustomFontTextView;
        TextView openCountCustomFontTextView, codeCustomFontTextView;


        public MyViewHolder(View view) {
            super(view);

            stationNameCustomFontTextView = view.findViewById(R.id.department_nmae);
            openCountCustomFontTextView =  view.findViewById(R.id.count_text);
            codeCustomFontTextView =  view.findViewById(R.id.department_code);


        }
    }

    public AdminFavoriteAdapter(ArrayList<Adminstationlist> adminStationListArrayList) {
        this.continentList = adminStationListArrayList;
        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public AdminFavoriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_favorite_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdminFavoriteAdapter.MyViewHolder holder, final int position) {

        final Adminstationlist movie = continentList.get(position);
        holder.stationNameCustomFontTextView.setText(continentList.get(position).getName());
        holder.openCountCustomFontTextView.setText(movie.getOpen_ticket_count());
        holder.codeCustomFontTextView.setText(movie.getCode());
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

