package com.ypyproductions.voicechanger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ypyproductions.voicechanger.R;
import com.ypyproductions.voicechanger.abtractclass.DBBaseAdapter;
import com.ypyproductions.voicechanger.constants.IVoiceChangerConstants;
import com.ypyproductions.voicechanger.object.SongObject;
import com.ypyproductions.voicechanger.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:TemplateChangeTheme
 * @Date:Aug 4, 2015
 */
public class GalleryAdapter extends DBBaseAdapter implements IVoiceChangerConstants {

	public static final String TAG = GalleryAdapter.class.getSimpleName();
	private OnGalleryListener onGalleryListener;
	private Typeface mTypeface;
	private Date mCurrentDate;

	public GalleryAdapter(Activity mContext, ArrayList<? extends Object> listObjects, Typeface mTypeface) {
		super(mContext, listObjects);
		this.mTypeface= mTypeface;
		
		mCurrentDate = new Date();
	}

	@Override
	public View getAnimatedView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public View getNormalView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;
		LayoutInflater mInflater;
		if (convertView == null) {
			mHolder = new ViewHolder();
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_gallery, null);
			convertView.setTag(mHolder);
		}
		else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_name_effect);
		mHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
		mHolder.mBtnPlay = (Button) convertView.findViewById(R.id.btn_play);
		mHolder.mBtnShare = (Button) convertView.findViewById(R.id.btn_share);
		
		mHolder.mTvName.setTypeface(mTypeface);
		mHolder.mTvTime.setTypeface(mTypeface);
		
		final SongObject mItemObject = (SongObject) mListObjects.get(position);
		
		mHolder.mBtnPlay.setBackgroundResource(mItemObject.isPlaying()?R.drawable.pause:R.drawable.play);
		mHolder.mTvName.setText(mItemObject.getName());
		
		Date mDate = mItemObject.getDate();
		if(mDate!=null){
			mHolder.mTvTime.setVisibility(View.VISIBLE);
			long deltaSecond = (mCurrentDate.getTime() - mDate.getTime()) / 1000;
			String timeAgo = DateTimeUtils.getStringTimeAgo(mContext,deltaSecond);
			mHolder.mTvTime.setText(timeAgo);
		}
		else{
			mHolder.mTvTime.setVisibility(View.GONE);
		}

		mHolder.mBtnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onGalleryListener != null) {
					onGalleryListener.onPlayEffect(mItemObject);
				}
			}
		});
		mHolder.mBtnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onGalleryListener != null) {
					onGalleryListener.onShareEffect(mItemObject);
				}
			}
		});


		return convertView;
	}

	private static class ViewHolder {
		public TextView mTvName;
		public TextView mTvTime;
		public Button mBtnPlay;
		public Button mBtnShare;
	}

	public interface OnGalleryListener {
		public void onPlayEffect(SongObject mEffectObject);
		public void onShareEffect(SongObject mEffectObject);
	}

	public void setOnGalleryListener(OnGalleryListener onListItemListener) {
		this.onGalleryListener = onListItemListener;
	}

}
