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
import oyun.net.anagram.helper.TimerHelper;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    private final int MarkedLettersFadeDelay = 1000;

    private int mNextAnagramIndex = 0;
    private AnagramView mAnagramView;

    private TextView mMarkedLetters;
    private TextView mTimerText;
    private TextView mTimerDeciText;

    private TimerHelper mTimerHelper;

    public AnagramQuizView(Context context, Category category, AnagramQuiz quiz) {
        super(context, category, quiz);
    }

    private void init() {
        mTimerHelper = new TimerHelper();
        mTimerHelper.setListener(new TimerHelper.TimerListener() {
                @Override
                public void onTimer(int minutes, int seconds, int deciMillis) {
                    String formatString = minutes==0?
                        String.format("%1$02d", seconds):
                        String.format("%1$02d:%2$02d", minutes, seconds);

                    String deciFormat = String.format(".%1$d", deciMillis);

                    mTimerText.setText(formatString);
                    mTimerDeciText.setText(deciFormat);
                }
            });
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

        init();

        Anagram nextAnagram = getNextAnagram();

        //mAnagramView = new AnagramView(getContext());
        View rootView = getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);

        mTimerText = (TextView)rootView.findViewById(R.id.timer_text);
        mTimerDeciText = (TextView)rootView.findViewById(R.id.timer_deci_text);

        mMarkedLetters = (TextView) rootView.findViewById(R.id.marked_letters);

        mAnagramView = (AnagramView) rootView.findViewById(R.id.anagram_content);

        mAnagramView.setAnagram(nextAnagram);

        mAnagramView
            .setAnagramListener(new AnagramView.AnagramListener() {
                
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
                            mAnagramView.vanishLetters();
                        } else {
                            mAnagramView.shakeMarkedLetters();
                        }
                        mAnagramView.clearMarkedLetters();
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

        rootView.findViewById(R.id.reload_button)
            .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mAnagramView.vanishLetters();
                    }
                });

        mTimerHelper.start();

        return rootView;
    }
}
