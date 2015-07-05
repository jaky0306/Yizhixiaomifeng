package com.yizhixiaomifeng.tools;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.LogUtil.log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class HeadTool {
	
	public static Bitmap haveHead() {
		Bitmap bitmap = BitmapFactory
				.decodeFile("data/data/com.yizhixiaomifeng/files/head.jpg");
		return bitmap;
	}
	
	public static void saveHead(String username){
		try{
			AVFile file = AVFile.withAbsoluteLocalPath(username, "data/data/com.yizhixiaomifeng/files/head.jpg");
			file.addMetaData("type", "head");
			file.saveInBackground();
		}catch(Exception  e){
			log.e("avfile error",e.toString());
		}
		//{"_checksum":"beeca77717bdaae48f77b7ddb27f0e78","_name":"head.jpg","owner":"","phone":"13265025645","size":80510,"type":"head"}
		
	}
	
	
	/**
	 * ����Բ�ε�bitmap
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {  
        //Բ��ͼƬ���  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //�����εı߳�  
        int r = 0;  
        //ȡ��̱����߳�  
        if(width > height) {  
            r = height;  
        } else {  
            r = width;  
        }  
        //����һ��bitmap  
        Bitmap backgroundBmp = Bitmap.createBitmap(width,  
                 height, Config.ARGB_8888);  
        //newһ��Canvas����backgroundBmp�ϻ�ͼ  
        Canvas canvas = new Canvas(backgroundBmp);  
        Paint paint = new Paint();  
        //���ñ�Ե�⻬��ȥ�����  
        paint.setAntiAlias(true);  
        //�����ȣ���������  
        RectF rect = new RectF(0, 0, r, r);  
        //ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��  
        //�Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��  
        canvas.drawRoundRect(rect, r/2, r/2, paint);  
        //���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        //canvas��bitmap����backgroundBmp��  
        canvas.drawBitmap(bitmap, null, rect, paint);  
        //�����Ѿ��滭�õ�backgroundBmp  
        return backgroundBmp;  
    }
}
