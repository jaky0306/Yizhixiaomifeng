package com.yizhixiaomifeng.user;

import java.io.File;
import java.io.FileOutputStream;

import com.baidu.location.f;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.AdminMainActivity;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.HeadTool;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetHead extends Activity {
	private static final String IMAGE_FILE_NAME = "head.jpg";
	private String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	ImageView head ;
	LinearLayout uphead_tip;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0x111){
				uphead_tip.setVisibility(View.VISIBLE);
			}
			if(msg.what==0x112){
				uphead_tip.setVisibility(View.INVISIBLE);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_head);
		
		ActivityCloser.activities.add(this);
		uphead_tip=(LinearLayout)findViewById(R.id.show_uphead_tip);
		head = (ImageView) findViewById(R.id.staff_head);
		Bitmap bm = null;
		if ((bm = HeadTool.haveHead()) != null) {
			//head.setImageBitmap(bm);
			//Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.bt);
		      Bitmap output=HeadTool.toRoundBitmap(bm);
		      head.setImageBitmap(output);
		}
		head.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog();
			}
		});
		
		ImageView set_head_back = (ImageView)findViewById(R.id.set_head_back);
		set_head_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetHead.this,SettingCenter.class);
				startActivity(intent);
				SetHead.this.finish();
			}
		});
	}
/**********************����ͷ��******************************/
	
	public void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("����ͷ��")
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent fileIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							//fileIntent.setClassName("com.android.gallery", "com.android.camera.CropImage"); 
							fileIntent.setType("image/*");
							fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
							startActivityForResult(fileIntent,
									IMAGE_REQUEST_CODE);
							break;
						case 1:
							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE); // �����Դ
							if (YzxmfConfig.hasSdcard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) // �����صĽ��
	{
		
		if(resultCode!=0)
		{
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (YzxmfConfig.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(SetHead.this, "δ�ҵ��洢�����޷��洢��Ƭ��",
							Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	//ͷ��Ľ�ȡ
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);//RESULT_REQUEST_CODE
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//ת��Բ��
			Bitmap roundBitmap = HeadTool.toRoundBitmap(photo);
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(roundBitmap);
			saveHead(roundBitmap);
			head.setImageDrawable(drawable);

		}
	}
	
	//����ͷ���ڸ�Ŀ¼���head.jpg

	public void saveHead(Bitmap bitmap) {
		
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("head.jpg", 0);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		/**
		 * ͼƬ��ȡ�����̱��浽��
		 */
		LocalStorage ls = new LocalStorage(this);
		String username = ls.getString("username", "");
		String type = ls.getString("type", "");
		Log.e("aaaaaaaaa", "aa"+username+" "+type);
		if(YzxmfConfig.isConnect(this)){
			new HeadUper().execute(username,type);
		}
		
		
	}

	
	
	class HeadUper extends AsyncTask<String, Integer, Void>{
		@Override
		protected Void doInBackground(String... params) {
			/**
			 * �ù�������ʾ
			 */
			Message msg = new Message();
			msg.what=0x111;
			handler.sendMessage(msg);
			new AvosTool().saveHead(params[0],params[1]);  //��ͷ�񱣴浽LeanCloud
//			String url = new AvosTool().getHeadUrl(params[0], params[1]);
//			Log.e("aaaaaaaaaaaaaa", url);
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			/**
			 * �ù���������
			 */
			Message msg = new Message();
			msg.what=0x112;
			handler.sendMessage(msg);
			ParameterConfig.headChange=true; //ͷ������ˣ�����MainActivity
			LocalStorage ls = new LocalStorage(SetHead.this);
			String type=ls.getString("type", "");
			if(type.equals("staff")){
				Intent intent = new Intent(SetHead.this,MainActivity.class);
				startActivity(intent);
				SetHead.this.finish();
			}
			if(type.equals("admin")){
				Intent intent = new Intent(SetHead.this,AdminMainActivity.class);
				startActivity(intent);
				SetHead.this.finish();
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
