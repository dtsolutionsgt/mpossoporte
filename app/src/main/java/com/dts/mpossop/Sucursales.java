package com.dts.mpossop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.dts.base.clsClasses;
import com.dts.ladapter.LA_sucursal;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class Sucursales extends PBase {

    private ListView listview;
    private ProgressBar progressBar;
    private EditText txtf;

    private wsOpenDT wso;
    private Runnable rnListaParam;

    private ArrayList<clsClasses.clsSucursal> items= new ArrayList<clsClasses.clsSucursal>();
    public clsClasses.clsSucursal item;

    private LA_sucursal adapter;
    private String s,flt;
    private boolean idle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sucursales);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);
            txtf = findViewById(R.id.editTextText4);

            setHandlers();

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);
            rnListaParam = () -> listaParam();

            buscar();

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }


    //region Events

    public void doSearch(View view) {
        buscar();
    }

    public void doExit(View view) {
        if (idle) finish();
    }

    public void doClear(View view) {
        txtf.setText("");
    }

    public void setHandlers() {
        try {

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object lvObj = listview.getItemAtPosition(position);
                    item = (clsClasses.clsSucursal)lvObj;

                    adapter.setSelectedIndex(position);

                    s=item.empresa+"\n\n"+
                      item.descripcion+"\n\n"+
                      item.nombre+"\n\n"+
                      item.direccion+"\n\n"+
                      item.nit+"\n\n"+
                      item.texto;

                    msgbox(s);
                };
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    //endregion

    //region Main

    private void buscar() {
        try {
            flt=txtf.getText().toString();

            items.clear();
            adapter =new LA_sucursal(this,this,items);
            listview.setAdapter(adapter);

            sql="SELECT P_SUCURSAL.CODIGO_SUCURSAL, P_EMPRESA.NOMBRE, P_SUCURSAL.DESCRIPCION, " +
                "P_SUCURSAL.NOMBRE AS Expr1, P_SUCURSAL.DIRECCION, P_SUCURSAL.NIT, P_SUCURSAL.TEXTO " +
                "FROM  P_SUCURSAL INNER JOIN P_EMPRESA ON P_SUCURSAL.EMPRESA = P_EMPRESA.EMPRESA ";
            if (!flt.isEmpty()) {
                sql+="WHERE  (P_EMPRESA.NOMBRE LIKE '%"+flt+"%') " +
                     "OR (P_SUCURSAL.DESCRIPCION LIKE '%"+flt+"%') " +
                     "OR (P_SUCURSAL.NOMBRE LIKE '%"+flt+"%') " +
                     "OR (P_SUCURSAL.DIRECCION LIKE '%"+flt+"%') " +
                     "OR (P_SUCURSAL.NIT LIKE '%"+flt+"%') " +
                     "OR (P_SUCURSAL.TEXTO LIKE '%"+flt+"%') ";
            }
            sql+="ORDER BY  P_SUCURSAL.DESCRIPCION";

            progressBar.setVisibility(View.VISIBLE);
            wso.execute(sql,rnListaParam);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void listaParam() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            items.clear();

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item=clsCls.new clsSucursal();

                    item.id=dt.getInt(0);
                    item.empresa=dt.getString(1);
                    item.descripcion=dt.getString(2);
                    item.nombre=dt.getString(3);
                    item.direccion=dt.getString(4);
                    item.nit=dt.getString(5);
                    item.texto=dt.getString(6);

                    items.add(item);
                    dt.moveToNext();
                }
            }

            adapter =new LA_sucursal(this,this,items);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;
        progressBar.setVisibility(View.INVISIBLE);
    }

    //endregion

    //region Aux


    //endregion

    //region Dialogs


    //endregion

    //region Activity Events


    //endregion

}