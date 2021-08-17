package com.cm.cmrl.arcLayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.cm.cmrl.R;
/**
 * Created by Iddinesh.
 */
public class ArcLayoutSettings {
    private final static int CROP_INSIDE = 0;

    final static int POSITION_BOTTOM = 0;
    final static int POSITION_TOP = 1;
    final static int POSITION_LEFT = 2;
    final static int POSITION_RIGHT = 3;
    private boolean cropInside;
    private float arcHeight;
    private float elevation;

    private int position;

    private static float dpToPx(Context context) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
    }

    ArcLayoutSettings(Context context, AttributeSet attrs) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);
        arcHeight = styledAttributes.getDimension(R.styleable.ArcLayout_arc_height, dpToPx(context));

        final int cropDirection = styledAttributes.getInt(R.styleable.ArcLayout_arc_cropDirection, CROP_INSIDE);
        cropInside = (cropDirection == CROP_INSIDE);

        position = styledAttributes.getInt(R.styleable.ArcLayout_arc_position, POSITION_BOTTOM);

        styledAttributes.recycle();
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

     boolean isCropInside() {
        return cropInside;
    }

     float getArcHeight() {
        return arcHeight;
    }

    public int getPosition() {
        return position;
    }
}
