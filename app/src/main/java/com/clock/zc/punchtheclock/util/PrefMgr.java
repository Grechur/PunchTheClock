package com.clock.zc.punchtheclock.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefMgr {
	private SharedPreferences pref;

	public PrefMgr(Context ctx) {
		pref = PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	public String getString(@UniqueKeyAnn.AllKey String key) {
		return pref.getString(key, null);
	}

	public String getString(@UniqueKeyAnn.AllKey String key, String defVal) {
		return pref.getString(key, defVal);
	}

	public void putString(@UniqueKeyAnn.AllKey String key, String val) {
		pref.edit().putString(key, val).commit();
	}
	public int getInt(@UniqueKeyAnn.AllKey String key, int defVal) {
		return pref.getInt(key, defVal);
	}

	public void putLong(@UniqueKeyAnn.AllKey String key, long val) {
		pref.edit().putLong(key, val).commit();
	}

	public long getLong(@UniqueKeyAnn.AllKey String key, long defVal) {
		return pref.getLong(key, defVal);
	}

	public void putInt(@UniqueKeyAnn.AllKey String key, int val) {
		pref.edit().putInt(key, val).commit();
	}

	public float getFloat(@UniqueKeyAnn.AllKey String key, float defVal) {
		return pref.getFloat(key, defVal);
	}

	public void putFloat(@UniqueKeyAnn.AllKey String key, float val) {
		pref.edit().putFloat(key, val).commit();
	}

	public boolean getBool(@UniqueKeyAnn.AllKey String key, boolean defVal) {
		return pref.getBoolean(key, defVal);
	}

	public void putBool(@UniqueKeyAnn.AllKey String key, boolean val) {
		pref.edit().putBoolean(key, val).commit();
	}

	// 便于链式调用
	public Editor getEditor() {
		return pref.edit();
	}
}
