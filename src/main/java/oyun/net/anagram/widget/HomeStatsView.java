package oyun.net.anagram.widget;

import android.util.Log;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import android.graphics.Canvas;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;

import oyun.net.anagram.widget.StarView;

import oyun.net.anagram.model.Profile;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class HomeStatsView extends LinearLayout {

    private StarView mStarView;
    private TextView mNbSolvedText;

    public HomeStatsView(Context context) {
        super(context);
        init();
    }

    public HomeStatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeStatsView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_home_stats, this, true);


        mNbSolvedText = (TextView) findViewById(R.id.solved_words_text);

        mStarView = (StarView) findViewById(R.id.star_view);
        mStarView.setTextSize(R.dimen.quadz_text_size);
        mStarView.setTextColor(R.color.text_light);

        initProfile();
        updateStats();
    }

    private void initProfile() {
        // mProfile = AnagramDatabaseHelper.getProfileFromCategories(getContext(), false);
    }

    public void updateStats() {
        // no cache
        Profile mProfile = AnagramDatabaseHelper.getProfileFromCategories(getContext(), false);

        int nbSolvedWords = mProfile.getSolvedWords();
        int nbStars = mProfile.getStars();

        String nbSolvedTextString = getContext().getString(R.string.nbSolved, nbSolvedWords);

        mStarView.setNbStar(nbStars);
        mNbSolvedText.setText(nbSolvedTextString);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
