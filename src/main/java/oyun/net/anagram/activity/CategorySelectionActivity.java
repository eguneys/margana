package oyun.net.anagram.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import oyun.net.anagram.R;
import oyun.net.anagram.fragment.MainFragment;

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
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance(false))
                .commit();
        }
    }
}
