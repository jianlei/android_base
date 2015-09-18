package com.daren.base.location;

/**
 * Description: 定位位置发生改变的事件
 * Created by wangjianlei on 15/5/7.
 */
public class LocationChangeEvent {
    private double lng;//经度
    private double lat;//纬度
    private String address;
    private String time;//定位事件

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
