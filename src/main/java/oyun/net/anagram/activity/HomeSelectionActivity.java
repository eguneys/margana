package oyun.net.anagram.activity;

import android.app.Activity;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.AsyncTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.view.animation.Interpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import android.view.View;
import android.widget.ImageView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import oyun.net.anagram.R;

import oyun.net.anagram.fragment.HomeSelectionFragment;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;
import oyun.net.anagram.helper.ButtonTouchListener;


public class HomeSelectionActivity extends AppCompatActivity
{

    private static final int REQUEST_CATEGORY = 0x2300;

    private final Interpolator DialogTranslateInterpolator = new AnticipateOvershootInterpolator();

    private View mNavigateMenu;

    private View mBackgroundContainer;
    private View mDialogContainer;
    private View mMenuDialog;

    private View mRateButton;
    private View mFeedbackButton;

    private boolean mIsDialogOpen;


    private final View.OnClickListener mDialogOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                case R.id.navigate_menu:
                    setDialogState(true);
                    break;
                case R.id.dialog_container:
                case R.id.dialog_close:
                    setDialogState(false);
                    break;
                case R.id.feedback_button:
                    onSendFeedback();
                    break;
                case R.id.rate_button:
                    onRateApp();
                    break;
                default:
                }
            }
        };

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

    @Override
    public void onResume() {
        super.onResume();
        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_menu);
    }

    private void setUpToolbar() {
        mMenuDialog = findViewById(R.id.menu_dialog);

        mMenuDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {}
            });

        mDialogContainer = findViewById(R.id.dialog_container);
        mDialogContainer.setOnClickListener(mDialogOnClickListener);

        findViewById(R.id.dialog_close).setOnClickListener(mDialogOnClickListener);

        mBackgroundContainer = findViewById(R.id.background_container);
        mNavigateMenu = (View) findViewById(R.id.navigate_menu);
        mNavigateMenu.setOnClickListener(mDialogOnClickListener);

        mRateButton = (View) findViewById(R.id.rate_button);
        mRateButton.setOnTouchListener(new ButtonTouchListener(mRateButton));
        mRateButton.setOnClickListener(mDialogOnClickListener);

        mFeedbackButton = (View) findViewById(R.id.feedback_button);
        mFeedbackButton.setOnTouchListener(new ButtonTouchListener(mFeedbackButton));
        mFeedbackButton.setOnClickListener(mDialogOnClickListener);

    }

    private void setDialogState(boolean isOpen) {
        if (mIsDialogOpen != isOpen) {
            mIsDialogOpen = isOpen;

            if (mIsDialogOpen) {
                openDialogWithTransition();
            } else {
                closeDialogWithTransition();
            }
        }
    }

    private void closeDialogWithTransition() {
        mBackgroundContainer
            .animate()
            .scaleX(1f)
            .scaleY(1f)
            .start();

        mMenuDialog
            .animate()
            .translationY(-100)
            .alpha(0)
            .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mMenuDialog.animate().setListener(null);
                        mDialogContainer.setVisibility(View.GONE);
                    }
                })
            .start();
    }

    private void openDialogWithTransition() {
        mDialogContainer.setVisibility(View.VISIBLE);
        mBackgroundContainer
            .animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .start();

        mMenuDialog.setTranslationY(-100);
        mMenuDialog.setAlpha(0);
        mMenuDialog
            .animate()
            .translationY(0)
            .alpha(1)
            .setInterpolator(DialogTranslateInterpolator)
            .start();
    }

    private void onSendFeedback() {
        Intent contactIntent = new Intent(Intent.ACTION_SENDTO,
                                          Uri.fromParts("mailto",
                                                        getString(R.string.email_to),
                                                        null));
        contactIntent.putExtra(Intent.EXTRA_SUBJECT,
                               getString(R.string.email_subject));

        startActivity(Intent.createChooser(contactIntent,
                                           getString(R.string.email_chooser)));
    }

    private void onRateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(myAppLinkToMarket);
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

    public void startPlayActivityWithTransition() {
        ViewCompat.animate(mNavigateMenu)
            .translationX(-mNavigateMenu.getWidth())
            .setDuration(150)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        // http://stackoverflow.com/questions/40307283/why-does-running-a-second-viewpropertyanimation-on-a-view-break-the-animation-li
                        ((ImageView)mNavigateMenu).setImageResource(R.drawable.ic_arrow_back);
                        ViewCompat
                            .animate(view)
                            .translationX(0)
                            .setDuration(150)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(View view) {
                                        startPlayActivity();
                                    }
                                })
                            .start();
                    }
                })
            .start();
    }

    private void startPlayActivity() {
        final Bundle transitionBundle = new Bundle();
        Intent startIntent = CategorySelectionActivity.getStartIntent(this);
        ActivityCompat.startActivityForResult(this,
                                              startIntent,
                                              REQUEST_CATEGORY,
                                              transitionBundle);
        // disable animation
        overridePendingTransition(0, 0);
    }
}
