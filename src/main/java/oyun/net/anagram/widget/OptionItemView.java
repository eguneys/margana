package oyun.net.anagram.widget;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;

import android.widget.FrameLayout;
import android.widget.TextView;

import oyun.net.anagram.R;

public class OptionItemView extends FrameLayout {

    public OptionItemView(Context context) {
        super(context);
        init();
    }

    public OptionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_option_item, this, true);
    }
}
