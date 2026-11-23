package com.dts.fbase;

import androidx.annotation.NonNull;


import com.dts.base.clsClasses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//#FIREBASE_ORDEN_20231016
public class fbOrden extends fbBase {

    public int new_id;
    public String transerr="", snapstr;
    public boolean transresult=false,transstat=false;

    public clsClasses.clsT_orden item,litem;
    public ArrayList<clsClasses.clsT_orden> items= new ArrayList<clsClasses.clsT_orden>();

    private Runnable rnListener;

    private clsClasses.clsT_orden tritem;

    private int maxid,tr_newid,tr_cant,ln;


    public fbOrden(String troot, int idsucursal, String idorden) {
        super(troot);
        idsuc=idsucursal;
        node=idorden;

        refnode=fdb.getReference(root+"/"+idsuc+"/"+node+"/");
    }

    public void setItem(int itemid, clsClasses.clsT_orden item) {
        fdt=fdb.getReference(root+"/"+idsuc+"/"+node+"/"+itemid);
        fdt.setValue(item);
    }

    public void setItem(clsClasses.clsT_orden item) {
        fdt=fdb.getReference(root+"/"+idsuc+"/"+item.corel+"/"+item.id);
        fdt.setValue(item);
    }

    public void removeKey() {
        fdb.getReference(root).child(""+idsuc).child(node).removeValue();
    }

    public void removeItem(int prodid) {
        fdb.getReference(root).child(""+idsuc).child(node).child(""+prodid).removeValue();
    }

    public void getItem(String itemid,Runnable rnCallback) {
        errflag=false;error="";itemexists=false;

        fdb.getReference(root+"/"+idsuc+"/"+node+"/"+itemid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot res=task.getResult();

                    try {

                        item=clsCls.new clsT_orden();

                        item.id=res.child("id").getValue(Integer.class);
                        item.corel=res.child("corel").getValue(String.class);
                        item.producto=res.child("producto").getValue(String.class);
                        item.empresa=res.child("empresa").getValue(String.class);
                        item.um=res.child("um").getValue(String.class);
                        item.cant=res.child("cant").getValue(Double.class);
                        item.umstock=res.child("umstock").getValue(String.class);
                        item.factor=res.child("factor").getValue(Double.class);
                        item.precio=res.child("precio").getValue(Double.class);
                        item.imp=res.child("imp").getValue(Double.class);
                        item.des=res.child("des").getValue(Double.class);
                        item.desmon=res.child("desmon").getValue(Double.class);;
                        item.total=res.child("total").getValue(Double.class);
                        item.preciodoc=res.child("preciodoc").getValue(Double.class);
                        item.peso=res.child("peso").getValue(Double.class);
                        item.val1=res.child("val1").getValue(Double.class);
                        item.val2=res.child("val2").getValue(String.class);
                        item.val3=res.child("val3").getValue(Double.class);
                        item.val4=res.child("val4").getValue(String.class);
                        item.percep=res.child("percep").getValue(Double.class);
                        item.cuenta=res.child("cuenta").getValue(Integer.class);
                        item.estado=res.child("estado").getValue(Integer.class);
                        item.idmesero=res.child("idmesero").getValue(Integer.class);

                        itemexists=true;

                    } catch (Exception ee) {}

                callBack=rnCallback;
                    runCallBack();
                } else {
                    error=task.getException().getMessage();errflag=true;
                    try {
                        throw new Exception(task.getException().getMessage());
                    } catch (Exception e) {}
                }
            }
        });
    }

    public void listItems(Runnable rnCallback) {

        try {
            items.clear();

            fdb.getReference(root+"/"+idsuc+"/"+node+"/").
                    get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            items.clear();
                            errflag=false;listresult=false;error="";snapstr="";

                            if (task.isSuccessful()) {

                                DataSnapshot res=task.getResult();

                                if (res.exists()) {

                                    for (DataSnapshot snap : res.getChildren()) {

                                        try {
                                            ln=0;

                                            try {
                                                snapstr=snap.toString();
                                            } catch (Exception e) {
                                                snapstr="[  ]";
                                            }

                                            litem=clsCls.new clsT_orden();

                                            litem.id=snap.child("id").getValue(Integer.class);ln++;
                                            litem.corel=snap.child("corel").getValue(String.class);ln++;
                                            litem.producto=snap.child("producto").getValue(String.class);ln++;
                                            litem.empresa=snap.child("empresa").getValue(String.class);ln++;
                                            litem.um=snap.child("um").getValue(String.class);ln++;
                                            litem.cant=snap.child("cant").getValue(Double.class);ln++;
                                            litem.umstock=snap.child("umstock").getValue(String.class);ln++;
                                            litem.factor=snap.child("factor").getValue(Double.class);ln++;
                                            litem.precio=snap.child("precio").getValue(Double.class);ln++;
                                            litem.imp=snap.child("imp").getValue(Double.class);ln++;
                                            litem.des=snap.child("des").getValue(Double.class);ln++;
                                            litem.desmon=snap.child("desmon").getValue(Double.class);ln++;
                                            litem.total=snap.child("total").getValue(Double.class);ln++;
                                            litem.preciodoc=snap.child("preciodoc").getValue(Double.class);ln++;
                                            litem.peso=snap.child("peso").getValue(Double.class);ln++;
                                            litem.val1=snap.child("val1").getValue(Double.class);ln++;
                                            litem.val2=snap.child("val2").getValue(String.class);ln++;
                                            litem.val3=snap.child("val3").getValue(Double.class);ln++;
                                            litem.val4=snap.child("val4").getValue(String.class);ln++;
                                            litem.percep=snap.child("percep").getValue(Double.class);ln++;
                                            litem.cuenta=snap.child("cuenta").getValue(Integer.class);ln++;
                                            litem.estado=snap.child("estado").getValue(Integer.class);ln++;

                                            try {
                                                litem.idmesero=snap.child("idmesero").getValue(Integer.class);ln++;
                                            } catch (Exception e) {
                                                litem.idmesero=0;
                                            }

                                            items.add(litem);

                                        } catch (Exception e) {
                                            error = e.getMessage()+" ["+ln+"] ";
                                            errflag = true;break;
                                        }
                                    }

                                    listresult=true;

                                } else {
                                    listresult=false;error="Not syncronized";
                                }
                            } else {
                                error=task.getException().getMessage();errflag=true;
                            }

                            callBack=rnCallback;
                            runCallBack();
                        }
                    });
        } catch (Exception e) {
            String ss=e.getMessage();
            errflag=true;
        }
    }

    public void listItems(String idorden,Runnable rnCallback) {
        try {
            items.clear();
            errflag=false;error="";

            fdb.getReference(root+"/"+idsuc+"/"+idorden+"/").
                    get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            items.clear();

                            if (task.isSuccessful()) {

                                DataSnapshot res=task.getResult();

                                if (res.exists()) {

                                    long ii=res.getChildrenCount();

                                    for (DataSnapshot snap : res.getChildren()) {

                                        try {

                                            litem=clsCls.new clsT_orden();

                                            litem.id=snap.child("id").getValue(Integer.class);
                                            litem.corel=snap.child("corel").getValue(String.class);
                                            litem.producto=snap.child("producto").getValue(String.class);
                                            litem.empresa=snap.child("empresa").getValue(String.class);
                                            litem.um=snap.child("um").getValue(String.class);
                                            litem.cant=snap.child("cant").getValue(Double.class);
                                            litem.umstock=snap.child("umstock").getValue(String.class);
                                            litem.factor=snap.child("factor").getValue(Double.class);
                                            litem.precio=snap.child("precio").getValue(Double.class);
                                            litem.imp=snap.child("imp").getValue(Double.class);
                                            litem.des=snap.child("des").getValue(Double.class);
                                            litem.desmon=snap.child("desmon").getValue(Double.class);;
                                            litem.total=snap.child("total").getValue(Double.class);
                                            litem.preciodoc=snap.child("preciodoc").getValue(Double.class);
                                            litem.peso=snap.child("peso").getValue(Double.class);
                                            litem.val1=snap.child("val1").getValue(Double.class);
                                            litem.val2=snap.child("val2").getValue(String.class);
                                            litem.val3=snap.child("val3").getValue(Double.class);
                                            litem.val4=snap.child("val4").getValue(String.class);
                                            litem.percep=snap.child("percep").getValue(Double.class);
                                            litem.cuenta=snap.child("cuenta").getValue(Integer.class);
                                            litem.estado=snap.child("estado").getValue(Integer.class);

                                            try {
                                                litem.idmesero=snap.child("idmesero").getValue(Integer.class);
                                            } catch (Exception e) {
                                                litem.idmesero=0;
                                            }

                                            items.add(litem);

                                        } catch (Exception e) {
                                            error = e.getMessage();
                                            errflag = true;break;
                                        }
                                    }
                                }

                            } else {
                                error=task.getException().getMessage();errflag=true;
                            }

                            callBack=rnCallback;
                            runCallBack();
                        }
                    });
        } catch (Exception e) {
            String ss=e.getMessage();
            errflag=true;
        }
    }

    public void browseItems(Runnable rnCallback) {
        try {
            items.clear();

            fdb.getReference(root+"/"+idsuc+"/").
                    get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            items.clear();
                            errflag=false;listresult=false;error="";

                            if (task.isSuccessful()) {

                                DataSnapshot res=task.getResult();

                                if (res.exists()) {

                                    for (DataSnapshot subnode : res.getChildren()) {
                                        for (DataSnapshot snap : subnode.getChildren()) {
                                            try {

                                                litem=clsCls.new clsT_orden();

                                                litem.id=snap.child("id").getValue(Integer.class);
                                                litem.corel=snap.child("corel").getValue(String.class);
                                                litem.producto=snap.child("producto").getValue(String.class);
                                                litem.empresa=snap.child("empresa").getValue(String.class);
                                                litem.um=snap.child("um").getValue(String.class);
                                                litem.cant=snap.child("cant").getValue(Double.class);
                                                litem.umstock=snap.child("umstock").getValue(String.class);
                                                litem.factor=snap.child("factor").getValue(Double.class);
                                                litem.precio=snap.child("precio").getValue(Double.class);
                                                litem.imp=snap.child("imp").getValue(Double.class);
                                                litem.des=snap.child("des").getValue(Double.class);
                                                litem.desmon=snap.child("desmon").getValue(Double.class);;
                                                litem.total=snap.child("total").getValue(Double.class);
                                                litem.preciodoc=snap.child("preciodoc").getValue(Double.class);
                                                litem.peso=snap.child("peso").getValue(Double.class);
                                                litem.val1=snap.child("val1").getValue(Double.class);
                                                litem.val2=snap.child("val2").getValue(String.class);
                                                litem.val3=snap.child("val3").getValue(Double.class);
                                                litem.val4=snap.child("val4").getValue(String.class);
                                                litem.percep=snap.child("percep").getValue(Double.class);
                                                litem.cuenta=snap.child("cuenta").getValue(Integer.class);
                                                litem.estado=snap.child("estado").getValue(Integer.class);

                                                try {
                                                    litem.idmesero=snap.child("idmesero").getValue(Integer.class);
                                                } catch (Exception e) {
                                                    litem.idmesero=0;
                                                }

                                                items.add(litem);

                                            } catch (Exception e) {
                                                error = e.getMessage();
                                                errflag = true;break;
                                            }
                                        }
                                    }

                                    listresult=true;

                                } else {
                                    listresult=false;error="Not syncronized";
                                }
                            } else {
                                error=task.getException().getMessage();errflag=true;
                            }

                            callBack=rnCallback;
                            runCallBack();
                        }
                    });
        } catch (Exception e) {
            String ss=e.getMessage();
            errflag=true;
        }
    }

    public void newid(Runnable rnCallback) {

        try {
            new_id=0;maxid=0;
            errflag=false;error="";

            fdb.getReference(root+"/"+idsuc+"/"+node+"/").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    int ival;

                    if (task.isSuccessful()) {

                        DataSnapshot res=task.getResult();

                        if (res.exists()) {
                            for (DataSnapshot snap : res.getChildren()) {
                                try {
                                    ival=snap.child("id").getValue(Integer.class);
                                    if (ival>maxid) maxid=ival;
                                } catch (Exception e) {
                                    error = e.getMessage();errflag = true;break;
                                }
                            }
                            new_id=maxid+1;
                        } else {
                            new_id=1;
                        }

                    } else {
                        error=task.getException().getMessage();errflag=true;
                    }

                    callBack=rnCallback;
                    runCallBack();
                }
            });
        } catch (Exception e) {
            String ss=e.getMessage();
            errflag=true;
        }
    }

    public void updateValue(String itemid,String field,double value) {
        fdb.getReference(root+"/"+idsuc+"/"+node+"/"+itemid).child(field).setValue(value);
    }

    public void updateValue(int itemid,String field,double value) {
        fdb.getReference(root+"/"+idsuc+"/"+node+"/"+itemid).child(field).setValue(value);
    }

    public void updatePagado(int itemid) {
        fdb.getReference(root+"/"+idsuc+"/"+node+"/"+itemid).child("estado").setValue(2);
    }

    public void updateValues(String itemid, HashMap upd) {
        String path=root+"/"+idsuc+"/"+node+"/"+itemid;

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, upd);
        fdb.getReference().updateChildren(childUpdates);
    }

    public boolean transSplitItem(clsClasses.clsT_orden titem, int tnewid,Runnable rnTrCallback) {

        try {
            tritem=titem;
            transerr="";transresult=false;transstat=false;
            tr_newid=tnewid;
            tr_cant=(int) tritem.cant;


            fdt.runTransaction(new Transaction.Handler() {
                public Transaction.Result doTransaction(MutableData mutableData) {
                    double prec_uni;
                    try {

                        removeItem(tritem.id);

                        tritem.cant=1;
                        tritem.total=tritem.precio;

                        for (int i = 0; i <tr_cant; i++) {
                            tritem.id=tr_newid+i;
                            setItem(tritem.id,tritem);
                        }

                        transresult=true;
                    } catch (Exception e) {
                        transerr=e.getMessage();transresult=false;
                    }
                    return Transaction.success(mutableData);
                }

                public void onComplete(DatabaseError databaseError, boolean complete, DataSnapshot dataSnapshot) {
                    transstat=complete;
                    callBack=rnTrCallback;
                    runCallBack();
                }
            });

            return transresult;
        } catch (Exception e) {
            return false;
        }
    }

}
