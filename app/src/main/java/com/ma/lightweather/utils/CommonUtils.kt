package com.ma.lightweather.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aeolus on 2018/6/5.
 */

object CommonUtils {
    var mSnackbar: Snackbar? = null

    fun showShortSnackBar(view: View?, content: String) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view!!,content, Snackbar.LENGTH_SHORT)
        } else {
            mSnackbar?.setText(content)
            mSnackbar?.duration =BaseTransientBottomBar.LENGTH_SHORT
        }
        mSnackbar!!.show()
    }


    /**
     * 绘制文字到右下角
     * @param context
     * @param bitmap
     * @return
     */
    fun drawTextToRightBottom(context: Context, bitmap: Bitmap, phoneText: String, loctionText: String, weatherText: String,view: View?): Bitmap {
        var bitmap = bitmap

        val w = bitmap.width
        val h = bitmap.height
        var textSize = (h / 8).toFloat()
        var offset = h / 50
        if (w>h){
            textSize=(w / 8).toFloat()
            offset = w / 50
        }
        var phoneSpace = 0
        var loctionSpace = 0
        val paddingLeft = w - offset
        val paddingTop = h - offset

        //间隔大小
        if (!phoneText.isEmpty()) {
            //绘制机型
            val phonePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            phonePaint.color = context.resources.getColor(R.color.white)
            phonePaint.textSize = textSize / 4
            val phoneBounds = Rect()
            phonePaint.getTextBounds(phoneText, 0, phoneText.length, phoneBounds)
            phoneSpace = phoneBounds.height() + offset
            bitmap = drawTextToBitmap(context, bitmap, phoneText, phonePaint, phoneBounds,
                    paddingLeft - phoneBounds.width(),
                    paddingTop,view)
        }

        if (!loctionText.isEmpty()) {
            //绘制地点
            val loctionPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            loctionPaint.color = context.resources.getColor(R.color.white)
            loctionPaint.textSize = textSize / 4
            val loctionBounds = Rect()
            loctionPaint.getTextBounds(loctionText, 0, loctionText.length, loctionBounds)
            loctionSpace = loctionBounds.height() + offset
            bitmap = drawTextToBitmap(context, bitmap, loctionText, loctionPaint, loctionBounds,
                    paddingLeft - loctionBounds.width(),
                    paddingTop - phoneSpace,view)
        }

        if (!weatherText.isEmpty()) {
            //绘制天气
            val weatherPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            weatherPaint.color = context.resources.getColor(R.color.white)
            weatherPaint.textSize = textSize / 3
            val weatherBounds = Rect()
            weatherPaint.getTextBounds(weatherText, 0, weatherText.length, weatherBounds)

            bitmap = drawTextToBitmap(context, bitmap, weatherText, weatherPaint, weatherBounds,
                    paddingLeft - weatherBounds.width(),
                    paddingTop - phoneSpace - loctionSpace,view)
        }

        return bitmap
    }


    //图片上绘制文字
    private fun drawTextToBitmap(context: Context, bitmap: Bitmap, text: String,
                                 paint: Paint, bounds: Rect, paddingLeft: Int, paddingTop: Int,view: View?): Bitmap{
        try {
            paint.isDither = true // 获取跟清晰的图像采样
            paint.isFilterBitmap = true// 过滤一些
            val bmp = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            canvas.drawText(text, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
            bitmap.recycle()
            return bmp
        } catch (error: OutOfMemoryError) {
            showShortSnackBar(view, "图片过大")
        }

        return bitmap
    }


    fun isContains(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        val packageInfoList = packageManager.getInstalledPackages(0)
        val nameList = ArrayList<String>()
        if (packageInfoList != null) {
            for (i in packageInfoList.indices) {
                val name = packageInfoList[i].packageName
                if (name == packageName) {
                    return true
                }
            }
        }
        return false
    }

    fun getBackColor(context: Context): Int {
        val themeTag = SharedPrefencesUtils.getParam(context, Contants.THEME, 0) as Int
        return when (themeTag) {
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


    fun getTextColor(context: Context): Int {
        val themeTag = SharedPrefencesUtils.getParam(context, Contants.THEME, 0) as Int
        return when (themeTag) {
            0 -> R.color.cyanColorPrimary
            1 -> R.color.purpleColorPrimary
            2 -> R.color.redColorPrimary
            3 -> R.color.pinkColorPrimary
            4 -> R.color.greenColorPrimary
            5 -> R.color.blueColorPrimary
            6 -> R.color.orangeColorPrimary
            7 -> R.color.greyColorPrimary
            else -> R.color.primary_black_text
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
        return R.drawable.ic_unknow
    }

    fun getColorWeatherBack(condTxt:String):Int {
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
        return R.drawable.ic_unknow
    }

    fun getColorWeatherBackColor(condTxt:String):Int {
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
        return R.drawable.ic_unknow
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
        return R.drawable.ic_unknow
    }

    /**
     * dp转换成px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转换成dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转换成px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转换成sp
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val ratio = 3
        return if (bitmap.height >= 1000 || bitmap.width >= 1000) {
            Bitmap.createBitmap(bitmap.width / ratio, bitmap.height / ratio, Bitmap.Config.ARGB_8888)
        } else bitmap
    }

    fun changeTimeFormat(date: String): String {
        if(date==null){
            return ""
        }
        var dates= listOf<String>()
        var years= listOf<String>()
        if(date.contains(" ")) {
            dates = date.split(" ")
            years = listOf()
            if (dates.size >= 2) {
                years = dates[0].split("-")
            }
        }else if(date.contains("-")) {
            years=date.split("-")
        }
        if(years.size<3){
            years = listOf("","","")
        }
        if(dates.size<2){
            dates = listOf("","")
        }
        return ""+years[1].toInt()+"月"+years[2].toInt()+"日 "+dates[1]
    }

    fun getTimeFormat(date: String): Array<Int> {
        if(date==null){
            return arrayOf(0,0)
        }
        var times= listOf<String>()
        if(date.contains(" ")) {
            var dates = date.split(" ")
            if (dates.size >= 2) {
                times = dates[1].split(":")
            }
        }else if(date.contains(":")) {
            times=date.split(":")
        }
        if(times.size<2){
            return arrayOf(0,0)
        }
        return arrayOf(times[0].toInt(),times[1].toInt())
    }

    fun getTimeValue(date: String,startTime: String,endTime: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val start = df.parse(date+" "+startTime).time
        val end = df.parse(date+" "+endTime).time
        return (end-start)/(1000*60)
    }

    fun minutesToHours(minutes:Long): String {
        val hours =(minutes/60).toInt()
        val otherMinutes = minutes-(hours*60)
        return ""+hours+"小时"+otherMinutes+"分"
    }

    fun change24To12(time24: Int): String {
        return if (time24 <= 12) {
            "上午" + time24
        } else {
            "下午" + (time24 - 12)
        }
    }

}
