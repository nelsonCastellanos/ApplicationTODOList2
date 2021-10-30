package com.umb.applicationtodolist.model;

import java.util.Date;

public class Agenda {
    private String uid;
    private Date fecha;
    private String asunto;
    private String actividad;


    public Agenda() {
    }

    public Agenda(String uid, Date fecha, String asunto, String actividad) {
        this.uid = uid;
        this.fecha = fecha;
        this.asunto = asunto;
        this.actividad = actividad;
    }

    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "uid='" + uid + '\'' +
                ", fecha=" + fecha +
                ", asunto='" + asunto + '\'' +
                ", actividad='" + actividad + '\'' +
                '}';
    }
}
