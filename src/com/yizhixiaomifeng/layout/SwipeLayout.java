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
	 * ��xml�м����겼�֣�֪���м�����view�󱻵��ã�ps����û��û�в�����view)
	 * ��һ����Գ�ʼ����view�����ã�
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	/**
	 * ��������view����ã��ɻ�ȡ��view�������Ϣ��ps���߶ȡ���ȵȣ�
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
		 * topֵ�̶�Ϊ0
		 */
		int t=0;
		int r=0;
		/**
		 * ����bottomֵ�̶�Ϊ�������ĸ߶�
		 * ����ֱ�ӻ�ȡ
		 */
		int b=getHeight();
		/**
		 * ����ÿһ����view��λ�ã�
		 * ��view��left��rightֵ�ļ��㹫ʽ����
		 * left = ����֮ǰ����view�Ŀ�ȵ��ܺ�
		 * right = ����֮ǰ����view�Ŀ�ȵ��ܺ�   + ����Ŀ��
		 */
		for(int i=0;i<getChildCount();i++){
			//��ȡ��view
			child=getChildAt(i);
			/**
			 * ���㵱ǰ��view��rightֵ
			 * right = ����֮ǰ����view�Ŀ�ȵ��ܺ�   + ����Ŀ��
			 */
			r+=getChildAt(i).getMeasuredWidth();
			//������viewλ��
			child.layout(l, t, r, b);
			/**
			 * ���㵱ǰ��view��leftֵ
			 * left = ����֮ǰ����view�Ŀ�ȵ��ܺ�
			 */
			l+=getChildAt(i).getMeasuredWidth();
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		//��viewDragHelper���¼����нӹ�
		return viewDragHelper.shouldInterceptTouchEvent(ev);
	};
	/**
	 * ��һ�δ�����x������
	 */
	private int firstX=0;
	/**
	 * ��һ�δ�����Y������
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
				//�����¼������ø�iew����
				requestDisallowInterceptTouchEvent(true);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			break;
		}
		//��viewDragHelper���¼����нӹ�
		viewDragHelper.processTouchEvent(event);
		return true;
	};
	
	
	private Callback callback = new Callback(){

		/**
		 * ����true��ʾ��Ҫ����ǰchild�Ĵ����¼�
		 */
		@Override
		public boolean tryCaptureView(View child, int id) {
			return true;
		}
		
		/**
		 * �����Ҫ�������¼�����ô�ͻص����������tryCaptureView��View child, int id������trueʱ�ص���
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		};
		
		/**
		 * ���������Ӱ��������
		 * ���˽�����
		 * 
		 * ���Խ����view��Ӧ�ͼ��¼��Ͳ��ֵĻ����¼�û�г�ͻ
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {
			return getChildAt(getChildCount()-1).getWidth();
//			return 0;
		};
		
		/**
		 * ����view��x�᷽���ʵ�ʻ���
		 * 
		 * left:���´����leftֵ
		 * dx:�����ľ���
		 * ����ֵ��view�������ʾleftֵ
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			//��һ����view�����λ��
			int firstChildLeft=getChildAt(0).getLeft();
			//���һ����view���ұ�λ��
			int lastChildRight=getChildAt(getChildCount()-1).getRight();
			/**
			 * ��֤�������һ����view��߲��ܳ�������������߽�
			 */
			left = firstChildLeft+dx>0? 0:left;
			/**
			 * ��֤���������һ����view�ұ߲������벼�������ұ߽�
			 * left = left + (λ�����ľ���ֵ) = left + abs(���һ����view�ƶ����λ��-ϣ�������λ��)
			 */
			left = lastChildRight+dx<getWidth()? left+Math.abs((lastChildRight+dx)-getWidth()):left;
			return left;
		};
		
		/**
		 * view������Ļص�
		 * 
		 * left:ΪclampViewPositionHorizontal(View child, int left, int dx)�ķ���ֵ
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			View child=null;
			/**
			 * �ƶ�������view��ֻ�ƶ�x�ᣬ���������������
			 */
			for(int i=0;i<getChildCount();i++){
				child=getChildAt(i);
				//���ٴ���������view
				if(!changedView.equals(child))
				//������viewλ��
				child.layout(child.getLeft()+dx, child.getTop(), child.getRight()+dx, child.getBottom());
			}
			
			/**
			 * ��¼״̬�ĸı䣬���ص�������
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
		 * touchup�ǵĻص�
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
	 * Ϊ�������ṩ������ͼ�Ĳ���
	 */
	public void open(){
		/**
		 * �ƶ���һ����view
		 */
//		viewDragHelper.smoothSlideViewTo(getChildAt(0), -getChildAt(getChildCount()-1).getMeasuredWidth(), 0);
		/**
		 * �ƶ����һ����view
		 * left = (�������Ŀ��) - (���һ����view�Ŀ��)
		 */
		viewDragHelper.smoothSlideViewTo(getChildAt(getChildCount()-1), getWidth()-getChildAt(getChildCount()-1).getMeasuredWidth(), 0);
		//ִ�ж������������������ص�
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}
	
	/**
	 * Ϊ�������ṩ�ر���ͼ�Ĳ���
	 */
	public void close(){
		viewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0);
		//ִ�ж������������������ص�
		ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
	}
	
	/**
	 * ���ٴ򿪣�û��ִ�ж���
	 */
	public void fastOpen(){
		/**
		 * ���²�����view�������һ����view��ȫ��ʾ
		 */
	}
	/**
	 * ���ٹرգ�û��ִ�ж���
	 */
	public void fastClose(){
		/**
		 * ���²�����view������viewλ�ûָ�ԭװ
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
