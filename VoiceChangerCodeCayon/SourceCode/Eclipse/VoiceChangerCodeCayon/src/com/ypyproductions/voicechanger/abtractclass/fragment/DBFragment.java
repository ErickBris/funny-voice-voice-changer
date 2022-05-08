package com.ypyproductions.voicechanger.abtractclass.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypyproductions.voicechanger.utils.StringUtils;

public abstract class DBFragment extends Fragment implements IDBFragmentConstants{
	
	public static final String TAG = DBFragment.class.getSimpleName();
	
	public View mRootView;
	private boolean isExtractData;

	public String mNameFragment;
	public int mIdFragment;
	private boolean isAllowFindViewContinous;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = onInflateLayout(inflater,container,savedInstanceState);
		return mRootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!isExtractData) {
			isExtractData = true;
			onExtractData();
			findView();
		}
		else{
			if(isAllowFindViewContinous){
				findView();
			}
		}
	}
	public abstract View onInflateLayout(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState);
	public abstract void findView();
	
	public void onExtractData(){
		Bundle args = getArguments();
		if (args != null) {
			mNameFragment = args.getString(KEY_NAME_FRAGMENT);
			mIdFragment = args.getInt(KEY_ID_FRAGMENT);
		}
	}
	
	public void backToHome(FragmentActivity mContext) {
		FragmentTransaction mFragmentTransaction = null;
		FragmentManager mFragmentManager = mContext.getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.remove(this);

		Fragment mFragmentHome = getFragmentHome(mContext);
		if(mFragmentHome!=null){
			mFragmentTransaction.show(mFragmentHome);
		}
		mFragmentTransaction.commit();
	}

	public boolean isAllowFindViewContinous() {
		return isAllowFindViewContinous;
	}

	public void setAllowFindViewContinous(boolean isAllowFindViewContinous) {
		this.isAllowFindViewContinous = isAllowFindViewContinous;
	}
	
	
	public Fragment getFragmentHome(FragmentActivity mContext){
		Fragment mFragmentHome=null;
		if(mIdFragment>0){
			mFragmentHome = mContext.getSupportFragmentManager().findFragmentById(mIdFragment);
		}
		else{
			if(!StringUtils.isEmptyString(mNameFragment)){
				mFragmentHome = mContext.getSupportFragmentManager().findFragmentByTag(mNameFragment);
			}
		}
		return mFragmentHome;
	}
	
}
