package oyun.net.anagram.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;

import android.view.animation.Interpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import android.view.MotionEvent;
import android.view.View;

public class ButtonTouchListener implements View.OnTouchListener {

    private static final Interpolator DownInterpolator = new OvershootInterpolator();

    private final float SCALE_X = 0.92f;

    private View mButton;
    private AnimatorSet mDownAnimatorSet;
    private AnimatorSet mUpAnimatorSet;

    public ButtonTouchListener(View button) {
        mButton = button;
        initAnimators();
    }

    private void initAnimators() {
        View view = mButton;
        mDownAnimatorSet = new AnimatorSet();
        mDownAnimatorSet.playTogether(ObjectAnimator
                                      .ofPropertyValuesHolder(view,
                                                              PropertyValuesHolder.ofFloat("scaleX", SCALE_X),
                                                              PropertyValuesHolder.ofFloat("scaleY", SCALE_X)));
        mDownAnimatorSet.setInterpolator(DownInterpolator);

        mUpAnimatorSet = new AnimatorSet();
        mUpAnimatorSet.playTogether(ObjectAnimator
                                    .ofPropertyValuesHolder(view,
                                                            PropertyValuesHolder.ofFloat("scaleX", 1f),
                                                            PropertyValuesHolder.ofFloat("scaleY", 1f)));
        mUpAnimatorSet.setInterpolator(DownInterpolator);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mDownAnimatorSet.start();
            break;
        case MotionEvent.ACTION_UP:
            mUpAnimatorSet.start();
            break;
        }
        return false;
    }
}
