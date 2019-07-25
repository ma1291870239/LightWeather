package com.ma.lightweather.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ma.lightweather.model.Weather
import com.ma.lightweather.widget.HourWeatherView
import com.ma.lightweather.widget.WeatherView
import java.util.*







/**
 * Created by Ma-PC on 2016/12/13.
 */
object Parse {

    private var hfWeatherList: List<Weather>? = null

    fun parse(resource: String, weatherView: WeatherView?, hourWeatherView: HourWeatherView?, context: Context): List<Weather> {
        hfWeatherList = ArrayList()
        val jsonObject = JsonParser().parse(resource).asJsonObject
        //再转JsonArray 加上数据头
        val jsonArray = jsonObject.getAsJsonArray("HeWeather6")
        val gson=Gson()
        for (weather in jsonArray) {
            val weatherBean:Weather = gson.fromJson(weather, object : TypeToken<Weather>(){}.type)
            (hfWeatherList as ArrayList<Weather>).add(weatherBean)
            DbUtils.writeDb(context, hfWeatherList!!)
            weatherView?.loadViewData(weatherBean.daily_forecast)
            hourWeatherView?.loadViewData(weatherBean.hourly)
        }
        return hfWeatherList!!
    }
}
