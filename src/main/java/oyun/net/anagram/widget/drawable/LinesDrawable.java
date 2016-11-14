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

public class LinesDrawable extends Drawable
{
    private final int LinesAnimationDuration = 600;

    private static final Interpolator LinesInterpolator = new DecelerateInterpolator();

    private final float mDensity = Resources.getSystem().getDisplayMetrics().density;

    private final float DP_LINES_WIDTH = 30;
    private final float PX_LINES_WIDTH = DP_LINES_WIDTH * mDensity;

    private AnimationListener mAnimationListener;

    private AnimatorSet mLinesAnimatorSet;

    private float mLinesProgress = 0f;
    private float mAlphaProgress = 1f;

    private int mNbLines;
    private float[] mLinePoints;
    private float[] mLinePointsProgress;

    private int mColor;

    private Paint mPaint;
    private Paint mAlphaPaint;

    private Bitmap mTempBitmap;
    private Canvas mTempCanvas;

    private final RectF mBounds = new RectF();

    public LinesDrawable(int bgColor) {
        mColor = bgColor;

        initPaints();
        initAnimations();
    }

    private void initPaints() {
        int AFD_FLAG = Paint.ANTI_ALIAS_FLAG |
            Paint.FILTER_BITMAP_FLAG |
            Paint.DITHER_FLAG;
        mAlphaPaint = new Paint(AFD_FLAG);
        
        mPaint = new Paint(AFD_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);

        mPaint.setStrokeWidth(PX_LINES_WIDTH);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initAnimations() {
        mLinesAnimatorSet = new AnimatorSet();
        mLinesAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, LINES_PROGRESS, 1f));
        mLinesAnimatorSet.setDuration(LinesAnimationDuration);
        mLinesAnimatorSet.setInterpolator(LinesInterpolator);
        mLinesAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationEnd();
                    }
                }
            });
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

        mNbLines = (int) (height / PX_LINES_WIDTH);

        // can't create canvas with 0 size
        if (width > 0 && height > 0) {
            mTempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mTempCanvas = new Canvas(mTempBitmap);
            initLinePoints();
        } else {
            mTempCanvas = new Canvas();
            mLinePoints = new float[0];
        }
    }

    private void setupMode() {
    }

    private void initLinePoints() {
        float height = mBounds.height();
        mLinePoints = new float[mNbLines * 4];
        mLinePointsProgress = new float[mNbLines * 4];

        for (int i = 0; i < mNbLines; i++) {

            boolean odd = i % 2 == 0;

            float x0 = i * PX_LINES_WIDTH;
            float x1 = i * PX_LINES_WIDTH;
            float yTop = i * 0;
            float yMiddle = height / 2f;
            float yBottom = height;

            mLinePoints[i * 4 + 0] = x0;
            mLinePoints[i * 4 + 1] = odd ? yTop : yBottom;
            mLinePoints[i * 4 + 2] = x1;
            mLinePoints[i * 4 + 3] = odd ? yBottom : yTop;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mTempCanvas.drawLines(mLinePointsProgress, mPaint);

        // canvas.rotate(30, mBounds.width() / 2f, mBounds.height() / 2f);

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

    public void setLinesProgress(float linesProgress) {
        this.mLinesProgress = linesProgress;
        updateLinePoints();
        invalidateSelf();
    }

    public float getLinesProgress() {
        return mLinesProgress;
    }

    public boolean isAnimating() {
        return mLinesAnimatorSet.isRunning();
    }

    public void animateLines() {
        mLinesAnimatorSet.start();
    }

    private void updateLinePoints() {
        for (int i = 0; i < mNbLines; i++) {
            float x0 = mLinePoints[i * 4 + 0];
            float y0 = mLinePoints[i * 4 + 1];
            float x1 = mLinePoints[i * 4 + 2];
            float y1 = mLinePoints[i * 4 + 3];

            mLinePointsProgress[i * 4 + 0] = x0;
            mLinePointsProgress[i * 4 + 1] = y0;
            mLinePointsProgress[i * 4 + 2] = x1;
            mLinePointsProgress[i * 4 + 3] = y0 + (y1 - y0) * mLinesProgress;
        }        
    }

    
    public static final Property<LinesDrawable, Float> LINES_PROGRESS =
        new Property<LinesDrawable, Float>(Float.class, "linesProgress") {
            @Override
            public Float get(LinesDrawable object) {
                return object.getLinesProgress();
            }

            @Override
            public void set(LinesDrawable object, Float value) {
                object.setLinesProgress(value);
            }
        };

    public void setAnimationListener(AnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public interface AnimationListener {
        public void onAnimationEnd();
    }
}
