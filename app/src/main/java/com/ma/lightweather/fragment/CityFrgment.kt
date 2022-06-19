package com.ma.lightweather.fragment


import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ma.lightweather.R
import com.ma.lightweather.adapter.CityWeatherAdapter
import com.ma.lightweather.databinding.FragCityBinding
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.DbUtils
import com.ma.lightweather.utils.WeatherUtils
import java.util.*

/**
 * Created by Ma-PC on 2016/12/5.
 */
class CityFrgment : BaseFragment<FragCityBinding>() {

    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val weatherList = ArrayList<Weather>()
    private var cityWeatherAdapter: CityWeatherAdapter? = null
    private var city: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_city, null)
        if (isAdded) {
            initView(view)
            initData()
        }
        return view
    }

    fun initData() {
        city = ""
        weatherList.clear()
        weatherList.addAll(DbUtils.queryDb(mContext))
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout?.isRefreshing = false
        }
        if (cityWeatherAdapter == null&&activity!=null) {
            cityWeatherAdapter = CityWeatherAdapter(activity!!, weatherList)
            recyclerView?.adapter = cityWeatherAdapter
        } else {
            cityWeatherAdapter?.notifyDataSetChanged()
        }
    }

    private fun initView(view: View?) {
        recyclerView = view?.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setColorSchemeResources(WeatherUtils.getBackColor(mContext))
        swipeRefreshLayout?.setOnRefreshListener { initData() }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = layoutManager
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        recyclerView?.addItemDecoration(divider)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
        }
    }

}
