package br.com.gerson.mobile.condominio.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.gerson.mobile.condominio.model.Config;

/**
 * Created by gerson on 02/06/2017.
 */

public class CondominioController {
    private final Context contex;
    public static final String PENDENDE = "P";
    public static final String RESERVADO = "R";
    public static final String REJEITADO = "E";
    public static final String CANCELADO = "C";
    public static final String ADMIN = "A";

    public CondominioController(Context contex) {
        this.contex = contex;
    }

    public Boolean isLoggedIn() {
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

    public Boolean isAdmin() {
        Config config = new Config(contex);
        Boolean result = false;
        if (config.find(1))
            result = config.getTipo().equals(ADMIN);
        return result;
    }

    public Boolean isOwner(String apto, String bloco) {
        Config config = new Config(contex);
        Boolean result = false;
        if (config.find(1))
            result = config.getApto().equals(apto) && config.getBloco().equals(bloco);
        return result;
    }

    @NonNull
    public String getAcao(int which, Boolean isAdmin) {
        if (isAdmin)
            switch (which) {
                case 0:
                    return CondominioController.CANCELADO;
                case 2:
                    return CondominioController.REJEITADO;
                default:
                    return CondominioController.RESERVADO;
            }
        else
            return CondominioController.CANCELADO;
    }

    public String getEndereco() {
        return "http://10.1.20.164:8080";
    }

    public String getBaseUrl() {
        return "/datasnap/rest/metodo/";
    }

    public String getUrlConsulta(String data) {
        return getEndereco().concat(getBaseUrl()).concat("consulta/").concat(getToken()).concat("/")
                .concat(data);
    }

    public String getUrlSalva(String  data, String apto, String bloco, String descricao) {
        return getEndereco().concat(getBaseUrl()).concat("salva/").concat(getToken()).concat("/")
                .concat(data).concat("/").concat(apto).concat("/").concat(bloco).concat("/").concat(descricao);
    }

    public String getUrlPendentes() {
        return getEndereco().concat(getBaseUrl()).concat("pendentes/").concat(getToken());
    }

    public String getUrlStatusEvento(String data, String apto, String bloco, String status) {
        return getEndereco().concat(getBaseUrl()).concat("statusevento/").concat(getToken()).concat("/")
                .concat(data).concat("/").concat(apto).concat("/").concat(bloco).concat("/").concat(status);
    }

    public String getUrlLogin(String email, String senha) {
        return getEndereco().concat(getBaseUrl()).concat("login/").concat(email).concat("/").concat(senha);
    }
}
