package oyun.net.anagram.widget.quiz;

import android.util.Log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramQuizAdapter;
import oyun.net.anagram.widget.AnagramView;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    private AnagramView mAnagramView;

    public AnagramQuizView(Context context, Category category, AnagramQuiz quiz) {
        super(context, category, quiz);
    }

    private boolean checkIfSolved(String markedAnagram) {
        return markedAnagram.toLowerCase()
            .equals(getQuiz().getAnswer().toLowerCase());
    }

    @Override
    protected View createQuizContentView() {
        //mAnagramView = new AnagramView(getContext());
        mAnagramView = (AnagramView) getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);
        mAnagramView.setAdapter(new AnagramQuizAdapter(getContext(), getQuiz()));
        mAnagramView.setAnagramListener(new AnagramView.AnagramListener() {
                @Override
                public void onAnagramMarked(String markedAnagram) {
                    boolean vanishIfSolved = checkIfSolved(markedAnagram);
                    mAnagramView.withShakeOrVanishClearMarkedLetters(vanishIfSolved);
                }

                @Override
                public void onAnagramVanish() {
                }

                @Override
                public void onAnagramPop() {
                }
            });
        return mAnagramView;
    }
    
}
