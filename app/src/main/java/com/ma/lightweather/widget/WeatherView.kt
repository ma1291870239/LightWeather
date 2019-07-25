package com.ma.lightweather.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ma.lightweather.R
import com.ma.lightweather.model.Weather
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

/**
 * Created by Ma-PC on 2016/12/14.
 */
class WeatherView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val pointPaint = Paint()
    private val outPointPaint = Paint()
    private val textPaint = Paint()
    private val maxPaint = Paint()
    private val minPaint = Paint()
    private val maxPath = Path()
    private val minPath = Path()

    private var maxList: MutableList<Int> = ArrayList()
    private var minList: MutableList<Int> = ArrayList()
    private var dateList: MutableList<String> = ArrayList()
    private var txtList: MutableList<String> = ArrayList()
    private var dirList: MutableList<String> = ArrayList()

    private var xSpace: Int = 0
    private var offsetHigh: Int = 0
    private var ySpace: Int = 0
    private var max: Int = 0
    private var min: Int = 0

    init {
        init()
    }

    private fun init() {

        pointPaint.isAntiAlias = true
        pointPaint.strokeWidth = pointWidth.toFloat()
        pointPaint.style = Paint.Style.FILL

        outPointPaint.isAntiAlias = true
        outPointPaint.strokeWidth = outPointWidth.toFloat()
        outPointPaint.style = Paint.Style.STROKE

        maxPaint.color = resources.getColor(R.color.temp_high)
        maxPaint.isAntiAlias = true
        maxPaint.strokeWidth = lineWidth.toFloat()
        maxPaint.style = Paint.Style.STROKE

        minPaint.color = resources.getColor(R.color.temp_low)
        minPaint.isAntiAlias = true
        minPaint.strokeWidth = lineWidth.toFloat()
        minPaint.style = Paint.Style.STROKE

        textPaint.color = resources.getColor(R.color.text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

    }

    override fun onDraw(canvas: Canvas) {
        val viewhigh = measuredHeight
        val viewwidth = measuredWidth
        textPaint.textSize = (viewwidth / 30).toFloat()
        val fm = textPaint.fontMetrics
        val texthigh = ceil((fm.bottom - fm.top).toDouble()).toInt()
        offsetHigh = 4 * texthigh + 10
        xSpace = viewwidth / 14
        ySpace = (viewhigh - 6 * texthigh - 40) / 50
        if (maxList.isNotEmpty() && minList.isNotEmpty()) {
            max = Collections.max(maxList)
            min = Collections.min(minList)
            ySpace = (viewhigh - 8 * texthigh - 40) / (max - min)
        }
        maxPath.reset()
        minPath.reset()
        //最高温度折线
        for (i in maxList.indices) {
            pointPaint.color = resources.getColor(R.color.temp_high)
            outPointPaint.color = resources.getColor(R.color.temp_high)
            //            if(i<maxList.size()-1){
            //                canvas.drawLine(getX(2*i+1),getY(i,maxList),
            //                        getX(2*i+3),getY(i+1,maxList),maxPaint);
            //            }
            val x = getX(2 * i + 2)
            var k1 = 0f
            var k2 = 0f
            var y1 = 0f
            var y2 = 0f
            when {
                i == 0 -> {
                    k2 = (getY(i + 2, maxList) - getY(i, maxList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, maxList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, maxList)
                    maxPath.moveTo(getX(2 * i + 1), getY(i, maxList))
                    maxPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, maxList))
                }
                i < maxList.size - 2 -> {
                    k1 = (getY(i + 1, maxList) - getY(i - 1, maxList)) / getX(4)
                    k2 = (getY(i + 2, maxList) - getY(i, maxList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, maxList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, maxList)
                    maxPath.moveTo(getX(2 * i + 1), getY(i, maxList))
                    maxPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, maxList))
                }
                i == maxList.size - 2 -> {
                    k1 = (getY(i + 1, maxList) - getY(i - 1, maxList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, maxList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, maxList)
                    maxPath.moveTo(getX(2 * i + 1), getY(i, maxList))
                    maxPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, maxList))
                }
                //            canvas.drawCircle(getX(2*i+1),getY(i,maxList),pointRadius,pointPaint);
                //            canvas.drawCircle(getX(2*i+1),getY(i,maxList),outPointRadius,outPointPaint);
            }
            canvas.drawPath(maxPath, maxPaint)
            //            canvas.drawCircle(getX(2*i+1),getY(i,maxList),pointRadius,pointPaint);
            //            canvas.drawCircle(getX(2*i+1),getY(i,maxList),outPointRadius,outPointPaint);
            canvas.drawText(maxList[i].toString() + "°", getX(2 * i + 1), getY(i, maxList) - 20, textPaint)
        }
        //最低温度折线
        for (i in minList.indices) {
            pointPaint.color = resources.getColor(R.color.temp_low)
            outPointPaint.color = resources.getColor(R.color.temp_low)
            //            if(i<minList.size()-1){
            //                canvas.drawLine((2*i+1)*xSpace,(max-minList.get(i))*ySpace+offsetHigh,
            //                        (2*i+3)*xSpace,(max-minList.get(i+1))*ySpace+offsetHigh,minPaint);
            //            }
            val x = getX(2 * i + 2)
            var k1 = 0f
            var k2 = 0f
            var y1 = 0f
            var y2 = 0f
            when {
                i == 0 -> {
                    k2 = (getY(i + 2, minList) - getY(i, minList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, minList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, minList)
                    minPath.moveTo(getX(2 * i + 1), getY(i, minList))
                    minPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, minList))
                }
                i < minList.size - 2 -> {
                    k1 = (getY(i + 1, minList) - getY(i - 1, minList)) / getX(4)
                    k2 = (getY(i + 2, minList) - getY(i, minList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, minList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, minList)
                    minPath.moveTo(getX(2 * i + 1), getY(i, minList))
                    minPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, minList))
                }
                i == minList.size - 2 -> {
                    k1 = (getY(i + 1, minList) - getY(i - 1, minList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, minList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, minList)
                    minPath.moveTo(getX(2 * i + 1), getY(i, minList))
                    minPath.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, minList))
                }
                //canvas.drawCircle(getX(2*i+1),getY(i,minList),pointRadius,pointPaint);
                //canvas.drawCircle(getX(2*i+1),getY(i,minList),outPointRadius,outPointPaint);
            }
            canvas.drawPath(minPath, minPaint)
            //canvas.drawCircle(getX(2*i+1),getY(i,minList),pointRadius,pointPaint);
            //canvas.drawCircle(getX(2*i+1),getY(i,minList),outPointRadius,outPointPaint);
            canvas.drawText(minList[i].toString() + "°", getX(2 * i + 1), getY(i, minList) + texthigh.toFloat() + 5f, textPaint)
        }
        //日期
        for (i in dateList.indices) {
            val data1 = dateList[i].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            canvas.drawText(data1[1] + "/" + data1[2], getX(2 * i + 1), texthigh.toFloat(), textPaint)
        }
        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getX(2 * i + 1), (viewhigh - Math.ceil((fm.bottom - fm.leading).toDouble()).toInt()).toFloat(), textPaint)
        }
        //分割线
        for (i in 1 until dateList.size) {
            canvas.drawLine(getX(2 * i), 50f, getX(2 * i), (viewhigh - texthigh).toFloat(), textPaint)
        }
    }

    fun loadViewData(dailyList: List<Weather.DailyWeather>?) {
        if (dailyList != null) {
            maxList.clear()
            minList.clear()
            dateList.clear()
            txtList.clear()
            dirList.clear()
            for (daily in dailyList){
                (maxList as ArrayList).add(daily.tmp_max!!.toInt())
                (minList as ArrayList).add(daily.tmp_min!!.toInt())
                daily.date?.let { (dateList as ArrayList).add(it) }
                daily.cond_txt_d?.let { (txtList as ArrayList).add(it) }
                daily.wind_dir?.let { (dirList as ArrayList).add(it) }
            }
        }
        postInvalidate()
    }

    private fun getX(i: Int): Float {
        return (i * xSpace).toFloat()
    }

    private fun getY(i: Int, list: List<Int>): Float {
        return ((max - list[i]) * ySpace + offsetHigh).toFloat()
    }

    companion object {

        private val lineWidth = 5
        private val pointWidth = 3
        private val outPointWidth = 3
        private val pointRadius = 5
        private val outPointRadius = 10
    }

}
