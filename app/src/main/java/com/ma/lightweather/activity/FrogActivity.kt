package com.ma.lightweather.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.ma.lightweather.adapter.ViewPager2Adapter
import com.ma.lightweather.adapter.ViewPagerAdapter
import com.ma.lightweather.databinding.ActivityFrogBinding
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.widget.WeatherSearchView

class FrogActivity : BaseActivity<ActivityFrogBinding>() {

    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFrogBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        viewPagerAdapter= ViewPagerAdapter(supportFragmentManager, mBinding.viewPager.height)
        mBinding.viewPager.adapter=viewPagerAdapter
        viewPager2Adapter= ViewPager2Adapter(this, mBinding.viewPager.measuredHeight)
//        mBinding.viewPager.adapter=viewPager2Adapter
//        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
//            when(position){
//                0->{
//                    tab.text="今天"
//                }
//                1->{
//                    tab.text="未来"
//                }
//            }
//        }.attach()

        mBinding.searchView.setOnLeftClickListener(object:WeatherSearchView.OnLeftClickListener{
            override fun onLeftClick(tag: Int) {
                mBinding.drawerLayout.openDrawer(Gravity.LEFT)
            }
        })

        mBinding.searchView.setOnAccountClickListener(object :WeatherSearchView.OnAccountClickListener{
            override fun onAccountClick() {
                mBinding.drawerLayout.openDrawer(Gravity.LEFT)
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.e(TAG, "onWindowFocusChanged: ${mBinding.viewPager.measuredHeight- CommonUtils.dp2px(this,40f)}", )
    }
}