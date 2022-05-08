package com.ypyproductions.voicechanger.utils;

import java.util.Stack;

import com.ypyproductions.voicechanger.task.IDBCallback;

/**
 * 
 *  DBListExcuteAction.java	
 * @Author    DoBao
 * @Email     baodt@hanet.vn
 * @Phone     +84983028786
 * @Skype     baopfiev_k50
 * @Date      Dec 23, 2013  	
 * @Project   WhereMyLocation
 * @Package   com.ypyproductions.utils
 * @Copyright � 2013 YPY Productions
 */
public class DBListExcuteAction {
	
	public static final String TAG=DBListExcuteAction.class.getSimpleName();
	
	public static DBListExcuteAction mListExcuteAction;
	private static Stack<IDBCallback> mListDBCallback;
	private static ActionLoaderThread mActionLoaderThread;
	private static boolean isRunning=false;
	
	private DBListExcuteAction() {
		mListDBCallback = new Stack<IDBCallback>();
		mActionLoaderThread = new ActionLoaderThread();
		mActionLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
	}
	
	public static DBListExcuteAction getInstance(){
		if(mListExcuteAction==null){
			mListExcuteAction = new DBListExcuteAction();
		}
		return mListExcuteAction;
	}
	
	public void queueAction(IDBCallback mDBCallback){
		if(mListDBCallback==null) {
			return;
		}
		synchronized (mListDBCallback) {
			mListDBCallback.push(mDBCallback);
			mListDBCallback.notifyAll();
		}
		if (mActionLoaderThread.getState() == Thread.State.NEW) {
			isRunning=true;
			mActionLoaderThread.start();
		}
	}
	
	public void onDestroy(){
		if(mListDBCallback!=null){
			synchronized (mListDBCallback) {
				mListDBCallback.clear();
				mListDBCallback=null;
			}
		}
		if(mActionLoaderThread!=null){
			isRunning=false;
			mActionLoaderThread.interrupt();
			mActionLoaderThread=null;
		}
		if(mListExcuteAction!=null){
			mListExcuteAction=null;
		}
	}
	
	/**
	 * Thread to excute network
	 * @author DoBao
	 * 
	 */
	protected class ActionLoaderThread extends Thread {
		@Override
		public void run() {
			try {
				while (isRunning) {
					int size = mListDBCallback.size();
					if (size == 0) {
						synchronized (mListDBCallback) {
							mListDBCallback.wait();
						}
					} 
					else if (size > 0) {
						IDBCallback mDBCallback=null;
						synchronized (mListDBCallback) {
							if(!mListDBCallback.isEmpty()){
								mDBCallback = mListDBCallback.pop();
							}
						}
						if(mDBCallback!=null){
							mDBCallback.onAction();
						}
					}
					if (Thread.interrupted()){
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
