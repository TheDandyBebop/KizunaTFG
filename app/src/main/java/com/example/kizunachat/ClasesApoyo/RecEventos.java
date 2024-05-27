package com.example.kizunachat.ClasesApoyo;

import com.example.kizunachat.ClasesObjetos.EventosAyuntamiento;
import com.example.kizunachat.ClasesObjetos.EventosResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecEventos {

    static ArrayList<EventosAyuntamiento> listaEventosFormat;
    static DatabaseReference eventosRefInterno;
    // Interfaz de devolución de llamada para notificar cuando se reciben los eventos
    public interface EventosCallback {
        void onEventosReceived(ArrayList<EventosAyuntamiento> eventos);
        void onFailure(String errorMessage);
    }

    // Método para recuperar eventos en segundo plano
    public static void obtenerEventos(final EventosCallback callback, DatabaseReference eventosRef) {
        eventosRefInterno = eventosRef;
        listaEventosFormat = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.zaragoza.es/sede/servicio/actividades/evento/programa?rf=html")
                .addHeader("accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonData = response.body().string();
                        Gson gson = new Gson();
                        EventosResponse eventoSinFormato = gson.fromJson(jsonData, EventosResponse.class);
                        List<EventosAyuntamiento> eventosLista = eventoSinFormato.getResult();
                        // Guardar los eventos en la base de datos en tiempo real de Firebase
                        eventosRefInterno = FirebaseDatabase.getInstance().getReference().child("eventos").child("zaragoza").child("ids");
                        for (EventosAyuntamiento evento : eventosLista) {
                            evento.arreglarEvento();
                            eventosRefInterno.child(String.valueOf(evento.getId())).setValue(evento);
                            listaEventosFormat.add(evento);
                        }
                        // Notificar que se han recibido los eventos
                        callback.onEventosReceived(listaEventosFormat);
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure("Error al obtener el JSON: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
