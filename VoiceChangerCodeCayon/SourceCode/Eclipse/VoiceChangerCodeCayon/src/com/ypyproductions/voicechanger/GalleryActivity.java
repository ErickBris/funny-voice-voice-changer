package com.ypyproductions.voicechanger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.un4seen.bass.BASS;
import com.ypyproductions.materialdialogs.MaterialDialog;
import com.ypyproductions.voicechanger.adapter.GalleryAdapter;
import com.ypyproductions.voicechanger.basseffect.DBMediaPlayer;
import com.ypyproductions.voicechanger.basseffect.IDBMediaListener;
import com.ypyproductions.voicechanger.object.SongObject;
import com.ypyproductions.voicechanger.soundMng.SoundManager;
import com.ypyproductions.voicechanger.task.DBTask;
import com.ypyproductions.voicechanger.task.IDBCallback;
import com.ypyproductions.voicechanger.task.IDBTaskListener;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DBListExcuteAction;
import com.ypyproductions.voicechanger.utils.DirectionUtils;
import com.ypyproductions.voicechanger.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

public class GalleryActivity extends DBFragmentActivity implements OnClickListener, GalleryAdapter.OnGalleryListener {

	public static final String TAG = GalleryActivity.class.getSimpleName();

	private ListView mListViewGallery;

	private ArrayList<SongObject> mListSongObjects;

	private GalleryAdapter mEffectApdater;

	private TextView mTvHeader;

	private boolean isInit;

	private DBMediaPlayer mDBMedia;

	private AdView adView;

	private TextView mTvNoResult;

	private SongObject mCurrentSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_gallery);

		mListViewGallery = (ListView) findViewById(R.id.list_voices);

		mTvHeader = (TextView) findViewById(R.id.tv_header);
		mTvNoResult = (TextView) findViewById(R.id.tv_no_result);

		mTvHeader.setTypeface(mTypefaceLogo);
		mTvNoResult.setTypeface(mTypefaceAvenir);

		setUpBlurLayout();
		setUpLayoutAdmob();
		onInitAudioDevice();
		startGetAllFile();

	}

	private void startGetAllFile() {
		if (!ApplicationUtils.hasSDcard()) {
			mTvNoResult.setVisibility(View.VISIBLE);
			mListViewGallery.setVisibility(View.GONE);
			return;
		}
		DBTask mDBTask = new DBTask(new IDBTaskListener() {

			@Override
			public void onPreExcute() {
				showProgressDialog();
			}

			@Override
			public void onDoInBackground() {
				final File mRootDir = new File(Environment.getExternalStorageDirectory(), NAME_FOLDER_RECORD);
				if (!mRootDir.exists()) {
					mRootDir.mkdirs();
				}
				File[] mListFiles = mRootDir.listFiles();
				if (mListFiles != null && mListFiles.length > 0) {
					mListSongObjects = new ArrayList<SongObject>();
					for (File mFile : mListFiles) {
						String nameFile = mFile.getName();
						if (!StringUtils.isEmptyString(nameFile) && (nameFile.endsWith(AUDIO_RECORDER_FILE_EXT_MP3)||nameFile.endsWith(AUDIO_RECORDER_FILE_EXT_WAV))) {
							nameFile = nameFile.replaceAll(AUDIO_RECORDER_FILE_EXT_MP3, "");
							nameFile = nameFile.replaceAll(AUDIO_RECORDER_FILE_EXT_WAV, "");
							Date mDate = new Date(mFile.lastModified());
							String path = mFile.getAbsolutePath();
							SongObject mSongObject = new SongObject(nameFile, mDate, path);
							mListSongObjects.add(mSongObject);
						}
					}
					Collections.sort(mListSongObjects, new Comparator<SongObject>(){
						@Override
						public int compare(SongObject lhs, SongObject rhs) {
							Date time1 = lhs.getDate();
							Date time2 = rhs.getDate();
							return time2.compareTo(time1);
						}
						
					});
				}
			}

			@Override
			public void onPostExcute() {
				dimissProgressDialog();
				if (mListSongObjects == null || mListSongObjects.size() == 0) {
					mTvNoResult.setVisibility(View.VISIBLE);
					mListViewGallery.setVisibility(View.GONE);
				}
				else {
					mTvNoResult.setVisibility(View.GONE);
					mListViewGallery.setVisibility(View.VISIBLE);
					mEffectApdater = new GalleryAdapter(GalleryActivity.this, mListSongObjects, mTypefaceAvenir);
					mEffectApdater.setOnGalleryListener(GalleryActivity.this);
					mListViewGallery.setAdapter(mEffectApdater);
				}
			}

		});
		mDBTask.execute();
	}

	private void setUpLayoutAdmob() {
		boolean b=SHOW_ADVERTISEMENT;
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if (b && ApplicationUtils.isOnline(this)) {
			adView = new AdView(this);
			adView.setAdUnitId(ADMOB_ID_BANNER);
			adView.setAdSize(AdSize.SMART_BANNER);
			layout.addView(adView);
			AdRequest mAdRequest = new AdRequest.Builder().build();
			adView.loadAd(mAdRequest);
			return;
		}
		layout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gallery_mic:
			mSoundMng.play(this, R.raw.click);
			Intent mIntent = new Intent(this, RecordActivity.class);
			DirectionUtils.changeActivity(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		if (mDBMedia != null) {
			mDBMedia.releaseAudio();
		}
		if(mListSongObjects!=null){
			mListSongObjects.clear();
			mListSongObjects=null;
		}
		BASS.BASS_PluginFree(0);
		BASS.BASS_Free();
		super.onDestroy();
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
		Intent mIntent = new Intent(this, MainActivity.class);
		DirectionUtils.changeActivity(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, mIntent);
	}


	@Override
	protected void onPause() {
		super.onPause();
		if (mDBMedia != null) {
			resetStateAudio();
		}
	}

	private void resetStateAudio() {
		for(SongObject mSongObject2:mListSongObjects){
			mSongObject2.setPlaying(false);
		}
		if (mEffectApdater != null) {
			mEffectApdater.notifyDataSetChanged();
		}
		if (mDBMedia!=null && mDBMedia.isPlaying()) {
			mDBMedia.pauseAudio();
		}
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
			BASS.BASS_PluginLoad(libpath + "/libbass_fx.so", 0);
		}
	}

	private void createDBMedia(SongObject mSongObject) {
		if (!StringUtils.isEmptyString(mSongObject.getPath())) {
			if(mDBMedia!=null){
				mDBMedia.releaseAudio();
				mDBMedia=null;
			}
			mDBMedia = new DBMediaPlayer(mSongObject.getPath());
			mDBMedia.prepareAudio();
			mDBMedia.setReverse(false);
			mDBMedia.setOnDBMediaListener(new IDBMediaListener() {
				@Override
				public void onMediaError() {

				}

				@Override
				public void onMediaCompletion() {
					for(SongObject mSongObject2:mListSongObjects){
						mSongObject2.setPlaying(false);
					}
					mEffectApdater.notifyDataSetChanged();
				}
			});
		}
	}

	private void showDialogShare(final SongObject mSongObject) {
		MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(this);
		mBuilder.backgroundColor(getResources().getColor(R.color.white));
		mBuilder.title(R.string.title_options);
		mBuilder.titleColor(getResources().getColor(R.color.black_text));
		mBuilder.items(R.array.list_gallery_menu);
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
					shareFile(mSongObject);
				}
				else if (which == 1) {
					saveAsRingtone(mSongObject);
				}
				else if (which == 2) {
					saveAsNotification(mSongObject);
				}
				else if (which == 3) {
					showDialogDelete(mSongObject);
				}
			}
		});
		mBuilder.build().show();

	}

	private void shareFile(SongObject mSongObject) {
		final File mOutPutFile = new File(mSongObject.getPath());
		if (mOutPutFile.exists() && mOutPutFile.isFile()) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			if(mSongObject.getPath().endsWith(AUDIO_RECORDER_FILE_EXT_MP3)){
				shareIntent.setType("audio/mp3");
			}
			else{
				shareIntent.setType("audio/*");
			}
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mOutPutFile));
			startActivity(Intent.createChooser(shareIntent, "Share Via"));
		}
	}

	private void saveAsRingtone(SongObject mSongObject) {
		final File mOutPutFile = new File(mSongObject.getPath());
		if (mOutPutFile != null && mOutPutFile.isFile()) {
			Uri mUri=null;
			ContentValues values = new ContentValues();
			values.put(MediaColumns.DATA, mOutPutFile.getAbsolutePath());
			values.put(MediaColumns.TITLE, mSongObject.getName());
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

	private void saveAsNotification(SongObject mSongObject) {
		final File mOutPutFile = new File(mSongObject.getPath());
		if (mOutPutFile != null && mOutPutFile.isFile()) {
			Uri mUri=null;
			
			ContentValues values = new ContentValues();
			values.put(MediaColumns.DATA, mOutPutFile.getAbsolutePath());
			values.put(MediaColumns.TITLE, mSongObject.getName());
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

	@Override
	public void onPlayEffect(SongObject mSongObject) {
		if(mCurrentSong!=null && mCurrentSong.equals(mSongObject)){
			boolean isPlaying = mSongObject.isPlaying();
			if (isPlaying) {
				mSongObject.setPlaying(false);
				if (mDBMedia != null) {
					mDBMedia.pauseAudio();
				}
			}
			else {
				for(SongObject mSongObject2:mListSongObjects){
					mSongObject2.setPlaying(false);
				}
				mSongObject.setPlaying(true);
				createDBMedia(mSongObject);
				if (mDBMedia != null) {
					if (mDBMedia.isPlaying()) {
						mDBMedia.seekTo(0);
					}
					mDBMedia.startAudio();
				}
			}
			mEffectApdater.notifyDataSetChanged();
		}
		else{
			mCurrentSong = mSongObject;
			for(SongObject mSongObject2:mListSongObjects){
				mSongObject2.setPlaying(false);
			}
			mSongObject.setPlaying(true);
			createDBMedia(mSongObject);
			if (mDBMedia != null) {
				if (mDBMedia.isPlaying()) {
					mDBMedia.seekTo(0);
				}
				mDBMedia.startAudio();
			}
			mEffectApdater.notifyDataSetChanged();
		}
	}

	@Override
	public void onShareEffect(SongObject mSongObject) {
		SoundManager.getInstance().play(this, R.raw.click);
		resetStateAudio();
		showDialogShare(mSongObject);
	}
	
	private void showDialogDelete(final SongObject mSongObject){
		showFullDialog(R.string.title_confirm, R.string.info_delete_sound, R.string.title_ok, R.string.title_cancel, new IDBCallback() {
			
			@Override
			public void onAction() {
				if (!StringUtils.isEmptyString(mSongObject.getPath())) {
					DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
						@Override
						public void onAction() {
							File mFile = new File(mSongObject.getPath());
							if(mFile.exists() && mFile.isFile()){
								mFile.delete();
							}
							mListSongObjects.remove(mSongObject);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mTvNoResult.setVisibility((mListSongObjects == null || mListSongObjects.size() == 0) ? View.VISIBLE : View.GONE);
									if (mEffectApdater != null) {
										mEffectApdater.notifyDataSetChanged();
									}
								}
							});
						}
					});
				}
			}
		});
	}

}
