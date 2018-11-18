package com.hit.zhou.scanmachine.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Scroller;

import com.hit.zhou.scanmachine.common.header.HeaderClass;
import com.hit.zhou.scanmachine.common.header.RefreshHeaderInterface;


public class EasyRefreshLayout extends ViewGroup implements NestedScrollingParent{

    public static final String TAG = "TryRefreshLayout";
    private static final int INVALID_POINTER = -1;

    private View mHeader;
    private RefreshHeaderInterface refreshHeaderInterface;
    private View mTarget;

    private Scroller mScroller;

    // 头部下拉程度，百分比
    private float mPercent = 0.0f;

    // 最小滑动距离
    private int mTouchSlop;
    private float mHeaderHeightPx = -1;

    // 活动手指id
    private int mActivePointId = -1;
    // 最大下拉距离
    private float mTotalDragDistancePx = -1;
    // 手指按下的位置
    private float mInitDownY;
    private float mInitMotionY;
    // 已经偏移的偏移量
    private float mTotalOffset;
    //  活跃手指的偏移量
    private float mOffset;


    // 刷新监控
    private EasyRefreshLayout.OnRefreshListener mOnRefreshListener;
    // 子View是否能够滚动的回调
    private EasyRefreshLayout.OnChildScrollUpCallback mChildScrollUpCallback;


    /* 这个变量是我看swiperefreshlayout源码有的一个变量，在onTouchEvent和onInterceptTouchEvent最先有个这样一个if
    *         if (this.mReturningToStart && action == 0) {
            this.mReturningToStart = false;
            }
    但我阅读其他部分，从来没有任何一个函数对mReturningToStart赋值过true，我所以我觉得这个if不会执行，就把他删了
    private boolean mReturningToStart;
    */

    // 是否正在刷新
    private boolean mRefreshing;
    // 是否正在下拉
    private boolean mIsBeingDragged;
    // 状态
    private State mState;
    private boolean mNestedScrollInProgress;
    private int mTotalUnconsumed;


    private NestedScrollingParentHelper mParentHelper;


    public void setOnRefreshListener(EasyRefreshLayout.OnRefreshListener listener){
        this.mOnRefreshListener = listener;

    }


    /*设置刷新状态函数。
     * refreshing和mRefreshing实际上只有两个可能
     * true和false执行if，即本来不在刷新态，通过函数调用进入刷新态，直接改变状态到刷新态
     * false和true执行else，本来在刷新态，置为非刷新态，就进入刷新完成状态
     *
     * */
    public void setRefreshing(boolean refreshing,boolean refreshResult){
        if (refreshing && !mRefreshing) {
            this.mRefreshing = true;
            mPercent = 1.0f;
            mState = State.LOADING;
            refreshHeaderInterface.refreshing();
            scrollTo(0,-(int)Math.min(mTotalDragDistancePx,mHeaderHeightPx));
        }
        else{
            if(this.mRefreshing != refreshing){
                if(mState == State.LOADING){
                    refreshHeaderInterface.showRefreshResult(refreshResult);
                    postDelayed(delayToScrollTopRunnable,2000);
                }
            }
        }

    }

    public void setRefreshing(boolean refreshing){
        if (refreshing && !mRefreshing) {
            this.mRefreshing = true;
            mPercent = 1.0f;
            mState = State.LOADING;
            refreshHeaderInterface.refreshing();
            scrollTo(0,-(int)Math.min(mTotalDragDistancePx,mHeaderHeightPx));
        }
        else{
            if(this.mRefreshing != refreshing){
                if(mState == State.LOADING){
                    mScroller.startScroll(0,getScrollY(),0,-getScrollY());
                    invalidate();
                    mState = State.OVERING_TO_RESET;
                }
            }
        }
    }


    private void enSureTarget(){
        if(mTarget == null) {
            for(int i = 0;i<getChildCount();i++) {
                View child = this.getChildAt(i);
                if(!child.equals( mHeader)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }



    public void setHeader(RefreshHeaderInterface refreshHeaderInterface){
        if(refreshHeaderInterface == null)
            return;
        View view = refreshHeaderInterface.sendHeaderView(this);
        if(view == null)
            return;
        this.refreshHeaderInterface = refreshHeaderInterface;


        if(view.getParent() != null)
            ((ViewGroup)view.getParent()).removeView(view);

        float scale = getContext().getResources().getDisplayMetrics().density;

        mHeaderHeightPx = refreshHeaderInterface.sendHeaderHeightDp() * scale + 0.5f ;
        mTotalDragDistancePx = refreshHeaderInterface.sendHeightDpWhenRefreshing() * scale + 0.5f;

        if(mHeader == null){
            mHeader = view;
            addView(mHeader);
        }
        else{
            removeView(mHeader);
            mHeader = view;
            addView(mHeader);
        }
    }



    public EasyRefreshLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        // 在这里可以该头部类型，也可以用setHeader在外部更换

        RefreshHeaderInterface headerClass = new HeaderClass.Builder(context).create();

        setHeader(headerClass);

        mRefreshing = false;
        mState = State.RESET;
        mScroller= new Scroller(context);
        mParentHelper = new NestedScrollingParentHelper(this);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(mTarget == null)
            enSureTarget();
        if(mTarget == null)
            return;

        mTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),MeasureSpec.EXACTLY));
        mHeader.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int)mHeaderHeightPx,MeasureSpec.EXACTLY));

    }

    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom) {
        Log.e(TAG,"OnLayout");
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if(getChildCount() == 0){
            return;
        }

        if(mTarget == null)
            enSureTarget();
        if(mTarget == null)
            return;

        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        mHeader.layout(0,-(int)mHeaderHeightPx,width,0);
        Log.e(TAG,"myHeader"+mHeader);
    }



    @Override
    public boolean onStartNestedScroll(@NonNull View child,@NonNull View target,int nestedScrollAxes){
        Log.e(TAG,"onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy,@NonNull int[] consumed){
        Log.e(TAG,"onNestedPreScroll");

        if((getScrollY() == 0 && dy > 0) || canChildScrollUp() || mRefreshing){
            Log.e(TAG,"dy"+dy);
            mTotalUnconsumed = 0;
            consumed[1] = 0;
            consumed[0] = dy;
        }
        else{
            mTotalUnconsumed -= dy;
            consumed[1] = dy;
            consumed[0] = 0;
            moveSpinner(mTotalUnconsumed);

        }
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child,@NonNull View target, int nestedScrollAxes){
        Log.e(TAG,"onNestedScrollAccepted");
        mParentHelper.onNestedScrollAccepted(child,target,nestedScrollAxes);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target){
        mParentHelper.onStopNestedScroll(target);
        finishSpinner();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        Log.e(TAG,"dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev){
        Log.e(TAG,"onInterceptTouchEvent");

        int action = ev.getActionMasked();


        // 如果isEnabled为false，或者子view能滚动，或者正在刷新，不拦截touch事件
        if (!this.isEnabled() /*|| this.mReturningToStart*/  || this.mRefreshing || mNestedScrollInProgress
                || canChildScrollUp()) {
            return false;
        }


        // 这个条件判断是因为我的refreshlayout不同于swiperefreshlayout，有一个complete状态


        int pointerIndex;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"I_ACTION_DOWN");

                mActivePointId = ev.getPointerId(0);
                mIsBeingDragged = false;
                mTotalOffset = 0;

                pointerIndex = ev.findPointerIndex(mActivePointId);

                if(pointerIndex < 0)
                    return false;

                mInitDownY = ev.getY(pointerIndex);

                break;
            case MotionEvent.ACTION_MOVE:

                Log.e(TAG,"I_ACTION_MOVE");
                if(mActivePointId == INVALID_POINTER)
                    return false;
                pointerIndex = ev.findPointerIndex(mActivePointId);

                if(pointerIndex < 0)
                    return false;

                float y = ev.getY(pointerIndex);

                startDragging(y);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG,"ACTION_POINTER_DOWN");
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    return false;
                }
                mActivePointId = ev.getPointerId(pointerIndex);
                mInitDownY = ev.getY(pointerIndex);

                Log.e(TAG,"pointer_Y"+ev.getY(pointerIndex));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"I_ACTION_POINTER_UP");
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"I_ACTION_UP");
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointId = INVALID_POINTER;
                break;

        }
        return this.mIsBeingDragged;
    }

    private void startDragging(float y){
        final float yDiff = y - mInitDownY;
        // 滑动距离要大于最小滑动距离，否则拦截事件
        //Log.e(TAG,"yDiff"+yDiff);
        if(yDiff > mTouchSlop && !mIsBeingDragged && mState == State.RESET){
            mInitMotionY = mInitDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }


    private  void onSecondaryPointerUp(MotionEvent ev){
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);

        if(pointerId == mActivePointId) {

            final int newPointerIndex = pointerIndex == 0 ?1:0;
            mActivePointId = ev.getPointerId(newPointerIndex);

            Log.e(TAG,"mActivePointId" + mActivePointId);

            mTotalOffset += mOffset;
            mInitDownY = mInitMotionY = ev.getY(newPointerIndex);

            Log.e(TAG,"mInitMotionY"+mInitMotionY);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev){
        Log.e(TAG,"onTouchEvent");
        int action = ev.getActionMasked();

        if (!this.isEnabled() /*|| this.mReturningToStart*/ || this.canChildScrollUp() || this.mRefreshing || mNestedScrollInProgress) {
            return false;
        }

        int pointerIndex;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"ACTION_DOWN");
                mActivePointId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                Log.e(TAG, "ACTION_MOVE");
                pointerIndex = ev.findPointerIndex(mActivePointId);
                Log.e(TAG,"mActivePointId"+mActivePointId);
                if (pointerIndex < 0)
                    return false;
                final float y = ev.getY(pointerIndex);

                startDragging(y);
                if (mIsBeingDragged) {
                    final float overscrollTop = ((y - mInitMotionY) + mTotalOffset);
                    mOffset = (y- mInitMotionY);
                    moveSpinner(overscrollTop);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                // 获取当前活跃手指的信息
                Log.e(TAG,"ACTION_POINTER_DOWN");
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    return false;
                }
                mActivePointId = ev.getPointerId(pointerIndex);


                mTotalOffset += mOffset;
                mOffset = 0;
                // 获取新手指的点击位置
                pointerIndex = ev.findPointerIndex(mActivePointId);
                mInitDownY = mInitMotionY = ev.getY(pointerIndex);

                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"ACTION_POINTER_UP");
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                Log.e(TAG, "ACTION_UP");
                pointerIndex = ev.findPointerIndex(mActivePointId);
                if (pointerIndex < 0)
                    return false;
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    finishSpinner();
                }
                mActivePointId = INVALID_POINTER;
                mTotalOffset = 0;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "ACTION_CANCEL");
                return false;

        }
        return true;
    }

    /*
     *  moveSpinner和finishSpinner可以看为状态机
     *  RESET状态到PULL的触发条件是ACTION_MOVE
     *  PULL到RESET状态的触发条件是手指up（偏移量不够）
     *  PULL状态到PULLFULL的触发条件是偏移量达到最大值
     *  PULLFULL状态到PULL状态的触发条件是手指上划偏移量减少
     *  PULLFULL状态到LOADING状态的触发条件是手指up
     *  LOADING到COMPLETE的触发条件是回调Onrefresh
     * */
    private void moveSpinner(float overscrollTop){
        //Log.e(TAG,Float.toString(overscrollTop));

        if(overscrollTop < 0){
            scrollTo(0,0);
            switch (mState){
                case PULL:
                case PULLFULL:
                    refreshHeaderInterface.reset();
            }
            mState = State.RESET;
            return;
        }


        float canDrag = Math.min(mHeaderHeightPx,mTotalDragDistancePx);
        mPercent = Math.min(overscrollTop / canDrag, 1.0f);


        switch (mState) {
            case RESET:
                mState = State.PULL;
            case PULL:
            case PULLFULL:
                if(overscrollTop < canDrag) {
                    refreshHeaderInterface.pull(mPercent);
                    scrollTo(0,-(int)overscrollTop);
                    mState = State.PULL;
                }
                else{
                    //refreshHeaderInterface.pull(mPercent);
                    refreshHeaderInterface.pullFull();
                    float dampingY = dampingFunc(overscrollTop, canDrag);
                    scrollTo(0, -(int)dampingY);
                    mState = State.PULLFULL;
                }
                break;
        }

    }

    private float dampingFunc(float overscrollTop, float canDrag) {
        float dampingY = mHeaderHeightPx;
        if(canDrag < mHeaderHeightPx){
            if(overscrollTop < (mTotalDragDistancePx + 0.25f*(mHeaderHeightPx - mTotalDragDistancePx))){
                dampingY = 0.8f*(overscrollTop - mTotalDragDistancePx) + mTotalDragDistancePx;
            }
            else if(overscrollTop < (mTotalDragDistancePx + 0.65f*(mHeaderHeightPx - mTotalDragDistancePx))){
                dampingY = (mTotalDragDistancePx + 0.2f*(mHeaderHeightPx - mTotalDragDistancePx))
                        + (overscrollTop - (mTotalDragDistancePx + 0.25f*(mHeaderHeightPx - mTotalDragDistancePx))) * 0.5f;
            }
            else if(overscrollTop < (mTotalDragDistancePx + 1.15f*(mHeaderHeightPx - mTotalDragDistancePx))){
                dampingY = (mTotalDragDistancePx + 0.4f*(mHeaderHeightPx - mTotalDragDistancePx))
                        + (overscrollTop - (mTotalDragDistancePx + 0.65f*(mHeaderHeightPx - mTotalDragDistancePx))) * 0.4f;
            }
            else if(overscrollTop < (mTotalDragDistancePx + 2.15f*(mHeaderHeightPx - mTotalDragDistancePx))){
                dampingY = (mTotalDragDistancePx + 0.6f*(mHeaderHeightPx - mTotalDragDistancePx))
                        + (overscrollTop - (mTotalDragDistancePx + 1.15f*(mHeaderHeightPx - mTotalDragDistancePx))) * 0.2f;
            }
            else if(overscrollTop < (mTotalDragDistancePx + 4.15f*(mHeaderHeightPx - mTotalDragDistancePx))){
                dampingY = (mTotalDragDistancePx + 0.8f*(mHeaderHeightPx - mTotalDragDistancePx))
                        + (overscrollTop - (mTotalDragDistancePx + 2.15f*(mHeaderHeightPx - mTotalDragDistancePx))) *0.1f;
            }
            else{
                dampingY = (mTotalDragDistancePx + 1.0f*(mHeaderHeightPx - mTotalDragDistancePx));
            }
        }
        return dampingY;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.e(TAG,"computeScroll"+mState);
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
        else{
            switch (mState){
                case OVERING_TO_RESET:
                    mRefreshing = false;
                    mState = State.RESET;
                    refreshHeaderInterface.reset();
                    break;
                case OVERING_TO_LOADING:
                    mState = State.LOADING;
                    refreshHeaderInterface.refreshing();
                    break;
            }
        }

    }


    private void finishSpinner(){
        switch (mState){
            case RESET:
                scrollTo(0,0);
                break;
            case PULL:
                mScroller.startScroll(0,getScrollY(),0,-getScrollY());
                invalidate();
                mState = State.OVERING_TO_RESET;
                break;
            case PULLFULL:
                mScroller.startScroll(0,getScrollY(),0,-getScrollY() - (int)Math.min(mHeaderHeightPx,mTotalDragDistancePx));
                invalidate();
                mState = State.OVERING_TO_LOADING;
                mRefreshing = true;
                if(mOnRefreshListener != null)
                    mOnRefreshListener.onRefresh();
                break;
        }
    }



    private Runnable delayToScrollTopRunnable = new Runnable() {
        @Override
        public void run() {
            mScroller.startScroll(0,getScrollY(),0,-getScrollY());
            invalidate();
            mState = State.OVERING_TO_RESET;
        }
    };



    public boolean canChildScrollUp() {
        if (this.mChildScrollUpCallback != null) {
            return this.mChildScrollUpCallback.canChildScrollUp(this, this.mTarget);
        } else {
            return this.mTarget instanceof ListView ? ListViewCompat.canScrollList((ListView)this.mTarget, -1) : this.mTarget.canScrollVertically(-1);
        }
    }

    // 子view可以告知refreshlayout它是否可以刷新

    public void setOnChildScrollUpCallback(@Nullable EasyRefreshLayout.OnChildScrollUpCallback callback) {
        this.mChildScrollUpCallback = callback;
    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(@NonNull EasyRefreshLayout var1, @Nullable View var2);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }



}

