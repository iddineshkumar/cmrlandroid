package com.cm.cmrl.materialprogressbar;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by Iddinesh.
 */
public interface TintableDrawable {


    void setTint(@ColorInt int tintColor);


    void setTintList(@Nullable ColorStateList tint);


    void setTintMode(@NonNull PorterDuff.Mode tintMode);
}