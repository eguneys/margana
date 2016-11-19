package oyun.net.anagram.widget;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import android.widget.TextView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;

public class ScoreTextView extends TextView {

    private final int PopAnimationDuration = 200;
    private final int VanishAnimationDuration = 1000;

    private final int VanishAnimationDelay = 1500 - VanishAnimationDuration;

    private static final Interpolator VanishInterpolator = new LinearInterpolator();
    private static final Interpolator PopInterpolator = new OvershootInterpolator();

    private ScoreAnimationListener mAnimationListener;

    private AnimatorSet mPopAnimatorSet;
    private AnimatorSet mVanishAnimatorSet;

    private int mScore;
    private String mScoreFormat = "+%1d";

    public ScoreTextView(Context context) {
        super(context);
        init();
    }

    public ScoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreTextView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        mVanishAnimatorSet = new AnimatorSet();
        mVanishAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, "alpha", 0f),
                                        ObjectAnimator.ofFloat(this, "translationY", -20));
        mVanishAnimatorSet.setStartDelay(VanishAnimationDelay);
        mVanishAnimatorSet.setDuration(VanishAnimationDuration);
        mVanishAnimatorSet.setInterpolator(VanishInterpolator);


        mPopAnimatorSet = new AnimatorSet();
        mPopAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 1f),
                                     ObjectAnimator.ofFloat(this, "scaleY", 1f));
        mPopAnimatorSet.setDuration(PopAnimationDuration);
        mPopAnimatorSet.setInterpolator(PopInterpolator);

        mPopAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    mVanishAnimatorSet.start();
                }
            });
    }

    public boolean isAnimating() {
        return mPopAnimatorSet.isRunning() ||
            mVanishAnimatorSet.isRunning();
    }


    public void pop(int score) {
        mScore = score;

        setText(String.format(mScoreFormat, mScore));

        setAlpha(1f);
        setScaleX(0f);
        setScaleY(0f);
        setTranslationY(0f);
        mPopAnimatorSet.start();
    }

    public void doublePop(int addScore) {
        mScore += addScore;

        setText(String.format(mScoreFormat, mScore));
        setAlpha(0.9f);
        setScaleX(0.9f);
        setScaleY(0.9f);
        setTranslationY(0f);
        mPopAnimatorSet.cancel();
        mVanishAnimatorSet.cancel();
        mPopAnimatorSet.start();
    }

    public void setScoreFormat(String format) {
        mScoreFormat = format;
    }

    public void setAnimationListener(ScoreAnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public interface ScoreAnimationListener {
        public void onScoreVanish(ScoreTextView v);
    }
}
