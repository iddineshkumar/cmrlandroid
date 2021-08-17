package com.cm.cmrl.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;

import android.util.Property;

import androidx.annotation.RequiresApi;

/**
 * Created by Iddinesh.
 */
public class ObjectAnimatorCompatLollipop {

    private ObjectAnimatorCompatLollipop() {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ObjectAnimator ofArgb(Object target, String propertyName, int... values) {
        return ObjectAnimator.ofArgb(target, propertyName, values);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static <T> ObjectAnimator ofArgb(T target, Property<T, Integer> property,
                                            int... values) {
        return ObjectAnimator.ofArgb(target, property, values);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName,
                                         Path path) {
        return ObjectAnimator.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty,
                                             Property<T, Float> yProperty, Path path) {
        return ObjectAnimator.ofFloat(target, xProperty, yProperty, path);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static ObjectAnimator ofInt(Object target, String xPropertyName, String yPropertyName,
                                       Path path) {
        return ObjectAnimator.ofInt(target, xPropertyName, yPropertyName, path);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> xProperty,
                                           Property<T, Integer> yProperty, Path path) {
        return ObjectAnimator.ofInt(target, xProperty, yProperty, path);
    }
}
