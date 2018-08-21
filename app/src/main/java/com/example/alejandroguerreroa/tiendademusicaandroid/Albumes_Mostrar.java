package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Albumes_Mostrar extends AppCompatActivity implements View.OnClickListener{

    //Edit Texts
    EditText etBuscarAlbum;

    //Buttons
    Button btnBuscar;
    Button btnAgregarAlbum;
    Button btnCancelar;

    //ListView
    ListView lvAlbumes;

    //Lista
    ArrayList<Album> albumes = new ArrayList<>();

    //Lista con Map para ListView con su adapter
    List<Map<String, String>> listaAlbumes = new ArrayList<>();
    SimpleAdapter adapter;

    //Crea otra lista para busqueda
    List<Map<String, String>> listaAlbumesBusqueda = new ArrayList<>();
    SimpleAdapter adapterBusqueda;

    //boolean para busqueda
    boolean busqueda = false;


    //FireBase
    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("Albumes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumes__mostrar);

        //carga items
        iniciaPantalla();

        //No abre el teclado automaticamente
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Ejecuta acciones de ListView
        ListViewClick();
    }

    private void iniciaPantalla() {
        //EditText
        etBuscarAlbum = findViewById(R.id.etBuscarNombre);

        //Buttons
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);

        btnAgregarAlbum = findViewById(R.id.btnAgregarAlbum);
        btnAgregarAlbum.setOnClickListener(this);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        //ListView
        lvAlbumes = findViewById(R.id.lvAlbumes);
        adapter = new SimpleAdapter(this, listaAlbumes,android.R.layout.simple_list_item_2,
                new String[] {"nombre", "id"}, new int[] {android.R.id.text1, android.R.id.text2});
        adapterBusqueda = new SimpleAdapter(this, listaAlbumesBusqueda, android.R.layout.simple_list_item_2,
                new String[] {"nombre", "id"}, new int[] {android.R.id.text1, android.R.id.text2});
        lvAlbumes.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Listener
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpia lista
                listaAlbumes.clear();

                //limpia albumes
                albumes.clear();

                try {
                    //Toma información y la guarda
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Saca info
                        Album album = snapshot.getValue(Album.class);

                        //Crea Mapa
                        Map<String, String> mapaAlbumes = new LinkedHashMap<>();

                        //agrega info a mapa
                        mapaAlbumes.put("nombre", album.Album + " de " +  album.Artista);
                        mapaAlbumes.put("id", snapshot.getKey());

                        //Crea cliente para lista Clientes
                        Album albumLista = new Album();

                        //Atributos
                        albumLista.id = snapshot.getKey();
                        albumLista.Album = album.Album;
                        albumLista.Artista = album.Artista;
                        albumLista.Genero = album.Genero;
                        albumLista.Year = album.Year;

                        //agrega a lista
                        listaAlbumes.add(mapaAlbumes);
                        albumes.add(albumLista);
                    }

                    //Notifica cambios
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    mostrarToast("Hubo un error");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDB.addValueEventListener(valueEventListener);
    }

    //Toma info de lista y la lleva a EdicionAlbumes
    private void ListViewClick(){
        lvAlbumes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (busqueda) {

                    //ArrayList para mandar a Edicion
                    ArrayList<String> albumReturn = new ArrayList<>();

                    //Toma id
                    String idAlbum = listaAlbumesBusqueda.get(position).get("id");

                    //Ciclo para buscar por albumes
                    for (Album album : albumes) {

                        //verifica que el ID sea igual al que se tomó
                        if (idAlbum.equals(album.id)) {
                            //toma info
                            albumReturn.add(album.id);
                            albumReturn.add(album.Album);
                            albumReturn.add(album.Artista);
                            albumReturn.add(album.Genero);
                            albumReturn.add(Long.toString(album.Year));
                        }

                    }

                    //Crea intent
                    Intent intent = new Intent(Albumes_Mostrar.this, EdicionAlbumFrm.class);
                    intent.putExtra("agregar", false);
                    intent.putExtra("cliente", albumReturn);

                    startActivity(intent);

                } else {
                    //ArrayList para mandar a Edicion
                    ArrayList<String> albumReturn = new ArrayList<>();

                    //Toma id
                    String idAlbum = listaAlbumes.get(position).get("id");

                    //Ciclo para buscar por albumes
                    for (Album album : albumes) {

                        //verifica que el ID sea igual al que se tomó
                        if (idAlbum.equals(album.id)) {
                            //toma info
                            albumReturn.add(album.id);
                            albumReturn.add(album.Album);
                            albumReturn.add(album.Artista);
                            albumReturn.add(album.Genero);
                            albumReturn.add(Long.toString(album.Year));
                        }

                    }

                    //Crea intent
                    Intent intent = new Intent(Albumes_Mostrar.this, EdicionAlbumFrm.class);
                    intent.putExtra("agregar", false);
                    intent.putExtra("cliente", albumReturn);

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBuscar:

                try {

                    //activa boolean
                    busqueda = true;

                    //limpia busqueda
                    listaAlbumesBusqueda.clear();

                    //Oculta teclado
                    hideKeyboard(this);

                    //Toma Nombre
                    String nombre = etBuscarAlbum.getText().toString();

                    //Loop para buscar clientes
                    for (Map<String, String> map : listaAlbumes) {

                        //Crea Mapa
                        Map<String, String> mapaAlbumes = new HashMap<>();

                        //Separa nombre y apellidos
                        String[] textos = map.get("nombre").split(" de ");
                        String nombreBuscar = textos[0];

                        //Busca que el nombre sea igual
                        if (nombre.equals(nombreBuscar)) {
                            mapaAlbumes.put("nombre", map.get("nombre"));
                            mapaAlbumes.put("id", map.get("id"));

                            //agrega a lista de busqueda
                            listaAlbumesBusqueda.add(mapaAlbumes);
                        }
                    }

                    //notifica adapter
                    lvAlbumes.setAdapter(adapterBusqueda);
                    adapterBusqueda.notifyDataSetChanged();

                    //break
                    break;

                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                }


            //Cancela búsqueda
            case R.id.btnCancelar:

                try {
                    //establece otro adapter
                    lvAlbumes.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    //limpia lista
                    listaAlbumesBusqueda.clear();

                    //vacia EditText
                    etBuscarAlbum.getText().clear();

                    //desactiva busqueda
                    busqueda = false;

                    //break
                    break;

                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                    break;
                }


            case R.id.btnAgregarAlbum:

                try {
                    //boolean de agregar
                    boolean agregar = true;

                    //Crea intento
                    Intent intent = new Intent(Albumes_Mostrar.this, EdicionAlbumFrm.class);
                    intent.putExtra("agregar", agregar);

                    //Inicia actividad
                    startActivity(intent);

                    //break
                    break;
                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                    break;
                }

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

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
    }
}
