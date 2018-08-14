package com.animate.lvweihao.testanimate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lv.weihao on 2016/9/19.
 */
public class ViewActivity2 extends AppCompatActivity {
    private FrameLayout flView;
    private MyLinearLayout mllView;
    private ViewGroup.LayoutParams listLayoutParams, viewLayoutParams, headLayoutParams;
    private RecyclerView listView1, listView2;
    private View view, item1;
    private LinearLayout flItem;
    private int mScnWidth;
    private int mScnHeigh;
    private MyRecyclerAdapter myAdapter1, myAdapter2;
    private int startX, endX, startY, endY;
    private List<Map<String, Object>> list2 = new ArrayList<>();
    private OnSwipeChanged listener;
    private int firstViewHeight;

    private static Integer[] imgs = {R.mipmap.a001, R.mipmap.a002, R.mipmap.a003, R.mipmap.a004,
            R.mipmap.a005, R.mipmap.a007, R.mipmap.a022, R.mipmap.a009, R.mipmap.a010,
            R.mipmap.a011, R.mipmap.a012, R.mipmap.a013, R.mipmap.a014, R.mipmap.a015, R.mipmap.a016,
            R.mipmap.a017, R.mipmap.a018, R.mipmap.a019, R.mipmap.a020, R.mipmap.a021};

    private static ArrayList<Map<String, Object>> mLeftData = new ArrayList<Map<String, Object>>() {
        {
            for (int i = 0; i < 20; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("OLD_INDEX", String.valueOf(i));
                map.put("CONTENT", "hello world, hello android  " + i);
                map.put("IMG", imgs[i]);
                add(map);
            }
        }
    };
    private Map<String, Object> map;
    private int olderPosition;
    private int mOlderIndex;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScnWidth = dm.widthPixels;
        mScnHeigh = dm.heightPixels;

        mTextView = (TextView) findViewById(R.id.textView);
        View mView = findViewById(R.id.first_view);
        firstViewHeight = mView.getLayoutParams().height;
        Log.e("lwh", "firstViewHeight:" + firstViewHeight + "mScnHeigh: " + mScnHeigh);

        creatViews();
        getLayoutParams();
        layoutViews();
        setListViews();
    }

    private void creatViews() {
        listView1 = new RecyclerView(this);
        listView1.setBackgroundColor(Color.WHITE);
        listView2 = new RecyclerView(this);
        listView2.setBackgroundColor(Color.WHITE);
        view = new View(this);
        view.setBackgroundColor(Color.parseColor("#ffffef"));
        flItem = (LinearLayout) getLayoutInflater().inflate(R.layout.m_fl_item, null);
        item1 = flItem.findViewById(R.id.mll);
    }

    private void getLayoutParams() {
        listLayoutParams = new ViewGroup.LayoutParams(mScnWidth - 220, ViewGroup.LayoutParams.MATCH_PARENT);
        viewLayoutParams = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        headLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
    }

    private void layoutViews() {
        listener = new OnSwipeChanged() {
            @Override
            public void onSwipeChanged(boolean isShow) {
                myAdapter2.onSwipeChange(isShow);
                myAdapter1.setClickable(isShow);
                myAdapter2.setClickable(!isShow);
                if (isShow) {
                    mTextView.setText("浏览");
                } else {
                    mTextView.setText("我的收藏");
                }
            }
        };
        mllView = new MyLinearLayout(this, listener);
        mllView.addView(listView1, listLayoutParams);
        mllView.addView(view, viewLayoutParams);

        LinearLayout mLl = new LinearLayout(this);
        mLl.setOrientation(LinearLayout.VERTICAL);
        View head = new View(this);
        head.setBackgroundColor(Color.parseColor("#ffffef"));
        mLl.addView(head, headLayoutParams);
        mLl.addView(listView2, listLayoutParams);
        mllView.addView(mLl, listLayoutParams);

//        mllView.addView(listView2, listLayoutParams);
        flView = (FrameLayout) findViewById(R.id.fl);
        flView.addView(mllView);
        flView.addView(flItem);
    }

    private void setListViews() {
        myAdapter1 = new MyRecyclerAdapter(this, mLeftData, false);
        listView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView1.setAdapter(myAdapter1);
        listView1.setBackgroundColor(Color.parseColor("#eeeeee"));
        myAdapter2 = new MyRecyclerAdapter(this, list2, true);
        listView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView2.setAdapter(myAdapter2);
        listView2.setBackgroundColor(Color.parseColor("#eeeeee"));
        myAdapter2.setClickable(false);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(500);
        listView1.setItemAnimator(defaultItemAnimator);

        myAdapter1.setOnItemClickListener(new OnItemClickListenter() {
            @Override
            public void onItemClickListener(final View view, final int position) {
                int itemHeight = view.getMeasuredHeight();
                int Pos[] = {-1, -1};
                view.getLocationOnScreen(Pos);
                endX = listLayoutParams.width + viewLayoutParams.width;
                startY = Pos[1] - getStatusBarHeight() - firstViewHeight;
                endY = list2.size() * 108 + headLayoutParams.height;
                int idx = Integer.parseInt(((TextView)view.findViewById(R.id.tv2)).getText().toString());
                item1.findViewById(R.id.iv11).setBackground(getResources().getDrawable(imgs[idx]));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(item1, "translationX", 0, endX);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(item1, "translationY", startY, endY - 45);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(item1, "scaleY", 1f, 0.6f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
                animatorSet.setDuration(500);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myAdapter1.setClickable(true);
                        myAdapter2.setClickable(false);
                        item1.setVisibility(View.GONE);
                        list2.add(map);
                        myAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        myAdapter1.setClickable(false);
                        myAdapter2.setClickable(true);
                        map = mLeftData.get(position);
                        item1.setVisibility(View.VISIBLE);
                        TextView tv = (TextView) flItem.findViewById(R.id.tv1);
                        tv.setText(map.toString());
                        myAdapter1.removeData(position);
                    }
                });
                animatorSet.start();
            }
        });

        myAdapter2.setOnItemClickListener(new OnItemClickListenter() {
            @Override
            public void onItemClickListener(View view, final int position) {
                boolean isFirst = true;
                olderPosition = Integer.parseInt(list2.get(position).get("OLD_INDEX").toString());
                mOlderIndex = Integer.parseInt(list2.get(position).get("OLD_INDEX").toString());
                if (mLeftData.size() > 0) {
                    if (mLeftData.size() <= olderPosition) {
                        for (int i = 0; i < mLeftData.size(); i++) {
                            if (mOlderIndex > Integer.parseInt(mLeftData.get(mLeftData.size() - i - 1).get("OLD_INDEX").toString())) {
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
                        for (int i = 1; i < mOlderIndex + 1; i++) {
                            if (mOlderIndex > Integer.parseInt(mLeftData.get(mOlderIndex - i).get("OLD_INDEX").toString())) {
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
                int Pos[] = {-1, -1};
                view.getLocationOnScreen(Pos);
                startY = Pos[1] - getStatusBarHeight() - firstViewHeight;
                startX = mScnWidth - listLayoutParams.width;
                endX = -(listLayoutParams.width + viewLayoutParams.width) / 2;

                RecyclerView.LayoutManager layoutManager = listView1.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                //获取最后一个可见view的位置
                int lastVisiblePosition = linearManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                int firstVisiblePosition = linearManager.findFirstVisibleItemPosition();

                if (olderPosition > lastVisiblePosition) {
//                    endY = mLeftData.size() * itemHeight;
                    if (mLeftData.size() == 0) {
                        endY = 0;
                    } else {
                        Log.e("lwh", lastVisiblePosition - firstVisiblePosition + "");
                        listView1.getChildAt(lastVisiblePosition - firstVisiblePosition).getLocationOnScreen(Pos);
                        int lastVisibleHeight = Pos[1] + itemHeight;
                        Log.e("lwh", "lastVisibleHeight: " + lastVisibleHeight + "  mScnHeigh:" + mScnHeigh);
                        if (lastVisibleHeight < mScnHeigh) {
                            endY = mLeftData.size() * itemHeight;
                        } else {
                            endY = mScnHeigh + itemHeight;
                        }
                    }
                } else if (olderPosition < firstVisiblePosition) {
                    endY = -itemHeight;
                } else {
                    listView1.getChildAt(olderPosition - firstVisiblePosition).getLocationOnScreen(Pos);
                    endY = Pos[1] - getStatusBarHeight() - firstViewHeight;
                }

                int idx = Integer.parseInt(((TextView)view.findViewById(R.id.tv2)).getText().toString());
                item1.findViewById(R.id.iv11).setBackground(getResources().getDrawable(imgs[idx]));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(item1, "translationX", startX, endX);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(item1, "translationY", startY, endY);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(item1, "scaleY", 1f, 1.2f, 1f, 1f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
                animatorSet.setDuration(500);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        item1.setVisibility(View.GONE);
                        myAdapter2.setClickable(true);
                        myAdapter1.setClickable(false);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        item1.setVisibility(View.VISIBLE);
                        myAdapter2.setClickable(false);
                        myAdapter1.setClickable(true);

                        myAdapter1.addData(olderPosition, list2.get(position));
                        myAdapter2.removeData(position);
                    }
                });
                animatorSet.start();
            }
        });
    }

    private int getStatusBarHeight() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
