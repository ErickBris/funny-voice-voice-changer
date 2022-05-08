package com.ypyproductions.voicechanger.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * 
 * 
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:NhacVui
 * @Date:Jul 14, 2015
 * 
 */
public class DBSwipeRefreshLayout extends SwipeRefreshLayout {

	public static final String TAG = DBSwipeRefreshLayout.class.getSimpleName();
	
	private OnChildScrollUpListener mScrollListenerNeeded;

	public DBSwipeRefreshLayout(Context context) {
		super(context);
	}

	public DBSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setOnChildScrollUpListener(OnChildScrollUpListener listener) {
	    mScrollListenerNeeded = listener;   
	  }

	public interface OnChildScrollUpListener {
		public boolean canChildScrollUp();
	}
	@Override
	public boolean canChildScrollUp() {
		if(mScrollListenerNeeded!=null){
			return mScrollListenerNeeded.canChildScrollUp();
		}
		return super.canChildScrollUp();
	}
}
