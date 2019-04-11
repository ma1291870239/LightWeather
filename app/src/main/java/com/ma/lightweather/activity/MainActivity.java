package com.ma.lightweather.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ma.lightweather.R;
import com.ma.lightweather.app.Contants;
import com.ma.lightweather.app.WeatherService;
import com.ma.lightweather.db.MydataBaseHelper;
import com.ma.lightweather.fragment.PhotoFragment;
import com.ma.lightweather.fragment.CityFrgment;
import com.ma.lightweather.fragment.WeatherFragment;
import com.ma.lightweather.model.BaiduLocation;
import com.ma.lightweather.utils.CommonUtils;
import com.ma.lightweather.utils.SharedPrefencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private WeatherFragment weatherFrag;
    private CityFrgment cityFrag;
    private PhotoFragment photoFrag;
    private ViewPager viewPager;
    private android.support.v7.widget.Toolbar toolBar;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> titleList=new ArrayList<>();
    private long clickTime = 0;
    public final static int CHANGETHEME=100;
    public final static int GETADDRESS_CODE=100;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location bdlocation;
    private String address;
    private static final int PERMISSION_CODE_LOCATION = 1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETADDRESS_CODE:
                    if(!(boolean)SharedPrefencesUtils.getParam(MainActivity.this,Contants.FIRSTSHOW,false)){
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        dialogBuilder.setTitle("提示");
                        dialogBuilder.setMessage("你当前所在位置是"+address+",需要切换地区吗");
                        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refresh(bdlocation.getLongitude()+","+bdlocation.getLatitude(),false);
                                dialog.dismiss();
                            }
                        });
                        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogBuilder.create().show();
                        SharedPrefencesUtils.setParam(MainActivity.this,Contants.FIRSTSHOW,true);
                    }else{
                        refresh(bdlocation.getLongitude()+","+bdlocation.getLatitude(),false);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestLocationPermission();
        initData();

    }

    @Override
    public void recreate() {
        try {//避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : fragmentList) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
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

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("权限请求");
                dialogBuilder.setMessage("我们希望获取您的位置信息，给您的书房安个家");
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_LOCATION);
                    }
                });
                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_LOCATION);
            }
        }
    }


    private void startLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                bdlocation=location;
                getDistrictFromLocation();
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                    locationListener = null;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        for (String s : locationManager.getAllProviders()) {
            if (s.equals(LocationManager.NETWORK_PROVIDER)) {

            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);

    }


    private void getDistrictFromLocation() {
        if (bdlocation == null) {
            return;
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, Contants.BAIDUGETADDRESS+bdlocation.getLatitude()+","+bdlocation.getLongitude() ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson=new Gson();
                        BaiduLocation baiduLocation=gson.fromJson(response,BaiduLocation.class);
                        address=baiduLocation.getResult().getAddressComponent().getDistrict();
                        handler.sendEmptyMessage(GETADDRESS_CODE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==CHANGETHEME){
            recreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE_LOCATION:
                startLocation();
                return;
        }
    }
}
