package com.example.kizunachat.Adaptadores;

// Importar la biblioteca Glide

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kizunachat.ClasesObjetos.Mensaje;
import com.example.kizunachat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.DatosHolder> {
    ArrayList<Mensaje> mensajitos;
    Context contexto;
    String miUid;
    String uidContacto;


    public MensajesAdapter(Context context, ArrayList<Mensaje> msj, String uid, String uidContacto) {

        this.mensajitos = msj;
        this.contexto = context;
        this.miUid = uid;
        this.uidContacto = uidContacto;
    }
    @NonNull
    @Override
    //Este metodo es el que carga el layout de los items
    public DatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        // Inflar diferentes diseños según el tipo de mensaje
        if (viewType != TIPO_MENSAJE_SELF) {
            View v = inflador.inflate(R.layout.item_mensaje_ext, parent, false);
            return new DatosHolder(v, 1);
        } else {
            View v = inflador.inflate(R.layout.item_mensaje_propio, parent, false);
            return new DatosHolder(v, 0);
        }
    }

@Override
public void onBindViewHolder(@NonNull DatosHolder holder, int position) {
    if(!mensajitos.isEmpty()) {
        // Obtener el contacto en la posición actual
        Mensaje msj = mensajitos.get(position);
        msj.setLeido(true);
        DatabaseReference refMensaje = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(miUid)
                .child("contacto")
                .child(uidContacto)
                .child("mensajes")
                .child(mensajitos.get(position).getIdMSJ());
        refMensaje.child("leido").setValue(true);
        // Establecer el nombre del contacto en el TextView
        holder.tvTexto.setText(msj.getContenido());
        // Crear un objeto Date utilizando el timestamp
        Date date = new Date(msj.getTimestamp());
        // Crear un objeto SimpleDateFormat para formatear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        // Formatear la fecha como una cadena de texto
        String fechaFormateada = sdf.format(date);
        holder.tvFecha.setText(fechaFormateada);
    }
}


    @Override
    public int getItemCount() {
        return mensajitos.size(); // Devuelve el tamaño de la lista de contactos
    }
    // Constantes para representar los tipos de mensajes
    private static final int TIPO_MENSAJE_SELF = 0;
    private static final int TIPO_MENSAJE_DEFAULT = 1;

    @Override
    public int getItemViewType(int position) {
        // Obtener el UID del usuario que envió el mensaje en la posición actual
        String uidMensaje = mensajitos.get(position).getUid();
        // Comparar con el UID del usuario actual
        if (uidMensaje == null){
            return TIPO_MENSAJE_DEFAULT;
        }
        return uidMensaje.equals(miUid) ? TIPO_MENSAJE_SELF : TIPO_MENSAJE_DEFAULT;
    }


    /**
     * COMPLETAR BIEN LA LOGICA ATRAS DE LA PESTAÑA DE contactos, esto hay que hacerlo de las sig maneras:
     * Se buscan en el telefono los contactos que tengas almacenados, Se buscan en la BD todos los numeros de telefono
     * por cada numero de telefono que encuentre te devuelve sus datos, el recycler los procesa y te los muestra
     * La forma de mostrarlo sera: Imagen del perfil + nombre
     *
     ***/
     // Se evaluaran otros metodos de añadir contactos como por ejemplo una busqueda por nombre de usuario
     //CLASE CON EL CONTENEDOR. Aca en este metodo se recuperan los id del layout y se pasa al metodo de onBindViewHolder
     public class DatosHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView tvTexto;
        TextView tvFecha;
        int tipoDeItem;

        public DatosHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            tipoDeItem = viewType;
            if (tipoDeItem == 1){
                //Leo los tv
                tvTexto = itemView.findViewById(R.id.message_text);
                tvFecha = itemView.findViewById(R.id.tv_fechahora);
            }
            else {
                //Leo los tv
                tvTexto = itemView.findViewById(R.id.message_text_self);
                tvFecha = itemView.findViewById(R.id.tv_fechahora_self);
            }

        }

        //Menu contextual para Recycler, no es igual que las listas.
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }

    }
}
