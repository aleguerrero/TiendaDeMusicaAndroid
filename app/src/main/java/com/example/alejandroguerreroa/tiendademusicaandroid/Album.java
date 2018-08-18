package com.example.alejandroguerreroa.tiendademusicaandroid;

public class Album {

    public Long Id,  Year;
    public String  Artista, Album, Genero;

    public Album(){

    }

    public Album (String Artista, String Album, String Genero, Long Year){
        this.Artista = Artista;
        this.Album = Album;
        this.Genero = Genero;
        this.Year = Year;
    }


}
