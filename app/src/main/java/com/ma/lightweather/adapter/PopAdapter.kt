package com.ma.lightweather.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.CommonUtils

class PopAdapter(private val context: Context, private var popList: List<HFWeather.HFWeatherHour>) :RecyclerView.Adapter<PopAdapter.PopHolder>() {

    fun setData(popList: List<HFWeather.HFWeatherHour>){
        this.popList=popList
    }

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
            holder.popIv.setImageResource(0)
            holder.popTv.textSize=11f
            holder.popTv.setTextColor(ContextCompat.getColor(context, R.color.TextColorSecondary))
            holder.popTv.gravity=Gravity.CENTER_VERTICAL
            holder.popCloudTv.text = "降水量\n（毫米）"
            holder.popCloudTv.textSize=11f
            holder.popCloudTv.setTextColor(ContextCompat.getColor(context, R.color.TextColorSecondary))
            holder.popCloudTv.gravity=Gravity.CENTER_VERTICAL
            holder.popTimeTv.visibility=View.INVISIBLE
        }else {
            holder.popTv.text = popList[position-1].pop + "%"
            holder.popTv.gravity=Gravity.CENTER
            holder.popTv.textSize=13f
            holder.popTv.setTextColor(ContextCompat.getColor(context, R.color.TextColorPrimary))
            val precip=popList[position-1].precip.toDouble()
            if(precip == 0.0){
                holder.popCloudTv.text = "-"
                holder.popIv.setImageResource(R.mipmap.ic_droplet_clear)
            }else{
                holder.popCloudTv.text = "$precip"
                when((popList[position-1].precip.toFloat()).toInt()*24){
                    in 0 until 10->{holder.popIv.setImageResource(R.mipmap.ic_droplet_drizzle)}
                    in 10 until 25->{holder.popIv.setImageResource(R.mipmap.ic_droplet_light)}
                    in 25 until 100->{holder.popIv.setImageResource(R.mipmap.ic_droplet_moderate)}
                    in 100 until 2000->{holder.popIv.setImageResource(R.mipmap.ic_droplet_heavy)}
                }
            }
            holder.popCloudTv.gravity=Gravity.CENTER
            holder.popCloudTv.textSize=13f
            holder.popCloudTv.setTextColor(ContextCompat.getColor(context, R.color.wind_text))
            holder.popTimeTv.visibility=View.VISIBLE
            holder.popTimeTv.text = CommonUtils.dateTimeFormat(popList[position-1].fxTime,"HH时")
        }
    }

    inner class PopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val popTv: TextView = itemView.findViewById(R.id.item_pop)
        val popCloudTv: TextView = itemView.findViewById(R.id.item_pop_cloud)
        val popTimeTv: TextView = itemView.findViewById(R.id.item_pop_time)
        val popIv: ImageView = itemView.findViewById(R.id.item_pop_iv)
    }
}