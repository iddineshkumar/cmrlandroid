package com.cm.cmrl.ups;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.cm.cmrl.R;
import com.cm.cmrl.arraylist.UpsBankUpdateList;
import com.cm.cmrl.sweetalertdialog.SweetAlertDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Iddinesh.
 */

public class UpsBankUpdateTwo  extends Fragment {
    Toolbar toolbar;
    ArrayList<UpsBankUpdateList> monitorUpsListArrayList =new ArrayList<>();
    TextView dateCustomFontTextView;
    LinearLayout signOutLayout;
    LinearLayout sidemenuLayout,dateLayout;
    Button subButton,sync;
    LinearLayout cancel;
    ArrayList<ImageView> imageViewArrayList=new ArrayList<>();
    ArrayList<EditText> editViewArrayList=new ArrayList<>();
    ArrayList<TextView> textViewArrayList=new ArrayList<>();
    LayoutInflater mInflater;
    LinearLayout expandablelinearLayout;
    ScrollView expandableListView;
    boolean skipOnChange = false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.ups_bank_update_two, container,
                false);

        this.mInflater= LayoutInflater.from(getActivity());

        dateCustomFontTextView =  view.findViewById(R.id.date_text);
        sidemenuLayout =  view.findViewById(R.id.back_layout);
        signOutLayout =  view.findViewById(R.id.sign_out);
        dateLayout =  view.findViewById(R.id.date_layout);

        toolbar =  view.findViewById(R.id.toolbar);
        subButton =  view.findViewById(R.id.add_button);
        sync =  view.findViewById(R.id.sync);

        cancel =  view.findViewById(R.id.clear_button);


        sidemenuLayout.setVisibility(View.INVISIBLE);
        signOutLayout.setVisibility(View.INVISIBLE);

//        titleCustomFontTextView.setText("Update");
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy , hh:mm a");
        String formattedDate = df.format(c.getTime());

       dateCustomFontTextView.setText(formattedDate);

        expandableListView = view.findViewById(R.id.productListScrollView);
        expandablelinearLayout= view.findViewById(R.id.expandablelinearLayout);


        subButton.setOnClickListener(view1 -> new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Update confirmation")
                .setConfirmText("Ok")
                .setCancelText("No")
                .setConfirmClickListener(sDialog -> sDialog.dismiss())
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show());

        sync.setOnClickListener(view12 -> new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("CMRL")
                .setContentText("Update confirmation")
                .setConfirmText("Ok")
                .setCancelText("No")
                .setConfirmClickListener(sDialog -> sDialog.dismiss())
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show());


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    UpsBank orderManagement = new UpsBank();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();


                    return true;
                }
            }
            return false;
        });
        prepareAlbums();

        addDynamicView();

        return view;

    }
    public void addDynamicView() {

        Log.e("Dynamiv00", "View Crearte");

        expandablelinearLayout.removeAllViews();
        expandableListView.removeAllViews();

        imageViewArrayList.clear();
        textViewArrayList.clear();

        for (int i =0; i<monitorUpsListArrayList.size();i++){

            View dynamicAdapterView=mInflater.inflate(R.layout.ups_bank_grid,expandablelinearLayout,false);
            final LinearLayout categoryLayout = dynamicAdapterView.findViewById(R.id.categoryLayout);
            TextView batteryName = dynamicAdapterView.findViewById(R.id.battery);
            EditText quantityEditText = dynamicAdapterView.findViewById(R.id.quantity);
            ImageView statusImageView = dynamicAdapterView.findViewById(R.id.image_status);
            categoryLayout.setTag(i);

            imageViewArrayList.add(statusImageView);
            editViewArrayList.add(quantityEditText);
            textViewArrayList.add(batteryName);

            textViewArrayList.get(i).setText(monitorUpsListArrayList.get(i).getBatteryNam());
            editViewArrayList.get(i).setText(monitorUpsListArrayList.get(i).getQuantity());

            imageViewArrayList.get(i).setVisibility(View.INVISIBLE);

            final int finalI = i;
            editViewArrayList.get(i).addTextChangedListener(new TextWatcher() {


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

                        if(!editViewArrayList.get(finalI).getText().toString().equalsIgnoreCase("")){


                            imageViewArrayList.get(finalI).setVisibility(View.VISIBLE);
                            int value = Integer.parseInt(editViewArrayList.get(finalI).getText().toString());

                            if(value>=12){

                                imageViewArrayList.get(finalI).setImageResource(R.mipmap.write);
                            }
                            else {

                                imageViewArrayList.get(finalI).setImageResource(R.mipmap.wrong);
                            }

                        }
                        else {

                            imageViewArrayList.get(finalI).setVisibility(View.INVISIBLE);


                        }


                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        skipOnChange = false;
                    }
                }
            });
            expandablelinearLayout.addView(dynamicAdapterView);

        }
        expandableListView.addView(expandablelinearLayout);

    }

    private void prepareAlbums() {

        UpsBankUpdateList a = new UpsBankUpdateList("Battery 1","","0");
        monitorUpsListArrayList.add(a);

        a = new UpsBankUpdateList("Battery 2","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 3","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 4","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 5","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 6","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 7","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 8","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 9","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 10","","0");
        monitorUpsListArrayList.add(a);
        a = new UpsBankUpdateList("Battery 11","","0");
        monitorUpsListArrayList.add(a);

    }

}
