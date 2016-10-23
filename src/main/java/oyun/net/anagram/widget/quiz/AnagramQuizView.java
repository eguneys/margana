package oyun.net.anagram.widget.quiz;

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

    @Override
    protected View createQuizContentView() {
        //mAnagramView = new AnagramView(getContext());
        mAnagramView = (AnagramView) getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);
        mAnagramView.setAdapter(new AnagramQuizAdapter(getContext(), getQuiz()));

        return mAnagramView;
    }
    
}
