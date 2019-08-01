package com.ma.lightweather.utils

import android.content.Context

/**
 * Created by Aeolus on 2018/7/26.
 */

object SharedPrefencesUtils {


    private val FILE_NAME = "lightweather"


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    fun setParam(context: Context?, key: String, `object`: Any) {

        val type = `object`.javaClass.simpleName
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp?.edit()

        when (type) {
            "String" -> editor?.putString(key, `object` as String)
            "Integer" -> editor?.putInt(key, `object` as Int)
            "Boolean" -> editor?.putBoolean(key, `object` as Boolean)
            "Float" -> editor?.putFloat(key, `object` as Float)
            "Long" -> editor?.putLong(key, `object` as Long)
        }
        editor?.apply()
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    fun getParam(context: Context?, key: String, defaultObject: Any): Any? {
        val type = defaultObject.javaClass.simpleName
        val sp = context?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        return when (type) {
            "String" -> sp?.getString(key, defaultObject as String)
            "Integer" -> sp?.getInt(key, defaultObject as Int)
            "Boolean" -> sp?.getBoolean(key, defaultObject as Boolean)
            "Float" -> sp?.getFloat(key, defaultObject as Float)
            "Long" -> sp?.getLong(key, defaultObject as Long)
            else -> null
        }

    }


}
