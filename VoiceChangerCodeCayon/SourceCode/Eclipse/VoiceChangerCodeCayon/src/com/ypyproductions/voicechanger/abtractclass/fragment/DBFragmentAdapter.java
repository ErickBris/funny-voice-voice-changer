package com.ypyproductions.voicechanger.abtractclass.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * DesignSeriesFragmentAdapter
 * 
 * @author DOBAO
 * @Email dotrungbao@gmail.com
 * @Skype baopfiev_k50
 * @Date Sep 19, 2013
 * @Packagename com.greenandcompanycreative.adapter
 */
public class DBFragmentAdapter extends FragmentPagerAdapter {

	public static final String TAG = DBFragmentAdapter.class.getSimpleName();

	private ArrayList<Fragment> listFragments;

	public DBFragmentAdapter(FragmentManager fm, ArrayList<Fragment> listFragments) {
		super(fm);
		this.listFragments = listFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return listFragments.get(position);
	}

	@Override
	public int getCount() {
		return listFragments.size();
	}

	@Override
	public void destroyItem(View pView, int pIndex, Object pObject) {
		try {
			((ViewPager) pView).removeView((View) pObject);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
