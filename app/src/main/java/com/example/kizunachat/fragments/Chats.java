package com.example.kizunachat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kizunachat.Adaptadores.ChatsAdapter;
import com.example.kizunachat.Adaptadores.ContactosAdapter;
import com.example.kizunachat.ClasesApoyo.ComprobadorInactividad;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.Mensaje;
import com.example.kizunachat.MainActivity;
import com.example.kizunachat.R;
import com.example.kizunachat.dialogs.DialogoNotificacionContacto;
import com.example.kizunachat.ventanaChat;
import com.example.kizunachat.ventanaContactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class Chats extends Fragment {


    ArrayList<AmigosChats> contactos;
    DatabaseReference chatsRef;
    ChatsAdapter miAdaptador;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Query query;
    DatabaseReference contactosRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflo el Layout correspondiente al fragmento
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Encuentro el botón flotante en la vista inflada del fragmento
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        // Asigno el OnClickListener al botón flotante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ventanaContactos.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        contactos = new ArrayList<AmigosChats>();
        chatsRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(user.getUid()).child("contacto");
        leerChats();
        recyclerView = view.findViewById(R.id.recyclerChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Crear el adaptador y pasarlo al RecyclerView
        miAdaptador = new ChatsAdapter(getContext(),contactos,  this::onItemClick);
        recyclerView.setAdapter(miAdaptador);

        // Devuelvo la vista inflada
        return view;
    }

//VERIFICAR ESTO; DA ERROR AL DEFINIR EL ULTIMO MENSAJE ENVIADO POR EL AVISO DE CONTACTO
    private void leerChats(){
        chatsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildKey) {
                // Se añadió un nuevo contacto
                String uidContacto = snapshot.getKey();
                //verificarCambiosContactos(uidContacto);
                // Recuperar los datos del contacto
                DatabaseReference contactoRef = FirebaseDatabase.getInstance().getReference()
                        .child("usuarios").child(uidContacto);
                contactoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AmigosChats contacto = dataSnapshot.getValue(AmigosChats.class);
                        contacto.setUID(uidContacto);
                        // Obtener la referencia de la base de datos
                        String ruta = "usuarios/" + user.getUid() + "/contacto/" + uidContacto;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(ruta);
                        // Añadir un listener para obtener el valor de notfContacto
                        databaseReference.child("notfContacto").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Obtener el valor de notfContacto
                                Integer notfContacto = dataSnapshot.getValue(Integer.class);
                                if (notfContacto != null) {
                                    contacto.setNotfContacto(notfContacto);
                                } else {
                                    Log.e("NotificacionContacto", "El valor de notfContacto es nulo");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar errores de lectura de la base de datos
                                Log.e("NotificacionContacto", "Error al leer el valor de notfContacto", databaseError.toException());
                            }
                        });
                        contactos.add(contacto);
                        DatabaseReference mensajesRef =
                                chatsRef.child(uidContacto)
                                .child("mensajes").getRef();
                        mensajesRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Aca podes recuperar el último mensaje del contacto
                                for (DataSnapshot mensajeSnapshot : dataSnapshot.getChildren()) {
                                    Mensaje ultimoMensaje = mensajeSnapshot.getValue(Mensaje.class);
                                    contacto.setLstMsj(ultimoMensaje);
                                    miAdaptador.notifyDataSetChanged();
                                    ComprobadorInactividad.verificarInactividad(contactos, getContext());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar errores de lectura de la base de datos
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores de lectura de la base de datos
                    }
                });
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                String uidContacto = snapshot.getKey();
                // Recuperar los datos del contacto
                DatabaseReference contactoRef = FirebaseDatabase.getInstance().getReference()
                        .child("usuarios").child(uidContacto);
                contactoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AmigosChats contacto = dataSnapshot.getValue(AmigosChats.class);
                        contacto.setUID(uidContacto);
                        DatabaseReference mensajesRef = chatsRef.child(uidContacto)
                                .child("mensajes")
                                .getRef();
                        mensajesRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Aca podes recuperar el último mensaje del contacto
                                for (DataSnapshot mensajeSnapshot : dataSnapshot.getChildren()) {
                                    Mensaje ultimoMensaje = mensajeSnapshot.getValue(Mensaje.class);
                                    contacto.setLstMsj(ultimoMensaje);
                                    int index = contactos.indexOf(contacto);
                                    contactos.set(index,contacto);
                                    //contactos.get(index).setLstMsj(ultimoMensaje);
                                    miAdaptador.notifyItemChanged(index);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar errores de lectura de la base de datos
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores de lectura de la base de datos
                    }
                });
            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {
                Log.println(Log.INFO,"",snapshot.getKey());
            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }


        });
    }

    // Método para obtener la posición de un contacto en el ArrayList basado en su UID
    private int getIndexByUserID(String userID) {
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getUID().equals(userID)) {
                return i;
            }
        }
        return -1; // Si el contacto no se encuentra, devuelve -1
    }

    public void onItemClick(int position) {
        Intent intentChat = new Intent(getContext(), ventanaChat.class);
        intentChat.putExtra("contacto", contactos.get(position));
        startActivity(intentChat);
    }
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set_notification) {
            // Acción cuando se selecciona "Establecer notificaciones de contacto"
            int position = item.getGroupId(); // Obtiene la posición del elemento en el adaptador
            // Crear una instancia del diálogo
            DialogoNotificacionContacto dialogo =
                    new DialogoNotificacionContacto(contactos.get(position));
            // Mostrar el diálogo
            dialogo.show(getParentFragmentManager(), "DialogoNotificacionContacto");
            return true;
        }
        return super.onContextItemSelected(item);
    }

}
