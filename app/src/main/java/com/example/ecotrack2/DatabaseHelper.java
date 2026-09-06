package com.example.ecotrack2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecolim_db";
    private static final int DATABASE_VERSION = 1;

    // Tabla Residuos
    private static final String TABLE_RESIDUOS = "residuos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIPO = "tipo_residuo";
    private static final String COLUMN_PESO = "peso_kg";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_CLIENTE = "cliente";
    private static final String COLUMN_OBSERVACIONES = "observaciones";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RESIDUOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIPO + " TEXT, " +
                COLUMN_PESO + " REAL, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_CLIENTE + " TEXT, " +
                COLUMN_OBSERVACIONES + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESIDUOS);
        onCreate(db);
    }

    // ==========================================
    // MÉTODOS EXISTENTES (CREATE y READ)
    // ==========================================

    // Método para agregar residuo
    public boolean addResiduo(Residuo residuo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TIPO, residuo.getTipoResiduo());
        values.put(COLUMN_PESO, residuo.getPesoKg());
        values.put(COLUMN_FECHA, residuo.getFecha());
        values.put(COLUMN_HORA, residuo.getHora());
        values.put(COLUMN_CLIENTE, residuo.getCliente());
        values.put(COLUMN_OBSERVACIONES, residuo.getObservaciones());

        long result = db.insert(TABLE_RESIDUOS, null, values);
        db.close();

        return result != -1;
    }

    // Método para obtener todos los residuos
    public List<Residuo> getAllResiduos() {
        List<Residuo> residuos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESIDUOS + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Residuo residuo = new Residuo();
                residuo.setId(cursor.getInt(0));
                residuo.setTipoResiduo(cursor.getString(1));
                residuo.setPesoKg(cursor.getDouble(2));
                residuo.setFecha(cursor.getString(3));
                residuo.setHora(cursor.getString(4));
                residuo.setCliente(cursor.getString(5));
                residuo.setObservaciones(cursor.getString(6));

                residuos.add(residuo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return residuos;
    }

    // Método para obtener residuos por tipo
    public List<Residuo> getResiduosByTipo(String tipo) {
        List<Residuo> residuos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESIDUOS +
                " WHERE " + COLUMN_TIPO + " = ? ORDER BY id DESC", new String[]{tipo});

        if (cursor.moveToFirst()) {
            do {
                Residuo residuo = new Residuo();
                residuo.setId(cursor.getInt(0));
                residuo.setTipoResiduo(cursor.getString(1));
                residuo.setPesoKg(cursor.getDouble(2));
                residuo.setFecha(cursor.getString(3));
                residuo.setHora(cursor.getString(4));
                residuo.setCliente(cursor.getString(5));
                residuo.setObservaciones(cursor.getString(6));

                residuos.add(residuo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return residuos;
    }

    // Método para obtener estadísticas (peso total por tipo)
    public double getTotalPesoByTipo(String tipo) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_PESO + ") FROM " +
                TABLE_RESIDUOS + " WHERE " + COLUMN_TIPO + " = ?", new String[]{tipo});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    // ==========================================
    // NUEVOS MÉTODOS AGREGADOS (UPDATE, DELETE, READ BY ID)
    // ==========================================

    // Método para ACTUALIZAR un residuo existente
    public boolean updateResiduo(Residuo residuo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TIPO, residuo.getTipoResiduo());
        values.put(COLUMN_PESO, residuo.getPesoKg());
        values.put(COLUMN_FECHA, residuo.getFecha());
        values.put(COLUMN_HORA, residuo.getHora());
        values.put(COLUMN_CLIENTE, residuo.getCliente());
        values.put(COLUMN_OBSERVACIONES, residuo.getObservaciones());

        // Actualiza donde el ID coincida
        int result = db.update(TABLE_RESIDUOS, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(residuo.getId())});
        db.close();

        return result > 0; // Retorna true si se actualizó al menos 1 fila
    }

    // Método para ELIMINAR un residuo por su ID
    public boolean deleteResiduo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Elimina donde el ID coincida
        int result = db.delete(TABLE_RESIDUOS,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return result > 0; // Retorna true si se eliminó al menos 1 fila
    }

    // Método para obtener UN SOLO residuo por su ID (necesario para la pantalla de editar)
    public Residuo getResiduoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Residuo residuo = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESIDUOS +
                " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            residuo = new Residuo();
            residuo.setId(cursor.getInt(0));
            residuo.setTipoResiduo(cursor.getString(1));
            residuo.setPesoKg(cursor.getDouble(2));
            residuo.setFecha(cursor.getString(3));
            residuo.setHora(cursor.getString(4));
            residuo.setCliente(cursor.getString(5));
            residuo.setObservaciones(cursor.getString(6));
        }

        cursor.close();
        db.close();
        return residuo;
    }
}