package com.movies.progressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author White
 * Date 2017/4/23
 * Time 14:21
 */

public class HorizontalProgressView extends ProgressBar {

  private static final String TAG = HorizontalProgressView.class.getSimpleName();

  @IntDef({TOP, CENTRE, BOTTOM})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Position {

  }

  public static final int TOP = 1;
  public static final int CENTRE = 0;
  public static final int BOTTOM = -1;

  private static final String STATE = "state";
  private static final String NORMAL_BAR_SIZE = "normal_bar_size";
  private static final String NORMAL_BAR_COLOR = "normal_bar_color";
  private static final String REACH_BAR_SIZE = "reach_bar_size";
  private static final String REACH_BAR_COLOR = "reach_bar_color";
  private static final String TEXT_COLOR = "text_color";
  private static final String TEXT_SIZE = "text_size";
  private static final String TEXT_SUFFIX = "text_suffix";
  private static final String TEXT_PREFIX = "text_prefix";
  private static final String TEXT_OFFSET = "text_offset";
  private static final String TEXT_POSITION = "text_position";
  private static final String TEXT_VISIBLE = "text_visible";
  private static final String TEXT_SKEW_X = "text_skew_x";

  private int mNormalBarSize = Utils.dp2px(getContext(), 2);
  private int mNormalBarColor = Color.parseColor("#FFD3D6DA");
  private int mReachBarSize = Utils.dp2px(getContext(), 2);
  private int mReachBarColor = Color.parseColor("#108ee9");
  private int mTextSize = Utils.sp2px(getContext(), 14);
  private int mTextColor = Color.parseColor("#108ee9");
  private int mTextOffset = Utils.dp2px(getContext(), 6);
  private int mProgressPosition = CENTRE;
  private boolean mTextVisible = true;
  private float mTextSkewX;
  private String mTextPrefix = "";
  private String mTextSuffix = " %";

  private Paint mTextPaint;
  private Paint mNormalPaint;
  private Paint mReachPaint;
  /**
   * ????????????????????????????????????????????????
   */
  private int mDrawWidth;

  public HorizontalProgressView(Context context) {
    this(context, null);
  }

  public HorizontalProgressView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    obtainAttributes(attrs);
    // ???????????????
    initPaint();
  }

  protected void initPaint() {
    mTextPaint = new Paint();
    mTextPaint.setColor(mTextColor);
    mTextPaint.setStyle(Paint.Style.FILL);
    mTextPaint.setTextSize(mTextSize);
    mTextPaint.setTextSkewX(mTextSkewX);
    mTextPaint.setAntiAlias(true); // ?????????

   /* mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,
            getResources().getDisplayMetrics()));*/
    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "righteous.ttf");
    mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

    mNormalPaint = new Paint();
    mNormalPaint.setColor(mNormalBarColor);
    mNormalPaint.setStyle(Paint.Style.FILL);
    mNormalPaint.setAntiAlias(true);
    mNormalPaint.setStrokeWidth(mNormalBarSize);

    mReachPaint = new Paint();
    mReachPaint.setColor(mReachBarColor);
    mReachPaint.setStyle(Paint.Style.FILL);
    mReachPaint.setAntiAlias(true);
    mReachPaint.setStrokeWidth(mReachBarSize);
  }

  /**
   * ????????????????????????
   */
  protected void obtainAttributes(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressView);
    mNormalBarSize = (int) ta.getDimension(R.styleable.HorizontalProgressView_progressNormalSize,
        mNormalBarSize);
    mNormalBarColor =
        ta.getColor(R.styleable.HorizontalProgressView_progressNormalColor, mNormalBarColor);

    mReachBarSize =
        (int) ta.getDimension(R.styleable.HorizontalProgressView_progressReachSize, mReachBarSize);
    mReachBarColor =
        ta.getColor(R.styleable.HorizontalProgressView_progressReachColor, mReachBarColor);

    mTextSize =
        (int) ta.getDimension(R.styleable.HorizontalProgressView_progressTextSize, mTextSize);
    mTextColor = ta.getColor(R.styleable.HorizontalProgressView_progressTextColor, mTextColor);
    mTextSkewX = ta.getDimension(R.styleable.HorizontalProgressView_progressTextSkewX, 0);
    if (ta.hasValue(R.styleable.HorizontalProgressView_progressTextSuffix)) {
      mTextSuffix = ta.getString(R.styleable.HorizontalProgressView_progressTextSuffix);
    }
    if (ta.hasValue(R.styleable.HorizontalProgressView_progressTextPrefix)) {
      mTextPrefix = ta.getString(R.styleable.HorizontalProgressView_progressTextPrefix);
    }
    mTextOffset =
        (int) ta.getDimension(R.styleable.HorizontalProgressView_progressTextOffset, mTextOffset);

    mProgressPosition =
        ta.getInt(R.styleable.HorizontalProgressView_progressTextPosition, mProgressPosition);

    mTextVisible =
        ta.getBoolean(R.styleable.HorizontalProgressView_progressTextVisible, mTextVisible);
    ta.recycle();
  }

  @Override
  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int textHeight = (int) (mTextPaint.descent() - mTextPaint.ascent());
    int heightSize = Math.max(Math.max(mNormalBarSize, mReachBarSize), Math.abs(textHeight * 2))
        + getPaddingTop()
        + getPaddingBottom();
    heightSize = resolveSize(heightSize, heightMeasureSpec);
    setMeasuredDimension(widthSize, heightSize);
    // ??????????????????
    mDrawWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }

  @Override
  protected synchronized void onDraw(Canvas canvas) {
    canvas.save();
    drawHorizontalProgressView(canvas);
    canvas.restore();
  }

  private void drawHorizontalProgressView(Canvas canvas) {
    canvas.translate(getPaddingLeft(), getHeight() / 2);

    boolean needDrawUnReachArea = true; //?????????????????????????????????
    float textWidth = 0;
    String text = mTextPrefix + getProgress() + mTextSuffix;
    if (mTextVisible) {
      textWidth = mTextPaint.measureText(text);
    } else {
      mTextOffset = 0;
    }
    float textHeight = (mTextPaint.descent() + mTextPaint.ascent()) / 2;
    float radio = getProgress() * 1.0f / getMax();
    float progressPosX = (int) (mDrawWidth - textWidth) * (radio);

    if (progressPosX + textWidth >= mDrawWidth) {
      progressPosX = mDrawWidth - textWidth;
      needDrawUnReachArea = false;
    }

    // ?????????????????????
    float endX = progressPosX - mTextOffset / 2;
    if (endX > 0) {
      canvas.drawLine(0, 0, endX, 0, mReachPaint);
    }

    // ?????????????????????
    if (needDrawUnReachArea) {
      float start = progressPosX + mTextOffset / 2 + textWidth;
      canvas.drawLine(start, 0, mDrawWidth, 0, mNormalPaint);
    }

    // ????????????
    if (!mTextVisible) {
      return;
    }
    switch (mProgressPosition) {
      case BOTTOM: //BOTTOM
        canvas.drawText(text, progressPosX, -textHeight * 2 + mTextOffset, mTextPaint);
        break;
      case TOP: // TOP
        canvas.drawText(text, progressPosX, 0 - mTextOffset, mTextPaint);
        break;
      default: // CENTER
        canvas.drawText(text, progressPosX, -textHeight, mTextPaint);
        break;
    }
  }

  public int getNormalBarSize() {
    return mNormalBarSize;
  }

  public void setNormalBarSize(int normalBarSize) {
    mNormalBarSize = Utils.dp2px(getContext(), normalBarSize);
    invalidate();
  }

  public int getNormalBarColor() {
    return mNormalBarColor;
  }

  public void setNormalBarColor(int normalBarColor) {
    mNormalBarColor = normalBarColor;
    invalidate();
  }

  public int getReachBarSize() {
    return mReachBarSize;
  }

  public void setReachBarSize(int reachBarSize) {
    mReachBarSize = Utils.dp2px(getContext(), reachBarSize);
    invalidate();
  }

  public int getReachBarColor() {
    return mReachBarColor;
  }

  public void setReachBarColor(int reachBarColor) {
    mReachBarColor = reachBarColor;
    invalidate();
  }

  public int getTextSize() {
    return mTextSize;
  }

  public void setTextSize(int textSize) {
    mTextSize = Utils.sp2px(getContext(), textSize);
    invalidate();
  }

  public int getTextColor() {
    return mTextColor;
  }

  public void setTextColor(int textColor) {
    mTextColor = textColor;
    invalidate();
  }

  public int getTextOffset() {
    return mTextOffset;
  }

  public void setTextOffset(int textOffset) {
    mTextOffset = Utils.dp2px(getContext(), textOffset);
    invalidate();
  }

  @Position
  public int getProgressPosition() {
    return mProgressPosition;
  }

  public void setProgressPosition(@Position int progressPosition) {
    if (progressPosition > 1 || progressPosition < -1) {
      mProgressPosition = 0;
    } else {
      mProgressPosition = progressPosition;
    }
    invalidate();
  }

  public boolean isTextVisible() {
    return mTextVisible;
  }

  public void setTextVisible(boolean textVisible) {
    mTextVisible = textVisible;
    invalidate();
  }

  public float getTextSkewX() {
    return mTextSkewX;
  }

  public void setTextSkewX(float textSkewX) {
    mTextSkewX = textSkewX;
    invalidate();
  }

  public String getTextPrefix() {
    return mTextPrefix;
  }

  public void setTextPrefix(String textPrefix) {
    mTextPrefix = textPrefix;
    invalidate();
  }

  public String getTextSuffix() {
    return mTextSuffix;
  }

  public void setTextSuffix(String textSuffix) {
    mTextSuffix = textSuffix;
    invalidate();
  }

  public void runProgressAnim(long duration) {
    setProgressInTime(0, duration);
  }

  /**
   * @param progress ?????????
   * @param duration ??????????????????
   */
  public void setProgressInTime(final int progress, final long duration) {
    setProgressInTime(progress, getProgress(), duration);
  }

  /**
   * @param startProgress ????????????
   * @param progress ?????????
   * @param duration ??????????????????
   */
  public void setProgressInTime(int startProgress, final int progress, final long duration) {
    ValueAnimator valueAnimator = ValueAnimator.ofInt(startProgress, progress);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

      @Override
      public void onAnimationUpdate(ValueAnimator animator) {
        //??????????????????????????????????????????1-100??????
        int currentValue = (Integer) animator.getAnimatedValue();
        setProgress(currentValue);
      }
    });
    AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
    valueAnimator.setInterpolator(interpolator);
    valueAnimator.setDuration(duration);
    valueAnimator.start();
  }

  @Override
  public Parcelable onSaveInstanceState() {
    final Bundle bundle = new Bundle();
    bundle.putParcelable(STATE, super.onSaveInstanceState());
    // ??????text??????
    bundle.putInt(TEXT_COLOR, getTextColor());
    bundle.putInt(TEXT_SIZE, getTextSize());
    bundle.putInt(TEXT_OFFSET, getTextOffset());
    bundle.putInt(TEXT_POSITION, getProgressPosition());
    bundle.putFloat(TEXT_SKEW_X, getTextSkewX());
    bundle.putBoolean(TEXT_VISIBLE, isTextVisible());
    bundle.putString(TEXT_SUFFIX, getTextSuffix());
    bundle.putString(TEXT_PREFIX, getTextPrefix());
    // ???????????????????????????
    bundle.putInt(REACH_BAR_COLOR, getReachBarColor());
    bundle.putInt(REACH_BAR_SIZE, getReachBarSize());

    // ???????????????????????????
    bundle.putInt(NORMAL_BAR_COLOR, getNormalBarColor());
    bundle.putInt(NORMAL_BAR_SIZE, getNormalBarSize());
    return bundle;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    if (state instanceof Bundle) {
      final Bundle bundle = (Bundle) state;

      mTextColor = bundle.getInt(TEXT_COLOR);
      mTextSize = bundle.getInt(TEXT_SIZE);
      mTextOffset = bundle.getInt(TEXT_OFFSET);
      mProgressPosition = bundle.getInt(TEXT_POSITION);
      mTextSkewX = bundle.getFloat(TEXT_SKEW_X);
      mTextVisible = bundle.getBoolean(TEXT_VISIBLE);
      mTextSuffix = bundle.getString(TEXT_SUFFIX);
      mTextPrefix = bundle.getString(TEXT_PREFIX);

      mReachBarColor = bundle.getInt(REACH_BAR_COLOR);
      mReachBarSize = bundle.getInt(REACH_BAR_SIZE);
      mNormalBarColor = bundle.getInt(NORMAL_BAR_COLOR);
      mNormalBarSize = bundle.getInt(NORMAL_BAR_SIZE);

      initPaint();
      super.onRestoreInstanceState(bundle.getParcelable(STATE));
      return;
    }
    super.onRestoreInstanceState(state);
  }

  @Override
  public void invalidate() {
    initPaint();
    super.invalidate();
  }
}
