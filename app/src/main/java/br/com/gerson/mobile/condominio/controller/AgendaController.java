package br.com.gerson.mobile.condominio.controller;

import java.util.ArrayList;

import br.com.gerson.mobile.condominio.model.Evento;

/**
 * Created by gerson on 18/05/2017.
 */

public class AgendaController {
    private ArrayList<Evento> eventos;

    public AgendaController() {
        eventos = new ArrayList<Evento>();
        Evento novo = new Evento(1, "18/5/2017", "Festa do pijama");
        novo.setApto(15);
        novo.setBloco("A");
        eventos.add(novo);
        novo = new Evento(1, "18/5/2017", "Festa das primas");
        novo.setApto(115);
        novo.setBloco("B");
        eventos.add(novo);
        novo = new Evento(1, "8/5/2017", "Festa dos amigos do síndico");
        novo.setApto(303);
        novo.setBloco("B");
        eventos.add(novo);
        novo = new Evento(1, "18/6/2017", "Missa pra pedir perdão");
        novo.setApto(1803);
        novo.setBloco("C");
        eventos.add(novo);
    }

    public ArrayList<Evento> findEvento(Integer id) {
        ArrayList<Evento> result = new ArrayList<Evento>();
        for (Evento evento: eventos) {
            if (evento.getId().equals(id))
                result.add(evento);
        }
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
