package com.daren.base.ui;

import android.os.Bundle;
import android.view.View;

/**
 * 此类废弃，不要在使用了
 * Created by wjl on 14-4-8.
 */
@Deprecated()
public abstract class BaseActivity extends BaseActionbarActivity {

    private String token = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(setContentViewResId());
        initView();
        initData();

    }

    @SuppressWarnings ("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();


    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        if (RongIM.getInstance() == null) {
//            if (savedInstanceState.containsKey("RONG_TOKEN")) {
//                token = DemoContext.getInstance().getSharedPreferences().getString("LOGIN_TOKEN", null);
//                reconnect(token);
//            }
//        }
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {

//            token = DemoContext.getInstance().getSharedPreferences().getString("LOGIN_TOKEN", null);
//        bundle.putString("RONG_TOKEN", token);
        super.onSaveInstanceState(bundle);
    }

//    private void reconnect(String token) {
//        try {
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//                @Override
//                public void onSuccess(String userId) {
//                    Log.e("ddd", "----------- BAse onSuccess:");
//                }
//
//                @Override
//                public void onError(final ErrorCode errorCode) {
//                    Log.e("ddd", "----------- BAse errorCode:");
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}