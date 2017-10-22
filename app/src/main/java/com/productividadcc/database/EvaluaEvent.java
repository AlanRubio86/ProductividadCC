package com.productividadcc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class EvaluaEvent extends DatabaseObject {

    public static final String TAG 				= "EvaluacionGrupo";

    public static final String DB_TABLE			= "evaluacion";
    public static final String KEY_ID			= "_id";
    public static final String KEY_GROUP		= "grupo";
    public static final String KEY_CICLO		= "ciclo";
    public static final String KEY_CLIENTE		= "cliente";
    public static final String KEY_SEMANA		= "semana";
    public static final String KEY_NASISTIO		= "noasistio";
    public static final String KEY_NPAGO		= "noPago";
    public static final String KEY_NAHORRO		= "noahorro";
    public static final String KEY_NSOLIDA		= "nosolida";
    public static final String KEY_STATUS		= "status";

    static String[] columnas = new String[]
            {
                    KEY_ID, KEY_GROUP, KEY_CICLO, KEY_CLIENTE, KEY_SEMANA, KEY_NASISTIO, KEY_NPAGO, KEY_NAHORRO, KEY_NSOLIDA, KEY_STATUS
            };

    public EvaluaEvent(){
        super(DB_TABLE, KEY_ID, columnas, 0);
        id			= 0;
        grupo		= "";
        ciclo		= "";
        cliente		= "";
        semana		= "";
        noAsistio	= 0;
        noPago		= 0;
        noAhorro 	= 0;
        noSolidario	= 0;
        status		= 0;
    }

    public String	grupo;
    public String 	ciclo;
    public String 	cliente;
    public String 	semana;
    public int		noAsistio;
    public int		noPago;
    public int		noAhorro;
    public int		noSolidario;
    public int		status;

    @Override
    protected ContentValues getValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_GROUP, 	grupo);
        values.put(KEY_CICLO, 	ciclo);
        values.put(KEY_CLIENTE, cliente);
        values.put(KEY_SEMANA, 	semana);
        values.put(KEY_NASISTIO,noAsistio);
        values.put(KEY_NPAGO, 	noPago);
        values.put(KEY_NAHORRO, noAhorro);
        values.put(KEY_NSOLIDA, noSolidario);
        values.put(KEY_STATUS, 	status);
        return values;
    }

    @Override
    public void copy(Cursor cursor) {
        id			= cursor.getLong	(0);
        grupo		= cursor.getString	(1);
        ciclo		= cursor.getString	(2);
        cliente		= cursor.getString	(3);
        semana		= cursor.getString	(4);
        noAsistio	= cursor.getInt	(5);
        noPago		= cursor.getInt	(6);
        noAhorro	= cursor.getInt	(7);
        noSolidario	= cursor.getInt	(8);
        status		= cursor.getInt	(9);

    }

    @Override
    public String toString() {
        return "id: " + id + " grupo: " + grupo + " ciclo: " + ciclo + " cliente: " + cliente + " semana: " + semana + " noAsistio: " + noAsistio + " noPago: " + noPago
                + " noAhorro: " + noAhorro + " noSolidario: " + noSolidario + " status: " + status;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public int getNoAsistio() {
        return noAsistio;
    }

    public void setNoAsistio(int noAsistio) {
        this.noAsistio = noAsistio;
    }

    public int getNoPago() {
        return noPago;
    }

    public void setNoPago(int noPago) {
        this.noPago = noPago;
    }

    public int getNoAhorro() {
        return noAhorro;
    }

    public void setNoAhorro(int noAhorro) {
        this.noAhorro = noAhorro;
    }

    public int getNoSolidario() {
        return noSolidario;
    }

    public void setNoSolidario(int noSolidario) {
        this.noSolidario = noSolidario;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}