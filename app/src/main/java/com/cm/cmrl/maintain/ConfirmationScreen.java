package com.cm.cmrl.maintain;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cm.cmrl.R;
import com.cm.cmrl.arraylist.AllTabList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

/**
 * Created by Iddinesh.
 */

public class ConfirmationScreen extends AppCompatActivity {

    ArrayList<AllTabList> searchListArrayList;
    ViewPager viewPager;
    LinearLayout bottomLayout;
    EditText remarkEditText;
    LinearLayout closeLinearLayout;
    FloatingActionButton floatingActionButton;
    Dialog alertDialog;
    LinearLayout cancelLayout;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_screen);


        viewPager = findViewById(R.id.viewpager);

        bottomLayout = findViewById(R.id.bottom_layout);
        cancelLayout = findViewById(R.id.clear_button);
        submitButton = findViewById(R.id.add_button);

        bottomLayout.setVisibility(View.INVISIBLE);

        final CustomPagerAdapter splashScreenViewPagerAdapter = new CustomPagerAdapter(ConfirmationScreen.this, searchListArrayList);
        viewPager.setAdapter(splashScreenViewPagerAdapter);

        viewPager.setPageMargin(-50);
        viewPager.setHorizontalFadingEdgeEnabled(true);
        viewPager.setFadingEdgeLength(30);
        transformPage(viewPager);


        cancelLayout.setOnClickListener(view -> popupView());


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                boolean isInLastPage = viewPager.getCurrentItem() == splashScreenViewPagerAdapter.getCount() - 1;

                if(isInLastPage){

                    bottomLayout.setVisibility(View.VISIBLE);
                }

                else {

                    bottomLayout.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public  void popupView(){

        // AlterDialog
        alertDialog = new Dialog(ConfirmationScreen.this, R.style.NewDialog);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.setContentView(R.layout.remarks_popup);

        remarkEditText = alertDialog.findViewById(R.id.remarks);

        closeLinearLayout = alertDialog.findViewById(R.id.add_close_layout);
        floatingActionButton = alertDialog.findViewById(R.id.add_consumer_fab);



        closeLinearLayout.setOnClickListener(view -> alertDialog.dismiss());

        floatingActionButton.setOnClickListener(view -> {

            if(remarkEditText.getText().toString().equalsIgnoreCase("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationScreen.this);
                //Uncomment the below code to Set the message and title from the strings.xml file
                //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

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
    public static class CustomPagerAdapter extends PagerAdapter
    {

        ArrayList<AllTabList> searchListArrayList;
        AppCompatActivity mContext;
        LayoutInflater mLayoutInflater;


        CustomPagerAdapter(ConfirmationScreen context, ArrayList<AllTabList> searchListArrayList)
        {
            mContext = context;
            this.searchListArrayList=searchListArrayList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount()
        {
            return 10;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int i)
        {

            View itemView = mLayoutInflater.inflate(R.layout.confirmation_view_layout, container, false);

            LinearLayout ll = itemView.findViewById(R.id.linearLayout2);


            //add checkboxes
            for(int j = 0; j < 5; j++) {
                TextView cb = new TextView(mContext);
                cb.setHint("Enter value");
                cb.setSingleLine();
                cb.setTextSize(18);
                cb.setPadding(0,5,0,5);
                cb.setGravity(Gravity.CENTER);
                // cb.setText("Dynamic Checkbox " + i);
                cb.setId(i+5);
                ll.addView(cb);
            }


            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object)
        {
            container.removeView((RelativeLayout) object);
        }


        public float getPageWidth(int Position)
        {
            return 0.99f;
        }
    }
    public void transformPage(View view) {

        view.setLayerType(View.LAYER_TYPE_NONE, null);
    }
    }
