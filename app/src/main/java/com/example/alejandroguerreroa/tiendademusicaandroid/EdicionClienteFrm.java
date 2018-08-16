package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    DatabaseReference mDBCliente = FirebaseDatabase.getInstance().getReference().child("clientes");

    //Boolean de Agregar
    Boolean agregar = getIntent().getExtras().getBoolean("agregar");
    ArrayList cliente = getIntent().getExtras().getStringArrayList("cliente");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_cliente_frm);

        //carga items
        iniciaPantalla();

        //No abre el teclado automaticamente
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void iniciaPantalla() {

        //EditTexts
        etCedula = (EditText) findViewById(R.id.etCedula);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etCorreoElectronico = (EditText) findViewById(R.id.etCorreoElectronico);

        //Buttons
        btnAgModCliente = (Button) findViewById(R.id.btnAgModCliente);
        btnAgModCliente.setOnClickListener(this);

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

        btnBorrarCliente = (Button) findViewById(R.id.btnBorrarCliente);
        btnBorrarCliente.setOnClickListener(this);
    }

    private void cargarDatos(ArrayList cliente) {

        //Carga datos en EditTexts
        etCedula.setText(cliente.indexOf(0));
        etNombre.setText(cliente.indexOf(1));
        etApellidos.setText(cliente.indexOf(2));
        etCorreoElectronico.setText(cliente.indexOf(3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Boton Agregar
            case R.id.btnAgModCliente:

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
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG);
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
