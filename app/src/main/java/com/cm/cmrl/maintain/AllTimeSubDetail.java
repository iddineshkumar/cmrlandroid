package com.cm.cmrl.maintain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.cm.cmrl.R;
import com.cm.cmrl.materialeditext.MaterialEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by Iddinesh.
 */

public class AllTimeSubDetail extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.all_sub_detail, container,
                false);


        LinearLayout ll = view.findViewById(R.id.linearLayout2);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_submit);

        //add checkboxes
        for(int i = 0; i < 5; i++) {
            MaterialEditText cb = new MaterialEditText(getActivity());
            cb.setHint("Enter value");
            cb.setSingleLine();
            cb.setTextSize(14);
            cb.setFloatingLabelTextSize(14);
            cb.setFloatingLabelText("Enter value");
            cb.setBaseColor(Color.parseColor("#80000000"));
            cb.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
            cb.setFloatingLabelAnimating(true);
            cb.setPrimaryColor(Color.parseColor("#246FC3"));
           // cb.setText("Dynamic Checkbox " + i);
            cb.setId(i+10);
            ll.addView(cb);
        }

        floatingActionButton.setOnClickListener(view1 -> {


            Intent intent = new Intent(getActivity(),ConfirmTwo.class);
            startActivity(intent);
        });


        return view;
    }
    }
