package com.ma.lightweather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
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
    private var mDst=Path()
    private var temPathMeasureSpec:PathMeasure?=null





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
    private var mLength= 0.toFloat()
    private var mAnimatorValue= 0.toFloat()

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

        tmpPaint.color = ContextCompat.getColor(context, com.ma.lightweather.R.color.temp)
        tmpPaint.isAntiAlias = true
        tmpPaint.strokeWidth = lineWidth.toFloat()
        tmpPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, com.ma.lightweather.R.color.text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { valueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Float
            invalidate()
        }
        valueAnimator.duration = 10000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.start()

    }

    override fun onDraw(canvas: Canvas) {
        val viewHigh = measuredHeight
        val viewWidth = measuredWidth
        textPaint.textSize = (viewWidth / 30).toFloat()
        val fm = textPaint.fontMetrics
        val textHigh = ceil((fm.bottom - fm.top).toDouble()).toInt()
        offsetHigh = 3 * textHigh + 10
        xSpace = viewWidth / 16
        ySpace = (viewHigh - 8 * textHigh - 40) / 50
        if (tmpList.isNotEmpty()) {
            max = Collections.max(tmpList)
            min = Collections.min(tmpList)
            ySpace = (viewHigh - 8 * textHigh - 40) / (max - min)
        }

        //实时温度折线
        drawTmpLine(canvas)

        //日期
        for (i in dateList.indices) {
            val data1 = dateList[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            canvas.drawText(data1[1], getX(2 * i + 1), textHigh.toFloat(), textPaint)
        }

        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getX(2 * i + 1), (viewHigh - ceil((fm.bottom - fm.leading).toDouble()).toInt()-3*textHigh).toFloat(), textPaint)
        }

        //天气分割线
        for (i in 1 until dateList.size) {
            canvas.drawLine(getX(2 * i), 50f, getX(2 * i), (viewHigh - 4*textHigh).toFloat(), textPaint)
        }

        //降雨分割线
        canvas.drawLine(getX(2 * 0), (viewHigh -2.5*textHigh).toFloat() , getX(2 * 8), (viewHigh -2.5*textHigh).toFloat(), textPaint)
        canvas.drawText("降水概率", getX(2 *4), (viewHigh - ceil((fm.bottom - fm.leading).toDouble()).toInt() - textHigh).toFloat(), textPaint)

        //降水概率
        for (i in popList.indices) {
            canvas.drawText(popList[i].toString() + "%", getX(2 * i + 1), viewHigh.toFloat(), textPaint)
        }
    }

    private fun drawTmpLine(canvas: Canvas){
        path.reset()
        for (i in tmpList.indices) {
            pointPaint.color = ContextCompat.getColor(context, com.ma.lightweather.R.color.temp)
            outPointPaint.color = ContextCompat.getColor(context, com.ma.lightweather.R.color.temp)
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


            canvas.drawText(tmpList[i].toString() + "°", getX(2 * i + 1), getY(i, tmpList) - 20, textPaint)

            //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),pointRadius,pointPaint);
            //            canvas.drawCircle(getX(2*i+1),getY(i,tmpList),outPointRadius,outPointPaint);

        }
        temPathMeasureSpec=PathMeasure()
        temPathMeasureSpec?.setPath(path, true)
        mLength = temPathMeasureSpec?.length!!
        mDst.reset()
        mDst.lineTo(0f,0f)
        val stop = mLength * mAnimatorValue
        temPathMeasureSpec?.getSegment(0f,stop,mDst,true)
        canvas.drawPath(mDst, tmpPaint)

    }

    fun loadViewData(hourlyList: List<Weather.HourlyWeather>?) {
        if (hourlyList != null) {
            tmpList.clear()
            popList.clear()
            dateList.clear()
            txtList.clear()
            dirList.clear()
            for (hourly in hourlyList){
                (tmpList as ArrayList).add(hourly.tmp.toInt())
                (popList as ArrayList).add(hourly.pop.toInt())
                hourly.time.let { (dateList as ArrayList).add(it) }
                hourly.cond_txt.let { (txtList as ArrayList).add(it) }
                hourly.wind_dir.let { (dirList as ArrayList).add(it) }
            }
        }
    }

    private fun getX(i: Int): Float {
        return (i * xSpace).toFloat()
    }

    private fun getY(i: Int, list: List<Int>): Float {
        return ((max - list[i]) * ySpace + offsetHigh).toFloat()
    }

    companion object {

        private const val lineWidth = 5
        private const val pointWidth = 3
        private const val outPointWidth = 3
        private const val pointRadius = 5
        private const val outPointRadius = 10
    }

}

