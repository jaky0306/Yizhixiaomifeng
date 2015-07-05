package com.yizhixiaomifeng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.HeadTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {
	
	private ImageView head ;
	private ImageView show_menu_layout;
	private TextView showTime;
	private TextView showDate;
	private String timeString;
	private String dateString;
	private Button signin;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				showTime.setText(msg.obj.toString());
			}
			if(msg.what==0x112){
				showDate.setText(msg.obj.toString());
			}
		};
	};
	/**
	 * ���Ʋ໬����
	 */
	
	private ListView showMenuItems ;
	private int exit=0;  //	������¼click �Ĵ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActivityCloser.activities.add(this);
		
		/**
		 * �л��˵�
		 */
		show_menu_layout=(ImageView)findViewById(R.id.menu_layout_icon);
		show_menu_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isMenuVisible)
	        	{
	        		scrollToContent();
	        	}
	        	else {
	        		scrollToMenu();
				}
			}
		});
		
		/**
		 * ��ʼ��ͷ��
		 */
		head = (ImageView)findViewById(R.id.staff_head);
		Bitmap bm = null;
		if ((bm = HeadTool.haveHead()) != null) {
			//head.setImageBitmap(bm);
			//Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.bt);
		      Bitmap output=HeadTool.toRoundBitmap(bm);
		      head.setImageBitmap(output);
		}
		
		initValues();
		content.setOnTouchListener(this);
		
		/**
		 * ��¼�¼�
		 */
		signin=(Button)findViewById(R.id.singin);
		signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Login.class);
				startActivity(intent);
			}
		});
		
		/**
		 * ��ʾʱ��
		 */
		showTime=(TextView)findViewById(R.id.show_time);
		showDate=(TextView)findViewById(R.id.show_date);
		final String[] weekDays = {"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
					int week=c.get(Calendar.DAY_OF_WEEK)-1;
					if(week<0) week=0;
					int year = c.get(Calendar.YEAR); 
					int month = c.get(Calendar.MONTH)+1; 
					int date = c.get(Calendar.DATE); 
					int hour = c.get(Calendar.HOUR_OF_DAY); 
					int minute = c.get(Calendar.MINUTE);
					timeString=(hour<10?"0"+hour:""+hour)+":"+(minute<10?"0"+minute:""+minute);
					dateString=year+"-"+month+"-"+date+"  "+weekDays[week];
					Message ts = new Message();
					ts.what=0x111;
					ts.obj=timeString;
					handler.sendMessage(ts);
					Message ds = new Message();
					ds.what=0x112;
					ds.obj=dateString;
					handler.sendMessage(ds);
	                try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		
		/*********************��ʼ��MenuLayout************************/
		
		
		showMenuItems=(ListView)findViewById(R.id.menu_item_showMenuItems);
		SimpleAdapter adapter = new SimpleAdapter(this,getMenuData(),R.layout.menuitems,
				new String[]{"title"},
				new int[]{R.id.menuitems_title});
		showMenuItems.setAdapter(adapter);
		showMenuItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				switch (position) {
				case 0:
					
					break;

				case 1:
					
					break;
				case 2:
					
					break;
				case 3:
					
					break;
				case 4:
					Intent intent = new Intent(MainActivity.this, SettingCenter.class);
					startActivity(intent);
					break;
				default:
					break;
				}
				Toast.makeText(getApplicationContext(),""+position, Toast.LENGTH_LONG).show();
			}
		});
	}

	public List<Map<String,Object>> getMenuData()
	{
		List<Map<String, Object>> menuItemData=new ArrayList<Map<String,Object>>();
		
		String[] data={"������Ϣ","������Ϣ","������Ϣ","������Ϣ","������ҳ"};
		for(int i=0;i<data.length;i++)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("title", data[i]);
			menuItemData.add(map);
		}
		return menuItemData;
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit++ ;
            if(exit==2)
            {
                exit = 0 ;
                ActivityCloser.exitAllActivities(this);
              
            }
            else
            {
                Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        exit = 0 ;
                    }
                }, 3000);
                return true ;
            }
            
        }
        else if (keyCode==KeyEvent.KEYCODE_MENU)
        {
        	if(isMenuVisible)
        	{
        		scrollToContent();
        	}
        	else {
        		scrollToMenu();
			}
        	
        }
        return super.onKeyDown(keyCode, event);
    }
	
		
	
	/*************************����໬�¼�*****************************/
	
	
	/**
	 * ������ʾ������menuʱ����ָ������Ҫ�ﵽ���ٶȡ�
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * ��Ļ���ֵ��
	 */
	private int screenWidth;

	/**
	 * menu�����Ի����������Ե��ֵ��menu���ֵĿ��������marginLeft�����ֵ֮�󣬲����ټ��١�
	 */
	private int leftEdge;

	/**
	 * menu�����Ի��������ұ�Ե��ֵ��Ϊ0����marginLeft����0֮�󣬲������ӡ�
	 */
	private int rightEdge = 0;

	/**
	 * menu��ȫ��ʾʱ������content�Ŀ��ֵ��
	 */
	private int menuPadding = 80;

	/**
	 * �����ݵĲ��֡�
	 */
	private View content;

	/**
	 * menu�Ĳ��֡�
	 */
	private View menu;

	/**
	 * menu���ֵĲ�����ͨ���˲���������leftMargin��ֵ��
	 */
	private LinearLayout.LayoutParams menuParams;

	/**
	 * ��¼��ָ����ʱ�ĺ����ꡣ
	 */
	private float xDown;

	/**
	 * ��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	 */
	private float xMove;

	/**
	 * ��¼��̧��ʱ�ĺ����ꡣ
	 */
	private float xUp;

	/**
	 * menu��ǰ����ʾ�������ء�ֻ����ȫ��ʾ������menuʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	 */
	private boolean isMenuVisible;

	/**
	 * ���ڼ�����ָ�������ٶȡ�
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * ��ʼ��һЩ�ؼ������ݡ�������ȡ��Ļ�Ŀ�ȣ���content�����������ÿ�ȣ���menu�����������ÿ�Ⱥ�ƫ�ƾ���ȡ�
	 */
	private void initValues() {
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// ��menu�Ŀ������Ϊ��Ļ��ȼ�ȥmenuPadding(����ȫ��������ұߵľ���)
		menuParams.width = screenWidth - menuPadding;
		// ���Ե��ֵ��ֵΪmenu��ȵĸ���
		leftEdge = -menuParams.width;
		// menu��leftMargin����Ϊ���Ե��ֵ��������ʼ��ʱmenu�ͱ�Ϊ���ɼ�
		menuParams.leftMargin = leftEdge;
		// ��content�Ŀ������Ϊ��Ļ���
		content.getLayoutParams().width = screenWidth;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ָ����ʱ����¼����ʱ�ĺ�����
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬������menu��leftMarginֵ���Ӷ���ʾ������menu
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			// ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�����menu���棬���ǹ�����content����
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾcontent�������ָ�ƶ��ľ����Ǹ������ҵ�ǰmenu�ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾcontent��
	 * 
	 * @return ��ǰ��������ʾcontent����true�����򷵻�false��
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾmenu�������ָ�ƶ��ľ������������ҵ�ǰmenu�ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾmenu��
	 * 
	 * @return ��ǰ��������ʾmenu����true�����򷵻�false��
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����menuչʾ�����������ָ�ƶ����������Ļ��1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
	 * ����ΪӦ�ù�����menuչʾ������
	 * 
	 * @return ���Ӧ�ù�����menuչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�����contentչʾ�����������ָ�ƶ��������menuPadding������Ļ��1/2��
	 * ������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� ����ΪӦ�ù�����contentչʾ������
	 * 
	 * @return ���Ӧ�ù�����contentչʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * ����Ļ������menu���棬�����ٶ��趨Ϊ30.
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	/**
	 * ����Ļ������content���棬�����ٶ��趨Ϊ-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * ����VelocityTracker���󣬲�������content����Ļ����¼����뵽VelocityTracker���С�
	 * 
	 * @param event
	 *            content����Ļ����¼�
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * ��ȡ��ָ��content���滬�����ٶȡ�
	 * 
	 * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * ����VelocityTracker����
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				// Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��20���룬�������۲��ܹ���������������
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param millis
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

