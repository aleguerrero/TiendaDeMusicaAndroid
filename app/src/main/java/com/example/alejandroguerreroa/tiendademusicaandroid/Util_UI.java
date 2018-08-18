package com.example.alejandroguerreroa.tiendademusicaandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class Util_UI {

    public Util_UI(){}

    public void MensajeToast(Context elContexto, String elTexto){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(elContexto, elTexto, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public ArrayAdapter CargaArrayAdapter(Context elContexto, ArrayList<String> ArregloLista){
        final ArrayAdapter elAdaptador;
        elAdaptador = new ArrayAdapter(elContexto, android.R.layout.simple_list_item_1, ArregloLista);
        return elAdaptador;
    }
    public void MensajeAlertDialog(Context elContexto, String elTitulo, String elTexto){
        AlertDialog.Builder AlertConstruct = new AlertDialog.Builder(elContexto);
        AlertConstruct.setMessage(elTexto);
        AlertConstruct.setTitle(elTitulo);
        AlertConstruct.setCancelable(true);

        AlertConstruct.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        System.exit(1);
                    }
                });

        AlertConstruct.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog elAlert = AlertConstruct.create();
        elAlert.show();
    }


}//Fin Util_UI
