package com.example.appservicesce;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PrincipalActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    private final String server_url = "http://45.194.52.165/mvcapicontroller/api/articulo";
    //private String server_url = "http://192.168.0.101/mvcapicontroller/api/articulo";

    private int i = 0;
    private String server_url_product = "";
    NavigationView btnRequest;
    RequestQueue requestQueue;
    private static final String TAG = MainActivityTabs.class.getName();

    public boolean sw = true;
    public Integer sucursalEnable = -1;


    final String[] color = { "Amarillo", "Azul", "Beige", "Marron", "Estampado", "Gris", "Metalico", "Multicolor", "Naranja", "Negro", "Rojo", "Rosa", "Verde", "Violeta", "Plateado", "Dorado", "Blanco", "Animale Print", "Transparente", "Bronze" };
    final String[] hexadecimal = { "#FFD300", "#1630BE", "#D7D7A8", "#804000", "#ESTAMPADO", "#9B9B9B","#535654", "#MULTICOLOR", "#FFA500", "#000000", "#FF0000", "#FFC0CB", "#00913F", "#4C2882", "#E3E4E5", "#CFB53B", "#FFFFFF",  "#ANIMAL", "#FFFFFF", "#BF8970"  };
    final String[] sucursales = { "Central" , "America", "Bolivar", "La Paz", "Cochabamba", "velasco", "MegaCenter", "Transitorio", "Fallas", "outlet", "San Martin", "Distribuidor", "Almacen Saldos", "Comercial", "Ventura Mall", "Ventura Arezzo", "Las Brisas", "Insumos", "Patio Design", "Patio Arezzo", "Almacen Reserva", "PP", "Web", "central_x_Mayor", "", "Redes"  };

    public Integer textNormal = 10;

    public Integer textInter = 15;
    public Integer textBigger = 20;

    public Integer idAlmacen = -1;
    public Integer idTalla = -1;
    public Integer idColor = -1;
    public static Integer confAlmacen = -1;

    public static Integer bitSuc = 8311799;
    public static Integer bitTam = 8497109;
    public static Integer bitCol = 67111108;

    EditText textCodeBar;
    ImageView imageView;
    Button buttonCamera;
    Boolean switchCamera = true;
    LinearLayout textViewDetails;

    ListView simpleListView;
    TextView textViewSucursal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_principal);
        //if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
        if(isLandscape(this))
        {
            //Toast.makeText(this,"Screen switched to Landscape mode",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_principal);
        }
        else
        {
            //Toast.makeText(this,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_principal_portrait);

            toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            surfaceView = findViewById(R.id.surface_view);
            surfaceView.setVisibility(View.GONE);
            //initialiseDetectorsAndSources();
            textViewDetails = findViewById(R.id.textViewDetails);

            buttonCamera = (Button)findViewById(R.id.buttonCamera);
            buttonCamera.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // click handling code
                    if(switchCamera){
                        switchCamera = false;
                        imageView.setVisibility(View.GONE);
                        textViewDetails.setVisibility(View.GONE);
                        surfaceView.setVisibility(View.VISIBLE);
                        initialiseDetectorsAndSources();
                    }else{
                        switchCamera = true;
                        cameraSource.stop();
                        //cameraSource.release();
                        surfaceView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textViewDetails.setVisibility(View.VISIBLE);
                    }

                }
            });

        }

        createFile(this);
        String data = readFile(this);
        //Log.i("DATA", data);
        Sucursal s = new Sucursal(0, "Central");
        if(data.length() == 0) {
            s = new Sucursal(0, "Central");
            data = writeFile(this,s);
            //Log.i("DATA", data);

        }else{
            //s = ActivityConfig.sucursals.get(10);
            int idSuc = Integer.parseInt(data.trim());
            s = new Sucursal(idSuc, sucursales[idSuc]);
        }
        //Log.i("DATA",""+ Integer.parseInt(data.trim()));
        confAlmacen = Integer.parseInt(data.trim());
        this.idAlmacen = confAlmacen;

        textViewSucursal = (TextView) findViewById(R.id.textViewSucursal);
        textViewSucursal.setText(textViewSucursal.getText()+" "+s.getName().toUpperCase());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(isTablet(this)){
            textNormal=10;
            textInter=15;
            textBigger=20;
        }else{
            textNormal=8;
            textInter=12;
            textBigger=10;
        }

        imageView = (ImageView) findViewById(R.id.imageViewPicture);
        textCodeBar = (EditText)findViewById(R.id.editTextCode);

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

                    if(idColor == -1){
                        idAlmacen = confAlmacen;
                        idTalla = -1;
                    }

                    long uniqueNum =  (bitSuc+idAlmacen)+(bitTam+idTalla)+(bitCol+idColor); //UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

                    server_url_product = server_url + "?id=" + uniqueNum + "&code_bar=" + codebar + "&idAlm=" + idAlmacen + "&idTam="+ idTalla + "&idCol=" + idColor;
                    Log.i("URL1", server_url_product);
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

                                //Log.d("afoImagen value ", obj.getString("afoImagen"));

                                /*viewPager.setCurrentItem(0, true);
                                sectionsPagerAdapter.getItem(0).home_title.setText(app.getCurrentPodcast().getTitle());*/

                                /*EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
                                etTextInput.setTextColor(Color.WHITE);
                                etTextInput.setHintTextColor(Color.WHITE);
                                etTextInput.setTextSize(12.5f);*/
                                String url_image = obj.getString("image_src");
                                String href = "";
                                String image = "REFERENCIAL";
                                if(!TextUtils.isEmpty(url_image) && !url_image.equals("null")){
                                    href = "href='"+url_image+"'";
                                    image = "SHOPIFY";
                                }

                                String caaPrecio = obj.getString("caaPrecio");
                                String caaPrecioOferta = obj.getString("caaPrecioOferta");
                                float precio = 0;
                                float precioOferta = 0;
                                float tipoCambio = Float.parseFloat(obj.getString("tcavalor"));

                                if(!TextUtils.isEmpty(caaPrecio) && !caaPrecio.equals("null"))
                                    precio = Float.parseFloat(caaPrecio);
                                if(!TextUtils.isEmpty(caaPrecioOferta) && !caaPrecioOferta.equals("null"))
                                    precioOferta = Float.parseFloat(caaPrecioOferta);

                                int catId = Integer.parseInt(obj.getString("catId"));
                                int camIdExcep = Integer.parseInt(obj.getString("camIdExcep"));

                                String basedatos = obj.getString("basedatos");
                                String database = "";
                                if(basedatos.equals("LOCAL")) {
                                    database = "<font size='50' color=#FF0000><b>" + String.format("%.0f", precio * tipoCambio) + "</b> Bs.</font><br>";
                                }else if(basedatos.equals("PRODUCCION")) {
                                    if (precioOferta > 0) {
                                        database = "<font size='50'><b><strike>" + String.format("%.0f", precio * tipoCambio) + "</b> Bs.</strike> <b>" + String.format("%.0f", precioOferta * tipoCambio) + "</b> Bs.</font><br>";
                                    }else /*if (catId == 1 && camIdExcep == 0) {
                                        float precio100 = precio * tipoCambio;
                                        float porc30 = ((precio100*70)/100);
                                        float porc40 = ((precio100*60)/100);
                                        float porc50 = ((precio100*50)/100);
                                        database = "<b><strike>" + String.format("%.0f", precio * tipoCambio) + " Bs.</strike></b> <br><b><font color=#444444>(-30%):</font> <font color=#FF0000>" + String.format("%.0f", porc30) + " Bs.</font></b> <br><b><font color=#444444>(-40%):</font> <font color=#FF0000>" + String.format("%.0f", porc40) + " Bs.</font></b> <br><b><font color=#444444>(-50%):</font> <font color=#FF0000>" + String.format("%.0f", porc50) + " Bs.</font></b></font><br>";
                                    }else*/
                                    {
                                        database = "<font size='50'><b>" + String.format("%.0f", precio * tipoCambio) + "</b> Bs.</font><br>";
                                    }
                                }
                                String text_1 =
                                        "<table><tbody>" +
                                        "<tr><td><b><font color=#444444>PRECIO:</b> <b>"+database+"</b></font></td></tr><br>" +
                                        "<tr><td><b><font color=#444444>ALMACEN:</font></b> "+obj.getString("almNombre").toUpperCase()+"</td></tr><br>" +
                                        "<tr><td><b><font color=#444444>PROOVEEDOR:</font></b> "+obj.getString("pveNombre").toUpperCase()+"</td></tr><br>" +
                                        //"<tr><td><b><font color=#444444>CATEGORIA:</font></b> <font color=#0173B7>"+obj.getString("catNombre").toUpperCase()+"</font></td></tr><br>" +
                                        "<tr><td><b><font color=#444444>MATERIAL F.:</font></b> "+obj.getString("mafNombre").toUpperCase()+"</td></tr><br>" +
                                        //"<tr><td><b><font color=#444444>COLOR F.:</font></b> "+obj.getString("cofNombre").toUpperCase()+"</td></tr><br>" +


                                        //"<tr><td><strong>Codigo:</strong> <a target='_blank' "+href+" >"+obj.getString("lotCodigoBarras")+"</a></td></tr><br>"+
                                        //"<tr><td><strong>Shopify:</strong> <a target='_blank' href='"+obj.getString("image_src")+"'>ver mas...</a></td></tr><br>"+
                                        "</tbody></table>";

                                //txt_home_1.setText(Html.fromHtml(text_1)); view_pager
                                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_Layout_Details); //inflater.inflate(R.layout.fragment_main, container, false);
                                TextView textView1 = ((TextView) linearLayout. findViewById(R.id.textViewDetails1));
                                //Toast.makeText(getApplicationContext(),"Response :" + textView1.getId(), Toast.LENGTH_LONG).show();
                                textView1.setTextSize(textInter);
                                textView1.setText(Html.fromHtml(text_1));
                                textView1.setTag(url_image);



                                String text_2 = "<table><tbody>" +
                                        "<tr><td><b><font color=#444444>TALLA:</font></b> "+obj.getString("tamNombre").toUpperCase()+"</td></tr><br>" +
                                        "<tr><td><b><font color=#444444>MODELO:</font></b> "+obj.getString("modNombre").toUpperCase()+"</td></tr><br>" +
                                        //"<tr><td><b><font color=#444444>SUBCATEGORIA:</font></b> <font color=#0173B7>"+obj.getString("subNombre").toUpperCase()+"</font></td></tr><br>" +
                                        //"<tr><td><b><font color=#444444>MATERIAL:</font></b> <font color=#0173B7>"+obj.getString("matNombre").toUpperCase()+"</font></td></tr><br>" +
                                        "<tr><td><b><font color=#444444>COLOR:</font></b> "+obj.getString("colNombre").toUpperCase()+"</td></tr><br>" +
                                        //"<tr><td><b><font color=#444444>STOCK:</font></b> "+obj.getString("stock")+" <b><font color=#444444>VENDIDOS:</font></b> <font color=#FF0000>"+obj.getString("cantVenta")+"</font></td></tr><br>" +
                                        "<tr><td><b><font color=#444444>IMAGEN:</font></b> <a target='_blank' "+href+" >"+image+"</a></td></tr><br>"+
                                        "<tr><td><b><font color=#444444>REFERENCIA:</font></b> "+obj.getString("artNombre")+"</td></tr><br>" +
                                        "</tbody></table>";
                                        //"<h1><strong>Disponibles</strong></h1>";
                                //"<div><strong>Temporada:</strong> "+obj.getString("temNombre")+"</div>"+
                                //"<div><strong>Gestion:</strong> "+obj.getString("gesNombre")+"</div>"+

                                ;

                                String dispo = "";
                                //JSONObject jsonObj = new JSONObject(obj.getString("disponibles"));
                                //JSONObject first = new JSONArray(obj.getString("disponibles")).getJSONObject(0);
                                JSONArray jsonArray = new JSONArray(obj.getString("disponibles"));

                                List<Shoes> listShoes= new ArrayList<Shoes>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    int idalm=Integer.parseInt(jsonObject.getString("almId"));
                                    int idcol=Integer.parseInt(jsonObject.getString("colId"));
                                    int idtam=Integer.parseInt(jsonObject.getString("tamId"));

                                    String alm=jsonObject.getString("almNombre");
                                    String col=jsonObject.getString("colNombre");
                                    String tam=jsonObject.getString("tamNombre");
                                    //dispo = dispo + "<div><strong>"+alm+":</strong> "+col+" "+tam+"</div>";
                                    String codigo=jsonObject.getString("lotCodigoBarras");
                                    //Log.i("JsonArray",alm+"-"+tam+"-"+col);
                                    Shoes shoes = new Shoes(idalm,alm,idtam,tam,idcol,col,codigo);
                                    listShoes.add(shoes);
                                }

                                //Log.i("first", first.toString());
                                //text_2 = text_2 + dispo;
                                //txt_home_2.setText(Html.fromHtml(text_2));
                                //ViewPager rootView2 = (ViewPager)findViewById(R.id.view_pager2); //inflater.inflate(R.layout.fragment_main, container, false);
                                TextView textView2 = ((TextView) linearLayout. findViewById(R.id.textViewDetails2));
                                //Toast.makeText(getApplicationContext(),"Response :" + textView2.getId(), Toast.LENGTH_LONG).show();
                                textView2.setTextSize(textInter);
                                textView2.setText(Html.fromHtml(text_2));



                                imageView.setImageResource(R.drawable.ic_menu_camera);
                                //new ImageLoadTask("https://colibri2.com/sigece/16.gif", imageView).execute();
                                // Adding the gif here using glide library
                                //Glide.with(this).load(R.drawable.android).into(imageView);

                                if(!TextUtils.isEmpty(url_image) && !url_image.equals("null")) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isDestroyed())
                                                return;
                                            new ImageLoadTask(url_image, imageView).execute();
                                        }
                                    },100);

                                    //Log.i("SRC",url_image);
                                }else{
                                    String encodedImage = obj.getString("afoImagen");
                                    if(!TextUtils.isEmpty(encodedImage) && !encodedImage.equals("null")){
                                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        imageView.setImageBitmap(decodedByte);
                                    }
                                }

                                deleteButtons();
                                createButtons(listShoes, textCodeBar);

                                //HABILITAR SI SOLO QUIEREN VER EL DE SU SUCURSAL
                                int almId = Integer.parseInt(obj.getString("almId"));
                                LinearLayout ls = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal);
                                Button button = (Button) ls.findViewById(bitSuc + almId);
                                if(button != null)
                                    button.callOnClick();
                                else
                                    Log.i("SUCURSAL", "Button not exist" );

                            } catch (Throwable tx) {
                                disabledQuerty(textCodeBar);
                                Toast.makeText(getApplicationContext(),"Error de Conversión JSON" , Toast.LENGTH_LONG).show();
                                Log.e("My App", "Could not parse malformed JSON: " + json );
                            }

                            requestQueue.stop();
                            disabledQuerty(textCodeBar);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            disabledQuerty(textCodeBar);
                            Toast.makeText(getApplicationContext(),"Error de Conexión 404" , Toast.LENGTH_LONG).show();
                            Log.i(TAG,"Error :" + error.toString());
                        }
                    });

                    requestQueue.add(stringRequest);
                    //disabledQuerty(textCodeBar);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView txt= (TextView) findViewById(R.id.textViewSucursal); //txt is object of TextView
        //txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                String url_tag = v.getTag().toString();
                browserIntent.setData(Uri.parse(url_tag));
                startActivity(browserIntent);
                //disabledQuerty(textCodeBar);*/
                Intent openSucursal = new Intent(getApplicationContext(),ActivityConfig.class);
                startActivity(openSucursal);

                finish();
            }
        });

        //if(isLandscape(this))
            //textCodeBar.setText("294841i24r611");


        /*listViewConfig=(ListView)findViewById(R.id.listViewConfig);
        textViewConfig=(TextView)findViewById(R.id.itemTextView);
        listItemConfig = getResources().getStringArray(R.array.array_sucursal);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_view, R.id.itemTextView,  listItemConfig);
        listViewConfig.setAdapter(adapter);*/

        // array objects
        /*String courseList[] = {"C-Programming", "Data Structure", "Database", "Python",
                "Java", "Operating System", "Compiler Design", "Android Development"};

        simpleListView = (ListView) findViewById(R.id.simpleListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_view, 0, courseList);
        //simpleListView.setAdapter(arrayAdapter);

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=arrayAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

            }
        });*/

        textCodeBar.setText("294672I24R611");
        //textCodeBar.setText("183940I24R607");

    }

    public void updateSucursal(String name){
        textViewSucursal = (TextView) findViewById(R.id.textViewSucursal);
        textViewSucursal.setText(textViewSucursal.getText()+" "+name.toUpperCase());
    }

    public void reloadColor(Shoes shoes, EditText textCode){
        this.idAlmacen = shoes.getIdAlmacen();
        this.idTalla = shoes.getIdTalla();
        this.idColor = shoes.getIdColor();

        LinearLayout ls = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal);
        Button buttonAlm = (Button)ls.findViewById(bitSuc + shoes.getIdAlmacen()); //ls.findViewWithTag("Sucursal_"+shoes.getIdAlmacen());
        buttonAlm.setBackground(button_Active());
        LinearLayout lt = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla);
        Button buttonTalla = (Button)lt.findViewById(bitTam + shoes.getIdTalla()); //lt.findViewWithTag("Talla_"+shoes.getIdTalla());
        buttonTalla.setBackground(button_Active());
        textCode.setText(shoes.getLotCodigoBarras());

        //String data = readFile(this);
        this.idAlmacen = this.confAlmacen; //Integer.parseInt(data.trim());
        this.idTalla = -1;
        this.idColor = -1;
    }

    public GradientDrawable button_Normal(){
        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(0xFFCCCCCC); // Changes this drawbale to use a single color instead of a gradient
        gd1.setCornerRadius(10);
        gd1.setStroke(1, 0xFF888888);
        return gd1;
    }

    public GradientDrawable button_Active(){
        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(0xFF888888); // Changes this drawbale to use a single color instead of a gradient
        gd1.setCornerRadius(10);
        gd1.setStroke(1, 0xFF888888);
        return gd1;
    }

    public GradientDrawable button_DisActive(){
        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(0xFFEEEEEE); // Changes this drawbale to use a single color instead of a gradient
        gd1.setCornerRadius(10);
        gd1.setStroke(0, 0xFFFFFFFF);
        return gd1;
    }

    public GradientDrawable button_Color(String color){
        GradientDrawable gd1 = new GradientDrawable();
        if(color.equals("#ANIMAL") || color.equals("#ESTAMPADO") || color.equals("#MULTTICOLOR"))
            gd1.setColor(ColorStateList.valueOf(Color.TRANSPARENT));
        else
            gd1.setColor(Color.parseColor(color)); // Changes this drawbale to use a single color instead of a gradient
        gd1.setCornerRadius(10);
        gd1.setStroke(1, 0xFF888888);
        return gd1;
    }

    public void disabledQuerty(EditText textCode){
        textCode.setText("");
        textCode.setEnabled(true);
        textCode.setFocusable(true);
        textCode.setFocusableInTouchMode(true);

        textCode.requestFocus();
        if(textCode.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void createButtons(List<Shoes> listShoes, EditText textCode){

        //Log.i("ListSize", "" + listShoes.size());
        //LinearLayout ls = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal);
        LinearLayout ls1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_1);
        LinearLayout ls2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_2);
        LinearLayout ls3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_3);
        LinearLayout lt1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_1);
        LinearLayout lt2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_2);
        LinearLayout lt3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_3);
        LinearLayout lc = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color);
        LinearLayout lc1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_1);
        LinearLayout lc2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_2);
        LinearLayout lc3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_3);

        Typeface typeface = ResourcesCompat.getFont(this.getBaseContext(), R.font.made_tommy_light);

        ArrayList<Integer> listS = new ArrayList<Integer>();
        ArrayList<Integer> listT = new ArrayList<Integer>();
        ArrayList<Integer> listC = new ArrayList<Integer>();
        for (int i = 0; i < listShoes.size(); i++) {
            //Button button = (Button)findViewById (i);
            Shoes shoes = listShoes.get(i);

            if(!listS.contains(shoes.getIdAlmacen())) {
                listS.add(shoes.getIdAlmacen());
                Log.i("ButtonSucursal", "" + shoes.getIdAlmacen());

                //Button buttonS = new MaterialButton(this, null, R.attr.myButtonStyle);
                Button buttonS = new Button(this);
                buttonS.setId(bitSuc + shoes.getIdAlmacen());
                buttonS.setTag(shoes);
                buttonS.setText(shoes.getAlmacen());
                //buttonS.setWidth(80);
                //buttonS.setHeight(20);
                buttonS.setTextSize(textNormal);
                buttonS.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                buttonS.setPadding(10,10,10,10);

                buttonS.setBackground(button_Normal());

                buttonS.setTypeface(typeface);

                //buttonS.setBackgroundDrawable(R.drawable.button_border);

                //buttonS.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));

                //buttonS.setX(20);
                //buttonS.setY(20);
                //buttonS.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,     LinearLayout.LayoutParams.WRAP_CONTENT));
                //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                //params.width = 100;
                params.height = 50;
                //params.leftMargin = (listS.size()-1) * 115;

                params.setMargins(10,0,10,0);
                buttonS.setLayoutParams(params);
                buttonS.setGravity(Gravity.CENTER);

                if(listS.size()<=5)
                    ls1.addView(buttonS, params);
                    //Log.i("Sucursal_1", "" + buttonS);
                else if(listS.size()>5 && listS.size()<=10)
                    ls2.addView(buttonS, params);
                else
                    ls3.addView(buttonS, params);
                    //Log.i("Sucursal_2", "" + buttonS);

                buttonS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivityTabs.this,v.toString() ,Toast.LENGTH_SHORT).show();
                        //Log.i("Sucursal", "" + v.getId() +"-"+ v.getTag());
                        //sucursalEnable = v.getTag().toString();
                        Shoes shoes = (Shoes)v.getTag();
                        idAlmacen = shoes.getIdAlmacen();
                                //v.setBackgroundDrawable(getResources().getDrawable(R.drawable.whatever));
                        enabledButtons(listShoes,idAlmacen,-1,-1);
                    }
                });
            }

            if(!listT.contains(shoes.getIdTalla())) {
                listT.add(shoes.getIdTalla());
                Log.i("ButtonTalla", "" + shoes.getIdTalla());

                Button buttonT = new Button(this);
                buttonT.setId(bitTam + shoes.getIdTalla());
                buttonT.setTag(shoes);
                buttonT.setText(shoes.getTalla());
                //buttonT.setWidth(30);
                //buttonT.setHeight(20);
                buttonT.setTextSize(textInter);
                buttonT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                buttonT.setPadding(10,10,10,10);

                buttonT.setBackground(button_Normal());

                buttonT.setTypeface(typeface);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                params.height = 50;
                params.setMargins(10,0,10,0);
                buttonT.setLayoutParams(params);
                buttonT.setGravity(Gravity.CENTER);
                buttonT.setIncludeFontPadding(false);
                //lt.addView(buttonT, params);
                if(listT.size()<=5)
                    lt1.addView(buttonT, params);
                else if(listT.size()>5 && listT.size()<=10)
                    lt2.addView(buttonT, params);
                else
                    lt3.addView(buttonT, params);

                buttonT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivityTabs.this,v.toString() ,Toast.LENGTH_SHORT).show();
                        //Log.i("Talla", "" + v.getId() +"-"+ v.getTag());
                        Shoes shoes = (Shoes)v.getTag();
                        idTalla = shoes.getIdTalla();
                        enabledButtons(listShoes,idAlmacen,idTalla,-1);
                    }
                });
            }

            if(!listC.contains(shoes.getIdColor())) {
                listC.add(shoes.getIdColor());
                Log.i("ButtonColor", "" + shoes.getIdColor());

                Button buttonC = new Button(this);
                buttonC.setId(bitCol + shoes.getIdColor());
                buttonC.setTag(shoes);
                buttonC.setText(shoes.getColor());
                buttonC.setHint(shoes.getLotCodigoBarras());

                //buttonC.setWidth(50);
                //buttonC.setHeight(50);
                buttonC.setTextSize(textNormal);
                buttonC.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                buttonC.setPadding(10,10,10,10);

                //Log.i("Color",shoes.getColor());
                int index = Arrays.asList(color).indexOf(shoes.getColor());
                String hexa = hexadecimal[index];
                //Log.i("Hexa",hexa);
                String textColor = "#FFFFFF";
                if(hexa.equals("#FFFFFF"))
                    textColor = "#000000";
                buttonC.setTextColor(ColorStateList.valueOf(Color.parseColor(textColor)));

                if(hexa.equals("#ANIMAL"))
                    buttonC.setBackgroundResource(R.mipmap.ic_testing_logo);
                else if(hexa.equals("#ESTAMPADO"))
                    buttonC.setBackgroundResource(R.mipmap.ic_estampado);
                else if(hexa.equals("#MULTICOLOR"))
                    buttonC.setBackgroundResource(R.mipmap.ic_multicolor);
                else
                    buttonC.setBackground(button_Color(hexa));

                buttonC.setTypeface(typeface);
                //buttonC.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexa)));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
                params.height = 50;
                params.setMargins(10,0,10,0);
                buttonC.setLayoutParams(params);
                //buttonC.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

                //lc.addView(buttonC, params);
                if(listC.size()<=5)
                    lc1.addView(buttonC, params);
                else if(listC.size()>5 && listC.size()<=10)
                    lc2.addView(buttonC, params);
                else
                    lc3.addView(buttonC, params);

                //ShapeableImageView buttonC = new ShapeableImageView(this);
                /*ImageView buttonC = new ImageView(this);
                buttonC.setTag("Color_"+shoes.getIdColor());
                //buttonC.setImageResource(R.mipmap.ic_testing_logo_round);
                buttonC.setClipToOutline(true);

                int index = Arrays.asList(color).indexOf(shoes.getColor());
                String hexa = hexadecimal[index];
                String textColor = "#FFFFFF";
                if(hexa.equals("#FFFFFF"))
                    textColor = "#000000";
                //buttonC.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexa)));
                buttonC.setBackgroundColor(Color.parseColor(hexa));
                buttonC.setPadding(5,5,5,5);
                buttonC.setScaleType(ImageView.ScaleType.CENTER_CROP);*/

                /*Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_testing_logo_background);
                Bitmap circularBitmap1 = ImageConverter.getRoundedCornerBitmap(bitmap1, 100);
                Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.side_nav_bar);
                Bitmap circularBitmap2 = ImageConverter.getRoundedCornerBitmap(bitmap2, 100);
                //buttonC.setImageBitmap(circularBitmap1);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                //params.width=50;
                //params.height=50;
                buttonC.setLayoutParams(params);

                //buttonC.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                lc.addView(buttonC, params);*/

                buttonC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Shoes shoes = (Shoes)v.getTag();
                        idColor = shoes.getIdColor();
                        Log.i("TAG", "" + shoes);
                        Button button = (Button) lc.findViewById(bitCol + shoes.getIdColor()); //lc.findViewWithTag(shoes);
                        Log.i("BUTTON", "" + button);
                        String codebar = button.getHint().toString();
                        //Toast.makeText(PrincipalActivity.this, codebar , Toast.LENGTH_SHORT).show();

                        Shoes newShoes = null;
                        Shoes shoe = new Shoes(idAlmacen, "", idTalla, "", idColor, "", "");
                        if(idAlmacen > -1 && idTalla > -1 && idColor > -1)
                            newShoes = shoes.findShoes(listShoes, shoe);
                        if(newShoes == null)
                            newShoes = shoes;

                        reloadColor(newShoes, textCode);
                        //textCode.setText(codebar);
                        Log.i("Color", "" + v.getId() +"-"+ v.getTag());
                        //enabledButtons(listShoes,"","",color.replace("Color_",""));
                    }
                });
            }
            //Log.i("List Size", "" + listC.size());
            //Log.i(TAG,"Buttons :" + i);
        }
        //createButtonsTallas(listShoes);
    }

    public void deleteButtons(){
        //Log.i("ListSize", "" + listShoes.size());
        LinearLayout ls1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_1);
        LinearLayout ls2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_2);
        LinearLayout ls3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal_3);
        LinearLayout lt1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_1);
        LinearLayout lt2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_2);
        LinearLayout lt3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla_3);
        LinearLayout lc1 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_1);
        LinearLayout lc2 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_2);
        LinearLayout lc3 = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color_3);

        ls1.removeAllViews();
        ls2.removeAllViews();
        ls3.removeAllViews();
        lt1.removeAllViews();
        lt2.removeAllViews();
        lt3.removeAllViews();
        lc1.removeAllViews();
        lc2.removeAllViews();
        lc3.removeAllViews();

        /*ArrayList<Integer> listS = new ArrayList<Integer>();
        ArrayList<Integer> listT = new ArrayList<Integer>();
        ArrayList<Integer> listC = new ArrayList<Integer>();
        for (int i = 0; i < listShoes.size(); i++) {

        }*/
    }


    public void enabledButtons(List<Shoes> listShoes, Integer idSucursal, Integer idTalla, Integer idColor){
        Log.i("Sucursal", "" + idSucursal);
        Log.i("Talla", "" + idTalla);
        LinearLayout ls = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Sucursal);
        LinearLayout lt = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Talla);
        LinearLayout lc = (LinearLayout) findViewById(R.id.linear_Layout_Buttons_Color);

        ArrayList<Integer> listS = new ArrayList<Integer>();
        for (int i = 0; i < listShoes.size(); i++) {
            Shoes shoes = listShoes.get(i);
            //String tagAlmacen = "Sucursal_" + shoes.getIdAlmacen();
            if (!listS.contains(shoes.getIdAlmacen())) {
                listS.add(shoes.getIdAlmacen());
                Button button = (Button) ls.findViewById(bitSuc + shoes.getIdAlmacen()); //ls.findViewWithTag(tagAlmacen);
                Log.i("sucursal disabled", "" + button.getTag());
                //button.setBackgroundDrawable(getResources().getDrawable(R.color.grey));
                //button.setBackgroundColor(Color.LTGRAY);
                //button.setPressed(false);

                button.setBackground(button_Normal());
                //button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
            }
        }

        if(idSucursal > -1){
            Button b = (Button) ls.findViewById(bitSuc + idSucursal); //ls.findViewWithTag(TagSucursal);
            //b.setBackgroundColor(Color.rgb(81,209,246));

            b.setBackground(button_Active());
            //b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#888888")));

            ArrayList<Integer> listT = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagTalla = "Talla_" + shoes.getIdTalla();
                if (!listT.contains(shoes.getIdTalla())) {
                    listT.add(shoes.getIdTalla());
                    Button button = (Button) lt.findViewById(bitTam + shoes.getIdTalla()); //lt.findViewWithTag(tagTalla);
                    Log.i("talla disabled", "" + button.getTag());

                    button.setBackground(button_DisActive());
                    //button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                    button.setEnabled(false);
                }
            }
            listT = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagTalla = "Talla_" + shoes.getIdTalla();
                if ((shoes.getIdAlmacen() == idSucursal) && !listT.contains(shoes.getIdTalla())) {
                    listT.add(shoes.getIdTalla());
                    Button button = (Button) lt.findViewById(bitTam + shoes.getIdTalla()); //lt.findViewWithTag(tagTalla);
                    Log.i("talla enabled", "" + button.getTag());
                    button.setBackground(button_Normal());
                    button.setEnabled(true);
                }
            }

            ArrayList<Integer> listC = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagColor = "Color_" + shoes.getIdColor();
                if (!listC.contains(shoes.getIdColor())) {
                    listC.add(shoes.getIdColor());
                    Button button = (Button) lc.findViewById(bitCol + shoes.getIdColor());
                    Log.i("color disabled", "" + button.getTag());
                    //button.setEnabled(false);
                    //button.setTextColor(button.getBackgroundTintList());
                    button.setVisibility(View.INVISIBLE);
                }
            }
            listC = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagColor = "Color_" + shoes.getIdColor();
                if ((shoes.getIdAlmacen() == idSucursal) && !listC.contains(shoes.getIdColor())) {
                    listC.add(shoes.getIdColor());
                    Button button = (Button) lc.findViewById(bitCol + shoes.getIdColor());
                    button.setHint(shoes.getLotCodigoBarras());
                    Log.i("color enabled", "" + button.getTag());
                    //button.setEnabled(true);
                    button.setVisibility(View.VISIBLE);
                }
            }
        }



        if(idTalla > -1){

            /*ArrayList<Integer> listT = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagTalla = "Talla_" + shoes.getIdTalla();
                if (!listT.contains(shoes.getIdTalla())) {
                    listT.add(shoes.getIdTalla());
                    Button button = (Button) lt.findViewById(bitTam + shoes.getIdTalla()); //lt.findViewWithTag(tagTalla);
                    Log.i("talla disabled", "" + button.getTag());
                    //button.setBackgroundDrawable(getResources().getDrawable(R.color.grey));
                    button.setBackground(button_Normal());
                    //button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                    //button.setEnabled(false);
                }
            }*/

            Button b = (Button) lt.findViewById(bitTam + idTalla); //lt.findViewWithTag(TagTalla);
            b.setBackground(button_Active());
            b.setEnabled(true);
            //b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#888888")));

            ArrayList<Integer> listC = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagColor = "Color_" + shoes.getIdColor();
                if (!listC.contains(shoes.getIdColor())) {
                    listC.add(shoes.getIdColor());
                    Button button = (Button) lc.findViewById(bitCol + shoes.getIdColor());
                    Log.i("color disabled", "" + button.getTag());
                    //button.setEnabled(false);
                    //button.setTextColor(button.getBackgroundTintList());
                    button.setVisibility(View.INVISIBLE);
                }
            }
            listC = new ArrayList<Integer>();
            for (int i = 0; i < listShoes.size(); i++) {
                Shoes shoes = listShoes.get(i);
                //String tagColor = "Color_" + shoes.getIdColor();
                if ((shoes.getIdAlmacen() == idSucursal) && (shoes.getIdTalla() == idTalla)  && !listC.contains(shoes.getIdColor())) {
                    listC.add(shoes.getIdColor());
                    Button button = (Button) lc.findViewById(bitCol + shoes.getIdColor());
                    button.setHint(shoes.getLotCodigoBarras());
                    Log.i("color enabled", "" + button.getTag());
                    //button.setEnabled(true);
                    button.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static boolean isTablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(Context ctx){
        return (ctx.getResources().getDisplayMetrics().widthPixels > ctx.getResources().getDisplayMetrics().heightPixels);
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(PrincipalActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());

                        /*Camera mCam = Camera.open();
                        Camera.Parameters p = mCam.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCam.setParameters(p);
                        SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
                        try {
                            mCam.setPreviewTexture(mPreviewTexture);
                        } catch (IOException ex) {
                            // Ignore
                        }
                        mCam.startPreview();*/

                    } else {
                        ActivityCompat.requestPermissions(PrincipalActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    textCodeBar.post(new Runnable() {

                        @Override
                        public void run() {

                            /*if (barcodes.valueAt(0).email != null) {
                                //barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                //barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                                textCodeBar.setText("");
                                textCodeBar.setText(barcodeData);
                                surfaceView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                textViewDetails.setVisibility(View.VISIBLE);
                            } else {*/
                            if (barcodes.valueAt(0).displayValue != null) {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                //barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                                textCodeBar.setText("");
                                textCodeBar.setText(barcodeData);
                                surfaceView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                textViewDetails.setVisibility(View.VISIBLE);
                                switchCamera = true;
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        //cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initialiseDetectorsAndSources();
    }


    private void createFile(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);

        } else {
            Log.i("Mensaje", "Se tiene permiso para leer y escribir!");

            String direccion = context.getExternalFilesDir(null) + "/config.txt";
            File fich = new File(direccion);

            FileOutputStream fileOutputStream = null;
            if(!fich.exists()){
                try {
                    fich.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "Error statusFile " + e.getMessage());
                }
            }
        }

    }

    public static String readFile(Context context){
        String direccion = context.getExternalFilesDir(null) + "/config.txt";
        StringBuilder text = new StringBuilder();

        try {
            File file = new File(direccion);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                //text.append('\n');
            }

            br.close();
        } catch (IOException e) {
            Log.e(TAG, "Error readFile " + e.getMessage());
        }

        return text.toString();
    }

    public static String writeFile(Context context, Sucursal s){

        String direccion = context.getExternalFilesDir(null).toString();
        String fileName = "config.txt";
        String linea = s.getId()+"";
        try {
            FileWriter out = new FileWriter(new File(direccion, fileName));
            out.write(linea);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Error writeFile " + e.getMessage());
        }
        //TextView txt= (TextView) findViewById(R.id.textViewSucursal);
        //textViewSucursal.setText("SUCURSAL: "+s.getName());
        confAlmacen = s.getId();
        //PrincipalActivity p = null;
        //p.updateSucursal(s.getName());
        return linea;
    }


    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //LinearLayout lo = (LinearLayout) findViewById(R.id.linear_Layout_One);
        LinearLayout lp = (LinearLayout) findViewById(R.id.linear_Layout_Principal);
        LinearLayout ld = (LinearLayout) findViewById(R.id.linear_Layout_Details);
        LinearLayout la = (LinearLayout) findViewById(R.id.linear_Layout_Available);
            // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //change your desigen by java code
            Log.i("ORIENTATION_LANDSCAPE", "LANDSCAPE" );
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            Toast.makeText(PrincipalActivity.this, "ORIENTATION_LANDSCAPE" ,Toast.LENGTH_SHORT).show();

            lp.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams paramsd = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.2f);
            paramsd.gravity = Gravity.LEFT;
            paramsd.weight = 0.2f;
            ld.setLayoutParams(paramsd);
            ld.requestLayout();
            LinearLayout.LayoutParams paramsa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f);
            paramsa.gravity = Gravity.RIGHT;
            paramsa.weight = 0.8f;
            la.setLayoutParams(paramsa);
            la.requestLayout();
            lp.requestLayout();
            //lo.addView(la);
            //lo.addView(lp);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //change your desigen by java code
            Log.i("ORIENTATION_PORTRAIT", "PORTRAIT" );
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Toast.makeText(PrincipalActivity.this, "ORIENTATION_PORTRAIT" ,Toast.LENGTH_SHORT).show();

            lp.setOrientation(LinearLayout.VERTICAL);
            lp.setWeightSum(2f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
            params.gravity = Gravity.CENTER;
            params.weight=1f;
            ld.setLayoutParams(params);
            la.setLayoutParams(params);

            lp.requestLayout();
            ld.requestLayout();
            la.requestLayout();
            //lo.addView(lp);
            //lo.addView(ld);
            //lo.addView(la);
        }
    }*/

}