package com.daren.base.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * @author wjl
 * @description 加载页面父类
 */
public abstract class DarenWelcomeActivity extends AppCompatActivity implements
        Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    @Override
    protected void onResume() {
        super.onResume();
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root_view);
        // AlphaAnimation
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.welcome_alpha);
        animation.setAnimationListener(this);
        rootView.startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//		Intent intent = new Intent();
//		intent.setClass(getApplicationContext(), MainActivity.class);
//		startActivity(intent);
//		finish();
        // 加载框
        onCustomAnimationEnd(animation);
    }

    public abstract void onCustomAnimationEnd(Animation animation);

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
