package com.dts.mpossop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dts.base.clsClasses;
import com.dts.webservice.wsCommit;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class ParamEmpresa extends PBase {

    private ProgressBar progressBar;
    private TextView lbl1;

    private wsOpenDT wso;
    private wsCommit wsc;
    private Runnable rnListaPredeterminados,rnListaActivos,rnListaValores,rnCreaRegistros;

    public ArrayList<clsClasses.clsP_paramext> pitems= new ArrayList<clsClasses.clsP_paramext>();
    public ArrayList<clsClasses.clsP_paramext> aitems= new ArrayList<clsClasses.clsP_paramext>();
    public ArrayList<clsClasses.clsP_paramext> items= new ArrayList<clsClasses.clsP_paramext>();
    public ArrayList<clsClasses.clsP_paramext_val> vitems= new ArrayList<clsClasses.clsP_paramext_val>();
    public clsClasses.clsP_paramext item;
    public clsClasses.clsP_paramext_val vitem;

    private Cursor dt;

    private boolean idle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_param_empresa);

            super.InitBase(savedInstanceState);

            progressBar = findViewById(R.id.progressBar);
            lbl1 = findViewById(R.id.textView3);

            progressBar.setVisibility(View.VISIBLE);
            lbl1.setText("Procesando parámetros de \n"+gl.empname+"\n por favor espere . . .");

            app.getURL();
            wso=new wsOpenDT(gl.wsurl);
            wsc=new wsCommit(gl.wsurl);

            rnListaPredeterminados = () -> listaPredeterminados();
            rnListaActivos = () -> listaActivos();
            rnListaValores = () -> listaValores();
            rnCreaRegistros = () -> creaRegistros();

            listItems();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doExit(View view) {
        if (idle) finish();
    }

    //endregion

    //region Main

    private void listItems() {
        try {
            pitems.clear();aitems.clear();items.clear();vitems.clear();

            sql="SELECT DISTINCT ID,NOMBRE FROM P_PARAMEXT WHERE (EMPRESA=24) AND (ID>=100) AND (ID<500)";
            wso.execute(sql,rnListaPredeterminados);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }

    private void listaPredeterminados() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item = clsCls.new clsP_paramext();
                    item.id=dt.getInt(0);
                    item.nombre=dt.getString(1);
                    pitems.add(item);

                    dt.moveToNext();
                }

                sql="SELECT DISTINCT ID,NOMBRE FROM P_PARAMEXT WHERE (EMPRESA="+gl.empid+") AND (ID>=100) AND (ID<500)";
                wso.execute(sql,rnListaActivos);
            } else {
                throw new Exception("No existen parametros predeterminados. Proceso cancelado.");
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }

    private void listaActivos() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    item = clsCls.new clsP_paramext();
                    item.id=dt.getInt(0);
                    item.nombre=dt.getString(1);
                    aitems.add(item);

                    dt.moveToNext();
                }
            } else {
                throw new Exception("No existen parametros predeterminados. Proceso cancelado.");
            }

            sql="SELECT ID_PARAMEXT,VALOR_PREDET,DESCRIPCION,NOTA FROM P_PARAMEXT_VAL";
            wso.execute(sql,rnListaValores);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }

    private void listaValores() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            dt=wso.openDTCursor;

            if (dt.getCount()>0) {
                dt.moveToFirst();
                while (!dt.isAfterLast()) {
                    vitem = clsCls.new clsP_paramext_val();

                    vitem.id_paramext=dt.getInt(0);
                    vitem.valor_predet=dt.getString(1);
                    vitem.descripcion=dt.getString(2);
                    vitem.nota=dt.getString(3);

                    vitems.add(vitem);

                    dt.moveToNext();
                }
            } else {
                throw new Exception("No existen parametros predeterminados. Proceso cancelado.");
            }

            creaListaPendientes();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }


    private void creaListaPendientes() {
        try {
            for (int i = 0; i <pitems.size(); i++) {
                if (!Contains(pitems.get(i).id)) {
                    item=pitems.get(i);

                    item.empresa=gl.empid;
                    item.idruta="";
                    item.valor=valor(item.id);

                    items.add(item);
                }
            }

            creaPendientes();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            doFinish();
        }
    }

    private void creaPendientes() {
        String cmd="";

        try {
            if (items.size()>0) {
                for (int i = 0; i < items.size(); i++) {
                    cmd += addItemSql(items.get(i)) + ";";
                }

                wsc.execute(cmd,rnCreaRegistros);
            } else {
                msgboxexit("Parámetros creados.");
                doFinish();
            }
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
                msgboxexit("Parámetros creados.");
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        doFinish();
    }

    //endregion

    //region Aux

    private boolean Contains(int id) {
        for (int i = 0; i <aitems.size(); i++) {
            if (aitems.get(i).id==id) return true;
        }

        return false;
    }

    private String valor(int id) {
        for (int i = 0; i <vitems.size(); i++) {
            if (vitems.get(i).id_paramext==id) return vitems.get(i).valor_predet;
        }
        return "N";
    }

    private void doFinish() {
        idle=true;
        lbl1.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }

    public String addItemSql(clsClasses.clsP_paramext item) {

        ins.init("P_paramext");

        ins.add("EMPRESA",item.empresa);
        ins.add("ID",item.id);
        ins.add("IDRUTA",item.idruta);
        ins.add("NOMBRE",item.nombre);
        ins.add("VALOR",item.valor);

        return ins.sql();

    }

    //endregion

    //region Dialogs

    private void msgboxexit(String msg) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("MPos Soporte");
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    //endregion

    //region Activity Events


    //endregion

}