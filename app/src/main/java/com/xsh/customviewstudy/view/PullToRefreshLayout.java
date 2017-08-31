package com.xsh.customviewstudy.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.xsh.customviewstudy.R;

public class PullToRefreshLayout extends LinearLayout {
	private final String TAG="PullToRefreshLayout";
	/**
	 * 下拉状态
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * 释放立即刷新状态
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * 正在刷新状态
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * 刷新完成或未刷新状态
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;


	/**
	 * 下拉头的View
	 */
	private View header;

	/**
	 * 刷新时显示的进度条
	 */
	private ProgressBar progressBar;

	/**
	 * 指示下拉和释放的文字描述
	 */
	private TextView description;

	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;


	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private int lastStatus = currentStatus;

	/**
	 * 用于处理事件分发的mDragger
	 */
	private ViewDragHelper mDragger;

	/**
	 * 需要去下拉刷新的ListView
	 */
	private AbsListView listView;

	private LinearLayout pullLayout;
	/**
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;

	/**
	 * 当前滑动的距离
	 */
	private int currentTop;

	/**
	 * 允许滑动的最大距
	 */
	private int maxDragHeight;
	/**
	 * 是否加载过
	 */
	private boolean loadOnce=false;

	private boolean isTouch=false;

	private PullToRefreshListener pullToRefreshListener;

	public PullToRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed && !loadOnce){
			maxDragHeight=getMeasuredHeight()/2;
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) pullLayout.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			loadOnce = true;
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mDragger.continueSettling(true)){
            invalidate();
        }
	}

	/**
	 * 设置刷新回调接口
	 * @param listener
	 */
	public void setPullToRefreshistener(PullToRefreshListener listener){
		this.pullToRefreshListener=listener;
	}

	/**
	 * 完成刷新
	 */
	public void finishPullToRefresh(){
		currentStatus=STATUS_REFRESH_FINISHED;
		mDragger.smoothSlideViewTo(pullLayout, 0, hideHeaderHeight);
        invalidate();
	}

	/**
	 * 初始化view
	 */
	private void init(){
		pullLayout=(LinearLayout) getChildAt(0);
		listView = (AbsListView) pullLayout.getChildAt(1);
		header = pullLayout.getChildAt(0);
		progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
		description = (TextView) header.findViewById(R.id.description);

		mDragger=ViewDragHelper.create(this, 0.7f, new ViewDragHelper.Callback() {
			/**
	         * 进行捕获拦截，那些View可以进行drag操作
	         * @param childView
	         * @param position
	         * @return  直接返回true，拦截所有的VIEW
	         */
			@Override
			public boolean tryCaptureView(View childView, int position) {
				Log.i(TAG, "tryCaptureView");
				isTouch=true;
				return childView==pullLayout && isTop() ;
			}

			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				Log.i(TAG, "clampViewPositionVertical: top="+top+"dy="+dy+"hideHeaderHeight="+hideHeaderHeight+"maxDragHeight="+maxDragHeight);
				if(top<=hideHeaderHeight){
					return hideHeaderHeight;
				}else if(top>maxDragHeight){
					return maxDragHeight;
				}
				return top;
			}
			 @Override
			public void onViewReleased(View releasedChild, float xvel,	float yvel) {
				 Log.i(TAG, "onViewReleased");
				super.onViewReleased(releasedChild, xvel, yvel);

				if (releasedChild == pullLayout){
					isTouch=false;
					if (currentStatus == STATUS_RELEASE_TO_REFRESH ) {
						// 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
						currentStatus = STATUS_REFRESHING;
						updateHeadView();
	                    mDragger.settleCapturedViewAt(0,0);
	                    invalidate();
	                    if(pullToRefreshListener!=null){
	                    	pullToRefreshListener.onRefresh();
	                    }
					}else if(currentStatus == STATUS_PULL_TO_REFRESH ){
	                    mDragger.settleCapturedViewAt(0,hideHeaderHeight);
	                    invalidate();
					}else if(currentStatus == STATUS_REFRESHING){
						mDragger.settleCapturedViewAt(0,0);
	                    invalidate();
					}

                }
			}

			 @Override
			public void onViewPositionChanged(View changedView, int left,
					int top, int dx, int dy) {
				super.onViewPositionChanged(changedView, left, top, dx, dy);
				Log.i(TAG, "onViewPositionChanged: top="+top + "  ,dy" + dy);
				currentTop=top;
				if (isTouch && currentStatus != STATUS_REFRESHING){
					if (currentTop <0) {
						currentStatus = STATUS_PULL_TO_REFRESH;
					} else {
						currentStatus = STATUS_RELEASE_TO_REFRESH;
					}
					updateHeadView();
					lastStatus=currentStatus;
				}
			}
			 @Override
			public int getViewVerticalDragRange(View child) {
				 return maxDragHeight;
			}

		});

	}

	private void updateHeadView(){
		if(lastStatus!=currentStatus){
			if (currentStatus == STATUS_PULL_TO_REFRESH) {
				description.setText(getResources().getString(R.string.pull_to_refresh));
				progressBar.setVisibility(View.GONE);
			} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
				description.setText(getResources().getString(R.string.release_to_refresh));
				progressBar.setVisibility(View.GONE);
			} else if (currentStatus == STATUS_REFRESHING) {
				description.setText(getResources().getString(R.string.refreshing));
				progressBar.setVisibility(View.VISIBLE);
			}
		}

	}
	private boolean isTop(){
		return listView.getFirstVisiblePosition() == 0
				&& getScrollY() <= listView.getMeasuredHeight();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragger.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragger.processTouchEvent(event);
		return true;
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public interface PullToRefreshListener {
		/**
		 * 下拉刷新时调用
		 */
		void onRefresh();

		/**
		 * 上拉加载更多的刷新
		 */
//		void onLoadMoreRefresh();

	}

}
