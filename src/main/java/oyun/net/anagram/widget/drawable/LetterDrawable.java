package oyun.net.anagram.widget.drawable;

import android.util.Property;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PixelFormat;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import android.animation.ArgbEvaluator;

public class LetterDrawable extends Drawable
{
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private float mBackgroundColorProgress = 0f;

    private int mColor;
    private int mTextColor;

    private Paint mPaint;
    private Paint mTextPaint;

    private Paint mAlphaPaint;

    private Bitmap mTempBitmap;
    private Canvas mTempCanvas;

    private final RectF mBounds = new RectF();

    public LetterDrawable(int bgColor, int textColor) {
        mColor = bgColor;
        mTextColor = textColor;
        initPaints();
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG |
                                Paint.FILTER_BITMAP_FLAG |
                                Paint.DITHER_FLAG);
        

        mPaint.setStyle(Paint.Style.FILL);
        // mMaskPaint.setXfermode(new Por
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        float colorProgress = mBackgroundColorProgress;
        this.mPaint.setColor((Integer)argbEvaluator
                             .evaluate(colorProgress, mColor, mTextColor));
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

        mTempCanvas.drawOval(mBounds, mPaint);

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

    public void setBackgroundColorProgress(float backgroundColorProgress) {
        this.mBackgroundColorProgress = backgroundColorProgress;
        updateBackgroundColor();
    }
    
    public float getBackgroundColorProgress() {
        return mBackgroundColorProgress;
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
}
