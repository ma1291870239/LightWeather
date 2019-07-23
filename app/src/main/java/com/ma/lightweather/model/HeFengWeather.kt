package com.ma.lightweather.model

class HeFengWeather : Bean() {
    var heWeather6: List<HeWeather>? = null

    inner class HeWeather : Bean() {
        var basic: Basic? = null
        var uodate: Update? = null
        var now: Now? = null
        var daily_forecast: List<Daily>? = null
        var hourly: List<Hourly>? = null
        var lifestyle: List<Lifestyle>? = null
    }

    inner class Basic : Bean() {
        var cid: String? = null
        var location: String? = null
        var parent_city: String? = null
        var admin_area: String? = null
        var cnty: String? = null
        var lat: String? = null
        var lon: String? = null
        var tz: String? = null
    }

    inner class Update : Bean() {
        var loc: String? = null
        var utc: String? = null
    }

    inner class Now : Bean() {
        var cloud: String? = null
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

    inner class Daily : Bean() {
        var cond_code_d: String? = null
        var cond_code_n: String? = null
        var cond_txt_d: String? = null
        var cond_txt_n: String? = null
        var date: String? = null
        var hum: String? = null
        var mr: String? = null
        var ms: String? = null
        var pcpn: String? = null
        var pop: String? = null
        var pres: String? = null
        var sr: String? = null
        var ss: String? = null
        var tmp_max: Int = 0
        var tmp_min: Int = 0
        var uv_index: String? = null
        var vis: String? = null
        var wind_deg: String? = null
        var wind_dir: String? = null
        var wind_sc: String? = null
        var wind_spd: String? = null
    }

    inner class Hourly : Bean() {
        var cloud: String? = null
        var cond_code: String? = null
        var cond_txt: String? = null
        var hum: String? = null
        var pop: String? = null
        var pres: String? = null
        var time: String? = null
        var tmp: Int = 0
        var wind_deg: String? = null
        var wind_dir: String? = null
        var wind_sc: String? = null
        var wind_spd: String? = null
    }

    inner class Lifestyle : Bean() {
        var type: String? = null
        var brf: String? = null
        var txt: String? = null
    }
}
