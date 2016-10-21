package oyun.net.anagram.activity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;

import oyun.net.anagram.R;
import oyun.net.anagram.fragment.QuizFragment;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.helper.ApiLevelHelper;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class QuizActivity extends AppCompatActivity
{

    private static final String TAG = "QuizActivity";
    private static final String IMAGE_CATEGORY = "image_category_";
    private static final String FRAGMENT_TAG = "Quiz";

    private Category mCategory;

    private Interpolator mInterpolator;
    private QuizFragment mQuizFragment;

    private FloatingActionButton mQuizFab;
    private View mToolbarBack;
    private ImageView mIcon;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                case R.id.fab_quiz:
                    startQuizFromClickOn(v);
                    break;
                case R.id.back:
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
    }

    @Override
    protected void onResume() {
        initQuizFragment();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mIcon == null || mQuizFab == null) {
            super.onBackPressed();
            return;
        }

        ViewCompat.animate(mToolbarBack)
            .scaleX(0f)
            .scaleY(0f)
            .alpha(0f)
            .setDuration(100)
            .start();

        ViewCompat.animate(mIcon)
            .scaleX(.7f)
            .scaleY(.7f)
            .alpha(0f)
            .setInterpolator(mInterpolator)
            .start();

        ViewCompat.animate(mQuizFab)
            .scaleX(0f)
            .scaleY(0f)
            .setInterpolator(mInterpolator)
            .setStartDelay(100)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        QuizActivity.super.onBackPressed();
                    }
                })
            .start();
    }

    private void startQuizFromClickOn(final View clickedView) {
        initQuizFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.quiz_fragment_container, mQuizFragment, FRAGMENT_TAG)
            .commit();
        final FrameLayout container = (FrameLayout) findViewById(R.id.quiz_fragment_container);
        revealFragmentContainer(clickedView, container);
        setToolbarElevation(false);
    }

    private void revealFragmentContainer(final View clickedView,
                                         final FrameLayout fragmentContainer) {
        if (ApiLevelHelper.isAtLeastLollipop()) {
            revealFragmentContainerLollipop(clickedView, fragmentContainer);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
            clickedView.setVisibility(View.GONE);
            mIcon.setVisibility(View.GONE);
        }
    }

    private void revealFragmentContainerLollipop(final View clickedView,
                                                 final FrameLayout fragmentContainer) {
        
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
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        setContentView(R.layout.activity_quiz);
        mIcon = (ImageView) findViewById(R.id.icon);
        int resId = getResources().getIdentifier("image_category_play", "drawable",
                                                 getApplicationContext().getPackageName());
        mIcon.setImageResource(resId);
        ViewCompat.animate(mIcon)
            .scaleX(1)
            .scaleY(1)
            .alpha(1)
            .setInterpolator(mInterpolator)
            .setStartDelay(300)
            .start();

        mQuizFab = (FloatingActionButton) findViewById(R.id.fab_quiz);
        mQuizFab.setImageResource(R.drawable.ic_play);
        mQuizFab.show();

        mQuizFab.setOnClickListener(mOnClickListener);
    }

    private void initToolbar() {
        String categoryName = "Play";
        mToolbarBack = findViewById(R.id.back);
        mToolbarBack.setOnClickListener(mOnClickListener);
        TextView titleView = (TextView) findViewById(R.id.category_title);
        titleView.setText(categoryName);
        // titleView.setTextColor(ContextCompat
        // .getColor(this, this.getTheme().getTextPrimaryColor()));
    }
}
