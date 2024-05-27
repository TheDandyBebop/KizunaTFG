package com.example.kizunachat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kizunachat.Adaptadores.CalendarAdapter;
import com.example.kizunachat.ClasesApoyo.BusquedaContactos;
import com.example.kizunachat.ClasesApoyo.OnContactosCargadosListener;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.EventosUsuarios;
import com.example.kizunachat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Calendario extends Fragment {
    RecyclerView recyclerView;
    ArrayList<AmigosChats> contactos;
    CalendarAdapter adapter;
    AmigosChats miUsuario;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        recyclerView = view.findViewById(R.id.reciclerCalendarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contactos = new ArrayList<>();
        adapter = new CalendarAdapter(getContext(), contactos);
        recyclerView.setAdapter(adapter);
        cargarTuCalendario();
        cargarContactos();

        return view;
    }

    private void cargarTuCalendario(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(user.getUid());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                miUsuario = snapshot.getValue(AmigosChats.class);
                miUsuario.setUID(user.getUid());
                miUsuario.setNombre("Tu Calendario");
                contactos.clear();
                contactos.add(miUsuario);
                DatabaseReference eventosRefpublico = usuarioRef.child("misEventos");
                eventosRefpublico.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<EventosUsuarios> eventos = new ArrayList<>();
                        for (DataSnapshot tipoSnapshot : dataSnapshot.getChildren()) {
                            String tipo = tipoSnapshot.getKey();
                            for (DataSnapshot eventoSnapshot : tipoSnapshot.getChildren()) {
                                EventosUsuarios evento = eventoSnapshot.getValue(EventosUsuarios.class);
                                evento.setTipo(tipo);
                                eventos.add(evento);
                            }
                        }
                        miUsuario.setEventosUsuario((ArrayList<EventosUsuarios>) eventos);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Manejar error
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cargarContactos() {
        BusquedaContactos.recuperarContactos(getContext(), new OnContactosCargadosListener() {
            @Override
            public void onContactosCargados(ArrayList<AmigosChats> contactosCargados) {

                for (AmigosChats contacto : contactosCargados) {
                    if(!contactos.contains(contacto)){
                    contactos.add(contacto);
                    }
                }
                for (AmigosChats contacto : contactos) {
                    if (!contacto.getUID().equals(miUsuario.getUID())){
                    cargarEventos(contacto);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void cargarEventos(AmigosChats contacto) {
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(contacto.getUID()).child("misEventos").child("Publico");

        eventosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<EventosUsuarios> eventos = new ArrayList<>();
                for (DataSnapshot eventoSnapshot : dataSnapshot.getChildren()) {
                    EventosUsuarios evento = eventoSnapshot.getValue(EventosUsuarios.class);
                    evento.setTipo("Publico");
                    eventos.add(evento);
                }
                contacto.setEventosUsuario((ArrayList<EventosUsuarios>) eventos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error
            }
        });
    }
}
