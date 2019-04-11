package com.ma.lightweather.app;

/**
 * Created by Ma-PC on 2016/12/12.
 */
public class Contants {

    //中国和全球付费
    //public final static String BASE_URL="https://api.heweather.com/v5/";
    //中国免费
    public final static String BASE_URL="https://free-api.heweather.com/s6/";
    //全部数据
    public final static String WEATHER_ALL=BASE_URL+"weather?key=4ddad270cdb94639ae5f4cfe11aadf82&location=";
    //必应图片
    public final static String BINGURL="http://guolin.tech/api/bing_pic";
    //百度逆地理
    public final static String BAIDUGETADDRESS="http://api.map.baidu.com/geocoder/v2/?" +
            "output=json" +
            "&pois=1" +
            "&latest_admin=1" +
            "&ak=fGsdz2W5duRKjf1Gafg2nQRhgkbm7Tad" +
            "&mcode=FD:DB:F3:34:1C:AF:37:38:10:8A:38:44:06:BC:52:10:D7:41:CD:69;com.ma.lightweather"+
            "&location=";

    public final static String CITY="city";
    public final static String TMP="tmp";
    public final static String TXT="txt";
    public final static String MODEL="model";
    public final static String LOCTION="loction";
    public final static String WEATHER="weather";
    public final static String CITYNAME="洛阳";
    public final static String THEME="themetag";
    public final static String NOTIFY="notify";
    public final static String STATUS="status";
    public final static String BINGIMG="bingimg";
    public final static String LIFE="life";
    public final static String FIRSTSHOW="firstshow";
}
