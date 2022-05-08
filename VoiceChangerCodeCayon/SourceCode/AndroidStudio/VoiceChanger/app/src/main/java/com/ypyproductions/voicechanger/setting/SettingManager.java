package com.ypyproductions.voicechanger.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 *
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:TemplateChangeTheme
 * @Date:Aug 4, 2015 
 *
 */
public class SettingManager implements ISettingConstants {

	public static final String TAG = SettingManager.class.getSimpleName();

	public static final String DOBAO_SHARPREFS = "ypy_prefs";

	public static void saveSetting(Context mContext, String mKey, String mValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DOBAO_SHARPREFS, Context.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(mKey, mValue);
		editor.commit();
	}

	public static String getSetting(Context mContext, String mKey, String mDefValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DOBAO_SHARPREFS, Context.MODE_PRIVATE);
		return mSharedPreferences.getString(mKey, mDefValue);
	}

	public static boolean getFirstTime(Context mContext) {
		return Boolean.parseBoolean(getSetting(mContext, KEY_FIRST_TIME, "false"));
	}

	public static void setFirstTime(Context mContext, boolean mValue) {
		saveSetting(mContext, KEY_FIRST_TIME, String.valueOf(mValue));
	}

	public static void setOnline(Context mContext, boolean mValue){
		saveSetting(mContext, KEY_IS_ONLINE, String.valueOf(mValue));
	}
	
	public static boolean getOnline(Context mContext){
		return Boolean.parseBoolean(getSetting(mContext, KEY_IS_ONLINE, "false"));
	}
	
	public static int getCurrentAccentColor(Context mContext){
		return Integer.parseInt(getSetting(mContext, KEY_COLOR_ACCENT, "0"));
	}
	
	public static void setCurrentAccentColor(Context mContext, int mValue){
		saveSetting(mContext, KEY_COLOR_ACCENT, String.valueOf(mValue));
	}
	
	public static int getCurrentMainColor(Context mContext){
		return Integer.parseInt(getSetting(mContext, KEY_COLOR_MAIN, "0"));
	}
	
	public static void setCurrentMainColor(Context mContext, int mValue){
		saveSetting(mContext, KEY_COLOR_MAIN, String.valueOf(mValue));
	}
	
	
}
