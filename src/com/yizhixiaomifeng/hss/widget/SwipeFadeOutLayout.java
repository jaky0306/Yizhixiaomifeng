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
	 * �ж��Ƿ������ٻ���
	 */
	protected boolean isFling;
	/**
	 * 0-160 ��Ӧ alphaֵ�� 0-1
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
			 * �������ٻ���ʱ�Ļص�
			 */
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				isFling = true;
//				Log.e("", "------onFling::"+velocityX);
				if(velocityX>0){ // �������һ���
					close();
				}else if(velocityX<0){ // �������󻬶�
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
	 * ��������view����ã��ɻ�ȡ��view�������Ϣ��ps���߶ȡ���ȵȣ�
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private int firstLeft=0;
	@Override
	/**
	 * ����view���в��֣�ȷ����view��λ��
	 * changed  ��Ϊtrue ��˵�����ַ����˱仯
	 * l\t\r\b\  ��ָ��ǰviewgroup ���丸view�е�λ�� 
	 */
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//������viewλ��
		firstLeft=operateView.getLeft();
		operateView.layout(getWidth(), operateView.getTop(), operateView.getRight(), operateView.getBottom());
	}
	
	/**
	 * ����ʶ��Ĺ�����
	 */
	private GestureDetector detector;
	
	
	/**
	 * down �¼�ʱ��x����
	 */
	private int firstX = 0;

	/**
	 * down �¼�ʱ��y����
	 */
	private int firstY=0;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		//�������������������view��Ӧ�¼�
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				/**
				 * �����ƽ����ܻ�ȡ��down�¼��������һ�λ���ʱ������bug
				 * 
				 * ����view��������down�¼���ʱ��
				 * SwipeFadeOutLayout��Ȼ������Ӧ�����¼�
				 */
				detector.onTouchEvent(event);
				
				firstX = (int) event.getX();
				firstY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				/**
				 * ������view���������¼�
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
		
		
		//����Լ����¼�����
		
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
				//�����¼������ø�view����
				requestDisallowInterceptTouchEvent(true);
			}
			if(dx>0&&operateView.getAlpha()>0){
				/**
				 * �����ƶ��Ҳ��Ǵ�����ȫ͸��״̬ʱ,ʹ����View͸����
				 */
				swipe(defaultFadeWidth-Math.abs(dx));
			}else if(dx<=0&&operateView.getAlpha()<1){
				/**
				 * �����ƶ��Ҳ��Ǵ�����ȫ��ʾʱ��ʹ����View����ʾ
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
			if(!isFling){//  ��û�з������ٻ�����ʱ�򣬲�ִ��
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
	 * Ϊ�������ṩ������ͼ�Ĳ���
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
	 * �ƶ�ʱ�ı������ͼ��͸����
	 * 
	 * ����ΪdefaultFadeWidth�ı���ֵ��
	 * @param dx
	 */
	private void swipe(float dx){
		/**
		 * �黯����ʾ������ͼ
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
	 * Ϊ�������ṩ�ر���ͼ�Ĳ���
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
