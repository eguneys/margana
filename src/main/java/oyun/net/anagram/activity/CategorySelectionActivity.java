package oyun.net.anagram.activity;

import android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.widget.ImageView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import oyun.net.anagram.R;

import oyun.net.anagram.model.JsonAttributes;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.adapter.CategoryAdapter;

import oyun.net.anagram.fragment.CategorySelectionFragment;

public class CategorySelectionActivity extends AppCompatActivity
{
    private static final int REQUEST_CATEGORY = 0x2300;

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


    private View mNavigateMenu;

    private boolean hasOnCreateCalledBeforeOnResume;

    public static void start(Context context) {
        Intent starter = getStartIntent(context);
        context.startActivity(starter);
    }

    static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, CategorySelectionActivity.class);
        return starter;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        setUpToolbar();

        if (savedInstanceState == null) {
            attachCategoryGridFragment();
        }

        supportPostponeEnterTransition();

        hasOnCreateCalledBeforeOnResume = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!hasOnCreateCalledBeforeOnResume) {
            navigateToolbarLogoTransitionEnter();
        }
        hasOnCreateCalledBeforeOnResume = false;
        startEnterTransition();
    }


    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        navigateBackMenuTransition();

        animateVanishStart(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    // http://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa
                    try {
                        CategorySelectionActivity.super.onBackPressed();
                    } catch (IllegalStateException e) {
                        finish();
                    }
                }
            }, false);
    }

    public void startEnterTransition() {
        final RecyclerView mCategoriesView = (RecyclerView)findViewById(R.id.categories);

        // http://stackoverflow.com/questions/24989218/get-visible-items-in-recyclerview
        final LinearLayoutManager mCategoriesLayout = (LinearLayoutManager)mCategoriesView.getLayoutManager();

        // final int firstIndex = mCategoriesLayout.findFirstVisibleItemPosition();
        // final int lastIndex = mCategoriesLayout.findLastVisibleItemPosition();
        final int firstIndex = 0;
        final int lastIndex = mCategoriesView.getChildCount() - 1;
        int lPivotY;
        int lStartDelay;

        for (int i = firstIndex; i <= lastIndex && i > -1; i++) {
            // RecyclerView.ViewHolder holder = mCategoriesView.findViewHolderForAdapterPosition(i);
            //View view = holder.itemView;
            // View view = mCategoriesLayout.findViewByPosition(i);
            View view = mCategoriesView.getChildAt(i);

            int startDelay = i - firstIndex;

            view.setPivotY(view.getHeight());
            view.setScaleY(0);
            view.animate()
                .scaleY(1)
                .setDuration(CategoryAdapter.ANIM_DURATION)
                .setStartDelay(startDelay * CategoryAdapter.ANIM_DELAY)
                .start();
        }
    }

    // returns the last animator
    private void animateVanishStart(final AnimatorListenerAdapter lastAnimatorListener, boolean directionDown) {
        final RecyclerView mCategoriesView = (RecyclerView)findViewById(R.id.categories);
        // http://stackoverflow.com/questions/24989218/get-visible-items-in-recyclerview
        final LinearLayoutManager mCategoriesLayout = (LinearLayoutManager)mCategoriesView.getLayoutManager();

        // final int firstIndex = mCategoriesLayout.findFirstVisibleItemPosition();
        // final int lastIndex = mCategoriesLayout.findLastVisibleItemPosition();
        final int firstIndex = 0;
        final int lastIndex = mCategoriesView.getChildCount() - 1;
        int lPivotY;
        int lStartDelay;

        for (int i = firstIndex; i <= lastIndex; i++) {
            // RecyclerView.ViewHolder holder = mCategoriesView.findViewHolderForAdapterPosition(i);
            // View v = holder.itemView;

            View v = mCategoriesView.getChildAt(i);

            if (directionDown) {
                lPivotY = v.getHeight();
                lStartDelay = i - firstIndex;
            } else {
                lPivotY = 0;
                lStartDelay = lastIndex - i;
            }

            v.setPivotY(lPivotY);

            final ViewPropertyAnimator animator = v
                .animate()
                .scaleY(0f)
                .setDuration(CategoryAdapter.ANIM_DURATION)
                .setStartDelay(lStartDelay * CategoryAdapter.ANIM_DELAY);

            animator.start();
        }

        mCategoriesView
            .animate()
            .translationY(100 * (directionDown ? 1 : -1))
            .setDuration(CategoryAdapter.ANIM_DURATION)
            .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        animator.removeListener(this);
                        mCategoriesView.setTranslationY(0);
                        lastAnimatorListener.onAnimationEnd(animator);
                    }
                })
            .start();
    }

    private void setUpToolbar() {
        mNavigateMenu = (View) findViewById(R.id.navigate_menu);
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
        mNavigateMenu.setOnClickListener(mOnClickListener);
    }

    private void attachCategoryGridFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.category_container);
        if (!(fragment instanceof CategorySelectionFragment)) {
            fragment = CategorySelectionFragment.newInstance();
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.category_container, fragment)
            .commit();
    }

    public void startQuizActivityWithTransition(final Category category) {
        navigateToolbarLogoTransition();

        animateVanishStart(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    startQuizActivity(category);
                }
            }, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("YYY act result", requestCode + " " + resultCode);
        if (requestCode == REQUEST_CATEGORY && resultCode == R.id.solved) {
            String categoryId = data.getStringExtra(JsonAttributes.ID);

            ((CategorySelectionFragment)getSupportFragmentManager()
             .findFragmentById(R.id.category_container))
                .updateCategories(categoryId);
        }
    }

    private void startQuizActivity(Category category) {
        final Bundle transitionBundle = new Bundle();
        Intent startIntent = QuizActivity.getStartIntent(this, category);
        ActivityCompat.startActivityForResult(this,
                                              startIntent,
                                              REQUEST_CATEGORY,
                                              transitionBundle);
        // disable animation
        overridePendingTransition(0, 0);
    }

    private void navigateBackMenuTransition() {
        mNavigateMenu
            .animate()
            .translationX(-mNavigateMenu.getWidth())
            .setDuration(150)
            .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_menu);
                        mNavigateMenu
                            .animate()
                            .translationX(0)
                            .setDuration(150)
                            .setListener(null)
                            .start();
                    }
                });
    }

    private void navigateToolbarLogoTransition() {
        View logo = findViewById(R.id.logo);

        logo.animate()
            .translationY(-logo.getHeight())
            .start();
    }

    private void navigateToolbarLogoTransitionEnter() {
        View logo = findViewById(R.id.logo);

        logo.setTranslationY(-100);
        logo.animate()
            .translationY(0)
            .start();
    }
}
