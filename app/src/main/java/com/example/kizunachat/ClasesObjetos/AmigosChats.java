package com.example.kizunachat.ClasesObjetos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Objects;

public class AmigosChats implements Parcelable {
    private String nombre;
    private String telefono;
    private String urlImagen;

    public void setUID(String UID) {
        this.UID = UID;
    }

    private String UID;
    private Mensaje lstMsj;



    private ArrayList<EventosUsuarios> eventosUsuario;


    private int notfContacto;
    // Constructor vacío requerido para Firebase
    public AmigosChats() {
    }

    // Constructor con parámetros
    public AmigosChats(String nombre, String tlf, String urlImagen, String UID ) {
        this.nombre = nombre;
        this.telefono = tlf;
        this.urlImagen = urlImagen;
        this.UID = UID;
    }

    public static final Creator<AmigosChats> CREATOR = new Creator<AmigosChats>() {
        @Override
        public AmigosChats createFromParcel(Parcel in) {
            return new AmigosChats(in);
        }

        @Override
        public AmigosChats[] newArray(int size) {
            return new AmigosChats[size];
        }
    };

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public Mensaje getLstMsj() {
        return lstMsj;
    }
    public String getUID() {
        return UID;
    }

    public void setLstMsj(Mensaje lstMsj) {
        this.lstMsj = lstMsj;
    }
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    // Métodos para Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeString(urlImagen);
        dest.writeString(UID);
    }

    protected AmigosChats(Parcel in) {
        nombre = in.readString();
        telefono = in.readString();
        urlImagen = in.readString();
        UID = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == AmigosChats.class){
            AmigosChats copia = (AmigosChats) o;
            if (this.UID.equals(copia.UID) && this.telefono.equals(copia.telefono) ){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    public int getNotfContacto() {
        return notfContacto;
    }

    public void setNotfContacto(int notfContacto) {
        this.notfContacto = notfContacto;
    }
    public ArrayList<EventosUsuarios> getEventosUsuario() {
        return eventosUsuario;
    }

    public void setEventosUsuario(ArrayList<EventosUsuarios> eventosUsuario) {
        this.eventosUsuario = eventosUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(telefono, UID);
    }
}
