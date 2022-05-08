package com.ypyproductions.voicechanger.utils;

import android.content.Context;

public class ResourceUtils {
	
	public static final int RESOURCE_ERROR=0;
	private static final String TAG = ResourceUtils.class.getSimpleName();
	
	public static int getIdStringByName(Context  mContext,String mName){
		if(mName==null){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		if(mName.equals("")){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		String mPackagename = mContext.getPackageName();
		int id = mContext.getResources().getIdentifier(mName, "string", mPackagename);
		if(id==0){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
		}
		return id;
		
	}
	public static int getIdMenuByName(Context  mContext,String mName){
		if(mName==null){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		if(mName.equals("")){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		String mPackagename = mContext.getPackageName();
		int id = mContext.getResources().getIdentifier(mName, "menu", mPackagename);
		if(id==0){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
		}
		return id;
		
	}
	public static int getIdStringArrayByName(Context  mContext,String mName){
		if(mName==null){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		if(mName.equals("")){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
			return RESOURCE_ERROR;
		}
		String mPackagename = mContext.getPackageName();
		int id = mContext.getResources().getIdentifier(mName, "string-array", mPackagename);
		if(id==0){
			DBLog.e(TAG, "------>RESOURCE "+mName+" error");
		}
		return id;
		
	}
}
