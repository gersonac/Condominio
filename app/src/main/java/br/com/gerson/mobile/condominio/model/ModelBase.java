package br.com.gerson.mobile.condominio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import br.com.gerson.mobile.condominio.persistence.CondominioSQLiteOpenHelper;

/**
 * Created by gerson on 07/05/2017.
 */

public class ModelBase {
    private CondominioSQLiteOpenHelper helper;
    String TABLE_NAME;
    private Integer id;

    public ModelBase(Context context) {
        if (context != null)
            helper = new CondominioSQLiteOpenHelper(context);
        TABLE_NAME = "";
    }

    protected String[] getProjection() {
        return null;
    }

    protected void setValues(Cursor c) { }

    protected ContentValues getContentValues() {
        return null;
    }

    public boolean find(Integer id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        Cursor c = db.query(TABLE_NAME, getProjection(), selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            setValues(c);
            return true;
        } else
            return false;
    }

    public void save() {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.insert(TABLE_NAME, null, getContentValues());
    }

    public void update() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        db.update(TABLE_NAME, getContentValues(), selection, selectionArgs);
    }

    public void delete() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
