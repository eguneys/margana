package oyun.net.anagram.widget.quiz;

import android.util.Log;

import android.content.Context;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramAdapter;
import oyun.net.anagram.widget.AnagramView;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    private final int MarkedLettersFadeDelay = 1000;

    private int mNextAnagramIndex = 0;
    private AnagramView mAnagramView;

    private TextView mMarkedLetters;

    public AnagramQuizView(Context context, Category category, AnagramQuiz quiz) {
        super(context, category, quiz);
    }

    private boolean checkIfSolved(String markedAnagram) {
        return markedAnagram.toLowerCase()
            .equals(getNextAnagram().getAnswer().toLowerCase());
    }

    private Anagram getNextAnagram() {
        return ((AnagramQuiz)getQuiz()).get(mNextAnagramIndex);
    }

    private void setNextAnagram() {
        mNextAnagramIndex++;
        mNextAnagramIndex = mNextAnagramIndex % ((AnagramQuiz)getQuiz()).size();
    }

    private Anagram withSetGetNextAnagram() {
        setNextAnagram();
        return getNextAnagram();
    }

    private void nextAnagramWithTransition(Anagram nextAnagram) {
        mAnagramView.setAnagram(nextAnagram);
        mAnagramView.popLetters();
    }

    private void fadeMarkedLetters() {
        mMarkedLetters
            .animate()
            .alpha(0)
            .setStartDelay(MarkedLettersFadeDelay)
            .setListener(new AnimatorListenerAdapter() {
                    
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mMarkedLetters.setText("");
                        mMarkedLetters.setAlpha(1);
                    }
                });
    }

    @Override
    protected View createQuizContentView() {
        Anagram nextAnagram = getNextAnagram();


        //mAnagramView = new AnagramView(getContext());
        View rootView = getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);

        mMarkedLetters = (TextView) rootView.findViewById(R.id.marked_letters);

        mAnagramView = (AnagramView) rootView.findViewById(R.id.anagram_content);

        mAnagramView.setAnagram(nextAnagram);

        mAnagramView.setAnagramListener(new AnagramView.AnagramListener() {
                
                @Override
                public void onLetterMarked(String markedAnagram) {
                    mMarkedLetters.setText(markedAnagram);
                    mMarkedLetters.animate().cancel();
                }

                @Override
                public void onAnagramMarked(String markedAnagram) {
                    boolean vanishIfSolved = checkIfSolved(markedAnagram);
                    if (vanishIfSolved) {
                        setNextAnagram();
                    }
                    mAnagramView.withShakeOrVanishClearMarkedLetters(vanishIfSolved);
                    fadeMarkedLetters();
                }

                @Override
                public void onAnagramVanish() {
                    Anagram nextAnagram = getNextAnagram();
                    nextAnagramWithTransition(nextAnagram);
                }

                @Override
                public void onAnagramPop() {
                }
            });

        rootView.findViewById(R.id.reload_button).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mAnagramView.vanishLetters();
                }

            });
        return rootView;
    }
}
