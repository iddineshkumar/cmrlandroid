package com.cm.cmrl.maintain;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.cm.cmrl.arraylist.CardItem;
import com.cm.cmrl.R;
import com.cm.cmrl.ticketview.CardAdapter;
import com.cm.cmrl.ticketview.ShadowTransformer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class ConfirmTwo  extends AppCompatActivity {

    ViewPager viewPager;

    LinearLayout bottomLayout;

    EditText remarkEditText;
    LinearLayout closeLinearLayout;
    FloatingActionButton floatingActionButton;
    Dialog alertDialog;

    LinearLayout cancelLayout,leftLayout,rightLayout;

    Button submitButton;
    private CardPagerAdapter mCardAdapter;

    public ConfirmTwo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_screen);

        viewPager = findViewById(R.id.viewpager);
        bottomLayout = findViewById(R.id.bottom_layout);
        leftLayout = findViewById(R.id.left_layout);
        rightLayout = findViewById(R.id.right_layout);
        cancelLayout = findViewById(R.id.clear_button);
        submitButton = findViewById(R.id.add_button);
        bottomLayout.setVisibility(View.INVISIBLE);


        mCardAdapter = new CardPagerAdapter();
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(viewPager, mCardAdapter);

        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_5, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_6, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_7, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_8, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_9, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_10, R.string.text_1));
        viewPager.setPageTransformer(false, mCardShadowTransformer);
        viewPager.setAdapter(mCardAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(dpToPx(-25));
        leftLayout.setVisibility(View.INVISIBLE);
        rightLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(view -> {
            boolean isInFirstPage = viewPager.getCurrentItem() == 0;
            if( !isInFirstPage){

                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }

        });
        rightLayout.setOnClickListener(view -> {


            boolean isInLastPage = viewPager.getCurrentItem() == mCardAdapter.getCount() - 1;
           if( !isInLastPage){

               viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
           }


        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                boolean isInLastPage = viewPager.getCurrentItem() == mCardAdapter.getCount() - 1;
                boolean isInFirstPage = viewPager.getCurrentItem() == 0;


                if (isInLastPage) {

                    leftLayout.setVisibility(View.VISIBLE);
                    rightLayout.setVisibility(View.INVISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    Log.e("last true",""+"last true");

                } else if (isInFirstPage) {

                    Log.e("Left true",""+"Left true");
                    leftLayout.setVisibility(View.INVISIBLE);
                    rightLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.INVISIBLE);

                } else {

                    leftLayout.setVisibility(View.VISIBLE);
                    rightLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.INVISIBLE);
                    Log.e("both true",""+"both true");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        cancelLayout.setOnClickListener(view -> popupView());

    }


    public  void popupView(){

        // AlterDialog
        alertDialog = new Dialog(ConfirmTwo.this, R.style.NewDialog);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.setContentView(R.layout.remarks_popup);

        remarkEditText = alertDialog.findViewById(R.id.remarks);

        closeLinearLayout = alertDialog.findViewById(R.id.add_close_layout);
        floatingActionButton = alertDialog.findViewById(R.id.add_consumer_fab);



        closeLinearLayout.setOnClickListener(view -> alertDialog.dismiss());

        floatingActionButton.setOnClickListener(view -> {

            if(remarkEditText.getText().toString().equalsIgnoreCase("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmTwo.this);
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
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ConfirmTwo.this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

        private List<CardView> mViews;
        private List<CardItem> mData;
        private float mBaseElevation;

        CardPagerAdapter() {
            mData = new ArrayList<>();
            mViews = new ArrayList<>();
        }

        void addCardItem(CardItem item) {
            mViews.add(null);
            mData.add(item);
        }

        public float getBaseElevation() {
            return mBaseElevation;
        }

        @Override
        public CardView getCardViewAt(int position) {
            return mViews.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.adapter, container, false);
            container.addView(view);
            bind(view);
            CardView cardView = view.findViewById(R.id.cardView);

            if (mBaseElevation == 0) {
                mBaseElevation = cardView.getCardElevation();
            }

            cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            mViews.set(position, cardView);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            mViews.set(position, null);
        }

        private void bind(View view) {
            LinearLayout ll = view.findViewById(R.id.linearLayout2);


            //add checkboxes
            for(int j = 0; j < 5; j++) {
                TextView cb = new TextView(ConfirmTwo.this);
                cb.setHint("Enter value");
                cb.setSingleLine();
                cb.setTextSize(18);
                cb.setPadding(0,5,0,5);
                cb.setGravity(Gravity.CENTER);
                // cb.setText("Dynamic Checkbox " + i);
                cb.setId(j+5);
                ll.addView(cb);
            }


        }
        public float getPageWidth(int Position)
        {
            return 0.99f;
        }

    }

}

