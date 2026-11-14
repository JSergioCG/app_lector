package com.example.appservicesce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appservicesce.ui.main.SectionsPagerAdapter;
import com.example.appservicesce.databinding.ActivityMainTabsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivityTabs extends AppCompatActivity {

private ActivityMainTabsBinding binding;

    //private String server_url = "http://54.207.205.169/mvcapicontroller/Api/Product?id=1&code_bar=";
    private String server_url = "http://45.194.52.165/mvcapicontroller/Api/Product?id=1&code_bar=";

    //private String server_url = "http://192.168.0.101/colibri3/Objetos/Caja/Almacen/Transacciones/WSCompleteNombre.asmx";
    //private String server_url = "http://54.207.205.169/SIGECE/Objetos/Gerencial/Formularios/gefRecepcionVentaJTable.aspx/ObtenerFiltros";
    //private String server_url = "http://192.168.0.101:8080/ApiRestTest/api.php?name=pen&id=";
    //private String server_url = "https://jsonplaceholder.typicode.com/todos/";
    private int i = 0;
    private String server_url_product = "";
    NavigationView btnRequest;
    RequestQueue requestQueue;
    private static final String TAG = MainActivityTabs.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainTabsBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager1 = binding.viewPager1;
        viewPager1.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager1);

        /*ViewPager viewPager2 = binding.viewPager2;
        viewPager2.setAdapter(sectionsPagerAdapter);
        //TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager2);*/

        /*FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });*/

        EditText textCodeBar = (EditText)findViewById(R.id.editTextText5);
        //TextView txt_home_1 = (TextView)findViewById(R.id.section_label);
        //TextView txt_home_2 = (TextView)findViewById(R.id.text_home_2);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        textCodeBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Log.i("aqui",s.toString());
                if(s.length() == 13){

                    String codebar = textCodeBar.getText().toString();
                    Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache,network);
                    requestQueue.start();

                    server_url_product = server_url + codebar;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url_product, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                            //Snackbar.make(view, response.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).setAnchorView(R.id.fab).show();
                            Log.d(TAG, response.toString());
                            String json;
                            json = response;
                            try {

                                JSONObject obj = new JSONObject(json);

                                Log.d("My App", obj.toString());

                                Log.d("afoImagen value ", obj.getString("afoImagen"));

                                /*viewPager.setCurrentItem(0, true);
                                sectionsPagerAdapter.getItem(0).home_title.setText(app.getCurrentPodcast().getTitle());*/

                                /*EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
                                etTextInput.setTextColor(Color.WHITE);
                                etTextInput.setHintTextColor(Color.WHITE);
                                etTextInput.setTextSize(12.5f);*/

                                String text_1 = "<h1><strong>Producto</strong></h1>"+
                                        //"<div><strong>CodigoBarra:</strong> "+obj.getString("lotCodigoBarras")+"</div>"+
                                        "<div><strong>Categoria:</strong> "+obj.getString("catNombre")+"</div>"+
                                        "<div><strong>SubCategoria:</strong> "+obj.getString("subNombre")+"</div>"+
                                        "<div><strong>Proveedor:</strong> "+obj.getString("pveNombre")+"</div>"+
                                        "<div><strong>Material F.:</strong> "+obj.getString("mafNombre")+"</div>"+
                                        "<div><strong>Material:</strong> "+obj.getString("matNombre")+"</div>"+
                                        "<div><strong>Color F.:</strong> "+obj.getString("cofNombre")+"</div>"+
                                        "<div><strong>Color:</strong> "+obj.getString("colNombre")+"</div>"+
                                        "<div><strong>Almacen:</strong> "+obj.getString("almNombre")+"</div>"+
                                        "<div><strong>Tamaño:</strong> "+obj.getString("tamNombre")+"</div>"+
                                        //"<div><strong>Caña:</strong> "+obj.getString("subCanha")+"</div>"+
                                        "<div><strong>Precio:</strong> "+obj.getString("drePrecio")+"</div>"+
                                        "<div><strong>Precio Sistema:</strong> "+obj.getString("drePrecioSistema")+"</div>"+
                                        "<div><strong>Precio Manual:</strong> "+obj.getString("drePrecioManual")+"</div>"+
                                        "<div><strong>Stock:</strong> "+obj.getString("Stock")+"</div>"+
                                        "";
                                //txt_home_1.setText(Html.fromHtml(text_1)); view_pager
                                ViewPager rootView1 = (ViewPager)findViewById(R.id.view_pager1); //inflater.inflate(R.layout.fragment_main, container, false);
                                TextView textView1 = ((TextView) rootView1. findViewById(R.id.section_label_1));
                                //Toast.makeText(getApplicationContext(),"Response :" + textView1.getId(), Toast.LENGTH_LONG).show();
                                textView1.setText(Html.fromHtml(text_1));

                                String text_2 = "<h1><strong>Disponibles</strong></h1>";
                                        //"<div><strong>Temporada:</strong> "+obj.getString("temNombre")+"</div>"+
                                        //"<div><strong>Gestion:</strong> "+obj.getString("gesNombre")+"</div>"+

                                        ;

                                String dispo = "";
                                //JSONObject jsonObj = new JSONObject(obj.getString("disponibles"));
                                //JSONObject first = new JSONArray(obj.getString("disponibles")).getJSONObject(0);
                                JSONArray JsonArray = new JSONArray(obj.getString("disponibles"));

                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject jsonObject = JsonArray.getJSONObject(i);

                                    String alm=jsonObject.getString("almNombre");
                                    String col=jsonObject.getString("colNombre");
                                    String tam=jsonObject.getString("tamNombre");
                                    dispo = dispo + "<div><strong>"+alm+":</strong> "+col+" "+tam+"</div>";
                                    //Log.i("JsonArray", dispo);
                                }

                                //Log.i("first", first.toString());
                                text_2 = text_2 + dispo;
                                //txt_home_2.setText(Html.fromHtml(text_2));
                                //ViewPager rootView2 = (ViewPager)findViewById(R.id.view_pager2); //inflater.inflate(R.layout.fragment_main, container, false);
                                TextView textView2 = ((TextView) rootView1. findViewById(R.id.section_label_2));
                                //Toast.makeText(getApplicationContext(),"Response :" + textView2.getId(), Toast.LENGTH_LONG).show();

                                //textView2.setText(Html.fromHtml(text_2));

                                createButtons(10);

                                String encodedImage = obj.getString("afoImagen");
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imageView.setImageBitmap(decodedByte);

                            } catch (Throwable tx) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                            }

                            requestQueue.stop();
                            textCodeBar.setText("");
                            textCodeBar.setEnabled(true);
                            textCodeBar.setFocusable(true);
                            textCodeBar.setFocusableInTouchMode(true);

                            textCodeBar.requestFocus();
                            if(textCodeBar.requestFocus()) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG,"Error :" + error.toString());
                        }
                    });

                    requestQueue.add(stringRequest);

                    /*textCodeBar.setText("");
                    textCodeBar.setEnabled(true);
                    textCodeBar.setFocusable(true);
                    textCodeBar.setFocusableInTouchMode(true);

                    textCodeBar.requestFocus();
                    if(textCodeBar.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textCodeBar.setText("290936V23R594");
        //textCodeBar.setVisibility(View.INVISIBLE);
        //textCodeBar.callOnClick();
    }

    public void createButtons(int count){
        for (int i = 1; i <= count; i++) {
            Button myButton = new Button(this);
            myButton.setId(i);
            myButton.setText("Push Me "+i);
            //myButton.setWidth(20);
            //myButton.setHeight(20);
            //myButton.setPadding(0,10,0,10);
            //myButton.setX(20);
            //myButton.setY(20);
            //myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,     LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            myButton.setLayoutParams(params);
            myButton.setGravity(Gravity.CENTER_HORIZONTAL);
            //myButton.setTextSize(32);

            LinearLayout ll = (LinearLayout)findViewById(R.id.linear_Layout_1);
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, params);

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MainActivityTabs.this,v.toString() ,Toast.LENGTH_SHORT).show();
                    Log.i("Clicked", "" + v.getId());
                }
            });

            Log.i(TAG,"Buttons :" + i);
        }
    }

}