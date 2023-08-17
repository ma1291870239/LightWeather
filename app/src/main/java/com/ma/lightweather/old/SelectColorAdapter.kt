package com.ma.lightweather.old

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_selectcolor_dialog, null)
            holder.txtTextView = view!!.findViewById(R.id.txtTextView)
            holder.colorTextView = view.findViewById(R.id.colorTextView)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.txtTextView!!.text = textList!![position]
        val gradientDrawable = holder.colorTextView!!.background as GradientDrawable
        gradientDrawable.setColor(ContextCompat.getColor(context,colorList[position]))
        return view
    }

    class ViewHolder {
        var txtTextView: TextView? = null
        var colorTextView: TextView? = null
    }
}
