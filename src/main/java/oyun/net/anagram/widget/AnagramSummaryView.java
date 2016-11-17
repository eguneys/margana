package oyun.net.anagram.widget;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.util.AttributeSet;

import android.content.Context;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;

import android.graphics.Rect;
import android.graphics.Canvas;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;

import oyun.net.anagram.activity.QuizActivity;

import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.adapter.AnagramSummaryAdapter;
import oyun.net.anagram.helper.ResourceUtil;

import oyun.net.anagram.widget.drawable.LinesDrawable;;

public class AnagramSummaryView extends RelativeLayout {


    private long CongratzScaleAnimationDuration = 400;
    private long CongratzTranslateAnimationDuration = 600;

    private long StarScaleAnimationDuration = 400;
    private long StarTranslateAnimationDuration = 600;

    private Interpolator CongratzScaleInterpolator = new OvershootInterpolator();
    private Interpolator CongratzTranslateInterpolator = new AccelerateInterpolator();

    private Interpolator StarScaleInterpolator = new AnticipateInterpolator();
    private Interpolator StarTranslateInterpolator = new AccelerateInterpolator();

    private Interpolator LinearInterpolator = new LinearInterpolator();

    private AnimationListener mAnimationListener;

    private LinesDrawable mLinesDrawable;

    private ImageButton mReplayButton;

    private ImageView mStarIcon;

    private TextView mAnagramScoreSummary;
    private TextView mCongratzText;
    private TextView mAnagramTitle;
    private TextView mAnagramMeaning;

    private ListView mAnagramsList;

    private AnagramSummaryAdapter mAnagramsAdapter;

    private Category mCategory;
    
    public AnagramSummaryView(Context context) {
        super(context);
        init();
    }

    public AnagramSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnagramSummaryView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        int bgColor = ContextCompat.getColor(getContext(), R.color.anagram_primary_dark);

        LayoutInflater.from(getContext()).inflate(R.layout.view_anagram_summary, this, true);

        mReplayButton = (ImageButton) findViewById(R.id.replay_button);

        mReplayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((QuizActivity)getContext()).replayCategory();
                }
            });

        mStarIcon = (ImageView) findViewById(R.id.star_icon);

        mCongratzText = (TextView) findViewById(R.id.congratz_text);

        mAnagramTitle = (TextView) findViewById(R.id.anagram_title);
        mAnagramMeaning = (TextView) findViewById(R.id.anagram_meaning);

        mAnagramScoreSummary = (TextView) findViewById(R.id.anagram_score_summary);

        mAnagramsList = (ListView) findViewById(R.id.anagrams_list);

        mAnagramsList.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent,
                                        View view,
                                        int position,
                                        long id) {
                    
                    selectAnagramAtAdapterPosition(position);
                }
            });

        mLinesDrawable = new LinesDrawable(bgColor);
        setBackground(mLinesDrawable);

        mLinesDrawable.setAnimationListener(new LinesDrawable.AnimationListener() {
                @Override
                public void onAnimationEnd() {
                    if (mAnimationListener != null) {
                        mAnimationListener.onLineAnimationEnd();
                    }
                    animateCongratz();
                }

                @Override
                public void onVanishAnimationEnd() {
                    if (mAnimationListener != null) {
                        mAnimationListener.onVanishEnd();
                    }
                }
            });
    }

    private void updateAnagramSummaryTexts() {
        AnagramQuiz quiz = (AnagramQuiz)mCategory.getRecentQuiz();
        mAnagramScoreSummary.setText(ResourceUtil.getDynamicString(getContext(),
                                                                   R.string.youScored,
                                                                   "" + quiz.getScore()));
        if (quiz.isSolved()) {
            mCongratzText.setVisibility(View.VISIBLE);
        } else {
            // mCongratzText.setVisibility(View.GONE);
        }
    }

    private void selectAnagramAtAdapterPosition(int position) {
        Anagram anagram = mAnagramsAdapter.getItem(position);
        mAnagramTitle.setText(anagram.getAnswer());
        mAnagramMeaning.setText(anagram.getMeaning());
    }

    public void setCategory(Category category) {
        mCategory = category;

        AnagramQuiz recentQuiz = (AnagramQuiz)category.getRecentQuiz();

        List<Anagram> items = recentQuiz.getAnagramsWithTimeSpent();
        mAnagramsAdapter = new AnagramSummaryAdapter(getContext(), items);
        mAnagramsList.setAdapter(mAnagramsAdapter);

        // TODO Fix for no anagram case
        selectAnagramAtAdapterPosition(0);

        updateAnagramSummaryTexts();

        if (recentQuiz.isSolved()) {
            mLinesDrawable.setOrientation(LinesDrawable.HORIZONTAL);
        } else {
            mLinesDrawable.setOrientation(LinesDrawable.VERTICAL);
        }
    }

    public void animateVanish() {
        

        mReplayButton
            .animate()
            .scaleX(0)
            .scaleY(0)
            .setInterpolator(LinearInterpolator)
            .start();

        mAnagramScoreSummary
            .animate()
            .scaleX(0)
            .scaleY(0)
            .setInterpolator(LinearInterpolator)
            .start();

        mAnagramsList
            .animate()
            .alpha(0f)
            .scaleY(0)
            .start();

        mAnagramTitle.setAlpha(0);
        mAnagramMeaning.setAlpha(0);

        mCongratzText
            .animate()
            .scaleX(0)
            .scaleY(0)
            .setInterpolator(LinearInterpolator)
            .start();

        mStarIcon
            .animate()
            .scaleX(0)
            .scaleY(0)
            .setInterpolator(LinearInterpolator)
            .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mCongratzText.animate().setListener(null);
                        mLinesDrawable.animateLinesReverse();
                    }
                })
            .start();
    }

    public void animateSummary() {
        float parentCenterX = this.getX() + this.getWidth() / 2;
        float parentCenterY = this.getY() + this.getHeight() / 2;

        float iconMargin = 30;

        mStarIcon.setScaleX(4);
        mStarIcon.setScaleY(4);

        mStarIcon
            .setTranslationX(parentCenterX - mStarIcon.getWidth() / 2 - mStarIcon.getX());
        mStarIcon
            .setTranslationY(parentCenterY - mStarIcon.getHeight() / 2 - mStarIcon.getY());

        mCongratzText.setScaleX(0);
        mCongratzText.setScaleY(0);

        // mCongratzText
        //     .setTranslationY(parentCenterY - mCongratzText.getHeight() / 2
        //                      + mStarIcon.getHeight()
        //                      + iconMargin);

        mReplayButton.setScaleX(0);
        mReplayButton.setScaleY(0);

        mAnagramScoreSummary.setScaleX(0);
        mAnagramScoreSummary.setScaleY(0);

        mAnagramsList.setPivotY(0);
        mAnagramsList.setAlpha(0);
        mAnagramsList.setScaleY(0);

        mAnagramTitle.setAlpha(0);
        mAnagramMeaning.setAlpha(0);

        mLinesDrawable.animateLines();
    }

    public void animateCongratz() {
        mCongratzText
            .animate()
            .scaleX(1)
            .scaleY(1)
            .translationX(0)
            .translationY(0)
            .setInterpolator(CongratzScaleInterpolator)
            .setDuration(CongratzScaleAnimationDuration)
            .start();

        mStarIcon
            .animate()
            .translationX(0)
            .translationY(0)
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(StarTranslateInterpolator)
            .setDuration(StarTranslateAnimationDuration)
            .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mCongratzText.animate().setListener(null);
                        animateSummaryLayout();
                    }
                })
            .start();
    }

    public void animateSummaryLayout() {
        mReplayButton
            .animate()
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(CongratzScaleInterpolator)
            .setDuration(CongratzScaleAnimationDuration)
            .start();

        mAnagramScoreSummary
            .animate()
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(CongratzScaleInterpolator)
            .setDuration(CongratzScaleAnimationDuration)
            .setStartDelay(CongratzScaleAnimationDuration)
            .start();        

        mAnagramsList
            .animate()
            .alpha(1)
            .scaleY(1)
            .setDuration(CongratzScaleAnimationDuration)
            .start();

        mAnagramTitle
            .animate()
            .alpha(1)
            .start();
        mAnagramMeaning
            .animate()
            .alpha(1)
            .start();
    }

    public void setAnimationListener(AnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    public interface AnimationListener {
        public void onLineAnimationEnd();
        public void onVanishEnd();
    }
}
