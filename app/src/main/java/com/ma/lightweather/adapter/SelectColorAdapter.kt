package com.ma.lightweather.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.ma.lightweather.R

/**
 * Created by Aeolus on 2018/12/25.
 */

class SelectColorAdapter(private val context: Context, private val textList: List<String>?, private val colorList: List<Int>) : BaseAdapter() {

    override fun getCount(): Int {
        return textList!!.size
    }

    override fun getItem(position: Int): Any? {
        return if (position >= count || textList == null) {
            null
        } else textList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.item_selectcolor_dialog, null)
            holder.txtTextView = convertView!!.findViewById(R.id.txtTextView)
            holder.colorTextView = convertView.findViewById(R.id.colorTextView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.txtTextView!!.text = textList!![position]
        val gradientDrawable = holder.colorTextView!!.background as GradientDrawable
        gradientDrawable.setColor(ContextCompat.getColor(context,colorList[position]))
        return convertView
    }

    class ViewHolder {
        var txtTextView: TextView? = null
        var colorTextView: TextView? = null
    }
}
