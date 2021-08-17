package com.cm.cmrl.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.cm.cmrl.arraylist.AllTimeList;
import com.cm.cmrl.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class CheckerTimeAdapter extends  RecyclerView.Adapter<CheckerTimeAdapter.MyViewHolder> {

    private List<AllTimeList> continentList;
    private FragmentActivity fragmentActivity;
    private EditText remarkEditText;
    private Dialog alertDialog;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

         TextView timeCustomFontTextView;
         TextView completedCountCustomFontTextView;
         LinearLayout verifiedLayout,remarksLayout;


        public MyViewHolder(View view) {
            super(view);

            timeCustomFontTextView =  view.findViewById(R.id.time_text);
            completedCountCustomFontTextView =  view.findViewById(R.id.completed_count);

            verifiedLayout =  view.findViewById(R.id.verify);
            remarksLayout =  view.findViewById(R.id.remarks);



        }
    }

    public CheckerTimeAdapter(FragmentActivity fragmentActivity, List<AllTimeList> moviesList) {
        this.fragmentActivity = fragmentActivity;
        this.continentList = moviesList;

    }

    @NonNull
    @Override
    public CheckerTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checker_time_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CheckerTimeAdapter.MyViewHolder holder, int position) {

        final AllTimeList movie = continentList.get(position);
        holder.timeCustomFontTextView.setText("Time : "+movie.getEquipmentTime());
        holder.completedCountCustomFontTextView.setText(movie.getCompletedCount());

        holder.remarksLayout.setOnClickListener(view -> popupView());


    }
    @Override
    public int getItemCount() {
        return continentList.size();
    }
    private void popupView(){

        // AlterDialog
        alertDialog = new Dialog(fragmentActivity, R.style.NewDialog);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.setContentView(R.layout.remarks_popup);

        remarkEditText = alertDialog.findViewById(R.id.remarks);

        LinearLayout closeLinearLayout = alertDialog.findViewById(R.id.add_close_layout);
        FloatingActionButton floatingActionButton = alertDialog.findViewById(R.id.add_consumer_fab);



        closeLinearLayout.setOnClickListener(view -> alertDialog.dismiss());

        floatingActionButton.setOnClickListener(view -> {

            if(remarkEditText.getText().toString().equalsIgnoreCase("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);

                //Setting message manually and performing action on button click
                builder.setMessage("Please enter your remarks")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialog, id) -> {


                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
            else {


                alertDialog.dismiss();

            }
        });

        alertDialog.show();
    }
}



