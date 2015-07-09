package com.yizhixiaomifeng.hss.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeFadeOutLayout extends FrameLayout{


	/**
	 * 判断是否发生快速滑动
	 */
	protected boolean isFling;
	/**
	 * 0-160 对应 alpha值的 0-1
	 */
	private float defaultFadeWidth=110;
	
	protected View mainContent;
	private View hideContent;
	private View operateView;

	public SwipeFadeOutLayout(Context context) {
		super(context);
		initView();
	}
	
	public SwipeFadeOutLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		
		detector = new GestureDetector(getContext(), new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
			}
			
			@Override
			/**
			 * 发生快速滑动时的回调
			 */
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				isFling = true;
//				Log.e("", "------onFling::"+velocityX);
				if(velocityX>0){ // 快速向右滑动
					close();
				}else if(velocityX<0){ // 快速向左滑动
					open();
//					Log.e("", "----------------onFling::"+velocityX);
				}
				
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mainContent=getChildAt(0);
		hideContent=getChildAt(1);
		operateView=getChildAt(2);
		operateView.setAlpha(0);
	}
	/**
	 * 测量完子view后调用，可获取子view的相关信息（ps：高度、宽度等）
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private int firstLeft=0;
	@Override
	/**
	 * 对子view进行布局，确定子view的位置
	 * changed  若为true ，说明布局发生了变化
	 * l\t\r\b\  是指当前viewgroup 在其父view中的位置 
	 */
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//布局子view位置
		firstLeft=operateView.getLeft();
		operateView.layout(getWidth(), operateView.getTop(), operateView.getRight(), operateView.getBottom());
	}
	
	/**
	 * 手势识别的工具类
	 */
	private GestureDetector detector;
	
	
	/**
	 * down 事件时的x坐标
	 */
	private int firstX = 0;

	/**
	 * down 事件时的y坐标
	 */
	private int firstY=0;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		//如果发生滑动，则不让子view相应事件
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				/**
				 * 让手势解析能获取到down事件，决解第一次滑动时的跳动bug
				 * 
				 * 在子view可以消费down事件的时候，
				 * SwipeFadeOutLayout依然可以响应滑动事件
				 */
				detector.onTouchEvent(event);
				
				firstX = (int) event.getX();
				firstY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				/**
				 * 不让子view操作滑动事件
				 */
				return true;
			case MotionEvent.ACTION_UP:
				break;
		}
		return false;
	}
	private boolean isOutside=true;
	private boolean isMove=false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		//添加自己的事件解析
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			super.onTouchEvent(event);
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			detector.onTouchEvent(event);
			
			isMove=true;
			int dx=(int) event.getX()-firstX;
			int dy=(int) event.getY()-firstY;
			if(Math.abs(dx)>Math.abs(dy)){
				//拦截事件，不让父view操作
				requestDisallowInterceptTouchEvent(true);
			}
			if(dx>0&&operateView.getAlpha()>0){
				/**
				 * 向右移动且不是处于完全透明状态时,使操作View透明化
				 */
				swipe(defaultFadeWidth-Math.abs(dx));
			}else if(dx<=0&&operateView.getAlpha()<1){
				/**
				 * 向左移动且不是处于完全显示时，使操作View逐渐显示
				 */
				swipe(Math.abs(dx));
			}
			break;
		case MotionEvent.ACTION_UP:
			
			detector.onTouchEvent(event);
			if(!isMove&&!isFling){
				super.onTouchEvent(event);
			}
			
			isMove=false;
			if(!isFling){//  在没有发生快速滑动的时候，才执行
				if(operateView.getAlpha()>0.5){
					open();
//					Log.e("", "------onTouch");
				}else {
					close();
				}
			}
		
			isFling = false;
			
			break;
		}
		
		return true; 
	}
	

	/**
	 * 为调用者提供开启视图的操作
	 */
	public void open(){
		hideContent.setAlpha(0);
		operateView.setAlpha(1);
		if(isOutside){
			operateView.layout(firstLeft, operateView.getTop(), operateView.getRight(), operateView.getBottom());
			isOutside=false;
		}
		if((status==SwipeStatus.SWIPING&&preStatus!=SwipeStatus.OPEN)||status==SwipeStatus.ClOSE){
			preStatus=status;
			status=SwipeStatus.OPEN;
			if(onSwipeStatusChangeListener!=null){
				onSwipeStatusChangeListener.onOpen(this);
			}
		}
	}
	/**
	 * 移动时改变操作视图的透明度
	 * 
	 * 参数为defaultFadeWidth的比例值，
	 * @param dx
	 */
	private void swipe(float dx){
		/**
		 * 虚化或显示操作视图
		 */
		float alpha=0;
		if(isOutside){
			operateView.layout(firstLeft, operateView.getTop(), operateView.getRight(), operateView.getBottom());
			isOutside=false;
		}
		alpha=(float) dx/(defaultFadeWidth);
		alpha=alpha<0? 0:alpha;
		alpha=alpha>1? 1:alpha;
		operateView.setAlpha(alpha);
		hideContent.setAlpha((1-alpha));
		if(status!=SwipeStatus.SWIPING){
			preStatus=status;
			status=SwipeStatus.SWIPING;
			if(onSwipeStatusChangeListener!=null){
				onSwipeStatusChangeListener.onSwipe(this);
			}
		}
	}
	/**
	 * 为调用者提供关闭视图的操作
	 */
	public void close(){
		hideContent.setAlpha(1);
		operateView.setAlpha(0);
		if(!isOutside){
			operateView.layout(getWidth(), operateView.getTop(), operateView.getRight(), operateView.getBottom());
			isOutside=true;
		}
		if(status==SwipeStatus.OPEN||(status==SwipeStatus.SWIPING&&preStatus!=SwipeStatus.ClOSE)){
			preStatus=status;
			status=SwipeStatus.ClOSE;
			if(onSwipeStatusChangeListener!=null){
				onSwipeStatusChangeListener.onClose(this);
			}
		}
	}

	private SwipeStatus status=SwipeStatus.ClOSE;
	private SwipeStatus preStatus=SwipeStatus.ClOSE;
	public enum SwipeStatus{
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
	
	public SwipeStatus getStatus() {
		return status;
	}

	public interface OnSwipeStatusChangeListener{
		void onOpen(SwipeFadeOutLayout layout);
		void onClose(SwipeFadeOutLayout layout);
		void onSwipe(SwipeFadeOutLayout layout);
	}
}
