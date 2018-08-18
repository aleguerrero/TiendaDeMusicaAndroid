package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class BuscaAPI extends AsyncTask<Void, Void, Void> {

    public String url = "http://www.json-generator.com/api/json/get/bVTuCHBCUO?indent=2";
    public String textoBuffer;
    public ArrayList<String> textoFinal;

    @Override
    protected void onPostExecute(Void aVoid) {

        try {
            //Guarda datos
            JSONArray clienteJson = new JSONArray(new ArrayList(textoFinal));


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
    }
}
