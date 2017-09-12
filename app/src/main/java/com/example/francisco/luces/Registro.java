package com.example.francisco.luces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;


public class Registro extends AppCompatActivity {
    private EditText TextNombre;
    private EditText TextRut;
    private EditText TextClave;
    private EditText TextCorreo;
    private EditText TextSerial;
    private Button buttonRegister;
    private static final String URL_BASE = "http://jashu.us.to/Luces_serweb";
    private static final String URL_RECURSO = "/usuarios";
    private static final String URL_ACCION = "/registrar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



        TextNombre = (EditText) findViewById(R.id.textNombre);
        TextRut = (EditText) findViewById(R.id.textRut);
        TextClave = (EditText) findViewById(R.id.textClave);
        TextCorreo = (EditText) findViewById(R.id.textCorreo);
        TextSerial = (EditText) findViewById(R.id.textSerial);
        buttonRegister = (Button) findViewById(R.id.botonir);
        buttonRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonRegister) {
                    registerUser();
                }
            }

            private void registerUser() {
                final String nombre = TextNombre.getText().toString().trim();
                final String rut = TextRut.getText().toString().trim();
                final String password = TextClave.getText().toString().trim();
                final String correo = TextCorreo.getText().toString().trim();
                final String serial = TextSerial.getText().toString().trim();

                StringRequest stringRequest = new StringRequest
                                    (   Request.Method.POST,
                                        URL_BASE + URL_RECURSO + URL_ACCION,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.trim().equals("true")) {
                                                    Toast.makeText(getBaseContext(), "Register OK", Toast.LENGTH_LONG).show();
                                                    openLogin();
                                                } else {
                                                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    )
                    {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> parametros = new HashMap<String, String>();
                            parametros.put("nombre", TextNombre.getText().toString().trim());
                            parametros.put("rut", TextRut.getText().toString().trim());
                            parametros.put("login", TextRut.getText().toString().trim());
                            parametros.put("password", TextClave.getText().toString().trim());
                            parametros.put("correo", TextCorreo.getText().toString().trim());
                            parametros.put("serial", TextSerial.getText().toString().trim());
                            return parametros;
                        }

                        /*@Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> parametros = new HashMap<String, String>();
                            parametros.put("Content-Type","application/x-www-form-urlencoded");
                        return parametros;
                        }*/
                    };

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                requestQueue.add(stringRequest);
            }


            private void openLogin(){
                Intent i=new Intent(getApplicationContext(), Demo.class);
                startActivity(i);
            }
        }
        );
    }
}

