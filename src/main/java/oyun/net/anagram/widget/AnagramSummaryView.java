package oyun.net.anagram.widget;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.util.AttributeSet;

import android.content.Context;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;

import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import android.widget.RelativeLayout;
import android.widget.TextView;

import android.graphics.Rect;
import android.graphics.Canvas;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.adapter.AnagramAdapter;

import oyun.net.anagram.widget.drawable.LinesDrawable;;

public class AnagramSummaryView extends RelativeLayout {


    private long CongratzScaleAnimationDuration = 400;
    private Interpolator CongratzScaleInterpolator = new OvershootInterpolator();

    private LinesDrawable mLinesDrawable;

    private TextView mCongratzText;

    public AnagramSummaryView(Context context) {
        super(context);
        init();
    }

    public AnagramSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnagramSummaryView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        int bgColor = ContextCompat.getColor(getContext(), R.color.anagram_primary_dark);

        LayoutInflater.from(getContext()).inflate(R.layout.view_anagram_summary, this, true);

        mCongratzText = (TextView) findViewById(R.id.congratz_text);
        mLinesDrawable = new LinesDrawable(bgColor);
        setBackground(mLinesDrawable);
    }

    public void animateSummary() {
        mLinesDrawable.animateLines();

        mCongratzText.setScaleX(0);
        mCongratzText.setScaleY(0);
        mCongratzText
            .animate()
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(CongratzScaleInterpolator)
            .setDuration(CongratzScaleAnimationDuration);
    }

}
