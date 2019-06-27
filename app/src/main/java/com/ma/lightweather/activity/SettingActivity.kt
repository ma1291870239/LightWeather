package com.ma.lightweather.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch

import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.app.WeatherService
import com.ma.lightweather.utils.SharedPrefencesUtils

class SettingActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {

    private var notifySwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        notifySwitch = findViewById(R.id.notifySwitch)
        notifySwitch!!.setOnCheckedChangeListener(this)
        notifySwitch!!.isChecked = SharedPrefencesUtils.getParam(this, Contants.NOTIFY, false) as Boolean
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.notifySwitch -> {
                SharedPrefencesUtils.setParam(this@SettingActivity, Contants.NOTIFY, isChecked)
                if (isChecked) {
                    val it = Intent(this@SettingActivity, WeatherService::class.java)
                    startService(it)
                } else {
                    val it  = Intent(this@SettingActivity, WeatherService::class.java)
                    stopService(it)
                }
            }
        }
    }
}
