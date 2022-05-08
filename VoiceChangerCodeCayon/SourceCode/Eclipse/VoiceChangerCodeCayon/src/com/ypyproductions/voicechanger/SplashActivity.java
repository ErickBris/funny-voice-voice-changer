package com.ypyproductions.voicechanger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ypyproductions.voicechanger.constants.IVoiceChangerConstants;
import com.ypyproductions.voicechanger.dataMng.JsonParsingUtils;
import com.ypyproductions.voicechanger.object.EffectObject;
import com.ypyproductions.voicechanger.soundMng.SoundManager;
import com.ypyproductions.voicechanger.task.DBTask;
import com.ypyproductions.voicechanger.task.IDBTaskListener;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.DirectionUtils;
import com.ypyproductions.voicechanger.utils.IOUtils;
import com.ypyproductions.voicechanger.view.DBShimmerFrameLayout;

import java.util.ArrayList;

/**
 * SplashActivity
 * @author  :DOBAO
 * @Email   :dotrungbao@gmail.com
 * @Skype   :baopfiev_k50
 * @Phone   :+84983028786
 * @Date    :May 5, 2013
 * @project :IOnAuctions
 * @Package :com.auction.ionauctions
 */

public class SplashActivity extends DBFragmentActivity implements IVoiceChangerConstants{
	
	public static final String TAG=SplashActivity.class.getSimpleName();

	private Handler mHandler = new Handler();
	private TextView mTvVersion;
	private TextView mTvCopyright;
	private TextView mTvLogo;
	private DBTask mDBTask;
	private DBShimmerFrameLayout mLayoutShimmer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.activity_splash);
	    
	    this.mTvCopyright =(TextView) findViewById(R.id.tv_copyright);
	    this.mTvLogo =(TextView) findViewById(R.id.tv_logo);
		this.mTvLogo.setTypeface(mTypefaceLogo);

	    this.mTvVersion = (TextView) findViewById(R.id.tv_version);

		this.mTvVersion.setText(String.format(getString(R.string.info_version_format), ApplicationUtils.getVersionName(this)));
		SoundManager.getInstance().addSound(this, R.raw.click);

		this.mTvCopyright.setTypeface(mTypefaceLight);
		this.mTvVersion.setTypeface(mTypefaceLight);

		setUpBlurLayout();

		mLayoutShimmer=(DBShimmerFrameLayout) findViewById(R.id.layout_shimmer);
		mLayoutShimmer.setDuration(1500);
		mLayoutShimmer.startShimmerAnimation();

		DBLog.setDebug(DEBUG);

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startLoad();
			}
		}, 3000);
	}
	
	private void startLoad(){
		mDBTask = new DBTask(new IDBTaskListener() {

			public ArrayList<EffectObject> mListEffects;

			@Override
			public void onPreExcute() {

			}
			@Override
			public void onDoInBackground() {
				String data = IOUtils.readStringFromAssets(SplashActivity.this, "effects.dat");
				mListEffects = JsonParsingUtils.parsingListEffectObject(data);
				DBLog.d(TAG, "===============>Size="+mListEffects.size());
				if(mListEffects!=null && mListEffects.size()>0){
					mTotalMng.setListEffectObjects(mListEffects);
				}
//				String dataSongs = IOUtils.readString(SplashActivity.this, mFile.getAbsolutePath(), FILE_SAVE);
//				ArrayList<SongObject> listSongObjects = JsonParsingUtils.parsingListSongObject(dataSongs);
//				if(listSongObjects==null){
//					listSongObjects=new ArrayList<SongObject>();
//				}
//				TotalDataManager.getInstance().setListSongObjects(listSongObjects);
			}
			
			@Override
			public void onPostExcute() {
				if(mListEffects==null || mListEffects.size()==0){
					showToastWithLongTime(R.string.info_parsing_error);
					finish();
					return ;
				}
				Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
				DirectionUtils.changeActivity(SplashActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent);
			}
			
			
		});
		mDBTask.execute();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLayoutShimmer!=null){
			mLayoutShimmer.stopShimmerAnimation();
		}
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
