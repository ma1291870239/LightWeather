package com.ma.lightweather.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide.init
import com.ma.lightweather.fragment.FrogWeatherFragment
import com.ma.lightweather.fragment.FutureDaysFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2



    override fun createFragment(position: Int): Fragment {
        lateinit var fragment: Fragment
        when(position){
            0->{
                fragment = FrogWeatherFragment()
                fragment.arguments = Bundle().apply {

                }
            }
            1->{
                fragment = FutureDaysFragment()
                fragment.arguments = Bundle().apply {

                }
            }
        }
        return fragment
    }
}