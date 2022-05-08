package com.ypyproductions.voicechanger.dataMng;

import com.ypyproductions.voicechanger.constants.IVoiceChangerConstants;
import com.ypyproductions.voicechanger.object.EffectObject;
import com.ypyproductions.voicechanger.soundMng.SoundManager;

import java.util.ArrayList;


public class TotalDataManager implements IVoiceChangerConstants {

	public static final String TAG = TotalDataManager.class.getSimpleName();

	private static TotalDataManager totalDataManager;
	private ArrayList<EffectObject> listEffectObjects;

	public static TotalDataManager getInstance() {
		if (totalDataManager == null) {
			totalDataManager = new TotalDataManager();
		}
		return totalDataManager;
	}

	private TotalDataManager() {

	}

	public void onDestroy() {
		if (listEffectObjects != null) {
			listEffectObjects.clear();
			listEffectObjects = null;
		}
//		if (listSongObjects != null) {
//			listSongObjects.clear();
//			listSongObjects = null;
//		}
		try {
			SoundManager.getInstance().releaseSound();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		totalDataManager = null;
	}

	public ArrayList<EffectObject> getListEffectObjects() {
		return listEffectObjects;
	}

	public void setListEffectObjects(ArrayList<EffectObject> listEffectObjects) {
		this.listEffectObjects = listEffectObjects;
	}
	
	public void onResetState(){
		if(listEffectObjects!=null && listEffectObjects.size()>0){
			for(EffectObject mEffectObject:listEffectObjects){
				mEffectObject.setPlaying(false);
			}
		}
	}
	
	
//	public ArrayList<SongObject> getListSongObjects() {
//		return listSongObjects;
//	}
//
//	public void setListSongObjects(ArrayList<SongObject> listSongObjects) {
//		this.listSongObjects = listSongObjects;
//	}
//	
//	public void addSongObjects(final Context mContext, SongObject mSongObject){
//		if(listSongObjects!=null && mSongObject!=null){
//			listSongObjects.add(mSongObject);
//			DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
//				@Override
//				public void onAction() {
//					saveSongs(mContext);
//				}
//			});
//		}
//	}
//
//	public synchronized void saveSongs(Context mContext) {
//		if (!ApplicationUtils.hasSDcard()) {
//			return;
//		}
//		File mFile = new File(Environment.getExternalStorageDirectory(),mContext.getPackageName());
//		if (!mFile.exists()) {
//			mFile.mkdirs();
//		}
//		if (listSongObjects != null && listSongObjects.size() > 0) {
//			JSONArray mJsArray = new JSONArray();
//			for (SongObject mSongObject : listSongObjects) {
//				mJsArray.put(mSongObject.toJson());
//			}
//			DBLog.d(TAG, "=============>saveSongs=" + mJsArray.toString());
//			IOUtils.writeString(mFile.getAbsolutePath(), FILE_SAVE, mJsArray.toString());
//			return;
//		}
//		IOUtils.writeString(mFile.getAbsolutePath(), FILE_SAVE, "");
//	}

}
