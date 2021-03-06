package com.daren.base.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by daren on 15/3/25.
 */
public class Utils {
    /**
     * 隐藏键盘输入法
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取应用程序是否在运行
     *
     * @param context
     * @return
     */
    public static boolean getApplicationIsRuning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        String packageName = getAppPackageName(context);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)) {
                return true;
            } else
                return false;
        }
        return false;
    }

    /**
     * 获取正在运行的activity，或者位于栈顶的activity
     *
     * @param context
     * @return
     */
    public static String getRuningActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        return component.getClassName();
    }

    /**
     * 判断某个应用是否安装了
     *
     * @param context
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e1) {
            //The app isn't installed.
            installed = false;
        }
        return installed;
    }

    /**
     * check availability of Internet
     *
     * @param context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null)
            return false;
        if (!netInfo.isConnected())
            return false;
        if (!netInfo.isAvailable())
            return false;
        return true;

//        Runtime runtime = Runtime.getRuntime();
//        try {
//
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 www.baidu.com");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        return false;
    }

//    public static final boolean ping() {
//
//        String result = null;
//        try {
//            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
//            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ip);// ping网址3次
//            // 读取ping的内容，可以不加
//            InputStream input = p.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(input));
//            StringBuffer stringBuffer = new StringBuffer();
//            String content = "";
//            while ((content = in.readLine()) != null) {
//                stringBuffer.append(content);
//            }
//            Log.d("------ping-----", "result content : " + stringBuffer.toString());
//            // ping的状态
//            int status = p.waitFor();
//            if (status == 0) {
//                result = "success";
//                return true;
//            } else {
//                result = "failed";
//            }
//        } catch (IOException e) {
//            result = "IOException";
//        } catch (InterruptedException e) {
//            result = "InterruptedException";
//        } finally {
//            Log.d("----result---", "result = " + result);
//        }
//        return false;
//    }

    /**
     * 启动一个activity
     *
     * @param context
     * @param kclass
     */
    public static void startActivity(Context context, Class kclass) {
        Intent intent = new Intent(context, kclass);
        context.startActivity(intent);
    }

    /**
     * 启动一个带result的activity
     *
     * @param context
     * @param kclass
     * @param requestCode
     */
    public static void startActivity(Context context, Class kclass, int requestCode) {
        Intent intent = new Intent(context, kclass);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 启动一个activity
     *
     * @param context
     * @param kclass
     */
    public static void startActivity(Context context, Class kclass, Bundle bundle) {
        Intent intent = new Intent(context, kclass);
        intent.putExtra("bundle", bundle);

        context.startActivity(intent);
    }

    /**
     * 获取版本号码
     *
     * @param context
     * @return
     */
    public static String getPackageVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取一个唯一标示字符串
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }


    public static String parseStringFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static Date parseDateFromString(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        return dm;
    }


    /**
     * 从intent bundle中获取指定key对应的对象值
     *
     * @param key
     * @param type
     * @param intent
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromIntentBundle(String key, Class<T> type, Intent intent) {

        if (intent == null)
            return null;
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null)
            return null;
        Serializable serializable = bundle.getSerializable(key);
        if (serializable == null)
            return null;

        return (T) serializable;

    }

    public static <T extends Parcelable> ArrayList<T> getArrayListFromIntentBundle(String key,
                                                                                   Intent intent) {
        if (intent == null)
            return null;
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null)
            return null;
        return bundle.getParcelableArrayList(key);
    }


    /**
     * 通过包名获取应用程序的名称。
     *
     * @param context Context对象。
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(getAppPackageName(context),
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 遍历数组，把对象总相应的值拼接起来
     * <p/>
     * 类似于“a,b,c,d”
     *
     * @param delimiter
     * @param tokens
     * @param filedName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static String joinWithCertainFiled(CharSequence delimiter, Iterable tokens, String filedName) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        try {
            for (Object token : tokens) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }


                sb.append(Reflector.getValue(token, filedName));

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 获取相对时间字符串
     *
     * @param now
     * @param time
     * @return
     */
    public static String getRelativeTime(long now, long time) {
        return DateUtils.getRelativeTimeSpanString(time, now,
                DateUtils.FORMAT_ABBREV_ALL).toString();
    }

    /**
     * 获取手机电量
     *
     * @param context
     * @return 返回的是电量的百分比
     */
    public static double getBatteryLevel(Context context) {
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int rawlevel = batteryIntent.getIntExtra("level", -1);
        double scale = batteryIntent.getIntExtra("scale", -1);
        double level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = rawlevel / scale;
        }
        return level * 100;
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return "";
        }
        String end = fileName.substring(dotIndex  + 1, fileName.length()).toLowerCase();
        return end;
    }
}
