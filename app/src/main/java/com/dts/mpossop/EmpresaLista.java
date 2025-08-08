package com.dts.mpossop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dts.base.clsClasses;
import com.dts.ladapter.LA_List;
import com.dts.ladapter.LA_Menu;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class EmpresaLista extends PBase {

    private ListView listview;
    private ProgressBar progressBar;

    private wsOpenDT wso;

    private ArrayList<clsClasses.clsList> items= new ArrayList<clsClasses.clsList>();

    private LA_List adapter;

    private boolean idle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_empresa_lista);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);

            gl.empid=0;

            setHandlers();

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);

            sql="SELECT EMPRESA, NOMBRE FROM P_EMPRESA WHERE (ACTIVO=1) ORDER BY NOMBRE";
            wso.execute(sql,this::listaEmpresas);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doExit(View view) {
        if (idle) finish();
    }

    public void setHandlers() {
        try {

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object lvObj = listview.getItemAtPosition(position);
                    clsClasses.clsList item = (clsClasses.clsList)lvObj;

                    adapter.setSelectedIndex(position);

                    gl.empid=item.id;
                    gl.empname=item.nombre;

                    idle=true;
                    finish();
                };
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    //endregion

    //region Main

    private void listaEmpresas() {
        clsClasses.clsList item;

        try {
            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            items.clear();

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item=clsCls.new clsList();

                    item.id=dt.getInt(0);
                    item.nombre=dt.getString(1)+" ("+item.id+")";

                    items.add(item);
                    dt.moveToNext();
                }
            }

            adapter =new LA_List(this,this,items);
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

    @Override
    public void onBackPressed() {
        if (idle) super.onBackPressed();
    }

    //endregion

}