package com.ma.lightweather.app

import android.app.Application

/**
 * Created by Aeolus on 2018/12/25.
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this//存储引用
    }

    companion object {

        var instance: MyApplication? = null
            private set
    }
}
