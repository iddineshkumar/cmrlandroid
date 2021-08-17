package com.cm.cmrl.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.cmrl.arraylist.UpsBankUpdateList;
import com.cm.cmrl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class UpsBankAdapter extends RecyclerView.Adapter<UpsBankAdapter.MyViewHolder>  {

    private List<UpsBankUpdateList> continentList;

    private ArrayList<ImageView> imageViewArrayList=new ArrayList<>();
    private ArrayList<EditText> textViewArrayList=new ArrayList<>();
    private boolean skipOnChange = false;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView batteryName;
        EditText quantityEditText;
        ImageView statusImageView;

        public MyViewHolder(View view) {
            super(view);

            batteryName =  view.findViewById(R.id.battery);
            quantityEditText =  view.findViewById(R.id.quantity);
            statusImageView = view.findViewById(R.id.image_status);


            for (int i =0; i<continentList.size();i++){

                textViewArrayList.add(quantityEditText);
                imageViewArrayList.add(statusImageView);

            }

        }
    }

    public UpsBankAdapter(List<UpsBankUpdateList> moviesList) {

        this.continentList = moviesList;

    }

    @NonNull
    @Override
    public UpsBankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ups_bank_grid, parent, false);

        return new UpsBankAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UpsBankAdapter.MyViewHolder holder, final int position) {

        final UpsBankUpdateList movie = continentList.get(position);

        Log.e("Battery name",""+continentList.get(position).getBatteryNam());
        holder.batteryName.setText(movie.getBatteryNam());


        holder.statusImageView.setVisibility(View.INVISIBLE);

        holder.quantityEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (skipOnChange)
                    return;

                skipOnChange = true;
                try
                {

                    if(!holder.quantityEditText.getText().toString().equalsIgnoreCase("")){

                        continentList.get(position).setStatus("1");
                        holder.statusImageView.setVisibility(View.VISIBLE);
                        int value = Integer.parseInt(holder.quantityEditText.getText().toString());

                        if(value>=12){

                            holder.statusImageView.setImageResource(R.mipmap.write);
                        }
                        else {

                            holder.statusImageView.setImageResource(R.mipmap.wrong);
                        }

                    }
                    else {

                        continentList.get(position).setStatus("0");


                    }


                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    skipOnChange = false;
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return continentList.size();
    }


}




