package oyun.net.anagram.widget;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;

import android.graphics.Canvas;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.widget.drawable.LetterDrawable;

public class LetterView extends View {

    private final Interpolator mLinearOutSlowInInterpolator = new LinearOutSlowInInterpolator();

    private LetterAnimationListener mAnimationListener;

    private LetterDrawable mLetterDrawable;

    private boolean isMarked;

    public LetterView(Context context) {
        super(context);
        init();
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LetterView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        int bgColor = ContextCompat.getColor(getContext(), R.color.letter_background);
        int shadowColor = ContextCompat.getColor(getContext(), R.color.letter_shadow);
        int textColor = ContextCompat.getColor(getContext(), R.color.letter_text);
        int textShadow = ContextCompat.getColor(getContext(), R.color.letter_text_shadow);
        mLetterDrawable = new LetterDrawable(bgColor, shadowColor, textColor, textShadow);

        isMarked = false;
        setBackground(mLetterDrawable);

        mLetterDrawable.setAnimationListener(new LetterDrawable.AnimationListener() {
                @Override
                public void onLetterPop() {
                    if (LetterView.this.mAnimationListener != null) {
                        LetterView.this.mAnimationListener.onLetterPop(LetterView.this);
                    }
                }
                @Override
                public void onLetterVanish() {
                    if (LetterView.this.mAnimationListener != null) {
                        LetterView.this.mAnimationListener.onLetterVanish(LetterView.this);
                    }
                }
            });
    }

    public void setLetter(String letter) {
        mLetterDrawable.setLetter(letter);
    }


    public void setMark(boolean mark) {
        if (isMarked != mark) {
            isMarked = mark;
            invalidateAnimation();
        }
    }

    public boolean getMark() {
        return isMarked;
    }

    public void setAnimationDelay(int delay) {
        mLetterDrawable.setAnimationDelay(delay);
    }

    public boolean isAnimating() {
        return mLetterDrawable.isAnimating();
    }

    public void shake() {
        mLetterDrawable.animateShake();
    }

    public void vanish() {
        mLetterDrawable.animateVanish();
    }

    public void pop() {
        mLetterDrawable.animatePop();
    }

    private void invalidateAnimation() {
        if (isMarked) {
            mLetterDrawable.animateMark();
        } else {
            mLetterDrawable.animateUnmark();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        mLetterDrawable.setBounds(0, 0, w, h);
    }

    // @Override
    // protected void onDraw(Canvas canvas) {
    //     super.onDraw(canvas);
    //     mLetterDrawable.draw(canvas);
    // }

    public void setAnimationListener(LetterAnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public interface LetterAnimationListener {
        public void onLetterPop(LetterView v);
        public void onLetterVanish(LetterView v);
    }
}
