package com.dts.mpossop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dts.base.clsClasses;
import com.dts.ladapter.LA_P_paramext_val;
import com.dts.ladapter.LA_sucursal;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class ParamList extends PBase {

    private ListView listview;
    private ProgressBar progressBar;
    private EditText txtf;
    private TextView lbl1;


    private wsOpenDT wso;
    private Runnable rnListaParam;

    private ArrayList<clsClasses.clsP_paramext_val> items= new ArrayList<clsClasses.clsP_paramext_val>();
    public clsClasses.clsP_paramext_val item;

    private LA_P_paramext_val adapter;

    private String flt;
    private boolean idle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_param_list);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);
            txtf = findViewById(R.id.editTextText5);
            lbl1 = findViewById(R.id.textView11);

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

    private void buscar() {
        try {
            flt=txtf.getText().toString();

            items.clear();
            adapter =new LA_P_paramext_val(this,this,items);
            listview.setAdapter(adapter);

            progressBar.setVisibility(View.VISIBLE);

            sql="SELECT ID_PARAMEXT,VALOR_PREDET,DESCRIPCION,NOTA FROM P_PARAMEXT_VAL " +
                "WHERE (ID_PARAMEXT>100) " +
                "AND (NOT (ID_PARAMEXT IN (103,106,107))) ";
            if (!flt.isEmpty()) sql+="AND (DESCRIPCION LIKE '%"+flt+"%') ";
            sql+="ORDER BY DESCRIPCION";

            wso.execute(sql,rnListaParam);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void listaParam() {
        int regs=0;

        try {
            if (wso.errflag) throw new Exception(wso.error);

            Cursor dt=wso.openDTCursor;

            items.clear();

            if (dt.getCount()>0) {
                dt.moveToFirst();regs=dt.getCount();

                while (!dt.isAfterLast()) {
                    item=clsCls.new clsP_paramext_val();

                    item.id_paramext=dt.getInt(0);
                    item.valor_predet=dt.getString(1);
                    item.descripcion=dt.getString(2);
                    item.nota=dt.getString(3);

                    items.add(item);
                    dt.moveToNext();
                }
            }

            adapter =new LA_P_paramext_val(this,this,items);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;
        progressBar.setVisibility(View.INVISIBLE);
        lbl1.setText("Registros: "+regs);

    }

    //endregion

    //region Aux


    //endregion

    //region Dialogs


    //endregion

    //region Activity Events


    //endregion

}