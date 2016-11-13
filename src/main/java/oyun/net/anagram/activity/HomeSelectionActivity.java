package oyun.net.anagram.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.ImageView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

import oyun.net.anagram.fragment.HomeSelectionFragment;

public class HomeSelectionActivity extends AppCompatActivity
{

    private static final int REQUEST_CATEGORY = 0x2300;

    private View mNavigateMenu;

    public static void start(Context context) {
        Intent starter = getStartIntent(context);
        context.startActivity(starter);
    }

    static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, HomeSelectionActivity.class);
        return starter;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_selection);
        setUpToolbar();

        if (savedInstanceState == null) {
            attachMenuButtons();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_menu);
    }

    private void setUpToolbar() {
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        // setSupportActionBar(toolbar);
        mNavigateMenu = (View) findViewById(R.id.navigate_menu);
    }

    private void attachMenuButtons() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.home_container);
        if (!(fragment instanceof HomeSelectionFragment)) {
            fragment = HomeSelectionFragment.newInstance();
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_container, fragment)
            .commit();
    }

    public void startPlayActivityWithTransition() {
        ViewCompat.animate(mNavigateMenu)
            .translationX(-mNavigateMenu.getWidth())
            .setDuration(150)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        // http://stackoverflow.com/questions/40307283/why-does-running-a-second-viewpropertyanimation-on-a-view-break-the-animation-li
                        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
                        ViewCompat
                            .animate(view)
                            .translationX(0)
                            .setDuration(150)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(View view) {
                                        startPlayActivity();
                                    }
                                })
                            .start();
                    }
                })
            .start();
    }

    private void startPlayActivity() {
        final Bundle transitionBundle = new Bundle();
        Intent startIntent = CategorySelectionActivity.getStartIntent(this);
        ActivityCompat.startActivityForResult(this,
                                              startIntent,
                                              REQUEST_CATEGORY,
                                              transitionBundle);
        // disable animation
        overridePendingTransition(0, 0);
    }


    private class FillDatabaseAsyncTask extends AsyncTask<Void, Void, Void>  {
        @Override
        protected Void doInBackground(Void... params) {
            AnagramDatabaseHelper.getCategories(HomeSelectionActivity.this, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {}
    }
}
