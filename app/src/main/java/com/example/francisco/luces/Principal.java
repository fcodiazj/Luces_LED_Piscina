package com.example.francisco.luces;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.francisco.luces.com.example.francisco.entities.Luces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    private boolean modificar = false;
    private boolean mas = false;
    private boolean menos = false;
    private boolean moviendo = false;

    //private int max_luces = 12;//MAxima cantidad de luces a controlar
    private int posx = 1;
    private int posy = 1;
    private int max_fila = 8;
    private int max_columna = 8;
    private String posicion;
    private int recursoID;

    public String serial;
    public int numero_luces_registradas;
    public int numero_luces_usadas = 0;


    private String uriH = "@drawable/horizontal";
    private String uriV = "@drawable/vertical";
    private String uriSI = "@drawable/sup_iz";
    private String uriSD = "@drawable/sup_de";
    private String uriII = "@drawable/inf_iz";
    private String uriID = "@drawable/inf_de";

    private String URL_BASE = "http://jashu.us.to/Luces_serweb";
    private String URL_RECURSO = "/usuarios";
    private String URL_ACCION = "/mostrar";

    @Override
    public void onClick(View v) {
        Integer iddrawable;
        iddrawable = (Integer) v.getTag(R.id.id_imagen);
        iddrawable = iddrawable == null ? 0 : iddrawable;

        //determino si estoy en un borde o en una luz (mover/agregar)
        boolean es_borde = ((iddrawable == R.drawable.vertical) || (iddrawable == R.drawable.horizontal) || (iddrawable == R.drawable.sup_iz) || (iddrawable == R.drawable.sup_de) || (iddrawable == R.drawable.inf_iz) || (iddrawable == R.drawable.inf_de));
        if (modificar == true) {
            //si modificar esta activado, se puede agregar, quitar, o mover
            if  (es_borde){
                //determino si estoy cambiando posicion o agregando luz
                if (moviendo == true) {
                    //estoy moviendo
                    /* Esto no va; les gusto mucho el poner y sacar una luz en forma independiente*/
                } else {
                    if (mas == true) {
                        //estoy agregando luces

                        if (numero_luces_usadas<numero_luces_registradas){
                            numero_luces_usadas=numero_luces_usadas+1;
                            String luz_a_poner = "@drawable/luz" + String.valueOf(numero_luces_usadas);
                            int id_luz_a_poner = getResources().getIdentifier(luz_a_poner, "drawable", getPackageName());
                            ImageView borde_a_cambiar = (ImageView) findViewById(v.getId());
                            borde_a_cambiar.setTag(R.id.id_imagenview_container, v.getId());
                            borde_a_cambiar.setTag(R.id.id_imagen, id_luz_a_poner);
                            borde_a_cambiar.setTag(R.id.imagen, luz_a_poner);
                            //asocio un menu contextal a la imagen de la luz
                            registerForContextMenu(borde_a_cambiar);
                            borde_a_cambiar.setBackgroundColor(Color.rgb(255, 255, 255));
                            borde_a_cambiar.setImageResource(id_luz_a_poner);

                            //aca hay que agregar el insert a la base de datos

                        } else {
                            Toast toast1 = Toast.makeText(getApplicationContext(),"No se pueden agregar mas luces", Toast.LENGTH_SHORT);
                            toast1.show();
                        }

                    }
                }
            } else {
                //estoy quitando luces
                if (menos == true) {

                    if (numero_luces_usadas>0){
                        numero_luces_usadas=numero_luces_usadas-1;

                        String posicion_a_borrar = (String)v.getTag(R.id.posicion);
                        int fila = Integer.valueOf(posicion_a_borrar.substring(3,4));
                        int columna = Integer.valueOf(posicion_a_borrar.substring(4,5));

                        String borde_a_poner="";
                        if (fila==1) {
                            switch(columna){
                                case 1:
                                    borde_a_poner = uriSI;
                                    break;

                                case 8:
                                    borde_a_poner = uriSD;
                                    break;

                                default:
                                    borde_a_poner = uriH;
                                    break;
                            }
                        }

                        if (fila==8) {
                            switch (columna) {
                                case 1:
                                    borde_a_poner = uriII;
                                    break;

                                case 8:
                                    borde_a_poner = uriID;
                                    break;

                                default:
                                    borde_a_poner = uriH;
                                    break;
                            }
                        }

                        if (!((fila==8)||(fila==1))){
                            borde_a_poner = uriV;
                        }

                        int id_imagen_a_poner = getResources().getIdentifier(borde_a_poner, "drawable", getPackageName());

                        ImageView borde_a_cambiar = (ImageView) findViewById(v.getId());
                        borde_a_cambiar.setTag(R.id.imagen, borde_a_poner);
                        borde_a_cambiar.setTag(R.id.id_imagen, id_imagen_a_poner);
                        borde_a_cambiar.setBackgroundColor(Color.rgb(255, 255, 255));
                        borde_a_cambiar.setImageResource(id_imagen_a_poner);

                        //aca hay que agregar el drop a la base de datos
                    } else {
                        Toast toast1 = Toast.makeText(getApplicationContext(),"No existen luces para borrar", Toast.LENGTH_SHORT);
                        toast1.show();
                    }

                }
            }
        } else {
            if (!es_borde) {
                ImageView luz_a_cambiar = (ImageView) findViewById(v.getId());
                int color_luz = ((ColorDrawable)luz_a_cambiar.getBackground()).getColor();
                if (color_luz==Color.rgb(255,255, 255))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(255, 0, 0));
                if (color_luz==Color.rgb(255,0, 0))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(0, 99, 0));
                if (color_luz==Color.rgb(0,99, 0))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(0, 0, 204));
                if (color_luz==Color.rgb(0,0,204))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(255, 235, 0));
                if (color_luz==Color.rgb(255, 235, 0))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(102,0,204));
                if (color_luz==Color.rgb(102,0,204))
                    luz_a_cambiar.setBackgroundColor(Color.rgb(255,255,255));

                //otro update, ahora para el color
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Seleccione el color de la luz:");
        //add(int groupId, int itemId, int order, CharSequence title)
        menu.add(view.getId(), 1, 1, "Rojo" );
        menu.add(view.getId(), 2, 2, "Verde" );
        menu.add(view.getId(), 3, 3, "Azul" );
        menu.add(view.getId(), 4, 4, "Amarillo" );
        menu.add(view.getId(), 5, 5, "Morado" );
        menu.add(view.getId(), 6, 6, "Blanco" );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.colores, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //busco la luz que quiero cambiar
        int selectedViewID = item.getGroupId();
        ImageView luz_a_cambiar = (ImageView) findViewById(selectedViewID);


        switch (item.getItemId()) {
            case 1:
                luz_a_cambiar.setBackgroundColor(Color.rgb(255, 0, 0));
                return true;
            case 2:
                luz_a_cambiar.setBackgroundColor(Color.rgb(0, 99, 0));
                return true;
            case 3:
                luz_a_cambiar.setBackgroundColor(Color.rgb(0, 0, 204));
                return true;
            case 4:
                luz_a_cambiar.setBackgroundColor(Color.rgb(255, 235, 0));
                return true;
            case 5:
                luz_a_cambiar.setBackgroundColor(Color.rgb(102, 0, 204));
                return true;
            case 6:
                luz_a_cambiar.setBackgroundColor(Color.rgb(255, 255, 255));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void ponerLuz (String color, int pos_fila, int pos_col ) {
        if (numero_luces_usadas < numero_luces_registradas) {
            numero_luces_usadas = numero_luces_usadas + 1;

            //selecciono el srawable de la luz correspondiente
            String luz_a_poner = "@drawable/luz" + String.valueOf(numero_luces_usadas);
            int id_luz_a_poner = getResources().getIdentifier(luz_a_poner, "drawable", getPackageName());

            //selecciono cual de todos los imageview debe ser cambiado
            posicion = "pos" + String.valueOf(pos_fila) + String.valueOf(pos_col);
            recursoID = getResources().getIdentifier(posicion, "id", getPackageName());

            ImageView img = (ImageView) findViewById(recursoID);
            img.setOnClickListener(this);

            img.setTag(R.id.id_imagenview_container,recursoID);
            img.setTag(R.id.id_imagen, id_luz_a_poner);
            img.setTag(R.id.imagen, luz_a_poner);
            img.setTag(R.id.posicion, posicion);

            //asocio un menu contextal a la imagen de la luz
            registerForContextMenu(img);
            switch (color) {
                case "rojo":
                    img.setBackgroundColor(Color.rgb(255, 0, 0));
                    break;

                case "verde":
                    img.setBackgroundColor(Color.rgb(0, 99, 0));
                    break;

                case "azul":
                    img.setBackgroundColor(Color.rgb(0, 0, 204));
                    break;

                case "amarillo":
                    img.setBackgroundColor(Color.rgb(255, 235, 0));
                    break;

                case "morado":
                    img.setBackgroundColor(Color.rgb(102, 0, 204));
                    break;

                case "blanco":
                    img.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;

                default:
                    img.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;
            }

            img.setImageResource(id_luz_a_poner);

        } else {
            Toast toast1 = Toast.makeText(getApplicationContext(),"No se pueden agregar mas luces", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //pongo las imagenes del marco
        int imageResourceH = getResources().getIdentifier(uriH, "drawable", getPackageName());
        int imageResourceV = getResources().getIdentifier(uriV, "drawable", getPackageName());
        int imageResourceSI = getResources().getIdentifier(uriSI, "drawable", getPackageName());
        int imageResourceSD = getResources().getIdentifier(uriSD, "drawable", getPackageName());
        int imageResourceII = getResources().getIdentifier(uriII, "drawable", getPackageName());
        int imageResourceID = getResources().getIdentifier(uriID, "drawable", getPackageName());


        for (int x=2,y=2;x<max_fila&&y<max_columna;x++,y++)
        {
            //x=i y=1 constante
            posicion = "pos" + String.valueOf(x) + String.valueOf(posy);
            recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
            ImageView img1 = (ImageView) findViewById(recursoID);
            img1.setOnClickListener(this);
            img1.setTag(R.id.posicion, posicion);
            img1.setTag(R.id.imagen, uriV);
            img1.setTag(R.id.id_imagen, R.drawable.vertical);
            img1.setImageResource(imageResourceV);

            //x=i y=5 constante
            posicion = "pos" + String.valueOf(x) + String.valueOf(max_fila);
            recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
            ImageView img2 = (ImageView) findViewById(recursoID);
            img2.setOnClickListener(this);
            img2.setTag(R.id.posicion, posicion);
            img2.setTag(R.id.imagen, uriV);
            img2.setTag(R.id.id_imagen, R.drawable.vertical);
            img2.setImageResource(imageResourceV);

            //x=1 constante y=i
            posicion = "pos" + String.valueOf(posx) + String.valueOf(y);
            recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
            ImageView img3 = (ImageView) findViewById(recursoID);
            img3.setOnClickListener(this);
            img3.setTag(R.id.posicion, posicion);
            img3.setTag(R.id.imagen, uriV);
            img3.setTag(R.id.id_imagen, R.drawable.horizontal);
            img3.setImageResource(imageResourceH);

            //x=5 constante y=i
            posicion = "pos" + String.valueOf(max_columna) + String.valueOf(y);
            recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
            ImageView img4 = (ImageView) findViewById(recursoID);
            img4.setOnClickListener(this);
            img4.setTag(R.id.posicion, posicion);
            img4.setTag(R.id.imagen, uriV);
            img4.setTag(R.id.id_imagen, R.drawable.horizontal);
            img4.setImageResource(imageResourceH);
        }
        //Esquina SI x=1 y=1 constante
        posicion = "pos" + String.valueOf(posx) + String.valueOf(posy);
        recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
        ImageView img1 = (ImageView) findViewById(recursoID);
        img1.setOnClickListener(this);
        img1.setTag(R.id.posicion, posicion);
        img1.setTag(R.id.imagen, uriV);
        img1.setTag(R.id.id_imagen, R.drawable.sup_iz);
        img1.setImageResource(imageResourceSI);

        //Esquina SD x=1 y=8 constante
        posicion = "pos" + String.valueOf(posx) + String.valueOf(max_fila);
        recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
        ImageView img2 = (ImageView) findViewById(recursoID);
        img2.setOnClickListener(this);
        img2.setTag(R.id.posicion, posicion);
        img2.setTag(R.id.imagen, uriV);
        img2.setTag(R.id.id_imagen, R.drawable.sup_de);
        img2.setImageResource(imageResourceSD);

        //Esquina II x=8 y=1 constante
        posicion = "pos" + String.valueOf(max_columna) + String.valueOf(posy);
        recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
        ImageView img3 = (ImageView) findViewById(recursoID);
        img3.setOnClickListener(this);
        img3.setTag(R.id.posicion, posicion);
        img3.setTag(R.id.imagen, uriV);
        img3.setTag(R.id.id_imagen, R.drawable.inf_iz);
        img3.setImageResource(imageResourceII);

        //Esquina ID x=8 y=8 constante
        posicion = "pos" + String.valueOf(max_columna) + String.valueOf(max_fila);
        recursoID = getResources().getIdentifier(posicion, "id", getPackageName());
        ImageView img4 = (ImageView) findViewById(recursoID);
        img4.setOnClickListener(this);
        img4.setTag(R.id.posicion, posicion);
        img4.setTag(R.id.imagen, uriV);
        img4.setTag(R.id.id_imagen, R.drawable.inf_de);
        img4.setImageResource(imageResourceID);

        //pongo los botones +/- en apagado
        final Button button_mas = (Button) findViewById(R.id.button_mas);
        final Button button_menos = (Button) findViewById(R.id.button_menos);
        button_mas.setVisibility(View.GONE);
        button_menos.setVisibility(View.GONE);

        //defino el comportamiento del boton modificar
        final Button button_modificar = (Button) findViewById(R.id.button_modificar);
        button_modificar.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!modificar) {
                    modificar=true;
                    //button_modificar.setBackgroundColor(0xFF00FF00);
                    button_mas.setVisibility(View.VISIBLE);
                    button_menos.setVisibility(View.VISIBLE);
                } else {
                    modificar=false;
                    //button_modificar.setBackgroundColor(0x00000000);
                    button_mas.setVisibility(View.GONE);
                    button_menos.setVisibility(View.GONE);
                }
            }
        });


        //defino el comportamiento del boton +
        button_mas.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numero_luces_usadas<numero_luces_registradas) {
                    mas=true;
                    menos=false;

                }else {
                    Toast toast1 = Toast.makeText(getApplicationContext(),"No se pueden agregar mas luces", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }
        });

        //defino el comportamiento del boton -
        button_menos.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numero_luces_usadas<numero_luces_registradas){
                    mas=false;
                    menos=true;

                }else {
                    Toast toast1 = Toast.makeText(getApplicationContext(),"No existen luces para borrar", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });



        //rescato los parametros que vienen desde la otra activity
        Bundle bundle = getIntent().getExtras();
        String rut = bundle.getString("rut");
        Boolean registrado = bundle.getBoolean("registrado");


        //debo rescatar las cosas desde la base de datos, con el rut obtenido
        final String URL = URL_BASE + URL_RECURSO + URL_ACCION;

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("rut", rut);

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
                            serial = response.getString("serial").toString();
                            numero_luces_registradas = Integer.valueOf(response.get("numero luces").toString());

                            final Luces luces[] = new Luces[response.getJSONArray("luces").length()];
                            JSONArray getArray = response.getJSONArray("luces");
                            for (int i = 0; i < response.getJSONArray("luces").length(); i++) {
                                JSONObject objects = getArray.getJSONObject(i);
                                Luces lucesItem = new Luces();
                                lucesItem.setIdLuz(Integer.valueOf(objects.get("id_luz").toString()));
                                lucesItem.setColor(objects.get("color").toString());
                                lucesItem.setPosFila(Integer.valueOf(objects.get("pos_fila").toString()));
                                lucesItem.setPosCol(Integer.valueOf(objects.get("pos_col").toString()));
                                luces[i]=lucesItem;
                                ponerLuz (lucesItem.getColor(), lucesItem.getPosFila(), lucesItem.getPosCol());
                            }
                        } catch (Exception e) {
                            //donde el string viene vacio???
                            e.printStackTrace();
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

        //Le asigno esos valores a los diferentes TextView
        TextView txtrut = (TextView)findViewById(R.id.rut);
        txtrut.setText(rut);

        TextView txtserial = (TextView)findViewById(R.id.serial);
        txtserial.setText(serial);

        TextView txtregistrado = (TextView)findViewById(R.id.registrado);
        if (registrado) {
            txtregistrado.setText("logeado");
        } else {
            txtregistrado.setText("no logeado");
        }

    }

}

