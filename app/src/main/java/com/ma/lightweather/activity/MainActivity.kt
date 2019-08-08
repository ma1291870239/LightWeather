package com.ma.lightweather.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.KeyEvent
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ma.lightweather.R
import com.ma.lightweather.fragment.CityFrgment
import com.ma.lightweather.fragment.PhotoFragment
import com.ma.lightweather.fragment.WeatherFragment
import com.ma.lightweather.utils.CommonUtils
import java.util.*
import kotlin.system.exitProcess


class MainActivity : BaseActivity() {

    private var weatherFrag: WeatherFragment? = null
    private var cityFrag: CityFrgment? = null
    private var photoFrag: PhotoFragment? = null
    private var viewPager: ViewPager? = null
    private var toolBar: android.support.v7.widget.Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var floatButton: FloatingActionButton? = null
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()
    private var clickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    override fun recreate() {
        try {//避免重启太快 恢复
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in fragmentList) {
                fragmentTransaction.remove(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
        }

        super.recreate()
    }

    private fun initData() {
        weatherFrag = WeatherFragment()
        cityFrag = CityFrgment()
        photoFrag = PhotoFragment()
        fragmentList.add(weatherFrag!!)
        fragmentList.add(cityFrag!!)
        fragmentList.add(photoFrag!!)
        titleList.add("天气")
        titleList.add("城市")
        titleList.add("拍照")
        viewPager?.adapter = ViewAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
    }

    private fun initView() {
        toolBar = findViewById(R.id.toolBar)
        toolBar?.inflateMenu(R.menu.toolbar_menu)
        tabLayout = findViewById(R.id.tabLayout)
        floatButton=findViewById(R.id.floatbutton)
        viewPager = findViewById(R.id.viewPager)
        viewPager?.offscreenPageLimit = 2
        viewPager?.currentItem = 0
        setSupportActionBar(toolBar)
        floatButton?.setOnClickListener {
            val intent=Intent()

        }
    }


    fun refresh(city: String, isSkip: Boolean) {
        if (isSkip) {
            viewPager?.currentItem = 0
        }
        weatherFrag?.loadData(city)
    }

    fun refreshCity() {
        cityFrag?.initData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val item = menu.findItem(R.id.toolBarSearch)
        val searchView = item.actionView as SearchView
        val et = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        et.textSize = 14f
        et.hint = "请输入要查询的城市名字"
        et.setHintTextColor(ContextCompat.getColor(this, R.color.text))
        et.setTextColor(ContextCompat.getColor(this, R.color.text))
        et.setBackgroundResource(R.drawable.bg_search_round_grey)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                refresh(query, true)
                searchView.clearFocus()
                searchView.setQuery("", false)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    internal inner class ViewAdapter(fm: android.support.v4.app.FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            CommonUtils.showShortSnackBar(tabLayout, "再按一次退出程序")
            clickTime = System.currentTimeMillis()
        } else {
            this.finish()
            exitProcess(0)
        }
    }


    // 获取点击事件
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isHideInput(view, ev)) {
                hideSoftInput(view!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 判定是否需要隐藏
    private fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(ev.x > left
                    && ev.x < right
                    && ev.y > top
                    && ev.y < bottom)
        }
        return false
    }

    // 隐藏软键盘
    private fun hideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
