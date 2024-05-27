package com.example.kizunachat.Adaptadores;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kizunachat.ClasesObjetos.EventosUsuarios;
import com.example.kizunachat.R;

import java.util.ArrayList;
public class EventosCalendarioAdapter extends  RecyclerView.Adapter<EventosCalendarioAdapter.DatosHolder> {
    private OnItemClickListener mListener;
    ArrayList<EventosUsuarios> listaEventos;
    Context contexto;
    public EventosCalendarioAdapter(Context context, ArrayList<EventosUsuarios> eventosDelDia, OnItemClickListener listener) {
        this.listaEventos = eventosDelDia;
        this.mListener = listener;
        this.contexto = context;
    }

    @NonNull
    @Override
    //Este metodo es el que carga el layout de los items
    public EventosCalendarioAdapter.DatosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());
        View v = inflador.inflate(R.layout.item_eventos_calendario, parent, false);
        return new EventosCalendarioAdapter.DatosHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull EventosCalendarioAdapter.DatosHolder holder, int position) {
        if(!listaEventos.isEmpty()) {
            EventosUsuarios event = listaEventos.get(position);
            holder.tvTitulo.setText(event.getTitulo());
            String textoDesc = truncateText(event.getDesc());
            holder.tvDesc.setText(textoDesc);
            if (event.getHsInicio()==null || event.getHsInicio().equals("")){
                holder.tvfInicio.setVisibility(View.GONE);
                holder.tvTituloHSI.setVisibility(View.GONE);
            }
            else {
                holder.tvfInicio.setText(event.getHsInicio());
            }
            if (event.getHsFin()==null || event.getHsFin().equals("")){
                holder.tvfFin.setVisibility(View.GONE);
                holder.tvTituloHSF.setVisibility(View.GONE);
            }else {
                holder.tvfFin.setText(event.getHsFin());
            }
        }
    }


    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class DatosHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener, View.OnClickListener {
        TextView tvTitulo,tvDesc,tvfInicio,tvfFin,tvTituloHSI,tvTituloHSF;
        public DatosHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //Leo los tv
            tvTitulo = itemView.findViewById(R.id.tv_titulo_calendar);
            tvDesc = itemView.findViewById(R.id.tv_desc_calendario);
            tvfInicio = itemView.findViewById(R.id.tv_hs_inicio);
            tvfFin = itemView.findViewById(R.id.tv_hs_fin);
            tvTituloHSI = itemView.findViewById(R.id.tv_titulo_hsI);
            tvTituloHSF = itemView.findViewById(R.id.tv_titulo_hsF);
            //Para el menu contextual.
            itemView.setOnCreateContextMenuListener(this);
        }

        //Menu contextual para Recycler, no es igual que las listas.
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }
        public void setOnItemClickListener(EventosCalendarioAdapter.OnItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getAdapterPosition());
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private String truncateText(String text) {
        int maxLength = 200; // Maximum length before truncation
        int minLength = 100; // Minimum length for padding

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
