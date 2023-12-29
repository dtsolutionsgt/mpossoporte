package com.dts.mpossop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dts.base.clsClasses;
import com.dts.webservice.wsCommit;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class ParamNuevo extends PBase {

    private ProgressBar progressBar;
    private TextView lbl1;
    private EditText txt1,txt2,txt3;

    private wsOpenDT wso;
    private wsCommit wsc;
    private Runnable rnNuevoCodigo,rnListaEmpresas,rnCreaRegistros;

    private Cursor dt;
    private int newid;
    private boolean idle=false;
    private String cmd,desc,valor,nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_param_nuevo);

            super.InitBase(savedInstanceState);

            progressBar = findViewById(R.id.progressBar);
            lbl1 = findViewById(R.id.textView3);
            txt1 = findViewById(R.id.editTextText);
            txt2 = findViewById(R.id.editTextText2);
            txt3 = findViewById(R.id.editTextText3);

            progressBar.setVisibility(View.VISIBLE);
            lbl1.setText("");

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);
            wsc=new wsCommit(gl.wsurl);

            rnNuevoCodigo = () -> nuevoCodigo();
            rnListaEmpresas = () -> listaEmpresas();
            rnCreaRegistros = () -> creaRegistros();

            sql="SELECT MAX(ID) FROM P_PARAMEXT WHERE (EMPRESA=24) AND (ID<500)";
            wso.execute(sql,rnNuevoCodigo);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doExit(View view) {
        if (idle) finish();
    }

    public void doSave(View view) {
        try {
            desc=txt1.getText().toString();
            valor=txt2.getText().toString();
            nota=txt3.getText().toString();

            if (desc.isEmpty()) {
                msgbox("Falta descripcion corta");txt1.requestFocus();return;
            }

            if (desc.isEmpty()) {
                msgbox("Falta valor predeterminado");txt2.requestFocus();return;
            }

            if (nota.isEmpty()) nota=desc;

            msgAskSave();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Main

    private void nuevoCodigo() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            dt=wso.openDTCursor;
            dt.moveToFirst();
            newid=dt.getInt(0)+1;

            doFinish();
            lbl1.setText("#"+newid);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }

    private void listaEmpresas() {
        clsClasses.clsP_paramext item;
        clsClasses.clsP_paramext_val vitem;

        try {
            if (wso.errflag) throw new Exception(wso.error);

            vitem=clsCls.new clsP_paramext_val();

            vitem.id_paramext=newid;
            vitem.valor_predet=valor;
            vitem.descripcion=desc;
            vitem.nota=nota;

            cmd=addItemSql(vitem) + ";";


            dt=wso.openDTCursor;

            dt.moveToFirst();
            while (!dt.isAfterLast()) {

                item=clsCls.new clsP_paramext();

                item.empresa=dt.getInt(0);
                item.id=newid;
                item.idruta="";
                item.nombre=desc;
                item.valor=valor;

                cmd+=addItemSql(item) + ";";

                dt.moveToNext();
            }

            progressBar.setVisibility(View.VISIBLE);
            wsc.execute(cmd,rnCreaRegistros);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }

    }

    private void creaRegistros() {
        try {
            if (wsc.errflag) {
                throw new Exception(wsc.error);
            } else {
                msgboxexit();
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        doFinish();
    }

    //endregion

    //region Aux

    public String addItemSql(clsClasses.clsP_paramext item) {

        ins.init("P_paramext");

        ins.add("EMPRESA",item.empresa);
        ins.add("ID",item.id);
        ins.add("IDRUTA",item.idruta);
        ins.add("NOMBRE",item.nombre);
        ins.add("VALOR",item.valor);

        return ins.sql();

    }

    public String addItemSql(clsClasses.clsP_paramext_val item) {

        ins.init("P_paramext_val");

        ins.add("ID_PARAMEXT",item.id_paramext);
        ins.add("VALOR_PREDET",item.valor_predet);
        ins.add("DESCRIPCION",item.descripcion);
        ins.add("NOTA",item.nota);

        return ins.sql();

    }

    private void doFinish() {
        idle=true;
        lbl1.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }

    //endregion

    //region Dialogs

    private void msgboxexit() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("MPos Soporte");
        dialog.setMessage("Proceso completo");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void msgAskSave() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Mpos");
        dialog.setMessage("¿Crear parametro para todas las empresas activas?");

        dialog.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sql="SELECT EMPRESA FROM P_EMPRESA WHERE (ACTIVO=1) ORDER BY EMPRESA";
                    wso.execute(sql,rnListaEmpresas);
                } catch (Exception e) {
                    msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
                }
            }
        });

        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });

        dialog.show();

    }

    //endregion

    //region Activity Events


    //endregion

}