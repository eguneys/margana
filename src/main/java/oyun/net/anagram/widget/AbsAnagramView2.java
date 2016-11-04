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

    private int mChildCount = 7;

    protected Anagram mAnagram;

    protected List<LetterViewHolder> mAllViews;

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
        mSpacing = (int) getContext().getResources().getDimension(R.dimen.spacing_nano);
        setColumnCount(3);

        mAllViews = new ArrayList<LetterViewHolder>();

        for (int i = 0; i < mChildCount; i++) {
            int row = i / 3;
            int col = i % 3;
            LetterView child = createLetterView();
            GridLayout.LayoutParams p = (GridLayout.LayoutParams) child.getLayoutParams();
            p.setMargins(mSpacing, mSpacing, mSpacing, mSpacing);
            p.rowSpec = GridLayout.spec(row);
            p.columnSpec = GridLayout.spec(col);
            child.setLayoutParams(p);

            mAllViews.add(new LetterViewHolder(child, i));
            addView(child, p);
        }
    }

    private LetterView createLetterView() {
        return (LetterView)LayoutInflater.from(getContext())
            .inflate(R.layout.view_letter, this, false);
    }

    public void setAnagram(Anagram anagram) {
        mAnagram = anagram;

        for (LetterViewHolder v : mAllViews) {
            v.itemView.setLetter(mAnagram.getLetter(v.index));
        }
    }

    protected View getLastAnimatedView() {
        return getChildAt(mChildCount - 1);
    }

    protected class LetterViewHolder {
        public LetterView itemView;
        public int index;

        public LetterViewHolder(LetterView itemView, int index) {
            this.itemView = itemView;
            this.index = index;
        }
    }
}
