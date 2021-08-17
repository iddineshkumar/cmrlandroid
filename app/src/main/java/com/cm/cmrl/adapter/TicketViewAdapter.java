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
import com.cm.cmrl.arraylist.DeapartmentStatusList;
import com.cm.cmrl.R;
import com.cm.cmrl.api.ApiCall;
import com.cm.cmrl.view.DepartmentStatusDetail;
import com.cm.cmrl.view.DepartmentStatusListClass;
import com.cm.cmrl.view.IssuseEditList;
import com.cm.cmrl.view.StationEditLayout;
import com.cm.cmrl.view.ViewTickets;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Iddinesh.
 */

public class TicketViewAdapter extends RecyclerView.Adapter<TicketViewAdapter.MyViewHolder> {

    private List<DeapartmentStatusList> moviesList;

    private FragmentActivity context;

    private String title;

    private String TAG ="TicketViewAdapter";


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView createNameView, createIdView, dayView, monthTextView, descriptionView, titleView;

        ImageView circularImageView;

        ImageView editImageView, viewImageView;

        LinearLayout editLayout, viewLayout, wholeLayout;

        public MyViewHolder(View view) {

            super(view);
            createNameView =  view.findViewById(R.id.created_name);
            createIdView =  view.findViewById(R.id.created_by_id);
            dayView =  view.findViewById(R.id.day_text);
            descriptionView = view.findViewById(R.id.description);
            monthTextView =  view.findViewById(R.id.last_visit);
            titleView =  view.findViewById(R.id.title_name);

            circularImageView =  view.findViewById(R.id.profile_image);
            editImageView =  view.findViewById(R.id.edit_image);
            viewImageView =  view.findViewById(R.id.view_image);

            editLayout = view.findViewById(R.id.edit_layout);
            editLayout.setVisibility(View.GONE);
            viewLayout =  view.findViewById(R.id.view_layout);
            wholeLayout =  view.findViewById(R.id.whole_layout);

        }
    }


    public TicketViewAdapter(FragmentActivity context, List<DeapartmentStatusList> moviesList, String title) {

        this.moviesList = moviesList;
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public TicketViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tickets_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TicketViewAdapter.MyViewHolder holder, int position) {

        final DeapartmentStatusList movie = moviesList.get(position);

        SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormattt = new SimpleDateFormat("yyyy-MM-dd");

        String[] updateAt = movie.getUpdated_at().split(" ");
        try {

            Date date = dateFormattt.parse(updateAt[0]);
            if (date != null) {
                holder.monthTextView.setText("Date: " + monthSimpleDateFormat.format(date));
            }

        } catch (ParseException e) {

            e.printStackTrace();
        }


        holder.descriptionView.setText(movie.getDescription());

    try{
        String ticketStatus = DepartmentStatusListClass.ticketStatus;
        String ticket = DepartmentStatusListClass.ticket;
        Log.w(TAG,"Ticket Status--->"+ticketStatus +"ticket :"+ticket+"userLevel "+ViewTickets.userLevel);
      /*  if (ViewTickets.userLevel.equalsIgnoreCase("4")) {
            if (ticket.equals("Inprogress") || ticket.equalsIgnoreCase("Inprogress") || ticket.equalsIgnoreCase("Pending Issues") || ticket.equalsIgnoreCase("Completed Issues") || ticket.equalsIgnoreCase("Close Issues")) {
                Log.w(TAG, "Ticket  IF--->" + ticket);
                holder.editLayout.setVisibility(View.GONE);
                holder.viewLayout.setVisibility(View.GONE);

            } else {
                holder.editLayout.setVisibility(View.VISIBLE);
                holder.viewLayout.setVisibility(View.VISIBLE);

            }
        }*/

            if (ViewTickets.userLevel.equalsIgnoreCase("4")) {
                if(ticket.equalsIgnoreCase("Open Issues")){
                    Log.w(TAG, "Ticket  IF Condition--->" + ticket);
                    holder.editLayout.setVisibility(View.VISIBLE);
                    holder.viewLayout.setVisibility(View.VISIBLE);
                }else{
                    Log.w(TAG, "Ticket Else condition--->" + ticket);
                    holder.editLayout.setVisibility(View.GONE);
                    holder.viewLayout.setVisibility(View.GONE);
                }

            }




                    }catch (Exception e){
                            e.printStackTrace();
                         }



        if (movie.getTicket_status().equalsIgnoreCase("1")) {
            holder.titleView.setText("Open");
            holder.createNameView.setText("Created by: " + movie.getUpdated_by_name());
            holder.createIdView.setText("Emp id: " + movie.getUpdated_by());

        } else if (movie.getTicket_status().equalsIgnoreCase("2")) {
            holder.titleView.setText("In progress");
            holder.createNameView.setText("Updated by: " + movie.getUpdated_by_name());
            holder.createIdView.setText("Emp id: " + movie.getUpdated_by());

        } else if (movie.getTicket_status().equalsIgnoreCase("3")) {

            holder.titleView.setText("Pending");
            holder.createNameView.setText("Updated by: " + movie.getUpdated_by_name());
            holder.createIdView.setText("Emp id: " + movie.getUpdated_by());

        } else if (movie.getTicket_status().equalsIgnoreCase("4")) {

            holder.titleView.setText("Completed");
            holder.createNameView.setText("Updated by DEPT name: " + movie.getUpdated_by_name());
            holder.createIdView.setText("Emp id: " + movie.getUpdated_by());


        } else if (movie.getTicket_status().equalsIgnoreCase("5")) {

            holder.titleView.setText("Closed");
            holder.createNameView.setText("Closed by: " + movie.getUpdated_by_name());
            holder.createIdView.setText("Emp id: " + movie.getUpdated_by());


        }


        holder.wholeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, DepartmentStatusDetail.class);
            intent.putExtra("image", movie.getPhotos());
            intent.putExtra("name", movie.getUpdated_by_name());
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("date", movie.getUpdated_at());
            intent.putExtra("status", movie.getTicket_status());
            intent.putExtra("description", movie.getDescription());
            intent.putExtra("createdby", movie.getUpdated_by());
            intent.putExtra("locationName", movie.getLocation());
            intent.putExtra("priorityName", movie.getPriority_name());
            intent.putExtra("ticketId", movie.getTicket_id());
            intent.putExtra("stationLocation", ViewTickets.stationLocation);
            intent.putExtra("code", ViewTickets.departmentCode);
            intent.putExtra("screenStatus", "1");

            Log.w(TAG,"stationid"+movie.getStation_id()+" "+"faulttitle"+movie.getFault_title());
            intent.putExtra("stationid",movie.getStation_id());
            intent.putExtra("faulttitle",movie.getFault_title());
            intent.putExtra("faulttype",movie.getFault_type());
            intent.putExtra("trainid",movie.getTrainid());
            intent.putExtra("train_id",movie.getTrain_id());
            intent.putExtra("report_datetime",movie.getReport_datetime());

            context.startActivity(intent);
            context.overridePendingTransition(R.anim.new_right, R.anim.new_left);

        });

        if (ViewTickets.userLevel.equalsIgnoreCase("5")) {
            Log.w(TAG,"UserLevel--->"+ViewTickets.userLevel);
            if (movie.getTicket_status().equalsIgnoreCase("1") || movie.getTicket_status().equalsIgnoreCase("2") || movie.getTicket_status().equalsIgnoreCase("3") || movie.getTicket_status().equalsIgnoreCase("4")||
                    movie.getTicket_status().equalsIgnoreCase("5")) {
                holder.editLayout.setVisibility(View.GONE);
                holder.viewLayout.setVisibility(View.GONE);
            } else {
                holder.editLayout.setVisibility(View.VISIBLE);
                holder.viewLayout.setVisibility(View.VISIBLE);
            }
        }  else if (ViewTickets.userLevel.equalsIgnoreCase("6")) {
            holder.editLayout.setVisibility(View.GONE);
            holder.viewLayout.setVisibility(View.GONE);
        }

        holder.editLayout.setOnClickListener(view -> {

            Log.e("departmentIdfgfghdfh", "" + movie.getDepartment_id());
            Intent intent = new Intent(context, StationEditLayout.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("departmenttt", movie.getDepartment_id());
            intent.putExtra("priority", movie.getPriority_name().replace("..", ""));
            intent.putExtra("location", movie.getLocation());
            intent.putExtra("description", movie.getDescription());
            intent.putExtra("image", movie.getPhotos().replace("..", ""));
            intent.putExtra("ticketId", movie.getTicket_id());
            intent.putExtra("code", ViewTickets.departmentCode);
            intent.putExtra("ticketStatus", movie.getTicket_status());
            intent.putExtra("Tickethistory_id", movie.getTickethistory_id());
            intent.putExtra("priorityId", movie.getPriority_id());
            context.startActivity(intent);

        });

        holder.viewLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, IssuseEditList.class);
            intent.putExtra("Tickethistory_id", movie.getTickethistory_id());
            intent.putExtra("title", title);
            intent.putExtra("updatedby", movie.getUpdated_by_name());
            Log.w(TAG,"updatedby-->"+movie.getUpdated_by_name());
            context.startActivity(intent);
        });


        String[] ImageString = movie.getPhotos().split(",");
        if (movie.getPhotos().equalsIgnoreCase("")) {
            Log.i("",movie.getPhotos());
        } else {

            if (ImageString.length >= 2) {

                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + ImageString[0])
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);

            } else {

                Glide.with(context)
                        .load(ApiCall.BASE_URL+"assets/uploads/" + movie.getPhotos())
                        .placeholder(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.circularImageView);

            }

        }
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}


