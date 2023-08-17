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
import java.util.*

class WindAdapter (private val context: Context, private var windList: List<HFWeather.HFWeatherHour>): RecyclerView.Adapter<WindAdapter.WindHolder>() {

    private var speedList: MutableList<Int> = ArrayList()
    private var maxSpeed=0
    private var minSpeed=0
    private var space=0f

    fun setData(windList: List<HFWeather.HFWeatherHour>){
        this.windList=windList

        for (i in windList.indices ){
            speedList.add(windList[i].windSpeed.toInt())
        }
        maxSpeed=Collections.max(speedList)
        minSpeed=Collections.min(speedList)
        space=20f/(maxSpeed-minSpeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wind, parent, false)
        return WindHolder(view)
    }

    override fun getItemCount(): Int {
        return windList.size
    }

    override fun onBindViewHolder(holder: WindHolder, position: Int) {
        holder.windDirTv.text = windList[position].windDir
        holder.windSpeedTv.text= windList[position].windSpeed
        holder.windTimeTv.text = CommonUtils.dateTimeFormat(windList[position].fxTime,"HH时")
        val height=20+(windList[position].windSpeed.toInt()-minSpeed)*space
        val lp=holder.windTv.layoutParams
        lp.height=CommonUtils.dp2px(context,height)
        holder.windTv.layoutParams=lp
    }

    inner class WindHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val windTv: TextView = itemView.findViewById(R.id.item_wind)
        val windDirTv: TextView = itemView.findViewById(R.id.item_wind_dir)
        val windSpeedTv: TextView = itemView.findViewById(R.id.item_wind_speed)
        val windTimeTv: TextView = itemView.findViewById(R.id.item_wind_time)
    }
}