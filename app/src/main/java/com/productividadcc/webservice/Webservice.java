package com.productividadcc.webservice;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.productividadcc.database.Event;

/**
 * Created by cesarrdz on 12/7/16.
 */
public class Webservice {
    protected static final String TAG = "Webservice";

    public static final String URL2 = "";
    public static final String URLLog = "http://asistente.crediclub.com/logVisita.php";
    public static final String URLEval = "http://asistente.crediclub.com/logEvaluacion.php";

    public static String inputStreamToString(InputStream is) throws Exception
    {
        String s = "";
        String line = "";

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // Read response until the end
        while ((line = rd.readLine()) != null)
        {
            s += line;
        }

        // Return full string
        return s;
    }

	/*
	 * @param VisitEvent
	 */

    public static boolean LogVisit(Context mContext, Event mEvent)
    {
        boolean ret = false;
        /*String eventType = "";
        if ((mEvent.getType() & Evaluacion.CHECKOUT_FLAG) == Evaluacion.CHECKOUT_FLAG) {
            eventType = VisitEvent.EVENT_TYPE_CHECKOUT;
        } else {
            eventType = VisitEvent.EVENT_TYPE_CHECKIN;
        }

        try {

            //final TextView outputView = (TextView) findViewById(R.id.showOutput);
            URL url = new URL(URLLog);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            String urlParameters = "phone=" + Globales.getPhoneID(mContext) + "&group=" + mEvent.getGroup() + "&ciclo="
                    + mEvent.getCiclo() + "&semana=" + mEvent.getSemana() + "&integrantes=" + mEvent.getIntegrantes()
                    + "&event=" + eventType + "&stamp=" + String.valueOf(mEvent.getStamp()/1000) + "&latitude="
                    + mEvent.getLatitude() + "&longitude=" + mEvent.getLongitude() + "&asistencia="
                    + String.valueOf(mEvent.getAsistencia()) + "&pago=" + String.valueOf(mEvent.getPago())
                    + "&ahorro=" + String.valueOf(mEvent.getAhorro()) + "&grupoNoAsiste=" + String.valueOf(mEvent.getGrupoNoAsiste())
                    + "&noVisitoGrupo=" + String.valueOf(mEvent.getNoVisitoGrupo())
                    + "&visit=" + Globales.getVisitType(mEvent.getType() & ~Evaluacion.CHECKOUT_FLAG);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            int responseCode = connection.getResponseCode();

            final StringBuilder output = new StringBuilder("Request URL " + url);
            output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
            output.append(System.getProperty("line.separator")  + "Type " + "POST");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            Log.d("LogVisit", "output" + br);
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();

            output.append(System.getProperty("line.separator") +
                    "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") +
                    responseOutput.toString());

            String responseString = responseOutput.toString();
            Log.d("LogVisit", "before if, responseString==" + responseString);

            if(responseString.toUpperCase().equals("OK"))
                ret = true;
            Log.d("LogVisit", "after if, return==" + ret);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return ret;
    }



}