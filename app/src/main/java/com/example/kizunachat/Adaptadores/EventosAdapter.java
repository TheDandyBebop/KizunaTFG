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
import com.bumptech.glide.request.RequestOptions;
import com.example.kizunachat.ClasesObjetos.EventosAyuntamiento;
import com.example.kizunachat.R;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.DatosHolder> {
    private OnItemClickListener mListener;
    ArrayList<EventosAyuntamiento> listaEventos;
    Context contexto;

    public EventosAdapter(Context context, ArrayList<EventosAyuntamiento> cntact, OnItemClickListener listener) {
        this.listaEventos = cntact;
        this.mListener = listener;
        this.contexto = context;
    }
    @NonNull
    @Override
    //Este metodo es el que carga el layout de los items
    public DatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        View v = inflador.inflate(R.layout.item_eventos, parent, false);

        return new DatosHolder(v);
    }

@Override
public void onBindViewHolder(@NonNull DatosHolder holder, int position) {
    if(!listaEventos.isEmpty()) {

        EventosAyuntamiento event = listaEventos.get(position);
        holder.tvTitulo.setText(event.getTitle());
        String textoDesc = truncateText(event.getDescription());
        holder.tvDesc.setText(textoDesc);
        holder.tvfInicio.setText(event.getInicioDestacado());
        holder.tvfFin.setText(event.getFinDestacado());
        if (event.getImage() == null || event.getImage().isEmpty()){
            // Cargar la imagen alt utilizando Glide si no tiene la img normal
            // Cargar la imagen de error utilizando Glide con dimensiones deseadas
            Glide.with(contexto)
                    .load(event.getImage())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()// Mantiene el centro y recorta los bordes
                            .override(400, 600))  // Dimensiones deseadas de la imagen de error
                    .into(holder.imagen);
        }
        else {
        // Cargar la imagen del perfil utilizando Glide
        // Cargar la imagen de error utilizando Glide con dimensiones deseadas
            Glide.with(contexto)
                    .load(event.getImage())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()  // Mantiene el centro y recorta los bordes
                            .override(400, 600))  // Dimensiones deseadas de la imagen de error
                    .into(holder.imagen);

        }
    }
}


    @Override
    public int getItemCount() {
        return listaEventos.size(); // Devuelve el tamaño de la lista de contactos
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
     TextView tvTitulo,tvDesc,tvfInicio,tvfFin;
     ImageView imagen;

     public DatosHolder(@NonNull View itemView) {
     super(itemView);
     itemView.setOnClickListener(this);
     //Leo los tv
         tvTitulo = itemView.findViewById(R.id.tvTitulo);
         tvDesc = itemView.findViewById(R.id.tvDesc);
         tvfInicio = itemView.findViewById(R.id.tv_inicioEvento);
         tvfFin = itemView.findViewById(R.id.tv_FinEvento);
     //Leo las img
     imagen = itemView.findViewById(R.id.iv_evento);
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
     
     //METODOS AUXILIARES PARA QUE SALGA BIEN

    private String truncateText(String text) {
        int maxLength = 200; // Maximum length before truncation
        int minLength = 50; // Minimum length for padding

        if (text.length() > maxLength) {
            // Truncate the text and add ellipsis
            return text.substring(0, maxLength) + "...";
        } else if (text.length() < minLength) {
            // Pad the text with spaces until it reaches the minimum length
            StringBuilder paddedText = new StringBuilder(text);
            while (paddedText.length() < minLength) {
                paddedText.append('ㅤ');
            }
            return paddedText.toString();
        } else {
            // Return the text as is if it doesn't need truncation or padding
            return text;
        }
    }

}
