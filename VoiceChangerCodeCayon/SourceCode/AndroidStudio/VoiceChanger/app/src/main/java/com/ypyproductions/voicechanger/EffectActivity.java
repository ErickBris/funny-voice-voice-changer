package com.ypyproductions.voicechanger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.un4seen.bass.BASS;
import com.ypyproductions.materialdialogs.MaterialDialog;
import com.ypyproductions.voicechanger.adapter.EffectAdapter;
import com.ypyproductions.voicechanger.basseffect.DBMediaPlayer;
import com.ypyproductions.voicechanger.basseffect.IDBMediaListener;
import com.ypyproductions.voicechanger.dataMng.JsonParsingUtils;
import com.ypyproductions.voicechanger.dataMng.TotalDataManager;
import com.ypyproductions.voicechanger.object.EffectObject;
import com.ypyproductions.voicechanger.soundMng.SoundManager;
import com.ypyproductions.voicechanger.task.DBTask;
import com.ypyproductions.voicechanger.task.IDBCallback;
import com.ypyproductions.voicechanger.task.IDBTaskListener;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.DirectionUtils;
import com.ypyproductions.voicechanger.utils.IOUtils;
import com.ypyproductions.voicechanger.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;

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

public class EffectActivity extends DBFragmentActivity implements OnClickListener, EffectAdapter.OnEffectListener {

	public static final String TAG = EffectActivity.class.getSimpleName();

	private ListView mListViewEffects;

	private ArrayList<EffectObject> mListEffectObjects;

	private EffectAdapter mEffectApdater;

	private String mPathAudio;

	private TextView mTvHeader;

	private boolean isInit;

	private DBMediaPlayer mDBMedia;

	private String mNameExportVoice;

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_effects);
		Intent mIntent = getIntent();
		if (mIntent != null) {
			mPathAudio = mIntent.getStringExtra(KEY_PATH_AUDIO);
		}
		mListViewEffects = (ListView) findViewById(R.id.list_effects);

		mTvHeader = (TextView) findViewById(R.id.tv_header);
		mTvHeader.setTypeface(mTypefaceLogo);

		setUpBlurLayout();
		setupLayoutAdmob();

		if (!StringUtils.isEmptyString(mPathAudio)) {
			File mFile = new File(mPathAudio);
			if (mFile.exists() && mFile.isFile()) {
				setupInfo();
			}
			else {
				showToast("File not found exception");
				backToHome();
			}
		}
		else {
			backToHome();
		}

	}

	private void setupInfo(){
		mListEffectObjects = TotalDataManager.getInstance().getListEffectObjects();
		if (mListEffectObjects != null && mListEffectObjects.size() > 0) {
			mEffectApdater = new EffectAdapter(this, mListEffectObjects, mTypefaceAvenir);
			mEffectApdater.setOnEffectListener(this);
			mListViewEffects.setAdapter(mEffectApdater);
			onInitAudioDevice();
			createDBMedia();
		}
		else{
			startLoad(new IDBCallback() {
				@Override
				public void onAction() {
					setupInfo();
				}
			});
		}
	}

	private void startLoad(final IDBCallback mCallback){
		DBTask mDBTask = new DBTask(new IDBTaskListener() {

			public ArrayList<EffectObject> mListEffects;

			@Override
			public void onPreExcute() {
				showProgressDialog();
			}

			@Override
			public void onDoInBackground() {
				String data = IOUtils.readStringFromAssets(EffectActivity.this, "effects.dat");
				mListEffects = JsonParsingUtils.parsingListEffectObject(data);
				DBLog.d(TAG, "===============>Size=" + mListEffects.size());
				if (mListEffects != null && mListEffects.size() > 0) {
					mTotalMng.setListEffectObjects(mListEffects);
				}
			}

			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				if (mListEffects == null || mListEffects.size() == 0) {
					backToHome();
					return;
				}
                if(mCallback!=null){
                    mCallback.onAction();
                }
			}
		});
		mDBTask.execute();
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			mSoundMng.play(this, R.raw.click);
			backToHome();
			break;
		case R.id.btn_gallery:
			mSoundMng.play(this, R.raw.click);
			deleteMainFile();
			onDestroyMedia();
			Intent mIntent = new Intent(this, GalleryActivity.class);
			DirectionUtils.changeActivity(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent);
			break;
		default:
			break;
		}
	}

	private void deleteMainFile() {
		if (!StringUtils.isEmptyString(mPathAudio)) {
			try {
				File file = new File(mPathAudio);
				file.delete();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
	
	private void onDestroyMedia(){
		try{
			if (mDBMedia != null) {
				mDBMedia.releaseAudio();
			}
			BASS.BASS_PluginFree(0);
			BASS.BASS_Free();
			TotalDataManager.getInstance().onResetState();
		}
		catch(Exception e){
			e.printStackTrace();
		}

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
		deleteMainFile();
		onDestroyMedia();
		Intent mIntent = new Intent(this, RecordActivity.class);
		DirectionUtils.changeActivity(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
	}

	@Override
	public void onPlayEffect(EffectObject mEffectObject) {
		boolean isPlaying = mEffectObject.isPlaying();
		if (isPlaying) {
			mEffectObject.setPlaying(false);
			if (mDBMedia != null) {
				mDBMedia.pauseAudio();
			}
		}
		else {
			TotalDataManager.getInstance().onResetState();
			mEffectObject.setPlaying(true);
			if (mDBMedia != null) {
				mDBMedia.setReverse(mEffectObject.isReverse());
				mDBMedia.setAudioPitch(mEffectObject.getPitch());
				mDBMedia.setAudioRate(mEffectObject.getRate());
				mDBMedia.setAudioReverb(mEffectObject.getReverb());
				mDBMedia.setAudioEQ(mEffectObject.getEq());
				mDBMedia.setFlangerEffect(mEffectObject.isFlanger());
				mDBMedia.setAudioEcho(mEffectObject.isEcho());
				
				if (mDBMedia.isPlaying()) {
					if(!mEffectObject.isReverse()){
						mDBMedia.seekTo(0);
					}
					else{
						mDBMedia.seekTo(mDBMedia.getDuration());
					}
				}
				
				mDBMedia.startAudio();
			}
		}
		mEffectApdater.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mDBMedia != null) {
			resetStateAudio();
		}
	}

	@Override
	public void onShareEffect(final EffectObject mEffectObject) {
		if(mDBMedia!=null){
			resetStateAudio();
		}
		SoundManager.getInstance().play(this, R.raw.click);
		mNameExportVoice=String.format(FORMAT_NAME_VOICE, String.valueOf(System.currentTimeMillis()/1000));
		showDialogEnterName(new IDBCallback() {
			@Override
			public void onAction() {
				if (mDBMedia != null) {
					startSaveEffect(mEffectObject, new IDBCallback() {
						@Override
						public void onAction() {
							File file = new File(mPathAudio);
							File mDir = file.getParentFile();
							final File mOutPutFile = new File(mDir, mNameExportVoice);
							if (mOutPutFile.exists() && mOutPutFile.isFile()) {
								String mInfoSave = String.format(getString(R.string.info_save_voice), mOutPutFile.getAbsolutePath());
								showToast(mInfoSave);
								showDiaglogShare(mEffectObject);
							}
						}
					});

				}
			}
		});
	}

	private void resetStateAudio() {
		TotalDataManager.getInstance().onResetState();
		if (mEffectApdater != null) {
			mEffectApdater.notifyDataSetChanged();
		}
		if (mDBMedia!=null && mDBMedia.isPlaying()) {
			mDBMedia.pauseAudio();
		}
	}

	private void startSaveEffect(final EffectObject mEffectObject, final IDBCallback mDBCallback) {
		File file = new File(mPathAudio);
		final File mDir = file.getParentFile();
		final File mTempOutPutFile = new File(mDir, mNameExportVoice);

		final DBMediaPlayer mDBExportMedia = new DBMediaPlayer(mPathAudio);

		DBTask mDBTask = new DBTask(new IDBTaskListener() {

			@Override
			public void onPreExcute() {
				showProgressDialog();
			}

			@Override
			public void onDoInBackground() {
				if (mDBExportMedia != null) {
					boolean b = mDBExportMedia.initMediaToSave();
					if (b) {
						mDBExportMedia.setReverse(mEffectObject.isReverse());
						mDBExportMedia.setAudioPitch(mEffectObject.getPitch());
						mDBExportMedia.setAudioRate(mEffectObject.getRate());
						mDBExportMedia.setAudioReverb(mEffectObject.getReverb());
						mDBExportMedia.setFlangerEffect(mEffectObject.isFlanger());
						mDBExportMedia.setAudioEcho(mEffectObject.isEcho());
						mDBExportMedia.setAudioEQ(mEffectObject.getEq());
						
						mDBExportMedia.saveToFile(mTempOutPutFile.getAbsolutePath());
						mDBExportMedia.releaseAudio();
					}
				}
			}

			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}

		});
		mDBTask.execute();

	}

	public void onInitAudioDevice() {
		if (!isInit) {
			isInit = true;
			if (!BASS.BASS_Init(-1, 44100, 0)) {
				new Exception(TAG + " Can't initialize device").printStackTrace();
				this.isInit = false;
				return;
			}
			String libpath = getApplicationInfo().nativeLibraryDir;
			try{
				BASS.BASS_PluginLoad(libpath + "/libbass_fx.so", 0);
				BASS.BASS_PluginLoad(libpath + "/libbassenc.so", 0);
				BASS.BASS_PluginLoad(libpath + "/libbassmix.so", 0);
				BASS.BASS_PluginLoad(libpath + "/libbasswv.so", 0);
			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
	}

	private void createDBMedia() {
		if (!StringUtils.isEmptyString(mPathAudio)) {
			mDBMedia = new DBMediaPlayer(mPathAudio);
			mDBMedia.prepareAudio();
			mDBMedia.setOnDBMediaListener(new IDBMediaListener() {
				@Override
				public void onMediaError() {

				}

				@Override
				public void onMediaCompletion() {
					TotalDataManager.getInstance().onResetState();
					mEffectApdater.notifyDataSetChanged();
				}
			});
		}
	}

	private void showDiaglogShare(final EffectObject mEffectObject) {
		MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(this);
		mBuilder.backgroundColor(getResources().getColor(R.color.white));
		mBuilder.title(R.string.title_options);
		mBuilder.titleColor(getResources().getColor(R.color.black_text));
		mBuilder.items(R.array.list_options);
		mBuilder.contentColor(getResources().getColor(R.color.black_text));
		mBuilder.positiveColor(getResources().getColor(R.color.pink));
		mBuilder.negativeColor(getResources().getColor(R.color.black_secondary_text));
		mBuilder.positiveText(R.string.title_cancel);
		mBuilder.autoDismiss(true);
		mBuilder.typeface(mTypefaceBold, mTypefaceLight);
		mBuilder.itemsCallback(new MaterialDialog.ListCallback() {

			@Override
			public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
				if (which == 0) {
					shareFile();
				}
				else if (which == 1) {
					saveAsRingtone();
				}
				else if (which == 2) {
					saveAsNotification();
				}
			}
		});
		mBuilder.build().show();
	}

	private void shareFile() {
		File file = new File(mPathAudio);
		File mDir = file.getParentFile();
		final File mOutPutFile = new File(mDir, mNameExportVoice);
		if (mOutPutFile.exists() && mOutPutFile.isFile()) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			if(mNameExportVoice.endsWith(AUDIO_RECORDER_FILE_EXT_MP3)){
				shareIntent.setType("audio/mp3");
			}
			else{
				shareIntent.setType("audio/*");
			}
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mOutPutFile));
			startActivity(Intent.createChooser(shareIntent, "Share Via"));
		}
	}

	private void saveAsRingtone() {
		File file = new File(mPathAudio);
		File mDir = file.getParentFile();
		final File mOutPutFile = new File(mDir, mNameExportVoice);
		if (mOutPutFile != null && mOutPutFile.isFile()) {
			Uri mUri=null;
			ContentValues values = new ContentValues();
			values.put(MediaColumns.DATA, mOutPutFile.getAbsolutePath());
			values.put(MediaColumns.TITLE, mNameExportVoice.replaceAll(AUDIO_RECORDER_FILE_EXT_WAV, ""));
			values.put(MediaColumns.MIME_TYPE, "audio/*");
			values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
			
			String id=getIdFromContentUri(mOutPutFile.getAbsolutePath());
			if(StringUtils.isEmptyString(id)){
				mUri = this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(mOutPutFile.getAbsolutePath()), values);
			}
			else{
				this.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
						, values, MediaColumns._ID+" = ?", new String[]{id});
				mUri=Uri.parse(String.format(FORMAT_URI, id));
			}
			RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, mUri);
			showToast(R.string.info_set_ringtone_successfully);

		}
	}

	private void saveAsNotification() {
		File file = new File(mPathAudio);
		File mDir = file.getParentFile();
		final File mOutPutFile = new File(mDir, mNameExportVoice);
		if (mOutPutFile != null && mOutPutFile.isFile()) {
			Uri mUri=null;
			ContentValues values = new ContentValues();
			values.put(MediaColumns.DATA, mOutPutFile.getAbsolutePath());
			values.put(MediaColumns.TITLE, mNameExportVoice.replaceAll(AUDIO_RECORDER_FILE_EXT_WAV, ""));
			values.put(MediaColumns.MIME_TYPE, "audio/*");
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
			
			String id=getIdFromContentUri(mOutPutFile.getAbsolutePath());
			if(StringUtils.isEmptyString(id)){
				mUri = this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(mOutPutFile.getAbsolutePath()), values);
			}
			else{
				this.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
						, values, MediaColumns._ID+" = ?", new String[]{id});
				mUri=Uri.parse(String.format(FORMAT_URI, id));
			}
			
			RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, mUri);
			showToast(R.string.info_set_notification_successfully);

		}
	}
	private String getIdFromContentUri(String path) {
		try {
			if(path!=null){
				String id;
				String[] filePathColumn = {MediaColumns._ID};
				String[] selectionArgs = {path};
				Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
						filePathColumn, MediaColumns.DATA+" = ?", selectionArgs, null);
				if(cursor!=null && cursor.moveToFirst()){
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					id = cursor.getString(columnIndex);
					cursor.close();
					return id;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	private void showDialogEnterName(final IDBCallback mDCallback) {
		final EditText mEdName = new EditText(this);
		mEdName.setSingleLine(true);
		Builder builder = new Builder(this).setTitle(getString(R.string.title_enter_name)).setView(mEdName)
				.setPositiveButton(getString(R.string.title_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ApplicationUtils.hiddenVirtualKeyboard(EffectActivity.this, mEdName);
						String mNewName = mEdName.getText().toString();
						if(!StringUtils.isEmptyString(mNewName)){
							if(StringUtils.isContainsSpecialCharacter(mNewName)){
								showToast(R.string.info_your_name_error);
								return;
							}
							mNameExportVoice=mNewName+".wav";
						}
						if(mDCallback!=null){
							mDCallback.onAction();
						}
					}
				}).setNegativeButton(getString(R.string.title_skip), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(mDCallback!=null){
							mDCallback.onAction();
						}
					}
				});
		AlertDialog mDialogEnterPass = builder.create();
		mDialogEnterPass.show();
	}

}
