package com.ma.lightweather.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager

class WeatherViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {


    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        if (v !== this) {
            if (v is HourFrogWeatherView) {
                return true
            }
        }
        return super.canScroll(v, checkV, dx, x, y)
    }

}