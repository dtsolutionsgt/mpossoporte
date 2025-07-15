package com.dts.mpossop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.dts.base.clsClasses;
import com.dts.classes.extListDlg;
import com.dts.fbase.fbVersion;
import com.dts.ladapter.LA_Version;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VersionCantidad extends PBase {

    private ListView listview;
    private ProgressBar pbar;

    private fbVersion fbv;

    private LA_Version adapter;

    private ArrayList<clsClasses.clsMenu> items= new ArrayList<clsClasses.clsMenu>();
    private ArrayList<clsClasses.clsMenu> emps= new ArrayList<clsClasses.clsMenu>();

    private ArrayList<String> vers= new ArrayList<String>();
    private ArrayList<Integer> cants= new ArrayList<Integer>();

    private boolean idle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_version_cantidad);

            super.InitBase(savedInstanceState);

            listview = findViewById(R.id.listview1);
            pbar = findViewById(R.id.progressBar3);

            fbv=new fbVersion("Version");
            fbv.listItems(() -> procesaVersiones());

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
                    clsClasses.clsMenu item = (clsClasses.clsMenu)lvObj;

                    adapter.setSelectedIndex(position);

                    listEmpresas(item.nombre);
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

            items.clear();

            for (int i = 0; i <vers.size(); i++) {
                item=clsCls.new clsMenu();
                item.nombre=vers.get(i);
                item.id=cants.get(i);

                items.add(item);
            }

            Collections.sort(items, new Comparator<clsClasses.clsMenu>() {
                @Override
                public int compare(clsClasses.clsMenu p1, clsClasses.clsMenu p2) {
                    return p2.nombre.compareTo(p1.nombre);
                }
            });

            adapter =new LA_Version(this,this,items,true);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }

        idle=true;pbar.setVisibility(View.INVISIBLE);
    }

    private void procesaVersiones() {
        String vn;
        int pos,cn;

        try {

            if (fbv.errflag) throw new Exception(fbv.error);

            vers.clear();cants.clear();

            for (clsClasses.clsfbVersion item : fbv.items) {
                vn=item.actver;

                pos=vers.indexOf(vn);
                if (pos<0) {
                    vers.add(vn);
                    cants.add(1);
                } else {
                    cn=cants.get(pos)+1;
                    cants.set(pos,cn);
                }

            }

            listItems();
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
            idle=true;pbar.setVisibility(View.INVISIBLE);
        }
    }

    private void listEmpresas(String vname) {
        clsClasses.clsMenu eitem;
        String vn;
        int pos;

        try {
            emps.clear();vers.clear();cants.clear();

            for (clsClasses.clsfbVersion item : fbv.items) {
                vn=item.enombre;

                if (item.actver.equalsIgnoreCase(vname)) {
                    pos = vers.indexOf(vn);
                    if (pos < 0) {
                        vers.add(vn);
                        cants.add(item.eid);
                    }
                }
            }

            for (int i = 0; i <vers.size(); i++) {
                eitem=clsCls.new clsMenu();
                eitem.nombre=vers.get(i);
                eitem.id=cants.get(i);

                emps.add(eitem);
            }

            Collections.sort(emps, new Comparator<clsClasses.clsMenu>() {
                @Override
                public int compare(clsClasses.clsMenu p1, clsClasses.clsMenu p2) {
                    return p1.nombre.compareTo(p2.nombre);
                }
            });

            extListDlg listdlg = new extListDlg();
            listdlg.buildDialog(VersionCantidad.this,"Empresas");
            listdlg.setWidth(5000);


            for (clsClasses.clsMenu itm : emps) {
                listdlg.add(itm.nombre);
            }

            listdlg.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    listdlg.dismiss();
                } catch (Exception e) {}
            });

            listdlg.setOnLeftClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listdlg.dismiss();
                }
            });

            listdlg.show();


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

    @Override
    public void onBackPressed() {
        if (idle) super.onBackPressed();
    }

    //endregion

}