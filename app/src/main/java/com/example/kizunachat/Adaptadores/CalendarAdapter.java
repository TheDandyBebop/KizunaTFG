package com.example.kizunachat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.ClasesObjetos.EventosUsuarios;
import com.example.kizunachat.R;
import com.example.kizunachat.dialogs.DialogoEventosCalendario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private Context context;
    private ArrayList<AmigosChats> amigos; // Reemplaza YourDataModel con el tipo de datos que estás utilizando

    public CalendarAdapter(Context context, ArrayList<AmigosChats> dataList) {
        this.context = context;
        this.amigos = dataList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendarios, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        AmigosChats amigo = amigos.get(position);
        holder.textView.setText(amigo.getNombre());
        holder.calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                // Obtener la fecha seleccionada
                Calendar clickedDayCalendar = eventDay.getCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM", Locale.getDefault());
                String fecha = dateFormat.format(clickedDayCalendar.getTime());
                String fecha2 = dateFormat2.format(clickedDayCalendar.getTime());
                // Obtener la lista de eventos para esa fecha
                List<EventosUsuarios> eventosTotales = amigo.getEventosUsuario();
                ArrayList<EventosUsuarios> eventosDelDia = new ArrayList<>();
                // Filtrar los eventos para obtener los eventos de la fecha seleccionada
                for (EventosUsuarios ev : eventosTotales){
                    if (ev.getFecha().equals(fecha)||ev.getFecha().equals(fecha2)){
                        eventosDelDia.add(ev);
                    }
                }
                // Crear un Intent para abrir el diálogo y pasar los datos necesarios
                Intent intent = new Intent(context, DialogoEventosCalendario.class);
                intent.putExtra("uid", amigo.getUID());
                intent.putExtra("titulo", amigo.getNombre());
                intent.putExtra("fecha", fecha);
                intent.putParcelableArrayListExtra("eventos", eventosDelDia);
                // Abrir el diálogo
                context.startActivity(intent);
            }
        });
        List<EventDay> events = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (amigo.getEventosUsuario() != null && !amigo.getEventosUsuario().isEmpty()){
        for (EventosUsuarios evento : amigo.getEventosUsuario()) {
            try {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(evento.getFecha()));
                if (evento.getTipo().equals("Privado")){
                    events.add(new EventDay(cal, R.drawable.evento_priv));
                }
                else {
                    events.add(new EventDay(cal, R.drawable.evento_public));
                }
            } catch (ParseException e) {
                dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(dateFormat.parse(evento.getFecha()));
                } catch (ParseException ex) {
                    e.printStackTrace();
                }
                cal.set(Calendar.YEAR, currentYear); // Asignar el año actual
                if (evento.getTipo().equals("Privado")){
                    events.add(new EventDay(cal, R.drawable.evento_priv));
                }
                else {
                    events.add(new EventDay(cal, R.drawable.evento_public));
                }
                e.printStackTrace();
            }
        }

        holder.calendarView.setEvents(events);
        }
    }

    @Override
    public int getItemCount() {
        return amigos.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        // Declara las vistas que necesitas en el ViewHolder
        // Asumiendo que tienes un TextView y un CalendarView en tu layout
        TextView textView;
        com.applandeo.materialcalendarview.CalendarView calendarView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encuentra las vistas por su ID
            textView = itemView.findViewById(R.id.textView);
            calendarView = itemView.findViewById(R.id.calendarView);
        }
    }
}
