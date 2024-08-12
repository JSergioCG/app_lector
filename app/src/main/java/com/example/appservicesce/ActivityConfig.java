package com.example.appservicesce;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivityConfig extends AppCompatActivity {

    ListView simpleListView;

    Files files;

    public static ArrayList<Sucursal> sucursals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_list_item), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setTitle("Cambiar a Sucursal:");

        //ArrayList<Sucursal> sucursals = new ArrayList<>();
        sucursals.add(new Sucursal(0, "Central"));
        sucursals.add(new Sucursal(1, "America"));
        sucursals.add(new Sucursal(2, "Bolivar"));
        sucursals.add(new Sucursal(3, "La Paz"));
        sucursals.add(new Sucursal(4, "Cochabamba"));
        sucursals.add(new Sucursal(5, "velasco"));
        sucursals.add(new Sucursal(6, "MegaCenter"));
        sucursals.add(new Sucursal(7, "Transitorio"));
        sucursals.add(new Sucursal(8, "Fallas"));
        sucursals.add(new Sucursal(9, "outlet"));
        sucursals.add(new Sucursal(10, "San Martin"));
        sucursals.add(new Sucursal(11, "Distribuidor"));
        sucursals.add(new Sucursal(12, "Almacen Saldos"));
        sucursals.add(new Sucursal(13, "Comercial"));
        sucursals.add(new Sucursal(14, "Ventura Mall"));
        sucursals.add(new Sucursal(15, "Ventura Arezzo"));
        sucursals.add(new Sucursal(16, "Las Brisas"));
        sucursals.add(new Sucursal(17, "Insumos"));
        sucursals.add(new Sucursal(18, "Patio Design"));
        sucursals.add(new Sucursal(19, "Patio Arezzo"));
        sucursals.add(new Sucursal(20, "Almacen Reserva"));
        sucursals.add(new Sucursal(21, "PP"));
        sucursals.add(new Sucursal(22, "Web"));
        sucursals.add(new Sucursal(23, "central_x_Mayor"));
        sucursals.add(new Sucursal(25, "Redes"));

        simpleListView = (ListView) findViewById(R.id.simpleListView);

        ArrayAdapter<Sucursal> arrayAdapter = new ArrayAdapter<Sucursal>(this,
                R.layout.item_view, R.id.itemTextView, sucursals);
        simpleListView.setAdapter(arrayAdapter);

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                Sucursal suc = (Sucursal)arrayAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),suc.name,Toast.LENGTH_SHORT).show();
                //files.writeFiles(this, suc);
                PrincipalActivity.writeFile(ActivityConfig.this, suc);

                /*LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linear_Layout_Available);
                LayoutInflater inflater = getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.activity_principal, mainLayout, false);

                TextView textViewSuc = (TextView) myLayout.findViewById(R.id.textViewSucursal);
                textViewSuc.setText("SUCURSAL: "+suc.getName());*/

                Intent refresh = new Intent(getApplicationContext(),PrincipalActivity.class);
                startActivity(refresh);

                finish();
            }
        });
    }
}