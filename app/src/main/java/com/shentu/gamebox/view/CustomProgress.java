package com.shentu.gamebox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.shentu.gamebox.R;

public class CustomProgress extends ProgressBar {

    private  int boxPercentColor = 0xfffffff;
    private  int boxPercentSize = sp2px(20);
    private  boolean boxShowPercent = true;
    private  int boxIcon=0;
    private Paint mPaint;
    private int color;
    private int icon;
    private int pixelSize;
    private boolean aBoolean;
    private String text;


    public CustomProgress(Context context) {

        super(context, null);
    }

    public CustomProgress(Context context, AttributeSet attrs) {

        super(context, attrs, 0);
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customProgressBar);
        color = typedArray.getColor(R.styleable.customProgressBar_boxPercentColor, boxPercentColor);
        icon = typedArray.getResourceId(R.styleable.customProgressBar_boxIcon, boxIcon);
        pixelSize = typedArray.getDimensionPixelSize(R.styleable.customProgressBar_boxPercentSize, boxPercentSize);
        aBoolean = typedArray.getBoolean(R.styleable.customProgressBar_boxShowPercent, boxShowPercent);
        typedArray.recycle();

        init();
//        initText();
    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec,bitmap.getHeight);
    }
    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(color);
        mPaint.setTextSize(pixelSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            mPaint = new Paint();
        int currentProgress = getProgress();
            String text = currentProgress + "%";
            Rect bounds = new Rect();
            mPaint.setTextSize(25);
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int x = (getWidth() /2) - bounds.centerX();
            int y = (getHeight() /2) - bounds.centerY();
            canvas.drawText(text, x, y, mPaint);
    }


    private int sp2px(float v) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, displayMetrics);
    }
}
