package com.ypyproductions.voicechanger.basseffect;

import android.os.Handler;
import android.os.Message;

import com.un4seen.bass.BASS;
import com.un4seen.bass.BASS_FX;
import com.un4seen.bass.BASSenc;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.StringUtils;

import java.nio.ByteBuffer;
import java.util.Locale;

public class DBMediaPlayer implements IDBMediaConstants {

	private static final String TAG = DBMediaPlayer.class.getSimpleName();
	public static final int FLABUFLEN = 350;

	private String mMediaPath;
	private IDBMediaListener mDBMediaListener;

	private int currrentPostion = 0;
	private int duration = 0;

	private int mChanPlay;

	private boolean isPlaying = false;
	private boolean isPausing = false;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			currrentPostion = getChannelPosition();
			duration = getChannelLength();
			if(!isReverse){
				if (currrentPostion >= duration) {
					removeMessages(0);
					if (mDBMediaListener != null) {
						mDBMediaListener.onMediaCompletion();
					}
				}
				else {
					sendEmptyMessageDelayed(0, 50);
				}
			}
			else{
				if (currrentPostion <= 0) {
					removeMessages(0);
					if (mDBMediaListener != null) {
						mDBMediaListener.onMediaCompletion();
					}
				}
				else {
					sendEmptyMessageDelayed(0, 50);
				}
			}
		}
	};

	private int mFxReverbEffect;
	private int mFxEQEffect;
	private int mFxFlangerEffect;
	private int mFxEchoEffect;
	private boolean isReverse;

	public DBMediaPlayer(String mBeatPath) {
		this.mMediaPath = mBeatPath;
	}

	public boolean prepareAudio() {
		if (!StringUtils.isEmptyString(mMediaPath)) {
			if (mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_MP3) || mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_WAV)
					|| mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_OGG) || mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_FLAC)) {
				this.initMedia();
				return true;
			}
			else {
				new Exception("DBMidiPlayer:can not support file format").printStackTrace();
			}
		}
		return false;
	}

	public void startAudio() {
		this.isPlaying = true;
		if (mChanPlay != 0) {
			BASS.BASS_ChannelPlay(mChanPlay, false);
		}
		this.mHandler.sendEmptyMessage(0);
	}

	/**
	 * -12 to 12
	 * 
	 * @param semitone
	 */
	public void setAudioPitch(int semitone) {
		if (mChanPlay != 0) {
			BASS.BASS_ChannelSetAttribute(mChanPlay, BASS_FX.BASS_ATTRIB_TEMPO_PITCH, semitone);
		}
	}

	/**
	 * -30 to 30
	 * 
	 * @param value
	 */

	public void setAudioRate(float value) {
		if (mChanPlay != 0) {
			BASS.BASS_ChannelSetAttribute(mChanPlay, BASS_FX.BASS_ATTRIB_TEMPO, value);
		}
	}

	/**
	 * ReverbMix- ReverbTime-HighFreg
	 * 
	 * @param value
	 */
	public void setAudioReverb(float[] value) {
		if (mChanPlay != 0) {
			if (value != null) {
				if (mFxReverbEffect == 0) {
					mFxReverbEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_REVERB, 0);
				}
				if (mFxReverbEffect != 0) {
					BASS.BASS_DX8_REVERB p = new BASS.BASS_DX8_REVERB();
					BASS.BASS_FXGetParameters(mFxReverbEffect, p);

					p.fReverbMix = value[0];
					p.fReverbTime = value[1];
					p.fHighFreqRTRatio = value[2];
					BASS.BASS_FXSetParameters(mFxReverbEffect, p);
				}
			}
			else {
				if (mFxReverbEffect != 0) {
					BASS.BASS_ChannelRemoveFX(mChanPlay, mFxReverbEffect);
					mFxReverbEffect = 0;
				}
			}
		}
	}
	
	/**
	 * fCenter- fBandwidth-fGain
	 * 
	 * @param value
	 */
	public void setAudioEQ(float[] value) {
		if (mChanPlay != 0) {
			if (value != null) {
				if (mFxEQEffect == 0) {
					mFxEQEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_PARAMEQ, 0);
				}
				if (mFxEQEffect != 0) {
					BASS.BASS_DX8_PARAMEQ p = new BASS.BASS_DX8_PARAMEQ();
					BASS.BASS_FXGetParameters(mFxEQEffect, p);

					p.fCenter = value[0];
					p.fBandwidth = value[1];
					p.fGain = value[2];
					
					BASS.BASS_FXSetParameters(mFxEQEffect, p);
				}
			}
			else {
				if (mFxEQEffect != 0) {
					BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQEffect);
					mFxEQEffect = 0;
				}
			}
		}
	}
	
	/**
	 * ReverbMix- ReverbTime-HighFreg
	 * 
	 * @param value
	 */
	public void setAudioEcho(boolean b) {
		if (mChanPlay != 0) {
			if (b) {
				if (mFxEchoEffect == 0) {
					mFxEchoEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_ECHO, 0);
				}
				if (mFxEchoEffect != 0) {
					BASS.BASS_DX8_ECHO p1 = new BASS.BASS_DX8_ECHO();
					p1.fLeftDelay=1000;
					p1.fRightDelay=1000;
					p1.fFeedback=50;
					BASS.BASS_FXSetParameters(mChanPlay, mFxEchoEffect);
				}
			}
			else {
				if (mFxEchoEffect != 0) {
					BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEchoEffect);
					mFxEchoEffect = 0;
				}
			}
		}
	}

	public void setFlangerEffect(boolean b) {
		if (mChanPlay != 0) {
			if (b) {
				if (mFxFlangerEffect == 0) {
					mFxFlangerEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_FLANGER, 0);
				}
				DBLog.d(TAG, "===========>mFxFlangerEffect=" + mFxFlangerEffect);
				if (mFxFlangerEffect != 0) {
					BASS.BASS_DX8_FLANGER p = new BASS.BASS_DX8_FLANGER();
					BASS.BASS_FXGetParameters(mFxFlangerEffect, p);
					p.fWetDryMix = 50;
					p.fDepth = 100;
					p.fFeedback = 80;
					p.fDelay = 10;
					p.lPhase = BASS.BASS_DX8_PHASE_90;
					// p.fFrequency=1;
					BASS.BASS_FXSetParameters(mFxFlangerEffect, p);
				}
			}
			else {
				if (mFxFlangerEffect != 0) {
					BASS.BASS_ChannelRemoveFX(mChanPlay, mFxFlangerEffect);
					mFxFlangerEffect = 0;
				}
			}
		}
	}

	public void pauseAudio() {
		if (!isPlaying) {
			new Exception(TAG + " pauseAudio:HanetMediaPlayer not init").printStackTrace();
			return;
		}
		this.isPausing = true;
		if (mChanPlay != 0) {
			BASS.BASS_ChannelPause(mChanPlay);
		}
	}

	public void releaseAudio() {
		mHandler.removeMessages(0);
		if (mMediaPath == null) {
			return;
		}
		if (mFxReverbEffect != 0) {
			BASS.BASS_ChannelRemoveFX(mChanPlay, mFxReverbEffect);
		}
		if (mFxFlangerEffect != 0) {
			BASS.BASS_ChannelRemoveFX(mChanPlay, mFxFlangerEffect);
		}
		if (mFxEchoEffect != 0) {
			BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEchoEffect);
		}
		if (mFxEQEffect != 0) {
			BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQEffect);
		}
		isPlaying = false;
		isPausing = false;
		BASS.BASS_StreamFree(mChanPlay);
	}

	public void resumeAudio() {
		if (!isPausing) {
			new Exception(TAG + " resumeAudio:HanetMediaPlayer is playing").printStackTrace();
			return;
		}
		this.isPausing = false;
		if (mChanPlay != 0) {
			BASS.BASS_ChannelPlay(mChanPlay, false);
		}
	}

	public void setOnDBMediaListener(IDBMediaListener mDBMediaListener) {
		this.mDBMediaListener = mDBMediaListener;
	}

	public int getDuration() {
		if (mChanPlay != 0) {
			duration = getChannelLength();
		}
		return duration;
	}

	public int getCurrrentPostion() {
		return currrentPostion;
	}

	public boolean isPausing() {
		return !isPausing;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void seekTo(int postion) {
		if (!isPlaying) {
			new Exception(TAG + " seekTo:HanetMediaPlayer is not playing").printStackTrace();
			return;
		}
		currrentPostion = postion;
		this.seekChannelTo(postion);
	}

	private void initMedia() {
		BASS.BASS_StreamFree(mChanPlay);
		if (!StringUtils.isEmptyString(mMediaPath)) {
			mChanPlay = BASS.BASS_StreamCreateFile(mMediaPath, 0, 0, BASS.BASS_STREAM_DECODE);
		}
		if (mChanPlay != 0) {
			mChanPlay=BASS_FX.BASS_FX_ReverseCreate(mChanPlay,2,BASS.BASS_STREAM_DECODE|BASS_FX.BASS_FX_FREESOURCE);
			if(mChanPlay!=0){
				BASS.BASS_CHANNELINFO infoPlay = new BASS.BASS_CHANNELINFO();
				BASS.BASS_ChannelGetInfo(mChanPlay, infoPlay);
				mChanPlay = BASS_FX.BASS_FX_TempoCreate(mChanPlay, BASS_FX.BASS_FX_FREESOURCE);
				if (mChanPlay == 0) {
					new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
					BASS.BASS_StreamFree(mChanPlay);
					return;
				}
			}
			else{
				new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
				BASS.BASS_StreamFree(mChanPlay);
			}
		}
		else{
			new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
			BASS.BASS_StreamFree(mChanPlay);
		}
	}

	public boolean initMediaToSave() {
		BASS.BASS_StreamFree(mChanPlay);
		mChanPlay = BASS.BASS_StreamCreateFile(mMediaPath, 0, 0, BASS.BASS_STREAM_DECODE);
		if (mChanPlay != 0) {
			mChanPlay=BASS_FX.BASS_FX_ReverseCreate(mChanPlay,2,BASS.BASS_STREAM_DECODE);
			if(mChanPlay!=0){
				mChanPlay = BASS_FX.BASS_FX_TempoCreate(mChanPlay, BASS.BASS_STREAM_DECODE);
				if (mChanPlay == 0) {
					new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
					BASS.BASS_StreamFree(mChanPlay);
					return false;
				}
				return true;
			}
			else{
				new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
				BASS.BASS_StreamFree(mChanPlay);
			}
		}
		return false;
	}

	public void seekChannelTo(int position) {
		if (mChanPlay != 0) {
			BASS.BASS_ChannelSetPosition(mChanPlay, BASS.BASS_ChannelSeconds2Bytes(mChanPlay, position), BASS.BASS_POS_BYTE);
		}
	}

	private int getChannelPosition() {
		if (mChanPlay != 0) {
			int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanPlay, (long) BASS.BASS_ChannelGetPosition(mChanPlay, BASS.BASS_POS_BYTE));
			return p;
		}
		return -1;
	}

	private int getChannelLength() {
		if (mChanPlay != 0) {
			int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanPlay, (long) BASS.BASS_ChannelGetLength(mChanPlay, BASS.BASS_POS_BYTE));
			return p;
		}
		return 0;
	}

	public void setChannelVolumne(float volumne) {
		if (mChanPlay != 0) {
			BASS.BASS_ChannelSetAttribute(mChanPlay, BASS.BASS_ATTRIB_VOL, volumne);
		}
	}
	
	public void setReverse(boolean b){
		isReverse=b;
		if(mChanPlay!=0){
			int srcChan = BASS_FX.BASS_FX_TempoGetSource(mChanPlay);
			float dir = 0.0f;
			BASS.BASS_ChannelGetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, dir);
			if(b){
				BASS.BASS_ChannelSetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, BASS_FX.BASS_FX_RVS_REVERSE);
			}
			else{
				BASS.BASS_ChannelSetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, BASS_FX.BASS_FX_RVS_FORWARD);
			}
		}
	}

	public void saveToFile(String filePath){
		if(!StringUtils.isEmptyString(filePath) && mChanPlay!=0){
			int encoder =BASSenc.BASS_Encode_Start(mChanPlay, filePath, BASSenc.BASS_ENCODE_PCM| BASSenc.BASS_ENCODE_AUTOFREE , null, 0);
			if(encoder!=0){
				try {
					ByteBuffer buf=ByteBuffer.allocateDirect(20000); // allocate buffer for decoding
					while (true) {
						int r=BASS.BASS_ChannelGetData(mChanPlay, buf, buf.capacity()); // decode some data (and send it to WAV writer)
						if (r==-1 || r==0) {
							break;
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
