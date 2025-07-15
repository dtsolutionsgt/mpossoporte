package com.dts.mpossop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.dts.base.clsClasses;
import com.dts.classes.extListDlg;
import com.dts.ladapter.LA_Menu;
import com.dts.webservice.wsOpenDT;

import java.util.ArrayList;

public class Principal extends PBase {

    private ListView menuview;

    private LA_Menu adapter;

    private wsOpenDT wso;

    private ArrayList<clsClasses.clsMenu> cats= new ArrayList<clsClasses.clsMenu>();

    private int cjCaja,cjSuc,cjEmp;
    private String cnCaja,cnSuc,cnEmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_principal);

            super.InitBase(savedInstanceState);

            menuview = (ListView) findViewById(R.id.listview1);

            setHandlers();

           app.getURL();
            wso=new wsOpenDT(gl.wsurl);
            //rnListaParam = () -> listaParam();

            listItems();

        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //region Events

    public void doDeveloper(View view) {
        inputValor();
        //startActivity(new Intent(Principal.this,ParamNuevo.class));
    }

    public void setHandlers() {
        try {

            menuview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object lvObj = menuview.getItemAtPosition(position);
                    clsClasses.clsMenu item = (clsClasses.clsMenu)lvObj;

                    adapter.setSelectedIndex(position);

                    switch (item.id) {
                        case 0:
                            gl.menuid=0;
                            startActivity(new Intent(Principal.this,EmpresaLista.class));break;
                        case 1:
                            gl.menuid=1;
                            startActivity(new Intent(Principal.this,ParamList.class));break;
                        case 2:
                            gl.menuid=2;
                            startActivity(new Intent(Principal.this,UsuariosMCP.class));break;
                        case 3:
                            gl.menuid=3;
                            startActivity(new Intent(Principal.this,Sucursales.class));break;
                        case 4:
                            gl.menuid=4;
                            cjInput();
                        case 5:
                            gl.menuid=5;
                            showVersMenu();
                    }

                    callback=gl.menuid+1;
                };
            });
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Main

    private void listItems() {
        try {
            cats.clear();

            addMenuCat(0,"Parámetros por empresa");
            addMenuCat(1,"Lista de parámetros ");
            addMenuCat(2,"Usuarios MCP");
            addMenuCat(3,"Sucursales");
            addMenuCat(4,"Caja");
            addMenuCat(5,"Version");

            adapter =new LA_Menu(this,this,cats);
            menuview.setAdapter(adapter);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void procesaParametros() {
        if (gl.empid>0) msgask(1,"¿Generar parámetros para la empresa "+gl.empname+"?");
    }

    //endregion

    //region Caja

    private void cjInput() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.DialogTheme);

        alert.setTitle("Número caja");

        final EditText input = new EditText(this);
        alert.setView(input);

        input.setInputType(InputType.TYPE_CLASS_NUMBER );
        input.setText("");
        input.requestFocus();

        alert.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    cjCaja=Integer.parseInt(input.getText().toString());
                    cjCaja();
                } catch (Exception e) {
                    mu.msgbox("Valor incorrecto");return;
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        AlertDialog dialog = alert.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }


    private void cjCaja() {
        try {
            cjSuc=0;cjEmp=0;cnCaja="";cnSuc="";cnEmp="";

            sql="SELECT NOMBRE, SUCURSAL FROM P_RUTA WHERE  (CODIGO_RUTA="+cjCaja+")";
            wso.execute(sql, () -> cjSucursal() );
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void cjSucursal() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            cnCaja=wso.openDTCursor.getString(0);
            cjSuc=wso.openDTCursor.getInt(1);

            sql="SELECT DESCRIPCION, EMPRESA FROM P_SUCURSAL WHERE (CODIGO_SUCURSAL="+cjSuc+")";
            wso.execute(sql, () -> cjEmpresa() );
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void cjEmpresa() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            cnSuc=wso.openDTCursor.getString(0);
            cjEmp=wso.openDTCursor.getInt(1);

            sql="SELECT NOMBRE FROM P_EMPRESA WHERE (EMPRESA="+cjEmp+")";
            wso.execute(sql, () -> cjResult() );
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    private void cjResult() {
        try {
            if (wso.errflag) throw new Exception(wso.error);

            cnEmp=wso.openDTCursor.getString(0);

            String s="C("+cjCaja+") "+cnCaja+"\n" +
                     "S("+cjSuc+") "+cnSuc+"\n" +
                     "E("+cjEmp+") "+cnEmp+"\n";
            msgbox(s);
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    //endregion

    //region Aux

    private void addMenuCat(int mid,String ss) {
        clsClasses.clsMenu item=clsCls.new clsMenu();
        item.id=mid;
        item.nombre=ss;
        cats.add(item);
    }

    //endregion

    //region Dialogs

    public void dialogswitch() {
        try {
            switch (gl.dialogid) {
                case 1:
                    startActivity(new Intent(Principal.this,ParamEmpresa.class));break;
                case 2:
                    ;break;
                case 3:
                    ;break;
            }
        } catch (Exception e) {
            msgbox(new Object(){}.getClass().getEnclosingMethod().getName()+" . "+e.getMessage());
        }
    }

    public void showVersMenu() {

        try {

            extListDlg listdlg = new extListDlg();
            listdlg.buildDialog(Principal.this,"Version");
            listdlg.setWidth(5000);

            listdlg.add("Cantidad");
            listdlg.add("Empresa");
            //listdlg.add("Generar");

            listdlg.setOnItemClickListener((parent, view, position, id) -> {

                try {
                    int vitemid=0;

                    String ss = listdlg.items.get(position).text;

                    if (ss.equalsIgnoreCase("Cantidad")) vitemid=1;
                    if (ss.equalsIgnoreCase("Empresa")) vitemid=2;
                    if (ss.equalsIgnoreCase("Generar")) vitemid=99;

                    switch (vitemid) {
                        case 1:
                            startActivity(new Intent(Principal.this,VersionCantidad.class));break;
                        case 2:
                            startActivity(new Intent(Principal.this,VersionEmpresa.class));break;
                        case 99:
                            startActivity(new Intent(Principal.this,VersionGenera.class));break;
                    }

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

    private void inputValor() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Contraseña desarrollo");

        final EditText input = new EditText(this);
        alert.setView(input);

        input.setText("");
        input.requestFocus();

        alert.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    String s=input.getText().toString();
                    if (s.equalsIgnoreCase("ADMIN")) startActivity(new Intent(Principal.this,ParamNuevo.class));
                 } catch (Exception e) {
                    mu.msgbox("Valor incorrecto");return;
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    //endregion

    //region Activity Events

    @Override
    public void onResume() {
        try {
            super.onResume();

            gl.dialogr = () -> {dialogswitch();};

            if (callback==1) {
                callback=0;
                procesaParametros();
                return;
            }


        } catch (Exception e) { }
    }

    //endregion

}