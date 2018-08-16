package com.example.alejandroguerreroa.tiendademusicaandroid;

public class Album {

    public String id, artista, album, genero, year;

    public Album() {
    }

    public Album(String artista, String album, String genero, String year) {
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.year = year;
    }
}
