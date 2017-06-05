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
import android.view.animation.OvershootInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import android.view.View;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import oyun.net.anagram.R;

import oyun.net.anagram.fragment.HomeSelectionFragment;
import oyun.net.anagram.widget.SettingsDialog;

import oyun.net.anagram.helper.ImageUtils;

import oyun.net.anagram.model.Category;
import oyun.net.anagram.persistence.AnagramDatabaseHelper;

public class HomeSelectionActivity extends AppCompatActivity
{

    private static final int REQUEST_CATEGORY = 0x2300;

    private final Interpolator DialogTranslateInterpolator = new OvershootInterpolator(0.5f);

    private View mNavigateMenu;

    private View mBackgroundContainer;
    private SettingsDialog mDialogContainer;
    private View mMenuDialog;

    private ImageView mBackgroundBlur;

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

        mDialogContainer = (SettingsDialog) findViewById(R.id.dialog_container);
        // mDialogContainer.setOnClickListener(mDialogOnClickListener);
        mDialogContainer.setDialogStateListener(new SettingsDialog.DialogStateListener() {
               @Override
               public void onOpen() {
                   openDialogWithTransition();
               }
               @Override
               public void onClose() {
                   closeDialogWithTransition();
               }
            });

        mBackgroundContainer = findViewById(R.id.background_container);
        mBackgroundContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogContainer.setDialogState(false);
                }
            });
        mNavigateMenu = (View) findViewById(R.id.navigate_menu);
        mNavigateMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogContainer.setDialogState(true);
                }
            });

        mBackgroundBlur = (ImageView) findViewById(R.id.background_blur);
    }

    private void buildBlurBitmap() {
        // mBackgroundContainer.buildDrawingCache();
        // mBackgroundContainer.setDrawingCacheEnabled(true);
        // Bitmap b1 = mBackgroundContainer.getDrawingCache();
        // mBackgroundContainer.setDrawingCacheEnabled(false);

        Bitmap b1 = ImageUtils.loadBitmapFromView(mBackgroundContainer);

        // Bitmap b = b1.copy(Bitmap.Config.ARGB_8888, false);
        Bitmap b = ImageUtils.blurBitmap(this, b1);
        BitmapDrawable d = new BitmapDrawable(b);
        mBackgroundBlur.setBackgroundDrawable(d);
        // mBackgroundContainer.destroyDrawingCache();
    }

    private int getDialogTranslationY() {
        return - mBackgroundContainer.getHeight() / 2
            - mMenuDialog.getHeight () / 2;
    }

    private void closeDialogWithTransition() {
        mBackgroundBlur
            .animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(0)
            .start();

        mBackgroundContainer
            .animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1)
            .start();

        mMenuDialog
            .animate()
            .translationY(getDialogTranslationY())
            .setInterpolator(DialogTranslateInterpolator)
            // .alpha(0)
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
        buildBlurBitmap();

        mBackgroundBlur
            .animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .alpha(1)
            .start();

        mDialogContainer.setVisibility(View.VISIBLE);

        mBackgroundContainer
            .animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .alpha(0)
            .start();

        mMenuDialog.setTranslationY(getDialogTranslationY());
        // mMenuDialog.setAlpha(0);
        mMenuDialog
            .animate()
            .translationY(0)
            .alpha(1)
            .setInterpolator(DialogTranslateInterpolator)
            .start();
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

