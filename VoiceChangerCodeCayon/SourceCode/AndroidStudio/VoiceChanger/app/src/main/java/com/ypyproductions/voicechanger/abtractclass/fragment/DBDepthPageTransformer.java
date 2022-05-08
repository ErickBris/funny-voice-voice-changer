package com.ypyproductions.voicechanger.abtractclass.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * DepthPageTransformer.java
 * 
 * @author :DOBAO
 * @Email :dotrungbao@gmail.com
 * @Skype :baopfiev_k50
 * @Phone :+84983028786
 * @Date :May 25, 2014
 * @project :WhereMyLocation
 * @Package :com.ypyproductions.abtractclass
 */
public class DBDepthPageTransformer implements ViewPager.PageTransformer {

	public static final String TAG = DBDepthPageTransformer.class.getSimpleName();
	public static final float MIN_SCALE = 0.75f;

	private float mMinScale = MIN_SCALE;

	public DBDepthPageTransformer(float mMinScale) {
		this.mMinScale = mMinScale;
	}

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();

		if (position < -1) {
			view.setAlpha(0);
		}
		else if (position <= 0) {
			view.setAlpha(1);
			view.setTranslationX(0);
			view.setScaleX(1);
			view.setScaleY(1);
		}
		else if (position <= 1) {
			view.setAlpha(1 - position);
			view.setTranslationX(pageWidth * -position);
			float scaleFactor = mMinScale + (1 - mMinScale) * (1 - Math.abs(position));
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
		}
		else {
			view.setAlpha(0);
		}
	}

}
