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

public abstract class AbsAnagramView extends GridLayout {
    
    private AdapterDataSetObserver mDataSetObserver;
    private BaseAdapter mAdapter;

    private int mSpacing;

    private int mChildCount = 7;

    private List<View> mViews;

    public AbsAnagramView(Context context) {
        this(context, null);
    }

    public AbsAnagramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsAnagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSpacing = (int) getContext().getResources().getDimension(R.dimen.spacing_nano);
        setColumnCount(3);

        mViews = new ArrayList<View>();
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;

        mDataSetObserver = new AdapterDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);

        for (int i = 0; i < mChildCount; i++) {
            int row = i / 3;
            int col = i % 3;
            View child = mAdapter.getView(i, null, this);
            GridLayout.LayoutParams p = (GridLayout.LayoutParams) child.getLayoutParams();
            p.setMargins(mSpacing, mSpacing, mSpacing, mSpacing);
            p.rowSpec = GridLayout.spec(row);
            p.columnSpec = GridLayout.spec(col);
            child.setLayoutParams(p);

            mViews.add(child);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < mChildCount; i++) {
            View item = mAdapter.getView(i, mViews.get(i), this);
            setupChild(item, i);
        }
    }

    protected View getLastAnimatedView() {
        return getChildAt(6);
    }

    private void setupChild(View child, int position) {
        addViewInLayout(child, position, child.getLayoutParams());
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            invalidate();
        }
    }
}
