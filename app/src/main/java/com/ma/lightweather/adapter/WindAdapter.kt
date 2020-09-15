package com.ma.lightweather.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ma.lightweather.R
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import java.util.*

class WindAdapter (private val context: Context, private val windList: List<Weather.HourlyWeather>): RecyclerView.Adapter<WindAdapter.WindHolder>() {

    private var speedList: MutableList<Int> = ArrayList()
    private var maxSpeed=0
    private var minSpeed=0
    private var xSpace=0f

    init {
        for (i in windList.indices ){
            speedList.add(windList[i].wind_spd.toInt())
        }
        maxSpeed=Collections.max(speedList)
        minSpeed=Collections.min(speedList)
        xSpace=20f/(maxSpeed-minSpeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wind, parent, false)
        return WindHolder(view)
    }

    override fun getItemCount(): Int {
        return windList.size
    }

    override fun onBindViewHolder(holder: WindHolder, position: Int) {
        holder.windDirTv.text = windList[position].wind_dir
        holder.windSpeedTv.text= windList[position].wind_spd
        val time = CommonUtils.getTimeFormat(windList[position].time)
        holder.windTimeTv.text = CommonUtils.change24To12(time[0])+ "时"
        val height=20+(windList[position].wind_spd.toInt()-minSpeed)*xSpace
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