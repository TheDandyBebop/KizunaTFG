package com.example.kizunachat.Adaptadores;

// Importar la biblioteca Glide

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.Mensaje;
import com.example.kizunachat.R;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.DatosHolder> {
    private OnItemClickListener mListener;
    ArrayList<AmigosChats> contactos;
    Context contexto;
    Mensaje lstMsj;

    public ChatsAdapter(Context context, ArrayList<AmigosChats> cntact, OnItemClickListener listener) {
        this.contactos = cntact;
        this.mListener = listener;
        this.contexto = context;
    }
    @NonNull
    @Override
    //Este metodo es el que carga el layout de los items
    public DatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        View v = inflador.inflate(R.layout.item_chats, parent, false);

        return new DatosHolder(v);
    }

@Override
public void onBindViewHolder(@NonNull DatosHolder holder, int position) {
    holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
        menu.add(holder.getAdapterPosition(), R.id.menu_set_notification, 0, "Establecer notificaciones de contacto");
    });
    if(!contactos.isEmpty()) {
        // Obtener el contacto en la posición actual
        AmigosChats amigo = contactos.get(position);
        lstMsj = contactos.get(position).getLstMsj();
        // Establecer el nombre del contacto en el TextView
        holder.tvNombre.setText(amigo.getNombre());
        //Establecer el ultimo mensaje
        holder.tvLstMsj.setText(lstMsj.getContenido());
        if (!lstMsj.isLeido()){
            holder.tv_notificacion.setVisibility(View.VISIBLE);
        }
        else {
            holder.tv_notificacion.setVisibility(View.INVISIBLE);
        }
        // Cargar la imagen del perfil utilizando Glide
        Glide.with(contexto)
                .load(amigo.getUrlImagen())
                .apply(new RequestOptions()
                        .error(R.drawable.hotline)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CircleCrop())
                        .override(150, 150))
                .into(holder.imagen);
    }
}


    @Override
    public int getItemCount() {
        return contactos.size(); // Devuelve el tamaño de la lista de contactos
    }


     // Se evaluaran otros metodos de añadir contactos como por ejemplo una busqueda por nombre de usuario
     //CLASE CON EL CONTENEDOR. Aca en este metodo se recuperan los id del layout y se pasa al metodo de onBindViewHolder
     public class DatosHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
     TextView tvNombre, tvLstMsj, tv_notificacion;

     ImageView imagen;

     public DatosHolder(@NonNull View itemView) {
     super(itemView);
     itemView.setOnClickListener(this);
     itemView.setOnCreateContextMenuListener(this);
     //Leo los tv
         tvNombre = itemView.findViewById(R.id.tv_Nombre_chat);
         tvLstMsj = itemView.findViewById(R.id.tv_ult_msj);
     //Leo las img
     imagen = itemView.findViewById(R.id.iv_Icono_chats);
     tv_notificacion = itemView.findViewById(R.id.iv_notificacion);
     //Para el menu contextual.
     itemView.setOnCreateContextMenuListener(this);

     }

     //Menu contextual para Recycler, no es igual que las listas.
     @Override
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
     }
     public void setOnItemClickListener(OnItemClickListener listener) {
     mListener = listener;
     }

     @Override
     public void onClick(View v) {
     if (mListener != null) {
     mListener.onItemClick(getAdapterPosition());
     }
     }
     }
     //Para despues definir que pasa al darle click
     public interface OnItemClickListener {
     void onItemClick(int position);
     }
}
