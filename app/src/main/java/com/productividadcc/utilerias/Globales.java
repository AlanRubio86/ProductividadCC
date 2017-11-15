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

    public final static String URL_REGISTRO_GRUPO =         "http://asistente.crediclub.com/2.0/altaGrupo.php?tokenID=%s&empleadoID=%s&nomContacto=%s&telcontacto=%s&refcontacto=%s&fechaEstDes=%s&fecCapac=%s&latitu=%s&longit=%s";                                                   ;
    public final static String URL_CONSULTA_GRUPOS =        "http://asistente.crediclub.com/2.0/consultaGrupos.php?empleadoID=";
    public final static String URL_CONSULTA_RENOVACION =    "http://asistente.crediclub.com/2.0/consultaRenovaciones.php?empleadoID=";


    public final static String URL_REGISTRO_AGENDA =        "http://asistente.crediclub.com/2.0/altaAgenda.php?";
    public final static String URL_CONSULTA_AGENDA =        "http://asistente.crediclub.com/2.0/consultaAgenda.php?empleadoID=";
    public final static String URL_CONSULTA_EMPLEADO =      "http://asistente.crediclub.com/2.0/consultaEmpleado.php?empleadoID=";
    public final static String URL_CONSULTA_BITACORA =      "http://asistente.crediclub.com/2.0/consultaBitacora.php?empleadoID=";
    public final static String URL_REGISTRO_VISITA =        "http://asistente.crediclub.com/2.0/logVisita.php?";
    public final static String URL_REGISTRO_EVALUACION =    "http://asistente.crediclub.com/2.0/logEvaluacion.php?";

    public final static String AGENDAR_CAPACITACION = "1";
    public final static String AGENDAR_VOBORENOVAC = "2";
    public final static String AGENDAR_COBRANZATEMP = "3";
    public final static String CAPACITACION_1 = "4";
    public final static String CAPACITACION_2 = "5";
    public final static String VOBO = "6";
    public final static String VOBO_RENOVACION = "7";
    public final static String DESEMBOLSO = "8";
    public final static String EVALUACION_SEMANAL = "9";
    public final static String VERIFICACION = "10";
    public final static String PROMOCION = "11";
    public final static String LLAMADA_GESTION = "12";
    public final static String AGENDAR_CAPACITACION2 = "13";
    public final static String AGENDAR_VOBO = "14";
    public final static String AGENDAR_PROMO = "15";

    public final static String CANCELA_CAP1 = "80";
    public final static String CANCELA_CAP2 = "81";
    public final static String CANCELA_VOBO = "82";
    public final static String CANCELA_DESE = "83";

    public final static String REAGENDAR_EVENTO = "99";

    public final static String DDMMMMYYYY 	= "dd '-' MMMMM '-' yyyy";

    public static final int TIPO_NUEVO 		= 0;
    public static final int TIPO_ESTRELLA 	= 1;
    public static final int TIPO_SUPEREST 	= 2;

    public static final int GESTION_SI 		= 0;
    public static final int GESTION_NO 	= 1;

    public static final int MC_CAP1_NOREUNIERON 	= 0;
    public static final int MC_CAP1_NOINTERESO 	    = 1;
    public static final int MC_CAP1_FALTAGENTE 	    = 2;

    public static final String STR_CAP1_NOREUNIERON 	= "1";
    public static final String STR_CAP1_NOINTERESO 	    = "2";
    public static final String STR_CAP1_FALTAGENTE      = "3";

    public static final int MC_CAP2_NOREUNIERON 	= 0;
    public static final int MC_CAP2_FALTAPAPELE 	= 1;

    public static final String STR_CAP2_NOREUNIERON 	= "4";
    public static final String STR_CAP2_FALTAPAPELE	    = "5";

    public static final int MC_VOBO_ANALISISRECHAZA 	= 0;
    public static final int MC_VOBO_GERENTERECHAZA	    = 1;
    public static final int MC_VOBO_GRUPOINCOMPLETO	    = 2;
    public static final int MC_VOBO_CLIENTENOPRESE	    = 3;
    public static final int MC_VOBO_FALTAINE	        = 4;
    public static final int MC_VOBO_LISTANEGRA	        = 5;

    public static final String STR_VOBO_ANALISISRECHAZA = "6";
    public static final String STR_VOBO_GERENTERECHAZA 	= "7";
    public static final String STR_VOBO_GRUPOINCOMPLETO = "8";
    public static final String STR_VOBO_CLIENTENOPRESE  = "9";
    public static final String STR_VOBO_FALTAINE        = "10";
    public static final String STR_VOBO_LISTANEGRA      = "11";

    public static final int MC_DESE_CLIENTENOPRE 	= 0;
    public static final int MC_DESE_FALTAINE	    = 1;
    public static final int MC_DESE_GERENTERECHAZA	= 2;

    public static final String STR_DESE_CLIENTENOPRE 	= "12";
    public static final String STR_DESE_FALTAINE 	    = "13";
    public static final String STR_DESE_GERENTERECHAZA  = "14";

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