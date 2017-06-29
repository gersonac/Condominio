package br.com.gerson.mobile.condominio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservasActivity extends AppCompatActivity {

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
                novoEvento.putExtra("data", "01/01/17");
                novoEvento.putExtra("dataYMD", "170107");
                startActivity(novoEvento);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mes = (TextView) findViewById(R.id.mes_atual);
        mes.setText(formataData(Calendar.getInstance().getTime()));

        CompactCalendarView calendar = (CompactCalendarView) this.findViewById(R.id.compactcalendar_view);

        Calendar cal = Calendar.getInstance();
        Event event0 = new Event(Color.BLUE, cal.getTimeInMillis());
        calendar.addEvent(event0, true);
        cal.add(Calendar.DATE, 1);
        Event event1 = new Event(Color.BLUE, cal.getTimeInMillis());
        calendar.addEvent(event1, true);
        Event event2 = new Event(Color.BLUE, cal.getTimeInMillis());
        calendar.addEvent(event2, true);
        cal.add(Calendar.DATE, -5);
        Event event3 = new Event(Color.BLUE, cal.getTimeInMillis());
        calendar.addEvent(event3, true);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                TextView mes = (TextView) findViewById(R.id.mes_atual);
                mes.setText(formataData(firstDayOfNewMonth));
            }
        });
    }

    private String formataData(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return df.format(date);
    }

}
