package oyun.net.anagram.widget;

import android.util.Log;
import android.util.AttributeSet;

import android.net.Uri;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import oyun.net.anagram.R;
import oyun.net.anagram.helper.ButtonTouchListener;

public class SettingsDialog extends FrameLayout {

    private boolean mIsLanguageOpen;
    private boolean mIsDialogOpen;

    private DialogStateListener mDialogStateListener;

    private Context mContext;

    private ViewGroup mHomeContainer;

    private View mRateButton;
    private View mFeedbackButton;

    private TextView mHeaderText;
    private TextView mLanguageText;

    private View mOverlayBackground;

    private final View.OnClickListener mDialogOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                case R.id.dialog_close:
                    if (!setLanguageState(false))
                        setDialogState(false);
                    break;
                case R.id.language_option_item:
                    setLanguageState(true);
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

    public SettingsDialog(Context context) {
        super(context);
        init();
    }

    public SettingsDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsDialog(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init();
    }

    private void init() {
        mContext = getContext();

        LayoutInflater.from(getContext()).inflate(R.layout.view_settings_dialog, this, true);

        findViewById(R.id.dialog_close).setOnClickListener(mDialogOnClickListener);
        findViewById(R.id.language_option_item).setOnClickListener(mDialogOnClickListener);

        mOverlayBackground = findViewById(R.id.dialog_overlay);

        mHomeContainer = (ViewGroup) findViewById(R.id.home_settings_container);

        mHeaderText = (TextView) findViewById(R.id.header_text);
        mLanguageText = (TextView) findViewById(R.id.language_text);

        mRateButton = (View) findViewById(R.id.rate_button);
        mRateButton.setOnTouchListener(new ButtonTouchListener(mRateButton));
        mRateButton.setOnClickListener(mDialogOnClickListener);

        mFeedbackButton = (View) findViewById(R.id.feedback_button);
        mFeedbackButton.setOnTouchListener(new ButtonTouchListener(mFeedbackButton));
        mFeedbackButton.setOnClickListener(mDialogOnClickListener);

        onOpened();
    }

    public void onOpened() {
        mIsLanguageOpen = false;

        mHeaderText.setAlpha(1);
        mLanguageText.setAlpha(0);
        mHomeContainer.setAlpha(1);

        mOverlayBackground
            .animate()
            .alpha(0.3f)
            .start();
    }

    public void onClosed() {
        mOverlayBackground
            .animate()
            .alpha(0f)
            .start();        
    }

    private void onSendFeedback() {
        Intent contactIntent = new Intent(Intent.ACTION_SENDTO,
                                          Uri.fromParts("mailto",
                                                        mContext.getString(R.string.email_to),
                                                        null));
        contactIntent.putExtra(Intent.EXTRA_SUBJECT,
                               mContext.getString(R.string.email_subject));

        mContext.startActivity(Intent.createChooser(contactIntent,
                                           mContext.getString(R.string.email_chooser)));
    }

    private void onRateApp() {
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(myAppLinkToMarket);
    }

    private void transitionToLanguageState() {
        mLanguageText
            .animate()
            .alpha(1);
        mHeaderText
            .animate()
            .alpha(0);

        mHomeContainer
            .animate()
            .alpha(0);
    }
    private void transitionFromLanguageState() {
        mLanguageText
            .animate()
            .alpha(0);
        mHeaderText
            .animate()
            .alpha(1);

        mHomeContainer
            .animate()
            .alpha(1);
    }

    private boolean setLanguageState(boolean isOpen) {
        if (mIsLanguageOpen != isOpen) {
            mIsLanguageOpen = isOpen;

            if (mIsLanguageOpen) {
                transitionToLanguageState();
            } else {
                transitionFromLanguageState();
            }
            return true;
        }
        return false;
    }

    public void setDialogState(boolean isOpen) {
        if (mIsDialogOpen != isOpen) {
            mIsDialogOpen = isOpen;

            if (mIsDialogOpen) {
                // openDialogWithTransition();
                onOpened();
                this.mDialogStateListener.onOpen();
            } else {
                // closeDialogWithTransition();
                onClosed();
                this.mDialogStateListener.onClose();
            }
        }
    }

    public void setDialogStateListener(DialogStateListener listener) {
        this.mDialogStateListener = listener;
    }

    public interface DialogStateListener {
        public void onOpen();
        public void onClose();
    }

}
