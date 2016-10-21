package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.TextView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.adapter.QuizAdapter;
import oyun.net.anagram.helper.ApiLevelHelper;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class QuizFragment extends Fragment
{
    private Category mCategory;
    private AdapterViewAnimator mQuizView;
    private QuizAdapter mQuizAdapter;

    public static QuizFragment newInstance(String categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("The category can not be null");
        }

        Bundle args = new Bundle();
        args.putString(Category.TAG, categoryId);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        String categoryId = getArguments().getString(Category.TAG);
        mCategory = AnagramDatabaseHelper.getCategoryWith(getActivity(), categoryId);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.fragment_quiz, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mQuizView = (AdapterViewAnimator) view.findViewById(R.id.quiz_view);
        decideOnViewToDisplay();
        setQuizViewAnimations();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setQuizViewAnimations() {
        if (ApiLevelHelper.isLowerThanLollipop()) {
            return;
        }
    }

    private void decideOnViewToDisplay() {
        final boolean isSolved = false;
        if (isSolved) {
        } else {
            mQuizView.setAdapter(getQuizAdapter());
        }
    }

    private QuizAdapter getQuizAdapter() {
        if (null == mQuizAdapter) {
            mQuizAdapter = new QuizAdapter(getActivity(), mCategory);
        }
        return mQuizAdapter;
    }
}
