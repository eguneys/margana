package oyun.net.anagram.activity;

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

import oyun.net.anagram.model.Category;
import oyun.net.anagram.helper.ApiLevelHelper;
import oyun.net.anagram.helper.ViewUtils;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

import oyun.net.anagram.widget.AnagramSummaryView;

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
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        if (mIcon == null || mQuizFab == null) {
            super.onBackPressed();
            return;
        }

        // ViewCompat.animate(mNavigateMenu)
        //     .scaleX(0f)
        //     .scaleY(0f)
        //     .alpha(0f)
        //     .setDuration(100)
        //     .start();

        // ViewCompat.animate(mIcon)
        //     .scaleX(.7f)
        //     .scaleY(.7f)
        //     .alpha(0f)
        //     .setInterpolator(mInterpolator)
        //     .start();

        // ViewCompat.animate(mQuizFab)
        //     .scaleX(0f)
        //     .scaleY(0f)
        //     .setInterpolator(mInterpolator)
        //     .setStartDelay(100)
        //     .setListener(new ViewPropertyAnimatorListenerAdapter() {
        //             @Override
        //             public void onAnimationEnd(View view) {
        //                 QuizActivity.super.onBackPressed();
        //             }
        //         })
        //     .start();
        super.onBackPressed();
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
        mCategory = AnagramDatabaseHelper.getCategoryWith(this, categoryId);
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

        // mIcon = (ImageView) findViewById(R.id.icon);
        // int resId = getResources().getIdentifier("image_category_play", "drawable",
        //                                          getApplicationContext().getPackageName());
        // mIcon.setImageResource(resId);
        // ViewCompat.animate(mIcon)
        //     .scaleX(1)
        //     .scaleY(1)
        //     .alpha(1)
        //     .setInterpolator(mInterpolator)
        //     .setStartDelay(300)
        //     .start();

        // mQuizFab = (FloatingActionButton) findViewById(R.id.fab_quiz);
        // mQuizFab.setImageResource(R.drawable.ic_play);
        // mQuizFab.show();

        // mQuizFab.setOnClickListener(mOnClickListener);
    }

    private void initToolbar(Category category) {
        mNavigateMenu = findViewById(R.id.navigate_menu);
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
        mNavigateMenu.setOnClickListener(mOnClickListener);
    }

    public void proceed() {
        // mQuizFragment.showSummary();
        mAnagramSummary.setVisibility(View.VISIBLE);
        mAnagramSummary.animateSummary();
    }
}
