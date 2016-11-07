package oyun.net.anagram.widget;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.util.AttributeSet;

import android.database.DataSetObserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.GridLayout;
import android.widget.BaseAdapter;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;

public abstract class AbsAnagramView2 extends GridLayout {

    private int mSpacing;

    private int mChildCount;

    protected Anagram mAnagram;

    protected List<LetterViewHolder> mAllViews;

    protected List<LetterViewHolder> mAllViews4;

    public AbsAnagramView2(Context context) {
        this(context, null);
    }

    public AbsAnagramView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsAnagramView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int MAX_CHILD = 9;
        mSpacing = (int) getContext().getResources().getDimension(R.dimen.spacing_nano);
        setColumnCount(3);

        mAllViews = new ArrayList<LetterViewHolder>();

        for (int i = 0; i < MAX_CHILD; i++) {
            LetterView child = createLetterView();
            mAllViews.add(new LetterViewHolder(child));
            addView(child, child.getLayoutParams());
        }
        // for (int i = 0; i < 4; i++) {
        //     LetterView child = createLetterView();
        //     mAllViews4.add(new LetterViewHolder(child));
        //     addView(child, child.getLayoutParams());
        // }
        layoutChildren();
    }

    private LetterView createLetterView() {
        return (LetterView)LayoutInflater.from(getContext())
            .inflate(R.layout.view_letter, this, false);
    }

    private void layoutChildren() {
        int size = getChildCount();
        int colCount = size > 4 ? 3 : 2;

        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            int row = i / colCount;
            int col = i % colCount;
            GridLayout.LayoutParams p = (GridLayout.LayoutParams) child.getLayoutParams();
            p.setMargins(mSpacing, mSpacing, mSpacing, mSpacing);
            p.rowSpec = GridLayout.spec(row);
            p.columnSpec = GridLayout.spec(col);
            child.setLayoutParams(p);
        }
    }

    public void setAnagram(Anagram anagram) {
        mAnagram = anagram;
        setLastAnimatedView();

        int size = anagram.size();
        for (int i = 0; i < size; i++) {
            LetterViewHolder v = mAllViews.get(i);
            v.itemView.setLetter(mAnagram.getLetter(i));
        }
        for (int i = size; i < getChildCount(); i++) {
            LetterView child = mAllViews.get(i).itemView;
            child.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < size; i++) {
            LetterView child = mAllViews.get(i).itemView;
            child.setVisibility(View.VISIBLE);
        }
    }

    private void setLastAnimatedView() {
        mChildCount = mAnagram.size() - 1;
    }

    protected View getLastAnimatedView() {
        return getChildAt(mChildCount);
    }

    protected class LetterViewHolder {
        public LetterView itemView;

        public LetterViewHolder(LetterView itemView) {
            this.itemView = itemView;
        }
    }
}
