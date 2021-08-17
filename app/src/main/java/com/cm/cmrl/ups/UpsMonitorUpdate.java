package com.cm.cmrl.ups;

import android.os.Bundle;




import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.cm.cmrl.R;
import com.cm.cmrl.helper.DatabaseHandler;
import com.cm.cmrl.materialeditext.MaterialEditText;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;


/**
 * Created by Iddinesh.
 */

public class UpsMonitorUpdate extends Fragment {
    Button subButton,sync;
    LinearLayout cancel;
    String title="";
    DatabaseHandler db;
    MaterialEditText rcMaterialEditText,rybMaterialEditText,kvaMaterialEditText,ampsMaterialEditText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.ups_monitor_update, container,
                false);

        db = new DatabaseHandler(getActivity());

        title =  getArguments().getString("name");

        rcMaterialEditText = view.findViewById(R.id.dc) ;
        rybMaterialEditText = view.findViewById(R.id.ryb) ;
        kvaMaterialEditText = view.findViewById(R.id.kva) ;
        ampsMaterialEditText = view.findViewById(R.id.amps) ;


        subButton = view.findViewById(R.id.add_button);
        sync =  view.findViewById(R.id.sync);

        cancel =  view.findViewById(R.id.clear_button);

        rcMaterialEditText.setOnTouchListener((view1, motionEvent) -> {
            rcMaterialEditText.setFocusableInTouchMode(true);
            rybMaterialEditText.setFocusableInTouchMode(true);
            kvaMaterialEditText.setFocusableInTouchMode(true);
            ampsMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });
        rybMaterialEditText.setOnTouchListener((view12, motionEvent) -> {

            rcMaterialEditText.setFocusableInTouchMode(true);
            rybMaterialEditText.setFocusableInTouchMode(true);
            kvaMaterialEditText.setFocusableInTouchMode(true);
            ampsMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });  kvaMaterialEditText.setOnTouchListener((view13, motionEvent) -> {

            rcMaterialEditText.setFocusableInTouchMode(true);
            rybMaterialEditText.setFocusableInTouchMode(true);
            kvaMaterialEditText.setFocusableInTouchMode(true);
            ampsMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });  ampsMaterialEditText.setOnTouchListener((view14, motionEvent) -> {

            rcMaterialEditText.setFocusableInTouchMode(true);
            rybMaterialEditText.setFocusableInTouchMode(true);
            kvaMaterialEditText.setFocusableInTouchMode(true);
            ampsMaterialEditText.setFocusableInTouchMode(true);

            return false;
        });
        subButton.setOnClickListener(view15 -> {

            if(rcMaterialEditText.getText().toString().equalsIgnoreCase("")){


                rcMaterialEditText.setError("Enter Reading Dc voltage");

            }
            else if(rybMaterialEditText.getText().toString().equalsIgnoreCase("")){

                rybMaterialEditText.setError("Enter RYB");

            }else if(kvaMaterialEditText.getText().toString().equalsIgnoreCase("")){

                kvaMaterialEditText.setError("Enter KVA");
            }
            else if(ampsMaterialEditText.getText().toString().equalsIgnoreCase("")){

                ampsMaterialEditText.setError("Enter AMPS");
            }
            else {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("CMRL")
                        .setContentText("Update confirmation")
                        .setConfirmText("Ok")
                        .setCancelText("No")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("CMRL")
                                    .setContentText("Updated successfully")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(sDialog1 -> sDialog1.dismiss())

                                    .show();

                        })
                        .setCancelClickListener(sDialog -> sDialog.cancel())
                        .show();

            }




        });

        sync.setOnClickListener(view16 -> new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Update confirmation")
                .setConfirmText("Ok")
                .setCancelText("No")
                .setConfirmClickListener(sDialog -> {

                    sDialog.dismiss();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("CMRL")
                            .setContentText("Updated successfully")


                            .setConfirmText("Ok")
                            .setConfirmClickListener(sDialog12 -> sDialog12.dismiss())

                            .show();

                })
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show());



        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Bundle bundle =new Bundle();
                    bundle.putString("name",title);
                    UpsTimeFragment orderManagement = new UpsTimeFragment();
                    orderManagement.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();


                    return true;
                }
            }
            return false;
        });
        return view;

    }



}
