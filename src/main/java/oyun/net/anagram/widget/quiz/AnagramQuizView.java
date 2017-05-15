package oyun.net.anagram.widget.quiz;

import android.util.Log;

import android.content.Context;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import oyun.net.anagram.R;
import oyun.net.anagram.activity.QuizActivity;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramAdapter;
import oyun.net.anagram.widget.AnagramView;
import oyun.net.anagram.widget.ScoreTextView;
import oyun.net.anagram.helper.TimerHelper;

import oyun.net.anagram.helper.ButtonTouchListener;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    private final int MarkedLettersFadeDelay = 1000;
    private final String ScoreFormatText = "%1$02d";

    private int mAnagramScore;
    
    private int mNextAnagramIndex;
    private View background;

    private AnagramView mAnagramView;

    private ImageButton mReloadButton;
    private ImageButton mSkipButton;

    private TextView mAnagramNb;
    private TextView mMarkedLetters;
    private TextView mTimerText;
    private TextView mTimerDeciText;

    private TextView mScoreText;
    private ScoreTextView mScorePopText;

    private long mTime;
    private TimerHelper mTimerHelper;

    public AnagramQuizView(Context context, Category category, AnagramQuiz quiz) {
        super(context, category, quiz);
    }

    private void init() {
        mTime = ((AnagramQuiz)getQuiz()).getTime() * 1000;
        Log.e("YYY init", this.toString());
        mTimerHelper = new TimerHelper();
        mTimerHelper.setListener(new TimerHelper.TimerListener() {
                @Override
                public void onTimeout() {
                    Log.e("YYY timeout", this.toString());
                    addTimeSpentBeforeNextAnagram();
                    stopAnagramInteraction();
                    // floatLettersDark();
                    proceedActivity();
                }

                @Override
                public boolean onTimer(long elapsed) {
                    elapsed = mTime - elapsed;

                    if (elapsed <= 0) {
                        elapsed = 0;
                    }

                    int deciMillis = (int) (elapsed / 100) % 10;
                    int seconds = (int) (elapsed / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;


                    String formatString = minutes==0?
                        String.format("%1$02d", seconds):
                        String.format("%1$02d:%2$02d", minutes, seconds);

                    String deciFormat = String.format(".%1$d", deciMillis);

                    mTimerText.setText(formatString);
                    mTimerDeciText.setText(deciFormat);

                    return elapsed == 0;
                }
            });

        
    }

    private void shuffleAnagram() {
        getNextAnagram().shuffle();
    }

    private boolean checkIfSolved(String markedAnagram) {
        return markedAnagram.toLowerCase()
            .equals(getNextAnagram().getAnswer().toLowerCase());
    }

    private Anagram getNextAnagram() {
         return ((AnagramQuiz)getQuiz()).get(mNextAnagramIndex);
    }

    private boolean setNextUnsolvedAnagram() {
        AnagramQuiz quiz = (AnagramQuiz)getQuiz();
        int size = quiz.size();


        // go up
        for (int i = mNextAnagramIndex + 1; i < size; i++) {
            if (!quiz.get(i).isSolved()) {
                mNextAnagramIndex = i;
                return true;
            }
        }

        // go down
        for (int i = mNextAnagramIndex - 1; i > -1; i--) {
            if (!quiz.get(i).isSolved()) {
                mNextAnagramIndex = i;
                return true;
            }
        }


        // Fix for proceed before last unsolved
        if (!quiz.get(mNextAnagramIndex).isSolved()) {
            return true;
        }

        return false;
    }

    private void addTimeSpentBeforeNextAnagram() {
        Anagram anagram = getNextAnagram();
        anagram.addTimeSpent((int)mTimerHelper.getLapTime() / 1000);
        mTimerHelper.lap();
    }

    private boolean setNextAnagram() {
        if (mNextAnagramIndex >= ((AnagramQuiz)getQuiz()).size() - 1) {
            return false;
        }
        mNextAnagramIndex++;
        return true;
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

    private void updateAnagramScoreWithMarked(int score) {
        if (mScorePopText.isAnimating()) {
            mScorePopText.doublePop(score);
        } else {
            mScorePopText.pop(score);
        }

        mAnagramScore += score;

        updateAnagramScore();
    }

    private void updateAnagramScore() {
        mScoreText.setText(String.format(ScoreFormatText, mAnagramScore));
    }

    private void updateAnagramNb() {
        mAnagramNb.setText(String.format("#%1$d", mNextAnagramIndex + 1));
    }

    private void markQuizSolved() {
        AnagramQuiz mQuiz = (AnagramQuiz)getQuiz();
        mQuiz.setSolved(true);
    }

    private void stopAnagramInteraction() {
        mTimerHelper.stop();
        mAnagramView.setInteraction(false);
    }

    private void proceedActivity() {
        ((QuizActivity)getContext()).proceed(getQuiz());
    }


    // TODO fix thread issue ontimeout vs solve
    private void decideOnNextAnagram() {
        addTimeSpentBeforeNextAnagram();
        if (!setNextUnsolvedAnagram()) {
            markQuizSolved();

            addTimeSpentBeforeNextAnagram();
            stopAnagramInteraction();
            proceedActivity();
        }

        updateAnagramNb();
        mAnagramView.vanishLetters();
    }

    @Override
    protected View createQuizContentView() {

        init();

        View rootView = getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);


        background = rootView.findViewById(R.id.quiz_background);

        mTimerText = (TextView)rootView.findViewById(R.id.timer_text);
        mTimerDeciText = (TextView)rootView.findViewById(R.id.timer_deci_text);

        mScoreText = (TextView)rootView.findViewById(R.id.anagram_score_text);
        mScorePopText = (ScoreTextView)rootView.findViewById(R.id.anagram_score_poptext);


        mMarkedLetters = (TextView) rootView.findViewById(R.id.marked_letters);

        mAnagramNb = (TextView) rootView.findViewById(R.id.anagram_nb);

        mAnagramView = (AnagramView) rootView.findViewById(R.id.anagram_content);

        // debug
        // getCategory().setSolved(true);
        // getNextAnagram().addTimeSpent(10);
        // getNextAnagram().setSolved(true);
        // setNextAnagram();
        // getNextAnagram().addTimeSpent(1);
        // ((QuizActivity)getContext()).proceed(getCategory());

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
                            updateAnagramScoreWithMarked(markedAnagram.length());
                            
                            getNextAnagram().setSolved(true);

                            decideOnNextAnagram();
                        } else {
                            mAnagramView.shakeMarkedLetters();
                        }
                        mAnagramView.clearMarkedLetters();
                        fadeMarkedLetters();
                    }

                    @Override
                    public void onAnagramVanish() {
                        shuffleAnagram();
                        Anagram nextAnagram = getNextAnagram();
                        nextAnagramWithTransition(nextAnagram);
                    }

                    @Override
                    public void onAnagramPop() {
                    }
                });

        mReloadButton = (ImageButton) rootView.findViewById(R.id.reload_button);
        mReloadButton.setOnTouchListener(new ButtonTouchListener(mReloadButton));
        mReloadButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mAnagramView.vanishLetters();
                    }
                });

        mSkipButton = (ImageButton) rootView.findViewById(R.id.skip_button);
        mSkipButton.setOnTouchListener(new ButtonTouchListener(mSkipButton));
        mSkipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decideOnNextAnagram();
                    }                    
                });

        reset(getQuiz());

        return rootView;
    }

    public void reset(AnagramQuiz quiz) {
        
        mAnagramScore = 0;
        mNextAnagramIndex = 0;

        setQuiz(quiz);

        shuffleAnagram();

        Anagram nextAnagram = getNextAnagram();
        Log.e("YYY resetquiz", nextAnagram.getQuestion());

        updateAnagramScore();
        updateAnagramNb();

        mAnagramView.setAnagram(nextAnagram);
        mAnagramView.setInteraction(true);

        mTimerHelper.stop();
        mTimerHelper.start();
    }

    public void floatLettersDark() {
        background.setBackground(mReloadButton.getDrawable());
        mAnagramView.floatLettersDark();
    }
}
