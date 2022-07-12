package com.ma.lightweather.utils

import android.content.Context
import androidx.annotation.ColorRes
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import androidx.annotation.ColorInt as ColorInt1

object WeatherUtils {

    fun getBackColor(context: Context): Int {
        return when (SPUtils.getParam(context, Contants.THEME, 0) as Int) {
            0 -> R.color.cyanColorPrimaryDark
            1 -> R.color.purpleColorPrimaryDark
            2 -> R.color.redColorPrimaryDark
            3 -> R.color.pinkColorPrimaryDark
            4 -> R.color.greenColorPrimaryDark
            5 -> R.color.blueColorPrimaryDark
            6 -> R.color.orangeColorPrimaryDark
            7 -> R.color.greyColorPrimaryDark
            else -> R.color.cyanColorPrimaryDark
        }
    }

    fun getTheme(context: Context): Int {
        return when (SPUtils.getParam(context, Contants.THEME, 0) as Int ) {
            0 -> R.style.CyanAppTheme
            1 -> R.style.PuroleAppTheme
            2 -> R.style.RedAppTheme
            3 -> R.style.PinkAppTheme
            4 -> R.style.GreenAppTheme
            5 -> R.style.BlueAppTheme
            6 -> R.style.OrangeAppTheme
            7 -> R.style.GreyAppTheme
            else -> R.style.CyanAppTheme
        }
    }


    fun getTextColor(context: Context): Int {
        return when (SPUtils.getParam(context, Contants.THEME, 0) as Int) {
            0 -> R.color.cyanColorPrimary
            1 -> R.color.purpleColorPrimary
            2 -> R.color.redColorPrimary
            3 -> R.color.pinkColorPrimary
            4 -> R.color.greenColorPrimary
            5 -> R.color.blueColorPrimary
            6 -> R.color.orangeColorPrimary
            7 -> R.color.greyColorPrimary
            else -> R.color.cyanColorPrimary
        }
    }


    fun getWeatherIcon(condTxt:String):Int {
        if (condTxt.contains("云")) {
            return R.drawable.ic_cloudy
        }
        if (condTxt.contains("阴")) {
            return R.drawable.ic_shade
        }
        if (condTxt.contains("雨")) {
            return R.drawable.ic_rain
        }
        if (condTxt.contains("雪")) {
            return R.drawable.ic_snow
        }
        if (condTxt.contains("雾")) {
            return R.drawable.ic_fog
        }
        if (condTxt.contains("霾")) {
            return R.drawable.ic_smog
        }
        if (condTxt.contains("风")) {
            return R.drawable.ic_wind
        }
        if (condTxt.contains("晴")||condTxt.contains("静")) {
            return R.drawable.ic_sunny
        }
        if (condTxt.contains("沙")||condTxt.contains("尘")) {
            return R.drawable.ic_sand
        }
        return R.drawable.ic_unknow
    }

    fun getColorWeatherIcon(condTxt:String):Int {
        if (condTxt.contains("云")) {
            return R.mipmap.ic_cloudy
        }
        if (condTxt.contains("阴")) {
            return R.mipmap.ic_shadow
        }
        if (condTxt.contains("雨")) {
            return R.mipmap.ic_rain
        }
        if (condTxt.contains("雪")) {
            return R.mipmap.ic_snow
        }
        if (condTxt.contains("风")) {
            return R.mipmap.ic_wind
        }
        if (condTxt.contains("晴")||condTxt.contains("静")) {
            return R.mipmap.ic_sunny
        }
        if (condTxt.contains("雾")
                ||condTxt.contains("霾")
                ||condTxt.contains("沙")
                ||condTxt.contains("尘")) {
            return R.mipmap.ic_fog
        }
        return R.mipmap.ic_unknow
    }

    fun getColorWeatherImg(condTxt:String):Int {
        if (condTxt.contains("云")) {
            return R.mipmap.ic_cloudy_back
        }
        if (condTxt.contains("阴")) {
            return R.mipmap.ic_shadow_back
        }
        if (condTxt.contains("雨")) {
            return R.mipmap.ic_rain_back
        }
        if (condTxt.contains("雪")) {
            return R.mipmap.ic_snow_back
        }
        if (condTxt.contains("风")) {
            return R.mipmap.ic_wind_back
        }
        if (condTxt.contains("晴")||condTxt.contains("静")) {
            return R.mipmap.ic_sunny_back
        }
        if (condTxt.contains("雾")
                ||condTxt.contains("霾")
                ||condTxt.contains("沙")
                ||condTxt.contains("尘")) {
            return R.mipmap.ic_fog_back
        }
        return R.mipmap.ic_unknow_back
    }

    fun getColorWeatherBack(condTxt:String): Int {
        if (condTxt.contains("云")) {
            return R.color.weather_back_cloud
        }
        if (condTxt.contains("阴")) {
            return R.color.weather_back_shadow
        }
        if (condTxt.contains("雨")) {
            return R.color.weather_back_rain
        }
        if (condTxt.contains("雪")) {
            return R.color.weather_back_snow
        }
        if (condTxt.contains("风")) {
            return R.color.weather_back_wind
        }
        if (condTxt.contains("晴")||condTxt.contains("静")) {
            return R.color.weather_back_sunny
        }
        if (condTxt.contains("雾")
                ||condTxt.contains("霾")
                ||condTxt.contains("沙")
                ||condTxt.contains("尘")) {
            return R.color.weather_back_fog
        }
        return R.color.weather_back_unknow
    }

    fun getColorWeatherTheme(condTxt:String):Int {
        if (condTxt.contains("云")) {
            return R.color.weather_theme_cloud
        }
        if (condTxt.contains("阴")) {
            return R.color.weather_theme_shadow
        }
        if (condTxt.contains("雨")) {
            return R.color.weather_theme_rain
        }
        if (condTxt.contains("雪")) {
            return R.color.weather_theme_snow
        }
        if (condTxt.contains("风")) {
            return R.color.weather_theme_wind
        }
        if (condTxt.contains("晴")||condTxt.contains("静")) {
            return R.color.weather_theme_sunny
        }
        if (condTxt.contains("雾")
                ||condTxt.contains("霾")
                ||condTxt.contains("沙")
                ||condTxt.contains("尘")) {
            return R.color.weather_theme_fog
        }
        return R.color.weather_theme_unknow
    }
}