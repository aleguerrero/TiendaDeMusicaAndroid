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

public class Clientes_Mostrar extends AppCompatActivity implements View.OnClickListener{

    //Edit Texts
    EditText etBuscarCliente;

    //Buttons
    Button btnBuscar;
    Button btnAgregarCliente;
    Button btnCancelar;

    //ListView
    ListView lvClientes;

    //Lista
    ArrayList<Cliente> clientes = new ArrayList<>();

    //Lista con Map para ListView con su adapter
    List<Map<String, String>> listaClientes = new ArrayList<>();
    SimpleAdapter adapter;

    //Crea otra lista
    List<Map<String, String>> listaClientesBusqueda = new ArrayList<>();
    SimpleAdapter adapterBusqueda;


    //FireBase
    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("Clientes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes__mostrar);

        //carga items
        iniciaPantalla();

        //No abre el teclado automaticamente
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Ejecuta acciones de ListView
        ListViewClick();
    }

    private void iniciaPantalla() {
        //EditText
        etBuscarCliente = findViewById(R.id.etBuscarNombre);

        //Buttons
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);
        btnAgregarCliente.setOnClickListener(this);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        //ListView
        lvClientes = findViewById(R.id.lvClientes);
        adapter = new SimpleAdapter(this, listaClientes,android.R.layout.simple_list_item_2,
                new String[] {"nombre", "id"}, new int[] {android.R.id.text1, android.R.id.text2});
        adapterBusqueda = new SimpleAdapter(this, listaClientesBusqueda, android.R.layout.simple_list_item_2,
                new String[] {"nombre", "id"}, new int[] {android.R.id.text1, android.R.id.text2});
        lvClientes.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Listener
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    //Toma información y la guarda
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Saca info
                        Cliente cliente = snapshot.getValue(Cliente.class);

                        //Crea Mapa
                        Map<String, String> mapaClientes = new LinkedHashMap<>();

                        //agrega info a mapa
                        mapaClientes.put("nombre", cliente.nombre + " " + cliente.apellidos);
                        mapaClientes.put("id", snapshot.getKey());

                        //Crea cliente para lista Clientes
                        Cliente clienteLista = new Cliente();

                        //Atributos
                        clienteLista.id = snapshot.getKey();
                        clienteLista.nombre = cliente.nombre;
                        clienteLista.apellidos = cliente.apellidos;
                        clienteLista.correoElectronico = cliente.correoElectronico;

                        //agrega a lista
                        listaClientes.add(mapaClientes);
                        clientes.add(clienteLista);
                    }

                    //Notifica cambios
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(null, "Hubo un error", Toast.LENGTH_LONG);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDB.addValueEventListener(valueEventListener);
    }

    //Toma info de lista y la lleva a EdicionCliente
    private void ListViewClick(){
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 String a = parent.getItemAtPosition(position).toString();


                //Busca info de cliente en lista
                for (Cliente cliente : clientes) {


                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscar:

                try {

                    //Oculta teclado
                    hideKeyboard(this);

                    //Toma Nombre
                    String nombre = etBuscarCliente.getText().toString();

                    //Loop para buscar clientes
                    for (Map<String, String> map : listaClientes) {

                        //Crea Mapa
                        Map<String, String> mapaClientes = new HashMap<>();

                        //Separa nombre y apellidos
                        String[] textos = map.get("nombre").split(" ");
                        String nombreBuscar = textos[0];

                        //Busca que el nombre sea igual
                        if (nombre.equals(nombreBuscar)) {
                            mapaClientes.put("nombre", map.get("nombre"));
                            mapaClientes.put("id", map.get("id"));

                            //agrega a lista de busqueda
                            listaClientesBusqueda.add(mapaClientes);
                        }
                    }

                    //notifica adapter
                    lvClientes.setAdapter(adapterBusqueda);
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
                    lvClientes.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    //break
                    break;
                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                    break;
                }


            case R.id.btnAgregarCliente:

                try {
                    //boolean de agregar
                    boolean agregar = true;

                    //Crea intento
                    Intent intent = new Intent(Clientes_Mostrar.this, EdicionClienteFrm.class);
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
}
