package br.com.gerson.mobile.condominio.model;

/**
 * Created by gerson on 29/05/2017.
 */

public class Config {
    static public String getEndereco() {
        return "http://10.1.20.164:8080";
    }

    static public String getConsulta() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/consulta/");
    }

    static public String getSalva() {
        return getEndereco().concat("/datasnap/rest/TServerMethods1/salva/");
    }
}
