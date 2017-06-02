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

    @Override
    protected String[] getProjection() {
        String[] projection = {
                CondominioContract.Config._ID,
                CondominioContract.Config.COLUMN_NAME_TOKEN
        };
        return projection;
    }

    @Override
    protected void setValues(Cursor c) {
        super.setValues(c);
        setId(c.getInt(c.getColumnIndex(CondominioContract.Config._ID)));
        setToken(c.getString(c.getColumnIndex(CondominioContract.Config.COLUMN_NAME_TOKEN)));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CondominioContract.Config.COLUMN_NAME_TOKEN, getToken());
        return values;
    }

    static public String getEndereco() {
        return "http://192.168.0.4:8080";
    }

    static public String getConsulta() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/consulta/");
    }

    static public String getSalva() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/salva/");
    }

    static public String getPendentes() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/pendentes");
    }

    static public String getStatusEvento() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/statusevento/");
    }

    static public String getLogin() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/login/");
    }
}
