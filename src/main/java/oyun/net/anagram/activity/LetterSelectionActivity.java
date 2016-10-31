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

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;

import oyun.net.anagram.fragment.LetterSelectionFragment;

public class LetterSelectionActivity extends AppCompatActivity
{
    private View mNavigateMenu;

    public static void start(Context context) {
        Intent starter = getStartIntent(context);
        context.startActivity(starter);
    }

    static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, LetterSelectionActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    private void setUpToolbar() {
        mNavigateMenu = (View) findViewById(R.id.navigate_menu);
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
    }

    private void attachCategoryGridFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.category_container);
        if (!(fragment instanceof LetterSelectionFragment)) {
            fragment = LetterSelectionFragment.newInstance();
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.category_container, fragment)
            .commit();
    }

    public void startQuizActivityWithTransition(Category category) {
        startQuizActivity(category);
    }

    private void startQuizActivity(Category category) {
        final Bundle transitionBundle = new Bundle();
        Intent startIntent = QuizActivity.getStartIntent(this, category);
        ActivityCompat.startActivity(this,
                                     startIntent,
                                     transitionBundle);
        // disable animation
        overridePendingTransition(0, 0);        
    }
}
