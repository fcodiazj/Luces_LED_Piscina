package com.example.francisco.luces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {


    private EditText TextRut;
    private EditText TextClave;

    private Button buttonRegister;
    private static final String URL_BASE = "http://jashu.us.to/Luces_serweb";
    private static final String URL_RECURSO = "/usuarios";
    private static final String URL_ACCION = "/loguear";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button botonAceptar = (Button)findViewById(R.id.boton_aceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validarDatos()
            }
        });

        TextRut = (EditText) findViewById(R.id.textRut);
        TextClave = (EditText) findViewById(R.id.textClave);

        buttonRegister = (Button) findViewById(R.id.botonir);
        buttonRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonRegister) {
                    //aca deben ir las validaciones de los campos
                    registerUser();
                }
            }

            private void registerUser() {

                final String rut = TextRut.getText().toString().trim();
                final String password = TextClave.getText().toString().trim();

                final String URL = URL_BASE + URL_RECURSO + URL_ACCION;
                // Post params to be sent to the server
                HashMap<String, String> params = new HashMap<String, String>();
                 params.put("rut", rut);
                 params.put("login", rut);
                 params.put("password", password);


                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                JsonObjectRequest request_json = new JsonObjectRequest(
                        Request.Method.POST,
                        URL,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Process os success response
                                try {
                                    Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                                    final String rut = response.getString("rut");
                                    openLogin(rut);
                                } catch (JSONException e) {
                                    //donde el string viene vacio???
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                            }
                        }
                );
                queue.add(request_json);
            }
        });
    }

    private void openLogin(String rut){
        final Boolean registrado = true;

        Intent i=new Intent(this, Principal.class);
        i.putExtra("rut",rut);
        i.putExtra("registrado", registrado);
        startActivity(i);
    }

}
