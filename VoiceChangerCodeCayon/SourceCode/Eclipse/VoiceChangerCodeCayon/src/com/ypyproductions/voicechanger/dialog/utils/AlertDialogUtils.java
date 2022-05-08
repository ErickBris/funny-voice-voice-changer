package com.ypyproductions.voicechanger.dialog.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogUtils {

	public static Dialog createInfoDialog(Context mContext, int iconId, int titleId, int idPositive, int messageId, final IOnDialogListener mOnDialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if(iconId!=0){
			builder.setIcon(iconId);
		}
		builder.setTitle(titleId).
		setMessage(messageId).setCancelable(false).setPositiveButton(idPositive, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonPositive();
				}
			}
		});
		return builder.create();
	}

	public static Dialog createInfoDialog(Context mContext, int iconId, int titleId, int idPositive, 
			String messageId, final IOnDialogListener mOnDialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if(iconId!=0){
			builder.setIcon(iconId);
		}
		builder.setTitle(titleId).setMessage(messageId).setCancelable(false).setPositiveButton(idPositive, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonPositive();
				}
			}
		});
		return builder.create();
	}

	public static Dialog createFullDialog(Context mContext, int iconId, int titleId, int idPositive, int idNegative,
			String messageId, final IOnDialogListener mOnDialogListener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if(iconId!=0){
			builder.setIcon(iconId);
		}
		builder.setTitle(titleId).setMessage(messageId).setCancelable(false).setPositiveButton(idPositive, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonPositive();
				}
			}
		}).setNegativeButton(idNegative, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonNegative();
				}
			}
		});
		return builder.create();
	}

	public static Dialog createFullDialog(Context mContext, int iconId, int titleId, int idPositive, int idNegative, 
			int messageId, final IOnDialogListener mOnDialogListener) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if(iconId!=0){
			builder.setIcon(iconId);
		}
		builder.setTitle(titleId).setMessage(messageId).setCancelable(false).setPositiveButton(idPositive, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonPositive();
				}
			}
		}).setNegativeButton(idNegative, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mOnDialogListener != null) {
					mOnDialogListener.onClickButtonNegative();
				}
			}
		});
		return builder.create();
	}

	public interface IOnDialogListener {
		public void onClickButtonPositive();
		public void onClickButtonNegative();
	}

}
