package com.productividadcc.utilerias;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Globales {

    private final static boolean logOn = true;

    public final static String STR_VACIO = "";
    public final static String URL_REGISTRO_GRUPO =         "http://asistente.crediclub.com/2.0/altaGrupo.php?tokenID=%s&empleadoID=%s&nomContacto=%s&telcontacto=%s&refcontacto=%s&fechaEstDes=%s&fecCapac=%s&latitu=%s&longit=%s";
    public final static String URL_REGISTRO_RECONTRATACION  ="http://asistente.crediclub.com/2.0/recontratacion.php?tokenID=%s&empleadoID=%s&grupo=%s&nomContacto=%s&telcontacto=%s&fechaEstDes=%s&fecRecontra=%s&latitu=%s&longit=%s";
    public final static String URL_ACTUALIZAR_ETAPA         ="http://asistente.crediclub.com/2.0/actualizaEtapa.php?tokenID=%s&empleadoID=%s&idAgenda=%s&etapa=%s&nombreGrupo=%s&monto=%s&integrantes=%s&intnuevos=%s&intrenova=%s&fecha=%s&fechaestimada=%s&tipodispersion=%s&tipogrupo=%s&latitu=%s&longit=%s";
    public final static String URL_REPROGRAMAR_ETAPA        ="http://asistente.crediclub.com/2.0/reprogramacion.php?tokenID=%s&empleadoID=%s&idAgenda=%s&etapa=%s&fechaRep=%s&motivoRep=%s&latitu=%s&longit=%s";
    public final static String URL_CANCELAR_ETAPA           ="http://asistente.crediclub.com/2.0/cancelacion.php?tokenID=%s&empleadoID=%s&idAgenda=%s&etapa=%s&fecha=%s&motivo=%s&latitu=%s&longit=%s";
    public final static String URL_CONSULTA_EMPLEADO =      "http://asistente.crediclub.com/2.0/consultaEmpleado.php?empleadoID=";



    public static void log(String tag, String msg)
    {

        if (logOn)
        {
            Log.d(tag, "" + msg);
        }

    }

    public static void sleeper(int i)
    {
        try
        {
            Thread.sleep(i);
        }
        catch (InterruptedException e)
        {
        }
    }

    public static boolean isWifiOnline(Context mContext)
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi != null && mWifi.isConnected();

    }

}