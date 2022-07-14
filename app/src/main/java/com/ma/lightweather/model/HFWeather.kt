package com.ma.lightweather.model

data class HFWeather(
    var code: String="",
    var updateTime:String="",
    var location: ArrayList<WeatherLocation> = arrayListOf(),
    var now:WeatherNow=WeatherNow(),
    var daily: ArrayList<WeatherFuture> =arrayListOf(),
    var hourly:ArrayList<WeatherHour> =arrayListOf()
) {

    data class WeatherLocation(
        var name:String="",
        var id:String="",
        var lat:String="",
        var lon:String="",
        var adm2:String="",
        var adm1:String="",
        var country:String="",
        var tz:String="",
        var utcOffset:String="",
        var isDst:String="",
        var type:String="",
        var rank:String="",
        var fxLink:String="",
    )

    data class WeatherNow(
        var obsTime:String="",
        var temp:String="",
        var feelsLike :String="",
        var icon :String="",
        var text:String="",
        var wind360 :String="",
        var windDir :String="",
        var windScale :String="",
        var windSpeed :String="",
        var humidity: String="",
        var precip: String="",
        var pressure: String="",
        var vis: String="",
        var cloud: String="",
        var dew: String="",
    )

    data class WeatherFuture(
        var fxDate: String="",
        var sunrise: String="",
        var sunset: String="",
        var moonrise: String="",
        var moonset: String="",
        var moonPhase: String="",
        var moonPhaseIcon: String="",
        var tempMax: String="",
        var tempMin: String="",
        var iconDay: String="",
        var textDay: String="",
        var iconNight: String="",
        var textNight: String="",
        var wind360Day: String="",
        var windDirDay: String="",
        var windScaleDay: String="",
        var windSpeedDay: String="",
        var wind360Night: String="",
        var windDirNight: String="",
        var windScaleNight: String="",
        var windSpeedNight: String="",
        var humidity: String="",
        var precip: String="",
        var pressure: String="",
        var vis: String="",
        var cloud: String="",
        var uvIndex: String="",
    )

    data class WeatherHour(
        var fxTime: String="",
        var temp: String="",
        var icon: String="",
        var text: String="",
        var wind360: String="",
        var windDir: String="",
        var windScale: String="",
        var windSpeed: String="",
        var humidity: String="",
        var pop: String="",
        var precip: String="",
        var pressure: String="",
        var cloud: String="",
        var dew: String="",
    )
}