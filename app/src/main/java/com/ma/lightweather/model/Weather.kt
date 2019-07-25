package com.ma.lightweather.model

/**
 * Created by Ma-PC on 2016/12/13.
 */
data class Weather(
        var status:String="",
        var now: NowWeather= NowWeather(),
        var basic: BasicWeather= BasicWeather(),
        var update: UpdateWeather= UpdateWeather(),
        var daily_forecast: List<DailyWeather> = arrayListOf(),
        var hourly: ArrayList<HourlyWeather> = arrayListOf(),
        var lifestyle: ArrayList<LifeWeather> = arrayListOf()
) {

    data class NowWeather(
            var cond_code: String="",
            var cond_txt: String="",
            var fl: String="",
            var hum: String="",
            var pcpn: String="",
            var pres: String="",
            var tmp: String="",
            var vis: String="",
            var wind_deg: String="",
            var wind_dir: String="",
            var wind_sc: String="",
            var wind_spd: String=""
    )

    data class BasicWeather(
            var cid: String="",
            var location: String="",
            var parent_city: String="",
            var admin_area: String="",
            var cnty: String="",
            var lat: String="",
            var lon: String="",
            var tz: String=""
    )

    data class UpdateWeather(
            var loc: String="",
            var utc: String=""
    )

    data class DailyWeather(
            var cond_code_d: String="",
            var cond_code_n: String="",
            var cond_txt_d: String="",
            var cond_txt_n: String="",
            var date: String="",
            var hum: String="",
            var pcpn: String="",
            var pop: String="",
            var pres: String="",
            var tmp_max: String="",
            var tmp_min: String="",
            var uv_index: String="",
            var vis: String="",
            var wind_deg: String="",
            var wind_dir: String="",
            var wind_sc: String="",
            var wind_spd: String=""
    )

    data class HourlyWeather(
            var cloud: String="",
            var cond_code: String="",
            var cond_txt: String="",
            var hum: String="",
            var pop: String="",
            var pres: String="",
            var time: String="",
            var tmp: String="",
            var wind_deg: String="",
            var wind_dir: String="",
            var wind_sc: String="",
            var wind_spd: String=""
    )

    data class LifeWeather(
            var brf: String="",
            var txt: String="",
            var type: String=""
    )
}
