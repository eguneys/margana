package oyun.net.anagram.widget;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.ViewTreeObserver;

import android.widget.GridView;

import android.graphics.Rect;
import android.graphics.Canvas;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.adapter.AnagramAdapter;

public class AnagramView extends GridView {


    private final int animPosDelay = 20;
    private final Rect hitRect = new Rect();
    private final List<Integer> allSquares = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);

    private AnagramListener mAnagramListener;

    private List<LetterViewHolder> mAllViews;
    private List<Integer> mMarkedLetters;

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
        mMarkedLetters = new ArrayList<Integer>();

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    initViewHolder();

                    popLetters();

                    ViewTreeObserver obs = AnagramView.this.getViewTreeObserver();
                    obs.removeOnPreDrawListener(this);
                    // make sure its the last item to animate
                    ((LetterView)getChildAt(6))
                        .setAnimationListener(new LetterView.LetterAnimationListener() {
                                @Override
                                public void onLetterPop() {
                                    if (mAnagramListener != null) {
                                        mAnagramListener.onAnagramPop();
                                    }
                                }

                                @Override
                                public void onLetterVanish() {
                                    if (mAnagramListener != null) {
                                        mAnagramListener.onAnagramVanish();
                                    }
                                }
                            });
                    return true;
                }
            });
    }

    private void initViewHolder() {
        mAllViews = new ArrayList<LetterViewHolder>();
        for (int i = 0; i < getChildCount(); i++) {
            LetterView itemView = (LetterView)getChildAt(i);
            int startDelay = i * animPosDelay;
            itemView.setAnimationDelay(startDelay);
            mAllViews.add(new LetterViewHolder(itemView, i));
        }
    }

    private String getMarkedLetters() {
        AnagramAdapter adapter = (AnagramAdapter)getAdapter();
        StringBuilder sb = new StringBuilder();
        for (int i : mMarkedLetters) {
            sb.append(adapter.getItem(i));
        }
        return sb.toString();
    }

    public void vanishLetters() {
        for (LetterViewHolder viewHolder : mAllViews) {
            LetterView item = viewHolder.itemView;
            item.vanish();
        }        
    }

    public void popLetters() {
        for (LetterViewHolder viewHolder : mAllViews) {
            viewHolder.itemView.pop();
        }
    }

    public void clearMarkedLetters() {
        for (int i : mMarkedLetters) {
            LetterView item = (LetterView) getChildAt(i);
            item.setMark(false);
        }
        mMarkedLetters.clear();
    }

    public void withShakeOrVanishClearMarkedLetters(boolean isSolved) {
        for (int i : mMarkedLetters) {
            LetterView item = (LetterView) getChildAt(i);
            if (isSolved) {
                item.vanish();
            } else {
                item.shake();
            }
            item.setMark(false);
        }
        mMarkedLetters.clear();
    }

    // http://stackoverflow.com/questions/24251029/android-get-position-listview-in-ontouchlistener
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
            for (LetterViewHolder viewHolder : mAllViews) {
                LetterView item = viewHolder.itemView;
                item.getHitRect(hitRect);
                hitRect.inset(10, 10);
                if (hitRect.contains(eventX, eventY)) {
                    if (!item.getMark() && !item.isAnimating()) {
                        mMarkedLetters.add(viewHolder.index);
                        item.setMark(true);
                        if (mAnagramListener != null) {
                            mAnagramListener.onLetterMarked(getMarkedLetters());
                        }
                        break;
                    }
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            if (mAnagramListener != null) {
                mAnagramListener.onAnagramMarked(getMarkedLetters());
            }
        }
        return true;
    }

    public class LetterViewHolder {
        public LetterView itemView;
        public int index;

        public LetterViewHolder(LetterView itemView, int index) {
            this.itemView = itemView;
            this.index = index;
        }
    }

    public void setAnagramListener(AnagramListener listener) {
        this.mAnagramListener = listener;
    }

    public interface AnagramListener {
        public void onLetterMarked(String markedAnagram);
        public void onAnagramMarked(String markedAnagram);
        public void onAnagramVanish();
        public void onAnagramPop();
    }
}
