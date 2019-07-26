package com.ma.lightweather.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import java.util.*

/**
 * Created by Aeolus on 2018/6/5.
 */

object CommonUtils {
    var mSnackbar: Snackbar? = null

    fun showShortSnackBar(view: View?, content: String) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view!!,content,Snackbar.LENGTH_SHORT)
        } else {
            mSnackbar!!.setText(content)
            mSnackbar!!.duration = Toast.LENGTH_SHORT
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
        val offset = h / 50
        var phoneSpace = 0
        var loctionSpace = 0
        val paddingLeft = w - offset
        val paddingTop = h - offset
        val textSize = (h / 10).toFloat()

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
        if (themeTag == 0) {
            return R.color.cyanColorPrimaryDark
        } else if (themeTag == 1) {
            return R.color.purpleColorPrimaryDark
        } else if (themeTag == 2) {
            return R.color.redColorPrimaryDark
        } else if (themeTag == 3) {
            return R.color.pinkColorPrimaryDark
        } else if (themeTag == 4) {
            return R.color.greenColorPrimaryDark
        } else if (themeTag == 5) {
            return R.color.blueColorPrimaryDark
        } else if (themeTag == 6) {
            return R.color.orangeColorPrimaryDark
        } else if (themeTag == 7) {
            return R.color.greyColorPrimaryDark
        }
        return R.color.text
    }


    fun getTextColor(context: Context): Int {
        val themeTag = SharedPrefencesUtils.getParam(context, Contants.THEME, 0) as Int
        if (themeTag == 0) {
            return R.color.cyanColorAccent
        } else if (themeTag == 1) {
            return R.color.purpleColorAccent
        } else if (themeTag == 2) {
            return R.color.redColorAccent
        } else if (themeTag == 3) {
            return R.color.pinkColorAccent
        } else if (themeTag == 4) {
            return R.color.greenColorAccent
        } else if (themeTag == 5) {
            return R.color.blueColorAccent
        } else if (themeTag == 6) {
            return R.color.orangeColorAccent
        } else if (themeTag == 7) {
            return R.color.greyColorAccent
        }
        return R.color.text
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

}
