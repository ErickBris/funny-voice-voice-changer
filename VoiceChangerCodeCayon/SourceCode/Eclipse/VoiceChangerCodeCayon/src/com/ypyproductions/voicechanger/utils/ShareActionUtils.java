package com.ypyproductions.voicechanger.utils;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

public class ShareActionUtils {

	public static final String TAG = ShareActionUtils.class.getSimpleName();

	public static void shareToTwitter(Activity mActivity, String content) {
		try {
			Intent tweetIntent = new Intent(Intent.ACTION_SEND);
			tweetIntent.putExtra(Intent.EXTRA_TEXT, content);
			tweetIntent.setType("text/plain");

			PackageManager pm = mActivity.getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(tweetIntent, 0);
			boolean isResold = false;
			for (final ResolveInfo app : activityList) {
				if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
					tweetIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					tweetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					tweetIntent.setComponent(name);
					mActivity.startActivity(tweetIntent);
					isResold = true;
					break;
				}
			}
			if (!isResold) {
				Toast.makeText(mActivity, "Please install the twitter application!", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shareViaSMS(Activity mActivity, String content) {
		try {
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", content);
			sendIntent.setType("vnd.android-dir/mms-sms");
			mActivity.startActivity(sendIntent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mActivity, "Can not share via sms!Please try again", Toast.LENGTH_LONG).show();
		}
	}

	public static void shareViaEmail(Activity mActivity, String destEmail, String subject, String body) {
		try {
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			if (!StringUtils.isEmptyString(destEmail) && EmailUtils.isEmailAddressValid(destEmail)) {
				sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { destEmail });
			}
			sharingIntent.setType("message/rfc822");
			if (!StringUtils.isEmptyString(subject)) {
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			}
			if (!StringUtils.isEmptyString(body)) {
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
			}
			mActivity.startActivity(sharingIntent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mActivity, "Can not share via email!Please try again", Toast.LENGTH_LONG).show();
		}
	}

	public static void callNumber(Activity mActivity, String mNumberPhone) {
		try {
			if (!StringUtils.isEmptyString(mNumberPhone)) {
				mNumberPhone = mNumberPhone.replaceAll("\\)+", "");
				mNumberPhone = mNumberPhone.replaceAll("\\(+", "");
				mNumberPhone = mNumberPhone.replaceAll("\\-+", "");
				mNumberPhone = "tel:" + mNumberPhone;
				Intent mItDial = new Intent(Intent.ACTION_CALL, Uri.parse(mNumberPhone));
				mActivity.startActivity(mItDial);
			} else {
				Toast.makeText(mActivity, "No phonenumber to call!", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mActivity, "Phonenumber error!Please try again", Toast.LENGTH_SHORT).show();
		}
	}

	public static void goToUrl(Activity mActivity, String mUrl) {
		try {
			Intent mIt = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
			mIt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			mIt.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			mIt.addFlags(Intent.FLAG_FROM_BACKGROUND);
			mActivity.startActivity(mIt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
