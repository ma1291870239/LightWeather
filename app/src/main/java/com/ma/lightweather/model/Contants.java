package com.ma.lightweather.model;

/**
 * Created by Ma-PC on 2016/12/12.
 */
public class Contants {

    //中国和全球付费
    //public final static String BASE_URL="https://api.heweather.com/v5/";
    //中国免费
    public final static String BASE_URL="https://free-api.heweather.com/v5/";
    //全部数据
    public final static String WEATHER_ALL=BASE_URL+"weather?city=";
    //实况天气
    public final static String WEATHER_NOW=BASE_URL+"now?city=";
    //未来天气
    public final static String WEATHER_FETURE=BASE_URL+"forecast?city=";
    //生活指数
    public final static String WEATHER_LIFE_=BASE_URL+"suggestion?city=";
    //灾害预警
    public final static String WEATHER_ALERT=BASE_URL+"alarm?city=";
    //认证密钥
    public final static String KEY="&key=4ddad270cdb94639ae5f4cfe11aadf82";
    //bing图片
    public final static String BINGURL="http://area.sinaapp.com/bingImg/";

    public final static String CITY="city";
    public final static String TMP="tmp";
    public final static String TXT="txt";
    public final static String MODEL="model";
    public final static String LOCTION="loction";
    public final static String WEATHER="weather";
}
