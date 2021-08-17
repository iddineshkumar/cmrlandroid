package com.cm.cmrl.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.IssueList;

import java.util.ArrayList;

/**
 * Created by Iddinesh.
 */

public class IssueTwoAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private ArrayList<IssueList> text;

    private LayoutInflater inflater;

    public IssueTwoAdapter(AppCompatActivity fragActivity, ArrayList<IssueList> slideListArrayList) {

        this.activity = fragActivity;
        this.text = slideListArrayList;
        inflater = LayoutInflater.from(fragActivity);


    }

    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int i) {
        return i;

    }

    @Override
    public long getItemId(int i) {
        return i;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;
        grid = inflater.inflate(R.layout.issue_grid_image, viewGroup, false);

        String[] ImageString = text.get(i).getImageUrl().split(",");
        ImageView circularImageView =  grid.findViewById(R.id.appointment_user_image);
        if (text.get(i).getImageUrl().equalsIgnoreCase("")) {
            Log.i("","");

        } else {

            if (ImageString.length >=2) {

                Glide.with(activity)
                        .load(ApiCall.BASE_URL+"assets/uploads/"+ImageString[0])
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(circularImageView);

            } else {


                Glide.with(activity)
                        .load(ApiCall.BASE_URL+"assets/uploads/"+text.get(i).getImageUrl())
                        .placeholder(R.drawable.no_image)
                        .into(circularImageView);

            }
        }


        return grid;
    }

}

