package com.ma.lightweather.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ma.lightweather.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Aeolus on 2018/6/5.
 */

object CommonUtils {
    var mSnackbar: Snackbar? = null

    fun showShortSnackBar(view: View?, content: String?) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view!!,content?:"", Snackbar.LENGTH_SHORT)
        } else {
            mSnackbar?.setText(content?:"")
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

    fun getStatusBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val ratio = 3
        return if (bitmap.height >= 1000 || bitmap.width >= 1000) {
            Bitmap.createBitmap(bitmap.width / ratio, bitmap.height / ratio, Bitmap.Config.ARGB_8888)
        } else bitmap
    }

    fun changeTimeFormat(date: String): List<String> {
        var dates=listOf<String>()
        var years=listOf<String>()
        var times=listOf<String>()
        if(date==null){
            return dates
        }
        if(date.contains(" ")) {
            dates = date.split(" ")
            if(dates.size>=2&&dates[0].contains("-")){
                years=dates[0].split("-")
            }
            if(dates.size>=2&&dates[1].contains(":")){
                times=dates[1].split(":")
            }
        }else if(date.contains("-")) {
            years=date.split("-")
        }else if(date.contains(":")) {
            times=date.split(":")
        }
        if(years.size<3){
            years = listOf("","","")
        }
        if(times.size<2){
            times = listOf("","")
        }
        return listOf(years[0],years[1],years[2],times[0],times[1])
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


    fun getVersion(context: Context): String? {
        return try {
            val manager: PackageManager = context.packageManager
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            val version = info.versionName
            "V $version"
        } catch (e: Exception) {
            e.printStackTrace()
            "找不到版本号"
        }
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

    fun goToMarket(context: Context) {
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            if (isContains(context, "com.tencent.android.qqdownloader")) {
                goToMarket.setPackage("com.tencent.android.qqdownloader")
            }else if (isContains(context, "com.coolapk.market")) {
                goToMarket.setPackage("com.coolapk.market")
            }
            context.startActivity(goToMarket)
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.ma.lightweather")
            context.startActivity(intent)
        }

    }

}
