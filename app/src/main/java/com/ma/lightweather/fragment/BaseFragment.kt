package com.ma.lightweather.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ma.lightweather.R
import com.ma.lightweather.app.MyApplication

/**
 * Created by Ma-PC on 2016/12/6.
 */
open class BaseFragment : Fragment() {

    val mContext=MyApplication.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_base, null)
    }

}
