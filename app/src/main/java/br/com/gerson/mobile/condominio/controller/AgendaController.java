package br.com.gerson.mobile.condominio.controller;

import java.util.ArrayList;

import br.com.gerson.mobile.condominio.model.Evento;

/**
 * Created by gerson on 18/05/2017.
 */

public class AgendaController {
    private ArrayList<Evento> eventos;

    public AgendaController() {

    }

    public ArrayList<Evento> findEvento(Integer id) {
        ArrayList<Evento> result = new ArrayList<Evento>();
        return result;
    }

    public ArrayList<Evento> findEventoPorData(String data) {
        ArrayList<Evento> result = new ArrayList<Evento>();
        for (Evento evento: eventos) {
            if (evento.getData().equals(data))
                result.add(evento);
        }
        return result;
    }
}
