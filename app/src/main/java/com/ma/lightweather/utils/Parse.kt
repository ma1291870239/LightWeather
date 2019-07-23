package com.ma.lightweather.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ma.lightweather.model.Weather
import com.ma.lightweather.widget.HourWeatherView
import com.ma.lightweather.widget.WeatherView
import org.json.JSONException
import org.json.JSONObject
import java.util.*







/**
 * Created by Ma-PC on 2016/12/13.
 */
object Parse {

    private var weatherList: MutableList<Weather>? = null
    private var hfWeatherList: List<Weather.WeatherBean>? = null

    @Throws(JSONException::class)
    fun parseWeather(resource: String, weatherView: WeatherView?, hourWeatherView: HourWeatherView?, context: Context): List<Weather> {
        weatherList = ArrayList()
        val weather = Weather()
        val jsonObject = JSONObject(resource)
        if (!jsonObject.isNull("HeWeather6")) {
            val jsonArray = jsonObject.getJSONArray("HeWeather6")
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    val weatherObj = jsonArray.getJSONObject(i)

                    //城市信息
                    if (!weatherObj.isNull("basic")) {
                        val basicObj = weatherObj.getJSONObject("basic")
                        if (!basicObj.isNull("location"))
                            weather.city = basicObj.getString("location")
                        if (!basicObj.isNull("cnty"))
                            weather.cnty = basicObj.getString("cnty")
                    }
                    //实况天气
                    if (!weatherObj.isNull("now")) {
                        val nowObj = weatherObj.getJSONObject("now")
                        if (!nowObj.isNull("fl"))
                            weather.feel = nowObj.getString("fl")
                        if (!nowObj.isNull("hum"))
                            weather.hum = nowObj.getString("hum")
                        if (!nowObj.isNull("pcpn"))
                            weather.pcpn = nowObj.getString("pcpn")
                        if (!nowObj.isNull("pres"))
                            weather.pres = nowObj.getString("pres")
                        if (!nowObj.isNull("tmp"))
                            weather.tmp = nowObj.getString("tmp")
                        if (!nowObj.isNull("vis"))
                            weather.vis = nowObj.getString("vis")
                        if (!nowObj.isNull("cond_txt"))
                            weather.txt = nowObj.getString("cond_txt")
                        if (!nowObj.isNull("wind_dir"))
                            weather.dir = nowObj.getString("wind_dir")
                        if (!nowObj.isNull("wind_spd"))
                            weather.wind = nowObj.getString("wind_spd")
                    }
                    //未来七天
                    if (!weatherObj.isNull("daily_forecast")) {
                        val dailyArray = weatherObj.getJSONArray("daily_forecast")
                        if (dailyArray.length() > 0) {
                            for (j in 0 until dailyArray.length()) {
                                val dailyObj = dailyArray.getJSONObject(j)
                                if (!dailyObj.isNull("date"))
                                    weather.dateList.add(dailyObj.getString("date"))
                                if (!dailyObj.isNull("cond_txt_d"))
                                    weather.txtList.add(dailyObj.getString("cond_txt_d"))
                                if (!dailyObj.isNull("tmp_max"))
                                    weather.maxList.add(Integer.valueOf(dailyObj.getString("tmp_max")))
                                if (!dailyObj.isNull("tmp_min"))
                                    weather.minList.add(Integer.valueOf(dailyObj.getString("tmp_min")))
                                if (!dailyObj.isNull("wind_dir"))
                                    weather.dirList.add(dailyObj.getString("wind_dir"))
                            }
                        }
                    }

                    //实时天气
                    if (!weatherObj.isNull("hourly")) {
                        val hourArray = weatherObj.getJSONArray("hourly")
                        if (hourArray.length() > 0) {
                            for (j in 0 until hourArray.length()) {
                                val hourObj = hourArray.getJSONObject(j)
                                if (!hourObj.isNull("time"))
                                    weather.hourDateList.add(hourObj.getString("time"))
                                if (!hourObj.isNull("cond_txt"))
                                    weather.hourTxtList.add(hourObj.getString("cond_txt"))
                                if (!hourObj.isNull("pop"))
                                    weather.hourPopList.add(hourObj.getInt("pop"))
                                if (!hourObj.isNull("tmp"))
                                    weather.hourTmpList.add(hourObj.getInt("tmp"))
                                if (!hourObj.isNull("wind_dir"))
                                    weather.hourDirList.add(hourObj.getString("wind_dir"))
                            }
                        }
                    }

                    //实时天气
                    if (!weatherObj.isNull("lifestyle")) {
                        val lifeArray = weatherObj.getJSONArray("lifestyle")
                        if (lifeArray.length() > 0) {
                            for (j in 0 until lifeArray.length()) {
                                val lifeObj = lifeArray.getJSONObject(j)
                                if (!lifeObj.isNull("type"))
                                    weather.lifeTypeList.add(lifeObj.getString("type"))
                                if (!lifeObj.isNull("txt"))
                                    weather.lifeTxtList.add(lifeObj.getString("txt"))
                                if (!lifeObj.isNull("brf"))
                                    weather.lifeBrfList.add(lifeObj.getString("brf"))
                            }
                        }
                    }
                }
            }
        }

        if (weather.city != null) {
            weatherList!!.add(weather)
            //DbUtils.createdb(context, weatherList!!)
            //weatherView?.loadViewData(weather.maxList, weather.minList, weather.dateList, weather.txtList, weather.dirList)
            //hourWeatherView?.loadViewData(weather.hourTmpList, weather.hourPopList, weather.hourDateList, weather.hourTxtList, weather.hourDirList)
        }
        return weatherList!!
    }


    fun parse(resource: String, weatherView: WeatherView?, hourWeatherView: HourWeatherView?, context: Context): List<Weather.WeatherBean> {
        hfWeatherList = ArrayList()
        val jsonObject = JsonParser().parse(resource).asJsonObject
        //再转JsonArray 加上数据头
        val jsonArray = jsonObject.getAsJsonArray("HeWeather6")
        val gson=Gson()
        for (weather in jsonArray) {
            val weatherBean:Weather.WeatherBean = gson.fromJson(weather, object : TypeToken<Weather.WeatherBean>(){}.type)
            (hfWeatherList as ArrayList<Weather.WeatherBean>).add(weatherBean)
            DbUtils.createdb(context, hfWeatherList!!)
            weatherView?.loadViewData(weatherBean.daily_forecast)
            hourWeatherView?.loadViewData(weatherBean.hourly)
        }
        return hfWeatherList!!
    }
}
