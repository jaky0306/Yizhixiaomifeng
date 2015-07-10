package com.yizhixiaomifeng.tools;

import java.io.File;
import java.io.IOException;

import com.yizhixiaomifeng.config.YzxmfConfig;

import android.media.MediaRecorder;

public class CatchVoiceTool {
	private MediaRecorder recorder;
	public CatchVoiceTool(){
	}
	public void initCatchVoice(){
		
		recorder = new MediaRecorder();// new出MediaRecorder对象  
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		//mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		//mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(YzxmfConfig.voicesrc);
	}
	public void startCatchVoice()
	{
		initCatchVoice();
        try {  
            recorder.prepare();// 准备录制  
            recorder.start();// 开始录制  
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	public void stopCatchVoice(){
		recorder.stop();//停止刻录
//		recorder.reset();//重新启动
		recorder.release(); //释放资源
	}
	public boolean deleteVoice(){
		File file = new File(YzxmfConfig.voicesrc);
		if(file.exists()){
			file.delete();
		}
		return true;
	}
}
