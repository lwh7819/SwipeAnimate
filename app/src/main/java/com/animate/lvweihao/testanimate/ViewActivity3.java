package com.animate.lvweihao.testanimate;

import android.animation.*;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lv.weihao on 2016/9/19.
 */
public class ViewActivity3 extends AppCompatActivity {
    private FrameLayout flView;
    private MyLinearLayout mllView;
    private ViewGroup.LayoutParams listLayoutParams,viewLayoutParams;
    private ListView listView1,listView2;
    private View view,item1;
    private LinearLayout flItem;
    private int mScnWidth;
    private MyAdapter myAdapter1, myAdapter2;
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

        creatViews();
        getLayoutParams();
        layoutViews();
        setListViews();
    }

    private void creatViews() {
        listView1 = new ListView(this);
        listView1.setBackgroundColor(Color.WHITE);
        listView1.setVerticalScrollBarEnabled(false);
        listView2 = new ListView(this);
        listView2.setBackgroundColor(Color.WHITE);
        listView2.setEnabled(false);
        listView2.setVerticalScrollBarEnabled(false);
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
                myAdapter2 = new MyAdapter(list2, ViewActivity3.this, isShow);
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
        listView1.setBackgroundColor(Color.parseColor("#eeeeee"));
        myAdapter2 = new MyAdapter(list2, this, true);
        listView2.setAdapter(myAdapter2);
        listView2.setBackgroundColor(Color.parseColor("#eeeeee"));
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                int itemHeight = view.getMeasuredHeight();
                int Pos[] = { -1, -1 };
                view.getLocationOnScreen(Pos);
                endX = listLayoutParams.width + viewLayoutParams.width;
                startY = Pos[1] - getStatusBarHeight();
                endY = list2.size() * 144;
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(item1, "translationX", 0, endX);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(item1, "translationY", startY, endY);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(item1, "scaleY", 1f, 0.8f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
                animatorSet.setDuration(500);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        listView1.setEnabled(true);
                        list2.add(map);
                        item1.setVisibility(View.GONE);
                        myAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        doDeleteTransformAnim(view,position);

                        listView1.setEnabled(false);
                        map= mLeftData.get(position);
                        item1.setVisibility(View.VISIBLE);
                        TextView tv = (TextView) flItem.findViewById(R.id.tv1);
                        tv.setText(map.toString());
                    }
                });
                animatorSet.start();
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
                endX = -(listLayoutParams.width + viewLayoutParams.width) / 2;
                Log.e("lwh", endX + "");

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

                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(item1, "translationX", startX, endX);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(item1, "translationY", startY, endY);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(item1, "scaleY", 0.8f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
                animatorSet.setDuration(500);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        item1.setVisibility(View.GONE);
                        listView2.setEnabled(true);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        item1.setVisibility(View.VISIBLE);
                        listView2.setEnabled(false);
                    }
                });
                animatorSet.start();

                if ((olderPosition - firstVisiblePosition) < listView1.getLastVisiblePosition()
                        && olderPosition -firstVisiblePosition>=0 && mOlderIndex != 19) {
                    doAddTransformAnim(listView1.getChildAt(olderPosition - firstVisiblePosition), olderPosition);
                } else if (olderPosition -firstVisiblePosition < 0 && mOlderIndex != 19) {
                    doAddTransformAnim(listView1.getChildAt(0),olderPosition);
                }

                doDeleteTransformAnim2(view, position);
            }
        });
    }

    private int getStatusBarHeight(){
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public void doDeleteTransformAnim(final View v, final int position){
        v.setVisibility(View.GONE);
        final int initialHeight = v.getMeasuredHeight();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "value", 0f, 1f);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int currentHeight = initialHeight - (int) (initialHeight * value);
                v.getLayoutParams().height = currentHeight;
                v.requestLayout();
            }
        });
        objectAnimator.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.VISIBLE);
                mLeftData.remove(position);
                myAdapter1.notifyDataSetChanged();
            }
        });
        animatorSet.start();
    }

    public void doAddTransformAnim(final View v, final int position){
//        v.setVisibility(View.GONE);
        final int initialHeight = v.getMeasuredHeight();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "value", 0f, 1f);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int currentHeight = (int) (initialHeight * value);
                v.getLayoutParams().height = currentHeight;
                v.requestLayout();
            }
        });
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.VISIBLE);
            }
        });
        objectAnimator.start();
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
        v.setVisibility(View.GONE);
        final int initialHeight = v.getMeasuredHeight();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "value", 0f, 1f);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int currentHeight = initialHeight - (int) (initialHeight * value);
                v.getLayoutParams().height = currentHeight;
                v.requestLayout();
            }
        });
        objectAnimator.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLeftData.add(olderPosition, list2.get(position));
                myAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.VISIBLE);
                list2.remove(position);
                myAdapter2.notifyDataSetChanged();
            }
        });
        animatorSet.start();
    }
}
