package com.dts.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

public class appGlobals extends Application {

    public Context context;

    public int empid,menuid;
	public String wsurl,empname;

	public Runnable dialogr;
	public int dialogid;

	@Override
	public void onCreate() {
		super.onCreate();
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
	}

	public void saveInstance(Bundle savedInstanceState) {
		try {
			savedInstanceState.putInt("empid", empid);
			savedInstanceState.putInt("menuid", menuid);

			savedInstanceState.putString("wsurl", wsurl);
			savedInstanceState.putString("empname", empname);
        } catch (Exception e) {}
	}

	public void restoreInstance(Bundle savedInstanceState) {
		try {
			empid =savedInstanceState.getInt("empid");
			menuid =savedInstanceState.getInt("menuid");

			wsurl =savedInstanceState.getString("wsurl");
			empname =savedInstanceState.getString("empname");
		} catch (Exception e) {}
	}

	private void toastlong(String msg) {
		Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
