package br.com.gerson.mobile.condominio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.com.gerson.mobile.condominio.persistence.CondominioContract;

/**
 * Created by gerson on 29/05/2017.
 */

public class Config extends ModelBase {
    private String token;
    private String tipo;

    public Config(Context context) {
        super(context);
        TABLE_NAME = CondominioContract.Config.TABLE_NAME;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    protected String[] getProjection() {
        String[] projection = {
                CondominioContract.Config._ID,
                CondominioContract.Config.COLUMN_NAME_TOKEN,
                CondominioContract.Config.COLUMN_NAME_TIPO
        };
        return projection;
    }

    @Override
    protected void setValues(Cursor c) {
        super.setValues(c);
        setId(c.getInt(c.getColumnIndex(CondominioContract.Config._ID)));
        setToken(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_TOKEN)));
        setTipo(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_TIPO)));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CondominioContract.Config.COLUMN_NAME_TOKEN, getToken());
        values.put(CondominioContract.Config.COLUMN_NAME_TIPO, getTipo());
        return values;
    }
}
