package com.ypyproductions.voicechanger.abtractclass;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ypyproductions.voicechanger.abtractclass.fragment.DBFragmentAdapter;

public class DBSlidingTripAdapter extends DBFragmentAdapter{

	private ArrayList<String> mListTitles;

	public DBSlidingTripAdapter(FragmentManager fm, ArrayList<Fragment> listFragments,ArrayList<String> mListTitles) {
		super(fm, listFragments);
		this.mListTitles=mListTitles;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		if(mListTitles!=null && mListTitles.size()>0){
			if(position<mListTitles.size() && position>=0){
				return mListTitles.get(position);
			}
		}
		return super.getPageTitle(position);
	}
}
