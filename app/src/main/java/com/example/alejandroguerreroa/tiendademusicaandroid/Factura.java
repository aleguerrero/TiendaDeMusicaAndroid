package com.example.alejandroguerreroa.tiendademusicaandroid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Factura {

    public String cliente, disco;
    public int cantidad, numero;
    public String fecha;
    public ArrayList<Factura> discos;

    public Factura() {
    }

    public Factura(String cliente, String fecha, ArrayList<Factura> discos) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.discos = discos;
    }

    public Factura(String disco, int cantidad) {
        this.disco = disco;
        this.cantidad = cantidad;
    }
}