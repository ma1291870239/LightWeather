package com.ma.lightweather.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ma.lightweather.R


/**
 * Created by Aeolus on 2018/4/13.
 */

class CardTextView(context: Context) : CardView(context) {

    var rootView:LinearLayout?=null
    var titleView:TextView?=null
    var textView:TextView?=null

    init {
        initView()
    }

    private fun initView() {
        val cardTextView=LayoutInflater.from(context).inflate(R.layout.item_cardtextview,null)
        rootView=cardTextView.findViewById<LinearLayout>(R.id.rootView)
        titleView=cardTextView.findViewById<TextView>(R.id.titleView)
        textView=cardTextView.findViewById<TextView>(R.id.textView)
    }

    fun setText(title:String,text:String) {
        titleView?.text=title
        textView?.text=text
    }

}
