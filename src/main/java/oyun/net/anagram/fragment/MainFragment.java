package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oyun.net.anagram.R;
import oyun.net.anagram.activity.HomeSelectionActivity;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class MainFragment extends Fragment
{
    private FloatingActionButton mDoneFab;

    public static MainFragment newInstance(boolean thing) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // if (savedInstanceState == null) {
        //     getSupportFragmentManager().beginTransaction()
        //         .replace(R.id.main_container, MainFragment.newInstance(false))
        //         .commit();
        // }
        new InitDbTask().execute();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.fragment_main, container, false);

        // contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
        //         @Override
        //         public void onLayoutChange(View v, int left, int top, int right, int bottom,
        //                                    int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //             v.removeOnLayoutChangeListener(this);
        //             setUpGridView(getView());
        //         }
        //     });
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initContentViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initContentViews(View view) {
        mDoneFab = (FloatingActionButton) view.findViewById(R.id.done);
        mDoneFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDoneFab(new Runnable() {
                            @Override public void run() {
                                performSignInWithTransition();
                            }
                        });
                }
            });
    }

    private void removeDoneFab(Runnable endAction) {
        ViewCompat.animate(mDoneFab)
            .scaleX(0)
            .scaleY(0)
            .setInterpolator(new FastOutSlowInInterpolator())
            .withEndAction(endAction)
            .start();
    }

    private void performSignInWithTransition() {
        final Activity activity = getActivity();

        // Don't run a transition if v is null
        HomeSelectionActivity.start(activity);
        activity.finish();
        return;
    }

    private class InitDbTask extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            AnagramDatabaseHelper.initDb(getActivity());
            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            performSignInWithTransition();
        }
    }
}
