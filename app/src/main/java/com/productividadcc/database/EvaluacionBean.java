package com.productividadcc.database;

/**
 * Created by Israel Cortes on 15/05/2017.
 */
public class EvaluacionBean {

    public EvaluacionBean(){
        super();
    }

    String urlWS;
    String IMEI;
    String empleado;

    public String getUrlWS() {
        return urlWS;
    }

    public void setUrlWS(String urlWS) {
        this.urlWS = urlWS;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
}
