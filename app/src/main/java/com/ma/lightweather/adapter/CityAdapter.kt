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
import com.bumptech.glide.Glide
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
        holder.cityTv.text = weatherList!![i].basic.location
        holder.tmpTv.text = weatherList[i].now.tmp + "℃"
        holder.txtTv.text = weatherList[i].now.cond_txt
        holder.dirTv.text=weatherList[i].now.wind_dir
        holder.dateTv.text=weatherList[i].update.loc
        Glide.with(context).load(getWeatherIcon(weatherList[i].now.cond_txt)).into(holder.weatherIv)
        holder.weatherLayout.setOnClickListener {
            if (context is MainActivity) {
                context.refresh(weatherList[i].basic.location, true)
            }
        }
        holder.iv.setOnClickListener {
            val city1 = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
            val city2 = weatherList[i].basic.location
            if (weatherList.size > 1) {
                DbUtils.deleteDb(context, weatherList[i].basic.location)
                Snackbar.make(holder.weatherLayout,"删除成功", Snackbar.LENGTH_SHORT)
                        .setAction("撤销") {

                        }.show()
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

    private fun getWeatherIcon(condTxt:String):Int {
        if (condTxt?.contains("晴")!!) {
            return R.mipmap.sunny
        }
        if (condTxt.contains("云")) {
            return R.mipmap.cloudy
        }
        if (condTxt.contains("阴")) {
            return R.mipmap.shade
        }
        if (condTxt.contains("雨")) {
            return R.mipmap.rain
        }
        if (condTxt.contains("雪")) {
            return R.mipmap.snow
        }
        if (condTxt.contains("雾")) {
            return R.mipmap.smog
        }
        if (condTxt.contains("霾")) {
            return R.mipmap.smog
        }
        if (condTxt.contains("沙")) {
            return R.mipmap.sand
        }
        return R.mipmap.sunny
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityTv: TextView = itemView.findViewById(R.id.item_city)
        val tmpTv: TextView = itemView.findViewById(R.id.item_tmp)
        val txtTv: TextView = itemView.findViewById(R.id.item_txt)
        val dirTv: TextView = itemView.findViewById(R.id.item_dir)
        val dateTv: TextView = itemView.findViewById(R.id.item_date)
        val weatherLayout: RelativeLayout = itemView.findViewById(R.id.item_weather)
        val iv: ImageView = itemView.findViewById(R.id.item_delete)
        val weatherIv: ImageView = itemView.findViewById(R.id.item_weathericon)

    }
}
