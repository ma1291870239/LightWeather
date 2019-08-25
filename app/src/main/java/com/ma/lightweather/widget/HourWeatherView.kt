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


/**
 * Created by Aeolus on 2018/8/24.
 */

class HourWeatherView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val pointPaint = Paint()
    private val outPointPaint = Paint()
    private val textPaint = Paint()
    private val dividerPaint = Paint()
    private val tmpPaint = Paint()
    private val path = Path()
    private var mDst=Path()
    private var temPathMeasureSpec=PathMeasure()





    private var tmpList: MutableList<Int> = ArrayList()
    private var popList: MutableList<Int> = ArrayList()
    private var dateList: MutableList<String> = ArrayList()
    private var txtList: MutableList<String> = ArrayList()
    private var dirList: MutableList<String> = ArrayList()

    private var viewHigh = 0 //控件高度
    private var viewWidth = 0 //控件宽度
    private var xUnit = 0f //x轴单位长度
    private var yUnit = 0f //y轴单位长度
    private var xPart = 8f //x轴等分
    private var textHigh = 0f //文字高度
    private var textSpace = 0f //文字间隔
    private var max= 0
    private var min = 0
    private var curvature=0.13f // 0<curvature<0.5
    private var mLength= 0f
    private var mAnimatorValue= 0f

    init {
        init()
    }

    private fun init() {

        pointPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        pointPaint.isAntiAlias = true
        pointPaint.strokeWidth = pointWidth
        pointPaint.style = Paint.Style.FILL


        outPointPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        outPointPaint.isAntiAlias = true
        outPointPaint.strokeWidth = outPointWidth
        outPointPaint.style = Paint.Style.STROKE

        tmpPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        tmpPaint.isAntiAlias = true
        tmpPaint.strokeWidth = lineWidth
        tmpPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        dividerPaint.color = ContextCompat.getColor(context, R.color.hint_black_text)
        dividerPaint.isAntiAlias = true
        dividerPaint.strokeMiter= dividerWidth
        dividerPaint.textAlign = Paint.Align.CENTER

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { valueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Float
            invalidate()
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
        yUnit = getY(5,7) / 50
        if (tmpList.isNotEmpty()) {
            max = Collections.max(tmpList)
            min = Collections.min(tmpList)
            yUnit = getY(5,7) / (max - min)
        }

        //实时温度曲线
        drawTmpCurve(canvas)

        //日期
        for (i in dateList.indices) {
            val data1 = dateList[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            canvas.drawText(data1[1],  getBezierX(i), textHigh, textPaint)
        }

        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getBezierX(i), getY(2,4), textPaint)
        }

        //天气分割线
        for (i in 1 until dateList.size) {
            canvas.drawLine(getBezierX(i)-xUnit, textHigh, getBezierX(i)-xUnit, getY(3,4), dividerPaint)
        }

        //降雨分割线
        canvas.drawLine(0f,getY(2,3) , viewWidth.toFloat(), getY(2,3), dividerPaint)
        canvas.drawText("降水概率", viewWidth/2f, getY(1,2), textPaint)

        //降水概率
        for (i in popList.indices) {
            canvas.drawText(popList[i].toString() + "%", getBezierX(i ),getY(0,1), textPaint)
        }
    }

    private fun drawTmpCurve(canvas: Canvas){
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

                    path.moveTo(getBezierX(i), getBezierY(i, tmpList))
                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))
                }
                i< tmpList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tmpList)+(getBezierY(i + 1,tmpList)-getBezierY(i-1,tmpList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 2)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tmpList)-(getBezierY(i + 2,tmpList)-getBezierY(i,tmpList))*curvature

                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))

                }
                i == tmpList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tmpList)+(getBezierY(i + 1,tmpList)-getBezierY(i-1,tmpList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 1)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tmpList)-(getBezierY(i + 1,tmpList)-getBezierY(i,tmpList))*curvature

                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tmpList))

                }
            }
            canvas.drawText(tmpList[i].toString() + "°", getBezierX(i), getBezierY(i, tmpList)-textSpace, textPaint)
        }
        temPathMeasureSpec.setPath(path, false)
        mLength = temPathMeasureSpec.length
        mDst.reset()
        mDst.lineTo(0f,0f)
        val stop = mLength * mAnimatorValue
        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
        canvas.drawPath(mDst, tmpPaint)

    }


    private fun drawTmpBrokenLine(canvas: Canvas){
        for (i in tmpList.indices) {
            when {
                i == 0 -> {
                    path.moveTo(getBezierX(2*i+1), getBezierY(i,tmpList))
                    path.lineTo(getBezierX(2*i+3), getBezierY(i,tmpList))
                }
                i<tmpList.size-1 -> {
                    path.lineTo(getBezierX(2*i+3), getBezierY(i,tmpList))
                }
            }
            //            canvas.drawCircle(getBezierX(2*i+1),getBezierY(i,tmpList),pointRadius,pointPaint);
            //            canvas.drawCircle(getBezierX(2*i+1),getBezierY(i,tmpList),outPointRadius,outPointPaint);
            canvas.drawText(tmpList[i].toString() + "°", getBezierX(2 * i + 1), getBezierY(i, tmpList) - 20, textPaint)
        }
        temPathMeasureSpec.setPath(path, false)
        mLength = temPathMeasureSpec.length
        mDst.reset()
        mDst.lineTo(0f,0f)
        val stop = mLength * mAnimatorValue
        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
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
        invalidate()
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

