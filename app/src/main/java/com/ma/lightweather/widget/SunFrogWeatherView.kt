package com.ma.lightweather.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.utils.CommonUtils
import kotlin.math.sqrt

class SunFrogWeatherView(context: Context, attrs: AttributeSet?) : View(context, attrs)  {

    private val textPaint = Paint()
    private val shadowPaint = Paint()
    private val sunPaint = Paint()
    private val path = Path()
    private val shadowPath = Path()

    private var viewHigh = 0f //控件高度
    private var viewWidth = 0f //控件宽度
    private var xUnit = 0f //x轴单位长度
    private var textHigh = 0f //文字高度
    private var textSpace = 0f //文字间隔
    private var ratio=0.4f//0.2-0.6比较好

    private var sunRiseStr="05:35"
    private var sunSetStr="15:34"
    private var sunRise=0
    private var sunSet=0
    private var list= mutableListOf<SunPoint>()

    init {
        init()
    }

    private fun init() {
        sunPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        sunPaint.isAntiAlias = true
        sunPaint.strokeWidth = lineWidth
        sunPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.setShadowLayer(5f,3f,3f, ContextCompat.getColor(context, R.color.shadow_black_text))

        shadowPaint.color = ContextCompat.getColor(context, R.color.primary_black_text)
        shadowPaint.isAntiAlias=true
        shadowPaint.strokeWidth = lineWidth
    }

    override fun onDraw(canvas: Canvas) {


        viewHigh = measuredHeight.toFloat()
        viewWidth = measuredWidth.toFloat()
        textPaint.textSize= CommonUtils.sp2px(context,14f).toFloat()
        val fm = textPaint.fontMetrics
        textHigh = fm.bottom - fm.top
        textSpace=0.5f*textHigh
        xUnit=viewWidth/1440
        drawSun(canvas)
    }

    private fun drawSun(canvas: Canvas){
        path.moveTo(0f,viewHigh*2/3)
        path.lineTo(viewWidth,viewHigh*2/3)

        canvas.drawPath(path, sunPaint)
        for(i in list.indices){

            val beforeSun1=getPoint(i,-1)
            val nowSun=getPoint(i,0)
            val nextSun1=getPoint(i,1)
            val nextSun2=getPoint(i,2)

            val nowControls=getControlPoint(beforeSun1.x,beforeSun1.y,
                nowSun.x,nowSun.y,
                nextSun1.x,nextSun1.y,canvas)
            val nextControls=getControlPoint( nowSun.x,nowSun.y,
                nextSun1.x,nextSun1.y,
                nextSun2.x,nextSun2.y,canvas)

            path.reset()
            path.moveTo(nowSun.x,nowSun.y)
            path.cubicTo(nowControls[1].x,nowControls[1].y,nextControls[0].x,nextControls[0].y,nextSun1.x,nextSun1.y)
            canvas.drawPath(path, sunPaint)

            when (i) {
                0 -> {
                    shadowPaint.color =
                        ContextCompat.getColor(context, R.color.weather_back_cloud)
                }
                1 -> {
                    shadowPaint.shader = LinearGradient(nowSun.x, viewHigh*2/3, nextSun1.x, nextSun1.y ,
                        intArrayOf(
                            ContextCompat.getColor(context, R.color.weather_back_cloud),
                            ContextCompat.getColor(context, R.color.white)),
                        null, Shader.TileMode.CLAMP)

//                    shadowPaint.shader =RadialGradient(nextSun1.x, nextSun1.y+(viewHigh*2/3-nextSun1.y)/2, 100f,
//                        intArrayOf(
//                            ContextCompat.getColor(context, R.color.white),
//                            ContextCompat.getColor(context, R.color.weather_back_cloud)),
//                        null, Shader.TileMode.CLAMP)
                }
                2 -> {
                    nowSun.x=nowSun.x-1 //抵消阴影中间线条
                    shadowPaint.shader = LinearGradient(nowSun.x, nowSun.y, nextSun1.x, viewHigh*2/3 ,
                        intArrayOf(
                            ContextCompat.getColor(context, R.color.white),
                            ContextCompat.getColor(context, R.color.weather_back_cloud)),
                        null, Shader.TileMode.CLAMP)
                }
                3 -> {
                    shadowPaint.color =
                        ContextCompat.getColor(context, R.color.weather_back_cloud)
                }
            }
            shadowPath.reset()
            shadowPath.moveTo(nowSun.x,nowSun.y)
            shadowPath.cubicTo(nowControls[1].x,nowControls[1].y,nextControls[0].x,nextControls[0].y,nextSun1.x,nextSun1.y)
            shadowPath.lineTo(nextSun1.x,viewHigh*2/3)
            shadowPath.lineTo(nowSun.x,viewHigh*2/3)
            shadowPath.lineTo(nowSun.x,nowSun.y)
            canvas.drawPath(shadowPath, shadowPaint)
        }

    }

    private fun getPoint(i: Int,offset:Int): SunPoint {
        val sunPoint= SunPoint(0f,0f)

        if((i==0)&&(offset<0)){
            sunPoint.x=list[0].x
            sunPoint.y=list[0].y
        }else if((i==list.size-1||i==list.size-2)&&(offset>0)){
            sunPoint.x=list[list.size-1].x
            sunPoint.y=list[list.size-1].y
        }else{
            sunPoint.x=list[i+offset].x
            sunPoint.y=list[i+offset].y
        }

        return sunPoint
    }

    private fun getControlPoint(beforeX: Float, beforeY: Float,nowX: Float, nowY: Float, nextX: Float, nextY: Float,canvas: Canvas): List<SunPoint> {
        val points= arrayListOf<SunPoint>()

        val beforeLength= sqrt((nowX-beforeX)*(nowX-beforeX)+(nowY-beforeY)*(nowY-beforeY))
        val nextLength= sqrt((nextX-nowX)*(nextX-nowX)+(nextY-nowY)*(nextY-nowY))

        //等长点计算  等价于 y/x=k  x²+y²=length²   x=length/√(1+k²)   y=k*x
        val lengthPoint= SunPoint(0f,0f)

        //控制点
        val pointLeft= SunPoint(0f,0f)
        val pointRight= SunPoint(0f,0f)

        var controlK=0f
        var controlLength=0f

        when {
            beforeLength>nextLength -> {
                val lengthK=(nowY-beforeY)/(nowX-beforeX)
                lengthPoint.x=nowX- nextLength/ sqrt(1+lengthK*lengthK)
                lengthPoint.y=nowY- lengthK*(nowX-lengthPoint.x)
                //canvas.drawPoint(lengthPoint.x,lengthPoint.y,lengthPaint)

                controlK=(nextY-lengthPoint.y)/(nextX-lengthPoint.x)
                controlLength=
                    sqrt((nextX-lengthPoint.x)*(nextX-lengthPoint.x)+(nextY-lengthPoint.y)*(nextY-lengthPoint.y)) *ratio/2

                if(nextX==lengthPoint.x){
                    controlK=0f
                    controlLength=0f
                }
            }
            beforeLength<nextLength -> {
                val lengthK=(nextY-nowY)/(nextX-nowX)
                lengthPoint.x=nowX+ beforeLength/ sqrt(1+lengthK*lengthK)
                lengthPoint.y=nowY+ lengthK*(lengthPoint.x-nowX)
                //canvas.drawPoint(lengthPoint.x,lengthPoint.y,lengthPaint)

                controlK=(lengthPoint.y-beforeY)/(lengthPoint.x-beforeX)
                controlLength=
                    sqrt((lengthPoint.x-beforeX)*(lengthPoint.x-beforeX)+(lengthPoint.y-beforeY)*(lengthPoint.y-beforeY)) *ratio/2

                if(lengthPoint.x==beforeX){
                    controlK=0f
                    controlLength=0f
                }
            }
            beforeLength==nextLength -> {
                controlK=(nextY-beforeY)/(nextX-beforeX)
                controlLength=
                    sqrt((nextX-beforeX)*(nextX-beforeX)+(nextY-beforeY)*(nextY-beforeY)) *ratio/2

            }
        }

        pointLeft.x=nowX- controlLength/ sqrt(1+controlK*controlK)
        pointLeft.y=nowY- controlK*(nowX-pointLeft.x)

        pointRight.x=nowX+ controlLength/ sqrt(1+controlK*controlK)
        pointRight.y=nowY+ controlK*(pointRight.x-nowX)

        if(beforeY==nowY){
            pointLeft.x= nowX
            pointLeft.y= nowY
        }
        if(nextY==nowY){
            pointRight.x= nowX
            pointRight.y= nowY
        }
        if(beforeY-nowY==nowY-nextY){
            pointLeft.x= nowX
            pointLeft.y= nowY
            pointRight.x= nowX
            pointRight.y= nowY
        }
        if(controlK==0f&&controlLength==0f){
            pointLeft.x= nowX
            pointLeft.y= nowY
            pointRight.x= nowX
            pointRight.y= nowY
        }

        points.add(pointLeft)
        points.add(pointRight)

        return points
    }



    fun setDate(sunRiseStr:String,sunSetStr:String){
        this.sunRiseStr=sunRiseStr
        this.sunSetStr=sunSetStr
        sunRise=changeTime(sunRiseStr)
        sunSet=changeTime(sunSetStr)
        var middle=(sunRise+(sunSet-sunRise)/2)
        list.add(SunPoint(0f,viewHigh))
        list.add(SunPoint(xUnit*sunRise,viewHigh*2/3))
        list.add(SunPoint(xUnit*middle,0f))
        list.add(SunPoint(xUnit*sunSet,viewHigh*2/3))
        list.add(SunPoint(viewWidth,viewHigh))

        postInvalidate()
    }


    private fun changeTime(time:String):Int{
        var min=0;
        var times=time.split(":")
        if(times.size>=2){
            min=times[0].toInt()*60+times[1].toInt()
        }
        return min
    }

    class SunPoint(x:Float,y:Float){
        var x = x
        var y = y
    }

    companion object {

        private const val dividerWidth = 1f
        private const val lineWidth = 1f
        private const val pointWidth = 3f
        private const val outPointWidth = 3f
        private const val pointRadius = 5f
        private const val outPointRadius = 10f
    }
}