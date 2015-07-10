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
		
		recorder = new MediaRecorder();// new��MediaRecorder����  
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
            recorder.prepare();// ׼��¼��  
            recorder.start();// ��ʼ¼��  
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	public void stopCatchVoice(){
		recorder.stop();//ֹͣ��¼
//		recorder.reset();//��������
		recorder.release(); //�ͷ���Դ
	}
	public boolean deleteVoice(){
		File file = new File(YzxmfConfig.voicesrc);
		if(file.exists()){
			file.delete();
		}
		return true;
	}
}
