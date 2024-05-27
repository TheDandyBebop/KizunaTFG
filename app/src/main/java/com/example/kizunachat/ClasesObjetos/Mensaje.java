package com.example.kizunachat.ClasesObjetos;
public class Mensaje {
    private String contenido;
    private long timestamp; // Timestamp de Unix para la fecha y hora de envío
    private String uid;
    private boolean leido;



    private String idMSJ;
    // Constructor vacío requerido para Firebase
    public Mensaje() {
    }

    public String getUid() {
        return uid;
    }

    // Constructor con parámetros
    public Mensaje(String contenido, long timestamp, String uid) {
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.uid = uid;
        this.leido = false;
    }
    //VER COMO GESTIONAR LOS VISTOS:
    public Mensaje(String contenido, long timestamp,String uid,boolean visto) {
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.uid = uid;
        this.leido = visto;
    }

    // Getters y setters
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isLeido() {
        return leido;
    }
    public String getIdMSJ() {
        return idMSJ;
    }

    public void setIdMSJ(String idMSJ) {
        this.idMSJ = idMSJ;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

}
