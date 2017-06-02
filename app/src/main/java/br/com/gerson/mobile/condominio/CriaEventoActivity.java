package br.com.gerson.mobile.condominio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import br.com.gerson.mobile.condominio.model.Config;

public class CriaEventoActivity extends AppCompatActivity {

    Spinner spnTipoEventos;
    Spinner spnAptos;
    String dataEventoYMD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_evento);
        String dataEvento = getIntent().getStringExtra("data");
        dataEventoYMD = getIntent().getStringExtra("dataYMD");

        TextView data = (TextView) this.findViewById(R.id.tvData);
        data.setText(dataEvento);

        ArrayAdapter<String> aptosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getListaAptos());
        spnAptos = (Spinner) this.findViewById(R.id.spnAp);
        spnAptos.setAdapter(aptosAdapter);

        ArrayAdapter<String> tiposAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getTiposEventos());
        spnTipoEventos = (Spinner) this.findViewById(R.id.spnTipoEvento);
        spnTipoEventos.setAdapter(tiposAdapter);

        Button addEvento = (Button) this.findViewById(R.id.btnAddEvento);
        addEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evento = getEvento();
                String apto = spnAptos.getSelectedItem().toString();
                StringBuilder sbUrl = new StringBuilder(Config.getSalva());
                sbUrl.append(dataEventoYMD).append("/").append(apto).append("/").append(getBloco()).append("/").append(evento);

                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, sbUrl.toString(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), getResultadoInclusao(response), Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                );
                queue.add(request);
            }
        });
    }

    private String getBloco() {
        RadioButton rbBlocoA = (RadioButton) this.findViewById(R.id.rbBlocoA);
        RadioButton rbBlocoB = (RadioButton) this.findViewById(R.id.rbBlocoB);
        if (rbBlocoA.isChecked())
            return "A";
        else if (rbBlocoB.isChecked())
            return "B";
        else
            return "C";
    }

    private String getEvento() {
        StringBuilder evento = new StringBuilder();
        String eventoSel = spnTipoEventos.getSelectedItem().toString();
        String[] split = eventoSel.split(" ");
        try {
            for (String evento1: split) {
                evento.append(URLEncoder.encode(evento1, "UTF-8")).append(" ");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return evento.toString().trim();
    }

    private ArrayList<String> getListaAptos() {
        final int QTD_ANDARES = 16;
        final int APTO_POR_ANDAR = 4;

        ArrayList<String> aptos = new ArrayList<>();
        for (Integer i = 1; i <= QTD_ANDARES; i++)
            for (Integer j = 1; j <= APTO_POR_ANDAR; j++)

                aptos.add(String.format(Locale.getDefault(), "%02d%02d", i, j));
        return aptos;
    }

    private String getResultadoInclusao(JSONObject obj) {
        try {
            JSONArray jsonArray = obj.getJSONArray("result");
            JSONObject mensagem = jsonArray.getJSONObject(0);
            return mensagem.getString("mensagem");
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private ArrayList<String> getTiposEventos() {
        ArrayList<String> tipos = new ArrayList<String>();
        tipos.add("Aniversário Infantil");
        tipos.add("Aniversário Adulto");
        tipos.add("Evento religioso");
        tipos.add("Evento de vendas");
        tipos.add("Festa de 15 anos");
        return tipos;
    }
}
