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

    private AnagramListener mAnagramListener;

    private final Rect hitRect = new Rect();

    private List<Integer> markedLetters;

    private final List<Integer> allSquares = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);

    private List<LetterView> allViews;

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
        markedLetters = new ArrayList<Integer>();

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    popLetters();

                    ViewTreeObserver obs = AnagramView.this.getViewTreeObserver();
                    obs.removeOnPreDrawListener(this);
                    ((LetterView)getChildAt(0))
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

    private String getMarkedLetters() {
        AnagramAdapter adapter = (AnagramAdapter)getAdapter();
        StringBuilder sb = new StringBuilder();
        for (int i : markedLetters) {
            sb.append(adapter.getItem(i));
        }
        return sb.toString();
    }

    public void popLetters() {
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            LetterView item = (LetterView) getChildAt(i);
            item.pop();
        }
    }

    public void clearMarkedLetters() {
        for (int i : markedLetters) {
            LetterView item = (LetterView) getChildAt(i);
            item.setMark(false);
        }
        markedLetters.clear();
    }

    public void withShakeOrVanishClearMarkedLetters(boolean isSolved) {
        for (int i : markedLetters) {
            LetterView item = (LetterView) getChildAt(i);
            if (isSolved) {
                item.vanish();
            } else {
                item.shake();
            }
            item.setMark(false);
        }
        markedLetters.clear();
    }

    // http://stackoverflow.com/questions/24251029/android-get-position-listview-in-ontouchlistener
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
            final int size = getChildCount();
            for (int i = 0; i < size; i++) {
                LetterView item = (LetterView) getChildAt(i);
                item.getHitRect(hitRect);
                hitRect.inset(10, 10);
                if (hitRect.contains(eventX, eventY)) {
                    if (!item.getMark()) {
                        markedLetters.add(i);
                        item.setMark(true);
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
        public void onAnagramMarked(String markedAnagram);
        public void onAnagramVanish();
        public void onAnagramPop();
    }
}
