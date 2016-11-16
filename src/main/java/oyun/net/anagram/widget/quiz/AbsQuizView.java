package oyun.net.anagram.widget.quiz;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.FrameLayout;

import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;

public abstract class AbsQuizView<Q extends Quiz> extends FrameLayout {

    private final int mSpacingDouble;

    private final Interpolator mLinearOutSlowInInterpolator;
    private final LayoutInflater mLayoutInflater;
    private final Category mCategory;
    private Q mQuiz;

    public AbsQuizView(Context context, Category category, Q quiz) {
        super(context);

        mQuiz = quiz;
        mCategory = category;
        mLayoutInflater = LayoutInflater.from(context);

        mSpacingDouble = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        mLinearOutSlowInInterpolator = new LinearOutSlowInInterpolator();

        setId(quiz.getId());
        setUpQuestionView();
        LinearLayout container = createContainerLayout(context);
        View quizContentView = getInitializedContentView();
        addContentView(container, quizContentView);
    }

    private void setUpQuestionView() {
    }


    private LinearLayout createContainerLayout(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setId(R.id.absQuizViewContainer);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private View getInitializedContentView() {
        View quizContentView = createQuizContentView();
        quizContentView.setId(R.id.quiz_content);
        setDefaultPadding(quizContentView);
        setMinHeightInternal(quizContentView);

        return quizContentView;
    }

    private void addContentView(LinearLayout container, View quizContentView) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                                                     LayoutParams.WRAP_CONTENT);
        // container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
    }

    private void setDefaultPadding(View view) {
        view.setPadding(mSpacingDouble, mSpacingDouble, mSpacingDouble, mSpacingDouble);
    }

    private void setMinHeightInternal(View view) {
        view.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.min_height_question));
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    protected abstract View createQuizContentView();

    public Category getCategory() {
        return mCategory;
    }

    public Q getQuiz() {
        return mQuiz;
    }

    public void setQuiz(Q quiz) {
        mQuiz = quiz;
    }
}
