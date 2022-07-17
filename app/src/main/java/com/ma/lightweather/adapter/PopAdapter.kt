package com.ma.lightweather.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils

class PopAdapter(private val context: Context, private var popList: List<HFWeather.WeatherHour>) :RecyclerView.Adapter<PopAdapter.PopHolder>() {

    fun setData(popList: List<HFWeather.WeatherHour>){
        this.popList=popList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pop, parent, false)
        return PopHolder(view)
    }

    override fun getItemCount(): Int {
        return popList.size
    }

    override fun onBindViewHolder(holder: PopHolder, position: Int) {
        if(position==0){
            holder.popTv.text =  "降水概率"
            holder.popTv.gravity=Gravity.CENTER_VERTICAL
            holder.popCloudTv.text = "云量"
            holder.popCloudTv.gravity=Gravity.CENTER_VERTICAL
            holder.popIv.setImageResource(0)
        }else {
            holder.popTv.text = popList[position].pop + "%"
            when(popList[position].pop.toInt()){
                0->{ holder.popIv.setImageResource(R.mipmap.ic_droplet_clear)}
                in 1..25->{holder.popIv.setImageResource(R.mipmap.ic_droplet_drizzle)}
                in 26..50->{holder.popIv.setImageResource(R.mipmap.ic_droplet_light)}
                in 51..75->{holder.popIv.setImageResource(R.mipmap.ic_droplet_moderate)}
                in 76..100->{holder.popIv.setImageResource(R.mipmap.ic_droplet_heavy)}
            }
            holder.popTv.gravity=Gravity.CENTER
            holder.popCloudTv.text = popList[position].cloud + "%"
            holder.popCloudTv.gravity=Gravity.CENTER
            holder.popTimeTv.text = CommonUtils.dateTimeFormat(popList[position].fxTime,"HH时")
        }
    }

    inner class PopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val popTv: TextView = itemView.findViewById(R.id.item_pop)
        val popCloudTv: TextView = itemView.findViewById(R.id.item_pop_cloud)
        val popTimeTv: TextView = itemView.findViewById(R.id.item_pop_time)
        val popIv: ImageView = itemView.findViewById(R.id.item_pop_iv)
    }
}