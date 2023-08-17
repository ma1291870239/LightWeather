package com.ma.lightweather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.widget.FrogHourWeatherView
import java.util.*

class HourWeatherAdapter (private val context: Context, private var hourList: List<HFWeather.HFWeatherHour>): RecyclerView.Adapter<HourWeatherAdapter.HourWeatherHolder>() {

    private var tempList: MutableList<Int> = ArrayList()
    private var maxTemp=0
    private var minTemp=0
    private var space=0f

    fun setData(hourList: List<HFWeather.HFWeatherHour>){
        this.hourList=hourList
        for (i in hourList.indices ){
            tempList.add(hourList[i].temp.toInt())
        }
        maxTemp= Collections.max(tempList)
        minTemp= Collections.min(tempList)
        space=200f/(maxTemp-minTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):HourWeatherAdapter.HourWeatherHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hourweather, parent, false)
        return HourWeatherHolder(view)
    }

    override fun getItemCount(): Int {
        return hourList.size
    }

    override fun onBindViewHolder(holder: HourWeatherAdapter.HourWeatherHolder, position: Int) {
        holder.hourWeatherView.setData(tempList,position,true)
        holder.textTv.text=hourList[position].text
        holder.timeTv.text= CommonUtils.dateTimeFormat(hourList[position].fxTime,"HH时")
    }

    inner class HourWeatherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val hourWeatherView: FrogHourWeatherView = itemView.findViewById(R.id.item_hourWeatherView)
        val textTv: TextView = itemView.findViewById(R.id.item_text)
        val timeTv: TextView = itemView.findViewById(R.id.item_time)
    }
}