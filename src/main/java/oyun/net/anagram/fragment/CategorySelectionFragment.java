package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import android.support.design.widget.FloatingActionButton;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import oyun.net.anagram.R;
import oyun.net.anagram.activity.CategorySelectionActivity;
import oyun.net.anagram.activity.QuizActivity;

import oyun.net.anagram.model.Category;

public class CategorySelectionFragment extends Fragment
{
    private static final int REQUEST_CATEGORY = 0x2300;
    
    private FloatingActionButton mDoneFab;

    public static CategorySelectionFragment newInstance() {
        CategorySelectionFragment fragment = new CategorySelectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupCategories(view.findViewById(R.id.categories));
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupCategories(final View view) {
        view.getViewTreeObserver()
            .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
