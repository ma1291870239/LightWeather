package com.ma.lightweather.old

import android.os.Bundle
import com.ma.lightweather.R
import com.ma.lightweather.activity.BaseActivity
import com.ma.lightweather.databinding.ActivityCityBinding

class CityActivity : BaseActivity<ActivityCityBinding>() {

    private val provinceArry= arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
    }
}
