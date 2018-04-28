package com.animate.lvweihao.testanimate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mCoinIv;
    private ImageView mWalletIv;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                    setAnimation();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        setAnimation();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendEmptyMessage(msg.what);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
    }

    private void initViews() {
        mCoinIv = (ImageView) findViewById(R.id.coin_iv);
        mWalletIv = (ImageView) findViewById(R.id.wallet_iv);
        Button startBtn = (Button) findViewById(R.id.start_btn);
        assert startBtn != null;
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimation();
            }
        });
    }

    private void setAnimation() {
        startCoin();
        setWallet();
    }

    private void startCoin() {
        Animation animationTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        ThreeDRotateAnimation animation3D = new ThreeDRotateAnimation();
        animation3D.setRepeatCount(10);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(800);
        animationSet.addAnimation(animation3D);
        animationSet.addAnimation(animationTranslate);
        mCoinIv.startAnimation(animationSet);
    }

    private void setWallet() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction >= 0.75) {
                    valueAnimator.cancel();
                    startWallet();
                }
            }
        });
        valueAnimator.start();
    }

    private void startWallet() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mWalletIv, "scaleX", 1, 1.1f, 0.9f, 1);
        objectAnimator1.setDuration(600);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mWalletIv, "scaleY", 1, 0.75f, 1.25f, 1);
        objectAnimator2.setDuration(600);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.start();
    }
}
