package br.com.gerson.mobile.condominio;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PendenteDialog.PendenteDialogListener {

    CalendarView calendarView;
    private Integer day;
    private Integer month;
    private Integer year;
    private ArrayList<Evento> listaEventosDia = null;
    private Integer itemSel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        calendarView = (CalendarView) this.findViewById(R.id.calendarView);
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

                text1.setText(lista.get(position).toString());
                text2.setText(lista.get(position).getStatus());
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
        if (which != 2) {
            Evento eventoSel = listaEventosDia.get(itemSel);
            String acao = which == 0 ? "N" : "R";
            final String url = new CondominioController(this).getUrlStatusEvento(eventoSel.getData(), eventoSel.getApto(), eventoSel.getBloco(), acao);
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

                }
            });
            queue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pendentes) {
            Intent i = new Intent(this, PendentesActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_login) {
            CondominioController condominioController = new CondominioController(this);
            if (condominioController.isLogedIn())
                condominioController.logout();
            else {
                Intent i = new Intent(this, LoginActivity.class);
                this.startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
