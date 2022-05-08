package com.ypyproductions.voicechanger;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.DirectionUtils;
import com.ypyproductions.voicechanger.utils.StringUtils;
import com.ypyproductions.voicechanger.view.DBShimmerFrameLayout;
import com.ypyproductions.voicechanger.wave.WaveWriter;

import java.io.File;
import java.io.IOException;

/**
 *
 *
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:TemplateChangeTheme
 * @Date:Aug 4, 2015
 *
 */

public class RecordActivity extends DBFragmentActivity implements OnClickListener {

	public static final String TAG = RecordActivity.class.getSimpleName();

	private AdView adView;
	private TextView mTvInfoRecord;

	private Button mBtnRecord;
	private Handler mHandler = new Handler();
	private long currentTime = 0;
	private boolean isRecording;
	private Button mBtnBack;
	private RelativeLayout mLayoutSave;
	private MediaPlayer mPlayer;
	private AudioRecord mAudioRecord;

	private int mBufferSize;

	private String mFileName;

	private Thread mRecordingThread;

	private Button mBtnPlay;
	private boolean isPlaying;

	private WaveWriter mWaveWriter;

	private DBShimmerFrameLayout mLayoutShimmer;
	private TextView mTvHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording);

		mTvInfoRecord = (TextView) findViewById(R.id.tv_record);
		mTvInfoRecord.setTypeface(mTypefaceAvenir);

		mTvHeader = (TextView) findViewById(R.id.tv_header);
		mTvHeader.setTypeface(mTypefaceLogo);

		mLayoutShimmer=(DBShimmerFrameLayout) findViewById(R.id.layout_record);
		mLayoutShimmer.setDuration(DURATION_SHIMMER);
		mLayoutShimmer.startShimmerAnimation();

		mBtnRecord = (Button) findViewById(R.id.btn_record);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnPlay = (Button) findViewById(R.id.btn_play);
		mLayoutSave = (RelativeLayout) findViewById(R.id.layout_stored_data);

		mBufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

		setUpBlurLayout();
		setupLayoutAdmob();
	}

	private void setupLayoutAdmob() {
		boolean b=SHOW_ADVERTISEMENT;
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if (ApplicationUtils.isOnline(this) && b) {
			adView = new AdView(this);
			adView.setAdUnitId(ADMOB_ID_BANNER);
			adView.setAdSize(AdSize.SMART_BANNER);

			layout.addView(adView);
			AdRequest mAdRequest = new AdRequest.Builder().addTestDevice("51F0A3F4C13F05DD49DE0D71F2B369FB").build();
			adView.loadAd(mAdRequest);
			return;
		}
		layout.setVisibility(View.GONE);
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mLayoutParams.bottomMargin=getResources().getDimensionPixelOffset(R.dimen.dialog_margin);
		mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		findViewById(R.id.layout_stored_data).setLayoutParams(mLayoutParams);

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
		if(mLayoutShimmer!=null){
			mLayoutShimmer.stopShimmerAnimation();
		}
		stopAudioRecording(false);
		stopAudioPlaying();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToHome();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void backToHome() {
		if (!isRecording) {
			deleteMainFile();
			Intent mIntent = new Intent(this, MainActivity.class);
			DirectionUtils.changeActivity(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_record:
			if (!isRecording) {
				startRecord();
			}
			else {
				stopRecord();
			}
			break;
		case R.id.btn_play:
			if(!isPlaying){
				startAudioPlaying();
			}
			else{
				pauseAudioPlaying();
			}
			break;
		case R.id.btn_next:
			mSoundMng.play(this, R.raw.click);
			if(!StringUtils.isEmptyString(mFileName)){
				Intent mIntent = new Intent(this, EffectActivity.class);
				mIntent.putExtra(KEY_PATH_AUDIO, mFileName);
				DirectionUtils.changeActivity(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent);
			}
			break;
		case R.id.btn_back:
			mSoundMng.play(this, R.raw.click);
			backToHome();
			break;
		default:
			break;
		}
	}

	private void startRecord() {
		if (!isRecording) {
			stopAudioPlaying();
			mBtnPlay.setBackgroundResource(R.drawable.play);
			
			isRecording = true;
			currentTime = 0;
			mTvInfoRecord.setText("00:00");
			mBtnRecord.setBackgroundResource(R.drawable.stop);
			mLayoutSave.setVisibility(View.INVISIBLE);
			mBtnBack.setVisibility(View.INVISIBLE);
			displayTime();
			startAudioRecording();
		}
	}

	private void stopRecord() {
		if (isRecording) {
			mHandler.removeCallbacksAndMessages(null);
			isRecording = false;
			mTvInfoRecord.setText(R.string.info_start_record);
			mBtnRecord.setBackgroundResource(R.drawable.record);
			if (currentTime > 0) {
				mLayoutSave.setVisibility(View.VISIBLE);

			}
			mBtnBack.setVisibility(View.VISIBLE);
			stopAudioRecording(true);
		}
	}

	private void displayTime() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (currentTime < MAX_TIME) {
					currentTime = currentTime + 1000;
					long minius = (currentTime / 1000) / 60;
					long seconds = (currentTime / 1000) % 60;
					String strMinius = String.valueOf(minius);
					String strSeconds = String.valueOf(seconds);
					if (strMinius.length() == 1) {
						strMinius = "0" + strMinius;
					}
					if (strSeconds.length() == 1) {
						strSeconds = "0" + strSeconds;
					}
					mTvInfoRecord.setText(strMinius + ":" + strSeconds);
					displayTime();
				}
				else {
					stopRecord();
				}
			}
		}, 1000);
	}

	private void startAudioRecording() {
		if (mBufferSize <= 0) {
			stopRecord();
			return;
		}
		deleteMainFile();

		mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mBufferSize);
		mFileName =getFilename();
		mWaveWriter = new WaveWriter(new File(mFileName), 44100, 1, 16);
		try {
			mWaveWriter.createWaveFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
			mAudioRecord.startRecording();
			mRecordingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					writeAudioDataToFile();
				}
			});
			mRecordingThread.start();
		}
		else {
			showToast(R.string.info_record_error);
			stopRecord();
		}
	}

	private void writeAudioDataToFile() {
		short data[] = new short[SIMPLE_RECORDER_BUFFER_SIZE];
		int numberSample = 0;
		if (mWaveWriter != null) {
			while (isRecording) {
				numberSample = mAudioRecord.read(data, 0, SIMPLE_RECORDER_BUFFER_SIZE);
				if (AudioRecord.ERROR_INVALID_OPERATION != numberSample) {
					try {
						mWaveWriter.write(data, 0, numberSample);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void stopAudioRecording(boolean isNeedToSave) {
		if (mAudioRecord != null) {
			try {
				if (mRecordingThread != null) {
					mRecordingThread.interrupt();
					mRecordingThread = null;
				}
				mAudioRecord.stop();
				mAudioRecord.release();
				mAudioRecord = null;
				try {
					mWaveWriter.closeWaveFile();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteMainFile() {
		if (!StringUtils.isEmptyString(mFileName)) {
			try {
				File file = new File(mFileName);
				file.delete();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, NAME_FOLDER_RECORD);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath() + "/record"+ AUDIO_RECORDER_FILE_EXT_WAV);
	}


	private void startAudioPlaying() {
		if (StringUtils.isEmptyString(mFileName)) {
			return;
		}
		if (!isPlaying) {
			if (mPlayer == null) {
				try {
					mPlayer = new MediaPlayer();
					mPlayer.setDataSource(mFileName);
					mPlayer.prepare();
					mPlayer.start();
					mBtnPlay.setBackgroundResource(R.drawable.pause);
					isPlaying=true;
					mPlayer.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							stopAudioPlaying();
							mBtnPlay.setBackgroundResource(R.drawable.play);
						}
					});
					mPlayer.setOnErrorListener(new OnErrorListener() {
						@Override
						public boolean onError(MediaPlayer mp, int what, int extra) {
							stopAudioPlaying();
							mBtnPlay.setBackgroundResource(R.drawable.play);
							return false;
						}
					});
				}
				catch (IOException e) {
					DBLog.d(TAG, "prepare() failed");
				}
			}
			else{
				if(!mPlayer.isPlaying()){
					mPlayer.start();
					isPlaying=true;
					mBtnPlay.setBackgroundResource(R.drawable.pause);
				}
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopRecord();
		pauseAudioPlaying();
	}
	
	private void pauseAudioPlaying(){
		if(isPlaying){
			if(mPlayer!=null && mPlayer.isPlaying()){
				mPlayer.pause();
				isPlaying=false;
				mBtnPlay.setBackgroundResource(R.drawable.play);
			}
		}
	}

	private void stopAudioPlaying() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		isPlaying=false;
	}

}
