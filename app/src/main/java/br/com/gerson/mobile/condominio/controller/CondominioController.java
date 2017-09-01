package br.com.gerson.mobile.condominio.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            result = config.getTipo().equals(CondominioController.ADMIN);
        return result;
    }

    public Boolean isOwner(String token) {
        return getToken().equals(token);
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
        //return "http://192.168.0.7:8080";
        //return "http://ec2-52-14-246-157.us-east-2.compute.amazonaws.com:8080";
        return "http://condominioapp.ddns.net:8080";
    }

    public String getBaseUrl() {
        return "/datasnap/rest/metodo/";
    }

    public String getUrlConsulta(String data) {
        return getEndereco().concat(getBaseUrl()).concat("consulta/").concat(getToken()).concat("/")
                .concat(data);
    }

    public String getUrlConsultaMes(String mes) {
        return getEndereco().concat(getBaseUrl()).concat("consultames/").concat(getToken()).concat("/")
                .concat(mes);
    }

    public String getUrlSalva(String data, String apto, String bloco, String descricao) {
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

    public String getUrlNovoUsuario(String email, String senha) {
        return getEndereco().concat(getBaseUrl()).concat("novousuario/").concat(email).concat("/").concat(senha);
    }

    public String getUrlResetPassword(String email) {
        return getEndereco().concat(getBaseUrl()).concat("resetsenha/").concat(email);
    }

    public String hashMd5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
