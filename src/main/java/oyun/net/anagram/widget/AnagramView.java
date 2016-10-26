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
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramQuizAdapter;

public class AnagramView extends GridView {

    private final Rect hitRect = new Rect();

    private List<Integer> markedSquares;

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
        markedSquares = new ArrayList<Integer>();

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    ViewTreeObserver obs = AnagramView.this.getViewTreeObserver();
                    obs.removeOnPreDrawListener(this);
                    int size = getChildCount();
                    for (int i = 0; i < size; i++) {
                        LetterView item = (LetterView) getChildAt(i);
                        item.setAnimationListener(new LetterView.LetterAnimationListener() {
                                @Override
                                public void onLetterPop() {
                                    
                                }

                                @Override
                                public void onLetterVanish() {
                                    
                                }
                            });
                        item.pop();
                    }
                    return true;
                }
            });
    }

    private String getMarkedString() {
        AnagramQuizAdapter adapter = (AnagramQuizAdapter)getAdapter();
        StringBuilder sb = new StringBuilder();
        for (int i : markedSquares) {
            sb.append(adapter.getItem(i));
        }
        return sb.toString();
    }

    private String getSolutionString() {
        AnagramQuizAdapter adapter = (AnagramQuizAdapter)getAdapter();
        AnagramQuiz anagram = adapter.getAnagram();
        return anagram.getAnswer();
    }

    private boolean isSuccess() {
        String markedString = getMarkedString();
        String solutionString = getSolutionString();
        return solutionString.toLowerCase().equals(markedString.toLowerCase());
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
                        markedSquares.add(i);
                        item.setMark(true);
                    }
                }
            }
            //mLetterViews[0].setMark(!mLetterViews[0].getMark());
            break;
        case MotionEvent.ACTION_UP:

            boolean solved = isSuccess();

            for (int i : markedSquares) {
                LetterView item = (LetterView) getChildAt(i);
                item.setMark(false);

                if (solved) {
                    item.vanish();
                } else {
                    item.shake();
                }
            }

            markedSquares.clear();
        }
        return true;
    }
}
