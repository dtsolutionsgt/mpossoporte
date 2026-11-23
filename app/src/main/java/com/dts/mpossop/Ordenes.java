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
import com.dts.fbase.fbOrden;
import com.dts.fbase.fbOrdenEstado;
import com.dts.ladapter.LA_ordenes;
import com.dts.webservice.wsOpenDT;
import java.util.ArrayList;


public class Ordenes extends PBase {

    private ListView listview;
    private ProgressBar progressBar;
    private EditText txtf;
    private TextView lbl1;

    private wsOpenDT wso;
    private Runnable rnListaParam;

    private fbOrden fbo;
    private fbOrdenEstado fboe;

    private ArrayList<clsClasses.clsP_paramext_val> items= new ArrayList<clsClasses.clsP_paramext_val>();
    public ArrayList<clsClasses.clsT_orden> ordenes= new ArrayList<clsClasses.clsT_orden>();

    public clsClasses.clsP_paramext_val item;

    private LA_ordenes adapter;

    private String flt;
    private boolean idle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ordenes);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);
            txtf = findViewById(R.id.editTextText5);
            lbl1 = findViewById(R.id.textView11);

            setHandlers();

            app.getURL();

            fbo=new fbOrden("Orden",371,"");
            fboe=new fbOrdenEstado("OrdenEstado",371);

            fbo.browseItems( ()-> fboList() );

            wso=new wsOpenDT(gl.wsurl);
            //rnListaParam = () -> listaParam();

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

    private void fboList() {
        try {
            ordenes.clear();

            for (clsClasses.clsT_orden it:fbo.items) {
                ordenes.add(it);
            }

            fboe.listItems( ()-> fboeList() );
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle = true;
        }
    }

    private void fboeList() {
        try {
            if (fboe.warcount>0) {
                //msgbox("No se logro determinar estado de cuentas.\nPor favor revise con la caja si falta alguna cuenta para pagar.");
            }

            showItems();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle = true;
        }
    }

    private void showItems() {
        int regs=0;

        try {

            items.clear();

            for (int i = 0; i <ordenes.size(); i++) {
                item=clsCls.new clsP_paramext_val();

                item.id_paramext=ordenes.get(i).estado;
                item.valor_predet="";
                item.descripcion=ordenes.get(i).corel;
                item.nota="";

                items.add(item);
            }

            adapter =new LA_ordenes(this,this,items);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;
        progressBar.setVisibility(View.INVISIBLE);
        lbl1.setText("Registros: "+items.size());

    }


    private void buscar() {
        try {
            flt=txtf.getText().toString();

            items.clear();
            adapter =new LA_ordenes(this,this,items);
            listview.setAdapter(adapter);

            progressBar.setVisibility(View.VISIBLE);

            sql="SELECT ID_PARAMEXT,VALOR_PREDET,DESCRIPCION,NOTA FROM P_PARAMEXT_VAL " +
                    "WHERE (ID_PARAMEXT>100) " +
                    "AND (NOT (ID_PARAMEXT IN (103,106,107,109,112,130,154))) ";
            if (!flt.isEmpty()) {
                sql+="AND ((DESCRIPCION LIKE '%"+flt+"%') OR (NOTA LIKE '%"+flt+"%')) ";
            }
            sql+="ORDER BY DESCRIPCION";

            wso.execute(sql,rnListaParam);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }


    //endregion

    //region Aux


    //endregion

    //region Dialogs


    //endregion

    //region Activity Events


    //endregion

}