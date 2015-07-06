package com.yizhixiaomifeng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.yizhixiaomifeng.adapter.MenuListViewAdapter;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.HeadLoader;
import com.yizhixiaomifeng.tools.HeadTool;
import com.yizhixiaomifeng.tools.InfoLoader;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {
	
	private ImageView head ;  //用户头像
	private ImageView show_menu_layout;  //点击可用来切换menu
	private ImageView menu_head;
	private TextView menu_show_user_name; //侧滑中的名字显示
	private TextView main_show_staff_name; //主页面中的名字显示
	private TextView main_show_staff_department; //主页面中的员工部门信息
	private TextView main_show_staff_job;	//主页面中的员工职位信息
	private LinearLayout show_loadinfo_tip;
	private TextView showTime; //显示时间
	private TextView showDate;  //显示日期
	private String timeString;  //用来合成时间信息
	private String dateString; 	//用来合成日期信息
	private Button signin;		//登录按钮
	private String user;		//当前用户名
	private String type;		//用户类型
	private List<String> menuitems=new ArrayList<String>();	//menu 显示的数据
	
	private Button checkin ;
	private Button checkout;
	
	
	private ListView showMenuItems ;		//menu显示item的listview
	private int exit=0;  //	用来记录click 的次数
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				showTime.setText(msg.obj.toString());
			}
			if(msg.what==0x112){
				showDate.setText(msg.obj.toString());
			}
			if(msg.what==0x113){
				LocalStorage ls = new LocalStorage(MainActivity.this);
				menu_show_user_name.setText(ls.getString("name","****"));
				main_show_staff_name.setText(ls.getString("name", "****"));
				main_show_staff_job.setText(ls.getString("duty", "****"));
				main_show_staff_department.setText(ls.getString("department", "****"));
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * 初始化leancloud
		 */
		String appid="l06kinvak9zixn4u1paent0an7nfsb662swhrk1qvfc998w0";
		String key="b7tlw6hcvzsu3t875abdf9htcygrbaypnftgnmo90mwnk7ie";
		AVOSCloud.initialize(this, appid, key);
		
		ActivityCloser.activities.add(this);
		
		signin=(Button)findViewById(R.id.singin);
		checkin=(Button)findViewById(R.id.check_in);
		checkout=(Button)findViewById(R.id.check_out);
		
		LocalStorage ls = new LocalStorage(this);
		user = ls.getString("username", "");
		type = ls.getString("type", "");
		
		
		initByUser();
		
		
		
		
		/**
		 * 切换菜单
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
		 * 初始化侧滑
		 */
		initValues();
		content.setOnTouchListener(this);
		
		/**
		 * 登录事件
		 */
		
		signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user.equals("")){
					Intent intent = new Intent(MainActivity.this,Login.class);
					startActivity(intent);
				}else {
					Toast.makeText(getApplicationContext(), "你已经登录...", Toast.LENGTH_LONG).show();
					return;
				}
				
			}
		});
		
		/**
		 * 显示时间日期信息
		 */
		showTimeAndDate();
		
		
		/**
		 * 签到签退事件
		 */
		checkin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user.equals("")){
					Toast.makeText(getApplicationContext(), "请先登录...", Toast.LENGTH_LONG).show();
					return;
				}else {
					Intent intent = new Intent(MainActivity.this,CheckIn.class);
					startActivity(intent);
				}
			}
		});
		
	}

	public void initByUser()
	{
		if(user.equals("")){
			
			/**
			 * 改变登录按钮状态
			 */
			signin.setText("登录");
			signin.setEnabled(true);
			initWithoutUser();
			/*********************初始化MenuLayout************************/
			
		}else 
		{
			/**
			 * 改变登录按钮状态
			 */
			signin.setText("已登录");
			signin.setEnabled(false);
			loadUserInfo(); //加载用户信息
			
			if(type.equals("员工"))
			{
				menuitems.clear();
				menuitems.add("个人中心");
				showMenuItems=(ListView)findViewById(R.id.showMenuItems_listview);
				showMenuItems.setAdapter(new MenuListViewAdapter(MainActivity.this, menuitems));
				showMenuItems.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int position,
							long id) {
						switch (position) {
						case 0:
							Intent intent1 = new Intent(MainActivity.this, SettingCenter.class);
							startActivity(intent1);
							break;
						default:
							break;
						}
					}
				});
			}else if(type.equals("管理员"))
			{
				
			}
		}
	}
	
	/**
	 * 初始化没有用户信息时，界面的显示
	 */
	public void initWithoutUser(){
		menuitems.clear();
		menuitems.add("个人中心");
		showMenuItems=(ListView)findViewById(R.id.showMenuItems_listview);
		showMenuItems.setAdapter(new MenuListViewAdapter(MainActivity.this, menuitems));
		showMenuItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				switch (position) {
				case 0:
					Intent intent1 = new Intent(MainActivity.this, SettingCenter.class);
					startActivity(intent1);
					break;
				default:
					break;
				}
			}
		});
	}
	/**
	 * 初始化有用户信息时。用户信息的加载
	 */
	public void loadUserInfo()
	{
		/**
		 * 初始化头像
		 */
		show_loadinfo_tip=(LinearLayout)findViewById(R.id.show_loadinfo_tip_LinearLayout);
		head = (ImageView)findViewById(R.id.staff_head);
		menu_head=(ImageView)findViewById(R.id.menu_head);
		ImageView [] heads=new ImageView[]{head,menu_head};
		
		if(ParameterConfig.headChange||ParameterConfig.firstUse){ //如果头像改变了,或第一次使用，立刻更新头像
			new HeadLoader(heads,show_loadinfo_tip).execute(user);     //获取最新头像信息
			ParameterConfig.headChange=false; //头像更新完毕了，把标记设为false
		}
		/**
		 * 初始化信息
		 */
		menu_show_user_name=(TextView)findViewById(R.id.menu_show_user_name);
		main_show_staff_name=(TextView)findViewById(R.id.staff_name);
		main_show_staff_department=(TextView)findViewById(R.id.staff_info_department);
		main_show_staff_job=(TextView)findViewById(R.id.staff_info_job);
		
//		if(ParameterConfig.infoChange||ParameterConfig.firstUse){
//			List<TextView> textViews = new ArrayList<TextView>();
//			textViews.add(menu_show_user_name);
//			textViews.add(main_show_staff_department);
//			textViews.add(main_show_staff_department);
//			textViews.add(main_show_staff_job);
//			new InfoLoader(textViews,show_loadinfo_tip).execute(user);  //获取最新用户信息
//			ParameterConfig.infoChange=false; //信息更新完毕了，把标记设为false
//			
//		}
		Message msg = new Message();
		msg.what=0x113;
		handler.sendMessage(msg);
		
		ParameterConfig.firstUse=false; //用户信息加载完毕了，说明已经使用过了
	}
	/**
	 * 显示时间
	 */
	public void showTimeAndDate(){
		
		showTime=(TextView)findViewById(R.id.show_time);
		showDate=(TextView)findViewById(R.id.show_date);
		final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
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
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
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
	
	
	@Override
	protected void onResume() {
		
		LocalStorage ls = new LocalStorage(this);
		user = ls.getString("username", "");
		type = ls.getString("type", "");
		initByUser();
		super.onResume();
	}
	
	
	/*************************处理侧滑事件*****************************/
	
	
	/**
	 * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * 屏幕宽度值。
	 */
	private int screenWidth;

	/**
	 * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
	 */
	private int leftEdge;

	/**
	 * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	 */
	private int rightEdge = 0;

	/**
	 * menu完全显示时，留给content的宽度值。
	 */
	private int menuPadding = 80;

	/**
	 * 主内容的布局。
	 */
	private View content;

	/**
	 * menu的布局。
	 */
	private View menu;

	/**
	 * menu布局的参数，通过此参数来更改leftMargin的值。
	 */
	private LinearLayout.LayoutParams menuParams;

	/**
	 * 记录手指按下时的横坐标。
	 */
	private float xDown;

	/**
	 * 记录手指移动时的横坐标。
	 */
	private float xMove;

	/**
	 * 记录手抬起时的横坐标。
	 */
	private float xUp;

	/**
	 * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
	 */
	private boolean isMenuVisible;

	/**
	 * 用于计算手指滑动的速度。
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
	 */
	private void initValues() {
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		// 将menu的宽度设置为屏幕宽度减去menuPadding(即完全划出后跟右边的距离)
		menuParams.width = screenWidth - menuPadding;
		// 左边缘的值赋值为menu宽度的负数
		leftEdge = -menuParams.width;
		// menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
		menuParams.leftMargin = leftEdge;
		// 将content的宽度设置为屏幕宽度
		content.getLayoutParams().width = screenWidth;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时，记录按下时的横坐标
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
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
			// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
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
	 * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
	 * 
	 * @return 当前手势想显示content返回true，否则返回false。
	 */
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	/**
	 * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
	 * 
	 * @return 当前手势想显示menu返回true，否则返回false。
	 */
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	/**
	 * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
	 * 就认为应该滚动将menu展示出来。
	 * 
	 * @return 如果应该滚动将menu展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
	 * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
	 * 
	 * @return 如果应该滚动将content展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 将屏幕滚动到menu界面，滚动速度设定为30.
	 */
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	/**
	 * 将屏幕滚动到content界面，滚动速度设定为-30.
	 */
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 *            content界面的滑动事件
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
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
				// 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
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
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

