package com.cm.cmrl.materialeditext;

import android.graphics.Color;

/**
 * Created by Iddinesh.
 */
class Colors {
    static boolean isLight(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068) > 130;
    }
}
