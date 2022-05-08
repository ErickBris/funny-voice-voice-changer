package com.ypyproductions.voicechanger.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

import com.ypyproductions.voicechanger.R;

public class DateTimeUtils {
	
	public static final int TIME_VIET_NAM=1;
	public static final int TIME_ENGLISH=2;
	public static final String[] DAY_ENGLISH={"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
	public static final String[] MONTH_VIET_NAM={"T1","T2","T3","T4","T5","T6","T7","T8"
												,"T9","T10","T11","T12"};
	
	public static final String[] MONTH_ENGLISH={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug"
											,"Sep","Oct","Nov","Dec"};
	
	public static final String TAG = DateTimeUtils.class.getSimpleName();
	
	public static Date getDateFromString(String mData, String mPattern){
		SimpleDateFormat  format = new SimpleDateFormat(mPattern);  
		try {  
		    Date date = format.parse(mData);
		    return date;
		} catch (ParseException e) {  
		    e.printStackTrace();  
		}
		return null;
	}
	
	
	
	public static String convertDateToString(Date mDate,String mPattern){
		if(mDate==null || mPattern==null){
			return null;
		}
		String mStrDate =DateFormat.format(mPattern, mDate.getTime()).toString();
		return mStrDate;
	}
	
	/**
	 * get time base on kindTime
	 * @param kindTime (1:VietName 2:English)
	 * @return string of time
	 */
	public static String getFullDate(){
		Calendar c = Calendar.getInstance(); 
		String minutes = String.valueOf(c.get(Calendar.MINUTE));
		if(minutes.length()==1){
			minutes="0"+minutes;
		}
		String hours = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		if(hours.length()==1){
			hours="0"+hours;
		}
		String year = String.valueOf(c.get(Calendar.YEAR));
		int indexMonths =c.get(Calendar.MONTH);
		int indexDay = c.get(Calendar.DAY_OF_WEEK);
		
		String months=MONTH_ENGLISH[indexMonths];
		String day=DAY_ENGLISH[indexDay-1];
		
		String mDayOfMonths = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if(mDayOfMonths.length()==1){
			mDayOfMonths="0"+mDayOfMonths;
		}
		String totalString = day+" "+mDayOfMonths+"-"+months+"-"+year+" "+hours+":"+minutes;
		return totalString;
		
	}
	
	public static String getStringDate(Date mDate){
		Calendar c = Calendar.getInstance(); 
		c.setTime(mDate);
		String year = String.valueOf(c.get(Calendar.YEAR));
		
		int indexMonths =c.get(Calendar.MONTH);
		int indexDay = c.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek =DAY_ENGLISH[indexDay-1];
		
		String dayOfMonths =String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String month=MONTH_ENGLISH[indexMonths];
		String totalString = dayOfWeek+", "+month+" "+dayOfMonths+", "+year;
		return totalString;
	}
	
	public static String getStringHours(Date mDate){
		Calendar c = Calendar.getInstance(); 
		c.setTime(mDate);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String min = String.valueOf(c.get(Calendar.MINUTE));
		
		String mPivot="";
		if (c.get(Calendar.AM_PM) == Calendar.AM){
			mPivot = "AM";
		}
	    else if (c.get(Calendar.AM_PM) == Calendar.PM){
	    	mPivot = "PM";		
	    }
		if(hour.length()==1){
			hour="0"+hour;
		}
		if(min.length()==1){
			min="0"+min;
		}
		String totalString = hour+":"+min+" "+mPivot;
		return totalString;
	}
	
	public static String getCurrentDate(String mPattern){
		Date mDate = new Date();
		String mStrDate =DateFormat.format(mPattern, mDate.getTime()).toString();
		return mStrDate;
	}
	
	public static String convertMilliToStrDate(long mCurrentDate, String mPattern){
		if(mCurrentDate>0){
			String mStrDate =DateFormat.format(mPattern, mCurrentDate).toString();
			return mStrDate;
		}
		return null;
	}
	
	public static String getStringTimeAgo(Context mContext, long second) {
		double minutes = second / 60f;
		if (second < 5) {
			return mContext.getString(R.string.title_just_now);
		}
		else if (second < 60) {
			return String.valueOf(second) + " " + mContext.getString(R.string.title_second_ago);
		}
		else if (second < 120) {
			return mContext.getString(R.string.title_a_minute_ago);
		}
		else if (minutes < 60) {
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_minute_ago);
		}
		else if (minutes < 120) {
			return mContext.getString(R.string.title_a_hour_ago);
		}
		else if (minutes < 24 * 60) {
			minutes = Math.floor(minutes / 60);
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_hour_ago);
		}
		else if (minutes < 24 * 60 * 2) {
			return mContext.getString(R.string.title_yesterday);
		}
		else if (minutes < 24 * 60 * 7) {
			minutes = Math.floor(minutes / (60 * 24));
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_day_ago);
		}
		else if (minutes < 24 * 60 * 14) {
			return mContext.getString(R.string.title_last_week);
		}
		else if (minutes < 24 * 60 * 31) {
			minutes = Math.floor(minutes / (60 * 24 * 7));
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_weeks_ago);
		}
		else if (minutes < 24 * 60 * 61) {
			return mContext.getString(R.string.title_last_month);
		}
		else if (minutes < 24 * 60 * 365.25) {
			minutes = Math.floor(minutes / (60 * 24 * 30));
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_month_ago);
		}
		else if (minutes < 24 * 60 * 731) {
			return mContext.getString(R.string.title_last_year);
		}
		else if (minutes > 24 * 60 * 731) {
			minutes = Math.floor(minutes / (60 * 24 * 365));
			return String.valueOf((int) minutes) + " " + mContext.getString(R.string.title_year_ago);
		}
		return mContext.getString(R.string.title_unknown);
	}
}
