package com.ypyproductions.voicechanger.constants;


/**
 * 
 *
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:AndroidCloundMusicPlayer
 * @Date:Dec 14, 2014 
 *
 */
public interface IVoiceChangerConstants {

	public static final boolean DEBUG = false;

	public static final boolean SHOW_ADVERTISEMENT = false;

	public static final String ADMOB_ID_BANNER = "ADMOB_ID_BANNER";
	public static final String ADMOB_ID_INTERTESTIAL = "ADMOB_ID_INTERTESTIAL";

	public static final String YOUR_EMAIL_CONTACT = "YOUR_EMAIL_CONTACT";
	public static final String URL_YOUR_WEBSITE = "URL_YOUR_WEBSITE";
	public static final String URL_YOUR_FACE_BOOK = "URL_YOUR_FACE_BOOK";
	public static final String URL_MORE_APPS = "URL_MORE_APPS";

	public static final String NAME_FOLDER_RECORD = "voice_changer";

	public static final String KEY_HEADER = "KEY_HEADER";
	public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	public static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";
	public static final int RECORDER_SAMPLE_RATE = 44100;
	public static final long MAX_TIME = 60000 ;//ONE MINUTE;
	public static final int DURATION_SHIMMER = 1500 ;//ONE MINUTE;
	public static final int SIMPLE_RECORDER_BUFFER_SIZE = 8192;

	public static final String KEY_PATH_AUDIO = "KEY_PATH_AUDIO";
	public static final String FORMAT_NAME_VOICE = "voice_%1$s.wav";
	
	public static final String FORMAT_URI = "content://media/external/audio/media/%1$s";
	
	public static final String URL_FORMAT_LINK_APP="https://play.google.com/store/apps/details?id=%1$s";
}
