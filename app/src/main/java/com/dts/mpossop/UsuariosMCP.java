package com.dts.mpossop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dts.base.clsClasses;
import com.dts.ladapter.LA_P_paramext_val;
import com.dts.ladapter.LA_usuarios;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class UsuariosMCP extends PBase {

    private ListView listview;
    private ProgressBar progressBar;

    private wsOpenDT wso;
    private Runnable rnListaParam;

    private ArrayList<clsClasses.clsP_paramext_val> items= new ArrayList<clsClasses.clsP_paramext_val>();
    public clsClasses.clsP_paramext_val item;

    private LA_usuarios adapter;

    private boolean idle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_usuarios_mcp);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);

            setHandlers();

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);
            rnListaParam = () -> listaParam();

            sql="SELECT  P_EMPRESA.NOMBRE, Users.Empresa, Users.UserName " +
                "FROM  Users INNER JOIN P_EMPRESA ON Users.Empresa=P_EMPRESA.EMPRESA " +
                "ORDER BY P_EMPRESA.NOMBRE";
            wso.execute(sql,rnListaParam);

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
                    item = (clsClasses.clsP_paramext_val)lvObj;

                    adapter.setSelectedIndex(position);

                    msgbox(item.nota);
                };
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    //endregion

    //region Main

    private void listaParam() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            items.clear();

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item=clsCls.new clsP_paramext_val();

                    item.id_paramext=dt.getInt(1);
                    item.valor_predet=dt.getString(0);
                    item.descripcion=dt.getString(2);
                    item.nota="";

                    items.add(item);
                    dt.moveToNext();
                }
            }

            adapter =new LA_usuarios(this,this,items);
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