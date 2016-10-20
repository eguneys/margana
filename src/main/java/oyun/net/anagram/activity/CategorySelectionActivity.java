package oyun.net.anagram.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.view.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import oyun.net.anagram.R;
import oyun.net.anagram.fragment.CategorySelectionFragment;
import oyun.net.anagram.databinding.ActivityCategorySelectionBinding;

public class CategorySelectionActivity extends AppCompatActivity
{

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
        ActivityCategorySelectionBinding binding = DataBindingUtil
            .setContentView(this, R.layout.activity_category_selection);
        setUpToolbar();

        if (savedInstanceState == null) {
            attachCategoryGridFragment();
        }
        setProgressBarVisibility(View.GONE);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        System.out.println(toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progress).setVisibility(visibility);
    }
}
