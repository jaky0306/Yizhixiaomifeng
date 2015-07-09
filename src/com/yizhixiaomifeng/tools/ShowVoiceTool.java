package com.yizhixiaomifeng.tools;

import java.io.File;

import com.yizhixiaomifeng.config.YzxmfConfig;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

public class ShowVoiceTool {
	private MediaPlayer player; // MediaPlayer����
	private File file; // Ҫ���ŵ���Ƶ�ļ�
	private Context context;
	public ShowVoiceTool(Context context){
		this.context=context;
		file=new File(YzxmfConfig.voicesrc);
		if (file.exists()) { // ����ļ�����
			player = MediaPlayer
					.create(context, Uri.parse(file.getAbsolutePath())); // ����MediaPlayer����
		}
		// ΪMediaPlayer�����������¼�������
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				play(); // ���¿�ʼ����
			}
		});
	}
	// �������ֵķ���
	public void play() {
		try {
			player.reset();
			//������Դ�������Ƶ
			player.setDataSource(file.getAbsolutePath()); // ��������Ҫ���ŵ���Ƶ
			player.prepare(); // Ԥ������Ƶ
			player.start(); // ��ʼ����
		} catch (Exception e) {
			e.printStackTrace(); // ����쳣��Ϣ
		}
	}
	public void stop() 
	{
		if(player.isPlaying())
		{
			player.stop();	//ֹͣ��Ƶ�Ĳ���
		}
		player.release();	//�ͷ���Դ
	}
}
