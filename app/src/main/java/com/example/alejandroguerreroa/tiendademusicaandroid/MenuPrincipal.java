package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener{

    //Botones
    Button clientesBtn;
    Button albumesBtn;
    Button facturasBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        iniciaPantalla();
    }

    private void iniciaPantalla() {
        //Botones
        clientesBtn = (Button) findViewById(R.id.btnClientes);
        clientesBtn.setOnClickListener(this);
        albumesBtn = (Button) findViewById(R.id.btnAlbumes);
        albumesBtn.setOnClickListener(this);
        facturasBtn = (Button) findViewById(R.id.btnFacturacion);
        facturasBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Abre clientes
            case R.id.btnClientes:
                startActivity(new Intent(MenuPrincipal.this, Clientes_Mostrar.class));
                break;
            
            //Abre facturacion
            case R.id.btnFacturacion:
                startActivity(new Intent(MenuPrincipal.this, FacturaFrm.class));
                break;
            
            //Abre Albumes
            case R.id.btnAlbumes:
                startActivity(new Intent(MenuPrincipal.this, Albumes_Mostrar.class));
                break;
        }
    }
}
