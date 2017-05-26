package br.com.gerson.mobile.condominio.model;

/**
 * Created by gerson on 18/05/2017.
 */

public class Condomino {
    private Integer id;
    private String nome;
    private Integer apto;
    private String bloco;

    public Condomino(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
