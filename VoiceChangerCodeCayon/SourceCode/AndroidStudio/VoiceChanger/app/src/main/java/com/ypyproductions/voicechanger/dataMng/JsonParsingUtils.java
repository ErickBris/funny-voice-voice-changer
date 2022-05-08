package com.ypyproductions.voicechanger.dataMng;

import com.ypyproductions.voicechanger.constants.IVoiceChangerConstants;
import com.ypyproductions.voicechanger.object.EffectObject;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParsingUtils implements IVoiceChangerConstants {

	public static final String TAG = JsonParsingUtils.class.getSimpleName();
	
	public static ArrayList<EffectObject> parsingListEffectObject(String data) {
		if (!StringUtils.isEmptyString(data)) {
			try {
				JSONArray mJsonArray =new JSONArray(data);
				int size = mJsonArray.length();
				if (size > 0) {
					ArrayList<EffectObject> mListEffectObjects = new ArrayList<EffectObject>();
					for (int i = 0; i < size; i++) {
						JSONObject mJs = mJsonArray.getJSONObject(i);
						String id = mJs.getString("id");
						String name = mJs.getString("name");
						int pitch =mJs.getInt("pitch") ;
						int rate =mJs.getInt("rate");
						boolean flanger =mJs.getBoolean("flanger");
						boolean isReverse =false;
						if(mJs.opt("reverse")!=null){
							isReverse=mJs.getBoolean("reverse");
						}
						boolean echo =false;
						if(mJs.opt("echo")!=null){
							echo=mJs.getBoolean("echo");
						}
						EffectObject mEffectObject = new EffectObject(id, name, pitch, rate);
						mEffectObject.setFlanger(flanger);
						mEffectObject.setReverse(isReverse);
						mEffectObject.setEcho(echo);
						
						mListEffectObjects.add(mEffectObject);
						
						JSONArray mJArray = mJs.getJSONArray("reverb");
						int len = mJArray.length();
						if(len>0){
							float[] reverb=new float[3];
							for(int j=0;j<3;j++){
								reverb[j]=(float) mJArray.getDouble(j);
							}
							mEffectObject.setReverb(reverb);
						}
						if(mJs.opt("eq")!=null){
							JSONArray mJArray1 = mJs.getJSONArray("eq");
							int len1 = mJArray1.length();
							if(len1>0){
								float[] eq=new float[3];
								for(int j=0;j<3;j++){
									eq[j]=(float) mJArray1.getDouble(j);
								}
								mEffectObject.setEq(eq);
							}
						}
						
					}
					DBLog.d(TAG, "===================>size effect ="+mListEffectObjects.size());
					return mListEffectObjects;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
//	public static ArrayList<SongObject> parsingListSongObject(String data) {
//		DBLog.d(TAG, "=======================>parsingListSongObjectString=" + data);
//		if (!StringUtils.isEmptyString(data)) {
//			try {
//				JSONArray mJsonArray =new JSONArray(data);
//				int size = mJsonArray.length();
//				if (size > 0) {
//					ArrayList<SongObject> mListEffectObjects = new ArrayList<SongObject>();
//					for (int i = 0; i < size; i++) {
//						JSONObject mJs = mJsonArray.getJSONObject(i);
//						String name = mJs.getString("name");
//						String date = mJs.getString("date");
//						
//						SongObject mSongObject = new SongObject(name, date);
//						mListEffectObjects.add(mSongObject);
//						
//					}
//					DBLog.d(TAG, "=======================>mListEffectObjects=" + mListEffectObjects.size());
//					return mListEffectObjects;
//				}
//			}
//			catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
}
