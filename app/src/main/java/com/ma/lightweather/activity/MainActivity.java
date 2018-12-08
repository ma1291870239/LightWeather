package com.ma.lightweather.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ma.lightweather.R;
import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.fragment.PhotoFragment;
import com.ma.lightweather.fragment.CityFrgment;
import com.ma.lightweather.fragment.WeatherFragment;
import com.ma.lightweather.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private WeatherFragment weatherFrag;
    private CityFrgment cityFrag;
    private PhotoFragment photoFrag;
    private ViewPager viewPager;
    private android.support.v7.widget.Toolbar toolBar;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> titleList=new ArrayList<>();
    private MydataBaseHelper dbHelper;
    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        weatherFrag=new WeatherFragment();
        cityFrag=new CityFrgment();
        photoFrag=new PhotoFragment();
        fragmentList.add(weatherFrag);
        fragmentList.add(cityFrag);
        fragmentList.add(photoFrag);
        titleList.add("天气");
        titleList.add("城市");
        titleList.add("拍照");
        viewPager.setAdapter(new ViewAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        toolBar=findViewById(R.id.toolBar);
        toolBar.inflateMenu(R.menu.toolbar_menu);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        setSupportActionBar(toolBar);
    }


    public void refresh(String city,boolean isSkip){
        if(isSkip) {
            viewPager.setCurrentItem(0);
        }
        weatherFrag.loadData(city);
    }

    public void refreshCity(){
        cityFrag.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.toolBarSearch);
        final SearchView searchView = (SearchView) item.getActionView();
        SearchView.SearchAutoComplete et = searchView.findViewById(R.id.search_src_text);
        et.setTextSize(14);
        et.setHint("请输入要查询的城市名字");
        et.setHintTextColor(getResources().getColor(R.color.text));
        et.setTextColor(getResources().getColor(R.color.text));
        et.setBackgroundResource(R.drawable.bg_search_round_grey);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refresh(query,true);
                searchView.clearFocus();
                searchView.setQuery("",false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu) ;
    }

    class ViewAdapter extends FragmentPagerAdapter{
        public ViewAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            CommonUtils.showShortToast(this,"再按一次退出程序");
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }


    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if(isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if(v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            }else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if(token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
