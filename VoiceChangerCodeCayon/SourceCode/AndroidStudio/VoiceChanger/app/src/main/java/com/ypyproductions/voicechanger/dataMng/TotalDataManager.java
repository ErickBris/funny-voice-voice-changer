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


}
