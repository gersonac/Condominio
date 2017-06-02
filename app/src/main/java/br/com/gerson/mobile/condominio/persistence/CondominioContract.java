package br.com.gerson.mobile.condominio.persistence;

import android.provider.BaseColumns;

/**
 * Created by gerson on 06/05/2017.
 */

public final class CondominioContract {
    private CondominioContract() {}

    public  static class Config implements BaseColumns {
        public static final String TABLE_NAME = "config";
        public static final String COLUMN_NAME_TOKEN = "token";
    }

    public static final String CREATE_CONFIG =
            "CREATE TABLE " + Config.TABLE_NAME + " ( " +
                    Config._ID + " INTEGER PRIMARY KEY," +
                    Config.COLUMN_NAME_TOKEN + " VARCHAR(100) )";
}
