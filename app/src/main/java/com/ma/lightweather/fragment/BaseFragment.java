package com.ma.lightweather.fragment;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma.lightweather.R;
import com.ma.lightweather.activity.MainActivity;
import com.ma.lightweather.app.MyApplication;

/**
 * Created by Ma-PC on 2016/12/6.
 */
public class BaseFragment extends Fragment {

    private View view;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_base,null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= MyApplication.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
