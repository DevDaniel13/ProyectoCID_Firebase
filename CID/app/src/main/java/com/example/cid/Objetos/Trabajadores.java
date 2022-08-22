package com.example.cid.Objetos;

import java.io.Serializable;

public class Trabajadores implements Serializable {

    private String _ID;
    private String nombre;
    private String area_trabajo;
    private String celular;
    private int disponibilidad;

    public Trabajadores(){
        this.setNombre("");
        this.setArea_trabajo("");
        this.setCelular("");
        this.setDisponibilidad(0);
    }

    public Trabajadores(String nombre, String area_trabajo, String celular, int disponibilidad){
        this.setNombre(nombre);
        this.setArea_trabajo(area_trabajo);
        this.setCelular(celular);
        this.setDisponibilidad(disponibilidad);
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea_trabajo() {
        return area_trabajo;
    }

    public void setArea_trabajo(String area_trabajo) {
        this.area_trabajo = area_trabajo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
