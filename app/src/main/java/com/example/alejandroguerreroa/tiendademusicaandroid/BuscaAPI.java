package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.os.AsyncTask;

public class BuscaAPI extends AsyncTask<Void, Void, Void> {

    public String album;
    public String url;
    public String textoBuffer;
    public String textoFinal;

    public BuscaAPI(String album) {
        this.album = album;
    }
    
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
