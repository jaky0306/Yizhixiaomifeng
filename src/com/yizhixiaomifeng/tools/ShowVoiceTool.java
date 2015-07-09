package com.yizhixiaomifeng.tools;

import java.io.File;

import com.yizhixiaomifeng.config.YzxmfConfig;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class ShowVoiceTool {
	private MediaPlayer player; // MediaPlayer对象
	private File file; // 要播放的音频文件
	private Context context;
	public ShowVoiceTool(Context context){
		this.context=context;
		file=new File(YzxmfConfig.voicesrc);
		if (file.exists()) { // 如果文件存在
			player = MediaPlayer
					.create(context, Uri.parse(file.getAbsolutePath())); // 创建MediaPlayer对象
		}
		// 为MediaPlayer对象添加完成事件监听器
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				play(); // 重新开始播放
			}
		});
	}
	// 播放音乐的方法
	public void play() {
		try {
			player.reset();
			//这里可以传网络音频
			player.setDataSource(file.getAbsolutePath()); // 重新设置要播放的音频
			player.prepare(); // 预加载音频
			player.start(); // 开始播放
		} catch (Exception e) {
			e.printStackTrace(); // 输出异常信息
		}
	}
	public void stop() 
	{
		if(player.isPlaying())
		{
			player.stop();	//停止音频的播放
		}
		player.release();	//释放资源
	}
}
