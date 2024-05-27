package com.example.kizunachat.Adaptadores;

// Importar la biblioteca Glide
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.R;
import com.example.kizunachat.ventanaContactos;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.DatosHolder> {
    private OnItemClickListener mListener;
    ArrayList<AmigosChats> contactos;
    Context contexto;

    public ContactosAdapter(Context context,ArrayList<AmigosChats> cntact, OnItemClickListener listener) {
        this.contactos = cntact;
        this.mListener = listener;
        this.contexto = context;
    }
    @NonNull
    @Override
    //Este metodo es el que carga el layout de los items
    public DatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        View v = inflador.inflate(R.layout.item_contactos, parent, false);

        return new DatosHolder(v);
    }
/**

    @Override
    public void onBindViewHolder(@NonNull DatosHolder holder, int position) {
        for (AmigosChats amg : contactos.values()){
            holder.tvNombre.setText(amg.getNombre());
            ImageView img = null;
            /*
            Glide es una biblioteca para cargar y mostrar imágenes en Android de manera eficiente y flexible.
            * Simplifica el proceso de carga desde diferentes fuentes y formatos, gestionando la memoria y
            * ofreciendo opciones de personalización. Es fácil de integrar en proyectos de Android y es una
            * herramienta poderosa para trabajar con imágenes en aplicaciones móviles.
            *

            Glide.with(contexto)
                    .load(amg.getUrlImagen()) // Cargar la imagen desde la URL
                    .apply(new RequestOptions()
                            .error(R.drawable.hotline) // Mostrar una imagen de error si no se puede cargar la imagen (opcional)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)) // Almacenar en caché la imagen descargada (opcional)
                    .into(img); // Mostrar la imagen en el ImageView
            holder.imagen.setImageBitmap(img.getDrawingCache());
        }
    }
**/
@Override
public void onBindViewHolder(@NonNull DatosHolder holder, int position) {
    if(!contactos.isEmpty()) {
        // Obtener el contacto en la posición actual
        AmigosChats amigo = contactos.get(position);

        // Establecer el nombre del contacto en el TextView
        holder.tvNombre.setText(amigo.getNombre());

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


    /**
     * COMPLETAR BIEN LA LOGICA ATRAS DE LA PESTAÑA DE contactos, esto hay que hacerlo de las sig maneras:
     * Se buscan en el telefono los contactos que tengas almacenados, Se buscan en la BD todos los numeros de telefono
     * por cada numero de telefono que encuentre te devuelve sus datos, el recycler los procesa y te los muestra
     * La forma de mostrarlo sera: Imagen del perfil + nombre
     *
     ***/
     // Se evaluaran otros metodos de añadir contactos como por ejemplo una busqueda por nombre de usuario
     //CLASE CON EL CONTENEDOR. Aca en este metodo se recuperan los id del layout y se pasa al metodo de onBindViewHolder
     public class DatosHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
     TextView tvNombre;
     ImageView imagen;

     public DatosHolder(@NonNull View itemView) {
     super(itemView);
     itemView.setOnClickListener(this);
     //Leo los tv
         tvNombre = itemView.findViewById(R.id.tv_Nombre_tlf);
     //Leo las img
     imagen = itemView.findViewById(R.id.iv_Icono);
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
