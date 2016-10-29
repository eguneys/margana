package oyun.net.anagram.fragment;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.view.animation.Interpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.activity.HomeSelectionActivity;
import oyun.net.anagram.activity.QuizActivity;

import oyun.net.anagram.model.Category;

import oyun.net.anagram.helper.ButtonTouchListener;

public class HomeSelectionFragment extends Fragment
{
    private static final int REQUEST_CATEGORY = 0x2300;

    private final Interpolator PlayTransitionInterpolator = new AnticipateInterpolator();
    private final Interpolator PlayTransitionEnterInterpolator = new AnticipateOvershootInterpolator();

    private View mPlayButton;
    
    public static HomeSelectionFragment newInstance() {
        HomeSelectionFragment fragment = new HomeSelectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupButtons(view.findViewById(R.id.buttons));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        animateFragmentEnterTransition();
        super.onResume();
    }

    private void setupButtons(final View view) {
        mPlayButton = (View) view.findViewById(R.id.play_button);
        mPlayButton.setOnTouchListener(new ButtonTouchListener(mPlayButton));
        mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    startPlayActivityWithTransition(activity,
                                                    mPlayButton,
                                                    Category.DEFAULT);
                }
            });
    }

    private void animateFragmentEnterTransition() {
        mPlayButton.setTranslationY(200);
        mPlayButton.setScaleX(0f);
        ViewCompat.animate(mPlayButton)
            .translationY(0)
            .scaleX(1f)
            .setInterpolator(PlayTransitionEnterInterpolator)
            .start();
    }

    private void startPlayActivityWithTransition(final Activity activity, View toolbar, final Category category) {
        ViewCompat.animate(toolbar)
            .translationY(200)
            .scaleX(0)
            .setInterpolator(PlayTransitionInterpolator)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        ViewCompat.animate(view).setListener(null);
                        ((HomeSelectionActivity)activity).startPlayActivity(category);
                    }
                })
            .start();
    }
}
