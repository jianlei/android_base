package com.daren.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * Description:
 * Created by wangjianlei on 15/6/5.
 */
public class ApkUtils {

    /**
     * 显示调用安装软件activity
     * @param apkPath
     */
    private void showInstallAppActivity(Context context , String apkPath){
        if (TextUtils.isEmpty(apkPath)){
            return;
        }
        File apkFile = new File(apkPath);
        if (!apkFile.exists()){
            return;
        }
        Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);

        installAPKIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        installAPKIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installAPKIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        context.startActivity(installAPKIntent);
    }
}
