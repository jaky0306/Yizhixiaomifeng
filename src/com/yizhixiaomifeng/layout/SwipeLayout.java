package com.yizhixiaomifeng.layout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout{

	public SwipeLayout(Context context) {
		super(context);
		init();
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private ViewDragHelper viewDragHelper;
	public void init(){
		viewDragHelper = ViewDragHelper.create(this, callback);
	}

	/**
	 * 从xml中加载完布局，知道有几个子view后被调用（ps：并没有没有测量子view)
	 * （一般可以初始化子view的引用）
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	/**
	 * 测量完子view后调用，可获取子view的相关信息（ps：高度、宽度等）
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		initChildLayout();
	}
	
	public void initChildLayout(){
		View child=null;
		int l=0;
		/**
		 * top值固定为0
		 */
		int t=0;
		int r=0;
		/**
		 * 由于bottom值固定为布局器的高度
		 * 所以直接获取
		 */
		int b=getHeight();
		/**
		 * 布局每一个子view的位置，
		 * 子view的left和right值的计算公式如下
		 * left = 在它之前的子view的宽度的总和
		 * right = 在它之前的子view的宽度的总和   + 自身的宽度
		 */
		for(int i=0;i<getChildCount();i++){
			//获取子view
			child=getChildAt(i);
			/**
			 * 计算当前子view的right值
			 * right = 在它之前的子view的宽度的总和   + 自身的宽度
			 */
			r+=getChildAt(i).getMeasuredWidth();
			//布局子view位置
			child.layout(l, t, r, b);
			/**
			 * 计算当前子view的left值
			 * left = 在它之前的子view的宽度的总和
			 */
			l+=getChildAt(i).getMeasuredWidth();
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		//让viewDragHelper对事件进行接管
		return viewDragHelper.shouldInterceptTouchEvent(ev);
	};
	/**
	 * 第一次触摸的x轴坐标
	 */
	private int firstX=0;
	/**
	 * 第一次触摸的Y轴坐标
	 */
	private int firstY=0;
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX=(int) event.getX();
			firstY=(int) event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			int dx=(int) event.getX()-firstX;
			int dy=(int) event.getY()-firstY;
			if(Math.abs(dx)>Math.abs(dy)){
				//拦截事件，不让父iew操作
				requestDisallowInterceptTouchEvent(true);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			break;
		}
		//让viewDragHelper对事件进行接管
		viewDragHelper.processTouchEvent(event);
		return true;
	};
	
	
	private Callback callback = new Callback(){

		/**
		 * 返回true表示需要处理当前child的触摸事件
		 */
		@Override
		public boolean tryCaptureView(View child, int id) {
			return true;
		}
		
		/**
		 * 如果需要处理触摸事件，那么就回调这个方法（tryCaptureView（View child, int id）返回true时回调）
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		};
		
		/**
		 * 这个方法不影响操作结果
		 * 不了解作用
		 * 
		 * 可以解决子view响应低级事件和布局的滑动事件没有冲突
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {
			return getChildAt(getChildCount()-1).getWidth();
//			return 0;
		};
		
		/**
		 * 控制view在x轴方向的实际滑动
		 * 
		 * left:最新触点的left值
		 * dx:滑动的距离
		 * 返回值是view的最后显示left值
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			//第一个子view的左边位置
			int firstChildLeft=getChildAt(0).getLeft();
			//最后一个子view的右边位置
			int lastChildRight=getChildAt(getChildCount()-1).getRight();
			/**
			 * 保证滑动后第一个子view左边不能超过布局器的左边界
			 */
			left = firstChildLeft+dx>0? 0:left;
			/**
			 * 保证滑动后最后一个子view右边不能脱离布局器的右边界
			 * left = left + (位置误差的绝对值) = left + abs(最后一个子view移动后的位置-希望到达的位置)
			 */
			left = lastChildRight+dx<getWidth()? left+Math.abs((lastChildRight+dx)-getWidth()):left;
			return left;
		};
		
		/**
		 * view滑动后的回调
		 * 
		 * left:为clampViewPositionHorizontal(View child, int left, int dx)的返回值
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			View child=null;
			/**
			 * 移动所有子view，只移动x轴，因此其他参数不变
			 */
			for(int i=0;i<getChildCount();i++){
				child=getChildAt(i);
				//不再处理触摸的子view
				if(!changedView.equals(child))
				//布局子view位置
				child.layout(child.getLeft()+dx, child.getTop(), child.getRight()+dx, child.getBottom());
			}
			
			/**
			 * 记录状态的改变，并回调监听器
			 */
			if(getChildAt(0).getLeft()==0 && mStatus!=SwipeStatus.ClOSE){
				mStatus=SwipeStatus.ClOSE;
				if(onSwipeStatusChangeListener!=null){
					onSwipeStatusChangeListener.onClose(SwipeLayout.this);
				}
			}else if(getChildAt(0).getLeft()==-getChildAt(getChildCount()-1).getMeasuredWidth() && mStatus!=SwipeStatus.OPEN){
				mStatus=SwipeStatus.OPEN;
				if(onSwipeStatusChangeListener!=null){
					onSwipeStatusChangeListener.onOpen(SwipeLayout.this);
				}
			}else{
				mStatus=SwipeStatus.SWIPING;
				if(onSwipeStatusChangeListener!=null){
					onSwipeStatusChangeListener.onSwipe(SwipeLayout.this);
				}
			}
		};
		
		/**
		 * touchup是的回调
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			if(getChildAt(0).getLeft()<-getChildAt(getChildCount()-1).getMeasuredWidth()/2){
				//open
				open();
			}else{
				//close
				close();
			}
		}
	};

	/**
	 * 为调用者提供开启视图的操作
	 */
	public void open(){
		/**
		 * 移动第一个子view
		 */
//		viewDragHelper.smoothSlideViewTo(getChildAt(0), -getChildAt(getChildCount()-1).getMeasuredWidth(), 0);
		/**
		 * 移动最后一个子view
		 * left = (布局器的宽度) - (最后一个子view的宽度)
		 */
		viewDragHelper.smoothSlideViewTo(getChildAt(getChildCount()-1), getWidth()-getChildAt(getChildCount()-1).getMeasuredWidth(), 0);
		//执行动画，开启滚动操作回调
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}
	
	/**
	 * 为调用者提供关闭视图的操作
	 */
	public void close(){
		viewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0);
		//执行动画，开启滚动操作回调
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}
	
	/**
	 * 快速打开，没有执行动画
	 */
	public void fastOpen(){
		/**
		 * 重新布局子view，让最后一个子view完全显示
		 */
	}
	/**
	 * 快速关闭，没有执行动画
	 */
	public void fastClose(){
		/**
		 * 重新布局子view，将子view位置恢复原装
		 */
		initChildLayout();
		mStatus=SwipeStatus.ClOSE;
		if(onSwipeStatusChangeListener!=null){
			onSwipeStatusChangeListener.onClose(SwipeLayout.this);
		}
	}
	
	@Override
	public void computeScroll() {
		if(viewDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
		}
	}

	private SwipeStatus mStatus;
	enum SwipeStatus{
		OPEN,SWIPING,ClOSE
	}
	
	private OnSwipeStatusChangeListener onSwipeStatusChangeListener;
	
	public OnSwipeStatusChangeListener getOnSwipeStatusChangeListener() {
		return onSwipeStatusChangeListener;
	}

	public void setOnSwipeStatusChangeListener(
			OnSwipeStatusChangeListener onSwipeStatusChangeListener) {
		this.onSwipeStatusChangeListener = onSwipeStatusChangeListener;
	}
	
	public interface OnSwipeStatusChangeListener{
		void onOpen(SwipeLayout layout);
		void onClose(SwipeLayout layout);
		void onSwipe(SwipeLayout layout);
	}

}
