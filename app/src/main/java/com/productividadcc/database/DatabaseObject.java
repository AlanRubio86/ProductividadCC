package com.productividadcc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.productividadcc.Main_Activity;

abstract class DatabaseObject {
    protected String 	TAG 			= "DatabaseObject";
    protected String 	DATABASE_TABLE;
    protected String[] 	columns			= new String[]{};;
    protected String 	KEY_ID			= "_id";

    public long 		id;

    protected 	abstract ContentValues getValues();

    protected 	abstract void copy(Cursor cursor);
    //	protected 	abstract Cursor search(String search);
    public 		abstract String toString();

    public DatabaseObject(String table, String key, String[] columns, long id)
    {
        this.DATABASE_TABLE 	= table;
        this.KEY_ID 			= key;
        this.columns			= columns;
        this.id 				= id;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public boolean get(long id)
    {

        Cursor mCursor = null;
        try
        {
            mCursor = Main_Activity.db.query(true, DATABASE_TABLE, columns, KEY_ID + "=" + id, null, null, null, null, null);
            if (mCursor != null)
            {
                mCursor.moveToFirst();
                copy(mCursor);
                mCursor.close();
                return true;
            }
        }
        catch(Exception e)
        {
            if (mCursor != null)
            {
                mCursor.close();
            }
        }
        return false;
    }

    public Cursor load()
    {
        Log.i(TAG, "Loading table " + DATABASE_TABLE);
        return Main_Activity.db.query(DATABASE_TABLE, columns, null, null, null,  null, null);
    }

    public long insert(){
        Log.i(TAG, "Inserting into table " + DATABASE_TABLE);
        return Main_Activity.db.insert(DATABASE_TABLE, null, getValues());
    }

    public boolean update(){
        Log.i(TAG, "Updating table " + DATABASE_TABLE + " on id = " + id);
        return Main_Activity.db.update(DATABASE_TABLE, getValues(),  KEY_ID + "=" + id, null) > 0;
    }

    public boolean delete(){
        Log.i(TAG, "Deleting from table " + DATABASE_TABLE + " on id = " + id);
        return Main_Activity.db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public boolean delete(long id){
        Log.i(TAG, "Deleting from table " + DATABASE_TABLE + " on id = " + id);
        return Main_Activity.db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public boolean deleteAll(){
        Log.i(TAG, "Clearing table " + DATABASE_TABLE);
        return Main_Activity.db.delete(DATABASE_TABLE, null, null) > 0;
    }

}