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
    private String apto;
    private String bloco;

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

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    @Override
    protected String[] getProjection() {
        String[] projection = {
                CondominioContract.Config._ID,
                CondominioContract.Config.COLUMN_NAME_TOKEN,
                CondominioContract.Config.COLUMN_NAME_TIPO,
                CondominioContract.Config.COLUMN_NAME_APTO,
                CondominioContract.Config.COLUMN_NAME_BLOCO
        };
        return projection;
    }

    @Override
    protected void setValues(Cursor c) {
        super.setValues(c);
        setId(c.getInt(c.getColumnIndex(CondominioContract.Config._ID)));
        setToken(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_TOKEN)));
        setTipo(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_TIPO)));
        setApto(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_APTO)));
        setBloco(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_BLOCO)));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CondominioContract.Config.COLUMN_NAME_TOKEN, getToken());
        values.put(CondominioContract.Config.COLUMN_NAME_TIPO, getTipo());
        values.put(CondominioContract.Config.COLUMN_NAME_APTO, getApto());
        values.put(CondominioContract.Config.COLUMN_NAME_BLOCO, getBloco());
        return values;
    }
}
