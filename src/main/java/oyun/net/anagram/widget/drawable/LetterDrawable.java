package oyun.net.anagram.widget.drawable;

import android.util.Property;
import android.util.Log;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.CycleInterpolator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import android.animation.ArgbEvaluator;

public class LetterDrawable extends Drawable
{
    private static final int MAX_VANISH_ROTATE = 8 * 2;
    private static final int MAX_SHAKE_ROTATE = 8;
    private static final int SHADOW_DELTA = 4 * 2;
    private static final int TEXT_SIZE = 50 * 2;
    private static final Interpolator MarkInterpolator = new DecelerateInterpolator();
    private static final Interpolator ShakeInterpolator = new CycleInterpolator(3);
    private static final Interpolator VanishInterpolator = new DecelerateInterpolator();
    private static final Interpolator PopInterpolator = new OvershootInterpolator();

    private AnimationListener mAnimationListener;

    private AnimatorSet mMarkAnimatorSet;
    private AnimatorSet mUnmarkAnimatorSet;
    private AnimatorSet mVanishAnimatorSet;
    private AnimatorSet mPopAnimatorSet;

    private ObjectAnimator mShakeAnimator;

    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private float mBackgroundColorProgress = 0f;
    private float mRotateProgress = 0f;
    private float mScaleProgress = 0f;
    private float mAlphaProgress = 1f;

    private int mColor;
    private int mShadowColor;
    private int mTextColor;
    private int mTextShadow;

    private String mLetter;

    private Paint mPaint;
    private Paint mShadowPaint;
    private Paint mTextPaint;

    private Paint mAlphaPaint;

    private Bitmap mTempBitmap;
    private Canvas mTempCanvas;

    private final RectF mBounds = new RectF();
    private final RectF mShadowBounds = new RectF();

    public LetterDrawable(int bgColor, int shadowColor, int textColor, int textShadow) {
        mColor = bgColor;
        mShadowColor = shadowColor;
        mTextColor = textColor;
        mTextShadow = textShadow;
        initPaints();
        initAnimations();
    }

    private void initPaints() {
        mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG |
                                Paint.FILTER_BITMAP_FLAG |
                                Paint.DITHER_FLAG);
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        updateBackgroundColor();
    }

    private void initAnimations() {
        final int MarkAnimationDuration = 250;
        final int VanishAnimationDuration = 300;
        final int ShakeAnimationDuration = 300;

        mMarkAnimatorSet = new AnimatorSet();
        mMarkAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, BACKGROUND_COLOR_PROGRESS, 1f));
        mMarkAnimatorSet.setDuration(MarkAnimationDuration);
        mMarkAnimatorSet.setInterpolator(MarkInterpolator);

        mUnmarkAnimatorSet = new AnimatorSet();
        mUnmarkAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, BACKGROUND_COLOR_PROGRESS, 0f));
        mUnmarkAnimatorSet.setDuration(MarkAnimationDuration);
        mUnmarkAnimatorSet.setInterpolator(MarkInterpolator);

        mShakeAnimator = ObjectAnimator.ofFloat(this, ROTATE_PROGRESS, 0f, MAX_SHAKE_ROTATE);
        mShakeAnimator.setDuration(ShakeAnimationDuration);
        mShakeAnimator.setInterpolator(ShakeInterpolator);

        mVanishAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(this, SCALE_PROGRESS, 0f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, ALPHA_PROGRESS, 0f);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(this, ROTATE_PROGRESS, MAX_VANISH_ROTATE);

        mVanishAnimatorSet
            .play(rotateAnimator)
            .before(scaleAnimator)
            .with(alphaAnimator);
        mVanishAnimatorSet.setDuration(VanishAnimationDuration);
        mVanishAnimatorSet.setInterpolator(VanishInterpolator);
        mVanishAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onLetterVanish();
                    }
                }
            });
        
        mPopAnimatorSet = new AnimatorSet();
        mPopAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, SCALE_PROGRESS, 1f));
        mPopAnimatorSet.setDuration(VanishAnimationDuration);
        mPopAnimatorSet.setInterpolator(PopInterpolator);
        mPopAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onLetterPop();
                    }
                }
            });

    }

    private void updateBackgroundColor() {
        float colorProgress = mBackgroundColorProgress;
        this.mPaint.setColor((Integer)argbEvaluator
              .evaluate(colorProgress, mColor, mTextColor));
        this.mTextPaint.setColor((Integer)argbEvaluator
              .evaluate(colorProgress, mTextColor, mColor));
        this.mShadowPaint.setColor((Integer)argbEvaluator
              .evaluate(colorProgress, mShadowColor, mTextShadow));
        // this.mPaint.setColor(mColor);
        // this.mShadowPaint.setColor(mShadowColor);
        // this.mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int width = Math.abs(bounds.right - bounds.left);
        int height = Math.abs(bounds.top - bounds.bottom);

        int shadowDelta = SHADOW_DELTA;

        super.onBoundsChange(bounds);
        mBounds.left = bounds.left;
        mBounds.right = bounds.right - shadowDelta / 2;
        mBounds.top = bounds.top;
        mBounds.bottom = bounds.bottom - shadowDelta;

        mShadowBounds.left = bounds.left;
        mShadowBounds.right = bounds.right;
        mShadowBounds.top = bounds.top;
        mShadowBounds.bottom = bounds.bottom;


        // can't create canvas with 0 size
        if (width > 0 && height > 0) {
            mTempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mTempCanvas = new Canvas(mTempBitmap);
        } else {
            mTempCanvas = new Canvas();
        }
    }

    private void setupMode() {
    }

    @Override
    public void draw(Canvas canvas) {

        mTempCanvas.drawOval(mShadowBounds, mShadowPaint);
        mTempCanvas.drawOval(mBounds, mPaint);

        mTempCanvas.drawText(mLetter,
                             mBounds.width() / 2f,
                             mBounds.height() / 2f - ((mTextPaint.descent() + mTextPaint.ascent()) / 2f),
                             mTextPaint);

        canvas.rotate(mRotateProgress, mBounds.width() / 2f, mBounds.height() / 2f);

        canvas.scale(mScaleProgress, mScaleProgress, mBounds.width() / 2f, mBounds.height() / 2f);

        canvas.drawBitmap(mTempBitmap, 0, 0, mAlphaPaint);
    }

    public void setLetter(String letter) {
        mLetter = letter;
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(ColorFilter cf) {}

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setBackgroundColorProgress(float backgroundColorProgress) {
        this.mBackgroundColorProgress = backgroundColorProgress;
        updateBackgroundColor();
        invalidateSelf();
    }
    
    public float getBackgroundColorProgress() {
        return mBackgroundColorProgress;
    }

    public void setRotateProgress(float rotateProgress) {
        this.mRotateProgress = rotateProgress;
        invalidateSelf();
    }

    public float getRotateProgress() {
        return mRotateProgress;
    }

    public void setScaleProgress(float scaleProgress) {
        this.mScaleProgress = scaleProgress;
        invalidateSelf();
    }

    public float getScaleProgress() {
        return mScaleProgress;
    }

    public void setAlphaProgress(float alphaProgress) {
        this.mAlphaProgress = alphaProgress;
        invalidateSelf();
    }

    public float getAlphaProgress() {
        return mAlphaProgress;
    }


    public void animateMark() {
        mMarkAnimatorSet.start();
    }

    public void animateUnmark() {
        mUnmarkAnimatorSet.start();
    }

    public void animateShake() {
        mShakeAnimator.start();
    }

    public void animateVanish() {
        mVanishAnimatorSet.start();
    }

    public void animatePop() {
        mPopAnimatorSet.start();
    }

    public static final Property<LetterDrawable, Float> BACKGROUND_COLOR_PROGRESS =
        new Property<LetterDrawable, Float>(Float.class, "backgroundColorProgress") {
            @Override
            public Float get(LetterDrawable object) {
                return object.getBackgroundColorProgress();
            }

            @Override
            public void set(LetterDrawable object, Float value) {
                object.setBackgroundColorProgress(value);
            }
        };

    public static final Property<LetterDrawable, Float> ROTATE_PROGRESS =
        new Property<LetterDrawable, Float>(Float.class, "rotateProgress") {
            @Override
            public Float get(LetterDrawable object) {
                return object.getRotateProgress();
            }

            @Override
            public void set(LetterDrawable object, Float value) {
                object.setRotateProgress(value);
            }
        };

    public static final Property<LetterDrawable, Float> SCALE_PROGRESS =
        new Property<LetterDrawable, Float>(Float.class, "scaleProgress") {
            @Override
            public Float get(LetterDrawable object) {
                return object.getScaleProgress();
            }

            @Override
            public void set(LetterDrawable object, Float value) {
                object.setScaleProgress(value);
            }
        };

    public static final Property<LetterDrawable, Float> ALPHA_PROGRESS =
        new Property<LetterDrawable, Float>(Float.class, "alphaProgress") {
            @Override
            public Float get(LetterDrawable object) {
                return object.getAlphaProgress();
            }

            @Override
            public void set(LetterDrawable object, Float value) {
                object.setAlphaProgress(value);
            }
        };


    
    public void setAnimationListener(AnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public interface AnimationListener {
        public void onLetterPop();
        public void onLetterVanish();
    }
}
