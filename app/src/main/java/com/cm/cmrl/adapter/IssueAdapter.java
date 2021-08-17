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
import com.cm.cmrl.R;
import com.cm.cmrl.arraylist.IssueList;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Iddinesh.
 */

public class IssueAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private ArrayList<IssueList> text;

    private LayoutInflater inflater;

    public IssueAdapter(AppCompatActivity fragActivity, ArrayList<IssueList> slideListArrayList) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View grid;
        grid = inflater.inflate(R.layout.issue_grid_list, viewGroup, false);
        CircleImageView circularImageView = grid.findViewById(R.id.appointment_user_image);
        ImageView ivDelete = grid.findViewById(R.id.ivdelete);
        ivDelete.setOnClickListener(view1 -> {
            text.remove(i);
            IssueAdapter.this.notifyDataSetChanged();

        });

        Log.e("IssuseAdapterUrl", "" + text.get(i).getImageUrl());
        if (text.get(i).getImageUrl().equalsIgnoreCase("")) {
            Log.e("IssuseAdapterUrl", "" + text.get(i).getImageUrl());

        } else {

            Glide.with(activity)
                    .load(text.get(i).getImageUrl())
                    .into(circularImageView);

        }


        return grid;
    }

}

