package com.animate.lvweihao.testanimate.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.animate.lvweihao.testanimate.R;

/**
 * Created by lv.weihao on 2016/9/21.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FrameLayout flm = (FrameLayout) findViewById(R.id.fl);
        LinearLayout flItem = (LinearLayout) getLayoutInflater().inflate(R.layout.m_fl_item, null);
        flm.addView(new TestLinearLayout(this,flItem));
    }
}
