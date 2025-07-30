package com.dts.fbase;

import android.os.Handler;

import com.dts.base.clsClasses;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fbBase {

    public FirebaseDatabase fdb;
    public DatabaseReference fdt;
    public DatabaseReference refnode;

    public Runnable callBack;

    public clsClasses clsCls=new clsClasses();

    public boolean errflag,itemexists,listresult;
    public String node,error;
    public int idsuc,warcount;

    public String value,root;

    public fbBase(String troot) {
        fdb = FirebaseDatabase.getInstance();
        //fdb.setPersistenceEnabled(true); declarar en PBase.onCreate
        root=troot;
        fdt=fdb.getReference(root);
        fdt.keepSynced(true);
        callBack=null;
   }

    public void runCallBack() {
        if (callBack==null) return;

        final Handler cbhandler = new Handler();
        cbhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.run();
            }
        }, 50);

    }

    public void removeValue(String key) {
        fdb.getReference(root+"/"+key).removeValue();
    }

    public String key() {
        return fdt.push().getKey();
    }

}
