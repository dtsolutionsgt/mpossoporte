package com.dts.mpossop;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dts.base.clsClasses;
import com.dts.fbase.fbResSesion;
import com.dts.fbase.fbVersion;


public class ResSession extends PBase {

    private ProgressBar progressBar;
    private TextView lbl1;

    private fbResSesion fbrs;

    public clsClasses.clsP_paramext_val item;

    private int tienda;
    private boolean idle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_res_session);

            super.InitBase(savedInstanceState);

            progressBar = findViewById(R.id.progressBar);progressBar.setVisibility(View.VISIBLE);
            lbl1 = findViewById(R.id.textView11);

            setHandlers();

            tienda=371;
            fbrs=new fbResSesion("ResSesion",tienda);
            fbrs.listItemsActivos( ()-> fboList() );

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doBorrar(View view) {
        if (idle) msgask(0,"Eliminar todos?");
    }

    public void doExit(View view) {
        if (idle) finish();
    }

    public void setHandlers() {
        try {


        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

    }

    //endregion

    //region Main

    private void fboList() {
        try {
            if (fbrs.errflag) throw new Exception(fbrs.error);

            int cn=fbrs.items.size();
            lbl1.setText("Registros: "+cn);
        } catch (Exception e) {
            msgboxui(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
        idle = true;progressBar.setVisibility(View.INVISIBLE);
    }

    public void borraRegistros() {
        try {
            fbrs.fdb.getReference("ResSesion").child(""+tienda).removeValue();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Aux


    //endregion

    //region Dialogs

    public void dialogswitch() {
        try {
            switch (gl.dialogid) {
                case 0:
                    msgask(1,"Está seguro?");break;
                case 1:
                    borraRegistros();break;
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Activity Events

    @Override
    public void onResume() {
        try {
            super.onResume();

            gl.dialogr = () -> dialogswitch();

        } catch (Exception e) {
            String se =e.getMessage();
        }
    }

    //endregion

}