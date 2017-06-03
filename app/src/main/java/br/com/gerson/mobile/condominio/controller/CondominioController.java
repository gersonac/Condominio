package br.com.gerson.mobile.condominio.controller;

import android.content.Context;

import br.com.gerson.mobile.condominio.model.Config;

/**
 * Created by gerson on 02/06/2017.
 */

public class CondominioController {
    private final Context contex;

    public CondominioController(Context contex) {
        this.contex = contex;
    }

    public Boolean isLogedIn() {
        Config config = new Config(contex);
        if (config.find(1))
            return !config.getToken().isEmpty();
        else
            return false;
    }

    public Boolean logout() {
        Config config = new Config(contex);
        if (config.find(1)) {
            config.delete();
            return true;
        } else
            return false;
    }

    public String getToken() {
        Config config = new Config(contex);
        String result = "123456";
        if (config.find(1))
            result = config.getToken();
        return result;
    }

    public String getEndereco() {
        return "http://192.168.0.4:8080";
    }

    public String getUrlConsulta(String data) {
        return getEndereco().concat("/datasnap/rest/metodo/consulta/").concat(getToken()).concat("/")
                .concat(data);
    }

    public String getUrlSalva(String  data, String apto, String bloco, String descricao) {
        return getEndereco().concat("/datasnap/rest/metodo/salva/").concat(getToken()).concat("/")
                .concat(data).concat("/").concat(apto).concat("/").concat(bloco).concat("/").concat(descricao);
    }

    public String getUrlPendentes() {
        return getEndereco().concat("/datasnap/rest/metodo/pendentes/").concat(getToken());
    }

    public String getUrlStatusEvento(String data, String apto, String bloco, String status) {
        return getEndereco().concat("/datasnap/rest/metodo/statusevento/").concat(getToken()).concat("/")
                .concat(data).concat("/").concat(apto).concat("/").concat(bloco).concat("/").concat(status);
    }

    public String getUrlLogin(String email, String senha) {
        return getEndereco().concat("/datasnap/rest/metodo/login/").concat(email).concat("/").concat(senha);
    }
}
