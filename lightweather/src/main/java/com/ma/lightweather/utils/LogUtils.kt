package com.ma.lightweather.utils

import android.util.Log

open class LogUtils {

    var DEBUG=true
    var TAG="lightweather"


    open fun e(s : String){
        if (DEBUG) {
            Log.e(TAG,s)
        }
    }

    open fun d(s : String){
        if (DEBUG) {
            Log.d(TAG,s)
        }
    }


    open fun v(s : String){
        if (DEBUG) {
            Log.v(TAG,s)
        }
    }

    open fun i(s : String){
        if (DEBUG) {
            Log.i(TAG,s)
        }
    }


    open fun w(s : String){
        if (DEBUG) {
            Log.w(TAG,s)
        }
    }
}