package com.ma.lightweather.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class AppBarLayoutFlingBehavior extends AppBarLayout.Behavior {

    private static final String TAG = "AppBarLayoutFlingBehavior";
    private static final int TYPE_FLING = 1;
    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    private VelocityTracker velocityTracker;
    private boolean isShow=false;
    private boolean isDownSlide=false;
    private float firstMoveY;
    private boolean isFirst=false;


    public AppBarLayoutFlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        shouldBlockNestedScroll = isFlinging;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //手指触摸屏幕的时候停止fling事件
                stopAppbarLayoutFling(child);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull @NotNull CoordinatorLayout parent, @NonNull @NotNull AppBarLayout child, @NonNull @NotNull MotionEvent ev) {
        CollapsingToolbarLayout layout= (CollapsingToolbarLayout) child.getChildAt(0);
        RelativeLayout relativeLayout=(RelativeLayout) layout.getChildAt(0);
        int diff=layout.getChildAt(1).getHeight()-layout.getChildAt(0).getHeight();
        int offset=child.getTop();
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if(!isFirst) {
                    firstMoveY = ev.getY();
                    isFirst=true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isFirst=false;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                isFirst=false;
                isDownSlide=ev.getY()>firstMoveY;
                String s=isDownSlide?"下滑":"上滑";
                velocityTracker.computeCurrentVelocity(1000);
                float yvel = velocityTracker.getYVelocity();
                LogUtil.d(TAG, "onTouchEvent: yvel:" + yvel+"---diff:"+diff+"---offset:"+offset+s);
                if(isDownSlide&&-2000<yvel&&yvel<2000){//下滑
                    if(-diff*2/3<offset&&offset<0){
                        setShow(false);
                        //child.offsetTopAndBottom(-offset);
                        setAppbarLayoutOffset(0);
                        relativeLayout.offsetTopAndBottom(offset);
                        relativeLayout.getChildAt(relativeLayout.getChildCount()-1).setAlpha(1f);
                    }else if(-diff<offset&&offset<-diff*2/3){
                        setShow(true);
                        //child.offsetTopAndBottom(-diff-offset);
                        setAppbarLayoutOffset(-diff);
                        relativeLayout.offsetTopAndBottom(diff+offset);
                        relativeLayout.getChildAt(relativeLayout.getChildCount()-1).setAlpha(0f);
                    }
                    return true;
                }else if(!isDownSlide&&-2000<yvel&&yvel<2000){//上滑
                    if(-diff/3<offset&&offset<0){
                        setShow(false);
                        //child.offsetTopAndBottom(-offset);
                        setAppbarLayoutOffset(0);
                        relativeLayout.offsetTopAndBottom(offset);
                        relativeLayout.getChildAt(relativeLayout.getChildCount()-1).setAlpha(1f);
                    }else if(-diff<offset&&offset<-diff/3){
                        setShow(true);
                        //child.offsetTopAndBottom(-diff-offset);
                        setAppbarLayoutOffset(-diff);
                        relativeLayout.offsetTopAndBottom(diff+offset);
                        relativeLayout.getChildAt(relativeLayout.getChildCount()-1).setAlpha(0f);
                    }
                    return true;
                }
        }
        return super.onTouchEvent(parent, child, ev);
    }

    public boolean setAppbarLayoutOffset(int offset){
        LogUtil.d(TAG,"isshow="+isShow+"---offset:"+offset);
        boolean success=AppBarLayoutFlingBehavior.this.setTopAndBottomOffset(offset);
        return success;
    }

    public void setShow(boolean isShow){
       this.isShow=isShow;
    }

    public boolean isShow(){
       return isShow;
    }

    public boolean isDownSlide(){
        return isDownSlide;
    }
    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     *
     * @return Field
     * @throws NoSuchFieldException
     */
    private Field getFlingRunnableField() throws NoSuchFieldException {
        Class<?> superclass = this.getClass().getSuperclass();
        try {
            // support design 27及一下版本
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("mFlingRunnable");
            } else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // 可能是28及以上版本
            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("flingRunnable");
            } else {
                return null;
            }
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     *
     * @return Field
     * @throws NoSuchFieldException
     */
    private Field getScrollerField() throws NoSuchFieldException {
        Class<?> superclass = this.getClass().getSuperclass();
        try {
            // support design 27及一下版本
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("mScroller");
            } else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // 可能是28及以上版本
            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("scroller");
            } else {
                return null;
            }
        }
    }

    /**
     * 停止appbarLayout的fling事件
     *
     * @param appBarLayout
     */
    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            Field flingRunnableField = getFlingRunnableField();
            Field scrollerField = getScrollerField();
            if (flingRunnableField != null) {
                flingRunnableField.setAccessible(true);
            }
            if (scrollerField != null) {
                scrollerField.setAccessible(true);
            }
            Runnable flingRunnable = null;
            if (flingRunnableField != null) {
                flingRunnable = (Runnable) flingRunnableField.get(this);
            }
            OverScroller overScroller = (OverScroller) scrollerField.get(this);
            if (flingRunnable != null) {
                LogUtil.d(TAG, "存在flingRunnable");
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
                                       View directTargetChild, View target,
                                       int nestedScrollAxes, int type) {
        LogUtil.d(TAG, "onStartNestedScroll");
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target,
                nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  AppBarLayout child, View target,
                                  int dx, int dy, int[] consumed, int type) {
        LogUtil.d(TAG, "onNestedPreScroll:" + child.getTotalScrollRange()
                + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type);
        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动
        //子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                               View target, int dxConsumed, int dyConsumed, int
                                       dxUnconsumed, int dyUnconsumed, int type) {
        LogUtil.d(TAG, "onNestedScroll: target:" + target.getClass() + " ,"
                + child.getTotalScrollRange() + " ,dxConsumed:"
                + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type);
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                    dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl,
                                   View target, int type) {
        LogUtil.d(TAG, "onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

    private static class LogUtil {
        static void d(String tag, String string) {
            Log.d(tag, string);
        }
    }
}
