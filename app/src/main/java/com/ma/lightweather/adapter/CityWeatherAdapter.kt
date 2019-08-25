package com.ma.lightweather.adapter


import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
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
class CityWeatherAdapter(private val context: Context, private val weatherList: List<Weather>?) : RecyclerView.Adapter<CityWeatherAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
        return CityHolder(view)
    }

    override fun onBindViewHolder(holder: CityHolder, i: Int) {
        holder.cityTv.text = weatherList!![i].basic.location
        holder.tmpTv.text = weatherList[i].now.tmp + "℃"
        holder.txtTv.text = weatherList[i].now.cond_txt
        holder.dirTv.text=weatherList[i].now.wind_dir
        holder.scTv.text=weatherList[i].now.wind_sc+"级"
        val date1 =weatherList[i].update.loc.split(" |\\-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if(date1.size>=3){
            holder.dateTv.text=date1[date1.size-3]+"/"+date1[date1.size-2]+" "+date1[date1.size-1]
        }else{
            holder.dateTv.text=weatherList[i].update.loc
        }
        holder.qltyTv.text=weatherList[i].qlty
        holder.weatherIv.setImageResource(CommonUtils.getWeatherIcon(weatherList[i].now.cond_txt))
        holder.weatherLayout.setOnClickListener {
            if (context is MainActivity) {
                context.refresh(weatherList[i].basic.location, true)
            }
        }
        holder.iv.setOnClickListener {
            val city1 = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
            val city2 = weatherList[i].basic.location
            if (weatherList.size > 1) {
                Snackbar.make(holder.weatherLayout,"删除成功", Snackbar.LENGTH_SHORT)
//                        .setAction("撤销") {
//
//                        }
                        .show()
                DbUtils.deleteDb(context, weatherList[i].basic.location)
                if (context is MainActivity) {
                    context.refreshCity()
                    if (city1 == city2) {
                        context.refresh(weatherList[0].basic.location, false)
                    }
                }
            } else {
                CommonUtils.showShortSnackBar(holder.weatherLayout, "至少保留一个城市")
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
        val dirTv: TextView = itemView.findViewById(R.id.item_dir)
        val scTv: TextView = itemView.findViewById(R.id.item_sc)
        val dateTv: TextView = itemView.findViewById(R.id.item_date)
        val qltyTv: TextView = itemView.findViewById(R.id.item_qlty)
        val weatherLayout: RelativeLayout = itemView.findViewById(R.id.item_weather)
        val iv: ImageView = itemView.findViewById(R.id.item_delete)
        val weatherIv: ImageView = itemView.findViewById(R.id.item_weathericon)

    }
}
