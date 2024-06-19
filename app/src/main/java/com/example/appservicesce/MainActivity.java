package com.example.appservicesce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appservicesce.databinding.ActivityMainBinding;

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

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //private String server_url = "http://54.207.205.169/mvcapicontroller/Api/Product?id=1&code_bar=";
    private String server_url = "http://192.168.0.101/mvcapicontroller/Api/Product?id=1&code_bar=";

    //private String server_url = "http://192.168.0.101/colibri3/Objetos/Caja/Almacen/Transacciones/WSCompleteNombre.asmx";
    //private String server_url = "http://54.207.205.169/SIGECE/Objetos/Gerencial/Formularios/gefRecepcionVentaJTable.aspx/ObtenerFiltros";
    //private String server_url = "http://192.168.0.101:8080/ApiRestTest/api.php?name=pen&id=";
    //private String server_url = "https://jsonplaceholder.typicode.com/todos/";
    private int i = 0;

    private String server_url_product = "";
    NavigationView btnRequest;
    RequestQueue requestQueue;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        /*EditText textCodeBar = (EditText)findViewById(R.id.editTextCodeBar);
        TextView txt_home_1 = (TextView)findViewById(R.id.text_home);
        TextView txt_home_2 = (TextView)findViewById(R.id.text_home_2);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);*/

        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                i++;


                Toast.makeText(getApplicationContext(),"Response : Pruebilla " + i , Toast.LENGTH_LONG).show();

            }
        });*/

        /*textCodeBar.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Toast.makeText(getApplicationContext(),"Tecla : " + keyCode , Toast.LENGTH_LONG).show();
                Log.e("My Event", "On Key Pressed: \"" + keyCode + "\"");
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                }
                return true;
            }
        });*/

        /*textCodeBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Log.i("a",s.toString());
                if(s.length() == 13){

                    Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache,network);
                    requestQueue.start();

                    server_url_product = server_url + textCodeBar.getText();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url_product, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                            //Snackbar.make(view, response.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).setAnchorView(R.id.fab).show();

                            String json = response.toString();
                            try {

                                JSONObject obj = new JSONObject(json);

                                Log.d("My App", obj.toString());
                                Log.d("phonetype value ", obj.getString("lotCodigoBarras"));

                                String text_1 = "<h1><strong>Producto</strong></h1>"+
                                        //"<div><strong>CodigoBarra:</strong> "+obj.getString("lotCodigoBarras")+"</div>"+
                                        "<div><strong>Categoria:</strong> "+obj.getString("catNombre")+"</div>"+
                                        "<div><strong>SubCategoria:</strong> "+obj.getString("subNombre")+"</div>"+
                                        "<div><strong>Proveedor:</strong> "+obj.getString("pveNombre")+"</div>"+
                                        "<div><strong>Material F.:</strong> "+obj.getString("mafNombre")+"</div>"+
                                        "<div><strong>Material:</strong> "+obj.getString("matNombre")+"</div>"+
                                        "<div><strong>Color F.:</strong> "+obj.getString("cofNombre")+"</div>"+
                                        "<div><strong>Color:</strong> "+obj.getString("colNombre")+"</div>"+
                                        "";
                                //txt_home_1.setText(obj.getString("lotCodigoBarras"));
                                txt_home_1.setText(Html.fromHtml(text_1));

                                String text_2 = "<h1><strong>Detalle</strong></h1>"+
                                        //"<div><strong>Temporada:</strong> "+obj.getString("temNombre")+"</div>"+
                                        //"<div><strong>Gestion:</strong> "+obj.getString("gesNombre")+"</div>"+

                                        "<div><strong>Almacen:</strong> "+obj.getString("almNombre")+"</div>"+
                                        "<div><strong>Tamaño:</strong> "+obj.getString("tamNombre")+"</div>"+
                                        //"<div><strong>Caña:</strong> "+obj.getString("subCanha")+"</div>"+
                                        "<div><strong>Precio:</strong> "+obj.getString("drePrecio")+"</div>"+
                                        "<div><strong>Precio Sistema:</strong> "+obj.getString("drePrecioSistema")+"</div>"+
                                        "<div><strong>Precio Manual:</strong> "+obj.getString("drePrecioManual")+"</div>"+
                                        "<div><strong>Disponibles:</strong> "+obj.getString("disponibles")+"</div>"+
                                        "";
                                txt_home_2.setText(Html.fromHtml(text_2));


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
                            //setContentView(R.layout.sharedfiles);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG,"Error :" + error.toString());
                        }
                    });

                    requestQueue.add(stringRequest);

                    textCodeBar.setText("");
                    textCodeBar.setEnabled(true);
                    textCodeBar.setFocusable(true);
                    textCodeBar.setFocusableInTouchMode(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*btnRequest = (NavigationView) findViewById(R.id.nav_view);
        btnRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Error :" + error.toString());
                    }
                });

                requestQueue.add(stringRequest);
            }
        });*/

    }



    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != android.R.id.home) {
            //Timber.d("Home pressed");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                    requestQueue.stop();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            });

            requestQueue.add(stringRequest);
        }
        return super.onOptionsItemSelected(menuItem);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }*/
}