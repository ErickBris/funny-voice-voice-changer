package com.ypyproductions.voicechanger.task;


/**
 * Interface for DBTASK 
 * @author DoBao
 * Dec 26, 2012
 * @company Citigo
 * @email bao.do@citigovietnam.com
 */

public abstract interface IDBTaskListener {
	
	public abstract void onPreExcute();
	public abstract void onPostExcute();
	public abstract void onDoInBackground();
}
