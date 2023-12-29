package com.dts.mpossop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.dts.base.clsClasses;
import com.dts.ladapter.LA_Menu;

import java.util.ArrayList;

public class Principal extends PBase {

    private ListView menuview;

    private LA_Menu adapter;

    private ArrayList<clsClasses.clsMenu> cats= new ArrayList<clsClasses.clsMenu>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_principal);

            super.InitBase(savedInstanceState);

            menuview = (ListView) findViewById(R.id.listview1);

            setHandlers();
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