package oyun.net.anagram.widget.drawable;

import android.util.Property;
import android.util.Log;

import android.content.res.Resources;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

import android.view.animation.Interpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.BounceInterpolator;
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

import android.support.v4.view.animation.PathInterpolatorCompat;

public class AnagramProgressDrawable extends Drawable
{
    private final int NB_CELLS = 10;

    private final int MarkAnimationDuration = 250;

    private static final float MIN_MARK_SCALE = 0.9f;

    private static final Interpolator MarkInterpolator = new DecelerateInterpolator();

    private final float mDensity = Resources.getSystem().getDisplayMetrics().density;

    private final float DP_LINE_WIDTH = 0.5f;
    private final float PX_LINE_WIDTH = DP_LINE_WIDTH * mDensity;

    private AnimatorSet mMarkAnimatorSet;

    private float mAlphaProgress = 1f;

    private int mColor;

    private Paint mPaint;
    private Paint mAlphaPaint;

    private Drawable mMiddleBarDrawable;
    private Drawable mMiddleBarEmptyDrawable;

    private Bitmap mTempBitmap;
    private Canvas mTempCanvas;

    private final RectF mBounds = new RectF();
    private final Rect mCellBounds = new Rect();

    private float mCellHeight;
    private float mCellWidth;

    private final boolean[] mProgressStates;

    public AnagramProgressDrawable(Drawable middleBar,
                                   Drawable middleBarEmpty) {
        mMiddleBarDrawable = middleBar;
        mMiddleBarEmptyDrawable = middleBarEmpty;

        mProgressStates = new boolean[NB_CELLS];

        mProgressStates[5] = true;
        mProgressStates[6] = true;
        mProgressStates[1] = true;


        initPaints();
        initAnimations();
    }

    private void initPaints() {
        int AFD_FLAG = Paint.ANTI_ALIAS_FLAG |
            Paint.FILTER_BITMAP_FLAG |
            Paint.DITHER_FLAG;
        mAlphaPaint = new Paint(AFD_FLAG);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);

    }

    private void initAnimations() {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int width = Math.abs(bounds.right - bounds.left);
        int height = Math.abs(bounds.top - bounds.bottom);

        super.onBoundsChange(bounds);
        mBounds.left = bounds.left;
        mBounds.right = bounds.right;
        mBounds.top = bounds.top;
        mBounds.bottom = bounds.bottom;

        mCellHeight = mBounds.height();
        mCellWidth = mBounds.width() / NB_CELLS;

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

        for (int i = 0; i < mProgressStates.length; i++) {
            mCellBounds.left =(int) (i * mCellWidth);
            mCellBounds.top = 0;
            mCellBounds.right = (int) (mCellBounds.left + mCellWidth);
            mCellBounds.bottom = (int) (mCellBounds.top + mCellHeight);
            if (mProgressStates[i] == true) {
                mMiddleBarDrawable.setBounds(mCellBounds);
                mMiddleBarDrawable.draw(canvas);
            } else {
                mMiddleBarEmptyDrawable.setBounds(mCellBounds);
                mMiddleBarEmptyDrawable.draw(canvas);
            }
        }

        canvas.drawBitmap(mTempBitmap, 0, 0, mAlphaPaint);
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(ColorFilter cf) {}

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setProgress(int level, boolean mark) {
        if (mProgressStates[level] != mark) {
            mProgressStates[level] = mark;
            invalidateSelf();
        }
    }

    public void setAlphaProgress(float alphaProgress) {
        this.mAlphaProgress = alphaProgress;
        invalidateSelf();
    }

    public float getAlphaProgress() {
        return mAlphaProgress;
    }

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

}
