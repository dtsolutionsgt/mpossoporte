package com.dts.mpossop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dts.base.clsClasses;
import com.dts.fbase.fbEnviodb;
import com.dts.fbase.fbVersion;
import com.dts.ladapter.LA_Enviodb;
import com.dts.ladapter.LA_Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EnvioDB extends PBase {

    private ListView listview;
    private ProgressBar pbar;

    private fbEnviodb fbed;

    private LA_Enviodb adapter;

    private ArrayList<clsClasses.clsfbVersion> items= new ArrayList<clsClasses.clsfbVersion>();
    private clsClasses.clsfbVersion selitem;

    private boolean idle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_envio_db);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            pbar = findViewById(R.id.progressBar3);

            fbed=new fbEnviodb("Enviodb");
            fbed.listItems(() -> listItems());

            setHandlers();
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
                    selitem = (clsClasses.clsfbVersion)lvObj;

                    adapter.setSelectedIndex(position);

                    msgask(1,"Eliminar registro?");
                }
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Main

    private void listItems() {
        clsClasses.clsMenu item;

        try {
            if (fbed.errflag) throw new Exception(fbed.error);

            items.clear();

            for (int i = 0; i <fbed.items.size(); i++) {
                items.add(fbed.items.get(i));
            }

            Collections.sort(items, new Comparator<clsClasses.clsfbVersion>() {
                @Override
                public int compare(clsClasses.clsfbVersion p1, clsClasses.clsfbVersion p2) {
                    return p2.actver.compareTo(p1.actver);
                }
            });

            adapter =new LA_Enviodb(this,this,items);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;pbar.setVisibility(View.INVISIBLE);
    }

    private void deleteItem() {
        try {
            fbed.removeValue(""+selitem.rid);
            idle=false;
            fbed.listItems(() -> listItems());
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Dialogs

    public void dialogswitch() {
        try {
            switch (gl.dialogid) {
                case 1:
                    deleteItem();break;
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


        } catch (Exception e) {
            String se =e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        if (idle) super.onBackPressed();
    }

    //endregion

}