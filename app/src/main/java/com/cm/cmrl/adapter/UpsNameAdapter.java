package com.cm.cmrl.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.arraylist.MonitorUpsList;
import com.cm.cmrl.R;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class UpsNameAdapter extends RecyclerView.Adapter<UpsNameAdapter.MyViewHolder>  {

    private List<MonitorUpsList> continentList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView equipmentNameCustomFontTextView;

        public MyViewHolder(View view) {
            super(view);

            equipmentNameCustomFontTextView = view.findViewById(R.id.equipment_name);

        }
    }

    public UpsNameAdapter(List<MonitorUpsList> moviesList) {

        this.continentList = moviesList;

    }

    @NonNull
    @Override
    public UpsNameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ups_monitor_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UpsNameAdapter.MyViewHolder holder, int position) {

        final MonitorUpsList movie = continentList.get(position);
        holder.equipmentNameCustomFontTextView.setText(movie.getName());

    }
    @Override
    public int getItemCount() {
        return continentList.size();
    }


}



