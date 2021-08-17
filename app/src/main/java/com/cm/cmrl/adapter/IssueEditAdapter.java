package com.cm.cmrl.adapter;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cm.cmrl.arraylist.UpdateHistoryList;
import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;

import com.cm.cmrl.view.IssuseEditList;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class IssueEditAdapter extends RecyclerView.Adapter<IssueEditAdapter.MyViewHolder> {

    private List<UpdateHistoryList> moviesList;

    private FragmentActivity context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleView,updateView,empIdView,dateView,decriptopnView;

        ImageView circularImageView;

        public MyViewHolder(View view) {

            super(view);
            titleView = view.findViewById(R.id.title_name);
            updateView =  view.findViewById(R.id.user_nmae);
            empIdView = view.findViewById(R.id.employye_id);
            dateView = view.findViewById(R.id.date_text);
            decriptopnView = view.findViewById(R.id.discription);

            circularImageView = view.findViewById(R.id.circle_image);


        }
    }


    public IssueEditAdapter(FragmentActivity context, List<UpdateHistoryList> moviesList) {

        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueEditAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issuse_edit_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IssueEditAdapter.MyViewHolder holder, int position) {

        final UpdateHistoryList movie = moviesList.get(position);

        SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat("MMM dd yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormattt = new SimpleDateFormat("yyyy-MM-dd");

        String[] updateAt =movie.getUpdated_at().split(" ");
        try {

            Date date = dateFormattt.parse(updateAt[0]);
            if (date != null) {
                holder.dateView.setText(daySimpleDateFormat.format(date));
            }


        } catch (ParseException e) {

            e.printStackTrace();
        }


        String TAG = "IssueEditAdapter";
        Log.w(TAG,"Title :"+movie.getTitle());
        Log.w(TAG,"Updated by :"+movie.getUpdated_by_name());
        Log.w(TAG,"Employee ID :"+movie.getUpdated_by_id());


        holder.titleView.setText(movie.getTitle());
        holder.updateView.setText("Updated by  :"+movie.getUpdated_by_name());
        holder.empIdView.setText("Employee ID  :"+movie.getUpdated_by_id());



        if(IssuseEditList.userLevel.equalsIgnoreCase("4")){

            holder.decriptopnView.setText(movie.getDescription());

        }else if(IssuseEditList.userLevel.equalsIgnoreCase("5")){

            holder.decriptopnView.setText(movie.getRemarks());
        }


        String[] ImageString =movie.getPhotos().split(",");
        if (movie.getPhotos().equalsIgnoreCase("")) {
            Log.e("ImageIssueEdit",""+ movie.getPhotos());


        } else {

            if(ImageString.length>=2){

                Log.e("ImageIssueEdit",""+ ApiCall.BASE_URL+"assets/uploads/"+ImageString[0]);

                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/"+ImageString[0])
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);


            }
            else {
                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/"+movie.getPhotos())
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);


            }        }



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
