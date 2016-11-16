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
import android.view.animation.OvershootInterpolator;


import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
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
    private Interpolator CongratzScaleInterpolator = new OvershootInterpolator();

    private AnimationListener mAnimationListener;

    private LinesDrawable mLinesDrawable;

    private ImageButton mReplayButton;

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
                    animateSummaryLayout();
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
        AnagramQuiz quiz = (AnagramQuiz)mCategory.getFirstQuiz();
        mAnagramScoreSummary.setText(ResourceUtil.getDynamicString(getContext(),
                                                                   R.string.youScored,
                                                                   "" + quiz.getScore()));
    }

    private void selectAnagramAtAdapterPosition(int position) {
        Anagram anagram = mAnagramsAdapter.getItem(position);
        mAnagramTitle.setText(anagram.getAnswer());
        mAnagramMeaning.setText(anagram.getMeaning());
    }

    public void setCategory(Category category) {
        mCategory = category;
        List<Anagram> items = ((AnagramQuiz)category.getFirstQuiz()).getAnagramsWithTimeSpent();
        mAnagramsAdapter = new AnagramSummaryAdapter(getContext(), items);
        mAnagramsList.setAdapter(mAnagramsAdapter);
        selectAnagramAtAdapterPosition(0);

        updateAnagramSummaryTexts();

        if (category.isSolved()) {
            mLinesDrawable.setOrientation(LinesDrawable.HORIZONTAL);
        } else {
            mLinesDrawable.setOrientation(LinesDrawable.VERTICAL);
        }
    }

    public void animateVanish() {
        mCongratzText
            .animate()
            .scaleX(0)
            .scaleY(0)
            .start();

        mAnagramsList
            .animate()
            .alpha(0f)
            .scaleY(0)
            .start();

        mAnagramTitle.setAlpha(0);
        mAnagramMeaning.setAlpha(0);

        mLinesDrawable.animateLinesReverse();
    }

    public void animateSummary() {
        mCongratzText.setScaleX(0);
        mCongratzText.setScaleY(0);

        mAnagramsList.setPivotY(0);
        mAnagramsList.setAlpha(0);
        mAnagramsList.setScaleY(0);
        mLinesDrawable.animateLines();

        mAnagramTitle.setAlpha(0);
        mAnagramMeaning.setAlpha(0);
    }

    public void animateSummaryLayout() {

        mCongratzText
            .animate()
            .scaleX(1)
            .scaleY(1)
            .setInterpolator(CongratzScaleInterpolator)
            .setDuration(CongratzScaleAnimationDuration)
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
        public void onVanishEnd();
    }
}
