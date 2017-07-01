package br.com.gerson.mobile.condominio;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.gerson.mobile.condominio.controller.CondominioController;
import br.com.gerson.mobile.condominio.model.Evento;

public class ReservasActivity extends AppCompatActivity implements PendenteDialog.PendenteDialogListener{
    private CompactCalendarView calendar;
    private Date dataSel;
    private ArrayList<Evento> listaEventosDia = null;
    private Integer itemSel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novoEvento = new Intent(getApplicationContext(), CriaEventoActivity.class);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                novoEvento.putExtra("dataYMD", df.format(dataSel));
                df.applyPattern("dd/MM/yyyy");
                novoEvento.putExtra("data", df.format(dataSel));
                startActivity(novoEvento);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataSel = Calendar.getInstance().getTime();
        TextView mes = (TextView) findViewById(R.id.mes_atual);
        mes.setText(formataData(dataSel));

        calendar = (CompactCalendarView) this.findViewById(R.id.compactcalendar_view);
        getEventosMes(Calendar.getInstance().getTime());
        calendar.shouldDrawIndicatorsBelowSelectedDays(true);
        calendar.shouldSelectFirstDayOfMonthOnScroll(false);
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dataSel = dateClicked;
                onEscolheData(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dataSel = firstDayOfNewMonth;
                TextView mes = (TextView) findViewById(R.id.mes_atual);
                mes.setText(formataData(firstDayOfNewMonth));
                getEventosMes(firstDayOfNewMonth);
                setListaEventosDia(new ArrayList<Evento>());
            }
        });
    }

    private void getEventosMes(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        String mes = df.format(date);
        CondominioController controller = new CondominioController(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, controller.getUrlConsultaMes(mes), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    calendar.removeAllEvents();
                    result = response.getJSONArray("result");
                    JSONObject obj1 = result.getJSONObject(0);
                    String mes = obj1.optString("mes");
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    Date dataCal = df.parse(mes.concat("01"));
                    JSONArray dias = obj1.getJSONArray("dias");
                    for (int i = 0; i < dias.length(); i++) {
                        JSONObject obj = dias.getJSONObject(i);
                        int dia = obj.optInt("dia");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dataCal);
                        cal.set(Calendar.DAY_OF_MONTH, dia);
                        Event event = new Event(Color.BLUE, cal.getTimeInMillis());
                        calendar.addEvent(event, false);
                    }
                    calendar.invalidate();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private String formataData(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return df.format(date);
    }

    private void onEscolheData(Date dateClicked) {
        RequestQueue queue = Volley.newRequestQueue(this);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        final String url = new CondominioController(this).getUrlConsulta(df.format(dataSel));
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
