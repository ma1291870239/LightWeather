package com.ma.lightweather.fragment

import android.graphics.BitmapFactory
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log.e
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.appbar.AppBarLayout
import com.ma.lightweather.adapter.HourWeatherAdapter
import com.ma.lightweather.adapter.PopAdapter
import com.ma.lightweather.adapter.WindAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.behavior.AppBarLayoutFlingBehavior
import com.ma.lightweather.databinding.FragFrogweatherBinding
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.*
import kotlinx.android.synthetic.main.frag_frogweather.*


class FrogWeatherFragment: BaseFragment<FragFrogweatherBinding>() {

    private var city: String = "luoyang"
    private var cityCode: String = "101180901"
    private var isGetHeight: Boolean=false
    private var isHorWeatherShow: Boolean=false
    private var height: Int=0
    private var newHeight:Int=0
    private lateinit var hourWeatherAdapter: HourWeatherAdapter
    private lateinit var popAdapter: PopAdapter
    private lateinit var windAdapter: WindAdapter
    private lateinit var hfWeather: HFWeather
    private var offset:Int=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding= FragFrogweatherBinding.inflate(inflater,container,false)
        city=SPUtils.getParam(requireContext(), Contants.CITY, city) as String
        cityCode=SPUtils.getParam(requireContext(), Contants.CITYCODE, cityCode) as String
        initView()
        getNow()
        return mBinding.root
    }

    private fun initView() {
        mBinding.swipeRefreshLayout.setColorSchemeResources(WeatherUtils.getBackColor(mContext))
        mBinding.collapsingToolbarLayout.setOnClickListener {
            val behavior=(mBinding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayoutFlingBehavior
            if(0<=mBinding.relativeLayout1.top&&mBinding.relativeLayout1.top<=mBinding.hourWeatherRv.height) {
                if (behavior.isShow) {
                    val b = behavior.setAppbarLayoutOffset(0)
                    if (b) {
                        behavior.isShow = false
                        mBinding.relativeLayout1.offsetTopAndBottom(- mBinding.relativeLayout1.top)
                        mBinding.weatherIvBottom.alpha = 1f
                    }
                } else {
                    val b = behavior.setAppbarLayoutOffset(-mBinding.hourWeatherRv.height)
                    if (b) {
                        behavior.isShow = true
                        mBinding.relativeLayout1.offsetTopAndBottom(mBinding.hourWeatherRv.height - mBinding.relativeLayout1.top)
                        mBinding.weatherIvBottom.alpha = 0f
                    }
                }
            }
        }
        mBinding.collapsingToolbarLayout.expandedTitleGravity
        mBinding.appBarLayout.addOnOffsetChangedListener (AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            mBinding.swipeRefreshLayout.isEnabled = verticalOffset >=0
            offset=verticalOffset
            val ratio=1+verticalOffset.toFloat()/mBinding.hourWeatherRv.measuredHeight
            LogUtils.e(TAG, "getOffset: ${verticalOffset.toFloat()}---${mBinding.hourWeatherRv.measuredHeight}---${ratio}")
            if(0<ratio&&ratio<1) {
                mBinding.weatherIvBottom.alpha = ratio
            }
        })
        mBinding.swipeRefreshLayout.setOnRefreshListener { getNow() }
        mBinding.hourWeatherRv.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isGetHeight
                &&mBinding.appBarLayout.measuredHeight!=0
                &&mBinding.hourWeatherRv.measuredHeight!=0) {
                LogUtils.e("getHeight: ${mBinding.appBarLayout.measuredHeight}---${mBinding.weatherIvBottom.measuredHeight}---${mBinding.hourWeatherRv.measuredHeight}")
                isGetHeight=true
                height = mBinding.appBarLayout.measuredHeight+1
                newHeight = mBinding.appBarLayout.measuredHeight + mBinding.hourWeatherRv.measuredHeight
                setHourWeatherHide()
            }
        }

        hfWeather= HFWeather()
        val hourWeatherManager=LinearLayoutManager(requireContext())
        val popManager=LinearLayoutManager(requireContext())
        val windManager=LinearLayoutManager(requireContext())
        hourWeatherManager.orientation=LinearLayoutManager.HORIZONTAL
        popManager.orientation=LinearLayoutManager.HORIZONTAL
        windManager.orientation=LinearLayoutManager.HORIZONTAL
        mBinding.hourWeatherRv.layoutManager=hourWeatherManager
        mBinding.popRv.layoutManager=popManager
        mBinding.windRv.layoutManager=windManager
        context?.let {
            hourWeatherAdapter= HourWeatherAdapter(it,hfWeather.hourly)
            popAdapter= PopAdapter(it,hfWeather.hourly)
            windAdapter= WindAdapter(it,hfWeather.hourly)
            mBinding.hourWeatherRv.adapter=hourWeatherAdapter
            mBinding.popRv.adapter=popAdapter
            mBinding.windRv.adapter=windAdapter
        }


        setFragmentResultListener("city") { requestKey, bundle ->
            city = bundle.getString("city",city)
            cityCode = bundle.getString("cityCode",cityCode)
            getNow()
        }
    }

    private fun setHourWeatherShow(){
        val appBarLayoutParams=mBinding.appBarLayout.layoutParams
        appBarLayoutParams.height =height
        mBinding.appBarLayout.layoutParams=appBarLayoutParams
        mBinding.collapsingToolbarLayout.layoutParams.height = height
        mBinding.relativeLayout1.layoutParams.height = height
        mBinding.relativeLayout2.layoutParams.height = height
        mBinding.weatherIvBottom.alpha =0f
        isHorWeatherShow=true
    }

    private fun setHourWeatherHide(){
        val appBarLayoutParams=mBinding.appBarLayout.layoutParams
        appBarLayoutParams.height =newHeight
        mBinding.appBarLayout.layoutParams=appBarLayoutParams
        mBinding.collapsingToolbarLayout.layoutParams.height = newHeight
        mBinding.relativeLayout1.layoutParams.height = height
        mBinding.relativeLayout2.layoutParams.height = newHeight
        mBinding.weatherIvBottom.alpha =1f
        isHorWeatherShow=false
    }

    private fun getNow() {
        hfWeather.now= HFWeather.WeatherNow()
        hfWeather.hourly.clear()
        hfWeather.daily.clear()
        VolleyUtils.requestGetHFWearher(requireContext(),Contants.HF_WEATHER_NOW + cityCode,
            {hfWeatherBean,code,msg->
                if (hfWeatherBean?.now != null){
                    hfWeather.now=hfWeatherBean.now
                    getHour()
                }else{
                    setMsg(msg)
                }
            },
            {
                setMsg(it)
            })
    }


    private fun getHour(){
        VolleyUtils.requestGetHFWearher(requireContext(),Contants.HF_WEATHER_HOUR + cityCode,
            {hfWeatherBean,code,msg->
                if (hfWeatherBean!=null&&!hfWeatherBean.hourly.isNullOrEmpty()){
                    hfWeather.hourly=hfWeatherBean.hourly
                    getFuture()
                }else{
                    setMsg(msg)
                }
            },
            {
                setMsg(it)
            })
    }

    private fun getFuture(){
        VolleyUtils.requestGetHFWearher(requireContext(),Contants.HF_WEATHER_FUTURE + cityCode,
            {hfWeatherBean,code,msg->
                if (hfWeatherBean!=null&&!hfWeatherBean.daily.isNullOrEmpty()){
                    hfWeather.daily=hfWeatherBean.daily
                    setData()
                }else{
                    setMsg(msg)
                }
            },
            {
                setMsg(it)
            })
    }

    private fun setMsg(s:String?){
        activity?.runOnUiThread {
            CommonUtils.showShortSnackBar(swipeRefreshLayout, s)
        }
    }

    private fun setData(){
        setFragmentResult(
                "hfWeather",
                bundleOf("hfWeather" to hfWeather)
        )
        mBinding.swipeRefreshLayout.isRefreshing = false
        mBinding.weatherTime.text =CommonUtils.dateTimeFormat(hfWeather.now.obsTime,"MM月dd日 HH:mm")
        mBinding.weatherMaxmintmp.text =
            "白天气温：${ hfWeather.daily[0].tempMax}℃ · 夜晚气温：${ hfWeather.daily[0].tempMin}℃"
        mBinding.weatherTmp.text = hfWeather.now.temp
        mBinding.weatherFeel.text = "体感温度：" + hfWeather.now.feelsLike + "℃"
        val cond = hfWeather.now.text
        mBinding.weatherCond.text = cond

        val backgroundColor = ContextCompat.getColor(
            requireContext(),
            WeatherUtils.getColorWeatherBack(cond)
        )
        var themeColor = ContextCompat.getColor(
            requireContext(),
            WeatherUtils.getColorWeatherTheme(cond)
        )
        val img=WeatherUtils.getColorWeatherImg(cond)
        val vibrantSwatch = Palette
            .from(BitmapFactory.decodeResource(resources, img))
            .setRegion(0,0,10,10)
            .generate()
            .vibrantSwatch
        vibrantSwatch?.let {
            themeColor = vibrantSwatch.rgb
        }
        mBinding.collapsingToolbarLayout.setBackgroundColor(backgroundColor)
        mBinding.weatherIvTop.setImageResource(WeatherUtils.getColorWeatherIcon(cond))
        mBinding.weatherIvBottom.setImageResource(img)
        setFragmentResult(
            "backgroundColor",
            bundleOf("backgroundColor" to themeColor)
        )


        mBinding.weatherHum.text = "${ hfWeather.now.humidity}%"
        mBinding.weatherDew.text = "${ hfWeather.now.dew}摄氏度"
        mBinding.weatherPres.text = "${ hfWeather.now.pressure}百帕"
        mBinding.weatherCloud.text = "${ hfWeather.now.cloud}%"
        mBinding.weatherVis.text = "${ hfWeather.now.vis}公里"

        hourWeatherAdapter.setData(hfWeather.hourly)
        popAdapter.setData(hfWeather.hourly)
        windAdapter.setData(hfWeather.hourly)
        hourWeatherAdapter.notifyItemRangeChanged(0,hfWeather.hourly.size)
        popAdapter.notifyItemRangeChanged(0,hfWeather.hourly.size)
        windAdapter.notifyItemRangeChanged(0,hfWeather.hourly.size)
    }



}