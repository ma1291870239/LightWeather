package com.ma.lightweather.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.Weather
import com.ma.lightweather.widget.HourWeatherView
import com.ma.lightweather.widget.WeatherView


/**
 * Created by Ma-PC on 2016/12/13.
 */
object Parse {

    private var weatherList: MutableList<Weather> = arrayListOf()
    private var airList: MutableList<Air> = arrayListOf()

    fun parseWeather(resource: String, weatherView: WeatherView?, hourWeatherView: HourWeatherView?, context: Context): List<Weather>? {
        weatherList.clear()
        val jsonObject = JsonParser().parse(resource).asJsonObject
        //再转JsonArray 加上数据头
        val jsonArray = jsonObject.getAsJsonArray("HeWeather6")
        val gson=Gson()
        for (weather in jsonArray) {
            val weatherBean:Weather = gson.fromJson(weather, object : TypeToken<Weather>(){}.type)
            (weatherList as ArrayList<Weather>).add(weatherBean)
            if(weatherBean.status== "ok") {
                DbUtils.writeDb(context, weatherList, airList)
                weatherView?.loadViewData(weatherBean.daily_forecast)
                hourWeatherView?.loadViewData(weatherBean.hourly)
            }
        }
        return weatherList
    }

    fun parseAir(resource: String, weatherView: WeatherView?, hourWeatherView: HourWeatherView?, context: Context): List<Air>? {
        airList.clear()
        val jsonObject = JsonParser().parse(resource).asJsonObject
        //再转JsonArray 加上数据头
        val jsonArray = jsonObject.getAsJsonArray("HeWeather6")
        val gson=Gson()
        for (air in jsonArray) {
            val airBean:Air = gson.fromJson(air, object : TypeToken<Air>(){}.type)
            (airList as ArrayList<Air>).add(airBean)
            if(airBean.status== "ok") {
                //DbUtils.writeDb(context, airList!!)
                //weatherView?.loadViewData(weatherBean.daily_forecast)
                //hourWeatherView?.loadViewData(weatherBean.hourly)
            }
        }
        return airList
    }
}
