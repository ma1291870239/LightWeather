package com.ma.lightweather.model

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by Ma-PC on 2016/12/13.
 */
class Weather : Serializable {
    var tmp: String? = null//温度
    var feel: String? = null//体感
    var hum: String? = null//相对湿度
    var pcpn: String? = null//降水量
    var city: String? = null//城市
    var cnty: String? = null//国家
    var dir: String? = null//风向
    var txt: String? = null//天气
    var wind: String? = null//风速
    var pres: String? = null//气压
    var vis: String? = null//能见度
    var maxList: List<Int> = ArrayList()
    var minList: List<Int> = ArrayList()
    var txtList: List<String> = ArrayList()
    var dirList: List<String> = ArrayList()
    var dateList: List<String> = ArrayList()


    var hourTmpList: List<Int> = ArrayList()
    var hourPopList: List<Int> = ArrayList()
    var hourTxtList: List<String> = ArrayList()
    var hourDirList: List<String> = ArrayList()
    var hourDateList: List<String> = ArrayList()

    var lifeTypeList: List<String> = ArrayList()
    var lifeTxtList: List<String> = ArrayList()
    var lifeBrfList: List<String> = ArrayList()


    var heWeather6: List<WeatherBean>? = null

    inner class WeatherBean : Serializable {
        var now: NowWeather? = null
        var basic: BasicWeather? = null
        var daily_forecast: List<DailyWeather>? = null
        var hourly: List<HourlyWeather>? = null
        var lifestyle: List<LifeWeather>? = null
    }

    inner class NowWeather : Serializable {
        var cond_code: String? = null
        var cond_txt: String? = null
        var fl: String? = null
        var hum: String? = null
        var pcpn: String? = null
        var pres: String? = null
        var tmp: String? = null
        var vis: String? = null
        var wind_deg: String? = null
        var wind_dir: String? = null
        var wind_sc: String? = null
        var wind_spd: String? = null
    }

    inner class BasicWeather : Serializable {
        var cid: String? = null
        var location: String? = null
        var parent_city: String? = null
        var admin_area: String? = null
        var cnty: String? = null
        var lat: String? = null
        var lon: String? = null
        var tz: String? = null
    }

    inner class DailyWeather : Serializable {
        var cond_code_d: String? = null
        var cond_code_n: String? = null
        var cond_txt_d: String? = null
        var cond_txt_n: String? = null
        var date: String? = null
        var hum: String? = null
        var pcpn: String? = null
        var pop: String? = null
        var pres: String? = null
        var tmp_max: String? = null
        var tmp_min: String? = null
        var uv_index: String? = null
        var vis: String? = null
        var wind_deg: String? = null
        var wind_dir: String? = null
        var wind_sc: String? = null
        var wind_spd: String? = null
    }

    inner class HourlyWeather : Serializable {
        var cloud: String? = null
        var cond_code: String? = null
        var cond_txt: String? = null
        var hum: String? = null
        var pop: String? = null
        var pres: String? = null
        var time: String? = null
        var tmp: String? = null
        var wind_deg: String? = null
        var wind_dir: String? = null
        var wind_sc: String? = null
        var wind_spd: String? = null
    }

    inner class LifeWeather : Serializable {
        var brf: String? = null
        var txt: String? = null
        var type: String? = null
    }
}
