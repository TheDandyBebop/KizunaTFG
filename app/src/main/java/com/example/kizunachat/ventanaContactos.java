package com.example.kizunachat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kizunachat.Adaptadores.ContactosAdapter;
import com.example.kizunachat.ClasesApoyo.BusquedaContactos;
import com.example.kizunachat.ClasesApoyo.OnContactosCargadosListener;
import com.example.kizunachat.ClasesObjetos.AmigosChats;

import java.util.ArrayList;

public class ventanaContactos extends AppCompatActivity implements ContactosAdapter.OnItemClickListener {

    private ArrayList<AmigosChats> contactos;
    private ContactosAdapter miAdaptador;
    private RecyclerView recyclerView;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_contactos);
        contactos = new ArrayList<>();

        // Verificar si se tienen los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen los permisos, solicitarlos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Si se tienen los permisos, cargar los contactos
            cargarContactos();
        }

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.reciclerContactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        miAdaptador = new ContactosAdapter(this, contactos, this::onItemClick);
        recyclerView.setAdapter(miAdaptador);
    }

    private void cargarContactos() {
        BusquedaContactos.recuperarContactos(this, new OnContactosCargadosListener() {
            @Override
            public void onContactosCargados(ArrayList<AmigosChats> contactosCargados) {
                contactos.clear();
                contactos.addAll(contactosCargados);
                miAdaptador.notifyDataSetChanged();
            }
        });
    }

    // Método para manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, cargar los contactos
                cargarContactos();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de lectura de contactos denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentChat = new Intent(this, ventanaChat.class);
        intentChat.putExtra("contacto", contactos.get(position));
        startActivity(intentChat);
    }
}
