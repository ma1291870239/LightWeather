package com.ma.lightweather.activity

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.ActivitySplashBinding
import com.ma.lightweather.utils.PhotoUtils
import com.ma.lightweather.utils.SPUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity<ActivitySplashBinding>(){

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        getBingImg()
        initView()
    }

    private fun getBingImg() {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, Contants.BINGURL,
            { response ->
                setData(response)
            },
            {
                Log.e(TAG, "getBingImg: error ${it.message}", )
            })
        requestQueue.add(stringRequest)
    }

    private fun initView() {
        mBinding.downloadIv.setOnClickListener{

        }
        mBinding.skipTv.setOnClickListener {
            countDownTimer.cancel()
            toMain()
        }

        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {
                mBinding.skipTv.text = getString(R.string.splash_skip_text,l/1000)
            }

            override fun onFinish() {
                toMain()
            }
        }
    }

    private fun setData(response:String) {
        countDownTimer.start()
        val bing=SPUtils.getParam(this, Contants.BING, "") as String
        val bingPath=SPUtils.getString(this, Contants.BINGPATH)
        if(response==bing && !bingPath.isNullOrEmpty()){
            Glide.with(this)
                .load(bingPath)
                .error(R.mipmap.splash)
                .into(mBinding.backIv)
        }else{
            SPUtils.setParam(this, Contants.BING, response)
            Glide.with(this)
                .load(response)
                .error(R.mipmap.splash)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e(TAG, "setData: error ${e?.message}", )
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val path=PhotoUtils.saveSplashImage(this@SplashActivity,(resource as BitmapDrawable).bitmap)
                        SPUtils.setParam(this@SplashActivity, Contants.BINGPATH, path)
                        return false
                    }

                })
                .into(mBinding.backIv)
        }

    }


    private fun toMain() {
        val oldVersion= SPUtils.getBoolean(this, Contants.OLDVERSION)
        var it= Intent(this, FrogActivity::class.java)
        if(oldVersion){
            it= Intent(this, MainActivity::class.java)
        }
        startActivity(it)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}
