package com.example.alejandroguerreroa.tiendademusicaandroid;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacturaFrm extends AppCompatActivity implements View.OnClickListener {

    //Spinner
    Spinner spCliente;
    Spinner spDisco;

    //TextViews
    TextView tvTotal;

    //EditText
    EditText etCantidad;

    //ListView
    ListView lvLista;

    //Buttons
    Button btnAgregar;
    Button btnPagar;

    //Arrays para llenar Spinners de Cliente y Disco
    ArrayList<String> listaClientes = new ArrayList<>();
    ArrayList<String> listaDiscos = new ArrayList<>();

    //Instancias
    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    Util_UI util_ui =  new Util_UI();

    //Para Lista
    ArrayList<String> carrito = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //Adapters para Clientes y Discos
    ArrayAdapter<String> adapterCliente;
    ArrayAdapter<String> adapterDisco;

    //Total
    int total;

    //Cantidades por disco
    ArrayList<Integer> cantidadesXDisco = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_frm);

        iniciaPantalla();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Crea Listener
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Limpia lista
                listaClientes.clear();
                listaDiscos.clear();

                //se toman todos los datos de cliente
                for (DataSnapshot clientes : dataSnapshot.child("Clientes").getChildren()) {

                    //Se guarda en un cliente
                    Cliente cliente =  clientes.getValue(Cliente.class);

                    //Se agrega a ListaClientes
                    listaClientes.add(clientes.getKey() + " - " + cliente.nombre + " - " + cliente.apellidos + " - "+cliente.correoElectronico);
                }

                //se toman todos los datos de productos
                for (DataSnapshot discos : dataSnapshot.child("Albumes").getChildren()) {

                    //Se guarda en un Producto
                    Album album = discos.getValue(Album.class);

                    //Se agrega a ListaDisco
                    listaDiscos.add(discos.getKey() + " - " + album.Artista + " - " +album.Album);
                }

                //Se agrega a Spinners
                adapterCliente.notifyDataSetChanged();
                adapterDisco.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Agrega el Listener
        mDB.addValueEventListener(valueEventListener);
    }

    private void iniciaPantalla() {
        //Spinners
        spCliente = findViewById(R.id.spCliente);
        spDisco = findViewById(R.id.spProducto);

        //TextViews
        tvTotal = findViewById(R.id.tvTotal);

        //EditText
        etCantidad = findViewById(R.id.etCantidad);

        //ListView
        lvLista = findViewById(R.id.lvLista);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carrito);
        lvLista.setAdapter(adapter);
        //Para Clientes
        adapterCliente = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaClientes);
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCliente.setAdapter(adapterCliente);
        //Para Discos
        adapterDisco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaDiscos);
        adapterDisco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDisco  .setAdapter(adapterDisco);

        //Buttons
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(this);
        btnPagar = findViewById(R.id.btnPagar);
        btnPagar.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAgregar:
                try {
                    //Separa texto
                    String[] caracDisco = spDisco.getSelectedItem().toString().split(" - ");


                    cantidadesXDisco.add(Integer.parseInt(etCantidad.getText().toString()));

                    //Agrega a la lista
                    carrito.add(caracDisco[0] + " - " + caracDisco[2] + " - Cantidad: " + etCantidad.getText().toString());
                    adapter.notifyDataSetChanged();




                    //Limpia campos
                    spDisco.setSelection(-1);
                    etCantidad.getText().clear();

                    break;
                } catch (Exception e) {
                    util_ui.MensajeToast(this, "Hubo un error");
                    break;
                }
            case R.id.btnPagar:
                try {
                    //ArrayList para cada fila
                    ArrayList<Factura> facturas = new ArrayList<>();

                    //Busca ID
                    String[] caracCliente = spCliente.getSelectedItem().toString().split(" - ");

                    //Por cada producto en carrito, lo va a agregar en factura
                    for (int i = 0; i < adapter.getCount(); i++) {

                        //Divide texto por fila de carrito
                        String[] caracFila = adapter.getItem(i).toString().split(" - ");

                        //Se crea una factura
                        Factura factura = new Factura(caracFila[0], cantidadesXDisco.get(i));

                        //Se agrega a facturasPorComprar
                        facturas.add(factura);
                    }

                    //Se crea la factura a guardar
                    Factura factura =  new Factura(caracCliente[0], java.time.LocalDate.now().toString(), facturas);

                    //Crea random key y agrega la factura
                    mDB.child("facturas").push().setValue(factura);

                    //Muestra mensaje
                    util_ui.MensajeToast(this, "Guardado con éxito");

                    //cierra Facturas
                    this.finish();

                    break;
                } catch (Exception e) {
                    util_ui.MensajeToast(this, "Hubo un error" + e.getMessage());
                    break;
                }
        }
    }
}


