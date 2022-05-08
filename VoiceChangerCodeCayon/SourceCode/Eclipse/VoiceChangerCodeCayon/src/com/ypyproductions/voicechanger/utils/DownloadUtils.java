package com.ypyproductions.voicechanger.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DownloadUtils
 * @author       DOBAO
 * @Email        dotrungbao@gmail.com
 * @Skype        baopfiev_k50
 * @Date         Aug 12, 2013
 * @Packagename  com.dobao.utils.net
 */

public class DownloadUtils {

	private static final Pattern URI_FILENAME_PATTERN = Pattern.compile("[^/]+$");
	private static final String TAG = DownloadUtils.class.getSimpleName();

	public static String getFileName(String mUrl) {
		Matcher mMatcher = URI_FILENAME_PATTERN.matcher(mUrl);
		if (!mMatcher.find()) {
			throw new IllegalArgumentException("uri");
		}
		return mMatcher.group();
	}

	public static String downloadString(String url){
		InputStream is = null;
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			 int response = conn.getResponseCode();
			 DBLog.d(TAG, "The response is: " + response);
			 if(response==HttpURLConnection.HTTP_OK){
				 is=conn.getInputStream();
				 return convertStreamToString(is);
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream download(String url){
		InputStream is = null;
		try {
			URL mUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			
			int response = conn.getResponseCode();
			DBLog.d(TAG, "The response is: " + response);
			if (response == HttpURLConnection.HTTP_OK) {
				is=conn.getInputStream();
				return is;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convertStreamToString(InputStream is){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
