package com.example.kizunachat.ClasesObjetos;

import java.util.Map;

public class Usuario {
    private String nombre;
    private String telefono;
    private String urlImagen;

    private Map<String, AmigosChats> listaAmigos;

    // Constructor vacío requerido para Firebase
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String nombre, String tlf, String url, Map<String, AmigosChats> listaAmigos) {
        this.nombre = nombre;
        this.telefono = tlf;
        this.urlImagen = url;
        this.listaAmigos = listaAmigos;
    }

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

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
    public Map<String, AmigosChats> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(Map<String, AmigosChats> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }
}
