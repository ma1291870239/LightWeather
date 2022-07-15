package com.ma.lightweather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ma.lightweather.R
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.LogUtils

class SearchAdapter (private val context: Context, private var searchList: List<HFWeather.WeatherLocation>) :
    RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    private lateinit var onItemClickListener: (position: Int)->Unit

    fun setOnItemClickListener(onItemClickListener: (position: Int)->Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setData(searchList: List<HFWeather.WeatherLocation>){
        this.searchList=searchList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_searchlist, parent, false)
        return SearchHolder(view)
    }

    override fun getItemCount(): Int {
        LogUtils.e("sieze:  ${searchList.size}")
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.cityTv.text="${searchList[position].country} ${searchList[position].adm1} ${searchList[position].name}"
        holder.cityTv.setOnClickListener {
            onItemClickListener(position)
        }
    }

    inner class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityTv: TextView = itemView.findViewById(R.id.cityTv)
    }
}