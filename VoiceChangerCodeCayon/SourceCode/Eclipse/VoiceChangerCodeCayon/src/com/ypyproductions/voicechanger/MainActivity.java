package com.ypyproductions.voicechanger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ypyproductions.materialdialogs.MaterialDialog;
import com.ypyproductions.voicechanger.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DirectionUtils;
import com.ypyproductions.voicechanger.utils.ShareActionUtils;
import com.ypyproductions.voicechanger.view.DBShimmerFrameLayout;


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

public class MainActivity extends DBFragmentActivity implements IDBFragmentConstants ,View.OnClickListener{
	
	public static final String TAG = MainActivity.class.getSimpleName();

	private Button mBtnRecord;
	private RelativeLayout mLayoutBlur;

	private Button mBtnAboutUs;
	private Button mBtnGallery;
	private Button mBtnMoreApp;

	private DBShimmerFrameLayout mLayoutShimmer;
	private TextView mTvHelpInfo;
	private AdView adView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLayoutBlur =(RelativeLayout)findViewById(R.id.layout_blur);

		TextView mTvLogo = (TextView) findViewById(R.id.tv_header);
		mTvLogo.setTypeface(mTypefaceLogo);

		mBtnRecord =(Button) findViewById(R.id.btn_record);
		mBtnAboutUs =(Button) findViewById(R.id.btn_about_us);
		mBtnGallery =(Button) findViewById(R.id.btn_gallery);
		mBtnMoreApp =(Button) findViewById(R.id.btn_moreapp);

		this.mTvHelpInfo = (TextView) findViewById(R.id.tv_help);
		this.mTvHelpInfo.setTypeface(mTypefaceAvenir);

		mLayoutShimmer=(DBShimmerFrameLayout) findViewById(R.id.layout_click_voice_to_start);
		mLayoutShimmer.setDuration(DURATION_SHIMMER);
		mLayoutShimmer.startShimmerAnimation();

		setUpBlurLayout();
		setupLayoutAdmob();
	}

	private void setupLayoutAdmob() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if (ApplicationUtils.isOnline(this)) {
			boolean b=SHOW_ADVERTISEMENT;
			if(b){
				adView = new AdView(this);
				adView.setAdUnitId(ADMOB_ID_BANNER);
				adView.setAdSize(AdSize.SMART_BANNER);

				layout.addView(adView);
				AdRequest mAdRequest = new AdRequest.Builder().addTestDevice("51F0A3F4C13F05DD49DE0D71F2B369FB").build();
				adView.loadAd(mAdRequest);
				return;

			}
		}
		layout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onDestroyData() {
		super.onDestroyData();
		showInterstitialAd();
		mTotalMng.onDestroy();
		mSoundMng.releaseSound();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLayoutShimmer!=null){
			mLayoutShimmer.stopShimmerAnimation();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_gallery:
				mSoundMng.play(this, R.raw.click);
				Intent mIntent2 = new Intent(this, GalleryActivity.class);
				DirectionUtils.changeActivity(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent2);
				break;
			case R.id.btn_about_us:
				mSoundMng.play(this, R.raw.click);
				showDialogAboutUs();
				break;
			case R.id.btn_moreapp:
				mSoundMng.play(this, R.raw.click);
				ShareActionUtils.goToUrl(MainActivity.this, URL_MORE_APPS);
				break;
			case R.id.btn_record:
				mSoundMng.play(this, R.raw.click);
				Intent mIntent = new Intent(this,RecordActivity.class);
				DirectionUtils.changeActivity(this,R.anim.slide_in_from_right,R.anim.slide_out_to_left,true,mIntent);
				break;
			default:
				break;
		}
	}

	private void showDialogAboutUs() {
		MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(this);
		mBuilder.backgroundColor(getResources().getColor(R.color.white));
		mBuilder.title(R.string.title_about_us);
		mBuilder.titleColor(getResources().getColor(R.color.black_text));
		mBuilder.items(R.array.list_share);
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
					ShareActionUtils.shareViaEmail(MainActivity.this, YOUR_EMAIL_CONTACT, "", "");
				}
				else if (which == 1) {
					Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
					mIntent.putExtra(KEY_URL, URL_YOUR_WEBSITE);
					mIntent.putExtra(KEY_HEADER, getString(R.string.title_website));
					startActivity(mIntent);
				}
				else if (which == 2) {
					Intent mIntent = new Intent(MainActivity.this, ShowUrlActivity.class);
					mIntent.putExtra(KEY_URL, URL_YOUR_FACE_BOOK);
					mIntent.putExtra(KEY_HEADER, getString(R.string.title_facebook));
					startActivity(mIntent);
				}
				else if (which == 3) {
					String url = String.format(URL_FORMAT_LINK_APP, getPackageName());
					ShareActionUtils.goToUrl(MainActivity.this, url);
				}
			}
		});
		mBuilder.build().show();
	}







}
