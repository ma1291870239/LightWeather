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
import com.ma.lightweather.R
import com.ma.lightweather.model.Weather
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Ma-PC on 2016/12/14.
 */
class WeatherView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val pointPaint = Paint()
    private val outPointPaint = Paint()
    private val textPaint = Paint()
    private val dividerPaint = Paint()
    private val maxPaint = Paint()
    private val minPaint = Paint()
    private val maxPath = Path()
    private val minPath = Path()
    private var maxDst=Path()
    private var minDst=Path()
    private var maxPathMeasureSpec=PathMeasure()
    private var minPathMeasureSpec= PathMeasure()

    private var maxList: MutableList<Int> = ArrayList()
    private var minList: MutableList<Int> = ArrayList()
    private var dateList: MutableList<String> = ArrayList()
    private var txtList: MutableList<String> = ArrayList()
    private var dirList: MutableList<String> = ArrayList()

    private var viewHigh = 0 //控件高度
    private var viewWidth = 0 //控件宽度
    private var xUnit= 0f //x轴单位长度
    private var yUnit= 0f //y轴单位长度
    private var xPart = 7f //x轴等分
    private var textHigh = 0f //文字高度
    private var textSpace = 0f //文字间隔
    private var curvature=0.13f // 0<curvature<0.5
    private var mLength= 0f
    private var mAnimatorValue= 0f
    private var max: Int = 0
    private var min: Int = 0

    init {
        init()
    }

    private fun init() {

        pointPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        pointPaint.isAntiAlias = true
        pointPaint.strokeWidth = pointWidth.toFloat()
        pointPaint.style = Paint.Style.FILL

        outPointPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        outPointPaint.isAntiAlias = true
        outPointPaint.strokeWidth = outPointWidth
        outPointPaint.style = Paint.Style.STROKE

        maxPaint.color = ContextCompat.getColor(context,R.color.temp_high)
        maxPaint.isAntiAlias = true
        maxPaint.strokeWidth = lineWidth
        maxPaint.style = Paint.Style.STROKE

        minPaint.color = ContextCompat.getColor(context,R.color.temp_low)
        minPaint.isAntiAlias = true
        minPaint.strokeWidth = lineWidth
        minPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context,R.color.text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        dividerPaint.color = ContextCompat.getColor(context, R.color.hint_black_text)
        dividerPaint.isAntiAlias = true
        dividerPaint.strokeMiter= dividerWidth
        dividerPaint.textAlign = Paint.Align.CENTER

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { valueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Float
            postInvalidate()
        }
        valueAnimator.duration = 3000
        valueAnimator.start()

    }

    override fun onDraw(canvas: Canvas) {
        viewHigh = measuredHeight
        viewWidth = measuredWidth
        textPaint.textSize = viewWidth / (4*xPart)//按每格4个字计算文字大小
        val fm = textPaint.fontMetrics
        textHigh = fm.bottom - fm.top
        textSpace=0.5f*textHigh
        xUnit = viewWidth / (2*xPart)
        yUnit = getY(4,5) / 50
        if (maxList.isNotEmpty() && minList.isNotEmpty()) {
            max = Collections.max(maxList)
            min = Collections.min(minList)
            yUnit = getY(4,5) / (max - min)
        }

        //最高温度折线
        maxPath.reset()
        drawTmpCurve(canvas,maxList,true)
        //最低温度折线
        minPath.reset()
        drawTmpCurve(canvas,minList,false)
        //日期
        for (i in dateList.indices) {
            val data1 = dateList[i].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            canvas.drawText(data1[1] + "/" + data1[2], getBezierX(i), textHigh, textPaint)
        }
        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getBezierX(i ), getY(0,1), textPaint)
        }
        //分割线
        for (i in 1 until dateList.size) {
            canvas.drawLine(getBezierX(i)-xUnit, textHigh, getBezierX(i)-xUnit, getY(1,1), dividerPaint)
        }
    }

    private fun drawTmpCurve(canvas: Canvas,tmpList: MutableList<Int>,isMax: Boolean){
        for (i in tmpList.indices) {

            var x1 = 0f
            var y1 = 0f

            var x2 = 0f
            var y2 = 0f

            when{
                i == 0 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i))*curvature
                    y1=getBezierY(i,tmpList)+(getBezierY(i + 1,tmpList)-getBezierY(i,tmpList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 2)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tmpList)-(getBezierY(i + 2,tmpList)-getBezierY(i,tmpList))*curvature

                    if(isMax){
                        maxPath.moveTo(getBezierX(i), getBezierY(i, tmpList))
                        maxPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }else{
                        minPath.moveTo(getBezierX(i), getBezierY(i, tmpList))
                        minPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }

                }
                i< tmpList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tmpList)+(getBezierY(i + 1,tmpList)-getBezierY(i-1,tmpList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 2)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tmpList)-(getBezierY(i + 2,tmpList)-getBezierY(i,tmpList))*curvature

                    if(isMax){
                        maxPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }else{
                        minPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }

                }
                i == tmpList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tmpList)+(getBezierY(i + 1,tmpList)-getBezierY(i-1,tmpList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 1)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tmpList)-(getBezierY(i + 1,tmpList)-getBezierY(i,tmpList))*curvature

                    if(isMax){
                        maxPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }else{
                        minPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                    }

                }
            }
            if(isMax) {
                canvas.drawText(tmpList[i].toString() + "°", getBezierX(i), getBezierY(i, tmpList) - textSpace, textPaint)
            }else{
                canvas.drawText(tmpList[i].toString() + "°", getBezierX(i), getBezierY(i, tmpList) +textHigh, textPaint)
            }
        }
        if (isMax){
            maxPathMeasureSpec.setPath(maxPath, false)
            mLength = maxPathMeasureSpec.length
            maxDst.reset()
            maxDst.lineTo(0f,0f)
            val stop = mLength * mAnimatorValue
            maxPathMeasureSpec.getSegment(0f,stop,maxDst,true)
            canvas.drawPath(maxDst, maxPaint)
        }else{
            minPathMeasureSpec.setPath(minPath, false)
            mLength = minPathMeasureSpec.length
            minDst.reset()
            minDst.lineTo(0f,0f)
            val stop = mLength * mAnimatorValue
            minPathMeasureSpec.getSegment(0f,stop,minDst,true)
            canvas.drawPath(minDst, minPaint)
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
                (maxList as ArrayList).add(daily.tmp_max.toInt())
                (minList as ArrayList).add(daily.tmp_min.toInt())
                daily.date.let { (dateList as ArrayList).add(it) }
                daily.cond_txt_d.let { (txtList as ArrayList).add(it) }
                daily.wind_dir.let { (dirList as ArrayList).add(it) }
            }
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.addUpdateListener { valueAnimator ->
                mAnimatorValue = valueAnimator.animatedValue as Float
                postInvalidate()
            }
            valueAnimator.duration = 3000
            valueAnimator.start()
        }

    }

    private fun getBezierX(i: Int): Float {
        return (2*i+1) * xUnit
    }

    private fun getBezierY(i: Int, list: List<Int>): Float {
        return (max - list[i]) * yUnit + 2 * textHigh+2*textSpace
    }

    private fun getY(i1: Int,i2:Int): Float {
        return viewHigh - (i1 * textHigh+i2*textSpace)
    }

    companion object {

        private const val dividerWidth = 1f
        private const val lineWidth = 4f
        private const val pointWidth = 3f
        private const val outPointWidth = 3f
        private const val pointRadius = 5f
        private const val outPointRadius = 10f
    }

}
