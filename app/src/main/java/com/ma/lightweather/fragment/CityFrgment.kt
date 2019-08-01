package com.ma.lightweather.fragment


import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ma.lightweather.R
import com.ma.lightweather.adapter.CityAdapter
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.DbUtils
import java.util.*

/**
 * Created by Ma-PC on 2016/12/5.
 */
class CityFrgment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val weatherList = ArrayList<Weather>()
    private var cityAdapter: CityAdapter? = null
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
        if (cityAdapter == null&&activity!=null) {
            cityAdapter = CityAdapter(activity!!, weatherList)
            recyclerView?.adapter = cityAdapter
        } else {
            cityAdapter?.notifyDataSetChanged()
        }
    }

    private fun initView(view: View?) {
        recyclerView = view?.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setColorSchemeResources(CommonUtils.getBackColor(mContext))
        swipeRefreshLayout?.setOnRefreshListener { initData() }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = layoutManager
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_divider)!!)
        recyclerView?.addItemDecoration(divider)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
        }
    }

}
