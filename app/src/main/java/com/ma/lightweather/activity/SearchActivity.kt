package com.ma.lightweather.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.ma.lightweather.R
import com.ma.lightweather.databinding.ActivityFrogBinding
import com.ma.lightweather.databinding.ActivitySearchBinding
import com.ma.lightweather.widget.SearchView

class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        mBinding.searchView.setState(true)
        mBinding.searchView.setCursorState(true)
        mBinding.searchView.setOnLeftClickListener(object: SearchView.OnLeftClickListener{
            override fun onLeftClick(tag: Int) {
                if(tag==SearchView.LEFT_IV_ARROW){
                    supportFinishAfterTransition()
                }
            }
        })

        mBinding.searchView.setOnAccountClickListener(object : SearchView.OnAccountClickListener{
            override fun onAccountClick(tag: Int) {
                if(tag==SearchView.RIGHT_ACCOUNT_ACCOUNT){
                    mBinding.searchView.clearEtText()
                }
            }
        })

    }
}