package com.ma.lightweather.activity

import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.ma.lightweather.R
import com.ma.lightweather.adapter.NavCityAdapter
import com.ma.lightweather.app.Contants
import com.ma.lightweather.databinding.ActivityMainBinding
import com.ma.lightweather.fragment.*
import com.ma.lightweather.model.Weather
import com.ma.lightweather.utils.CommonUtils
import com.ma.lightweather.utils.DbUtils
import com.ma.lightweather.utils.SPUtils
import com.ma.lightweather.utils.WeatherUtils
import com.ma.lightweather.widget.WeatherViewPager
import java.util.*
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>() {


    private var frogWeatherFrag: FrogWeatherFragment? = null
    private var futureDaysFrag: FutureDaysFragment? = null
    private var weatherFrag: WeatherFragment? = null
    private var cityFrag: CityFrgment? = null
    private var photoFrag: PhotoFragment? = null
    private var viewPager:WeatherViewPager? = null
    private var toolBar: Toolbar? = null
    private var searchView:SearchView?=null
    private var tabLayout: TabLayout? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var navHeaderLayout:RelativeLayout? = null
    private var navImgView: ImageView? = null
    private var navTextView: TextView? = null
    private var recyclerView: RecyclerView? = null
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()
    private val weatherList = ArrayList<Weather>()
    private var navCityAdapter:NavCityAdapter? = null
    private var clickTime: Long = 0
    private val backTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setSearch()
        val oldVersion= SPUtils.getParam(this, Contants.OLDVERSION, false) as Boolean
        if(oldVersion){
            initOldData()
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }else{
            initNewData()
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun recreate() {
        try {//避免重启太快 恢复
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in fragmentList) {
                fragmentTransaction.remove(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            CommonUtils.showShortSnackBar(tabLayout,"切换主题失败，请重试")
        }
        super.recreate()
    }

    private fun initOldData() {
        weatherFrag = WeatherFragment()
        cityFrag = CityFrgment()
        photoFrag = PhotoFragment()
        fragmentList.add(weatherFrag!!)
        fragmentList.add(cityFrag!!)
        fragmentList.add(photoFrag!!)
        titleList.add(getString(R.string.main_weather_text))
        titleList.add(getString(R.string.main_city_text))
        titleList.add(getString(R.string.main_setting_text))
        viewPager?.adapter = ViewAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initNewData() {
        frogWeatherFrag= FrogWeatherFragment()
        futureDaysFrag= FutureDaysFragment()
        fragmentList.add(frogWeatherFrag!!)
        fragmentList.add(futureDaysFrag!!)
        titleList.add(getString(R.string.main_weather_text))
        titleList.add(getString(R.string.main_future_text))
        viewPager?.adapter = ViewAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initView() {
        toolBar = findViewById(R.id.toolBar)
        toolBar?.inflateMenu(R.menu.toolbar_menu)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        navigationView=findViewById(R.id.navigation_view)
        drawerLayout=findViewById(R.id.drawerLayout)
        navHeaderLayout=navigationView?.getHeaderView(0)?.findViewById(R.id.nav_layout)
        navImgView=navigationView?.getHeaderView(0)?.findViewById(R.id.nav_iv)
        navTextView=navigationView?.getHeaderView(0)?.findViewById(R.id.nav_text)
        recyclerView=navigationView?.getHeaderView(0)?.findViewById(R.id.nav_recyclerView)
        viewPager?.offscreenPageLimit = 2
        viewPager?.currentItem = 0
        setSupportActionBar(toolBar)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = layoutManager
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView?.addItemDecoration(divider)
    }

    private fun setSearch(){
        searchView = findViewById(R.id.toolBarSearch)
        val et = searchView?.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        et?.textSize = 15f
        et?.hint = getString(R.string.main_search_text)
        et?.setHintTextColor(ContextCompat.getColor(this, R.color.hint_black_text))
        et?.setTextColor(ContextCompat.getColor(this, R.color.primary_black_text))
        et?.setBackgroundResource(R.drawable.bg_weather_sv_solid_grey)
        searchView?.onActionViewExpanded()
        searchView?.clearFocus()
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                refresh(query, true)
                searchView?.clearFocus()
                searchView?.setQuery("", false)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }


    fun refresh(city: String, isSkip: Boolean) {
        if (isSkip) {
            viewPager?.currentItem = 0
        }
        weatherFrag?.loadData(city)
        //frogWeatherFrag?.getNow(city)
    }

    fun refreshCity() {
        cityFrag?.initData()
        getNavCity()
    }

    fun setWeatherBack(cond:String) {
        var color= WeatherUtils.getColorWeatherTheme(cond)
        toolBar?.setBackgroundColor(ContextCompat.getColor(this,color))
        tabLayout?.setBackgroundColor(ContextCompat.getColor(this,color))
        navHeaderLayout?.setBackgroundColor(ContextCompat.getColor(this,WeatherUtils.getColorWeatherBack(cond)))
        navImgView?.setImageResource(WeatherUtils.getColorWeatherIcon(cond))
        navTextView?.text=cond

        if (Build.VERSION.SDK_INT >= 21) {
            val tDesc = ActivityManager.TaskDescription(getString(R.string.app_name),
                    BitmapFactory.decodeResource(resources, R.drawable.ic_app_launcher),
                    ContextCompat.getColor(this,color))
            setTaskDescription(tDesc)
        }
        setStatusColor(color)
    }

    fun getNavCity() {
        weatherList.clear()
        weatherList.addAll(DbUtils.queryDb(this))
        if (navCityAdapter == null) {
            navCityAdapter = NavCityAdapter(this, weatherList)
            recyclerView?.adapter = navCityAdapter
        } else {
            navCityAdapter?.notifyDataSetChanged()
        }
    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//        val item = menu.findItem(R.id.toolBarSearch)
//        val searchView = item.actionView as SearchView
//        val et = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
//        et.textSize = 15f
//        et.hint = getString(R.string.main_search_text)
//        et.setHintTextColor(ContextCompat.getColor(this, R.color.hint_black_text))
//        et.setTextColor(ContextCompat.getColor(this, R.color.primary_black_text))
//        et.setBackgroundResource(R.drawable.bg_weather_sv_solid_grey)
//        searchView.isIconified=false
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                refresh(query, true)
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    internal inner class ViewAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

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
        if (System.currentTimeMillis() - clickTime > backTime) {
            CommonUtils.showShortSnackBar(tabLayout, getString(R.string.main_exit_text))
            clickTime = System.currentTimeMillis()
        } else {
            this.finish()
            exitProcess(0)
        }
    }


    // 获取点击事件
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
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
