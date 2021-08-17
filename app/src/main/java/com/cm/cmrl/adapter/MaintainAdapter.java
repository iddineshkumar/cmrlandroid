package com.cm.cmrl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.arraylist.MainList;
import com.cm.cmrl.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class MaintainAdapter extends RecyclerView.Adapter<MaintainAdapter.MyViewHolder> {

    private List<MainList> continentList;
    private List<MainList> originalList;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView stationNameCustomFontTextView,stationCodeCustomFontTextView;


        public MyViewHolder(View view) {
            super(view);

            stationNameCustomFontTextView = view.findViewById(R.id.location_text);
            stationCodeCustomFontTextView = view.findViewById(R.id.code_text);

        }
    }

    public MaintainAdapter(List<MainList> moviesList) {

        this.continentList = moviesList;

        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public MaintainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maintain_stationlist_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MaintainAdapter.MyViewHolder holder, int position) {

        final MainList movie = continentList.get(position);

        holder.stationNameCustomFontTextView.setText(movie.getName());
        holder.stationCodeCustomFontTextView.setText(movie.getCode());


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
            for (MainList wp : originalList) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(query)||wp.getCode().toLowerCase(Locale.getDefault())
                        .contains(query)) {
                    continentList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }


}

