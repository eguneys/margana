package oyun.net.anagram.widget;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;

import android.graphics.Canvas;

import android.support.v4.content.ContextCompat;

import oyun.net.anagram.R;
import oyun.net.anagram.widget.drawable.AnagramProgressDrawable;

public class AnagramProgressView extends View {

    private AnagramProgressDrawable mProgressDrawable;

    public AnagramProgressView(Context context) {
        super(context);
        init();
    }

    public AnagramProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnagramProgressView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        mProgressDrawable = new AnagramProgressDrawable(ContextCompat.getDrawable(getContext(),
                                                                                  R.drawable.bar_middle),
                                                        ContextCompat.getDrawable(getContext(),
                                                                                  R.drawable.bar_middle_empty));
        setBackground(mProgressDrawable);
    }

    public void setProgress(int level, boolean mark) {
        mProgressDrawable.setProgress(level, mark);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        mProgressDrawable.setBounds(0, 0, w, h);
    }
}
