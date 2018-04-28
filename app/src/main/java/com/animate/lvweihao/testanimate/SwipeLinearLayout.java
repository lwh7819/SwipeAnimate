package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import static com.animate.lvweihao.testanimate.R.id.lv1;

/**
 * Created by lv.weihao on 2016/9/13.
 */
public class SwipeLinearLayout extends LinearLayout {

    private int mScnWidth;
    private int mDownX, mDownY, nowX, nowY, lastX, lastY;
    private View mFirstListView, mSecondView;
    private int mFirstListViewWidth, mSecondViewWidth;
    private LayoutParams mLayoutParams;
    private View mView;
    public int mWidth;
    public boolean isShow = false;

    public SwipeLinearLayout(Context context) {
        super(context);
    }

    public SwipeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SwipeLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScnWidth = dm.widthPixels;


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        boolean inetrcept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mFirstListView = getChildAt(0);
//                mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();
//                mFirstListViewWidth = mLayoutParams.width;
                inetrcept = false;
                break;
            case MotionEvent.ACTION_MOVE:

//                if (isEnable) {
                if (Math.abs(lastX - mDownX) > Math.abs(lastY - mDownY) && Math.abs(lastX - mDownX) > touchSlop) {
                    inetrcept = true;
                } else {
                    inetrcept = false;
                }
//                }
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

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
//        LayoutInflater factory=LayoutInflater.from(getContext());
////        接着使用LayoutInflater对象获取Layout
//        final View DialogView=factory.inflate(R.layout.activity_double_listview, null);
////        最后使用该layout获取组件
//        mFirstListView=(ListView)DialogView.findViewById(lv1);
//        mFirstListViewWidth = mFirstListView.getLayoutParams().width;
//        mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();
//        mLayoutParams.width = mFirstListViewWidth;
//        mFirstListView.setLayoutParams(mLayoutParams);

        mFirstListView = ((ViewGroup)(((ViewGroup)getChildAt(0)).getChildAt(0))).getChildAt(0);
        mFirstListViewWidth = mFirstListView.getLayoutParams().width;
        mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();


        mFirstListView = ((ViewGroup)(((ViewGroup)getChildAt(0)).getChildAt(0))).getChildAt(0);
        mFirstListViewWidth = mFirstListView.getLayoutParams().width;
        mSecondView = ((ViewGroup)(((ViewGroup)getChildAt(0)).getChildAt(0))).getChildAt(1);
        mSecondViewWidth = mSecondView.getLayoutParams().width;
        mWidth = mFirstListViewWidth + mSecondViewWidth;


    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        nowX = (int) ev.getX();
        nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if(nowX < mDownX) {
                // 计算要偏移的距离
                int scroll = (nowX - mDownX) / 2;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if(-scroll >= mFirstListViewWidth) {
                    scroll = -mFirstListViewWidth;
                }
                // 重新设置leftMargin
                mLayoutParams.leftMargin = scroll;
                mFirstListView.setLayoutParams(mLayoutParams);

            } else if (nowX > mDownX && mLayoutParams.leftMargin != 0) {
                int scroll = -mFirstListViewWidth + 130 + (nowX - mDownX) / 2;
                mLayoutParams.leftMargin = scroll;
                mFirstListView.setLayoutParams(mLayoutParams);
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private void performActionUp() {
        if (nowX < mDownX) {
            // 偏移量大于button的一半，则显示button
            // 否则恢复默认
            if (-mLayoutParams.leftMargin >= mFirstListViewWidth / 5) {
                mLayoutParams.leftMargin = -mFirstListViewWidth + 130;
                mFirstListView.setLayoutParams(mLayoutParams);
                isShow = true;
            } else {
                mLayoutParams.leftMargin = 0;
                mFirstListView.setLayoutParams(mLayoutParams);
                isShow = false;
            }
        } else {
            if (-mLayoutParams.leftMargin >= mFirstListViewWidth /3){
                mLayoutParams.leftMargin = 0;
                mFirstListView.setLayoutParams(mLayoutParams);
                isShow = false;
            }
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (!isShow) {
//                    return true;
//                } else if (isShow){
//                    return  false;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!isShow) {
//                    return true;
//                } else if (isShow){
//                    return  false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (isShow) {
//                return false;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
