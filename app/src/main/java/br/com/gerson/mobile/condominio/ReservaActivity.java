package br.com.gerson.mobile.condominio;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.gerson.mobile.condominio.controller.CondominioController;
import br.com.gerson.mobile.condominio.model.Evento;

public class ReservaActivity extends AppCompatActivity implements PendenteDialog.PendenteDialogListener {
    private Integer day;
    private Integer month;
    private Integer year;
    private ArrayList<Evento> listaEventosDia = null;
    private Integer itemSel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novoEvento = new Intent(getApplicationContext(), CriaEventoActivity.class);
                novoEvento.putExtra("data", getDataFormatada());
                novoEvento.putExtra("dataYMD", getDataFormatadaYMD(year, month, day));
                startActivity(novoEvento);
            }
        });

        CalendarView calendarView = (CalendarView) this.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                onEscolheData(year, month, dayOfMonth);
            }
        });

        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        this.year = Calendar.getInstance().get(Calendar.YEAR);
    }

    private void onEscolheData(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = new CondominioController(this).getUrlConsulta(getDataFormatadaYMD(year, month, dayOfMonth));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listaEventosDia = formataJSON(response);
                        setListaEventosDia(listaEventosDia);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.verifique_conexao_internet), Toast.LENGTH_SHORT).show();
            }
        }
        );
        queue.add(request);
    }

    private void setListaEventosDia(final ArrayList<Evento> lista) {
        ArrayAdapter<Evento> adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_2, android.R.id.text1, lista) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                Evento evento = lista.get(position);
                text1.setText(evento.toString());
                String status = evento.getStatusDescricao();
                if (evento.getStatus().equals(CondominioController.CANCELADO))
                    status = status.concat("(").concat(evento.getDataCancel()).concat(")");
                text2.setText(status);
                return view;
            }
        };
        ListView lvEventosDia = (ListView) findViewById(R.id.lv_evento_dia);
        lvEventosDia.setAdapter(adapter);
        lvEventosDia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSel = position;
                DialogFragment dialogFragment = new PendenteDialog();
                dialogFragment.show(getFragmentManager(), "pendentes_dialog");
            }
        });
    }

    private ArrayList<Evento> formataJSON(JSONObject jsonObject) {
        ArrayList<Evento> sbResult = new ArrayList<Evento>();
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("result");
            jsonArray = jsonArray.getJSONArray(0);
            for (int i = 0; i < jsonArray.length(); i++) {
                Evento evento = new Evento();
                evento.parseJSON(jsonArray.getJSONObject(i));
                sbResult.add(evento);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sbResult;
    }

    @NonNull
    private String getDataFormatada() {
        StringBuilder sb = new StringBuilder();
        sb.append(day).append("/").append(month + 1).append("/").append(year);
        return sb.toString();
    }

    @NonNull
    private String getDataFormatadaYMD(int year, int month, int dayOfMonth) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append(String.format("%02d", month + 1)).append(String.format("%02d", dayOfMonth));
        return sb.toString();
    }

    @Override
    public void onItemClick(DialogFragment dialog, int which) {
        Evento eventoSel = listaEventosDia.get(itemSel);
        CondominioController condominioController = new CondominioController(this);
        Boolean isOwner = condominioController.isOwner(eventoSel.getToken());
        Boolean isAdmin = condominioController.isAdmin();

        if ((which < 3 && isAdmin) || (which < 1 && !isAdmin)) {
            if ((!isAdmin && isOwner) || isAdmin) {
                String acao = condominioController.getAcao(which, isAdmin);
                final String url = condominioController.getUrlStatusEvento(eventoSel.getData(),
                        eventoSel.getApto(), eventoSel.getBloco(), acao);
                RequestQueue queue = Volley.newRequestQueue(this);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                setListaEventosDia(listaEventosDia);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.verifique_conexao_internet), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(request);
            } else
                Toast.makeText(this, "Só é possível cancelar sua própria reserva", Toast.LENGTH_LONG).show();
        }
    }

}
