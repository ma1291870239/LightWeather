package com.ma.lightweather.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Scroller

class ScrollRelativeLayout(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    var scroller:Scroller?=null

    init {
        init()
    }

    private fun init() {
        scroller= Scroller(context)
    }

    fun onScroll(dx: Int) {
        if (this.scrollX != 0) {
            scroller?.startScroll(this.scrollX, 0, dx, 0)
            invalidate()
        }
    }


    override fun computeScroll() {
        super.computeScroll()
        if (scroller?.computeScrollOffset()!!) {
            this.scrollTo(scroller?.currX!!, 0)
            invalidate()
        }
    }
}