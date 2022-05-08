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
import com.ypyproductions.voicechanger.object.EffectObject;
import java.util.ArrayList;

/**
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:TemplateChangeTheme
 * @Date:Aug 4, 2015
 */
public class EffectAdapter extends DBBaseAdapter {

	public static final String TAG = EffectAdapter.class.getSimpleName();
	private OnEffectListener onEffectListener;
	private Typeface mTypeface;

	public EffectAdapter(Activity mContext, ArrayList<? extends Object> listObjects, Typeface mTypeface) {
		super(mContext, listObjects);
		this.mTypeface= mTypeface;
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
			convertView = mInflater.inflate(R.layout.item_effects, null);
			convertView.setTag(mHolder);
		}
		else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_name_effect);
		mHolder.mBtnPlay = (Button) convertView.findViewById(R.id.btn_play);
		mHolder.mBtnSave = (Button) convertView.findViewById(R.id.btn_save);
		
		mHolder.mTvName.setTypeface(mTypeface);
		
		final EffectObject mItemObject = (EffectObject) mListObjects.get(position);
		mHolder.mBtnPlay.setBackgroundResource(mItemObject.isPlaying()?R.drawable.pause:R.drawable.play);
		mHolder.mTvName.setText(mItemObject.getName());

		mHolder.mBtnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onEffectListener != null) {
					onEffectListener.onPlayEffect(mItemObject);
				}
			}
		});
		mHolder.mBtnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onEffectListener != null) {
					onEffectListener.onShareEffect(mItemObject);
				}
			}
		});


		return convertView;
	}

	private static class ViewHolder {
		public TextView mTvName;
		public Button mBtnPlay;
		public Button mBtnSave;
	}

	public interface OnEffectListener {
		public void onPlayEffect(EffectObject mEffectObject);
		public void onShareEffect(EffectObject mEffectObject);
	}

	public void setOnEffectListener(OnEffectListener onListItemListener) {
		this.onEffectListener = onListItemListener;
	}

}
