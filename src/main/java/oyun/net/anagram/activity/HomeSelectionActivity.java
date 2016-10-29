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
import oyun.net.anagram.fragment.HomeSelectionFragment;

public class HomeSelectionActivity extends AppCompatActivity
{

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

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        setSupportActionBar(toolbar);
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
}
