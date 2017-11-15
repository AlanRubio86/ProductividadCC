package com.productividadcc.utilerias;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.productividadcc.webservice.VisitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Globales {

    private final static boolean logOn = true;

    private final static String PREF_LAST_CHECKIN_STATUS 	= "pref_last_checkin_status";
    private final static String PREF_LAST_CHECKIN_TYPE	 	= "pref_last_checkin_type";
    private final static String PREF_LAST_CHECKIN_GROUP	 	= "pref_last_checkin_group";
    private final static String PREF_LAST_CHECKIN_NOTES	 	= "pref_last_checkin_notes";

    public final static String STR_VACIO = "";
    public final static String STR_CERO = "0";
    public final static String STR_FECVAC = "1901-01-01";

    public final static String URL_REGISTRO_GRUPO =         "http://asistente.crediclub.com/2.0/altaGrupo.php?tokenID=%s&empleadoID=%s&nomContacto=%s&telcontacto=%s&refcontacto=%s&fechaEstDes=%s&fecCapac=%s&latitu=%s&longit=%s";
    public final static String URL_REGISTRO_RECONTRATACION  ="http://asistente.crediclub.com/2.0/recontratacion.php?tokenID=%s&empleadoID=%s&grupo=%s&nomContacto=%s&telcontacto=%s&fechaEstDes=%s&fecRecontra=%s&latitu=%s&longit=%s";
    public final static String URL_ACTUALIZAR_ETAPA         ="http://asistente.crediclub.com/2.0/actualizaEtapa.php?tokenID=%s&empleadoID=%s&idAgenda=%s&etapa=%s&nombreGrupo=%s&monto=%s&integrantes=%s&intnuevos=%s&intrenova=%s&fecha=%s&fechaestimada=%s&tipodispersion=%s&tipogrupo=%s&latitu=%s&longit=%s";
    public final static String URL_REPROGRAMAR_ETAPA        ="http://asistente.crediclub.com/2.0/reprogramacion.php?tokenID=%s&empleadoID=%s&idAgenda=%s&etapa=%s&fechaRep=%s&motivoRep=%s&latitu=%s&longit=%s";

    public final static String URL_REGISTRO_AGENDA =        "http://asistente.crediclub.com/2.0/altaAgenda.php?";
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

        if (mWifi != null && mWifi.isConnected())
        {
            return true;
        }

        return false;
    }

    public static String getDateTime(long time)
    {
        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String cadenaDate = format.format(date);

        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");
        String cadenaTime = format2.format(date);

        return cadenaDate + "T" + cadenaTime;
    }

    public static boolean validaNumerosCampo(String strCadena){
        boolean respuesta = true;

        for(int  i = 0 ; i < strCadena.length() ; i ++){
            int valor = strCadena.charAt(i);
            //System.out.println("i:" + i + ", valor:" + valor);
            if (valor < 48 || valor > 57) {
                respuesta = false;
            }
        }

        return respuesta;
    }

    public static String getVisitType(int i)
    {
        return VisitEvent.VISIT_TYPE_REGULAR;
    }

    public static String getPhoneID(Context context)
    {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();
    }


    public static boolean getLastCheckinStatus(Context context)
    {
        boolean count = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_LAST_CHECKIN_STATUS, false);
        return count;
    }

    public static void setLastCheckinStatus(Context context, boolean value)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mPrefs.edit();
        e.putBoolean(PREF_LAST_CHECKIN_STATUS, value);
        e.commit();
    }

    public static int getLastCheckinType(Context context)
    {
        int count = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_LAST_CHECKIN_TYPE, 0);
        return count;
    }

    public static void setLastCheckinType(Context context, int value)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mPrefs.edit();
        e.putInt(PREF_LAST_CHECKIN_TYPE, value);
        e.commit();
    }

    public static String getLastCheckinGroup(Context context)
    {
        String count = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_CHECKIN_GROUP, "");
        return count;
    }

    public static void setLastCheckinGroup(Context context, String value)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mPrefs.edit();
        e.putString(PREF_LAST_CHECKIN_GROUP, value);
        e.commit();
    }

    public static String getLastCheckinNote(Context context)
    {
        String count = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_CHECKIN_NOTES, "");
        return count;
    }

    public static void setLastCheckinNote(Context context, String value)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mPrefs.edit();
        e.putString(PREF_LAST_CHECKIN_NOTES, value);
        e.commit();
    }

    // CONVIERTE EL FORMATO DE LA FECHA ORIGINAL A DD-MMMMM-YYYY
    public static String convierteTexto(String valorFecha) {
        String valorFechaStr = STR_VACIO;
        String dia, mes, anio = STR_VACIO;

        try {
            dia = valorFecha.substring(8);
            valorFechaStr = dia;
            mes = valorFecha.substring(5,7);
            anio = valorFecha.substring(0,4);

            if(mes.equalsIgnoreCase("01")){
                valorFechaStr += "-Ene-";
            }else if(mes.equalsIgnoreCase("02")){
                valorFechaStr += "-Feb-";
            }else if(mes.equalsIgnoreCase("03")){
                valorFechaStr += "-Mar-";
            }else if(mes.equalsIgnoreCase("04")){
                valorFechaStr += "-Abr-";
            }else if(mes.equalsIgnoreCase("05")){
                valorFechaStr += "-May-";
            }else if(mes.equalsIgnoreCase("06")){
                valorFechaStr += "-Jun-";
            }else if(mes.equalsIgnoreCase("07")){
                valorFechaStr += "-Jul-";
            }else if(mes.equalsIgnoreCase("08")){
                valorFechaStr += "-Ago-";
            }else if(mes.equalsIgnoreCase("09")){
                valorFechaStr += "-Sep-";
            }else if(mes.equalsIgnoreCase("10")){
                valorFechaStr += "-Oct-";
            }else if(mes.equalsIgnoreCase("11")){
                valorFechaStr += "-Nov-";
            }else if(mes.equalsIgnoreCase("12")){
                valorFechaStr += "-Dic-";
            }
            valorFechaStr += anio;
            //
        } catch (Exception e) {
            valorFechaStr = valorFecha;
        }
        return valorFechaStr;
    }

    public static String cerosIzquierda(String valorTexto, int longitudDato) {
        String cerosTexto = "";

        for (int i = valorTexto.length(); i < longitudDato; i++) {
            cerosTexto = cerosTexto + "0";
        }

        return cerosTexto + valorTexto;
    }
}