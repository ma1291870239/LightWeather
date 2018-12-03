package com.ma.lightweather.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ma.lightweather.R;
import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.fragment.PhotoFragment;
import com.ma.lightweather.fragment.SearchFrgment;
import com.ma.lightweather.fragment.WeatherFragment;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.model.Weather;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private TextView weathertv,searchtv,phototv;
    private WeatherFragment weatherFrag;
    private SearchFrgment searchFrag;
    private PhotoFragment photoFrag;
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();
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
        searchFrag=new SearchFrgment();
        photoFrag=new PhotoFragment();
        fragmentList.add(weatherFrag);
        fragmentList.add(searchFrag);
        fragmentList.add(photoFrag);
        viewPager.setAdapter(new ViewAdapter(getSupportFragmentManager()));
    }

    private void initView() {
        weathertv=findViewById(R.id.main_weather);
        searchtv= findViewById(R.id.main_search);
        phototv= findViewById(R.id.main_photo);
        viewPager=findViewById(R.id.main_frag);
        weathertv.setOnClickListener(this);
        searchtv.setOnClickListener(this);
        phototv.setOnClickListener(this);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_weather:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_search:
                viewPager.setCurrentItem(1);
                break;
            case R.id.main_photo:
                viewPager.setCurrentItem(2);
                break;
        }
    }




    public void refresh(String city){
        viewPager.setCurrentItem(0);
        weatherFrag.loadData(city);
    }

    public void deleteCity(String city,List<Weather> weatherList,int position){
        dbHelper=new MydataBaseHelper(this,"Weather.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("weather","city = ?",new String[]{weatherList.get(position).city});
        weatherList.remove(position);
        searchFrag.selectdb(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionbar_refresh:
                String city=(String) SharedPrefencesUtils.getParam(MainActivity.this,Contants.CITY,"Surface Lumia");
                refresh(city);
                break;
        }
        return super.onContextItemSelected(item);
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
