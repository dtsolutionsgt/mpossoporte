package com.dts.mpossop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dts.base.clsClasses;
import com.dts.fbase.fbVersion;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class VersionGenera extends PBase {

    private TextView lblstat, lblbtn;
    private ProgressBar pbar;

    private wsOpenDT wso;

    private fbVersion fbv;

    private ArrayList<clsClasses.clsfbVersion> items= new ArrayList<clsClasses.clsfbVersion>();
    private clsClasses.clsfbVersion item;


    private boolean idle=false;
    private int pos,limite,idemp,idruta;
    private String ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_version_genera);

            super.InitBase(savedInstanceState);

            lblstat = findViewById(R.id.textView12);lblstat.setText("");
            lblbtn  = findViewById(R.id.textView13);lblbtn.setVisibility(View.INVISIBLE);
            pbar = findViewById(R.id.progressBar2);pbar.setVisibility(View.VISIBLE);

            fbv=new fbVersion("Version");

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);

            buildList();

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doGenera(View view) {
        if (!idle) return;

        try {
            lblbtn.setVisibility(View.INVISIBLE);
            pbar.setVisibility(View.INVISIBLE);

            pos=0;
            //limite=150;

            Handler mtimer = new Handler();
            Runnable mrunner= () -> {
                procesaValor();
            };
            mtimer.postDelayed(mrunner,200);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    public void doExit(View view) {
         if (idle) finish(); else toast("Espere . . . . ");
    }

    //endregion

    //region Main

    private void buildList() {
        try {

            pos=0;limite=0;

            sql="SELECT P_EMPRESA.EMPRESA, P_EMPRESA.NOMBRE, P_SUCURSAL.CODIGO_SUCURSAL,P_SUCURSAL.DESCRIPCION, P_SUCURSAL.SUCURSAL_DEMO, P_SUCURSAL.ACTIVO, " +
                " P_RUTA.CODIGO_RUTA, P_RUTA.NOMBRE AS Expr1, P_RUTA.ACTIVO AS Expr2 " +
                "FROM  P_EMPRESA INNER JOIN P_SUCURSAL ON P_EMPRESA.EMPRESA = P_SUCURSAL.EMPRESA INNER JOIN " +
                "P_RUTA ON P_EMPRESA.EMPRESA = P_RUTA.EMPRESA AND P_SUCURSAL.CODIGO_SUCURSAL = P_RUTA.SUCURSAL " +
                "WHERE  (P_SUCURSAL.ACTIVO = 1) AND (P_RUTA.ACTIVO = 1) AND (P_SUCURSAL.SUCURSAL_DEMO = 0) AND " +
                "(P_EMPRESA.EMPRESA >0) ";

            wso.execute(sql,() -> processList());
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void processList() {
        try {
            items.clear();

            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item=clsCls.new clsfbVersion();

                    item.actver="-";
                    item.eid=dt.getInt(0);
                    item.enombre=dt.getString(1);
                    item.rid=dt.getInt(6);
                    item.rnombre=dt.getString(7);
                    item.sid=dt.getInt(2);
                    item.snombre=dt.getString(3);

                    //fbv.setItem(item);

                    items.add(item);
                    dt.moveToNext();
                }
            }

            if (items.size()>0) {
                pos=0;
                limite=items.size()-1;
                lblstat.setText("0 / "+(limite+1));
                lblbtn.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;pbar.setVisibility(View.INVISIBLE);
    }

    private void procesaValor() {
        try {
            lblstat.setText((pos+1)+" / "+(limite+1));

            idemp=items.get(pos).eid;
            idruta=items.get(pos).rid;

            sql="SELECT TOP 1 VEHICULO FROM D_FACTURA WHERE (EMPRESA="+idemp+") AND (RUTA="+idruta+") ORDER BY FECHAENTR DESC";
            wso.execute(sql,() -> aplicaValor());

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    private void aplicaValor() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            if (pos>=limite) {
                guardaValores();
            } else {

                if (wso.openDTCursor.getCount()>0) {

                    wso.openDTCursor.moveToFirst();
                    ver= wso.openDTCursor.getString(0);

                    item=items.get(pos);
                    item.actver=ver;

                    fbv.setItem(item);
                }

                pos++;
                Handler mtimer = new Handler();
                Runnable mrunner= () -> {
                    procesaValor();
                };
                mtimer.postDelayed(mrunner,200);
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    private void guardaValores() {
        try {
                msgbox("Completo");
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    //endregion

    //region Dialogs

    public void dialogswitch() {
        try {
            switch (gl.dialogid) {
                case 0:
                    ;break;

            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Aux


    //endregion

    //region Activity Events

    @Override
    public void onResume() {
        try {
            super.onResume();

            gl.dialogr = () -> {dialogswitch();};


        } catch (Exception e) { }
    }

    //endregion


}