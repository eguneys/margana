package oyun.net.anagram.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.widget.GridView;

import android.graphics.Canvas;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;

public class AnagramView extends GridView {

    public AnagramView(Context context) {
        super(context);
        init();
    }

    public AnagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnagramView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
    }
}
