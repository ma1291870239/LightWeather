package com.ma.lightweather.widget

import android.animation.LayoutTransition
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.ma.lightweather.R
import com.ma.lightweather.databinding.ItemSearchviewBinding
import com.ma.lightweather.utils.DbUtils
import com.ma.lightweather.widget.WeatherSearchView.Companion.LEFT_IV_ARROW
import com.ma.lightweather.widget.WeatherSearchView.Companion.LEFT_IV_MENU
import com.ma.lightweather.widget.WeatherSearchView.Companion.RIGHT_IV1_SEARCH
import com.ma.lightweather.widget.WeatherSearchView.Companion.RIGHT_IV2_MIC
import kotlinx.android.synthetic.main.item_searchview.view.*

class SearchView (context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private lateinit var onLeftClickListener: (tag:Int)->Unit
    private lateinit var onRight1ClickListener:(tag:Int)->Unit
    private lateinit var onRight2ClickListener:(tag:Int)->Unit
    private lateinit var onAccountClickListener:(tag:Int)->Unit
    private var onFocusChangeListener:((hasFocus: Boolean)->Unit)?=null
    private var addTextChangedListener: ((String) -> Unit)? =null

    private var mTransition: LayoutTransition = LayoutTransition()

    private var leftTag: Int = LEFT_IV_SEARCH
    private var right1Tag: Int = RIGHT_IV1_SEARCH
    private var right2Tag: Int =RIGHT_IV2_MIC
    private var rightAccountTag: Int = RIGHT_ACCOUNT_ACCOUNT

    private lateinit var mBinding:ItemSearchviewBinding
    private var searchState=false

    companion object {
        const val LEFT_IV_SEARCH = 0X001
        const val LEFT_IV_ARROW = 0X002

        const val RIGHT_IV1_SEARCH = 0X003

        const val RIGHT_IV2_MIC = 0X004
        const val RIGHT_IV2_CLOSE = 0X005

        const val RIGHT_ACCOUNT_ACCOUNT = 0X004
        const val RIGHT_ACCOUNT_CLOSE = 0X005
    }

    init {
        initView()
    }

    private fun initView() {
        mBinding=ItemSearchviewBinding.bind(inflate(context,R.layout.item_searchview,this))
        mBinding.searchLeftIv.setOnClickListener {
            onLeftClickListener(leftTag)
        }
        mBinding.searchRightAccount.setOnClickListener {
            onAccountClickListener(rightAccountTag)
        }
        mBinding.searchTextEt.setOnEditorActionListener { p0, p1, p2 ->
            if (p1== EditorInfo.IME_ACTION_SEARCH){


            }
            false
        }
        mBinding.searchTextEt.setOnFocusChangeListener{ _, hasFocus ->
            onFocusChangeListener?.invoke(hasFocus)
        }
        mBinding.searchTextEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addTextChangedListener?.invoke(p0.toString())
            }

        })
        mTransition.enableTransitionType(LayoutTransition.CHANGING)
        mTransition.setDuration(300)
        mTransition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(
                    transition: LayoutTransition?,
                    container: ViewGroup?,
                    view: View?,
                    transitionType: Int) {
                Log.e("abc","start")
                if (view is LinearLayout) {
//                    search_account_iv.visibility= View.VISIBLE
//                    search_menu_iv.setImageResource(R.drawable.ic_search_menu)
                }else{

                }
            }

            override fun endTransition(
                    transition: LayoutTransition?,
                    container: ViewGroup?,
                    view: View?,
                    transitionType: Int) {
                Log.e("abc","end")
                if (view is LinearLayout) {
//                    search_account_iv.visibility= View.GONE
//                    search_menu_iv.setImageResource(R.drawable.ic_search_arrowback)
                }else{

                }
            }
        })

        mBinding.searchLayout.layoutTransition=mTransition
    }

    fun setState(searchState:Boolean){
        this.searchState=searchState
        val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp2px(context,50f)
        )
        if (searchState) {
            lp.setMargins(0,  dp2px(context,8f), 0,  dp2px(context,8f))
            mBinding.searchLayout.layoutParams = lp
            mBinding.searchLeftIv.setImageResource(R.drawable.ic_search_arrowback)
            mBinding.searchRightAccount.setImageResource(R.drawable.ic_search_close)
            mBinding.searchLayout.setBackgroundResource(R.drawable.bg_search_layout_bottom_border)
            leftTag= LEFT_IV_ARROW

            mBinding.searchTextEt.requestFocus()
        } else {
            lp.setMargins(dp2px(context,16f),
                    dp2px(context,8f),
                    dp2px(context,16f),
                    dp2px(context,8f))
            mBinding.searchLayout.layoutParams = lp
            mBinding.searchLeftIv.setImageResource(R.drawable.ic_search_search)
            mBinding.searchRightAccount.setImageResource(R.drawable.ic_search_account)
            mBinding.searchLayout.setBackgroundResource(R.drawable.bg_search_layout_corner_border)
            leftTag= LEFT_IV_SEARCH

            mBinding.searchTextEt.setText("")
            mBinding.searchTextEt.clearFocus()
        }
    }

    fun clearEtText(){
        mBinding.searchTextEt.setText("")
    }

    fun clearEtFocus(){
        mBinding.searchTextEt.clearFocus()
    }

    fun setCursorState(cursorState:Boolean){
        mBinding.searchTextEt.isCursorVisible=cursorState
    }

    fun setBackColor(backgroundColor:Int){
        mBinding.rootView.setBackgroundColor(backgroundColor)
    }

    /**
     * dp转换成px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    fun setOnLeftClickListener(onLeftClickListener:(tag:Int)->Unit){
        this.onLeftClickListener=onLeftClickListener
    }

    fun setOnRight1ClickListener(onRight1ClickListener:(tag:Int)->Unit){
        this.onRight1ClickListener=onRight1ClickListener
    }

    fun setOnRight2ClickListener(onRight2ClickListener:(tag:Int)->Unit){
        this.onRight2ClickListener=onRight2ClickListener
    }

    fun setOnAccountClickListener(onAccountClickListener:(tag:Int)->Unit){
        this.onAccountClickListener=onAccountClickListener
    }

    fun setOnFocusChangeListener(onFocusChangeListener:(hasFocus: Boolean)->Unit){
        this.onFocusChangeListener=onFocusChangeListener
    }

    fun addTextChangedListener(addTextChangedListener:(s: String)->Unit){
        this.addTextChangedListener=addTextChangedListener
    }
}