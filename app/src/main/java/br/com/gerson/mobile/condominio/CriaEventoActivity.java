package br.com.gerson.mobile.condominio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.gerson.mobile.condominio.R;
import br.com.gerson.mobile.condominio.model.Evento;
import br.com.gerson.mobile.condominio.network.DownloadCallback;
import br.com.gerson.mobile.condominio.network.NetworkFragment;

public class CriaEventoActivity extends AppCompatActivity implements DownloadCallback {

    Spinner spnTipoEventos;
    Spinner spnAptos;
    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
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
                String evento = spnTipoEventos.getSelectedItem().toString();
                String apto = spnAptos.getSelectedItem().toString();

                mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(),
                        "http://192.168.0.4:8080/datasnap/rest/TServerMethods1/salva/" + dataEventoYMD + "=" + apto + "|C|" + evento);
                startDownload();
            }
        });
    }

    private ArrayList<String> getListaAptos() {
        final int QTD_ANDARES = 16;
        final int APTO_POR_ANDAR = 4;

        ArrayList<String> aptos = new ArrayList<>();
        for (Integer i = 1; i <= QTD_ANDARES; i++)
            for (Integer j = 1; j <= APTO_POR_ANDAR; j++)

                aptos.add(String.format("%02d%02d", i, j));
        return aptos;
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

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().remove(mNetworkFragment).commit();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
