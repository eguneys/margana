package oyun.net.anagram.fragment;

import java.util.List;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.AdapterViewAnimator;
import android.widget.ProgressBar;
import android.widget.TextView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.Theme;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;

import oyun.net.anagram.widget.quiz.AnagramQuizView;

import oyun.net.anagram.adapter.QuizAdapter;
import oyun.net.anagram.helper.ApiLevelHelper;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class QuizFragment extends Fragment
{

    private int mQuizSize;

    private Category mCategory;
    private AnagramQuiz mQuiz;

    private FrameLayout mQuizViewContainer;
    private AnagramQuizView mQuizView;
    // private AdapterViewAnimator mQuizView;
    // private QuizAdapter mQuizAdapter;

    private TextView mProgressText;
    private ProgressBar mProgressBar;

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
        super.onCreate(savedInstanceState);

        String categoryId = getArguments().getString(Category.TAG);
        mCategory = AnagramDatabaseHelper.getCategoryById(getActivity(), categoryId);
        mQuiz = new AnagramQuiz(mCategory.getTime(), mCategory.getWordLength());

        if (!populateQuizWithAnagrams(mQuiz)) {
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create a themed Context and custom LayoutInflater
        // to get nicely themed views in this fragment
        final Theme theme = mCategory.getTheme();
        final ContextThemeWrapper context = new ContextThemeWrapper(getActivity(),
                                                                theme.getStyleId());
        final LayoutInflater themedInflater = LayoutInflater.from(context);

        final View contentView = themedInflater.inflate(R.layout.fragment_quiz, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // mQuizView = (AdapterViewAnimator) view.findViewById(R.id.quiz_view);
        mQuizViewContainer = (FrameLayout) view.findViewById(R.id.quiz_view);

        decideOnViewToDisplay();
        // setQuizViewAnimations();
        //initProgressToolbar(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean populateQuizWithAnagrams(AnagramQuiz quiz) {
        List<oyun.net.anagram.model.Anagram> anagrams = AnagramDatabaseHelper
            .getRandomUnsolvedAnagrams(getActivity(),
                                       quiz.getWordLength(),
                                       mCategory.getWordLimit());

        if (anagrams.size() < mCategory.getWordLimit()) {
            return false;
        }

        quiz.addAnagrams(anagrams);
        return true;
    }

    private void initProgressToolbar(View view) {
        // final int firstUnsolvedQuizPosition = mCategory.getFirstUnsolvedQuizPosition();
        // final List<Quiz> quizzes = mCategory.getQuizzes();
        // mQuizSize = quizzes.size();
        // mProgressText = (TextView) view.findViewById(R.id.progress_text);
        // mProgressBar = ((ProgressBar) view.findViewById(R.id.progress));
        // mProgressBar.setMax(mQuizSize);
        // setProgress(firstUnsolvedQuizPosition);
    }

    private void decideOnViewToDisplay() {
        final boolean isSolved = mQuiz.isSolved();
        if (isSolved) {
            showSummary();
        } else {
            mQuizView = new AnagramQuizView(getContext(),
                                            mCategory,
                                            mQuiz);
            mQuizViewContainer.addView(mQuizView);
        }
    }


    private void setQuizViewAnimations() {
        if (ApiLevelHelper.isLowerThanLollipop()) {
            return;
        }
    }

    private void setProgress(int currentQuizPosition) {
        // if (!isAdded()) {
        //     return;
        // }
        // mProgressText
        //     .setText(getString(R.string.quiz_of_quizzes, currentQuizPosition, mQuizSize));
        mProgressBar.setProgress(currentQuizPosition);
    }

    // private QuizAdapter getQuizAdapter() {
    //     if (null == mQuizAdapter) {
    //         mQuizAdapter = new QuizAdapter(getActivity(), mCategory);
    //     }

    //     return mQuizAdapter;
    // }

    public void showSummary() {
        
    }

    public void replay() {
        mQuiz.reset();
        if (populateQuizWithAnagrams(mQuiz)) {
            mQuizView.reset(mQuiz);
        } else {
            finishActivity();
            return;
        }
    }

    private void finishActivity() {
        getActivity().finish();
    }
}
