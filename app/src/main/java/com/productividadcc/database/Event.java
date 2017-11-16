package com.productividadcc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.productividadcc.Main_Activity;

public class Event extends DatabaseObject {
    public static final String TAG 				= "WSEvent";

    public static final String DB_TABLE			= "wsOffLine";
    public static final String KEY_ID			= "_id";
    public static final String KEY_URL		    = "urlWS";
    public static final String KEY_STATUS		= "status";

    public static SQLiteDatabase db;

    static String[] columns = new String[]{
            KEY_ID, KEY_URL,  KEY_STATUS
    };

    public String	urlWS;
    public int	 	status;

    public Event(){
        super(DB_TABLE, KEY_ID, columns, 0);
        id			= 0;
        urlWS		= "";
        status		= 0;
    }

    public String getUrlWS() {
        return urlWS;
    }

    public void setUrlWS(String urlWS) {
        this.urlWS = urlWS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    protected ContentValues getValues(){
        ContentValues values = new ContentValues();

        values.put(KEY_URL, 		urlWS);
        values.put(KEY_STATUS, 		status);
        return values;
    }

    @Override
    public void copy(Cursor cursor){
        id			= cursor.getLong	(0);
        urlWS		= cursor.getString	(1);
        status		= cursor.getInt		(2);
    }

    @Override
    public String toString(){
        return "id: " + id + " urlWS: " + urlWS + " status: " + status;
    }

    public Cursor load(){
        Log.i(TAG, "Loading table " + DB_TABLE);
        return Main_Activity.db.query(DB_TABLE, columns, null, null, null,  null, "_id ASC");
    }

    public boolean delete(long id){
        Log.i(TAG, "Deleting from table " + DB_TABLE + " on id = " + id);
        boolean ret = Main_Activity.db.delete(DB_TABLE, KEY_ID + "=" + id, null) > 0;
        return ret;
    }


    public void insertaWS(String strURL, int status){
        String sqlExec = "";

        sqlExec = "INSERT INTO " + DB_TABLE + " (urlWS, status) values ('" + strURL + "', " + status + ");";
        Log.d(TAG, "Inserting " + sqlExec);

        db.execSQL(sqlExec);
    }
}