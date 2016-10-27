package oyun.net.anagram.widget.quiz;

import android.util.Log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramAdapter;
import oyun.net.anagram.widget.AnagramView;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    private int mNextAnagramIndex = 0;
    private AnagramView mAnagramView;

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
    }

    private Anagram withSetGetNextAnagram() {
        setNextAnagram();
        return getNextAnagram();
    }

    @Override
    protected View createQuizContentView() {
        Anagram nextAnagram = getNextAnagram();
        setNextAnagram();

        //mAnagramView = new AnagramView(getContext());
        mAnagramView = (AnagramView) getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);

        mAnagramView.setAdapter(new AnagramAdapter(getContext(), nextAnagram));

        mAnagramView.setAnagramListener(new AnagramView.AnagramListener() {
                @Override
                public void onAnagramMarked(String markedAnagram) {
                    boolean vanishIfSolved = checkIfSolved(markedAnagram);
                    mAnagramView.withShakeOrVanishClearMarkedLetters(vanishIfSolved);
                }

                @Override
                public void onAnagramVanish() {
                    Anagram nextAnagram = withSetGetNextAnagram();
                    ((AnagramAdapter)mAnagramView.getAdapter()).setAnagram(nextAnagram);
                    mAnagramView.popLetters();
                }

                @Override
                public void onAnagramPop() {
                }
            });
        return mAnagramView;
    }
}
