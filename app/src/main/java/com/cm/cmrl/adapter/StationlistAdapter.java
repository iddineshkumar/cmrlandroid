package com.cm.cmrl.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cm.cmrl.arraylist.DepartmentList;
import com.cm.cmrl.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class StationlistAdapter extends RecyclerView.Adapter<StationlistAdapter.MyViewHolder>  {

    private List<DepartmentList> continentList;
    private List<DepartmentList> originalList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView locationCustomFontTextView, codeCustomFontTextView,countCustomFontTextView;


        public MyViewHolder(View view) {
            super(view);

            locationCustomFontTextView =  view.findViewById(R.id.location_text);
            codeCustomFontTextView =  view.findViewById(R.id.code_text);
            countCustomFontTextView =  view.findViewById(R.id.count);


        }
    }

    public StationlistAdapter(List<DepartmentList> moviesList) {

        this.continentList = moviesList;

        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }

    @NonNull
    @Override
    public StationlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_list_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(StationlistAdapter.MyViewHolder holder, int position) {

        final DepartmentList movie = continentList.get(position);
        holder.codeCustomFontTextView.setText(movie.getCode());
        holder.locationCustomFontTextView.setText(movie.getName());
        holder.countCustomFontTextView.setText("OPEN: "+movie.getOpen_ticket_count());

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
            for (DepartmentList wp : originalList) {
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

