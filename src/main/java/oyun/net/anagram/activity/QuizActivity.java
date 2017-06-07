package oyun.net.anagram.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.graphics.Color;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewAnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;

import oyun.net.anagram.R;
import oyun.net.anagram.fragment.QuizFragment;

import oyun.net.anagram.widget.AnagramSummaryView;

import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.JsonAttributes;
import oyun.net.anagram.model.quiz.AnagramQuiz;


import oyun.net.anagram.helper.ApiLevelHelper;
import oyun.net.anagram.helper.ResourceUtil;
import oyun.net.anagram.helper.ViewUtils;

import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class QuizActivity extends AppCompatActivity
{

    private static final String TAG = "QuizActivity";
    private static final String IMAGE_CATEGORY = "image_category_";
    private static final String FRAGMENT_TAG = "Quiz";

    private Category mCategory;

    private Interpolator mInterpolator;
    private QuizFragment mQuizFragment;

    private AnagramSummaryView mAnagramSummary;

    private Animator mCircularReveal;
    private ObjectAnimator mColorChange;

    private FrameLayout mContainer;
    private FloatingActionButton mQuizFab;
    private View mNavigateMenu;
    private View mLogoContainer;
    private ImageView mIcon;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                case R.id.navigate_menu:
                    onBackPressed();
                    break;
                default:
                    return;
                }
            }
        };


    public static Intent getStartIntent(Context context, Category category) {
        Intent starter = new Intent(context, QuizActivity.class);
        starter.putExtra(Category.TAG, category.getId());
        return starter;
    }


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        String categoryId = getIntent().getStringExtra(Category.TAG);
        mInterpolator = new FastOutSlowInInterpolator();

        super.onCreate(savedInstanceState);
        populate(categoryId);
        startQuizFragmentWithTransition();
    }

    @Override
    protected void onResume() {
        initQuizFragment();
        navigateToolbarLogoTransitionEnter();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        mLogoContainer
            .animate()
            .translationY(-mLogoContainer.getHeight())
            .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // http://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa
                        try {
                            QuizActivity.super.onBackPressed();
                        } catch (IllegalStateException e) {
                            finish();
                        }
                    }
                })
            .start();
    }

    private void startQuizFragmentWithTransition() {
        revealFragmentContainer(mContainer);
        setToolbarElevation(false);
        startQuizFragment();
    }

    private void startQuizFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.quiz_fragment_container, mQuizFragment, FRAGMENT_TAG)
            .commit();
    }

    private void revealFragmentContainer(//final View clickedView,
                                         final FrameLayout fragmentContainer) {
        // if (ApiLevelHelper.isAtLeastLollipop()) {
        //     revealFragmentContainerLollipop(clickedView, fragmentContainer);
        // } else {
        fragmentContainer.setVisibility(View.VISIBLE);
            // clickedView.setVisibility(View.GONE);
            // mIcon.setVisibility(View.GONE);
        // }
    }

    private void revealFragmentContainerLollipop(final View clickedView,
                                                 final FrameLayout fragmentContainer) {
        prepareCircularReveal(clickedView, fragmentContainer);

        ViewCompat.animate(clickedView)
            .scaleX(0)
            .scaleY(0)
            .alpha(0)
            .setInterpolator(mInterpolator)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        fragmentContainer.setVisibility(View.VISIBLE);
                        clickedView.setVisibility(View.GONE);
                    }
                })
            .start();

        fragmentContainer.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(mCircularReveal).with(mColorChange);
        animatorSet.start();
    }

    private void prepareCircularReveal(View startView, FrameLayout targetView) {
        int centerX = (startView.getLeft() + startView.getRight()) / 2;
        // Subtract the start view's height to adjust for relative coordinateson screen.
        int centerY = (startView.getTop() + startView.getBottom()) / 2 - startView.getHeight();
        float endRadius = (float) Math.hypot(centerX, centerY);
        mCircularReveal = ViewAnimationUtils.createCircularReveal(targetView,
                                                                  centerX,
                                                                  centerY,
                                                                  startView.getWidth(),
                                                                  endRadius);
        mCircularReveal.setInterpolator(new FastOutLinearInInterpolator());
        mCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // mIcon.setVisibility(View.GONE);
                    mCircularReveal.removeListener(this);
                }
            });
        // Adding a color animation from the FAB's color to transparent creates a dissolve like
        // effect to the circular reveal.
        int accentColor = ContextCompat.getColor(this, mCategory.getTheme().getAccentColor());
        mColorChange = ObjectAnimator.ofInt(targetView,
                                            ViewUtils.FOREGROUND_COLOR,
                                            accentColor,
                                            Color.TRANSPARENT);
        mColorChange.setEvaluator(new ArgbEvaluator());
        mColorChange.setInterpolator(mInterpolator);
    }


    private void initQuizFragment() {
        if (mQuizFragment != null) {
            return;
        }

        mQuizFragment = QuizFragment.newInstance(mCategory.getId());
        setToolbarElevation(false);
    }

    public void setToolbarElevation(boolean shouldElevate) {
        
    }

    private void populate(String categoryId) {
        if (categoryId == null) {
            Log.w(TAG, "Didn't find a category. Finishing");
            finish();
        }
        mCategory = AnagramDatabaseHelper.getCategoryById(this, categoryId);

        setTheme(mCategory.getTheme().getStyleId());
        initLayout();
        initToolbar(mCategory);

        mContainer = (FrameLayout) findViewById(R.id.quiz_fragment_container);
        mContainer.setBackgroundColor(ContextCompat.
                                      getColor(this, mCategory.getTheme().getWindowBackgroundColor()));
        initQuizFragment();
    }

    private void initLayout() {
        setContentView(R.layout.activity_quiz);

        mAnagramSummary = (AnagramSummaryView) findViewById(R.id.anagram_summary);
        mAnagramSummary.setCategory(mCategory);

        mAnagramSummary.setAnimationListener(new AnagramSummaryView.AnimationListener() {
                @Override
                public void onLineAnimationEnd() {
                    mContainer.setVisibility(View.GONE);
                }

                @Override
                public void onVanishEnd() {
                    mAnagramSummary.setVisibility(View.GONE);
                    mQuizFragment.replay();
                }
            });

        mAnagramSummary
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    private boolean mChanged = false;
                    @Override
                    public void onGlobalLayout() {
                        // TODO FIX THIS
                        // Log.e("YYY global", (mChanged ? "true" : "false") + mAnagramSummary.getVisibility());
                        if (mAnagramSummary.getVisibility() == View.VISIBLE) {
                            if (!mChanged) {
                                mChanged = true;
                                mAnagramSummary.animateSummary();
                            }
                        } else {
                            mChanged = false;
                        }
                    }
                });
    }

    private void initToolbar(Category category) {
        mNavigateMenu = findViewById(R.id.navigate_menu);
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
        mNavigateMenu.setOnClickListener(mOnClickListener);

        final TextView logoText = (TextView) findViewById(R.id.logo_text);

        final ImageView logoImage = (ImageView) findViewById(R.id.logo);
        logoImage.setImageResource(R.drawable.ic_star);
        
        if (mCategory.getName().equals("MIXED")) {
            logoText.setText(getString(R.string.mixed));
        } else {
            logoText.setText(ResourceUtil.getDynamicString(this,
                                                           R.string.nbLetters,
                                                           mCategory.getName()));
        }

        mLogoContainer = findViewById(R.id.logo_container);
    }

    private void navigateToolbarLogoTransitionEnter() {
        mLogoContainer.setTranslationY(-100);
        mLogoContainer
            .animate()
            .translationY(0)
            .start();
    }

    private void setResultSolved() {
        Intent categoryIntent = new Intent();
        categoryIntent.putExtra(JsonAttributes.ID, mCategory.getId());
        setResult(R.id.solved, categoryIntent);
    }

    private void syncQuiz(AnagramQuiz quiz) {
        int stars = quiz.getStars();

        if (quiz.isSolved()) {
            int moreStars = 1;
            quiz.addStars(moreStars);

            String quizId = AnagramDatabaseHelper
                .insertQuiz(this, quiz, mCategory.getId());

            List<Anagram> solved = quiz.markSolvedAnagrams(quizId);
            AnagramDatabaseHelper
                .insertAnagrams(this, solved);

            mCategory.syncInsertQuiz(quiz);

            // TODO find better sync
            // AnagramDatabaseHelper.syncProfileLocalAddStar(moreStars);
            AnagramDatabaseHelper.syncCategoryLocal(mCategory);

            setResultSolved();
        }
    }

    public void proceed(AnagramQuiz quiz) {
        syncQuiz(quiz);

        // mQuizFragment.showSummary();
        Log.e("YYY proceed", "lkajdsf");
        mAnagramSummary.setQuiz(quiz);
        mAnagramSummary.setVisibility(View.VISIBLE);
    }

    public void replayCategory() {
        mContainer.setVisibility(View.VISIBLE);
        mAnagramSummary.animateVanish();
    }
}
