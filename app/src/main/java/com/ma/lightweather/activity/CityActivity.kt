package com.ma.lightweather.activity

import android.os.Bundle
import com.ma.lightweather.R

class CityActivity : BaseActivity() {

    private val provinceArry= arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
    }
}
