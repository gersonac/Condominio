package br.com.gerson.mobile.condominio.model;

/**
 * Created by gerson on 18/05/2017.
 */

public class Evento {
    private Integer id;
    private String data;
    private String descricao;
    private Integer condomino_id;
    private Integer apto;
    private String bloco;

    public Evento(Integer id) {
        this.id = id;
    }

    public Evento(Integer id, String data, String descricao) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        StringBuilder evento = new StringBuilder("Ap ");
        return evento.append(getApto()).append("-").append(getBloco()).append(": ").append(getDescricao()).toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCondomino_id() {
        return condomino_id;
    }

    public void setCondomino_id(Integer condomino_id) {
        this.condomino_id = condomino_id;
    }

    public Integer getApto() {
        return apto;
    }

    public void setApto(Integer apto) {
        this.apto = apto;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }
}
