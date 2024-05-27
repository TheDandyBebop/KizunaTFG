package com.example.kizunachat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.Adaptadores.MensajesAdapter;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.Mensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ventanaChat extends AppCompatActivity {
    DatabaseReference msjRef;
    MensajesAdapter miAdaptador;
    ArrayList<Mensaje> mensajito;
    RecyclerView recyclerView;
    AmigosChats contactoAmigo;
    FirebaseAuth mAuth;
    FirebaseUser user;
    long lstTimeMsj;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece la vista de la actividad como el diseño definido en activity_main.xml
        setContentView(R.layout.activity_ventana_chats);
        lstTimeMsj = 0;
        mensajito = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //Creo mi objeto database
        contactoAmigo = getIntent().getParcelableExtra("contacto");
        msjRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios")
                .child(user.getUid())
                .child("contacto")
                .child(contactoAmigo.getUID())
                .child("mensajes");
        cargarMsj(contactoAmigo.getUID());
        ImageView icono = findViewById(R.id.iv_Contacto);
        // Cargar la imagen del perfil utilizando Glide
        Glide.with(this)
                .load(contactoAmigo.getUrlImagen())
                .apply(new RequestOptions()
                        .error(R.drawable.hotline)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CircleCrop())
                        .override(150, 150))
                        .into(icono);
        ImageButton btnMiBoton = findViewById(R.id.btnSend);
        btnMiBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMsj(v); // Llama al método miMetodoClick cuando se haga clic en el botón
            }
        });

        TextView tvNombre = findViewById(R.id.tv_NomContacto);
        tvNombre.setText(contactoAmigo.getNombre());
        // Obtener el contexto de la actividad o fragmento que contiene la vista de los contactos
        recyclerView = findViewById(R.id.reciclerChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Crear el adaptador y pasarlo al RecyclerView
        miAdaptador = new MensajesAdapter(ventanaChat.this, mensajito, user.getUid(), contactoAmigo.getUID());
        recyclerView.setAdapter(miAdaptador);
    }


    //leer bien y analizar a fondo funcional; falta aplicar el recicler view
    // Método para cargar los contactos del teléfono en un HashMap
    public void cargarMsj(String uidContacto) {
// Consultar los mensajes del contacto específico por su UID
        Query query = msjRef.orderByChild("timestamp").startAt(lstTimeMsj);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verificar si existe el nodo para el UID buscado
                if (dataSnapshot.exists()) {
                    // Recuperar los mensajes del UID y convertirlos en objetos MSJ
                    for (DataSnapshot mensajeSnapshot : dataSnapshot.getChildren()) {
                        Mensaje mensaje = mensajeSnapshot.getValue(Mensaje.class);
                        mensaje.setIdMSJ(mensajeSnapshot.getKey());
                        // Agregar el mensaje al array
                        if (mensaje.getTimestamp() > lstTimeMsj) //estoy obligado a hacer esto ya que se invoca este metodo cuando se actualiza la DB
                        {
                        mensajito.add(mensaje);
                        }
                        miAdaptador.notifyDataSetChanged();
                        int ultimoIndice = miAdaptador.getItemCount() - 1;
                        // Desplazar suavemente el RecyclerView al último elemento
                        recyclerView.smoothScrollToPosition(ultimoIndice);
                    }
                    //Correjir para que solo te muestren los ultimo luego de que se mande un msj
                    lstTimeMsj = mensajito.get(mensajito.size()-1).getTimestamp();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de la consulta
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void enviarMsj(View view){
        TextView tv_CampoMsj = findViewById(R.id.tv_MSJ);
        DatabaseReference envMsjRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios")
                .child(contactoAmigo.getUID())
                .child("contacto")
                .child(user.getUid())
                .child("mensajes");
        String msjKey = envMsjRef.push().getKey();
        Mensaje miMsj = new Mensaje( tv_CampoMsj.getText().toString(),
                System.currentTimeMillis(),
                mAuth.getUid(),false);
        miMsj.setLeido(true);
        msjRef.child(msjKey).setValue(miMsj);
        miMsj.setLeido(false);
        miMsj.setContenido(miMsj.getContenido());
        envMsjRef.child(msjKey).setValue(miMsj);
        tv_CampoMsj.setText("");
    }
    public void volverAtras(View v){
        super.finish();
    }
}

