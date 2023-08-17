package com.ma.lightweather.model;

import java.io.Serializable

data class CYWeather(
    var status: String="",
    var server_time:String="",
    var location: ArrayList<CYLocation> = arrayListOf(),
    var result: CYResult = CYResult(),
) :Serializable{

    data class CYResult(
        var realtime:CYRealtime= CYRealtime(),
        var hourly:CYHourly= CYHourly(),
        var daily:CYDaily= CYDaily(),
    ):Serializable

    data class CYLocation(
        var lat:String="",
        var lon:String="",
    ):Serializable

    data class CYRealtime(
        var air_quality: AirQuality= AirQuality(),
        var apparent_temperature: String="",
        var cloudrate: String="",
        var dswrf: String="",
        var humidity:  String="",
        var life_index: LifeIndex= LifeIndex(),
        var precipitation: Precipitation=Precipitation(),
        var pressure:  String="",
        var skycon:  String="",
        var status:  String="",
        var temperature:  String="",
        var visibility:  String="",
        var wind: Wind= Wind(),
    ):Serializable

    data class CYHourly(
        var air_quality: AirQuality=AirQuality(),
        var apparent_temperature: List<ApparentTemperature> = arrayListOf(),
        var cloudrate: List<Cloudrate> = arrayListOf(),
        var description:  String="",
        var dswrf: List<Dswrf> = arrayListOf(),
        var humidity: List<Humidity> = arrayListOf(),
        var precipitation: List<Precipitation> = arrayListOf(),
        var pressure: List<Pressure> = arrayListOf(),
        var skycon: List<Skycon> = arrayListOf(),
        var status:  String="",
        var temperature: List<Temperature> = arrayListOf(),
        var visibility: List<Visibility> = arrayListOf(),
        var wind: List<Wind> = arrayListOf()
    ):Serializable

    data class CYDaily(
        var air_quality: AirQuality= AirQuality(),
        var astro: List<Astro> = arrayListOf(),
        var cloudrate: List<Cloudrate> = arrayListOf(),
        var dswrf: List<Dswrf> = arrayListOf(),
        var humidity: List<Humidity> = arrayListOf(),
        var life_index: LifeIndex= LifeIndex(),
        var precipitation: List<Precipitation> = arrayListOf(),
        var precipitation_08h_20h: List<Precipitation> = arrayListOf(),
        var precipitation_20h_32h: List<Precipitation> = arrayListOf(),
        var pressure: List<Pressure> = arrayListOf(),
        var skycon: List<Skycon> = arrayListOf(),
        var skycon_08h_20h: List<Skycon> = arrayListOf(),
        var skycon_20h_32h: List<Skycon> = arrayListOf(),
        var status:  String="",
        var temperature: List<Temperature> = arrayListOf(),
        var temperature_08h_20h: List<Temperature> = arrayListOf(),
        var temperature_20h_32h: List<Temperature> = arrayListOf(),
        var visibility: List<Visibility> = arrayListOf(),
        var wind: List<Wind> = arrayListOf(),
        var wind_08h_20h: List<Wind> = arrayListOf(),
        var wind_20h_32h: List<Wind> = arrayListOf(),
    ):Serializable


    data class AirQuality(
        var aqi: Aqi= Aqi(),
        var co:  String="",
        var description: Description= Description(),
        var no2:  String="",
        var o3:  String="",
        var pm10:  String="",
        var pm25:  String="",
        var so2:  String=""
    ):Serializable

    data class LifeIndex(
        var comfort: Comfort= Comfort(),
        var ultraviolet: Ultraviolet= Ultraviolet(),
    ):Serializable

    data class Precipitation(
        var local: Local= Local(),
        var nearest: Nearest= Nearest(),
    ):Serializable

    data class Wind(
        var direction:  String="",
        var speed:  String="",
    ):Serializable

    data class Aqi(
        var chn:  String="",
        var usa:  String="",
    ):Serializable

    data class Description(
        var chn:  String="",
        var usa: String="",
    ):Serializable

    data class Comfort(
        var desc:  String="",
        var index:  String="",
    ):Serializable

    data class Ultraviolet(
        var desc:  String="",
        var index:  String="",
    ):Serializable

    data class Local(
        var datasource:  String="",
        var Stringensity:  String="",
        var status: String="",
    ):Serializable

    data class Nearest(
        var distance:  String="",
        var Stringensity:  String="",
        var status: String="",
    ):Serializable

    data class ApparentTemperature(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Cloudrate(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Dswrf(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Humidity(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Pressure(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Skycon(
        var datetime:  String="",
        var varue: String="",
    ):Serializable

    data class Temperature(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable

    data class Visibility(
        var datetime:  String="",
        var varue:  String="",
    ):Serializable
    
    data class Astro(
        var date:  String="",
        var sunrise: Sunrise= Sunrise(),
        var sunset: Sunset= Sunset(),
    ):Serializable

    data class Sunrise(
        var time: String="",
    ):Serializable

    data class Sunset(
        var time: String="",
    ):Serializable

}
