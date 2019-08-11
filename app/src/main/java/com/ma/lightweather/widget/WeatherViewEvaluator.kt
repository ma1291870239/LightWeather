package com.ma.lightweather.widget

import android.animation.TypeEvaluator
import android.graphics.PointF



class WeatherViewEvaluator(private val point1 :PointF, private val point2 :PointF,private val type:Int) : TypeEvaluator<PointF> {

    override fun evaluate(t: Float, point0: PointF, point3: PointF): PointF {
        val point = PointF()

        return point
    }


}