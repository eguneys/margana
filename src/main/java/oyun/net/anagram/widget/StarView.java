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

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;

public class StarView extends FrameLayout {

    private final int PopAnimationDuration = 200;
    private final int VanishAnimationDuration = 1000;
    private final int VanishAnimationDelay = 1500 - VanishAnimationDuration;

    private static final Interpolator VanishInterpolator = new LinearInterpolator();
    private static final Interpolator PopInterpolator = new OvershootInterpolator();

    private AnimationListener mAnimationListener;

    private AnimatorSet mPopAnimatorSet;
    private AnimatorSet mVanishAnimatorSet;

    private ImageView mStarImage;
    private TextView mNbStarText;

    private int mNbStar;

    public StarView(Context context) {
        super(context);
        init();
    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_star, this, true);

        mStarImage = (ImageView) findViewById(R.id.star_image);
        mNbStarText = (TextView) findViewById(R.id.nb_star_text);

        updateStarText();
    }

    private void updateStarText() {
        if (mNbStar > 0) {
            mNbStarText.setText(String.format("%1$d", mNbStar));
        } else {
            mNbStarText.setText("");
        }
    }

    public void setNbStar(int stars) {
        mNbStar = stars;
        updateStarText();
    }

    public void setTextSize(int size) {
        mNbStarText.setTextSize(getContext().getResources().getDimension(size));
    }

    public void setTextColor(int color) {
        mNbStarText.setTextColor(getContext().getResources().getColor(color));
    }

    public void shine() {
    }

    public void setAnimationListener(AnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public interface AnimationListener {
        public void onVanishEnd();
    }
}
