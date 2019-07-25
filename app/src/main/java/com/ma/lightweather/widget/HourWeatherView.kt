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
import kotlin.math.ceil

/**
 * Created by Aeolus on 2018/8/24.
 */

class HourWeatherView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val pointPaint = Paint()
    private val outPointPaint = Paint()
    private val textPaint = Paint()
    private val tmpPaint = Paint()
    private val path = Path()

    private var tmpList: MutableList<Int> = ArrayList()
    private var popList: MutableList<Int> = ArrayList()
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

        tmpPaint.color = resources.getColor(R.color.temp)
        tmpPaint.isAntiAlias = true
        tmpPaint.strokeWidth = lineWidth.toFloat()
        tmpPaint.style = Paint.Style.STROKE

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
        xSpace = viewwidth / 16
        ySpace = (viewhigh - 4 * texthigh - 40) / 50
        if (tmpList.isNotEmpty()) {
            max = Collections.max(tmpList)
            min = Collections.min(tmpList)
            ySpace = (viewhigh - 8 * texthigh - 40) / (max - min)
        }
        path.reset()
        //实时温度折线
        for (i in tmpList.indices) {
            pointPaint.color = resources.getColor(R.color.temp)
            outPointPaint.color = resources.getColor(R.color.temp)
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
                    k2 = (getY(i + 2, tmpList) - getY(i, tmpList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, tmpList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, tmpList)
                    path.moveTo(getX(2 * i + 1), getY(i, tmpList))
                    path.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, tmpList))
                }
                i < tmpList.size - 2 -> {
                    k1 = (getY(i + 1, tmpList) - getY(i - 1, tmpList)) / getX(4)
                    k2 = (getY(i + 2, tmpList) - getY(i, tmpList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, tmpList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, tmpList)
                    path.moveTo(getX(2 * i + 1), getY(i, tmpList))
                    path.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, tmpList))
                }
                i == tmpList.size - 2 -> {
                    k1 = (getY(i + 1, tmpList) - getY(i - 1, tmpList)) / getX(4)
                    y1 = (x - getX(2 * i + 1)) * k1 + getY(i, tmpList)
                    y2 = (x - getX(2 * i + 3)) * k2 + getY(i + 1, tmpList)
                    path.moveTo(getX(2 * i + 1), getY(i, tmpList))
                    path.cubicTo(x, y1, x, y2, getX(2 * i + 3), getY(i + 1, tmpList))
                }
                //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),pointRadius,pointPaint);
                //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),outPointRadius,outPointPaint);
            }
            canvas.drawPath(path, tmpPaint)
            //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),pointRadius,pointPaint);
            //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),outPointRadius,outPointPaint);
            canvas.drawText(tmpList[i].toString() + "°", getX(2 * i + 1), getY(i, tmpList) - 20, textPaint)
        }
        //日期
        for (i in dateList.indices) {
            val data1 = dateList[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            canvas.drawText(data1[1], getX(2 * i + 1), texthigh.toFloat(), textPaint)
        }
        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getX(2 * i + 1), (viewhigh - Math.ceil((fm.bottom - fm.leading).toDouble()).toInt()).toFloat(), textPaint)
        }
        //降水概率
        for (i in popList.indices) {
            canvas.drawText(popList[i].toString() + "%", getX(2 * i + 1), (viewhigh - Math.ceil((fm.bottom - fm.leading).toDouble()).toInt() - texthigh).toFloat(), textPaint)
        }
        //分割线
        for (i in 1 until dateList.size) {
            canvas.drawLine(getX(2 * i), 50f, getX(2 * i), (viewhigh - texthigh).toFloat(), textPaint)
        }
    }

    fun loadViewData(hourlyList: List<Weather.HourlyWeather>?) {
        if (hourlyList != null) {
            tmpList.clear()
            popList.clear()
            dateList.clear()
            txtList.clear()
            dirList.clear()
            for (hourly in hourlyList){
                (tmpList as ArrayList).add(hourly.tmp!!.toInt())
                (popList as ArrayList).add(hourly.pop!!.toInt())
                hourly.time?.let { (dateList as ArrayList).add(it) }
                hourly.cond_txt?.let { (txtList as ArrayList).add(it) }
                hourly.wind_dir?.let { (dirList as ArrayList).add(it) }
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

