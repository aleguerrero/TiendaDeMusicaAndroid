package com.example.alejandroguerreroa.tiendademusicaandroid;

public class Album {

    public String Artista, Album, Genero;
    public long id, Year;

    public Album() {
    }

    public Album(String artista, String album, String genero, long year) {
        Artista = artista;
        Album = album;
        Genero = genero;
        Year = year;
    }
}
