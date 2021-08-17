package com.cm.cmrl.ticketview;



import androidx.cardview.widget.CardView;

/**
 * Created by Iddinesh.
 */

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}