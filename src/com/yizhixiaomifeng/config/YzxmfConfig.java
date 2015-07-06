package com.yizhixiaomifeng.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class YzxmfConfig {
	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * 判断网络是否能连接
     * @param Context context
     * @return
     */
    public static boolean isConnect(Context context)
    {
        try{
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
            if(cm!=null)
            {
                NetworkInfo info=cm.getActiveNetworkInfo();
                if(info!=null&&info.isConnected())
                {
                    if(info.getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }catch(Exception e)
        {
            return false;
        }
        return false;
    }
}
