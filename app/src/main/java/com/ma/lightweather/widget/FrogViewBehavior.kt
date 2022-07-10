package com.ma.lightweather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FrogViewBehavior(context: Context,attributeSet: AttributeSet): CoordinatorLayout.Behavior<RelativeLayout>(context,attributeSet) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: RelativeLayout, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: RelativeLayout, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }
}