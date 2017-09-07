package com.jingjoeh.appshortcutsample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jingjoeh on 9/5/2017 AD.
 */

public class ShortcutsHelper {
	private static final String TAG = ShortcutsHelper.class.getSimpleName();
	private ShortcutManager mShortcutManager;
	private Context mContext;

	public ShortcutsHelper(Context context) {
		mContext = context;
		mShortcutManager = context.getSystemService(ShortcutManager.class);
	}

	boolean addDynamic(String name, String val, int type) {
		Icon icon;
		Intent intent;
		if (type == MainActivity.TYPE_EMAIL) {
			// set icon with ic_email
			icon = Icon.createWithResource(mContext, R.drawable.ic_email_black_24dp);

			// intent ACTION_SENDTO to send email
			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + val)); // val = email format.
		} else {
			// type phone
			icon = Icon.createWithResource(mContext, R.drawable.ic_local_phone_black_24dp);

			//intent ACTION_DIAL open dial call
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + val));
		}

		ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, name)
				.setShortLabel(name)
				.setLongLabel(val)
				.setIcon(icon)
				.setIntent(intent).build();
		try {
			return mShortcutManager.addDynamicShortcuts(Collections.singletonList(shortcutInfo));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void disable(String shortcutId) {
		try {
			mShortcutManager.disableShortcuts(Collections.singletonList(shortcutId), mContext.getString(R.string.shortcut_disabled_message));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Toast.makeText(mContext, "Static cannot disable by API", Toast.LENGTH_SHORT).show();
		}
	}

	public void remove(String shortcutId) {
		try {
			mShortcutManager.removeDynamicShortcuts(Collections.singletonList(shortcutId));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Toast.makeText(mContext, "Static cannot remove by API", Toast.LENGTH_SHORT).show();
		}
	}


	public List<ShortcutInfo> getShortcutsInfoList() {
		List<ShortcutInfo> infoList = new ArrayList<>();
		infoList.addAll(mShortcutManager.getManifestShortcuts());
		infoList.addAll(mShortcutManager.getDynamicShortcuts());
		Log.d("getShortcutsInfoList", " " + infoList.size());
		return infoList;
	}

	public boolean canAddMoreShortCut() {
		return getShortcutsInfoList().size() < mShortcutManager.getMaxShortcutCountPerActivity();
	}


}
