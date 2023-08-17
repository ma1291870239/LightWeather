package com.ma.lightweather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ma.lightweather.databinding.FragFrogfutureBinding


class FrogFutureFragment : BaseFragment<FragFrogfutureBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding= FragFrogfutureBinding.inflate(inflater,container,false)
        initView()
        getFuture()
        return mBinding.root
    }

    private fun initView() {
        mBinding.expandLv
    }

    private fun getFuture() {

    }
}