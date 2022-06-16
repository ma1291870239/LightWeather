package com.ma.lightweather.utils

import android.content.Context

/**
 * Created by Aeolus on 2018/7/26.
 */

object SPUtils {


    private val FILE_NAME = "lightweather"


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param `object`
     */
    fun setParam(context: Context?, key: String, value: Any) {

        val type = value.javaClass.simpleName
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp?.edit()

        when (type) {
            "String" -> editor?.putString(key, value as String)
            "Integer" -> editor?.putInt(key, value as Int)
            "Boolean" -> editor?.putBoolean(key, value as Boolean)
            "Float" -> editor?.putFloat(key, value as Float)
            "Long" -> editor?.putLong(key, value as Long)
        }
        editor?.apply()
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param value
     * @return
     */
    fun getParam(context: Context?, key: String, value: Any): Any? {
        val type = value.javaClass.simpleName
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        return when (type) {
            "String" -> sp?.getString(key, value as String)
            "Integer" -> sp?.getInt(key, value as Int)
            "Boolean" -> sp?.getBoolean(key, value as Boolean)
            "Float" -> sp?.getFloat(key, value as Float)
            "Long" -> sp?.getLong(key, value as Long)
            else -> null
        }

    }

    fun getString(context: Context?, key: String,  value: String =""): String {
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp?.getString(key, value)?:value
    }

    fun getInt(context: Context?, key: String, value: Int=-1): Int {
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp?.getInt(key, value)?:value
    }

    fun getBoolean(context: Context?, key: String, value: Boolean=false): Boolean {
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp?.getBoolean(key, value)?:value
    }


}
