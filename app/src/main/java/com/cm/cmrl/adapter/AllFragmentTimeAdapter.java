package com.cm.cmrl.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cm.cmrl.arraylist.AllTimeList;
import com.cm.cmrl.R;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class AllFragmentTimeAdapter extends  RecyclerView.Adapter<AllFragmentTimeAdapter.MyViewHolder> {

    private List<AllTimeList> continentList;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView timeCustomFontTextView;
    TextView completedCountCustomFontTextView;




    public MyViewHolder(View view) {
        super(view);

        timeCustomFontTextView =  view.findViewById(R.id.time_text);
        completedCountCustomFontTextView =  view.findViewById(R.id.completed_count);


    }
}

    public AllFragmentTimeAdapter(List<AllTimeList> moviesList) {

        this.continentList = moviesList;

    }

    @NonNull
    @Override
    public AllFragmentTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AllFragmentTimeAdapter.MyViewHolder holder, int position) {

        final AllTimeList movie = continentList.get(position);

        holder.timeCustomFontTextView.setText("Time : "+movie.getEquipmentTime());
        holder.completedCountCustomFontTextView.setText(movie.getCompletedCount());

    }
    @Override
    public int getItemCount() {
        return continentList.size();
    }

}


