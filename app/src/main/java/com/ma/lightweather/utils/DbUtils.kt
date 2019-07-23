package com.ma.lightweather.utils

import android.content.ContentValues
import android.content.Context
import com.ma.lightweather.db.MydataBaseHelper
import com.ma.lightweather.model.Weather
import java.util.*

/**
 * Created by Ma on 2018/12/8.
 */

object DbUtils {

    fun selectdb(context: Context): List<Weather> {
        val weatherData = ArrayList<Weather>()
        val dbHelper = MydataBaseHelper(context, "Weather.db", null, 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("weather", null, null, null, null, null, "id desc")
        if (cursor.moveToFirst()) {
            do {
                val weather = Weather()
                weather.city = cursor.getString(cursor.getColumnIndex("city"))
                weather.tmp = cursor.getString(cursor.getColumnIndex("tmp"))
                weather.txt = cursor.getString(cursor.getColumnIndex("txt"))
                weather.dir = cursor.getString(cursor.getColumnIndex("dir"))
                if (weather.city != null) {
                    weatherData.add(weather)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return weatherData
    }

    fun createdb(context: Context, weatherList: List<Weather.WeatherBean>) {
        val dbHelper = MydataBaseHelper(context, "Weather.db", null, 1)
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        for (i in weatherList.indices) {
            val values = ContentValues()
            values.put("city", weatherList[i].basic?.location)
            values.put("tmp", weatherList[i].now?.tmp)
            values.put("txt", weatherList[i].now?.cond_txt)
            values.put("dir", weatherList[i].now?.wind_dir)
            db.delete("weather", "city = ?", arrayOf<String>(weatherList[i].basic?.location!!))
            db.insert("weather", null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    fun deleteCity(context: Context, city: String) {
        val dbHelper = MydataBaseHelper(context, "Weather.db", null, 1)
        val db = dbHelper.writableDatabase
        db.delete("weather", "city = ?", arrayOf(city))
    }
}
