package oyun.net.anagram.widget.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;

public abstract class AbsQuizView<Q extends Quiz> extends FrameLayout {

    private final LayoutInflater mLayoutInflater;
    private final Category mCategory;
    private final Q mQuiz;

    public AbsQuizView(Context context, Category category, Q quiz) {
        super(context);

        mQuiz = quiz;
        mCategory = category;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Q getQuiz() {
        return mQuiz;
    }
}
