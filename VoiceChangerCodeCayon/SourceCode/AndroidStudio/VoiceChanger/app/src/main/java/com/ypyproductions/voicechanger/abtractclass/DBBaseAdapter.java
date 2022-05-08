package com.ypyproductions.voicechanger.abtractclass;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;

import com.ypyproductions.voicechanger.utils.ResolutionUtils;

/**
 * 
 * DBBaseAdapter.java
 * 
 * @Author DoBao
 * @Email baodt@hanet.vn
 * @Phone +84983028786
 * @Skype baopfiev_k50
 * @Date May 13, 2014
 * @Project WhereMyLocation
 * @Package com.ypyproductions.baseadapter
 * @Copyright � 2014 Softwares And Network Solutions HANET Co., Ltd
 */
public abstract class DBBaseAdapter extends BaseAdapter {

	public static final String TAG = DBBaseAdapter.class.getSimpleName();
	public static final long ANIM_DEFAULT_SPEED = 800L;
	public static final long ANIM_DEFAULT_MIN_SPEED = 300L;

	public Context mContext;
	public ArrayList<? extends Object> mListObjects;

	private SparseBooleanArray mPositionsMapper;
	private Interpolator interpolator;
	private boolean isAnimate;

	private int screenWidth;
	private int screenHeight;

	public DBBaseAdapter(Activity mContext, ArrayList<? extends Object> listObjects) {
		this.mContext = mContext;
		this.mListObjects = listObjects;
		this.mPositionsMapper = new SparseBooleanArray(listObjects.size());
		int[] mRes = ResolutionUtils.getDeviceResolution(mContext);
		if (mRes != null) {
			this.screenWidth = mRes[0];
			this.screenHeight = mRes[1];
		}
	}

	@Override
	public int getCount() {
		return mListObjects != null ? mListObjects.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (mListObjects != null && mListObjects.size() > 0) {
			int size = mListObjects.size();
			if (0 < position && position < size) {
				return mListObjects.get(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isAnimate) {
			return getAnimatedView(position, convertView, parent);
		}
		else {
			return getNormalView(position, convertView, parent);
		}
	}

	public ArrayList<? extends Object> getListObjects() {
		return mListObjects;
	}

	public void setListObjects(ArrayList<? extends Object> mListObjects, boolean isDestroyOldData) {
		if (mListObjects != null) {
			if (this.mListObjects != null && isDestroyOldData) {
				this.mListObjects.clear();
				this.mListObjects = null;
			}
			this.mPositionsMapper = null;
			this.mPositionsMapper = new SparseBooleanArray(mListObjects.size());
			this.mListObjects = mListObjects;
			this.notifyDataSetChanged();
		}
	}

	public Interpolator getInterpolator() {
		return interpolator;
	}

	public void setInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
	}

	public boolean isAnimate() {
		return isAnimate;
	}

	public void setAnimate(boolean isAnimate) {
		this.isAnimate = isAnimate;
		this.notifyDataSetChanged();
	}

	public abstract View getAnimatedView(int position, View convertView, ViewGroup parent);

	public abstract View getNormalView(int position, View convertView, ViewGroup parent);

	public void onDestroy(boolean isDestroyAll) {
		if (mListObjects != null) {
			mListObjects.clear();
			if (isDestroyAll) {
				mListObjects = null;
			}
		}
	}

	public void addPositionMapper(int pos, boolean value) {
		mPositionsMapper.put(pos, value);
	}

	public boolean checkPositionMapper(int pos) {
		return mPositionsMapper.get(pos);
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

}
