package com.example.kizunachat.ClasesApoyo;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;

import com.example.kizunachat.ClasesObjetos.AmigosChats;
import com.example.kizunachat.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ComprobadorInactividad {

    private static final String CHANNEL_ID = "ComprobadorInactividadChannel";

    public static List<AmigosChats> verificarInactividad(ArrayList<AmigosChats> listaAmigos, Context context) {
        List<AmigosChats> amigosInactivos = new ArrayList<>();

        // Obtener fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Recorrer la lista de amigos
        for (AmigosChats amigo : listaAmigos) {
            // Verificar si hay un último mensaje y si notfContacto es mayor que cero
            if (amigo.getLstMsj() != null && amigo.getNotfContacto() > 0) {
                // Obtener la fecha del último mensaje del amigo
                long fechaUltimoMsj = amigo.getLstMsj().getTimestamp();

                // Calcular la diferencia en días
                long diferenciaEnMilisegundos = fechaActual.getTimeInMillis() - fechaUltimoMsj;
                long diferenciaEnDias = diferenciaEnMilisegundos / (1000 * 60 * 60 * 24);

                // Verificar si la diferencia es mayor que notfContacto
                if (diferenciaEnDias > amigo.getNotfContacto()) {
                    amigosInactivos.add(amigo);

                    // Enviar notificación
                    enviarNotificacion(context, amigo.getNombre());
                }
            }
        }

        return amigosInactivos;
    }

    private static void enviarNotificacion(Context context, String nombreContacto) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }

        // Crear un canal de notificación (necesario para Android Oreo y versiones superiores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ComprobadorInactividadChannel";
            String description = "Canal de notificación para el Comprobador de Inactividad";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal en el sistema
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.documento_de_ericsanzeri)
                .setContentTitle("Hace mucho que no hablas con " + nombreContacto)
                .setContentText("¿Qué tal si le preguntas cómo está?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Mostrar la notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }
}
