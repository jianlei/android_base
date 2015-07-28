package com.daren.base.ui;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * 用于达到在高于4.4版本的系统中 状态栏颜色颜色跟标题栏一致
 * <p/>
 * Created by daren on 14-9-27.
 */
public class SystemBarTintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止屏幕横屏显示
        super.onCreate(savedInstanceState);
    }

    @TargetApi (19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }

    protected void initSystemBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.status_bar_color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getTopBarView() != null) {
            setTranslucentStatus(true);

//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//            getTopBarView().setPadding(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());


//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());
//            getTopBarView().setLayoutParams(lp);
        }
        Window win = getWindow();
        ViewGroup contentView = (ViewGroup) win.getDecorView().findViewById(android.R.id.content);
        ViewGroup rootView = (ViewGroup) contentView.getChildAt(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            rootView.setFitsSystemWindows(true);
        }
    }


    /**
     * 默认没有topbar view
     *
     * @return
     */
    protected View getTopBarView() {
        return null;
    }
}
