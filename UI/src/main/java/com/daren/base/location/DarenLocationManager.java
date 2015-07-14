package com.daren.base.location;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.daren.base.util.BusProvider;

/**
 * 定位工具类
 */
public class DarenLocationManager {

    public static final String LOG = DarenLocationManager.class.getSimpleName();

    public static final String MESSAGE_LOCATION_SUCCESSED = "MESSAGE_LOCATION_SUCCESSED";
    public static final String MESSAGE_LOCATION_FAILED = "MESSAGE_LOCATION_FAILED";

    private LocationClient mLocClient;
    private MyLocationListenner myListener;
    private Context mContext;
    private android.location.LocationManager locationManager = null;
    private WakeLock wakeLock = null;

    public DarenLocationManager(Context mContext) {
        this.mContext = mContext;
        mLocClient = new LocationClient(mContext);
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
    }

    /**
     * 设置定位参数
     */
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(5000);
        option.setCoorType("bd09ll");
        mLocClient.setLocOption(option);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        releaseWakeLock(wakeLock);
        wakeLock = aquireWakeLock();
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }
        mLocClient.requestLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        releaseWakeLock(wakeLock);
        mLocClient.stop();
    }

    /**
     * 监听函数，有新位置的时候通知需要定位的类去更新UI
     */
    class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                updateLocation(location);
            } else {
                if (!mLocClient.isStarted()) {
                    mLocClient.start();
                }
                setLocationOption();
                mLocClient.requestLocation();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation != null) {
                updateLocation(poiLocation);
            } else {
                if (!mLocClient.isStarted()) {
                    mLocClient.start();
                }
                setLocationOption();
                mLocClient.requestLocation();
            }

        }
    }

    /**
     * 定位后更新位置
     *
     * @param location
     */
    private void updateLocation(BDLocation location) {

        if (location.getLatitude() > 1 && location.getLongitude() > 1) {
            LocationChangeEvent event = new LocationChangeEvent();

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            event.setLat(location.getLatitude());
            event.setLng(location.getLongitude());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                event.setAddress(location.getAddrStr());
            }

            Log.e(LOG, "-------------------------------");
            Log.e(LOG, sb.toString());
            Log.e(LOG, "-------------------------------");

            BusProvider.getInstance().post(event);

            Intent intent = new Intent(MESSAGE_LOCATION_SUCCESSED);
            mContext.sendBroadcast(intent);
        } else {
            BusProvider.getInstance().post(new LocationChangeEvent());

            Intent intent = new Intent(MESSAGE_LOCATION_FAILED);
            mContext.sendBroadcast(intent);
        }
    }

    public boolean isGPSEnabled(Context mContext) {
        if (locationManager == null) {
            locationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    private WakeLock aquireWakeLock() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        wakeLock.acquire();
        return wakeLock;
    }

    private static void releaseWakeLock(WakeLock wakeLock) {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
