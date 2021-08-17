package com.cm.cmrl.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.arraylist.DeapartmentStatusList;
import com.cm.cmrl.R;
import com.cm.cmrl.utils.ClockDrableTwo;
import com.cm.cmrl.view.DepartmentStatusListClass;
import com.cm.cmrl.view.UpdateStatusActivity;
import com.cm.cmrl.view.ViewTickets;

import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Iddinesh.
 */

public class DepartmentStatusAdapter extends RecyclerView.Adapter<DepartmentStatusAdapter.MyViewHolder> {

    private String issues, list;
    private List<DeapartmentStatusList> moviesList;
    private FragmentActivity context;
    private final LocalDateTime now = LocalDateTime.now().withTime(0, 0, 0, 0);
    private String updateAt;
    private String outTime;

    private String TAG = "DepartmentStatusAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView, timeTextView, textTicketID,nameTextView, reasonTextView, amTextView, monthTextView, yearTextview;

        LinearLayout rscheduleLinearLayout, wholeLayout;

        ImageView circularImageView;

        ImageView clockImageView;

        public MyViewHolder(View view) {
            super(view);
            dateTextView =  view.findViewById(R.id.date_one_text);
            timeTextView =  view.findViewById(R.id.hour_text);
            textTicketID = view.findViewById(R.id.text_ticketid);
            nameTextView =  view.findViewById(R.id.name_text);
            reasonTextView =  view.findViewById(R.id.complaint_text);
            amTextView =  view.findViewById(R.id.min_text);
            monthTextView =  view.findViewById(R.id.month_text);
            yearTextview =  view.findViewById(R.id.year_text);
            rscheduleLinearLayout =  view.findViewById(R.id.reschedule_layout);
            wholeLayout =  view.findViewById(R.id.department_status);
            circularImageView =  view.findViewById(R.id.profile_image);
            clockImageView =  view.findViewById(R.id.image);
        }
    }


    public DepartmentStatusAdapter(FragmentActivity context, List<DeapartmentStatusList> moviesList, String issues, String list) {
        this.issues = issues;
        this.moviesList = moviesList;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DepartmentStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_status_list_grid, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DepartmentStatusAdapter.MyViewHolder holder, int position) {

        final DeapartmentStatusList movie = moviesList.get(position);
        SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat("dd", /*Locale.getDefault()*/Locale.ENGLISH);
        SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("MM", /*Locale.getDefault()*/Locale.ENGLISH);
        SimpleDateFormat yearSimpleDateFormat = new SimpleDateFormat("yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

        String outputPattern = "yyyy-MM-dd KK:mm a";
        String date1 = movie.getUpdated_at();
        Log.w(TAG,date1);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        Date d;
        try
        {
            d = input.parse(date1);
            Log.w(TAG,"DATE--->"+d);
            String formatted = output.format(Objects.requireNonNull(d));
            Log.w(TAG,"FormattedDate-->"+formatted);


            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat(inputPattern);
            Date date = spf.parse(date1);
            if (date != null) {
                updateAt = outputFormat.format(date);
            }
            String[] time = updateAt.split(" ");
            outTime = time[1];
            if (date != null) {
                holder.dateTextView.setText(daySimpleDateFormat.format(date));
            }
            if (date != null) {
                holder.monthTextView.setText(monthSimpleDateFormat.format(date));
            }
            if (date != null) {
                holder.yearTextview.setText(yearSimpleDateFormat.format(date));
            }
            holder.timeTextView.setText(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }





        holder.amTextView.setVisibility(View.GONE);


     /*   if (timeValue[1].equalsIgnoreCase("PM")) {

            holder.amTextView.setText("pm");

        } else if (timeValue[1].equalsIgnoreCase("AM")) {

            holder.amTextView.setText("am");
        }
*/
        ClockDrableTwo clockDrawable = new ClockDrableTwo(context.getResources());

        String mhourr;
        Log.w(TAG,"outTime"+outTime);

        if (outTime != null && !outTime.isEmpty() && !outTime.equals("null")){
            String[] timeValue = outTime.split(":");
            if (Integer.parseInt(timeValue[0]) < 10){
                mhourr = timeValue[0].replace("0", "");

            } else{
                mhourr = String.valueOf(timeValue[0]);
                clockDrawable.setAnimateDays(false);

            }
            if (mhourr.equalsIgnoreCase("")) {

                holder.clockImageView.setImageResource(R.drawable.time);

            }
            else {

                LocalDateTime current = now.plusHours(Integer.parseInt(mhourr)).plusMinutes(Integer.parseInt(timeValue[1]));
                clockDrawable.start(current);

                holder.clockImageView.setImageDrawable(clockDrawable);
            }
        }









        holder.textTicketID.setText("Ticket ID :"+" "+movie.getTicket_id());
        holder.nameTextView.setText(movie.getTitle());
        holder.reasonTextView.setText(movie.getDescription());
        // holder.timeTextView.setText(outTime + " " + time[2]);

        String[] ImageString = movie.getPhotos().split(",");
        if (movie.getPhotos().equalsIgnoreCase("")) {

            Log.e("Image", movie.getPhotos());

        } else {

            if (ImageString.length >= 2) {

                Log.w(TAG,"Image"+ "" + ApiCall.BASE_URL+"assets/uploads/" + ImageString[0]);

                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + ImageString[0])
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);


            } else {
                Log.w(TAG,"Else Image"+ "" + ApiCall.BASE_URL+"assets/uploads/" + movie.getPhotos());

                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + movie.getPhotos())
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);


            }
        }


        Log.e("userLevel", "" + DepartmentStatusListClass.userLevel);

        if (DepartmentStatusListClass.userLevel.equalsIgnoreCase("4")) {


            if (issues.equalsIgnoreCase("Completed")) {
                holder.rscheduleLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.rscheduleLinearLayout.setVisibility(View.GONE);
            }


        } else if (DepartmentStatusListClass.userLevel.equalsIgnoreCase("5")) {


            if (issues.equalsIgnoreCase("Completed")) {
                holder.rscheduleLinearLayout.setVisibility(View.GONE);
            } else if (issues.equalsIgnoreCase("Closed")) {
                holder.rscheduleLinearLayout.setVisibility(View.GONE);
            } else {
                holder.rscheduleLinearLayout.setVisibility(View.VISIBLE);
            }


        } else if (DepartmentStatusListClass.userLevel.equalsIgnoreCase("6")) {
            holder.rscheduleLinearLayout.setVisibility(View.GONE);
        }

        holder.wholeLayout.setOnClickListener(view -> {

            Log.w(TAG,"ticket_status"+DepartmentStatusListClass.ticketStatus);
            Log.w(TAG,"ticket"+DepartmentStatusListClass.ticket);

            Intent intent = new Intent(context, ViewTickets.class);
            intent.putExtra("ticketId", movie.getTicket_id());
            intent.putExtra("departmentCode", DepartmentStatusListClass.departmentCode);
            intent.putExtra("stationLocation", DepartmentStatusListClass.locName);
            intent.putExtra("ticket_status", DepartmentStatusListClass.ticketStatus);
            intent.putExtra("ticket", DepartmentStatusListClass.ticket);
            intent.putExtra("title", list + " - " + movie.getTitle());

            context.startActivity(intent);
            context.overridePendingTransition(R.anim.new_right, R.anim.new_left);
        });
        holder.rscheduleLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateStatusActivity.class);
            intent.putExtra("ticketId", movie.getTicket_id());
            intent.putExtra("departmentCode", DepartmentStatusListClass.departmentCode);
            intent.putExtra("stationLocation", DepartmentStatusListClass.locName);
            intent.putExtra("ticketStatus", movie.getTicket_status());
            intent.putExtra("issues", issues);
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.new_right, R.anim.new_left);


        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
