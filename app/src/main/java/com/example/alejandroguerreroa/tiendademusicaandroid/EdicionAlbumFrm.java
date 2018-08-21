package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class EdicionAlbumFrm extends AppCompatActivity implements View.OnClickListener {

    //EditTexts
    EditText etAlbum;
    EditText etArtista;
    EditText etYear;

    //Spinner
    Spinner spGeneros;

    //Buttons
    Button btnAgModAlbum;
    Button btnBorrarAlbum;

    //FireBase
    DatabaseReference mDBAlbum = FirebaseDatabase.getInstance().getReference().child("Albumes");

    //Boolean de Agregar
    Boolean agregar;
    ArrayList album;

    //ArrayList de Generos
    ArrayList<String> generos = new ArrayList<>();

    //ArrayAdapter de Generos
    ArrayAdapter<String> aaGeneros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_album_frm);

        //carga extras
        cargarExtras();

        //carga items
        iniciaPantalla();

        //No abre el teclado automaticamente
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Carga Spinner
        new BuscaAPI().execute();
    }

    private void cargarExtras() {

        //carga boolean de si es para agregar
        agregar = getIntent().getExtras().getBoolean("agregar");

        //carga lista
        album = getIntent().getExtras().getStringArrayList("cliente");

    }

    private void iniciaPantalla() {

        //EditTexts
        etAlbum = (EditText) findViewById(R.id.etAlbum);
        etArtista = (EditText) findViewById(R.id.etArtista);
        etYear = (EditText) findViewById(R.id.etYear);

        //Buttons
        btnAgModAlbum = (Button) findViewById(R.id.btnAgModAlbum);
        btnAgModAlbum.setOnClickListener(this);

        btnBorrarAlbum = (Button) findViewById(R.id.btnBorrarAlbum);
        btnBorrarAlbum.setOnClickListener(this);

        //Modifica Boton Agregar o Modificar
        if (agregar) {
            btnAgModAlbum.setText("Agregar Álbum");
        } else {
            btnAgModAlbum.setText("Modificar Álbum");

            //Muestra boton de borrar
            btnBorrarAlbum.setVisibility(View.VISIBLE);

            //Agrega Datos de album
            cargarDatos(album);
        }

        //Spinners
        spGeneros = findViewById(R.id.spGeneros);
        //Adapter
        aaGeneros = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        aaGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGeneros.setAdapter(aaGeneros);

    }

    private void cargarDatos(ArrayList album) {

        try {
            //Carga datos en EditTexts
            etAlbum.setText(album.get(0).toString());

            //deshabilita ID
            etAlbum.setEnabled(false);

            //continua cargando datos
            etArtista.setText(album.get(1).toString());
            etYear.setText(album.get(3).toString());
        } catch (Exception e) {
            Toast.makeText(this, "Hubo un error: \n" + e, Toast.LENGTH_LONG);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Boton Agregar o Modificar
            case R.id.btnAgModAlbum:

                try {
                    //Verifica si es Agregar o Modificar
                    if (agregar) {


                        //Crea album
                        Album albumModificar = new Album(
                                etAlbum.getText().toString(),
                                etArtista.getText().toString(),
                                spGeneros.getSelectedItem().toString(),
                                Long.parseLong(etYear.getText().toString())
                        );

                        //Agrega a FireBase
                        mDBAlbum.push().setValue(albumModificar);

                        //Toast
                        Toast.makeText(this, "Agregado con éxito", Toast.LENGTH_LONG);

                        //cierra
                        this.finish();

                        break;

                    } else {

                        //Crea album
                        Album albumMostrar = new Album(
                                etAlbum.getText().toString(),
                                etArtista.getText().toString(),
                                spGeneros.getSelectedItem().toString(),
                                Long.parseLong(etYear.getText().toString())
                        );

                        //Lo modifica
                        mDBAlbum.child(album.get(0).toString()).setValue(albumMostrar);

                        //Cierra
                        this.finish();

                        break;
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                }

                //Borra album
            case R.id.btnBorrarCliente:

                //Muestra cuadro de eliminar
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Seguro de que desea eliminar el álbum?").setPositiveButton("Sí", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

        }
    }

    //Oculta Keyboard
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Para eliminar cliente
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){

                //en caso de decir que sí
                case DialogInterface.BUTTON_POSITIVE:

                    try {
                        //elimina dato
                        mDBAlbum.child(album.get(0).toString()).removeValue();

                        //String mensaje
                        String mensaje = "Álbum eliminado";

                        //Muestra texto eliminado
                        mostrarToast(mensaje);

                        //termina
                        finish();

                        break;
                    } catch (Exception e) {
                        //Mensaje
                        String mensaje = "Hubo un error: " + e.getMessage();

                        //Muestra mensaje
                        mostrarToast(mensaje);

                        break;
                    }

            }
        }
    };

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
    }


    private class BuscaAPI extends AsyncTask<String, String, ArrayList<String>> {

        //JSON
        String urlJson = "http://www.json-generator.com/api/json/get/ceoUOTHDqW?indent=2";

        StringBuffer buffer = new StringBuffer();
        String line = "";

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            //Dunno
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlJson);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));


                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONObject json = new JSONObject(new String(finalJson));
                JSONArray jsonArray = (JSONArray) json.get("generos");

                List<String> listaGeneros = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    listaGeneros.add(jsonArray.get(i).toString());
                }

                generos = (ArrayList<String>) listaGeneros;

                aaGeneros.notifyDataSetChanged();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
