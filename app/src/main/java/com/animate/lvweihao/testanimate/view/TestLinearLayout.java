package com.animate.lvweihao.testanimate.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.animate.lvweihao.testanimate.MyAdapter;
import com.animate.lvweihao.testanimate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.animate.lvweihao.testanimate.DoubleLvActivity.mLeftData;

/**
 * Created by lv.weihao on 2016/9/20.
 */
public class TestLinearLayout extends LinearLayout {

    private int mScnWidth;
    private LinearLayout flItem;
    private int mDownX, mDownY, nowX, nowY;
    private View mFirstListView, mSecondView;
    private int mFirstListViewWidth, mSecondViewWidth;
    private LayoutParams mLayoutParams;
    private View mView;
    public int mWidth;
    public boolean isShow = false;
    private Context context;
    private ListView listView1,listView2;
    private MyAdapter myAdapter1,myAdapter2;
    private int endX,startX,endY,startY;
    private View view, item1;
    private ViewGroup.LayoutParams listLayoutParams,viewLayoutParams;
    private Animation animation;
    private Map<String, String> map;
    private int olderPosition;
    private int mOlderIndex;
    private Animation transformAnim;
    private AlphaAnimation listAnimate;
    private List<Map<String, String>> list2 = new ArrayList<>();
    private static ArrayList<Map<String, String>> mLeftData = new ArrayList<Map<String, String>>() {
        {
            for(int i=0;i<20;i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("OLD_INDEX", String.valueOf(i));
                map.put("CONTENT", "hello world, hello android  " + i);
//                add("hello world, hello android  " + i);
                add(map);
            }
        }
    };


    public TestLinearLayout(Context context, LinearLayout flItem) {
        super(context, null);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScnWidth = dm.widthPixels;
        listLayoutParams = new ViewGroup.LayoutParams(mScnWidth-220, ViewGroup.LayoutParams.MATCH_PARENT);
        viewLayoutParams = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        view = new View(context);
        view.setLayoutParams(viewLayoutParams);
        view.setBackgroundColor(Color.BLUE);
        listView1 = new ListView(context);
        listView1.setBackgroundColor(Color.BLACK);
        listView2 = new ListView(context);
        listView2.setBackgroundColor(Color.GREEN);
        listView1.setLayoutParams(listLayoutParams);
        listView2.setLayoutParams(listLayoutParams);
        addView(listView1,0);
        addView(view,1);
        addView(listView2,2);
        animation = AnimationUtils.loadAnimation(context, R.anim.animate_item_gone);
        this.flItem = flItem;
        this.context = context;
        item1 = flItem.findViewById(R.id.mll);
        setListViews();
    }

    public TestLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TestLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        boolean inetrcept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getRawX();
                mDownY = (int) ev.getRawY();
                mFirstListView = getChildAt(0);
                mLayoutParams = (LayoutParams) mFirstListView.getLayoutParams();
                mFirstListViewWidth = mLayoutParams.width;
                inetrcept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) ev.getRawX();
                int nowY = (int) ev.getRawY();
//                if (isEnable) {
                    if (Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY) && Math.abs(nowX - mDownX) > touchSlop) {
                        inetrcept = true;
                    } else {
                        inetrcept = false;
                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                inetrcept = false;
                break;
        }
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
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if (-scroll >= mFirstListViewWidth) {
                    scroll = -mFirstListViewWidth;
                }
                // 重新设置leftMargin
                mLayoutParams.leftMargin = scroll;
                mFirstListView.setLayoutParams(mLayoutParams);

            } else {
                mLayoutParams.leftMargin = 0;
                mFirstListView.setLayoutParams(mLayoutParams);
                getChildAt(0).setEnabled(true);
                getChildAt(2).setEnabled(false);
                isShow = false;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void performActionUp() {
        if (nowX < mDownX) {
            // 偏移量大于button的一半，则显示button
            // 否则恢复默认
            if (-mLayoutParams.leftMargin >= mFirstListViewWidth / 5) {
                mLayoutParams.leftMargin = -mFirstListViewWidth + 140;
                mFirstListView.setLayoutParams(mLayoutParams);
                getChildAt(0).setEnabled(false);
                getChildAt(2).setEnabled(true);
                isShow = true;
            } else {
                mLayoutParams.leftMargin = 0;
                mFirstListView.setLayoutParams(mLayoutParams);
                getChildAt(0).setEnabled(true);
                getChildAt(2).setEnabled(false);
            }
        }
    }

    private void setListViews() {
        myAdapter1 = new MyAdapter(mLeftData, context ,false);
        listView1.setAdapter(myAdapter1);
        myAdapter2 = new MyAdapter(list2, context, true);
        listView2.setAdapter(myAdapter2);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                int itemHeight = view.getMeasuredHeight();
                int Pos[] = { -1, -1 };
                view.getLocationOnScreen(Pos);
                endX = listLayoutParams.width + viewLayoutParams.width;
                startY = Pos[1] - 10;
                endY = list2.size() * itemHeight;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, endX, startY, endY);
                translateAnimation.setDuration(500);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(animation);
                animationSet.setDuration(500);
                view.startAnimation(animationSet);


                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        doDeleteTransformAnim(view,position);

//                        view.startAnimation(listAnimate);

                        listView1.setEnabled(false);
                        map= mLeftData.get(position);
                        item1.setVisibility(View.VISIBLE);
                        TextView tv = (TextView) flItem.findViewById(R.id.tv1);
                        tv.setText(map.toString());
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        if (isLastItemVisible()){
//                            mLeftData.remove(position);
//                            myAdapter1.notifyDataSetChanged();
//                        }
                        listView1.setEnabled(true);
                        list2.add(map);
                        myAdapter2.notifyDataSetChanged();
                        item1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isFirst = true;
                olderPosition = Integer.parseInt(list2.get(position).get("OLD_INDEX"));
                mOlderIndex = Integer.parseInt(list2.get(position).get("OLD_INDEX"));
                if (mLeftData.size() > 0) {
                    if (mLeftData.size() <= olderPosition) {
                        for (int i = 0; i < mLeftData.size(); i++) {
//                            if (mLeftData.size() -i -1  < 0){ break;}
                            if (mOlderIndex > Integer.parseInt(mLeftData.get(mLeftData.size() - i - 1).get("OLD_INDEX"))) {
                                if (isFirst) {
                                    olderPosition = mLeftData.size() - i;
                                }
                                break;

                            } else {
                                olderPosition = mLeftData.size() - (i + 1);
                                isFirst = false;
                            }
                        }

                    } else if (mLeftData.size() > olderPosition) {
                        for (int i = 1; i < olderPosition + 1; i++) {
                            if (mOlderIndex > Integer.parseInt(mLeftData.get(mOlderIndex - i).get("OLD_INDEX"))) {
                                break;
                            } else {
                                olderPosition = mOlderIndex - i;
                            }
                        }
                    }
                } else {
                    olderPosition = mLeftData.size();
                }

                int itemHeight = view.getMeasuredHeight();
                int Pos[] = { -1, -1 };
                view.getLocationOnScreen(Pos);
                startY = Pos[1] - 10;
                startX = mScnWidth - listLayoutParams.width;
                endX = -(listLayoutParams.width - startX);

                int lastVisiblePosition = listView1.getLastVisiblePosition();
                int firstVisiblePosition = listView1.getFirstVisiblePosition();
                if (olderPosition>lastVisiblePosition){
                    endY = mLeftData.size() *itemHeight;
                } else if (olderPosition < firstVisiblePosition) {
                    endY = - itemHeight;
                } else {
                    listView1.getChildAt(olderPosition - firstVisiblePosition).getLocationOnScreen(Pos);
                    endY = Pos[1] - 10;
                }
                TranslateAnimation translateAnimation = new TranslateAnimation(startX, endX, startY, endY);
                translateAnimation.setDuration(500);
                AnimationSet animationSet2 = new AnimationSet(true);
                animationSet2.addAnimation(translateAnimation);
                animationSet2.addAnimation(animation);
                animationSet2.setDuration(500);
                item1.startAnimation(animationSet2);

                if ((olderPosition - firstVisiblePosition) < listView1.getLastVisiblePosition()
                        && olderPosition -firstVisiblePosition>=0 && mOlderIndex != 19) {
                    doAddTransformAnim(listView1.getChildAt(olderPosition - firstVisiblePosition), olderPosition);
                } else if (olderPosition -firstVisiblePosition < 0 && mOlderIndex != 19) {
                    doAddTransformAnim(listView1.getChildAt(0),olderPosition);
                }

                doDeleteTransformAnim2(view, position);
//                mLeftData.add(olderPosition, list2.get(position));
//                myAdapter1.notifyDataSetChanged();
//                list2.remove(position);
//                myAdapter2.notifyDataSetChanged();
            }
        });
    }


    public void doDeleteTransformAnim(final View v, final int position){
        final int initialHeight = v.getMeasuredHeight();
        transformAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = initialHeight - (int) ((initialHeight) * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        transformAnim.setDuration(500);

        listAnimate = new AlphaAnimation(0,0);
        listAnimate.setDuration(500);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(transformAnim);
        animationSet.addAnimation(listAnimate);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLeftData.remove(position);
                myAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animationSet);
    }

    public void doAddTransformAnim(final View v, final int position){
        final int initialHeight = v.getMeasuredHeight();
        transformAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int) ((initialHeight) * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        transformAnim.setDuration(500);

        listAnimate = new AlphaAnimation(0,0);
        listAnimate.setDuration(500);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(transformAnim);
        animationSet.addAnimation(listAnimate);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animationSet);
    }

    /**
     * 判断最后listView中最后一个item是否完全显示出来
     * listView 是集合的那个ListView
     * @return true完全显示出来，否则false
     */

    protected boolean isLastItemVisible() {
        final Adapter adapter = listView1.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = listView1.getLastVisiblePosition()-1 ;

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - listView1.getFirstVisiblePosition();
            final int childCount = listView1.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = listView1.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= listView1.getBottom();
            }
        }

        return false;
    }

    public void doDeleteTransformAnim2(final View v, final int position){
        final int initialHeight = v.getMeasuredHeight();
        transformAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = initialHeight - (int) ((initialHeight) * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        transformAnim.setDuration(500);

        listAnimate = new AlphaAnimation(0,0);
        listAnimate.setDuration(500);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(transformAnim);
        animationSet.addAnimation(listAnimate);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLeftData.add(olderPosition, list2.get(position));
                myAdapter1.notifyDataSetChanged();
                list2.remove(position);
                myAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(animationSet);
    }
}
