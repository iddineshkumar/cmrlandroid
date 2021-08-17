package com.cm.cmrl.waveswiperefreshLayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Iddinesh.
 */

public class DisplayUtil {

    private DisplayUtil(){}

    /**
     *
     *
     * @param context {@link Context}
     * @return 600dp
     */
    public static boolean isOver600dp(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density >= 600;
    }
}