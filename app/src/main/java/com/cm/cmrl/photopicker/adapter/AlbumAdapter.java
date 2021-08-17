package com.cm.cmrl.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cm.cmrl.R;
import com.cm.cmrl.photopicker.model.ImageModel;
import com.cm.cmrl.photopicker.myinterface.OnAlbum;

import java.io.File;
import java.util.ArrayList;
/**
 * Created by Iddinesh.
 */

public class AlbumAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    ArrayList<ImageModel> data = new ArrayList();
    int layoutResourceId;
    OnAlbum onItem;
    int pHeightItem = 0;
    int pWHIconNext = 0;

    static class RecordHolder {
        ImageView iconNext;
        ImageView imageItem;
        RelativeLayout layoutRoot;
        TextView txtPath;
        TextView txtTitle;

        RecordHolder() {
        }
    }

    public AlbumAdapter(Context context, int layoutResourceId, ArrayList<ImageModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.pHeightItem = getDisplayInfo((AppCompatActivity) context).widthPixels / 6;
        this.pWHIconNext = this.pHeightItem / 4;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.name_album);
            holder.txtPath = (TextView) row.findViewById(R.id.path_album);
            holder.imageItem = (ImageView) row.findViewById(R.id.icon_album);
            holder.iconNext = (ImageView) row.findViewById(R.id.iconNext);
            holder.layoutRoot = (RelativeLayout) row.findViewById(R.id.layoutRoot);
            holder.layoutRoot.getLayoutParams().height = this.pHeightItem;
            holder.imageItem.getLayoutParams().width = this.pHeightItem;
            holder.imageItem.getLayoutParams().height = this.pHeightItem;
            holder.iconNext.getLayoutParams().width = this.pWHIconNext;
            holder.iconNext.getLayoutParams().height = this.pWHIconNext;
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        ImageModel item = (ImageModel) this.data.get(position);
        holder.txtTitle.setText(item.getName());
        holder.txtPath.setText(item.getPathFolder());
        Glide.with(this.context).load(new File(item.getPathFile())).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);

        row.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AlbumAdapter.this.onItem != null) {
                    AlbumAdapter.this.onItem.OnItemAlbumClick(position);
                }
            }
        });
        return row;
    }

    public OnAlbum getOnItem() {
        return this.onItem;
    }

    public void setOnItem(OnAlbum onItem) {
        this.onItem = onItem;
    }

    public static DisplayMetrics getDisplayInfo(AppCompatActivity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
