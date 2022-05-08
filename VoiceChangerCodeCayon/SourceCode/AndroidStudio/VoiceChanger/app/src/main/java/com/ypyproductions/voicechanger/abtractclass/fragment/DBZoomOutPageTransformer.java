package com.ypyproductions.voicechanger.abtractclass.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * ZoomOutPageTransformer.java
 * 
 * @author :DOBAO
 * @Email :dotrungbao@gmail.com
 * @Skype :baopfiev_k50
 * @Phone :+84983028786
 * @Date :May 25, 2014
 * @project :WhereMyLocation
 * @Package :com.ypyproductions.abtractclass.fragment
 */
public class DBZoomOutPageTransformer implements ViewPager.PageTransformer {

	public static final float MIN_SCALE = 0.85f;
	public static final float MIN_ALPHA = 0.5f;

	private float mMinScale = MIN_SCALE;
	private float mMinAlpha = MIN_ALPHA;

	public DBZoomOutPageTransformer(float mMinScale, float mMinAlpha) {
		this.mMinScale = mMinScale;
		this.mMinAlpha = mMinAlpha;
	}

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) {
			view.setAlpha(0);
		}
		else if (position <= 1) {
			float scaleFactor = Math.max(mMinScale, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			}
			else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			view.setAlpha(mMinAlpha + (scaleFactor - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));

		}
		else {
			view.setAlpha(0);
		}
	}

}
