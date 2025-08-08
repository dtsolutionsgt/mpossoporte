package com.dts.base;

public class clsClasses {

    public class clsfbPrecio {
        public int codigo;
        public int nivel;
        public String um;
        public double precio;
    }

    public class clsfbVersion {
        public String actver;
        public int  eid;
        public String enombre;
        public int  rid;
        public String rnombre;
        public int  sid;
        public String snombre;
    }

    public class clsCierre {
        public int  rid;
        public String nombre;
        public String enombre;
        public String rnombre;
        public String snombre;
        public long   fecha;
        public int estado;
        public int bandera;
        public int  eid;
        public int  sid;
    }

    public class clsCierreRuta {
        public int  rid;
        public long fecha;
    }

    public class clsMenu {
        public int  id;
        public String nombre;
    }

    public class clsList {
        public int  id;
        public String nombre;
    }

    public class clsPosicion {
        public int  id;
        public int  posicion;
    }

    public class clsP_paramext {
        public int  codigo_paramext;
        public int  empresa;
        public int  id;
        public String idruta;
        public String nombre;
        public String valor;
    }

    public class clsP_paramext_val {
        public int  id_paramext;
        public String valor_predet;
        public String descripcion;
        public String nota;
    }

    public class clsSucursal {
        public int  id;
        public String empresa;
        public String descripcion;
        public String nombre;
        public String direccion;
        public String nit;
        public String texto;
        public long fecha;
    }

    //endregion

}
