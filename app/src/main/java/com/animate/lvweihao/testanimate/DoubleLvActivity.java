package com.animate.lvweihao.testanimate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;
import static android.os.Build.VERSION_CODES.M;

public class DoubleLvActivity extends AppCompatActivity {

    public static ArrayList<Map<String, String>> mLeftData = new ArrayList<Map<String, String>>() {
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

    private mListView listView2;
    private ListView listView1;
    private mAdapter adapter1 = new mAdapter(mLeftData, R.layout.item_listview);
    private List<Map<String, String>> list2 = new ArrayList<>();
    private mAdapter adapter2;
    private int olderPos;
    private int mOlderIndex;
    private int olderPosition;
    private View item;
    private Animation animation;
    private int endX, startY, endY;
    private LinearLayout mContent;
    public int itemHeight;
    private Map<String, String> map;
    private Animation listAnimate,transformAnim;
    private SwipeLinearLayout swipeLinearLayout;
    private int res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_listview);

//        listAnimate = new TranslateAnimation(0,0,startY+itemHeight,startY);
        listAnimate = new AlphaAnimation(1,0);
        listAnimate.setDuration(500);



        swipeLinearLayout = (SwipeLinearLayout) findViewById(R.id.sll);
       adapter2 = new mAdapter(list2, R.layout.item_listview);


        mContent = (LinearLayout) findViewById(R.id.line_layout);
        endX = mContent.getChildAt(0).getLayoutParams().width + mContent.getChildAt(1).getLayoutParams().width;
        item = findViewById(R.id.ll);
        animation = AnimationUtils.loadAnimation(this, R.anim.animate_item_gone);

        mContent.getChildAt(1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                    if (swipeLinearLayout.isShow) {
                        res = R.layout.item_listview;
                    } else {
                        res = R.layout.item_listview;
                    }
                    adapter2 = new mAdapter(list2, res);
                    listView2.setAdapter(adapter2);

                return false;
            }
        });



        listView1 = (ListView) findViewById(R.id.lv1);
        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                olderPos = position;

                int Pos[] = { -1, -1 };
                view.getLocationOnScreen(Pos);
                startY = Pos[1] - getStatusBarHeight();
                endY = list2.size() * itemHeight;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, endX, startY, endY);
                translateAnimation.setDuration(1000);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(animation);
                animationSet.setDuration(500);
                item.startAnimation(animationSet);



                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        doTransformAnim(view,position);

//                        view.startAnimation(listAnimate);

                        listView1.setEnabled(false);
                        map= mLeftData.get(position);
//                        mLeftData.remove(position);
//                        adapter1.notifyDataSetChanged();
                        item.setVisibility(View.VISIBLE);
                        TextView tv = (TextView) item.findViewById(R.id.tv);
                        tv.setText(map.toString());
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        listView1.setEnabled(true);
                        list2.add(map);
                        adapter2.notifyDataSetChanged();
                        item.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });


        listView2 = (mListView) findViewById(R.id.lv2);
        listView2.setAdapter(adapter2);
        listView2.setDividerHeight(5);
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

                mLeftData.add(olderPosition, list2.get(position));
                adapter1.notifyDataSetChanged();
                list2.remove(position);
                adapter2.notifyDataSetChanged();
            }
        });


    }


    class mAdapter extends BaseAdapter{
        private List<Map<String, String>> arylist;
        private int resouce;
        public mAdapter(List<Map<String, String>> list, int resouce){
            this.arylist = list;
            this.resouce =resouce;
        }

        @Override
        public int getCount() {
            return arylist.size();
        }

        @Override
        public Object getItem(int position) {
            return arylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resouce, null);
                convertView.measure(0, 0);
                itemHeight = convertView.getMeasuredHeight();
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
            tv.setText(getItem(position).toString());
            tv2.setText(arylist.get(position).get("OLD_INDEX"));

            return convertView;
        }
    }


    private int getStatusBarHeight(){
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            return rect.top;
        }


    public void doTransformAnim(final View v, final int position){
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

            listAnimate = new AlphaAnimation(1,1);
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
                    adapter1.notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animationSet);
        }
}
