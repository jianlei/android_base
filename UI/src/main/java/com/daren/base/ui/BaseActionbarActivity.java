package com.daren.base.ui;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * actionbar 自定义activity
 * <p/>
 * Created by daren on 14-9-27.
 */
public class BaseActionbarActivity extends SystemBarTintActivity {

    private Toolbar mToolbar;
//    private LinearLayout mToolbarLy;
    private LayoutInflater mInflater;

    //actionbar 标题
    private TextView mTitleTextView;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setCustomTitle(int titleId) {
        setCustomTitle(getString(titleId));
    }

    protected void setCustomTitle(String title) {
        if (mTitleTextView != null && !TextUtils.isEmpty(title)) {
            mTitleTextView.setText(title);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        addToolBarAtTop();
        initToolBar();
        initSystemBar();
    }


    @Override
    protected View getTopBarView() {
        return mToolbar;
    }

    private void addToolBarAtTop() {
        View toolbarView = LayoutInflater.from(this).inflate(R.layout.top_bar, null);
        Window win = getWindow();
        ViewGroup contentView = (ViewGroup) win.getDecorView().findViewById(android.R.id.content);
        ViewGroup rootView = (ViewGroup) contentView.getChildAt(0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
//            rootView.setFitsSystemWindows(true);
//        }
        if (rootView instanceof RelativeLayout){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rootView.addView(toolbarView, lp);
        }else{
            ((ViewGroup) contentView.getChildAt(0)).addView(toolbarView, 0);
        }
    }

    /**
     * 初始化toolbar
     */
    protected void initToolBar() {
//        mToolbarLy = (LinearLayout) findViewById(R.id.toolbar_ly);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.home_as_up_indicator_bg);
        } else {

        }

        mTitleTextView = (TextView) findViewById(R.id.title);
        if (mTitleTextView != null) {
            mTitleTextView.setText(getTitle());
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addToolBarAtTop();
//        mActionBar = getSupportActionBar();
//
//        mActionBar.setIcon(null);
//
//        mInflater = getLayoutInflater();
//
//        //设置图片居中
//        View customView = mInflater.inflate(R.layout.actionbar_custom_title, null, false);
//        mTitleTextView = (TextView) customView.findViewById(R.id.text_title);
//        customView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
////        customView.setBackgroundColor(getResources().getColor(R.color.red));
//        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.MATCH_PARENT,
//                Gravity.CENTER);
//
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setCustomView(customView,lp);
//        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM| ActionBar.DISPLAY_HOME_AS_UP);
//
//        mActionBar.setTitle(null);
//        mActionBar.setDisplayShowTitleEnabled(true);
//        mActionBar.setDisplayShowHomeEnabled(true);
//
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setHomeAsUpIndicator(R.drawable.home_as_up_indicator);

//        initOverflowMenu();
    }

    /**
     * 由于我们使用的是Actionbar menu, 需要屏蔽实体菜单键
     */
    private void initOverflowMenu() {
        try {
            ViewConfiguration mconfig = ViewConfiguration.get(this);
            Field menuKeyField =
                    ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(mconfig, false);
            }
        } catch (Exception ex) {
//            ZsywLog.logV(ex.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
