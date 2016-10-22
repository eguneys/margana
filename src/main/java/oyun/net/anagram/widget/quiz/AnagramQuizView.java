package oyun.net.anagram.widget.quiz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;

public class AnagramQuizView extends AbsQuizView<AnagramQuiz> {

    public AnagramQuizView(Context context, Category category, AnagramQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        final View container = getLayoutInflater()
            .inflate(R.layout.quiz_anagram_layout, this, false);
        return container;
    }
    
}
