package com.example.kizunachat.ClasesObjetos;

import java.util.List;


//ESTA CLASE EXISTE POR QUE LA API DEL GOBIERNO TE DEVUELVE LA INFORMACION FATAL
public class EventosResponse {
    private int totalCount;
    private int start;
    private int rows;
    private List<EventosAyuntamiento> result;

    // Constructor vacío requerido por Firebase Realtime Database
    public EventosResponse() {
    }

    // Getters y Setters
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<EventosAyuntamiento> getResult() {
        return result;
    }

    public void setResult(List<EventosAyuntamiento> result) {
        this.result = result;
    }
}
