package com.ma.lightweather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ma.lightweather.R


/**
 * Created by Aeolus on 2018/4/13.
 */

class CardTextView(context: Context,attrs: AttributeSet) : LinearLayout(context, attrs) {

    var titleView:TextView?=null
    var textView:TextView?=null

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.item_cardtextview,this)
        titleView=findViewById(R.id.titleView)
        textView=findViewById(R.id.textView)
    }

    fun setText(title:String,text:String) {
        titleView?.text=title
        textView?.text=text
    }

}
