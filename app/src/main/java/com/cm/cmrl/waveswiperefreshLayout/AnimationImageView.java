package com.cm.cmrl.waveswiperefreshLayout;

import android.content.Context;

import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Created by Iddinesh.
 */

class AnimationImageView extends AppCompatImageView {

    /**
     * AnimationStartEndListener
     */
    private Animation.AnimationListener mListener;

    /**
     *
     * {@inheritDoc}
     */
    public AnimationImageView(Context context) {
        super(context);
    }

    /**
     * {@link AnimationImageView#mListener}
     *
     * @param listener {@link Animation.AnimationListener}
     */
    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    /**
     * ViewAnimationStarListener {@link Animation.AnimationListener#onAnimationStart(Animation)}
     *
     */
    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    /**
     * ViewAnimationListener {@link Animation.AnimationListener#onAnimationEnd(Animation)}
     * (Animation)
     */
    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }
}