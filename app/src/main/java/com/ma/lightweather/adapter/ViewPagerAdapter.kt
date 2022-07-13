package com.ma.lightweather.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ma.lightweather.fragment.FrogTodayFragment
import com.ma.lightweather.fragment.FrogWeatherFragment
import com.ma.lightweather.fragment.FutureDaysFragment

class ViewPagerAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int  = 2

    override fun getItem(position: Int): Fragment {
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

    override fun getPageTitle(position: Int): CharSequence {
        lateinit var title: String
        when(position){
            0->{
                title="今天"
            }
            1->{
                title="未来"
            }
        }
        return title
    }
}
