package com.ypyproductions.voicechanger;

import java.util.ArrayList;
import java.util.Random;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ypyproductions.materialdialogs.MaterialDialog;
import com.ypyproductions.materialdialogs.MaterialDialog.Builder;
import com.ypyproductions.materialdialogs.MaterialDialog.ButtonCallback;
import com.ypyproductions.voicechanger.abtractclass.fragment.DBFragment;
import com.ypyproductions.voicechanger.constants.IVoiceChangerConstants;
import com.ypyproductions.voicechanger.dataMng.TotalDataManager;
import com.ypyproductions.voicechanger.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.voicechanger.setting.SettingManager;
import com.ypyproductions.voicechanger.soundMng.SoundManager;
import com.ypyproductions.voicechanger.task.IDBCallback;
import com.ypyproductions.voicechanger.task.IDBConstantURL;
import com.ypyproductions.voicechanger.utils.ApplicationUtils;
import com.ypyproductions.voicechanger.utils.DBLog;
import com.ypyproductions.voicechanger.utils.ResolutionUtils;
import com.ypyproductions.voicechanger.utils.ShareActionUtils;
import com.ypyproductions.voicechanger.view.BlurringView;

/**
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:TemplateChangeTheme
 * @Date:Aug 4, 2015
 */
public class DBFragmentActivity extends ActionBarActivity implements IDBConstantURL, IDialogFragmentListener, IVoiceChangerConstants {

    public static final String TAG = DBFragmentActivity.class.getSimpleName();
    private Dialog mProgressDialog;

    private int screenWidth;
    private int screenHeight;

    public ArrayList<Fragment> mListFragments;

    public Typeface mTypefaceNormal;
    public Typeface mTypefaceLight;
    public Typeface mTypefaceBold;
    public Typeface mTypefaceLogo;
    public Typeface mTypefaceItalic;
    public Typeface mTypefaceAvenir;

    private InterstitialAd mInterstitial;
    private Random mRandom;

    public int mTextColor;
    public int mAccentColor;
    public int mBgColor;

    private boolean isAllowPressMoreToExit;
    private int countToExit;
    private long pivotTime;
    public TotalDataManager mTotalMng;
    public SoundManager mSoundMng;

    private ImageView mBlurredView;
    public BlurringView mBlurringView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.createProgressDialog();
        mTypefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        mTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        mTypefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
        mTypefaceLogo = Typeface.createFromAsset(getAssets(), "fonts/Satisfy-Regular.ttf");
        mTypefaceAvenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Oblique.ttf");

        int[] mRes = ResolutionUtils.getDeviceResolution(this);
        if (mRes != null && mRes.length == 2) {
            screenWidth = mRes[0];
            screenHeight = mRes[1];
        }
        mTextColor = getObjectColor(android.R.attr.textColor);
        if (SettingManager.getCurrentAccentColor(this) != 0) {
            mAccentColor = SettingManager.getCurrentAccentColor(this);
        }
        else {
            mAccentColor = getObjectColor(R.attr.colorAccent);
        }

        mTotalMng = TotalDataManager.getInstance();
        mSoundMng = SoundManager.getInstance();

        mBgColor = getObjectColor(android.R.attr.windowBackground);
        mRandom = new Random();
    }

    public void setUpBlurLayout() {
        mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
        mBlurredView = (ImageView) findViewById(R.id.img_bg);
        mBlurringView.setBlurredView(mBlurredView);
        mBlurringView.invalidate();

    }

    private int getObjectColor(int attId) {
        try {
            TypedValue typedValue = new TypedValue();
            if (this.getTheme().resolveAttribute(attId, typedValue, true)) {
                return typedValue.data;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showInterstitialAd() {
        boolean b = SHOW_ADVERTISEMENT;
        if (b && ApplicationUtils.isOnline(this)) {
            DBLog.d(TAG,"=============>showInterstitialAd");
            mInterstitial = new InterstitialAd(getApplicationContext());
            mInterstitial.setAdUnitId(ADMOB_ID_INTERTESTIAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitial.loadAd(adRequest);
            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    DBLog.d(TAG, "=============>onAdLoaded");
                    mInterstitial.show();
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isAllowPressMoreToExit) {
                showQuitDialog();
            }
            else {
                pressMoreToExitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void pressMoreToExitApp() {
        if (countToExit >= 1) {
            long delaTime = System.currentTimeMillis() - pivotTime;
            if (delaTime <= 2000) {
                onDestroyData();
                finish();
                return;
            }
            else {
                countToExit = 0;
            }
        }
        pivotTime = System.currentTimeMillis();
        showToast(R.string.info_press_again_to_exit);
        countToExit++;
    }

    public void showDialogFragment(int idDialog) {
        switch (idDialog) {
            case DIALOG_LOSE_CONNECTION:
                createWarningDialog(R.string.title_warning, R.string.info_lose_internet).show();
                break;
            case DIALOG_EMPTY:
                createWarningDialog(R.string.title_warning, R.string.info_empty).show();
                break;
            case DIALOG_SEVER_ERROR:
                createWarningDialog(R.string.title_warning, R.string.info_server_error).show();
                break;
            default:
                break;
        }
    }

    public MaterialDialog createWarningDialog(int titleId, int messageId) {
        Builder mBuilder = new MaterialDialog.Builder(this);
        if (mBgColor != 0) {
            mBuilder.backgroundColor(mBgColor);
        }
        mBuilder.title(titleId);
        mBuilder.titleColor(mTextColor);
        mBuilder.content(messageId);
        mBuilder.contentColor(mTextColor);
        mBuilder.positiveColor(mAccentColor);
        mBuilder.negativeColor(mTextColor);
        mBuilder.positiveText(R.string.title_ok);
        mBuilder.autoDismiss(true);
        mBuilder.typeface(mTypefaceBold, mTypefaceLight);
        return mBuilder.build();
    }

    public MaterialDialog createFullDialog(int iconId, int mTitleId, int mYesId, int mNoId, int messageId, final IDBCallback mCallback, final IDBCallback mNeCallback) {
        Builder mBuilder = new MaterialDialog.Builder(this);
        if (mBgColor != 0) {
            mBuilder.backgroundColor(mBgColor);
        }
        mBuilder.title(mTitleId);
        if (iconId != -1) {
            mBuilder.iconRes(iconId);
        }
        mBuilder.titleColor(mTextColor);
        mBuilder.content(messageId);
        mBuilder.contentColor(mTextColor);
        mBuilder.positiveColor(mAccentColor);
        mBuilder.negativeColor(mTextColor);
        mBuilder.negativeText(mNoId);
        mBuilder.positiveText(mYesId);
        mBuilder.autoDismiss(true);
        mBuilder.typeface(mTypefaceBold, mTypefaceLight);
        mBuilder.callback(new ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                if (mNeCallback != null) {
                    mNeCallback.onAction();
                }
            }
        });
        return mBuilder.build();
    }

    public MaterialDialog createInfoDialog(int iconId, int mTitleId, int mYesId, int messageId, final IDBCallback mCallback) {
        return createInfoDialog(iconId, mTitleId, mYesId, getString(messageId), mCallback);
    }

    public MaterialDialog createInfoDialog(int iconId, int mTitleId, int mYesId, String messageId, final IDBCallback mCallback) {
        Builder mBuilder = new MaterialDialog.Builder(this);
        if (mBgColor != 0) {
            mBuilder.backgroundColor(mBgColor);
        }
        mBuilder.title(mTitleId);
        if (iconId != -1) {
            mBuilder.iconRes(iconId);
        }
        mBuilder.titleColor(mTextColor);
        mBuilder.content(messageId);
        mBuilder.contentColor(mTextColor);
        mBuilder.positiveColor(mAccentColor);
        mBuilder.positiveText(mYesId);
        mBuilder.autoDismiss(true);
        mBuilder.typeface(mTypefaceBold, mTypefaceLight);
        mBuilder.callback(new ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }
        });
        return mBuilder.build();
    }

    public void showFullDialog(int titleId, int message, int idPositive, int idNegative, final IDBCallback mDBCallback) {
        createFullDialog(-1, titleId, idPositive, idNegative, message, mDBCallback, null).show();
    }

    public void showQuitDialog() {
        final int index = mRandom.nextInt(5);
        int mNoId = R.string.title_rate_us;
        if (index % 2 != 0) {
            mNoId = R.string.title_more_apps;
        }
        int mTitleId = R.string.title_confirm;
        int mYesId = R.string.title_yes;
        int iconId = R.drawable.ic_launcher;
        int messageId = R.string.info_close_app;

        createFullDialog(iconId, mTitleId, mYesId, mNoId, messageId, new IDBCallback() {
            @Override
            public void onAction() {
                onDestroyData();
                finish();
            }
        }, new IDBCallback() {

            @Override
            public void onAction() {
                if (index % 2 != 0) {
                    ShareActionUtils.goToUrl(DBFragmentActivity.this, URL_MORE_APPS);
                }
                else {
                    String url = String.format(URL_FORMAT_LINK_APP, getPackageName());
                    ShareActionUtils.goToUrl(DBFragmentActivity.this, url);
                }
            }
        }).show();

    }

    private void createProgressDialog() {
        this.mProgressDialog = new Dialog(this);
        this.mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mProgressDialog.setContentView(R.layout.item_progress_bar);
        TextView mTvMessage = (TextView) mProgressDialog.findViewById(R.id.tv_message);
        mTvMessage.setTypeface(mTypefaceLight);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    public void showProgressDialog() {
        showProgressDialog(R.string.info_loading);
    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(getString(messageId));
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog != null) {
            TextView mTvMessage = (TextView) mProgressDialog.findViewById(R.id.tv_message);
            mTvMessage.setText(message);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    public void dimissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToastWithLongTime(int resId) {
        showToastWithLongTime(getString(resId));
    }

    public void showToastWithLongTime(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void doPositiveClick(int idDialog) {
        switch (idDialog) {
            case DIALOG_QUIT_APPLICATION:
                onDestroyData();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void doNegativeClick(int idDialog) {

    }

    public void onDestroyData() {

    }


    public void createArrayFragment() {
        mListFragments = new ArrayList<Fragment>();
    }

    public void addFragment(Fragment mFragment) {
        if (mFragment != null && mListFragments != null) {
            synchronized (mListFragments) {
                mListFragments.add(mFragment);
            }
        }
    }

    public void showDialogTurnOnInternetConnection(final IDBCallback mCallback) {
        createFullDialog(0, R.string.title_warning, R.string.title_settings, R.string.title_cancel,
                R.string.info_lose_internet, new IDBCallback() {
                    @Override
                    public void onAction() {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        if (mCallback != null) {
                            mCallback.onAction();
                        }
                    }
                }, null).show();
    }

    public boolean backStack(IDBCallback mCallback) {
        if (mListFragments != null && mListFragments.size() > 0) {
            int count = mListFragments.size();
            if (count > 0) {
                synchronized (mListFragments) {
                    Fragment mFragment = mListFragments.remove(count - 1);
                    if (mFragment != null) {
                        if (mFragment instanceof DBFragment) {
                            ((DBFragment) mFragment).backToHome(this);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void setAllowPressMoreToExit(boolean isAllowPressMoreToExit) {
        this.isAllowPressMoreToExit = isAllowPressMoreToExit;
    }

}
