package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oyun.net.anagram.R;

public class QuizFragment extends Fragment
{
    private FloatingActionButton mDoneFab;

    public static QuizFragment newInstance() {
        QuizFragment fragment = new QuizFragment();
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
        super.onViewCreated(view, savedInstanceState);
    }
}
