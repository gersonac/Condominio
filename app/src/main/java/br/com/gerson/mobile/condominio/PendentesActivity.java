package br.com.gerson.mobile.condominio;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import br.com.gerson.mobile.condominio.controller.CondominioController;
import br.com.gerson.mobile.condominio.model.Evento;

public class PendentesActivity extends AppCompatActivity implements PendenteDialog.PendenteDialogListener {

    private Integer itemSel = -1;
    ArrayList<Evento> pendentes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendentes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPendentes();
    }

    private void getPendentes() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new CondominioController(this).getUrlPendentes();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setListaPendentes(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setListaPendentes(JSONObject response) {
        pendentes = parsePendentes(response);
        ListView lvPendentes = (ListView) this.findViewById(R.id.list_pendentes);
        ArrayAdapter<Evento> adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_2, android.R.id.text1, pendentes) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(pendentes.get(position).toString());
                text2.setText(pendentes.get(position).getDataFormatada());
                return view;
            }
        };
        lvPendentes.setAdapter(adapter);
        lvPendentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSel = position;
                DialogFragment dialogFragment = new PendenteDialog();
                dialogFragment.show(getFragmentManager(), "pendentes_dialog");
            }
        });
    }

    private ArrayList<Evento> parsePendentes(JSONObject response) {
        ArrayList<Evento> result = new ArrayList<Evento>();
        try {
            JSONArray jsonArray = response.getJSONArray("result");
            JSONArray pend = jsonArray.getJSONArray(0);
            for (int i = 0; i < pend.length(); i++) {
                Evento evento = new Evento();
                evento.parseJSON(pend.getJSONObject(i));
                result.add(evento);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onItemClick(DialogFragment dialog, int which) {
        if (which < 3) {
            CondominioController condominioController = new CondominioController(this);
            Evento evento = pendentes.get(itemSel);
            String acao = condominioController.getAcao(which, true);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = condominioController.getUrlStatusEvento(evento.getData(), evento.getApto(), evento.getBloco(), acao);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            getPendentes();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(request);
        }
    }
}
