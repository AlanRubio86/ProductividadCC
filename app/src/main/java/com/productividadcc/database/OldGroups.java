package com.productividadcc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.productividadcc.Main_Activity;

public class OldGroups extends DatabaseObject {
    public static final String TAG 				= "OldGroups";

    public static final String DB_TABLE			= "OldGroups";
    public static final String KEY_ID			= "_id";
    public static final String KEY_ITEM		    = "Item";

    public static SQLiteDatabase db;

    static String[] columns = new String[]{
            KEY_ID, KEY_ITEM
    };

    public String	item;

    public OldGroups(){
        super(DB_TABLE, KEY_ID, columns, 0);
        id			= 0;
        item		= "";
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    protected ContentValues getValues(){
        ContentValues values = new ContentValues();

        values.put(KEY_ITEM,item);
        return values;
    }

    @Override
    public void copy(Cursor cursor){
        id			= cursor.getLong	(0);
        item		= cursor.getString	(1);
    }

    @Override
    public String toString(){
        return "id: " + id + " item: " + item ;
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