package com.ma.lightweather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Scroller
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.CommonUtils
import java.util.*
import kotlin.math.abs


class HourFrogWeatherView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val pointPaint = Paint()
    private val outPointPaint = Paint()
    private val textPaint = Paint()
    private val shadowPaint = Paint()
    private val dividerPaint = Paint()
    private val tmpPaint = Paint()
    private val path = Path()
    private var mDst= Path()
    private val shadowPath = Path()
    private var temPathMeasureSpec= PathMeasure()




    private var tempList: MutableList<Int> = ArrayList()
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

    private var scroller: Scroller? = null
    private var velocityTracker: VelocityTracker? = null

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

        tmpPaint.color = ContextCompat.getColor(context, R.color.temp_line)
        tmpPaint.isAntiAlias = true
        tmpPaint.strokeWidth = lineWidth
        tmpPaint.style = Paint.Style.STROKE

        textPaint.color = ContextCompat.getColor(context, R.color.primary_white_text)
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.setShadowLayer(5f,3f,3f,ContextCompat.getColor(context,R.color.shadow_black_text))

        shadowPaint.isAntiAlias=true
        shadowPaint.strokeWidth = lineWidth


        dividerPaint.color = ContextCompat.getColor(context, R.color.hint_white_text)
        dividerPaint.isAntiAlias = true
        dividerPaint.strokeMiter= dividerWidth
        dividerPaint.textAlign = Paint.Align.CENTER


        scroller =Scroller(context)
        velocityTracker = VelocityTracker.obtain()
    }

    override fun onDraw(canvas: Canvas) {
        viewHigh = measuredHeight
        viewWidth = (measuredWidth*1.5).toInt()
        //textPaint.textSize = viewWidth / (4*xPart)//按每格4个字计算文字大小
        textPaint.textSize=CommonUtils.sp2px(context,14f).toFloat()
        val fm = textPaint.fontMetrics
        textHigh = fm.bottom - fm.top
        textSpace=0.5f*textHigh
        xUnit = viewWidth / (2*xPart)
        yUnit = getY(5,7) / 50
        if (tempList.isNotEmpty()) {
            max = Collections.max(tempList)
            min = Collections.min(tempList)
            yUnit = getY(5,7) / (max - min)
        }

        //实时温度曲线
        drawTmpCurve(canvas)


        //天气状况
        for (i in txtList.indices) {
            canvas.drawText(txtList[i], getBezierX(i), getY(3,4), textPaint)
//            val bitmap=BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
//            canvas.drawBitmap(bitmap, getBezierX(i), getY(3,4), Paint( Paint.ANTI_ALIAS_FLAG))
        }
        textPaint.color=ContextCompat.getColor(context,R.color.black)
        textPaint.setShadowLayer(5f,3f,3f,ContextCompat.getColor(context,R.color.shadow_white_text))
        //日期
        for (i in dateList.indices) {
            canvas.drawText(CommonUtils.dateTimeFormat(dateList[i],"HH时"), getBezierX(i), getY(2, 3), textPaint)
        }
        textPaint.color=ContextCompat.getColor(context,R.color.white)
        //降水概率
        for (i in popList.indices) {
            if(i>=0){
                canvas.drawText("预计今日有降水", 2*getBezierX(0),getY(0,2), textPaint)
                break
            }
            canvas.drawText("预计今日无降水", 2*getBezierX(0),getY(0,2), textPaint)
        }
    }

    private fun drawTmpCurve(canvas: Canvas){
        path.reset()
        shadowPath.reset()
        for (i in tempList.indices) {

            var x1 = 0f
            var y1 = 0f

            var x2 = 0f
            var y2 = 0f

            when{
                i == 0 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i))*curvature
                    y1=getBezierY(i,tempList)+(getBezierY(i + 1,tempList)-getBezierY(i,tempList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 2)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tempList)-(getBezierY(i + 2,tempList)-getBezierY(i,tempList))*curvature

                    path.moveTo(getBezierX(i), getBezierY(i, tempList))
                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))

                    shadowPath.moveTo(getBezierX(i), getBezierY(i, tempList))
                    shadowPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))
                }
                i< tempList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tempList)+(getBezierY(i + 1,tempList)-getBezierY(i-1,tempList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 2)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tempList)-(getBezierY(i + 2,tempList)-getBezierY(i,tempList))*curvature

                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))

                    shadowPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))

                }
                i == tempList.size - 2 -> {
                    x1=getBezierX(i)+(getBezierX(i + 1)-getBezierX(i-1))*curvature
                    y1=getBezierY(i,tempList)+(getBezierY(i + 1,tempList)-getBezierY(i-1,tempList))*curvature

                    x2=getBezierX(i+1)-(getBezierX(i + 1)-getBezierX(i))*curvature
                    y2=getBezierY(i+1,tempList)-(getBezierY(i + 1,tempList)-getBezierY(i,tempList))*curvature

                    path.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))

                    shadowPath.cubicTo(x1, y1, x2, y2, getBezierX(i + 1), getBezierY(i + 1, tempList))
                    shadowPath.lineTo(getBezierX(i + 1), (max - min+3) * yUnit + textHigh + textSpace)
                    shadowPath.lineTo(xUnit, (max - min+3) * yUnit + textHigh + textSpace)
                    shadowPath.lineTo(xUnit, getBezierY(0, tempList))
                }
            }
            canvas.drawText(tempList[i].toString() + "°", getBezierX(i), getBezierY(i, tempList)-textSpace, textPaint)
        }
        temPathMeasureSpec.setPath(path, false)
        mLength = temPathMeasureSpec.length
        mDst.reset()
        mDst.lineTo(0f,0f)
        val stop = mLength * mAnimatorValue
        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
        canvas.drawPath(mDst, tmpPaint)

        shadowPaint.shader = LinearGradient(0f, 0f, 0f, (max - min+3) * yUnit + textHigh + textSpace,
                intArrayOf(
                        Color.argb(55, 255, 255, 255),
                        Color.argb(0, 255, 255, 255)),
                null, Shader.TileMode.CLAMP)
        canvas.drawPath(shadowPath,shadowPaint)
    }


//    private fun drawTmpBrokenLine(canvas: Canvas){
//        for (i in tmpList.indices) {
//            when {
//                i == 0 -> {
//                    path.moveTo(getBezierX(2*i+1), getBezierY(i,tmpList))
//                    path.lineTo(getBezierX(2*i+3), getBezierY(i,tmpList))
//                }
//                i<tmpList.size-1 -> {
//                    path.lineTo(getBezierX(2*i+3), getBezierY(i,tmpList))
//                }
//            }
//                        canvas.drawCircle(getBezierX(2*i+1),getBezierY(i,tmpList),pointRadius,pointPaint);
//                        canvas.drawCircle(getBezierX(2*i+1),getBezierY(i,tmpList),outPointRadius,outPointPaint);
//            canvas.drawText(tmpList[i].toString() + "°", getBezierX(2 * i + 1), getBezierY(i, tmpList) - 20, textPaint)
//        }
//        temPathMeasureSpec.setPath(path, false)
//        mLength = temPathMeasureSpec.length
//        mDst.reset()
//        mDst.lineTo(0f,0f)
//        val stop = mLength * mAnimatorValue
//        temPathMeasureSpec.getSegment(0f,stop,mDst,true)
//        canvas.drawPath(mDst, tmpPaint)
//    }

    fun loadViewData(hourlyList: List<HFWeather.HFWeatherHour>?) {
        if (hourlyList != null) {
            tempList.clear()
            popList.clear()
            dateList.clear()
            txtList.clear()
            dirList.clear()
            for (hourly in hourlyList){
                (tempList as ArrayList).add(hourly.temp.toInt())
                (popList as ArrayList).add(hourly.pop.toInt())
                hourly.fxTime.let { (dateList as ArrayList).add(it) }
                hourly.text.let { (txtList as ArrayList).add(it) }
                hourly.windDir.let { (dirList as ArrayList).add(it) }
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
        return (max - list[i]) * yUnit + textHigh + textSpace
    }

    private fun getY(i1: Int,i2:Int): Float {
        return viewHigh - (i1 * textHigh+i2*textSpace)
    }


    private var startX = 0f
    private var startY = 0f
    private var deltaX = 0f
    private var deltaY = 0f

    private var touchX = 0f
    private var touchY = 0f
    private var touchDeltaX = 0f
    private var touchDeltaY = 0f
    private var touchStartX = 0f
    private var touchStartY = 0f

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->  {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                deltaX = abs(event.x - startX)
                deltaY = abs(event.y - startY)
                if (deltaX >deltaY) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }else{
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.dispatchTouchEvent(event)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker?.addMovement(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller!!.isFinished) {
                    scroller!!.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                touchDeltaX = event.x - touchX
                touchDeltaY = event.y - touchY
                Log.e("abs","===move"+event.x+"==="+touchX+"==="+touchDeltaX+"==="+scaleX)
                if(touchStartX<=0&&touchDeltaX>0){

                }else if(touchStartX>=measuredWidth/2&&touchDeltaX<0){

                }else{
                    scrollBy(-touchDeltaX.toInt(), 0)
                    touchStartX -= touchDeltaX
                }

            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                velocityTracker?.computeCurrentVelocity(1000)
                val dx: Int = measuredWidth - scrollX
                scroller!!.startScroll(scrollX, 0, dx, 0, 500)
                invalidate()
            }
        }
        event?.let {
            touchX = event.x
            touchY = event.y
        }

        return true
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

