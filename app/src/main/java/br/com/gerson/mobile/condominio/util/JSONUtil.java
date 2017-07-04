package br.com.gerson.mobile.condominio.util;

import android.graphics.Color;

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

/**
 * Created by gerson on 01/07/2017.
 */

public class JSONUtil {
    static public ArrayList<Event> getEventosMes(JSONObject response) {
        ArrayList<Event> resultList = new ArrayList<Event>();
        try {
            JSONArray result = response.getJSONArray("result");
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
                resultList.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
