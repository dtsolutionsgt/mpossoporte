package com.dts.mpossop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dts.base.clsClasses;
import com.dts.ladapter.LA_Cierre;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CierreDia extends PBase {

    private ListView listview;
    private ProgressBar pbar;
    private TextView lbl1,lbl2;
    private CheckBox cbox1;

    private wsOpenDT wso;

    private LA_Cierre adapter;

    private ArrayList<clsClasses.clsCierre> items = new ArrayList<clsClasses.clsCierre>();
    private ArrayList<clsClasses.clsCierre> ritems = new ArrayList<clsClasses.clsCierre>();
    private ArrayList<clsClasses.clsCierreRuta> rfitems = new ArrayList<clsClasses.clsCierreRuta>();

    private clsClasses.clsCierre item,ritem;
    private clsClasses.clsCierreRuta rfitem;

    private boolean idle=false,pendientes=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cierre_dia);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            lbl1 = findViewById(R.id.textView14);
            lbl2 = findViewById(R.id.textView15);
            cbox1 = findViewById(R.id.checkBox);
            pbar = findViewById(R.id.progressBar3);

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);

            buildList();

            setHandlers();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doExit(View view) {
        if (idle) finish(); else toast("Espere . . . . ");
    }

    private void setHandlers() {
        try {
            cbox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendientes = !cbox1.isChecked();
                    filterItems();
                }
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Main

    private void buildList() {
        try {

            sql="SELECT P_EMPRESA.EMPRESA, P_EMPRESA.NOMBRE, P_SUCURSAL.CODIGO_SUCURSAL,P_SUCURSAL.DESCRIPCION, P_SUCURSAL.SUCURSAL_DEMO, P_SUCURSAL.ACTIVO, " +
                    " P_RUTA.CODIGO_RUTA, P_RUTA.NOMBRE AS Expr1, P_RUTA.ACTIVO AS Expr2 " +
                    "FROM  P_EMPRESA INNER JOIN P_SUCURSAL ON P_EMPRESA.EMPRESA = P_SUCURSAL.EMPRESA INNER JOIN " +
                    "P_RUTA ON P_EMPRESA.EMPRESA = P_RUTA.EMPRESA AND P_SUCURSAL.CODIGO_SUCURSAL = P_RUTA.SUCURSAL " +
                    "WHERE  (P_SUCURSAL.ACTIVO = 1) AND (P_RUTA.ACTIVO = 1)  AND " +
                    "(P_EMPRESA.EMPRESA >0) ";


            sql="SELECT P_EMPRESA.EMPRESA, P_EMPRESA.NOMBRE, P_SUCURSAL.CODIGO_SUCURSAL,P_SUCURSAL.DESCRIPCION, P_SUCURSAL.SUCURSAL_DEMO, P_SUCURSAL.ACTIVO, " +
                    " P_RUTA.CODIGO_RUTA, P_RUTA.NOMBRE AS Expr1, P_RUTA.ACTIVO AS Expr2 " +
                    "FROM  P_EMPRESA INNER JOIN P_SUCURSAL ON P_EMPRESA.EMPRESA = P_SUCURSAL.EMPRESA INNER JOIN " +
                    "P_RUTA ON P_EMPRESA.EMPRESA = P_RUTA.EMPRESA AND P_SUCURSAL.CODIGO_SUCURSAL = P_RUTA.SUCURSAL " +
                    "WHERE  (P_SUCURSAL.ACTIVO = 1) AND (P_RUTA.ACTIVO = 1) AND (P_SUCURSAL.SUCURSAL_DEMO = 0) AND " +
                    "(P_EMPRESA.EMPRESA >0) ";


            wso.execute(sql,() -> processList());
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    private void processList() {
        try {
            ritems.clear();

            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    ritem =clsCls.new clsCierre();

                    ritem.rid=dt.getInt(6);

                    ritem.enombre=dt.getString(1);
                    ritem.snombre=dt.getString(3);
                    ritem.rnombre=dt.getString(7);
                    ritem.nombre= ritem.enombre+ ritem.snombre+ ritem.rnombre;

                    ritem.fecha=0;
                    ritem.estado=0;
                    ritem.bandera=0;

                    ritem.eid=dt.getInt(0);
                    ritem.sid=dt.getInt(2);

                    ritems.add(ritem);

                    dt.moveToNext();
                }
            }

            buildData();

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }

    }

    private void buildData() {
        try {
            long ff=du.getActDate();ff=du.addDays(ff,-3);
            long fl=du.getActDate();fl=du.addDays(fl,+1);
            String sd=du.univfechash(ff);
            String sl=du.univfechash(fl);

            sql="SELECT RUTA, dbo.AndrDate(fec_agr) FROM P_CAJACIERRE " +
                "WHERE  (fec_agr >= '"+sd+"') AND (fec_agr < '"+sl+"') AND (ESTADO = 1) ORDER BY RUTA, fec_agr";

            wso.execute(sql,() -> processData());
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    private void processData() {
        int pp = 0;

        try {


            rfitems.clear();

            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {

                    rfitem =clsCls.new clsCierreRuta();
                    rfitem.rid=dt.getInt(0);
                    rfitem.fecha=dt.getLong(1);

                    rfitems.add(rfitem);

                    dt.moveToNext();
                }
            }


            for (clsClasses.clsCierreRuta itm : rfitems) {
                pp=posrec(itm.rid);
                if (pp>-1) ritems.get(pp).fecha=itm.fecha;
            }

            filterItems();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+pp+"   "+e.getMessage());
        }

        idle=true;pbar.setVisibility(View.INVISIBLE);
    }

    private void filterItems() {
        boolean morning;
        long ff, fl, fi;
        int comp,pend;

        try {
            ff=du.getActDateTime();
            morning=du.gethour(ff)<12;
            fl=du.ffecha00(ff)+1200;fi=fl;

            if (morning) {
                fi=du.addDays(ff,-1);fi=du.ffecha00(fi);
            }

            items.clear();comp=0;pend=0;

            for (clsClasses.clsCierre itm : ritems) {
                if (itm.fecha>0) {
                    item=itm;
                    item.bandera=0;
                    if (morning) {
                        if (item.fecha>=fi) item.bandera=1;
                    } else {
                        if (item.fecha>=fl) item.bandera=1;
                    }

                    if (item.bandera==1) comp++; else pend++;

                    if (pendientes) {
                        if (item.bandera==0)  items.add(item);
                    } else {
                        if (item.bandera==1)  items.add(item);
                    }
                }
            }

            Collections.sort(items, new Comparator<clsClasses.clsCierre>() {
                @Override
                public int compare(clsClasses.clsCierre p1, clsClasses.clsCierre p2) {
                    return p1.nombre.compareTo(p2.nombre);
                }
            });

            adapter =new LA_Cierre(this,this, items);
            listview.setAdapter(adapter);

            lbl1.setText("Pendiente: "+pend);
            lbl2.setText("Completo: "+comp);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+"."+e.getMessage());
        }
    }

    private int posrec(int rid) {
        int pp=0;
        for (clsClasses.clsCierre itm : ritems) {
            if (itm.rid==rid) return pp;
            pp++;
        }

        return -1;
    }

    //endregion

    //region Dialogs


    //endregion

    //region Aux


    //endregion

    //region Activity Events



    //endregion


}