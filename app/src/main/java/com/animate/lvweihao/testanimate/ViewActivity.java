package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lv.weihao on 2016/9/19.
 */
public class ViewActivity extends AppCompatActivity {
    private FrameLayout flView;
    private MyLinearLayout mllView;
    private ViewGroup.LayoutParams listLayoutParams,viewLayoutParams;
    private ListView listView1,listView2;
    private View view,item1;
    private View item2;
    private LinearLayout flItem;
    private int mScnWidth;
    private MyAdapter myAdapter1, myAdapter2;
    private Animation animation, transformAnim, listAnimate;
    private int startX, endX, startY, endY;
    private List<Map<String, String>> list2 = new ArrayList<>();
    private OnSwipeChanged listener;

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
    private Map<String, String> map;
    private int olderPosition;
    private int mOlderIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScnWidth = dm.widthPixels;

        animation = AnimationUtils.loadAnimation(this, R.anim.animate_item_gone);
        creatViews();
        getLayoutParams();
        layoutViews();
        setListViews();
    }

    private void creatViews() {
        listView1 = new ListView(this);
        listView1.setBackgroundColor(Color.BLACK);
        listView2 = new ListView(this);
        listView2.setBackgroundColor(Color.BLACK);
        listView2.setEnabled(false);
        view = new View(this);
        view.setBackgroundColor(Color.parseColor("#ffffef"));
        flItem = (LinearLayout) getLayoutInflater().inflate(R.layout.m_fl_item,null);
        item1 = flItem.findViewById(R.id.mll);
    }

    private void getLayoutParams() {
        listLayoutParams = new ViewGroup.LayoutParams(mScnWidth-220, ViewGroup.LayoutParams.MATCH_PARENT);
        viewLayoutParams = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void layoutViews() {
        listener = new OnSwipeChanged() {
            @Override
            public void onSwipeChanged(boolean isShow) {
                myAdapter2 = new MyAdapter(list2, ViewActivity.this, isShow);
                listView2.setAdapter(myAdapter2);
                myAdapter2.notifyDataSetChanged();
            }
        };
        mllView = new MyLinearLayout(this, listener);
        mllView.addView(listView1, listLayoutParams);
        mllView.addView(view, viewLayoutParams);
        mllView.addView(listView2, listLayoutParams);
        flView = (FrameLayout) findViewById(R.id.fl);
        flView.addView(mllView);
        flView.addView(flItem);
    }

    private void setListViews() {
        myAdapter1 = new MyAdapter(mLeftData, this ,false);
        listView1.setAdapter(myAdapter1);
        myAdapter2 = new MyAdapter(list2, this, true);
        listView2.setAdapter(myAdapter2);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                int itemHeight = view.getMeasuredHeight();
                int Pos[] = { -1, -1 };
                view.getLocationOnScreen(Pos);
                endX = listLayoutParams.width + viewLayoutParams.width;
                startY = Pos[1] - getStatusBarHeight();
                endY = list2.size() * itemHeight;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, endX, startY, endY);
                translateAnimation.setDuration(500);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(animation);
                animationSet.setDuration(500);
                item1.startAnimation(animationSet);



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
                startY = Pos[1] - getStatusBarHeight();
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
                    endY = Pos[1] - getStatusBarHeight();
                }
                TranslateAnimation translateAnimation = new TranslateAnimation(startX, endX, startY, endY);
                translateAnimation.setDuration(500);
                AnimationSet animationSet2 = new AnimationSet(true);
                animationSet2.addAnimation(translateAnimation);
                animationSet2.addAnimation(animation);
                animationSet2.setDuration(500);
                item1.startAnimation(animationSet2);

                animationSet2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        listView2.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        listView2.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

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

    private int getStatusBarHeight(){
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
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
