package com.ma.lightweather.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ma.lightweather.R
import com.ma.lightweather.adapter.SearchAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.ActivityFrogBinding
import com.ma.lightweather.databinding.ActivitySearchBinding
import com.ma.lightweather.model.HFWeather
import com.ma.lightweather.utils.LogUtils
import com.ma.lightweather.utils.VolleyUtils
import com.ma.lightweather.widget.SearchView

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private lateinit var  hfWeather: HFWeather


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        val rvManager= LinearLayoutManager(this)
        rvManager.orientation= LinearLayoutManager.VERTICAL
        mBinding.rv.layoutManager=rvManager
        mBinding.searchView.setState(true)
        mBinding.searchView.setCursorState(true)
        mBinding.searchView.setOnLeftClickListener{
            if(it==SearchView.LEFT_IV_ARROW){
                supportFinishAfterTransition()
            }
        }
        mBinding.searchView.setOnAccountClickListener {
            if(it==SearchView.RIGHT_ACCOUNT_ACCOUNT){
                mBinding.searchView.clearEtText()
            }
        }
        mBinding.searchView.addTextChangedListener {
            if (it.isNotEmpty()){
                LogUtils.e(it)
                getArea(it)
            }
        }

    }

    private fun getArea(city: String?) {
        hfWeather= HFWeather()
        VolleyUtils.requestGetHFWearher(this, Contants.HF_WEATHER_AREA + city,
            {hfWeatherBean,code,msg->
                if (hfWeatherBean!=null&&code!=null){
                    hfWeather.location=hfWeatherBean.location
                    setData()
                }else{

                }
            },
            {

            })
    }

    private fun setData(){
        mBinding.rv.adapter=SearchAdapter(this,hfWeather.location)
    }
}