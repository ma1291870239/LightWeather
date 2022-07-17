package com.ma.lightweather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.CommonUtils
import java.text.FieldPosition
import java.util.*
import kotlin.collections.ArrayList

class FrogHourWeatherView (context: Context, attrs: AttributeSet?) : View(context, attrs) {



    private val textPaint = Paint()
    private val shadowPaint = Paint()
    private val tempPaint = Paint()
    private val path = Path()
    private var mDst= Path()
    private val shadowPath = Path()
    private var temPathMeasureSpec= PathMeasure()

    private var tempList:List<Int> = listOf()
    private var i:Int = 0
    private var maxTemp=0
    private var minTemp=0

    private var viewHigh = 0 //控件高度
    private var viewWidth = 0 //控件宽度
    private var yUnit = 0f //y轴单位长度
    private var textHigh = 0f //文字高度
    private var textSpace = 0f //文字间隔
    private var curvature=0.1f // 0<curvature<0.5
    private var mLength= 0f
    private var mAnimatorValue= 0f


    init {
        init()
    }

    private fun init() {

        tempPaint.color = ContextCompat.getColor(context, R.color.temp_line)
        tempPaint.isAntiAlias = true
        tempPaint.strokeWidth = lineWidth
        tempPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, R.color.primary_white_text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.setShadowLayer(5f,3f,3f, ContextCompat.getColor(context, R.color.shadow_black_text))

        shadowPaint.isAntiAlias=true
        shadowPaint.strokeWidth = lineWidth

    }

    override fun onDraw(canvas: Canvas) {
        viewHigh = measuredHeight
        viewWidth = measuredWidth
        textPaint.textSize= CommonUtils.sp2px(context,14f).toFloat()
        val fm = textPaint.fontMetrics
        textHigh = fm.bottom - fm.top
        textSpace=0.5f*textHigh
        yUnit=(viewHigh-(textHigh+textSpace))/(maxTemp-minTemp)
        drawTempCurve(canvas)
    }

    private fun drawTempCurve(canvas: Canvas){
        if(i<0||i>tempList.size-2) return
        path.reset()
        shadowPath.reset()

        var x1 = 0f
        var y1 = 0f
        if(i==0){
            x1 = viewWidth*curvature+viewWidth/2
            y1=getBezierY(i,tempList)
        }else{
            x1 = 2*viewWidth*curvature+viewWidth/2
            y1=getBezierY(i,tempList)+(getBezierY(i + 1,tempList)-getBezierY(i,tempList))*curvature
        }

        var x2 = 0f
        var y2 = 0f
        if(i==tempList.size-2){
            x2 = viewWidth*(1-curvature)+viewWidth/2
            y2=getBezierY(i+1,tempList)
        }else{
            x2 = viewWidth*(1-2*curvature)+viewWidth/2
            y2=getBezierY(i+1,tempList)-(getBezierY(i + 2,tempList)-getBezierY(i+1,tempList))*curvature
        }

        path.moveTo(0f, getBezierY(i, tempList))
        path.cubicTo(x1, y1, x2, y2, viewWidth.toFloat(), getBezierY(i + 1, tempList))

        shadowPath.moveTo(0f, getBezierY(i, tempList))
        shadowPath.cubicTo(x1, y1, x2, y2,viewWidth.toFloat(), getBezierY(i + 1, tempList))

        canvas.drawText("${tempList[i]}°", (viewWidth/2).toFloat(), getBezierY(i, tempList)-textSpace, textPaint)
        temPathMeasureSpec.setPath(path, false)
        mLength = temPathMeasureSpec.length
        mDst.reset()
        mDst.lineTo(0f,0f)
        val stop = mLength * mAnimatorValue
        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
        canvas.drawPath(mDst, tempPaint)

        shadowPaint.shader = LinearGradient(0f, 0f, 0f, (maxTemp - minTemp+3) * yUnit + textHigh + textSpace,
                intArrayOf(
                        Color.argb(55, 255, 255, 255),
                        Color.argb(0, 255, 255, 255)),
                null, Shader.TileMode.CLAMP)
        canvas.drawPath(shadowPath,shadowPaint)
    }

    private fun getBezierY(i: Int, list: List<Int>): Float {
        return (maxTemp - list[i]) * yUnit+(textHigh+textSpace)
    }

    fun setData(tempList:List<Int>,position: Int) {
        this.tempList=tempList
        this.i=position

        maxTemp= Collections.max(tempList)
        minTemp= Collections.min(tempList)

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { valueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Float
            postInvalidate()
        }
        valueAnimator.duration = 3000
        valueAnimator.start()
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