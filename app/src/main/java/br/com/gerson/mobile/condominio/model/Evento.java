package br.com.gerson.mobile.condominio.model;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gerson on 18/05/2017.
 */

public class Evento {
    private String data;
    private String descricao;
    private String apto;
    private String bloco;
    private String pendente;

    public Evento() {
        this.data = "";
        this.apto = "";
        this.bloco = "";
        this.descricao = "";
        this.pendente = "";
    }
    public Evento(String data, String descricao) {
        this.data = data;
        this.descricao = descricao;
    }

    public void parseJSON(JSONObject obj) {
        this.data = obj.optString("data");
        this.apto = obj.optString("apto");
        this.bloco = obj.optString("bloco");
        this.descricao = obj.optString("descricao");
        this.pendente = obj.optString("pendente");
    }

    @Override
    public String toString() {
        StringBuilder evento = new StringBuilder();
        return evento.append(getApto()).append("-").append(getBloco()).append(": ").append(getDescricao()).toString();
    }

    public String getDataFormatada() {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        try {
            df.applyLocalizedPattern("yyyyMMdd");
            Date date = df.parse(getData());
            df.applyPattern("dd/MM/yyyy");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return getData();
        }
    }

    public String getStatus() {
        String status = null;
        if (getPendente().equals("R"))
            status = "Rejeitado";
        else if (getPendente().equals("N"))
            status = "Reservado";
        else
            status = "Pendente";
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getPendente() {
        return pendente;
    }

    public void setPendente(String pendente) {
        this.pendente = pendente;
    }
}
