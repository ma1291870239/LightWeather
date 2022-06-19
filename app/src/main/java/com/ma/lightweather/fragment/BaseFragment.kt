package com.ma.lightweather.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.ma.lightweather.R
import com.ma.lightweather.app.MyApplication

/**
 * Created by Ma-PC on 2016/12/6.
 */
open class BaseFragment<VB: ViewBinding> : Fragment() {

    val mContext=MyApplication.instance
    var viewBinding: VB?=null
    val mBinding get() =viewBinding!!
    lateinit var TAG:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_base, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG=javaClass.simpleName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding=null
    }

}
