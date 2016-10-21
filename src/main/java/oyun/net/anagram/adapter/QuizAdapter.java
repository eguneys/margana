package oyun.net.anagram.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.widget.quiz.AbsQuizView;
import oyun.net.anagram.widget.quiz.AnagramQuizView;

public class QuizAdapter extends BaseAdapter {

    private final List<Quiz> mQuizzes;
    private final Context mContext;
    private final Category mCategory;

    public QuizAdapter(Context context, Category category) {
        mContext = context;
        mCategory = category;
        mQuizzes = category.getQuizzes();
    }

    @Override
    public int getCount() {
        return mQuizzes.size();
    }

    @Override
    public Quiz getItem(int position) {
        return mQuizzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mQuizzes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Quiz quiz = getItem(position);
        if (convertView instanceof AbsQuizView) {
            if (((AbsQuizView) convertView).getQuiz().equals(quiz)) {
                return convertView;
            }
        }
        convertView = getViewInternal(quiz);
        return convertView;
    }

    private AbsQuizView getViewInternal(Quiz quiz) {
        if (null == quiz) {
            throw new IllegalArgumentException("Quiz mustn't be null.");
        }
        return createViewFor(quiz);
    }

    private AbsQuizView createViewFor(Quiz quiz) {
        return new AnagramQuizView(mContext, mCategory, (AnagramQuiz) quiz);
    }
}
