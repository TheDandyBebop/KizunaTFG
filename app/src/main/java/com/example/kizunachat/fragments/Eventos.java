package com.example.kizunachat.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kizunachat.Adaptadores.EventosAdapter;
import com.example.kizunachat.ClasesApoyo.RecEventos;
import com.example.kizunachat.ClasesObjetos.EventosAyuntamiento;
import com.example.kizunachat.R;
import com.example.kizunachat.VentanaNuevoEventoGlobal;
import com.example.kizunachat.dialogs.DialogoEvento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;


public class Eventos extends Fragment {
    ArrayList<EventosAyuntamiento> eventosLocales;
    EventosAdapter miAdaptador;
    RecyclerView recyclerView;
    DatabaseReference eventsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventsRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("eventos")
                .child("zaragoza")
                .child("ids");
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
        setHasOptionsMenu(true);

        // Llama al método para obtener eventos
        RecEventos.obtenerEventos(new RecEventos.EventosCallback() {
            @Override
            public void onEventosReceived(ArrayList<EventosAyuntamiento> eventos) {
                eventosLocales = eventos;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = view.findViewById(R.id.recyclerEventos);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        // Crear el adaptador y pasarlo al RecyclerView
                        miAdaptador = new EventosAdapter(getContext(), eventosLocales, this::onItemClick);
                        recyclerView.setAdapter(miAdaptador);
                        listenForNewEvents();
                    }
                    private void onItemClick(int i) {
                        Intent dialogIntent = new Intent(getActivity(), DialogoEvento.class);
                        dialogIntent.putExtra("titulo", eventosLocales.get(i).getTitle());
                        dialogIntent.putExtra("description", eventosLocales.get(i).getDescription());
                        dialogIntent.putExtra("startDate", eventosLocales.get(i).getInicioDestacado());
                        dialogIntent.putExtra("endDate", eventosLocales.get(i).getFinDestacado());
                        dialogIntent.putExtra("imagen", eventosLocales.get(i).getImage());
                        startActivity(dialogIntent);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("RecEventos", "Error al obtener eventos: " + errorMessage);
            }
        }, eventsRef);

        // Inflo el Layout correspondiente a al fragmento
        return view;
        
    }
    public void listenForNewEvents() {
        eventsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Convertir el nuevo evento a objeto EventosAyuntamiento
                EventosAyuntamiento nuevoEvento = dataSnapshot.getValue(EventosAyuntamiento.class);
                // Verificar si el evento ya está en la lista
                boolean encontrado = false;
                for (EventosAyuntamiento evento : eventosLocales) {
                    if (evento.getId().equals(nuevoEvento.getId())) {
                        encontrado = true;
                        break;
                    }
                }
                // Si el evento no está en la lista, agrégalo
                if (!encontrado) {
                    eventosLocales.add(nuevoEvento);
                    miAdaptador.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                }
            }

            // Otros métodos de la interfaz ChildEventListener
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    // Método para verificar si el tema actual es oscuro
    public boolean isDarkTheme() {
        Configuration configuration = getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo oscuro activado
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo oscuro desactivado
                return false;
            default:
                return false;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isDarkTheme()) {
            inflater.inflate(R.menu.evento_menu_dark, menu);
        } else {
            inflater.inflate(R.menu.evento_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_crear_evento) {
            // Abrir la actividad "VentanaNuevoEventoGlobal"
            Intent intent = new Intent(getContext(), VentanaNuevoEventoGlobal.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}