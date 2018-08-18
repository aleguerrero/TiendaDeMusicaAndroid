package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class BuscaAPI extends AsyncTask<Void, Void, Void> {

    public String album;
    public String url = "http://ws.audioscrobbler.com/2.0/?method=album.getInfo&artist=Slayer&album" +
            "=ReignInBlood&api_key=e011f3deca10341bc14fc4384dcf79b8&format=json";
    public String textoBuffer;
    public String textoFinal;

    public BuscaAPI(String album) {
        this.album = album;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        try {
            //Guarda datos
            JSONObject clienteJson = new JSONObject(new String(textoFinal));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //Crea url
        URL urlBack;

        try {

            //Carga en variable
            urlBack = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlBack.openStream()));

            //Carga en textoFinal
            while ((textoBuffer = br.readLine()) != null) {
                textoFinal += textoBuffer;
            }

            //Cierra
            br.close();

        } catch (Exception e) {


        }
        return null;
    }
}
