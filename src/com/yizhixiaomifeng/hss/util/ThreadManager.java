package com.yizhixiaomifeng.hss.util;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


import android.util.Log;

public class ThreadManager {
	private static ExecutorService[] threadPool = null;
	/**
	 * 线程池的个数
	 */
	protected int threadPoolNum = 4;
	/**
	 * 每个线程池默认的最大线程执行个数
	 */
	private int defaultThreadNumber=1;
	private int thumbnailThreadNumber=1;
	private int bigImageThreadNumber=1;
	private int specialThreadNumber=1;
	
	public static final int THUMBNAIL_IMAGE_SHOW_POOL=0;
	public static final int BIG_IMAGE_SHOW_POOL=1;
	public static final int SPECIAL_REQUEST_POOL=2;
	public static final int DEFALSE_REQUEST_POOL=3;
	
	
	private Vector<Future<?>> poolResultSet[]=null;
	
	private static volatile ThreadManager instance=null;
	@SuppressWarnings("unchecked")
	private ThreadManager(){
		poolResultSet=new Vector[threadPoolNum];
	}
	public static ThreadManager getInstance(){
		if(instance==null){
			synchronized (ThreadManager.class) {
				if(instance==null){
					instance=new ThreadManager();
				}
			}
		}
		return instance;
	}

	public void submit(Runnable thread,int index){
		getResultSet(index).add((Future<?>) getThreadPool(index).submit(thread));
	}
	public synchronized void clear(int index){
		clear(index,false);
	}
	public synchronized void clear(int index,boolean clearingBraakTask){
		if (index >= threadPoolNum)
			index = threadPoolNum-1;
		Future<?> f=null;
		Log.e("ThreadManager", "第"+index+"个线程池的任务个数为："+getResultSet(index).size());
		for(int i=0;i<getResultSet(index).size();i++){
			Log.e("ThreadManager", "正在取消第"+index+"个线程池的第"+i+"个任务");
			f=getResultSet(index).get(i);
			f.cancel(clearingBraakTask);//不允许正在执行的线程中断
//			f.cancel(true);//允许正在执行的线程中断
			if(f.isCancelled()||f.isDone()){
//				getResultSet(index).remove(i);
				getResultSet(index).remove(f);
				i--;
				Log.e("ThreadManager", "任务已经取消");
			}
		}
	}
	public synchronized void clearALL(){
		for(int i=0;i<threadPoolNum;i++){
			clear(i);
		}
	}
	public synchronized void shutDownNow(int index){
		if (index >= threadPoolNum)
			index = threadPoolNum-1;
		if(threadPool[index] != null){
			threadPool[index].shutdownNow();
			threadPool[index]=null;
			Log.e("ThreadManager", "关闭第"+index+"个线程池");
		}
	}

	public synchronized void shutDownNowALL(){
		for(int i=0;i<threadPoolNum;i++){
			shutDownNow(i);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Vector<Future<?>> getResultSet(int index){
		if (poolResultSet == null)
			poolResultSet=new Vector[threadPoolNum];
		if (index >= threadPoolNum)
			index = threadPoolNum-1;
		if (poolResultSet[index] == null) {
			poolResultSet[index] = new Vector<Future<?>>();
		}
		return poolResultSet[index];
	}
	
	private ExecutorService getThreadPool(int index){
		if (threadPool == null)
			threadPool = new ExecutorService[threadPoolNum];
		if (index >= threadPoolNum)
			index = threadPoolNum-1;
		if (threadPool[index] == null) {
			synchronized (ExecutorService.class) {
				if(threadPool[index] == null){
					switch(index){
					case THUMBNAIL_IMAGE_SHOW_POOL:
						threadPool[index] = Executors.newFixedThreadPool(thumbnailThreadNumber);
						break;
					case BIG_IMAGE_SHOW_POOL:
						threadPool[index] = Executors.newFixedThreadPool(bigImageThreadNumber);
						break;
					case SPECIAL_REQUEST_POOL:
						threadPool[index] = Executors.newFixedThreadPool(specialThreadNumber);
						break;
					case DEFALSE_REQUEST_POOL:
						threadPool[index] = Executors.newFixedThreadPool(defaultThreadNumber);
						break;
					}
				}
			}
		}
		return threadPool[index];
	}
	
	public synchronized void setPoolMaxThreads(int threadNumber,int poolIndex) {
		ThreadPoolExecutor pool=(ThreadPoolExecutor) getThreadPool(poolIndex);
		pool.setMaximumPoolSize(threadNumber);
		pool.setCorePoolSize(threadNumber);
	}
}
