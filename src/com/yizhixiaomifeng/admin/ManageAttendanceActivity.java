package com.yizhixiaomifeng.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.avos.avoscloud.LogUtil.log;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.adapter.ShowClientListViewAdapter;
import com.yizhixiaomifeng.adapter.ShowFailAttendanceListViewAdapter;
import com.yizhixiaomifeng.admin.ManageClientActivity.LoadClientDataHelper;
import com.yizhixiaomifeng.admin.bean.FailAttendance;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.config.CheckInAndOutStauts;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnLoadListener;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnRefreshListener;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.NewsManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ManageAttendanceActivity extends Activity implements OnRefreshListener,OnLoadListener{
	private ImageView search_attendance_imageview;
	private AutoListView show_all_fail_attendance;
	private ShowFailAttendanceListViewAdapter adapter;
	private List<FailAttendance> all_failattendances_list = new ArrayList<FailAttendance>();
	
	private TextView manage_attendance_time;
	private TextView manage_attendance_nums;
	private TextView manage_attendance_ok_nums;
	private TextView manage_attendance_fail_nums;
	private TextView manage_attendance_be_late_nums;
	private TextView manage_attendance_leave_earlier_nums;
	private TextView manage_attendance_absence_nums;
	
	private int totalnum=0;		//考勤人数
	private int belatenum=0;	//迟到人数
	private int leaveeariernum=0;	//早退人数
	private int absencenum=0;
	private int start=0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<FailAttendance> result = (List<FailAttendance>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				show_all_fail_attendance.onRefreshComplete();
				all_failattendances_list.clear();
				all_failattendances_list.addAll(result);
				break;
			case AutoListView.LOAD:
				show_all_fail_attendance.onLoadComplete();
				all_failattendances_list.addAll(result);
				break;
			}
			show_all_fail_attendance.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_attendance);
		ActivityCloser.activities.add(this);
		
		manage_attendance_time=(TextView)findViewById(R.id.manage_attendance_time);
		manage_attendance_nums=(TextView)findViewById(R.id.manage_attendance_nums);
		manage_attendance_ok_nums=(TextView)findViewById(R.id.manage_attendance_ok_nums);
		manage_attendance_fail_nums=(TextView)findViewById(R.id.manage_attendance_fail_nums);
		manage_attendance_be_late_nums=(TextView)findViewById(R.id.manage_attendance_be_late_nums);
		manage_attendance_leave_earlier_nums=(TextView)findViewById(R.id.manage_attendance_leave_earlier_nums);
		manage_attendance_absence_nums=(TextView)findViewById(R.id.manage_attendance_absence_nums);
		
		search_attendance_imageview=(ImageView)findViewById(R.id.search_attendance_imageview);
		show_all_fail_attendance = (AutoListView) findViewById(R.id.show_all_fail_attendance);
		adapter = new ShowFailAttendanceListViewAdapter(ManageAttendanceActivity.this, all_failattendances_list);
		show_all_fail_attendance.setAdapter(adapter);
		show_all_fail_attendance.setOnRefreshListener(this);
		show_all_fail_attendance.setOnLoadListener(this);
		initData();
		show_all_fail_attendance.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
					long arg3) {
				FailAttendance fa = all_failattendances_list.get(postion-1);
				Intent intent = new Intent(ManageAttendanceActivity.this,ShowFailAttendanceInfoActivity.class);
				intent.putExtra("failattendance", fa);
				startActivity(intent);
			}
		});
		show_all_fail_attendance.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
        {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
            {
            	menu.setHeaderIcon(R.drawable.key_icon);
                menu.setHeaderTitle("操作");
                menu.add(0, 0, 0, "拨打电话");
            }
        });
		search_attendance_imageview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManageAttendanceActivity.this,SearchAttendanceActivity.class);
				startActivity(intent);
			}
		});
	}

	// 长按菜单响应函数 
    public boolean onContextItemSelected(MenuItem item)
    { 
    	AdapterView.AdapterContextMenuInfo menuInfo = 
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	FailAttendance attendance=null;
        switch (item.getItemId()) { 
        case 0: //操作
        	attendance = all_failattendances_list.get(menuInfo.position-1);	 //获取当前点击的news
        	String phone = attendance.getPhone();
        	//用intent启动拨打电话  
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
            startActivity(intent);  
            break; 
        } 

        return super.onContextItemSelected(item); 

    } 
	
	
	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	private void loadData(final int what) {
		//获取异常考勤
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date=sdf.format(new Date(Calendar.getInstance().getTimeInMillis()));
		Date goaldate;
		try {
			goaldate = sdf.parse(date);
			new LoadFailAttendanceDataHelper(what).execute(goaldate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		

	}

	@Override
	public void onRefresh() {
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		loadData(AutoListView.LOAD);
	}

	class LoadFailAttendanceDataHelper extends AsyncTask<Long, Integer, String>{
		private int what;
		public LoadFailAttendanceDataHelper(int what){
			this.what=what;
		}
		@Override
		protected String doInBackground(Long... params) {
			String result = new ConnectWeb().getAttendanceDataByDate(params[0]);
			return result;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			if(result!=null){
				Log.e("LoadFailAttendanceDataHelper", result);
				List<FailAttendance> data = new ArrayList<FailAttendance>();
				try {
					JSONArray jsonArray = new JSONArray(result);
					Log.e("size", ""+jsonArray.length());
					for(int i=0;i<jsonArray.length();i++){
						JSONObject object = jsonArray.getJSONObject(i);
						totalnum=object.getInt("RecordAllNum");
						long checkInTime = object.getLong("checkInTime");
						String checkInStatus = object.getString("checkInStatus");
						String checkInResult = object.getString("checkInResult");
						long checkOutTime = object.getLong("checkOutTime");
						String checkOutStatus = object.getString("checkOutStatus");
						String checkOutResult=object.getString("checkOutResult");
						String checkInSceneUrl=object.getString("checkInSceneURL");
						String checkInVoiceUrl=object.getString("checkInVoice");
						String checkOutVoiceUrl=object.getString("checkOutVoiceUrl");
						
						JSONObject mission= object.getJSONObject("missionAllotId");
						JSONObject jobnum=mission.getJSONObject("jobNum");
						String phone = jobnum.getString("phone");
						String name = jobnum.getString("name");
						
						FailAttendance fa = new FailAttendance(phone, name, checkInTime, checkInStatus, checkInResult, checkOutTime, checkOutStatus, checkOutResult, checkInSceneUrl, checkInVoiceUrl, checkOutVoiceUrl);
						data.add(fa);
						//迟到
						if(fa.getCheckInResult().equals(CheckInAndOutStauts.checkIn_late)){
							belatenum++;
						}
						//早退
						if(fa.getCheckOutResult().equals(CheckInAndOutStauts.checkOut_earier)){
							leaveeariernum++;
						}
						if(fa.getCheckInResult().equals(CheckInAndOutStauts.checkIn_nocheck)||fa.getCheckOutResult().equals(CheckInAndOutStauts.checkOut_nocheck)){
							absencenum++;
						}
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					manage_attendance_time.setText(sdf.format(new Date()));
					
					manage_attendance_nums.setText(""+totalnum);
					manage_attendance_ok_nums.setText(""+(totalnum-data.size()));
					manage_attendance_fail_nums.setText(""+(data.size()));
					manage_attendance_be_late_nums.setText(""+belatenum);
					manage_attendance_leave_earlier_nums.setText(""+leaveeariernum);
					manage_attendance_absence_nums.setText(""+absencenum);
					
					Log.e("datasize", "datasize "+data.size());
					Message msg = new Message();
					msg.what = what;
					msg.obj = data;
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//[{"address":"中国移动南方基地","endTime":1,"id":1,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"APP开发","startTime":1},{"address":"广州诺特科技有限公司","endTime":1,"id":2,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"企业外勤人员考勤管理系统","startTime":1}]
				
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
}
