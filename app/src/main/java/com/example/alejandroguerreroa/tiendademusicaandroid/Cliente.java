package com.example.alejandroguerreroa.tiendademusicaandroid;

public class Cliente {

    public String id, nombre, apellidos, correoElectronico;

    public Cliente() {
    }

    public Cliente(String id, String nombre, String apellidos, String correoElectronico) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
    }
}
