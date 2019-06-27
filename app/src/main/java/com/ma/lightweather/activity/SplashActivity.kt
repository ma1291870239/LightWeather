package com.ma.lightweather.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.ma.lightweather.R
import com.ma.lightweather.app.Contants
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.PhotoUtils

import java.util.concurrent.ExecutionException

class SplashActivity : BaseActivity(), View.OnClickListener {

    private var backIv: ImageView? = null
    private var downloadIv: ImageView? = null
    private var skipTv: TextView? = null
    private var countDownTimer: CountDownTimer? = null
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                DOWNLOAD_CODE -> CommonUtils.showShortToast(this@SplashActivity, "保存成功")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        initView()
        checkPermission()
    }

    private fun initView() {
        backIv = findViewById(R.id.backIv)
        downloadIv = findViewById(R.id.downloadIv)
        skipTv = findViewById(R.id.skipTv)
        downloadIv!!.setOnClickListener(this)
        skipTv!!.setOnClickListener(this)
    }

    private fun initData() {
        Glide.with(this).load(Contants.BINGURL).into(backIv!!)
        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(l: Long) {
                skipTv!!.text = "跳过" + l / 1000 + "秒"
            }

            override fun onFinish() {
                toMain()
            }
        }
        countDownTimer!!.start()
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_CODE_WRITE)
        } else {
            initData()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.downloadIv -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    CommonUtils.showShortToast(this, "当前没有读写权限")
                    return
                }
                Thread(Runnable {
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = Glide.with(this@SplashActivity)
                                .load(Contants.BINGURL)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                    val isSave = PhotoUtils.saveImageToGallery(this@SplashActivity, bitmap!!)
                    if (isSave) {
                        handler.sendEmptyMessage(DOWNLOAD_CODE)
                    }
                }).start()
            }
            R.id.skipTv -> {
                countDownTimer!!.cancel()
                toMain()
            }
        }
    }

    fun toMain() {
        val it: Intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(it)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer!!.cancel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE_WRITE -> {
                initData()
                return
            }
            else -> {
                initData()
                return
            }
        }
    }

    companion object {
        private val DOWNLOAD_CODE = 200
        private val PERMISSION_CODE_WRITE = 1
    }
}
