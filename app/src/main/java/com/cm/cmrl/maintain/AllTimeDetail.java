package com.cm.cmrl.maintain;

import android.graphics.Color;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cm.cmrl.arraylist.AllTabList;
import com.cm.cmrl.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class AllTimeDetail extends Fragment {

    private static List<AllTabList> allTimeDetailArrayList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle arg) {
        final View view = inflater.inflate(R.layout.all_time_detail, container,
                false);

        TabLayout smartTabLayout = view.findViewById(R.id.tabs);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        prepareAlbums();

        viewPager.setAdapter(new CategoryAdapter(getChildFragmentManager(), allTimeDetailArrayList));
        smartTabLayout.setupWithViewPager(viewPager);
        smartTabLayout.setSelectedTabIndicatorHeight(5);

        smartTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#246FC3"));
        //tabLayout.setSelectedTabIndicatorHeight(5);

        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    AllFragmentTime orderManagement = new AllFragmentTime();
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left).replace(R.id.flContent, orderManagement).commit();
                    }


                    return true;
                }
            }
            return false;
        });

        return  view;

    }
    private void prepareAlbums() {

        AllTabList a = new AllTabList("1","Tab 1");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("2","Tab 2");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("3","Tab 3");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("4","Tab 4");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("5","Tab 5");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("6","Tab 6");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("7","Tab 7");
        allTimeDetailArrayList.add(a);

        a = new AllTabList("8","Tab 8");
        allTimeDetailArrayList.add(a);


    }
    static class CategoryAdapter extends FragmentPagerAdapter {


        List<AllTabList> parentArrayListArrayList;



        CategoryAdapter(FragmentManager fragmentManager,
                        List<AllTabList> parentArrayListArrayList) {
            // TODO Auto-generated constructor stub

            super(fragmentManager);

            this.parentArrayListArrayList = parentArrayListArrayList;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return parentArrayListArrayList.size();
        }

        public CharSequence getPageTitle(int position) {

            return parentArrayListArrayList.get(position).getName();
        }

        @NonNull
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub

            Fragment fr = new AllTimeSubDetail(); // Replace with your Fragment class
            Bundle bundle = new Bundle();
            fr.setArguments(bundle);


            return fr;
        }
    }
}
