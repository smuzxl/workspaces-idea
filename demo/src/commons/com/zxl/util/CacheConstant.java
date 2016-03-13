package com.zxl.util;


public interface CacheConstant {
    // 缓存区域
    public static final String REGION_OPENSEAT = "openSeat";
    public static final String REGION_HALFMIN = "halfMin"; // 30秒
    public static final String REGION_ONEMIN = "oneMin";
    public static final String REGION_TENMIN = "tenMin";
    public static final String REGION_TWENTYMIN = "twentyMin";
    public static final String REGION_HALFHOUR = "halfHour";
    public static final String REGION_ONEHOUR = "oneHour";
    public static final String REGION_TWOHOUR = "twoHour";
    public static final String REGION_HALFDAY = "halfDay";

    public static final String REGION_ONEDAY = "oneDay";
    public static final String REGION_LOGINKEY = "loginKey";
    public static final String REGION_LOGINAUTH = "loginAuth";

    public static final int SECONDS_ONEMIN = 60;
    public static final int SECONDS_TWOMIN = 60 * 2;
    public static final int SECONDS_TENMIN = 60 * 10;
    public static final int SECONDS_TWENTYMIN = 60 * 20;
    public static final int SECONDS_HALFHOUR = 60 * 30;
    public static final int SECONDS_ONEHOUR = 60 * 60;
    public static final int SECONDS_TWOHOUR = 60 * 60 * 2;
    public static final int SECONDS_OPENSEAT = 60 * 60 * 8;
    public static final int SECONDS_HALFDAY = 60 * 60 * 12;
    public static final int SECONDS_ONEDAY = 60 * 60 * 24;

}
