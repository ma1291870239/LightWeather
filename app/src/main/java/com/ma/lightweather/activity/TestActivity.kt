package com.ma.lightweather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ma.lightweather.R
import com.ma.lightweather.databinding.ActivitySplashBinding
import com.ma.lightweather.databinding.ActivityTestBinding

class TestActivity : BaseActivity<ActivityTestBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}