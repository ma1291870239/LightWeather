package com.ma.lightweather.activity

import android.os.Bundle

import com.ma.lightweather.R
import com.ma.lightweather.databinding.ActivityGuideBinding

class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
    }
}
