package com.ma.lightweather.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.utils.CommonUtils
import java.util.*
import kotlin.math.sqrt

class FrogHourWeatherView (context: Context, attrs: AttributeSet?) : View(context, attrs) {



    private val textPaint = Paint()
    private val shadowPaint = Paint()
    private val tempPaint = Paint()
    private val pointPaint = Paint()
    private val lengthPaint = Paint()
    private val pointControlPaint = Paint()
    private val path = Path()
    private var mDst= Path()
    private val shadowPath = Path()
    private var temPathMeasureSpec= PathMeasure()

    private var tempList:List<Int> = listOf()
    private var i:Int = 0
    private var isSingle:Boolean=false
    private var maxTemp=0
    private var minTemp=0

    private var viewHigh = 0f //控件高度
    private var viewWidth = 0f //控件宽度
    private var yUnit = 0f //y轴单位长度
    private var textHigh = 0f //文字高度
    private var textSpace = 0f //文字间隔
    private var ratio=0.6f//0.2-0.6比较好
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

        pointPaint.color = ContextCompat.getColor(context, R.color.black)
        pointPaint.isAntiAlias = true
        pointPaint.strokeWidth = lineWidth
        pointPaint.strokeCap = Paint.Cap.ROUND

        lengthPaint.color = ContextCompat.getColor(context, R.color.redColorAccent)
        lengthPaint.isAntiAlias = true
        lengthPaint.strokeWidth = lineWidth
        lengthPaint.strokeCap = Paint.Cap.ROUND

        pointControlPaint.color = ContextCompat.getColor(context, R.color.wind_text)
        pointControlPaint.isAntiAlias = true
        pointControlPaint.strokeWidth = lineWidth
        pointControlPaint.strokeCap = Paint.Cap.ROUND

    }

    override fun onDraw(canvas: Canvas) {
        viewHigh = measuredHeight.toFloat()
        viewWidth = measuredWidth.toFloat()
        textPaint.textSize= CommonUtils.sp2px(context,14f).toFloat()
        val fm = textPaint.fontMetrics
        textHigh = fm.bottom - fm.top
        textSpace=0.5f*textHigh
        yUnit=(viewHigh-(textHigh+2*textSpace))/(maxTemp-minTemp)
        if(isSingle) {
            drawSingleTempCurve(canvas)
        }else {
            drawListTempCurve(canvas)
        }
    }


    private fun drawListTempCurve(canvas: Canvas){

    }

    private fun drawSingleTempCurve(canvas: Canvas){
        if(i<0||i>=tempList.size) return
        path.reset()
        shadowPath.reset()

        val beforeTemp2=getPoint(i,-2)
        val beforeTemp1=getPoint(i,-1)
        val nowTemp=getPoint(i,0)
        val nextTemp1=getPoint(i,1)
        val nextTemp2=getPoint(i,2)

        path.moveTo(beforeTemp1.x,beforeTemp1.y)
        shadowPath.moveTo(beforeTemp1.x,beforeTemp1.y)

        val beforeControls=getControlPoint(beforeTemp2.x,beforeTemp2.y,
            beforeTemp1.x,beforeTemp1.y,
            nowTemp.x,nowTemp.y,canvas)
        val nowControls=getControlPoint(beforeTemp1.x,beforeTemp1.y,
            nowTemp.x,nowTemp.y,
            nextTemp1.x,nextTemp1.y,canvas)
        val nextControls=getControlPoint( nowTemp.x,nowTemp.y,
            nextTemp1.x,nextTemp1.y,
            nextTemp2.x,nextTemp2.y,canvas)
        path.cubicTo(beforeControls[1].x,beforeControls[1].y,nowControls[0].x,nowControls[0].y,nowTemp.x,nowTemp.y)
        path.cubicTo(nowControls[1].x,nowControls[1].y,nextControls[0].x,nextControls[0].y,nextTemp1.x,nextTemp1.y)

        shadowPath.cubicTo(beforeControls[1].x,beforeControls[1].y,nowControls[0].x,nowControls[0].y,nowTemp.x,nowTemp.y)
        shadowPath.cubicTo(nowControls[1].x,nowControls[1].y,nextControls[0].x,nextControls[0].y,nextTemp1.x,nextTemp1.y)
        shadowPath.lineTo(nextTemp1.x,viewHigh)
        shadowPath.lineTo(beforeTemp1.x,viewHigh)
        shadowPath.lineTo(beforeTemp1.x,beforeTemp1.y)
        canvas.drawText("${tempList[i]}°", getX(0), getY(i) -textSpace, textPaint)

//        canvas.drawPoint(viewWidth/2,getY(i,tempList),pointPaint)
//        canvas.drawPoint(nowPoints[0].x,nowPoints[0].y,pointControlPaint)
//        canvas.drawPoint(nowPoints[1].x,nowPoints[1].y,pointControlPaint)

//        temPathMeasureSpec.setPath(path, false)
//        mLength = temPathMeasureSpec.length
//        mDst.reset()
//        mDst.lineTo(0f,0f)
//        val stop = mLength * mAnimatorValue
//        val stop = mLength
//        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
        canvas.drawPath(path, tempPaint)

        shadowPaint.shader = LinearGradient(0f, 0f, 0f, minTemp * yUnit ,
                intArrayOf(
                        Color.argb(55, 255, 255, 255),
                        Color.argb(0, 255, 255, 255)),
                null, Shader.TileMode.CLAMP)
        canvas.drawPath(shadowPath,shadowPaint)
    }


    /**
     * 当前点pt，当前点前后两点p1、p2， pt点对应的前后控制点c1、c2
     * 参考图assets/bezier
     *
     * 平滑
     * 参考图，对于p1-pt-p2两段线，算法生成p1~pt 和 pt~p2两条贝塞尔曲线
     * 两条贝塞尔曲线在pt点对应的控制点分别为c1、c2
     * 因为贝塞尔曲线与该点和其控制点的连线相切，所以保证c1、pt、c2三点共线就可以产生平滑的曲线
     *
     * 控制点自动计算
     * 手工控制各控制点是很繁琐的，如何自动计算控制点是本算法的核心，算法实现方式为：
     * 取c1、pt、c2为角p1-pt-p2的角平分线的垂线
     * 取c1-pt与c2-pt等长，为p1或p2在该垂线上的投影点，（参看图，当p1-pt长度大于pt-p2时，取p11点在垂线的投影点作为c1，p11到pt的距离与pt-p2等长）
     * 对c1、c2做一定比例的缩放，实际取的控制点距pt的距离为投影点距离的0.2-0.6之间时都可以取得很好的平滑效果
     *
     * 折线生成
     * 同样方法可以计算p1,p2的前后控制点，这样通过p1和p1的后控制点、pt和c1可以绘制曲线p1~pt，依次类推....
     * 对于首尾节点，可以简单的取其控制点为自身
     *
     * 多边形生成
     * 与折线区别仅在于首点控制点需要通过尾节点生成，尾节点需要利过首节点生成
     */

    private fun getControlPoint(beforeX: Float, beforeY: Float,nowX: Float, nowY: Float, nextX: Float, nextY: Float,canvas: Canvas): List<TempPoint> {
        val points= arrayListOf<TempPoint>()

        val beforeLength= sqrt((nowX-beforeX)*(nowX-beforeX)+(nowY-beforeY)*(nowY-beforeY))
        val nextLength= sqrt((nextX-nowX)*(nextX-nowX)+(nextY-nowY)*(nextY-nowY))

        //等长点计算  等价于 y/x=k  x²+y²=length²   x=length/√(1+k²)   y=k*x
        val lengthPoint=TempPoint()

        //控制点
        val pointLeft=TempPoint()
        val pointRight=TempPoint()

        var controlK=0f
        var controlLength=0f

        when {
            beforeLength>nextLength -> {
                val lengthK=(nowY-beforeY)/(nowX-beforeX)
                lengthPoint.x=nowX- nextLength/sqrt(1+lengthK*lengthK)
                lengthPoint.y=nowY- lengthK*(nowX-lengthPoint.x)
                //canvas.drawPoint(lengthPoint.x,lengthPoint.y,lengthPaint)

                controlK=(nextY-lengthPoint.y)/(nextX-lengthPoint.x)
                controlLength=sqrt((nextX-lengthPoint.x)*(nextX-lengthPoint.x)+(nextY-lengthPoint.y)*(nextY-lengthPoint.y))*ratio/2

            }
            beforeLength<nextLength -> {
                val lengthK=(nextY-nowY)/(nextX-nowX)
                lengthPoint.x=nowX+ beforeLength/sqrt(1+lengthK*lengthK)
                lengthPoint.y=nowY+ lengthK*(lengthPoint.x-nowX)
                //canvas.drawPoint(lengthPoint.x,lengthPoint.y,lengthPaint)

                controlK=(lengthPoint.y-beforeY)/(lengthPoint.x-beforeX)
                controlLength=sqrt((lengthPoint.x-beforeX)*(lengthPoint.x-beforeX)+(lengthPoint.y-beforeY)*(lengthPoint.y-beforeY))*ratio/2

            }
            beforeLength==nextLength -> {
                controlK=(nextY-beforeY)/(nextX-beforeX)
                controlLength=sqrt((nextX-beforeX)*(nextX-beforeX)+(nextY-beforeY)*(nextY-beforeY))*ratio/2

            }
        }

        pointLeft.x=nowX- controlLength/sqrt(1+controlK*controlK)
        pointLeft.y=nowY- controlK*(nowX-pointLeft.x)

        pointRight.x=nowX+ controlLength/sqrt(1+controlK*controlK)
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

        points.add(pointLeft)
        points.add(pointRight)

        return points
    }

    private fun getPoint(i: Int,offset:Int): TempPoint {
        val tempPoint=TempPoint()

        tempPoint.x=getX(offset)

        if((i==0||i==1)&&(offset<0)){
            tempPoint.y=getY(0)
        }else if((i==tempList.size-1||i==tempList.size-2)&&(offset>0)){
            tempPoint.y=getY(tempList.size-1)
        }else{
            tempPoint.y=getY(i+offset)
        }

        return tempPoint
    }

    private fun getX(i: Int): Float {
        return viewWidth/2*(1+i*2)
    }

    private fun getY(i: Int): Float {
        return (maxTemp - tempList[i]) * yUnit+(textHigh+textSpace)
    }

    fun setData(tempList:List<Int>,position: Int,isSingle:Boolean) {
        this.tempList=tempList
        this.i=position
        this.isSingle=isSingle

        maxTemp= Collections.max(tempList)
        minTemp= Collections.min(tempList)

//        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
//        valueAnimator.addUpdateListener { valueAnimator ->
//            mAnimatorValue = valueAnimator.animatedValue as Float
//            postInvalidate()
//        }
//        valueAnimator.duration = 3000
//        valueAnimator.start()
        postInvalidate()
    }

    class TempPoint{
        var x = 0f
        var y = 0f
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