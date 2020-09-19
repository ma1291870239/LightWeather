package com.ma.lightweather.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ma.lightweather.R
import com.ma.lightweather.activity.AboutActivity
import com.ma.lightweather.activity.MainActivity
import com.ma.lightweather.activity.SettingActivity
import com.ma.lightweather.app.Contants
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.DbUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.utils.WeatherUtils
import com.ma.lightweather.widget.ActionSheetDialog

class NavCityAdapter (private val context: Context, private val weatherList: List<Weather>?) : RecyclerView.Adapter<NavCityAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_nav_city, parent, false)
        return CityHolder(view)
    }

    override fun onBindViewHolder(holder: CityHolder, i: Int) {
        if(i< weatherList?.size!!) {
            holder.cityTv.text = weatherList!![i].basic.location
            holder.tmpTv.text = weatherList[i].now.tmp + "℃"
            val dates = CommonUtils.changeTimeFormat(weatherList!![i].update.loc)
            holder.dateTv.text = dates[1] + "/" + dates[2] + " " + dates[3] + ":" + dates[4]
//        val date1 =weatherList[i].update.loc.split(" |\\-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        if(date1.size>=3){
//            holder.dateTv.text=date1[date1.size-3]+"/"+date1[date1.size-2]+" "+date1[date1.size-1]
//        }else{
//            holder.dateTv.text=weatherList[i].update.loc
//        }
            holder.weatherIv.setImageResource(WeatherUtils.getWeatherIcon(weatherList[i].now.cond_txt))
            if (i == 0) {
                holder.cityTv.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.tmpTv.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.dateTv.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.weatherIv.setImageResource(0)
                holder.weatherLayout.setBackgroundColor(ContextCompat.getColor(context, WeatherUtils.getColorWeatherBack(weatherList[i].now.cond_txt)))
            }
            holder.weatherLayout.setOnClickListener {
                if (context is MainActivity) {
                    context.refresh(weatherList[i].basic.location, true)
                }
            }
            holder.weatherLayout.setOnLongClickListener {
                showActionSheet(holder, i)
                true
            }
        }
        else if(i==weatherList.size){
            holder.tmpTv.text = "设置"
            holder.weatherLayout.setOnClickListener {
                val it= Intent(context, SettingActivity::class.java)
                context.startActivity(it)
            }
        }else if(i==weatherList.size.plus(1)){
            holder.tmpTv.text = "评分"
            holder.weatherLayout.setOnClickListener {
                CommonUtils.goToMarket(context)
            }
        } else if(i==weatherList.size.plus(2)){
            holder.tmpTv.text = "关于"
            holder.weatherLayout.setOnClickListener {
                val it= Intent(context, AboutActivity::class.java)
                context.startActivity(it)
            }
        }
    }


    private fun showActionSheet(holder: CityHolder,i: Int) {
        ActionSheetDialog(context).builder().setCancelable(true).setCanceledOnTouchOutside(true)
                .setTitle("选择项目")
                .addSheetItem("删除", null, object : ActionSheetDialog.OnSheetItemClickListener {
                    override fun onClick(which: Int) {
                        val city1 = SharedPrefencesUtils.getParam(context, Contants.CITY, Contants.CITYNAME) as String
                        val city2 = weatherList?.get(i)?.basic?.location
                        if (weatherList?.size!! > 1) {
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
                }).setCanceledOnTouchOutside(true).show()
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getItemCount(): Int {
        return weatherList?.size?.plus(3) ?: 0
    }



    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityTv: TextView = itemView.findViewById(R.id.item_city)
        val tmpTv: TextView = itemView.findViewById(R.id.item_tmp)
        val dateTv: TextView = itemView.findViewById(R.id.item_date)
        val weatherLayout: RelativeLayout = itemView.findViewById(R.id.item_weather)
        val weatherIv: ImageView = itemView.findViewById(R.id.item_weathericon)

    }
}