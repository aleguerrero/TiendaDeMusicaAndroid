package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clientes_Mostrar extends AppCompatActivity implements View.OnClickListener{

    //Edit Texts
    EditText etBuscarCliente;

    //Buttons
    Button btnBuscar;
    Button btnAgregarCliente;

    //ListView
    ListView lvClientes;

    //Lista con Map para ListView con su adapter
    List<Map<String, String>> listaClientes = new ArrayList<>();
    SimpleAdapter adapter;

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
    }

    private void iniciaPantalla() {
        //EditText
        etBuscarCliente = findViewById(R.id.etBuscarNombre);

        //Buttons
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);
        btnAgregarCliente.setOnClickListener(this);

        //ListView
        lvClientes = findViewById(R.id.lvClientes);
        adapter = new SimpleAdapter(this, listaClientes,android.R.layout.simple_list_item_2,
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

                //Toma información y la guarda
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Saca info
                    Cliente cliente = snapshot.getValue(Cliente.class);

                    //Crea Mapa
                    Map<String, String> mapaClientes = new HashMap<>();

                    //agrega info a mapa
                    mapaClientes.put("nombre", cliente.nombre + " " + cliente.apellidos);
                    mapaClientes.put("id", snapshot.getKey());

                    //agrega a lista
                    listaClientes.add(mapaClientes);
                }

                //Notifica cambios
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDB.addValueEventListener(valueEventListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscar:
                //Toma Nombre
                String nombre = etBuscarCliente.getText().toString();

                //Crea otra lista
                List<Map<String, String>> listaClientesBusqueda = new ArrayList<>();

                //Loop para buscar clientes
                for (Map<String, String> map : listaClientes) {

                    //Crea Mapa
                    Map<String, String> mapaClientes = new HashMap<>();

                    for (Map.Entry<String, String> cliente : map.entrySet()) {

                        //Separa nombre y apellidos
                        String[] textos = cliente.getKey().;

                        //Busca que el nombre sea igual
                        if (nombre == textos[0]) {
                            mapaClientes.put("nombre", cliente.getValue());
                            mapaClientes.put("id", cliente.getValue());

                            //agrega a lista de busqueda
                            listaClientesBusqueda.add(mapaClientes);
                        }
                    }
                }

                //notifica adapter
                adapter.notifyDataSetChanged();

            case R.id.btnAgregarCliente:

        }
    }
}
