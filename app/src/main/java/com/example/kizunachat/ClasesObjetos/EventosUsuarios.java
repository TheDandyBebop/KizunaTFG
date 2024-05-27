package com.example.kizunachat.ClasesObjetos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventosUsuarios implements Parcelable {

    public String titulo;
    public String desc;
    public String fecha;
    public String hsInicio;
    public String hsFin;
    public String ubicacion;
    public  String tipo;
    public String imagen;

    public EventosUsuarios(String titulo, String fecha) {
        this.titulo = titulo;
        this.fecha = fecha;
    }

    public EventosUsuarios(String titulo, String desc, String fecha, String hsInicio, String hsFin, String ubicacion, String imagen) {
        this.titulo = titulo;
        this.desc = desc;
        this.fecha = fecha;
        this.hsInicio = hsInicio;
        this.hsFin = hsFin;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
    }

    public EventosUsuarios() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHsInicio() {
        return hsInicio;
    }

    public void setHsInicio(String hsInicio) {
        this.hsInicio = hsInicio;
    }

    public String getHsFin() {
        return hsFin;
    }

    public void setHsFin(String hsFin) {
        this.hsFin = hsFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Métodos Parcelable
    protected EventosUsuarios(Parcel in) {
        titulo = in.readString();
        desc = in.readString();
        fecha = in.readString();
        hsInicio = in.readString();
        hsFin = in.readString();
        tipo = in.readString();
        imagen = in.readString();
        ubicacion = in.readString();
    }

    public static final Creator<EventosUsuarios> CREATOR = new Creator<EventosUsuarios>() {
        @Override
        public EventosUsuarios createFromParcel(Parcel in) {
            return new EventosUsuarios(in);
        }

        @Override
        public EventosUsuarios[] newArray(int size) {
            return new EventosUsuarios[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(desc);
        dest.writeString(fecha);
        dest.writeString(hsInicio);
        dest.writeString(hsFin);
        dest.writeString(tipo);
        dest.writeString(imagen);
        dest.writeString(ubicacion);
    }

    public void setImagen(String urlImagen) {
        this.imagen = urlImagen;
    }
    public String getImagen(){
        return this.imagen;
    }
}