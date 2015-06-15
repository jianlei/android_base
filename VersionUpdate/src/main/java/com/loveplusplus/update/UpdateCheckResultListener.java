package com.loveplusplus.update;

/**
 * Description:获取服务器版本数据之后的毁掉函数
 * Created by wangjianlei on 15/6/6.
 */
public interface UpdateCheckResultListener {
    /**
     * 第三方应用在此方法中判断是否有新应用
     * @param versionContent
     */
    void handleVersionResult(String versionContent);
}
