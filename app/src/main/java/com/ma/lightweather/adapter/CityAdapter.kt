package com.ma.lightweather.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


import com.ma.lightweather.R
import com.ma.lightweather.activity.MainActivity
import com.ma.lightweather.app.Contants
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.DbUtils
import com.ma.lightweather.utils.SharedPrefencesUtils

/**
 * Created by Ma-PC on 2016/12/14.
 */
class CityAdapter(private val context: Context, private val weatherList: List<Weather>?) : RecyclerView.Adapter<CityAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
        return CityHolder(view)
    }

    override fun onBindViewHolder(holder: CityHolder, i: Int) {
        holder.cityTv.text = weatherList!![i].city
        holder.tmpTv.text = weatherList[i].tmp + "℃"
        holder.txtTv.text = weatherList[i].txt + " " + weatherList[i].dir
        holder.weatherLayout.setOnClickListener {
            if (context is MainActivity) {
                context.refresh(weatherList[i].city!!, true)
            }
        }
        holder.iv.setOnClickListener {
            val city1 = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
            val city2 = weatherList[i].city
            if (weatherList.size > 1) {
                weatherList[i].city?.let { it1 -> DbUtils.deleteCity(context, it1) }
                if (context is MainActivity) {
                    context.refreshCity()
                    if (city1 == city2) {
                        context.refresh(weatherList[0].city!!, false)
                    }
                }
            } else {
                CommonUtils.showShortToast(context, "至少保留一个城市")
            }
        }
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return weatherList?.size ?: 0
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityTv: TextView = itemView.findViewById(R.id.item_city)
        val tmpTv: TextView = itemView.findViewById(R.id.item_tmp)
        val txtTv: TextView = itemView.findViewById(R.id.item_txt)
        val weatherLayout: LinearLayout = itemView.findViewById(R.id.item_weather)
        val iv: ImageView = itemView.findViewById(R.id.item_delete)

    }
}
