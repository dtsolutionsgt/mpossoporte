package com.dts.mpossop;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.dts.base.AppMethods;
import com.dts.base.BaseDatos;
import com.dts.base.DateUtils;
import com.dts.base.MiscUtils;
import com.dts.base.appGlobals;
import com.dts.base.clsClasses;

import java.io.File;

public class PBase extends Activity {

    public int active;
    public SQLiteDatabase db;
    public BaseDatos Con;
    public BaseDatos.Insert ins;
    public BaseDatos.Update upd;
    public String sql, DBPath, DBName;

    public appGlobals gl;
    public MiscUtils mu;
    public DateUtils du;
    public AppMethods app;
    public clsClasses clsCls = new clsClasses();

    public int callback =0,mode;
    public int selid,selidx;
    public long fecha,stamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbase);
    }

    public void InitBase(Bundle savedInstanceState) {
        DBName="mpossop.db";
        DBPath =getStorage()+"/"+DBName;

        Con = new BaseDatos(this, DBPath);
        opendb();
        ins=Con.Ins;upd=Con.Upd;

        gl=((appGlobals) this.getApplication());
        gl.context=this;

        mu=new MiscUtils(this,gl);
        du=new DateUtils();
        app=new AppMethods(this,gl,Con,db);

        fecha=du.getActDateTime();stamp=du.getActDate();

        selid=-1;selidx=-1;
        callback =0;

        holdInstance(savedInstanceState);

    }

    //region Common

    public String getStorage() {

        String sd= getApplicationContext().getExternalFilesDir("").getAbsolutePath();

        try {
            File directory = new File(sd);
            directory.mkdirs();
        } catch (Exception e) {
            String ss=e.getMessage();
        }

        return sd;
    }

    //endregion

    //region Database

    public void opendb() {
        try {
            db = Con.getWritableDatabase();
            Con.vDatabase =db;
            active=1;
        } catch (Exception e) {
            mu.msgbox(e.getMessage());
            active= 0;
        }
    }

    public void exec() {
        db.execSQL(sql);
    }

    public Cursor open() {
        Cursor dt;

        dt=Con.OpenDT(sql);
        try {
            dt.moveToFirst();
        } catch (Exception e) {
        }

        return dt;
    }

    //endregion

    //region Web service callback

    public void wsCallBack(int callmode, Boolean throwing, String errmsg) throws Exception {
        if (throwing) throw new Exception(errmsg);
    }

    //endregion

    //region Messages

    public void toast(String msg) {

        if (mu.emptystr(msg)) return;

        Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void toastlong(String msg) {

        if (mu.emptystr(msg)) return;

        Toast toast= Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void toast(double val) {
        this.toast(""+val);
    }

    public void msgask(int dialogid,String msg){
        gl.dialogid=dialogid;
        mu.msgask(dialogid,msg);
    }

    public void msgbox(String msg){
        mu.msgbox(msg);
    }

    public void msgbox(int val){
        mu.msgbox(""+val);
    }

    public void msgbox(double val){
        mu.msgbox(""+val);
    }

    //endregion

    //region Instance

    public void holdInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) gl.restoreInstance(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        gl.saveInstance(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gl.restoreInstance(savedInstanceState);
    }

    //endregion

    //region Activity Events

    @Override
    public void onResume() {
        try {
            app.reconnect(Con, db);
        } catch (Exception e) { }
        opendb();
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
            Con.close();   }
        catch (Exception e) { }
        active= 0;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //endregion
}