package com.example.francisco.luces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }


    // Activacion de los botones. Mejorar la logica;un solo metodo con el destino como parametro
    public void iraLogin(View view) {
        Intent i=new Intent(this, Login.class);
        startActivity(i);
    }


    public void iraRegistro(View view) {
        Intent i=new Intent(this, Registro.class);
        startActivity(i);
    }

    //Esto es solo para efectos demo.
    //Enviamos el estado de logeo en TRUE, y con datos fijos
    //rut 13272823-2
    //clave 1234
    //serial ADBC100

    public void iraDemo(View view) {
        final String rut = "13272823-2";
        final String clave ="1234";
        final String serial ="ADBC100";
        final Boolean registrado = true;
        final int numero_luces = 0;

        Intent i=new Intent(this, Demo.class);
        i.putExtra("rut", rut);
        i.putExtra("clave", clave);
        i.putExtra("serial", serial);
        i.putExtra("registrado", registrado);
        i.putExtra("numero_luces", numero_luces);
        startActivity(i);
    }
}
