package br.com.gerson.mobile.condominio.model;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.gerson.mobile.condominio.controller.CondominioController;

/**
 * Created by gerson on 18/05/2017.
 */

public class Evento {
    private String data;
    private String descricao;
    private String apto;
    private String bloco;
    private String status;
    private String dataCancel;

    public Evento() {
        this.data = "";
        this.apto = "";
        this.bloco = "";
        this.descricao = "";
        this.status = "";
        this.dataCancel = "";
    }

    public void parseJSON(JSONObject obj) {
        this.data = obj.optString("data");
        this.apto = obj.optString("apto");
        this.bloco = obj.optString("bloco");
        this.descricao = obj.optString("descricao");
        this.status = obj.optString("status");
        this.dataCancel = obj.optString("data_cancel");
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

    public String getStatusDescricao() {
        String status = null;
        if (getStatus().equals(CondominioController.REJEITADO))
            status = "Rejeitado";
        else if (getStatus().equals(CondominioController.RESERVADO))
            status = "Reservado";
        else if (getStatus().equals(CondominioController.CANCELADO))
            status = "Cancelado";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataCancel() {
        return dataCancel;
    }

    public void setDataCancel(String dataCancel) {
        this.dataCancel = dataCancel;
    }
}
