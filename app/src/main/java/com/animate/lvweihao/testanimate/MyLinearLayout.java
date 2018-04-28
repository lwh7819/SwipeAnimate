package com.animate.lvweihao.testanimate;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lv.weihao on 2016/9/20.
 */
public class MyLinearLayout extends LinearLayout {
    private int mDownX, mDownY, nowX, nowY,lastX,lastY;
    private LayoutParams mLayoutParams;
    private View mFirstListView;
    private int mFirstListViewWidth;
    private float mLastPostion;
    private OnSwipeChanged listener;
    private static ArrayList<Map<String, String>> mLeftData = new ArrayList<Map<String, String>>() {
        {
            for(int i=0;i<20;i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("OLD_INDEX", String.valueOf(i));
                map.put("CONTENT", "hello world, hello android  " + i);
                add(map);
            }
        }
    };

    public MyLinearLayout(Context context, OnSwipeChanged listener) {
        super(context, null);
        this.listener = listener;
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        boolean inetrcept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstListView = getChildAt(0);
                mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();
                mFirstListViewWidth = mLayoutParams.width;
                inetrcept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(lastX - mDownX) > Math.abs(lastY - mDownY) && Math.abs(lastX - mDownX) > touchSlop) {
                    inetrcept = true;
                } else {
                    inetrcept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                inetrcept = false;
                break;
            default:
                break;
        }
        lastX = mDownX;
        lastY = mDownY;
        return inetrcept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                performActionUp();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionDown(MotionEvent ev) {
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        mFirstListView = getChildAt(0);
        mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();
        mFirstListViewWidth = mLayoutParams.width;
    }

    private boolean performActionMove(MotionEvent ev) {
        nowX = (int) ev.getX();
        nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if (nowX < mDownX) {
                // 计算要偏移的距离
                int scroll = (nowX - mDownX) / 2;
                if (mLayoutParams.leftMargin != -mFirstListViewWidth + 140) {
                    if (-scroll >= mFirstListViewWidth) {
                        scroll = -mFirstListViewWidth;
                    }
                    // 重新设置leftMargin
                    mLayoutParams.leftMargin = scroll;
                    mFirstListView.setLayoutParams(mLayoutParams);
                }
            } else {
               int scroll = (nowX - mDownX) / 2;
                if (mLayoutParams.leftMargin != 0) {
                    mLayoutParams.leftMargin = -mFirstListViewWidth + 140 + scroll;
                    mFirstListView.setLayoutParams(mLayoutParams);
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    private void performActionUp() {
        if (nowX < mDownX) {
            // 偏移量大于button的一半，则显示button
            // 否则恢复默认
            if (-mLayoutParams.leftMargin >= mFirstListViewWidth / 8) {
                mLastPostion = mLayoutParams.leftMargin;
                doLeftAnima();
                getChildAt(0).setEnabled(false);
                getChildAt(2).setEnabled(true);
                listener.onSwipeChanged(false);
            } else {
                mLastPostion = mLayoutParams.leftMargin;
                doRightAnima();
            }
        } else {
            if ((mLayoutParams.leftMargin + mFirstListViewWidth -140) >= mFirstListViewWidth / 8) {
                mLastPostion = mLayoutParams.leftMargin;
                doRightAnima();
                getChildAt(0).setEnabled(true);
                getChildAt(2).setEnabled(false);
                listener.onSwipeChanged(true);
            } else {
                mLastPostion = mLayoutParams.leftMargin;
                doLeftAnima();
            }
        }
    }

    public void setPostion(int height) {
        mLayoutParams.leftMargin = height;
        mFirstListView.setLayoutParams(mLayoutParams);
    }

    /**
     * 左滑动画
     */
    public void doLeftAnima() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "mLastPostion", mLastPostion, -mFirstListViewWidth + 140);
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setPostion(Math.round(value));
            }
        });
        anim.start();
    }

    /**
     * 右滑动画
     */
    public void doRightAnima() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "mLastPostion", mLastPostion, 0);
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setPostion(Math.round(value));
            }
        });
        anim.start();
    }
}
