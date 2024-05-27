package com.example.kizunachat.dialogs;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kizunachat.Adaptadores.EventosAdapter;
import com.example.kizunachat.Adaptadores.EventosCalendarioAdapter;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.EventosUsuarios;
import com.example.kizunachat.R;
import com.example.kizunachat.VentanaAñadirEvento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DialogoEventosCalendario extends Activity {
///QUEDA POR:
    /**
     * Arreglar esta vista para que concuerde con el verdadero dialogo
     * Añadir el adaptador que Va a llevar el dialogo
     * Añadir la funcionalidad de cuando hagas click en un evento
     * añador la funcionalodad de cuando apretas "crear evento"
     * hacer el dialogo y el item del recycler en relative layout
     * **/

    String usuario;
    String fecha;
    ArrayList<EventosUsuarios> eventosDelDia;
    String uid;
    RecyclerView recyclerEventos;
    EventosCalendarioAdapter miAdaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_eventos_calendarios);
        // Obtén los extras del intent
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        usuario = intent.getStringExtra("titulo");
        fecha = intent.getStringExtra("fecha");
        eventosDelDia = intent.getParcelableArrayListExtra("eventos");

        // Asigna los datos a los TextViews
        TextView nombreTextView = findViewById(R.id.tv_nombre_calendar);
        TextView fechaTextView = findViewById(R.id.tv_fecha_calendar);
        nombreTextView.setText(usuario);
        fechaTextView.setText(fecha);
        recyclerEventos = findViewById(R.id.RecyclerCalendarEvent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerEventos.setLayoutManager(linearLayoutManager);
        // Crear el adaptador y pasarlo al RecyclerView
        miAdaptador = new EventosCalendarioAdapter(this, eventosDelDia, this::onItemClick);
        recyclerEventos.setAdapter(miAdaptador);
        miAdaptador.notifyDataSetChanged();
        // Configura el botón de cerrar
        Button closeButton = findViewById(R.id.btn_cerrar);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser yo = auth.getCurrentUser();
        if (uid.equals(yo.getUid())){
        //Configura el botón de añadir nuevo evento a esa fecha
        Button addToCalendarButton = findViewById(R.id.btn_crear_evento_calendar);
        addToCalendarButton.setVisibility(View.VISIBLE);
        addToCalendarButton.setClickable(true);
        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VentanaAñadirEvento.class);
                intent.putExtra("InicioFecha",fecha);
                startActivity(intent);
            }
        });
        }

    }

    private void onItemClick(int i) {
        Intent dialogIntent = new Intent(this, DialogoEvento.class);
        dialogIntent.putExtra("titulo", eventosDelDia.get(i).getTitulo());
        dialogIntent.putExtra("description", eventosDelDia.get(i).getDesc());
        //Formateo esto para que salga en la otra ventana bien
        if (eventosDelDia.get(i).getHsInicio() == null || eventosDelDia.get(i).getHsInicio().equals("")){
            dialogIntent.putExtra("startDate", fecha);
        }
        else {
            String fechaI = eventosDelDia.get(i).getFecha() + " a las " + eventosDelDia.get(i).getHsInicio() +"hs";
            dialogIntent.putExtra("startDate", fechaI);
        }
        if (eventosDelDia.get(i).getHsFin() == null || eventosDelDia.get(i).getHsFin().equals("")){
        }
        else {
            String fechaf = eventosDelDia.get(i).getFecha() + " a las " + eventosDelDia.get(i).getHsFin() +"hs";
            dialogIntent.putExtra("endDate", fechaf);
        }
        if (eventosDelDia.get(i).getUbicacion() == null || eventosDelDia.get(i).getUbicacion().equals("")){
        }
        else {
            dialogIntent.putExtra("ubicacion", eventosDelDia.get(i).getUbicacion());
        }
        dialogIntent.putExtra("fecha", fecha);
        dialogIntent.putExtra("imagen", eventosDelDia.get(i).getImagen());
        startActivity(dialogIntent);
    }
}
