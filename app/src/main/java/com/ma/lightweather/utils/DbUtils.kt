package com.ma.lightweather.utils

import android.content.ContentValues
import android.content.Context
import com.ma.lightweather.app.Contants
import com.ma.lightweather.db.MydataBaseHelper
import com.ma.lightweather.model.Weather
import java.util.*

/**
 * Created by Ma on 2018/12/8.
 */

object DbUtils {

    fun queryDb(context: Context): List<Weather> {
        val weatherList = ArrayList<Weather>()
        val dbHelper = MydataBaseHelper(context, Contants.WEATHERDB, null, 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query(Contants.WEATHER, null, null, null, null, null, "id desc")
        if (cursor.moveToFirst()) {
            do {
                val weather = Weather()
                weather.basic.location = cursor.getString(cursor.getColumnIndex(Contants.CITY))
                weather.now.tmp = cursor.getString(cursor.getColumnIndex(Contants.TMP))
                weather.now.cond_txt = cursor.getString(cursor.getColumnIndex(Contants.TXT))
                weather.now.wind_dir = cursor.getString(cursor.getColumnIndex(Contants.DIR))
                weather.now.wind_sc = cursor.getString(cursor.getColumnIndex(Contants.SC))
                weather.update.loc=cursor.getString(cursor.getColumnIndex(Contants.DATE))
                if (weather.basic.location != null) {
                    weatherList.add(weather)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return weatherList
    }

    fun writeDb(context: Context, weatherList: List<Weather>) {
        val dbHelper = MydataBaseHelper(context, Contants.WEATHERDB, null, 1)
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        for (i in weatherList.indices) {
            val values = ContentValues()
            values.put(Contants.CITY, weatherList[i].basic.location)
            values.put(Contants.TMP, weatherList[i].now.tmp)
            values.put(Contants.TXT, weatherList[i].now.cond_txt)
            values.put(Contants.DIR, weatherList[i].now.wind_dir)
            values.put(Contants.SC, weatherList[i].now.wind_sc)
            values.put(Contants.DATE, weatherList[i].update.loc)
            db.delete(Contants.WEATHER, "city = ?", arrayOf(weatherList[i].basic.location))
            db.insert(Contants.WEATHER, null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    fun deleteDb(context: Context, city: String) {
        val dbHelper = MydataBaseHelper(context, Contants.WEATHERDB, null, 1)
        val db = dbHelper.writableDatabase
        db.delete(Contants.WEATHER, "city = ?", arrayOf(city))
    }
}
