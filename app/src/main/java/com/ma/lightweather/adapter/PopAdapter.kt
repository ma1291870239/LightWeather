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
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils

class PopAdapter(private val context: Context, private val popList: List<Weather.HourlyWeather>) :RecyclerView.Adapter<PopAdapter.PopHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pop, parent, false)
        return PopHolder(view)
    }

    override fun getItemCount(): Int {
        return popList.size+1
    }

    override fun onBindViewHolder(holder: PopHolder, position: Int) {
        if(position==0){
            holder.popTv.text =  "降水概率"
            holder.popTv.gravity=Gravity.CENTER_VERTICAL
            holder.popCloudTv.text = "云量"
            holder.popCloudTv.gravity=Gravity.CENTER_VERTICAL
            holder.popIv.setImageResource(0)
        }else {
            holder.popTv.text = popList[position-1].pop + "%"
            when(popList[position-1].pop.toInt()){
                0->{ holder.popIv.setImageResource(R.mipmap.ic_droplet_clear)}
                in 1..25->{holder.popIv.setImageResource(R.mipmap.ic_droplet_drizzle)}
                in 26..50->{holder.popIv.setImageResource(R.mipmap.ic_droplet_light)}
                in 51..75->{holder.popIv.setImageResource(R.mipmap.ic_droplet_moderate)}
                in 76..100->{holder.popIv.setImageResource(R.mipmap.ic_droplet_heavy)}
            }
            holder.popTv.gravity=Gravity.CENTER
            holder.popCloudTv.text = popList[position-1].cloud + "%"
            holder.popCloudTv.gravity=Gravity.CENTER
            val time = CommonUtils.getTimeFormat(popList[position-1].time)
            holder.popTimeTv.text = CommonUtils.change24To12(time[0])+ "时"
        }
    }

    inner class PopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val popTv: TextView = itemView.findViewById(R.id.item_pop)
        val popCloudTv: TextView = itemView.findViewById(R.id.item_pop_cloud)
        val popTimeTv: TextView = itemView.findViewById(R.id.item_pop_time)
        val popIv: ImageView = itemView.findViewById(R.id.item_pop_iv)
    }
}