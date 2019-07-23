package com.ma.lightweather.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 * Created by Aeolus on 2018/12/25.
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this//存储引用
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {

        @JvmStatic lateinit var instance: MyApplication
            private set
    }
}
