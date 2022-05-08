package com.ypyproductions.voicechanger.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * 
 * DirectionUtils.java
 * 
 * @Author DoBao
 * @Email baodt@hanet.vn
 * @Phone +84983028786
 * @Skype baopfiev_k50
 * @Date Mar 14, 2014
 * @Project WhereMyLocation
 * @Package com.ypyproductions.utils
 * @Copyright � 2014 Softwares And Network Solutions HANET Co., Ltd
 */
public class DirectionUtils {
	
	public static void changeActivity(Activity mActivity, int animIn, int animOut,boolean hasFinish, Intent mIntent){
		if(mActivity==null||mIntent==null){
			return;
		}
		mActivity.startActivity(mIntent);
		mActivity.overridePendingTransition(animIn, animOut);
		if(hasFinish){
			mActivity.finish();
		}
	}
}
