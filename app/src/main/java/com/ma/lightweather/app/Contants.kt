package com.ma.lightweather.app

/**
 * Created by Ma-PC on 2016/12/12.
 */
object Contants {

    //常规天气
    const val WEATHER_ALL ="https://free-api.heweather.com/s6/weather?key=4ddad270cdb94639ae5f4cfe11aadf82&location="
    //空气质量
    const val WEATHER_AIR = "https://free-api.heweather.net/s6/air/now?key=4ddad270cdb94639ae5f4cfe11aadf82&location="
    //行政区域
    const val AREAURL = "https://restapi.amap.com/v3/config/district?key=233dae1280c8b4c934e1a3928fdb1f10&subdistrict=3&extensions=base&keywords="
    //必应图片
    const val BINGURL = "http://guolin.tech/api/bing_pic"

    const val WEATHER_JSON = "weather_json"
    const val WEATHER_AQI_JSON = "weather_aqi_json"
    const val TMP = "tmp"
    const val TXT = "txt"
    const val DIR = "dir"
    const val SC = "sc"
    const val DATE = "date"
    const val QLTY = "qlty"
    const val MODEL = "model"
    const val LOCTION = "loction"
    const val WEATHER = "weather"
    const val WEATHERDB = "Weather.db"
    const val CITY = "cityName"
    const val CITYCODE = "cityCode"
    const val THEME = "themetag"
    const val NOTIFY = "notify"
    const val STATUS = "status"
    const val LIFE = "life"
    const val OLDVERSION = "version"
    const val BING = "bing"
    const val BINGPATH = "bingpath"

    //和风 KEY
    const val HF_WEATHER_KEY ="dbccdd9686e54c739cea4735879b7ae8"
    //和风 城市天气
    const val HF_WEATHER_AREA ="https://geoapi.qweather.com/v2/city/lookup?key=$HF_WEATHER_KEY&location="
    //和风 实时天气
    const val HF_WEATHER_NOW ="https://devapi.qweather.com/v7/weather/now?key=$HF_WEATHER_KEY&location="
    //和风 未来天气
    const val HF_WEATHER_FUTURE = "https://devapi.qweather.com/v7/weather/7d?key=$HF_WEATHER_KEY&location="
    //和风  24小时
    const val HF_WEATHER_HOUR = "https://devapi.qweather.com/v7/weather/24h?key=$HF_WEATHER_KEY&location="
    //彩云 KEY
    const val CY_WEATHER_KEY ="7iuyQv4ve1RAEBpS"
    //彩云 全部天气
    const val CY_WEATHER_AREA ="https://api.caiyunapp.com/v2.6/$CY_WEATHER_KEY/116.3176,39.9760/weather?alert=true&dailysteps=1&hourlysteps=24"
}
