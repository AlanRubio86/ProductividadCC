package com.productividadcc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.productividadcc.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String 	TAG					= "Database";
    public static final String 	DATABASE_NAME 		= "agendaDB.db";
    public static final int 	DATABASE_VERSION 	= 1;

    private static final String DROP_EVENTS = "DROP TABLE IF EXISTS events";
    private static final String DROP_EVALUA = "DROP TABLE IF EXISTS evaluacion";

    private static final String TABLA_WEBSERVICE = "CREATE TABLE IF NOT EXISTS wsOffLine " +
                                                   "(_id INTEGER PRIMARY KEY AUTOINCREMENT, urlWS TEXT, status INTEGER)";
    private static final String TABLA_AGENDA = "CREATE TABLE IF NOT EXISTS agendaOffLine " +
                                                    "(_id INTEGER PRIMARY KEY, " +
                                                    "fecha TEXT, hora TEXT," +
                                                    "tipoEvento TEXT, idEmpleado INTEGER," +
                                                    "grupo TEXT, ciclo TEXT," +
                                                    "longitud TEXT, latitud TEXT," +
                                                    "idSeguimiento TEXT, imei TEXT," +
                                                    "monto double, integrantes TEXT," +
                                                    "status TEXT);";

    protected Context context;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String s;

        try{
            InputStream in = context.getResources().openRawResource(R.raw.sql);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in, null);

            NodeList statements = doc.getElementsByTagName("statement");

            for (int i=0; i< statements.getLength(); i++){
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                Log.i(TAG, "SQL: " + s);

                db.execSQL(s);
            }

            // CREATE TABLA EVALUACION
            db.execSQL(TABLA_AGENDA);
            db.execSQL(TABLA_WEBSERVICE);
        }catch (Exception e){
            Log.i(TAG, "onCreate Error: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(TAG, "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which will destroy all old data");
        onCreate(db);
    }

}
