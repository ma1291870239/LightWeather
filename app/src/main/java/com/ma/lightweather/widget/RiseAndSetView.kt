package com.ma.lightweather.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.utils.CommonUtils
import java.text.SimpleDateFormat

class RiseAndSetView(context: Context, attrs: AttributeSet) : View(context, attrs)  {

    private var viewHigh = 0 //控件高度
    private var viewWidth = 0 //控件宽度
    private var riseTime = "6:08:00"
    private var setTime = "18:51:00"
    private var date = "2020-09-08 "

    private val linePaint = Paint()
    private val shadowPaint = Paint()
    private val textPaint = Paint()
    private val linePath = Path()
    private val shadowPath = Path()
    private var curvature=0.2f // 0<curvature<0.5

    init {
        init()
    }

    private fun init() {
        linePaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = pointWidth
        linePaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, R.color.primary_white_text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.setShadowLayer(5f,3f,3f, ContextCompat.getColor(context, R.color.shadow_black_text))

        shadowPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        shadowPaint.isAntiAlias=true
        shadowPaint.strokeWidth = lineWidth
        shadowPaint.style = Paint.Style.FILL

    }

    override fun onDraw(canvas: Canvas) {
        viewHigh = measuredHeight
        viewWidth = measuredWidth
        val nowTime=SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
        val time=CommonUtils.getTimeValue(date,riseTime,setTime)
        val now= CommonUtils.getTimeValue(date,"00:00:00",nowTime)
        val timeStart=CommonUtils.getTimeValue(date,"00:00:00",riseTime)
        val timeEnd=CommonUtils.getTimeValue(date,setTime,"23:59:59")
        val allTime=time+timeStart+timeEnd

        val ySpace=viewHigh/5

        val pointX1=0
        val pointY1=viewHigh-ySpace
        val pointX2=timeStart/allTime.toFloat()*viewWidth
        val pointY2=3f*ySpace
        val pointX3=(timeStart+time/2)/allTime.toFloat()*viewWidth
        val pointY3=1.5f*ySpace
        val pointX4=(timeStart+time)/allTime.toFloat()*viewWidth
        val pointY4=3f*ySpace
        val pointX5=viewWidth
        val pointY5=viewHigh-ySpace

        linePath.reset()
        shadowPath.reset()

        canvas.drawLine(0f,3f*ySpace,viewWidth.toFloat(),3f*ySpace,linePaint)
        for (i in 0..3) {
            var x1 = 0f
            var y1 = 0f

            var x2 = 0f
            var y2 = 0f
            when (i) {
                0 -> {
                    x1=pointX1+(pointX2-pointX1)*curvature
                    y1=pointY1+(pointY2-pointY1)*curvature

                    x2=pointX2-(pointX3-pointX1)*curvature
                    y2=pointY2-(pointY3-pointY1)*curvature
                    linePath.moveTo(0f,(viewHigh-ySpace).toFloat())
                    linePath.cubicTo(x1,y1,x2,y2,pointX2,pointY2)

                    shadowPath.moveTo(0f, (viewHigh - ySpace).toFloat())
                    if (now>timeStart) {
                        shadowPath.cubicTo(x1, y1, x2, y2, pointX2, pointY2)
                        shadowPath.lineTo(0f, 3f * ySpace)
                        shadowPath.lineTo(0f, (viewHigh - ySpace).toFloat())
                    }
                }
                1 -> {
                    x1=pointX2+(pointX3-pointX1)*curvature
                    y1=pointY2+(pointY3-pointY1)*curvature

                    x2=pointX3-(pointX4-pointX2)*curvature
                    y2=pointY3-(pointY4-pointY2)*curvature
                    linePath.cubicTo(x1,y1,x2,y2,pointX3,pointY3)
                }
                2 -> {
                    x1=pointX3+(pointX4-pointX2)*curvature
                    y1=pointY3+(pointY4-pointY2)*curvature

                    x2=pointX4-(pointX5-pointX3)*curvature
                    y2=pointY4-(pointY5-pointY3)*curvature
                    linePath.cubicTo(x1,y1,x2,y2,pointX4,pointY4)
                }
                3 -> {
                    x1=pointX4+(pointX5-pointX3)*curvature
                    y1=pointY4+(pointY5-pointY3)*curvature

                    x2=pointX5-(pointX5-pointX4)*curvature
                    y2=pointY5-(pointY5-pointY4)*curvature
                    linePath.cubicTo(x1,y1,x2,y2,pointX5.toFloat(),pointY5.toFloat())

                    shadowPath.moveTo(pointX4,pointY4)
                    shadowPath.cubicTo(x1,y1,x2,y2,pointX5.toFloat(),pointY5.toFloat())
                    shadowPath.lineTo(viewWidth.toFloat(),3f*ySpace)
                    shadowPath.lineTo(pointX4,pointY4)
                }
            }
        }
        canvas.drawPath(linePath, linePaint)

        canvas.drawPath(shadowPath, shadowPaint)
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

