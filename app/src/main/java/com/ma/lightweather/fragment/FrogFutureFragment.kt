package com.ma.lightweather.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.databinding.FragFrogfutureBinding
import com.ma.lightweather.databinding.FragFrogweatherBinding

import com.ma.lightweather.utils.Parse
import com.ma.lightweather.utils.SPUtils


class FrogFutureFragment : BaseFragment<FragFrogfutureBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding= FragFrogfutureBinding.inflate(inflater,container,false)
        return mBinding.root
    }
}