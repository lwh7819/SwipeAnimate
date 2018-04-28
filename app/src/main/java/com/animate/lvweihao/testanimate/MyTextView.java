package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import static android.R.attr.width;

/**
 * Created by lv.weihao on 2016/9/10.
 */
public class MyTextView extends View {

    private String mText = "";
    private int mTextSize = 60;
    private Paint mPaint = new Paint();
    private Rect mTextRect = new Rect();

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        intiAttrs(attrs);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);

       WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
    }

    public void setText(String text) {
        mText = text;
    }

    private void intiAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mText = typedArray.getString(R.styleable.MyTextView_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_textSize, 60);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        canvas.drawText(mText, width/2 - mTextRect.width()/2, height/2 + mTextRect.height()/2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPaint.getTextBounds(mText,0, mText.length(), mTextRect);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mTextRect.width(), mTextRect.height());
        } else if(widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mTextRect.width(), heightSpecSize);
        } else if(heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mTextRect.height());
        }

    }
}
