package com.yizhixiaomifeng.adapter;

import java.util.List;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.EditClientActivity;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.tools.ConnectWeb;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * @author Jaky
 * 
 */
public class ShowClientListViewAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<Client> list;
	private Context context;
//	RelativeLayout.LayoutParams edlp;
//	RelativeLayout.LayoutParams cilp;
//	private int screenWidth;
//	private boolean show_ed_button=false;
//	private int ed_layout_width=200;//�༭��ɾ����ť�Ŀ��
	public ShowClientListViewAdapter(Context context, List<Client> list) {
		this.list = list;
		this.context = context;
//		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		screenWidth = window.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.show_client_listview_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.projectName=(TextView)convertView.findViewById(R.id.project_name);
			holder.address=(TextView)convertView.findViewById(R.id.address);
			holder.edit_button=(Button)convertView.findViewById(R.id.edit_item_button);
			holder.delete_button=(Button)convertView.findViewById(R.id.delete_item_button);
			
//			holder.edit_delete_layout=(LinearLayout) convertView.findViewById(R.id.edit_delete_layout);
//			holder.client_item_info_layout=(RelativeLayout) convertView.findViewById(R.id.client_item_info);
//			//��ʼ��layout���������ĵ���
//			edlp = (LayoutParams) holder.edit_delete_layout.getLayoutParams();
//			cilp=(LayoutParams)holder.client_item_info_layout.getLayoutParams();
			
			convertView.setTag(holder); //����Item
			
			
			
			//�����ð�ť����߾�Ϊ��Ļ�ı߾࣬������
//			edlp.rightMargin=-ed_layout_width;
//			holder.edit_delete_layout.setLayoutParams(edlp);
			
			
//			convertView.setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					float x1 = 0,x2=0;
//					final ViewHolder holder = (ViewHolder) v.getTag();  
//					//������ʱ����  
//					switch (event.getAction()) {
//					case MotionEvent.ACTION_DOWN:
//						x1 = event.getX();  
//						Log.e("down x1",""+x1);
//						break;
//					case MotionEvent.ACTION_UP:
//						x2 = event.getX();  
//						Log.e("up x2",""+x2);
//						float dis = x1-x2;
//						if (Math.abs(dis)> 20) 
//						{  
//							if(dis>=0){
//								if(!show_ed_button){
//									Log.e("aaaaaaaaaaaaaaa", "un show");
//									//�������ʾ
////									edlp.leftMargin=screenWidth-ed_layout_width;
//									edlp.rightMargin=ed_layout_width;
//									holder.edit_delete_layout.setLayoutParams(edlp);
//									show_ed_button=true;
//									//�����ʾ�ˣ��Ҿ�Ҫ�ô�ؼ�������ͬ���ľ���
//									cilp.leftMargin=-ed_layout_width;
//									holder.client_item_info_layout.setLayoutParams(cilp);
//								}
//							}else{
//								if(show_ed_button){
//									Log.e("aaaaaaaaaaaaaaa", "show");
//									edlp.rightMargin=-ed_layout_width;
//									holder.edit_delete_layout.setLayoutParams(edlp);
//									show_ed_button=false;
//									//�����ʾ�ˣ��Ҿ�Ҫ�ô�ؼ�������ͬ���ľ���
//									cilp.rightMargin=0;
//									holder.client_item_info_layout.setLayoutParams(cilp);;
//								}
//								
//							}
//						} 
//						break;
//					case MotionEvent.ACTION_MOVE:
////						x2 = event.getX();  
////						Log.e("move x2",""+x2);
////						float move_dis = x1-x2;
////						if(move_dis>=0){ //���һ���
////							if(!show_ed_button)
////							{
////								
////							}else {
////								//�������ʾ
////								edlp.leftMargin=(int) (screenWidth-move_dis);
////								edl.setLayoutParams(edlp);
////								show_ed_button=true;
////								//�����ʾ�ˣ��Ҿ�Ҫ�ô�ؼ�������ͬ���ľ���
////								cilp.rightMargin=(int) (ed_layout_width-move_dis);
////								cil.setLayoutParams(cilp);
////							}
////						}else if(move_dis<0){
////							if(!show_ed_button)
////							{
////								
////							}else {
////								//�������ʾ
////								edlp.leftMargin=(int) (screenWidth-move_dis);
////								edl.setLayoutParams(edlp);
////								show_ed_button=true;
////								//�����ʾ�ˣ��Ҿ�Ҫ�ô�ؼ�������ͬ���ľ���
////								cilp.rightMargin=(int) (ed_layout_width-move_dis);
////								cil.setLayoutParams(cilp);
////							}
////						}
//						
//						break;
//					default:
//						break;
//					}
//					
//					return true;
//				}
//				
//			});
			
//		}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Client client = list.get(position);
		holder.name.setText(client.getName());
		holder.projectName.setText(client.getProjectName());
		holder.address.setText(client.getAddress());
		
		holder.edit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,EditClientActivity.class);
//				intent.putExtra("name", client.getName());
//				intent.putExtra("projectName", client.getProjiectName());
//				intent.putExtra("address", client.getAddress());
				intent.putExtra("client", client);
				context.startActivity(intent);
			}
		});
		holder.delete_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						new ConnectWeb().deleteClient(client);
						Message msg = new Message();
						msg.what=0x111;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
		return convertView;
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0x111){
				Toast.makeText(context, "����ɾ��,���Ժ�ˢ��...", Toast.LENGTH_LONG).show();
			}

		};
	};
	
	private static class ViewHolder {
		TextView name;
		TextView projectName;
		TextView address;
//		RelativeLayout client_item_info_layout;
//		LinearLayout edit_delete_layout;
		Button edit_button;
		Button delete_button;
	}

}
