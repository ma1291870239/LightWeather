package com.ma.lightweather.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ma.lightweather.R;


/**
 * Created by Francis on 09/12/2016.
 * <p>
 * 仲家源项目需要使用的searchView 貌似是一直用在toolbar上的
 * 只允许使用者自定义搜索框中的hint字体
 * 在布局文件中提供一个可选属性供使用者进行配置
 * hint 搜索框中提示字体
 * <p>
 * 获取搜索控件中字体 getSearchText()
 */

public class CustomerSearchTitle extends LinearLayout {


    public interface OnSearchListener {
        void onSearch(String keyWord);
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public interface OnLayoutClickListener {
        void onLayoutClick();
    }

    private EditText searchEditText;
    private ImageView clearImageView;
    private TextView cancelTextView;
    private LinearLayout searchLayout;
    private LinearLayout editLayout;

    private Context mContext;
    private String mSearchHint;
    private boolean hideCancle;
    private boolean isEnable;
    private OnSearchListener mOnSearchListener;
    private OnCancelListener mOnCancelListener;
    private OnLayoutClickListener mOnLayoutClickListener;
    private TextWatcher textWatcher;

    public CustomerSearchTitle(Context context) {
        this(context, null);
    }

    public CustomerSearchTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerSearchTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomerSearchTitle);
        mSearchHint = typedArray.getString(R.styleable.CustomerSearchTitle_hint);
        hideCancle=typedArray.getBoolean(R.styleable.CustomerSearchTitle_hidecancle,false);
        isEnable=typedArray.getBoolean(R.styleable.CustomerSearchTitle_isenable,true);
        typedArray.recycle();
    }

    /**
     * 设置标题文字
     *
     * @param searchHint
     */

    public void setSearchHint(String searchHint) {
        this.mSearchHint = searchHint;
        searchEditText.setHint(mSearchHint);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.mOnSearchListener = onSearchListener;
    }
    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void setOnLayoutClickListener(OnLayoutClickListener onLayoutClickListener) {
        this.mOnLayoutClickListener = onLayoutClickListener;
    }

    public void addTextChangeListener(TextWatcher textWatcher) {
        searchEditText.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onFinishInflate() throws ClassCastException {
        super.onFinishInflate();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_search_view, this);
        searchLayout= (LinearLayout) view.findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnLayoutClickListener!=null){
                    mOnLayoutClickListener.onLayoutClick();
                }
            }
        });
        editLayout= (LinearLayout) view.findViewById(R.id.editLayout);
        searchEditText = (EditText) view.findViewById(R.id.search_editText);
        searchEditText.setHint(mSearchHint);
        if(!isEnable){
           editLayout.setFocusable(true);
           editLayout.setFocusableInTouchMode(true);
        }

        searchEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnLayoutClickListener!=null){
                    mOnLayoutClickListener.onLayoutClick();
                }
            }
        });

        clearImageView = (ImageView) view.findViewById(R.id.clearImageView);
        clearImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                if (mOnCancelListener!=null){
                    mOnCancelListener.onCancel();
                }
            }
        });
        cancelTextView = (TextView) view.findViewById(R.id.cancel_textView);
        if(hideCancle){
            cancelTextView.setVisibility(GONE);
        }
        cancelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                } else {

                }
            }
        });
        searchEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mOnSearchListener != null) {
                        mOnSearchListener.onSearch(getSearchText());
                    }
                }
                return false;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clearImageView.setVisibility(INVISIBLE);
                    if (mOnCancelListener!=null){
                        mOnCancelListener.onCancel();
                    }
                } else {
                    clearImageView.setVisibility(VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /**
         * 防止使用者设置错误造成toolbar高度不一致或者显示异常，强制改变view的高度和宽度
         */
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen.search_height);
        if (mContext instanceof Activity) {
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            layoutParams.width = displayMetrics.widthPixels;
        }
        setLayoutParams(layoutParams);
    }

    /**
     * 获取搜索框中输入的文字
     */
    public String getSearchText() {
        return searchEditText.getText().toString();
    }

    /**
     * 设置搜索框中输入的文字
     */
    public void setSearchText(String searchText) {
        searchEditText.setText(searchText);
        searchEditText.setSelection(searchEditText.getText().length());
    }


    public void setTextAndSearch(String searchText) {
        searchEditText.setText(searchText);
        searchEditText.setSelection(searchEditText.getText().length());
        if (mOnSearchListener != null) {
            mOnSearchListener.onSearch(getSearchText());
        }
    }
}
