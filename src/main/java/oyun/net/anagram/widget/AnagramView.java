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
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.adapter.AnagramAdapter;

public class AnagramView extends AbsAnagramView2 {


    private final LetterView.LetterAnimationListener mLetterAnimationListener = 
        new LetterView.LetterAnimationListener() {
            @Override
            public void onLetterPop(LetterView v) {
                if (getLastAnimatedView() != v) {
                    return;
                }
                if (mAnagramListener != null) {
                    mAnagramListener.onAnagramPop();
                }
            }

            @Override
            public void onLetterVanish(LetterView v) {
                if (getLastAnimatedView() != v) {
                    return;
                }
                if (mAnagramListener != null) {
                    mAnagramListener.onAnagramVanish();
                }
            }
        };

    private final int animPosDelay = 20;
    private final Rect hitRect = new Rect();

    private AnagramListener mAnagramListener;

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

        setLetterStartDelay();
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    popLetters();

                    ViewTreeObserver obs = AnagramView.this.getViewTreeObserver();
                    obs.removeOnPreDrawListener(this);

                    return true;
                }
            });
    }

    private void setLetterStartDelay() {
        int colCount = 3;
        int[] colDelays = { 2, 1, 2 };
        int row = 0;
        int col = 0;
        for (LetterViewHolder viewHolder : mAllViews) {
            LetterView item = viewHolder.itemView;
            item.setAnimationDelay(row * 50 + colDelays[col] * 20);

            col++;
            if (col == colCount) {
                col = 0;
                row++;
            }
        }
    }

    private String getMarkedLetters() {
        StringBuilder sb = new StringBuilder();
        for (int i : mMarkedLetters) {
            sb.append(mAnagram.getLetter(i));
        }
        return sb.toString();
    }

    @Override
    public void setAnagram(Anagram anagram) {
        super.setAnagram(anagram);
        // make sure its the last item to animate
        ((LetterView)getLastAnimatedView())
            .setAnimationListener(mLetterAnimationListener);
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

    public void shakeMarkedLetters() {
        for (int i : mMarkedLetters) {
            LetterView item = (LetterView) getChildAt(i);
            item.shake();
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
            int size = mAnagram.size();
            for (int i = 0; i < size; i++) {
                LetterViewHolder viewHolder = mAllViews.get(i);
                LetterView item = viewHolder.itemView;
                item.getHitRect(hitRect);
                hitRect.inset(10, 10);
                if (hitRect.contains(eventX, eventY)) {
                    if (!item.getMark() && !item.isAnimating()) {
                        mMarkedLetters.add(i);
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
