package com.ma.lightweather.widget;

import android.content.Context;
import android.support.v7.widget.SearchView;


/**
 * Created by Aeolus on 2018/4/13.
 */

public class MySearchView extends SearchView {

    private Context context;
    public MySearchView(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    private void initView() {
    }
}
