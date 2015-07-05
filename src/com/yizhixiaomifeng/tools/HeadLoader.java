package com.yizhixiaomifeng.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HeadLoader extends AsyncTask<String, Integer, byte[]>{
	private ImageView []headImageView;
	private LinearLayout show_loadinfo_tip;
	public HeadLoader(ImageView[] imageView,LinearLayout linearLayout){
		this.headImageView=imageView;
		this.show_loadinfo_tip=linearLayout;
	}
	@Override
	protected byte[] doInBackground(String... params) {
		show_loadinfo_tip.setVisibility(View.VISIBLE);
		byte []headdata = new AvosTool().getHead(params[0]);
		return headdata;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(byte []result) {
		if(result!=null){
			Bitmap b = BitmapFactory.decodeByteArray(result, 0, result.length);
			for(int i=0;i<headImageView.length;i++){
				headImageView[i].setImageBitmap(b);
			}
		}
		show_loadinfo_tip.setVisibility(View.GONE);
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