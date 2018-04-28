package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by lv.weihao on 2016/9/18.
 */
public class mListView extends ListView {
    private int mDownX,mDownY,mNowX,mNowY;
    private int mFirstListViewWidth;
    private ViewGroup.LayoutParams mLayoutParams;
    private View mFirstListView;
    private ViewGroup mPointChild;

    public mListView(Context context) {
        super(context);
    }

    public mListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mDownX = (int) ev.getX();
//                mDownY = (int) ev.getY();
//                // 获取当前点的item
//                mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY)
//                        - getFirstVisiblePosition());
//                return false;
////                mFirstListView = getChildAt(0);
////                mLayoutParams = mFirstListView.getLayoutParams();
////                mFirstListViewWidth = mLayoutParams.width;
//            case MotionEvent.ACTION_MOVE:
////                mNowX = (int) ev.getX();
////                mNowY = (int) ev.getY();
////                if (Math.abs(mNowX - mDownX) > Math.abs(mNowY - mDownY)) {
////                    if (mNowX < mDownX) {
////                        int scroll = (mNowX - mDownX) / 2;
////                        // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
////                        if(-scroll >= mFirstListViewWidth) {
////                            scroll = -mFirstListViewWidth;
////                        }
////                        // 重新设置leftMargin
////                        mLayoutParams.resolveLayoutDirection(scroll);
////                        mFirstListView.setLayoutParams(mLayoutParams);
////                    }
////                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
