package com.example.kizunachat.ClasesObjetos;


import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//ESTA CLASE ES LA QUE PARSEA A LOS JSON DE LOS EVENTOS
public class EventosAyuntamiento {
    private String id;
    private String title;
    private String description;
    private String image;
    private String imageAlt;
    private int ordenDestacado;
    private String ubicacion;

    public EventosAyuntamiento( String title, String description, String image, String imageAlt, String inicioDestacado, String finDestacado,String ubicacion) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.imageAlt = imageAlt;
        this.inicioDestacado = inicioDestacado;
        this.finDestacado = finDestacado;
        this.ubicacion = ubicacion;
    }

    private String inicioDestacado;
    private String finDestacado;
    private String lastUpdated;

    // Constructor vacío requerido por Firebase Realtime Database
    public EventosAyuntamiento() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public int getOrdenDestacado() {
        return ordenDestacado;
    }

    public void setOrdenDestacado(int ordenDestacado) {
        this.ordenDestacado = ordenDestacado;
    }

    public String getInicioDestacado() {
        return inicioDestacado;
    }

    public void setInicioDestacado(String inicioDestacado) {
        this.inicioDestacado = inicioDestacado;
    }

    public String getFinDestacado() {
        return finDestacado;
    }

    public void setFinDestacado(String finDestacado) {
        this.finDestacado = finDestacado;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void arreglarEvento(){
        if(this.getDescription() != null){
        this.setDescription(stripHtml(this.getDescription()));
        }
        else{
            this.setDescription("Sin descripcion.");
        }
        this.setInicioDestacado(formatDate(this.getInicioDestacado()));
        this.setFinDestacado(formatDate(this.getFinDestacado()));
        if (this.getImage() == null || this.getImage().isEmpty()){
            this.setImage("https:"+this.getImageAlt());
        }
        else{
            this.setImage("https:"+this.getImage());
        }
    }

    private String formatDate(String dateStr) {
        // Define el formato de entrada de la fecha
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        // Define el formato de salida de la fecha en español
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd'/'MM 'a las' HH:mm 'hs'", new Locale("es", "ES"));
        // Define un formato de salida alternativo sin la hora
        SimpleDateFormat outputFormatNoTime = new SimpleDateFormat("dd 'de' MMMM", new Locale("es", "ES"));

        try {
            // Convierte la cadena de entrada a un objeto Date
            Date date = inputFormat.parse(dateStr);
            // Extrae la hora de la fecha
            String timePart = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);

            // Verifica si la hora es "00:00:00"
            if ("00:00:00".equals(timePart)) {
                // Si es "00:00:00", devuelve la fecha sin la hora
                return outputFormatNoTime.format(date);
            } else {
                // Si no, devuelve la fecha con la hora formateada
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            // Imprime la traza del error si ocurre un ParseException
            e.printStackTrace();
            // Devuelve la cadena original en caso de error
            return dateStr;
        }
    }
    //Este metodo no es mio pero sirve :D
    private String stripHtml(String html) {
        // Verifica si la versión de Android es Nougat (7.0) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Convierte el HTML a un objeto Spanned usando el modo LEGACY, que es más seguro para nuevas versiones
            Spanned spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
            // Retorna la cadena sin las etiquetas HTML
            return spanned.toString();
        } else {
            // Convierte el HTML a un objeto Spanned usando el método deprecated para versiones anteriores a Nougat
            Spanned spanned = Html.fromHtml(html);
            // Retorna la cadena sin las etiquetas HTML
            return spanned.toString();
        }
    }
}
