package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EdicionClienteFrm extends AppCompatActivity implements View.OnClickListener {

    //EditTexts
    EditText etCedula;
    EditText etNombre;
    EditText etApellidos;
    EditText etCorreoElectronico;

    //Buttons
    Button btnAgModCliente;
    Button btnBorrarCliente;

    //FireBase
    DatabaseReference mDBCliente = FirebaseDatabase.getInstance().getReference().child("Clientes");

    //Boolean de Agregar
    Boolean agregar;
    ArrayList cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_cliente_frm);

        //carga extras
        cargarExtras();

        //carga items
        iniciaPantalla();

        //No abre el teclado automaticamente
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void cargarExtras() {

        //carga boolean de si es para agregar
        agregar = getIntent().getExtras().getBoolean("agregar");

        //carga lista
        cliente = getIntent().getExtras().getStringArrayList("cliente");

    }

    private void iniciaPantalla() {

        //EditTexts
        etCedula = (EditText) findViewById(R.id.etAlbum);
        etNombre = (EditText) findViewById(R.id.etArtista);
        etApellidos = (EditText) findViewById(R.id.etGenero);
        etCorreoElectronico = (EditText) findViewById(R.id.etYear);

        //Buttons
        btnAgModCliente = (Button) findViewById(R.id.btnAgModAlbum);
        btnAgModCliente.setOnClickListener(this);

        btnBorrarCliente = (Button) findViewById(R.id.btnBorrarCliente);
        btnBorrarCliente.setOnClickListener(this);

        //Modifica Boton Agregar o Modificar
        if (agregar) {
            btnAgModCliente.setText("Agregar Cliente");
        } else {
            btnAgModCliente.setText("Modificar Cliente");

            //Muestra boton de borrar
            btnBorrarCliente.setVisibility(View.VISIBLE);

            //Agrega Datos de Cliente
            cargarDatos(cliente);
        }

    }

    private void cargarDatos(ArrayList cliente) {

        try {
            //Carga datos en EditTexts
            etCedula.setText(cliente.get(0).toString());

            //deshabilita ID
            etCedula.setEnabled(false);

            //continua cargando datos
            etNombre.setText(cliente.get(1).toString());
            etApellidos.setText(cliente.get(2).toString());
            etCorreoElectronico.setText(cliente.get(3).toString());
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
                        //Crea cliente
                        Cliente cliente = new Cliente(
                                etNombre.getText().toString(),
                                etApellidos.getText().toString(),
                                etCorreoElectronico.getText().toString()
                        );

                        //Agrega a FireBase
                        mDBCliente.child(etCedula.getText().toString()).setValue(cliente);

                        //Toast
                        Toast.makeText(this, "Agregado con éxito", Toast.LENGTH_LONG);

                        //cierra
                        this.finish();

                        break;

                    } else {

                        //Crea cliente
                        Cliente cliente = new Cliente(
                                etNombre.getText().toString(),
                                etApellidos.getText().toString(),
                                etCorreoElectronico.getText().toString()
                        );

                        //Lo modifica
                        mDBCliente.child(etCedula.getText().toString()).setValue(cliente);

                        //Cierra
                        this.finish();

                        break;
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
                }

            //Borra cliente
            case R.id.btnBorrarCliente:

                //Muestra cuadro de eliminar
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Seguro de que desea eliminar el cliente?").setPositiveButton("Sí", dialogClickListener)
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
                        mDBCliente.child(etCedula.getText().toString()).removeValue();

                        //String mensaje
                        String mensaje = "Cliente eliminado";

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
}
