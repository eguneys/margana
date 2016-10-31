package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import oyun.net.anagram.R;
import oyun.net.anagram.activity.LetterSelectionActivity;
import oyun.net.anagram.activity.QuizActivity;

import oyun.net.anagram.model.Category;

public class LetterSelectionFragment extends Fragment
{
    private static final int REQUEST_CATEGORY = 0x2300;
    
    public static LetterSelectionFragment newInstance() {
        LetterSelectionFragment fragment = new LetterSelectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_letter_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupCategories();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupCategories() {
    }

    private void startQuizActivityWithTransition(Activity activity, Category category) {
        ((LetterSelectionActivity)activity).startQuizActivityWithTransition(category);
    }
}
