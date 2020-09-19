package com.ma.lightweather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.SharedPrefencesUtils
import com.ma.lightweather.utils.WeatherUtils

class AboutActivity : BaseActivity() {

    private var versionTv: TextView? = null
    private var toolBar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initView()
        val oldVersion= SharedPrefencesUtils.getParam(this, Contants.VERSION, false) as Boolean
        if(oldVersion){
            versionTv?.text="旧版："+CommonUtils.getVersion(this)
        }else{
            val cond=SharedPrefencesUtils.getParam(this, Contants.TXT, "") as String
            var color= WeatherUtils.getColorWeatherTheme(cond)
            toolBar?.setBackgroundColor(ContextCompat.getColor(this,color))
            setStatusColor(color)
            versionTv?.text="新版："+CommonUtils.getVersion(this)
        }
    }

    private fun initView() {
        versionTv = findViewById(R.id.version)
        toolBar = findViewById(R.id.toolBar)
    }
}