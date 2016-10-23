package oyun.net.anagram.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;

import android.graphics.Canvas;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.widget.drawable.LetterDrawable;

public class LetterView extends View {

    private final Interpolator mLinearOutSlowInInterpolator = new LinearOutSlowInInterpolator();

    private LetterDrawable mLetterDrawable;

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
        mLetterDrawable = new LetterDrawable(R.color.theme_red_primary, R.color.theme_red_text);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        mLetterDrawable.setBounds(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLetterDrawable.draw(canvas);
    }
    
}
