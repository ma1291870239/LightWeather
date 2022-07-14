package com.ma.lightweather.fragment

import android.graphics.BitmapFactory
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log.e
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.appbar.AppBarLayout
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.FragFrogweatherBinding
import com.ma.lightweather.model.Air
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.*
import kotlinx.android.synthetic.main.frag_frogweather.*


class FrogWeatherFragment: BaseFragment<FragFrogweatherBinding>() {

    private var weatherList: List<Weather>? = null
    private var airList: List<Air>? = null

    private var locArea: String? = null
    private var weatherJson: String = ""
    private var weatherAqiJson: String = ""
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    private var city: String = "luoyang"
    private var cityCode: String = "101180901"
    private var isGetHeight: Boolean=false
    private var height: Int=0
    private var newHeight:Int=0
    private lateinit var  hfWeather: HFWeather


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

        }
        mBinding.appBarLayout.addOnOffsetChangedListener (AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            mBinding.swipeRefreshLayout.isEnabled = verticalOffset >=0
            var offset=1+verticalOffset.toFloat()/mBinding.hourweatherView.measuredHeight
            e(TAG, "getOffset: ${verticalOffset.toFloat()}---${mBinding.hourweatherView.measuredHeight}---${offset}")
            if(0<offset&&offset<1) {
                mBinding.weatherIvBottom.alpha = offset
            }
        })
        mBinding.swipeRefreshLayout.setOnRefreshListener { getNow() }
        mBinding.hourweatherView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isGetHeight
                &&mBinding.appBarLayout.measuredHeight!=0
                &&mBinding.hourweatherView.measuredHeight!=0) {
                LogUtils.e("getHeight: ${mBinding.appBarLayout.measuredHeight}---${mBinding.weatherIvBottom.measuredHeight}---${mBinding.hourweatherView.measuredHeight}")
                isGetHeight=true
                height = mBinding.appBarLayout.measuredHeight+1
                newHeight = mBinding.appBarLayout.measuredHeight + mBinding.hourweatherView.measuredHeight
                mBinding.appBarLayout.layoutParams.height = newHeight
                mBinding.collapsingToolbarLayout.layoutParams.height = newHeight
                mBinding.relativeLayout1.layoutParams.height = height
            }
        }

        hfWeather= HFWeather()
        val popManager=LinearLayoutManager(requireContext())
        val windManager=LinearLayoutManager(requireContext())
        popManager.orientation=LinearLayoutManager.HORIZONTAL
        windManager.orientation=LinearLayoutManager.HORIZONTAL
        mBinding.popRv.layoutManager=popManager
        mBinding.windRv.layoutManager=windManager

        setFragmentResultListener("city") { requestKey, bundle ->
            city = bundle.getString("city",city)
            cityCode = bundle.getString("cityCode",cityCode)
            getNow()
        }
    }

    fun getNow() {
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
        mBinding.swipeRefreshLayout.isRefreshing = false
        val dates = CommonUtils.changeTimeFormat(hfWeather.now.obsTime)
        mBinding.weatherTime.text =
            "${dates[1]}月${dates[2]}日 ${dates[3]}:${dates[4]}"
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
        mBinding.weatherDew.text = "${ hfWeather.now.dew}公里/小时"
        mBinding.weatherPres.text = "${ hfWeather.now.pressure}帕"
        mBinding.weatherCloud.text = "${ hfWeather.now.cloud}微克/立方米"
        mBinding.weatherVis.text = "${ hfWeather.now.vis}公里"


    }



}