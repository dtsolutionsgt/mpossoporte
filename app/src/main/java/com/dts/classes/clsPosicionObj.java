package com.dts.classes;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dts.base.BaseDatos;
import com.dts.base.clsClasses;

public class clsPosicionObj {

    public int count;

    private Context cont;
    private BaseDatos Con;
    private SQLiteDatabase db;
    public BaseDatos.Insert ins;
    public BaseDatos.Update upd;
    private clsClasses clsCls = new clsClasses();

    private String sel="SELECT * FROM Posicion";
    private String sql;
    public ArrayList<clsClasses.clsPosicion> items= new ArrayList<clsClasses.clsPosicion>();

    public clsPosicionObj(Context context, BaseDatos dbconnection, SQLiteDatabase dbase) {
        cont=context;
        Con=dbconnection;
        ins=Con.Ins;upd=Con.Upd;
        db = dbase;
        count = 0;
    }

    public void reconnect(BaseDatos dbconnection, SQLiteDatabase dbase) {
        Con=dbconnection;
        ins=Con.Ins;upd=Con.Upd;
        db = dbase;
    }

    public void add(clsClasses.clsPosicion item) {
        addItem(item);
    }

    public void update(clsClasses.clsPosicion item) {
        updateItem(item);
    }

    public void delete(clsClasses.clsPosicion item) {
        deleteItem(item);
    }

    public void delete(int id) {
        deleteItem(id);
    }

    public void fill() {
        fillItems(sel);
    }

    public void fill(String specstr) {
        fillItems(sel+ " "+specstr);
    }

    public void fillSelect(String sq) {
        fillItems(sq);
    }

    public clsClasses.clsPosicion first() {
        return items.get(0);
    }


    //region Private

    private void addItem(clsClasses.clsPosicion item) {

        ins.init("Posicion");

        ins.add("id",item.id);
        ins.add("posicion",item.posicion);

        db.execSQL(ins.sql());

    }

    private void updateItem(clsClasses.clsPosicion item) {

        upd.init("Posicion");

        upd.add("posicion",item.posicion);

        upd.Where("(id="+item.id+")");

        db.execSQL(upd.sql());


    }

    private void deleteItem(clsClasses.clsPosicion item) {
        sql="DELETE FROM Posicion WHERE (id="+item.id+")";
        db.execSQL(sql);
    }

    private void deleteItem(int id) {
        sql="DELETE FROM Posicion WHERE id=" + id;
        db.execSQL(sql);
    }

    private void fillItems(String sq) {
        Cursor dt;
        clsClasses.clsPosicion item;

        items.clear();

        dt=Con.OpenDT(sq);
        count =dt.getCount();
        if (dt.getCount()>0) dt.moveToFirst();

        while (!dt.isAfterLast()) {

            item = clsCls.new clsPosicion();

            item.id=dt.getInt(0);
            item.posicion=dt.getInt(1);

            items.add(item);

            dt.moveToNext();
        }

        if (dt!=null) dt.close();

    }

    public int newID(String idsql) {
        Cursor dt=null;
        int nid;

        try {
            dt=Con.OpenDT(idsql);
            dt.moveToFirst();
            nid=dt.getInt(0)+1;
        } catch (Exception e) {
            nid=1;
        }

        if (dt!=null) dt.close();

        return nid;
    }

    public int newCor(String idsql) {
        Cursor dt=null;
        int nid;

        try {
            String sel="SELECT MAX( + idsql + ) FROM Posicion";
            dt=Con.OpenDT(sel);
            dt.moveToFirst();
            nid=dt.getInt(0)+1;
        } catch (Exception e) {
            nid=1;
        }

        if (dt!=null) dt.close();

        return nid;
    }

    public String addItemSql(clsClasses.clsPosicion item) {

        ins.init("Posicion");

        ins.add("id",item.id);
        ins.add("posicion",item.posicion);

        return ins.sql();

    }

    public String updateItemSql(clsClasses.clsPosicion item) {

        upd.init("Posicion");

        upd.add("posicion",item.posicion);

        upd.Where("(id="+item.id+")");

        return upd.sql();


    }

    //endregion
}

