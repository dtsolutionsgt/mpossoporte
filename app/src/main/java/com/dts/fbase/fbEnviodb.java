package com.dts.fbase;

import androidx.annotation.NonNull;

import com.dts.base.clsClasses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class fbEnviodb extends fbBase {

    public clsClasses.clsfbVersion item,litem;
    public ArrayList<clsClasses.clsfbVersion> items= new ArrayList<clsClasses.clsfbVersion>();

    public fbEnviodb(String troot) {
        super(troot);
    }

    public void setItem(clsClasses.clsfbVersion item) {
        fdt=fdb.getReference(root+"/"+item.rid);
        fdt.setValue(item);
    }

    public void listItems(Runnable rnCallback) {
        try {

            fdb.getReference(root+"/").
                    get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            int estado;
                            items.clear();

                            if (task.isSuccessful()) {
                                DataSnapshot res=task.getResult();
                                if (res.exists()) {

                                    for (DataSnapshot snap : res.getChildren()) {

                                        try {

                                            litem = clsCls.new clsfbVersion();

                                            litem.actver = snap.child("actver").getValue(String.class);
                                            litem.eid = snap.child("eid").getValue(Integer.class);
                                            litem.enombre = snap.child("enombre").getValue(String.class);
                                            litem.rid = snap.child("rid").getValue(Integer.class);
                                            litem.rnombre = snap.child("rnombre").getValue(String.class);
                                            litem.sid = snap.child("sid").getValue(Integer.class);
                                            litem.snombre = snap.child("snombre").getValue(String.class);

                                            items.add(litem);
                                        } catch (Exception e) {
                                            error = e.getMessage();errflag = true;break;
                                        }

                                    }
                                }

                                errflag=false;
                            } else {
                                error=task.getException().getMessage();
                                errflag=true;
                            }

                            callBack=rnCallback;
                            runCallBack();
                        }
                    });
        } catch (Exception e) {
            errflag=true;
            String ss=e.getMessage();
        }

    }
}
