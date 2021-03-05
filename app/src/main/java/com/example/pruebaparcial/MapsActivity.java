package com.example.pruebaparcial;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pruebaparcial.webservice.Asynchtask;
import com.example.pruebaparcial.webservice.WebService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Asynchtask {

    private GoogleMap mMap;

    Paises paises;

    float[] paisselect;

    ImageView imgPais;
    TextView txtPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imgPais = (ImageView) findViewById(R.id.imgFotoPais);
        txtPais = (TextView) findViewById(R.id.txtNombrePais);

        paisselect = getIntent().getExtras().getFloatArray("floats");

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("http://www.geognos.com/api/en/countries/info/all.json", datos, this, this);
        ws.execute("");

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        set_map();
    }

    @Override
    public void processFinish(String result) throws JSONException {
        JSONObject paises_data_result = new JSONObject(result);
        paises = Paises.get_Pais(paises_data_result, paisselect);
        set_map();
    }

    private void set_map() {
        if (paises != null && mMap != null) {

            double[] lstCoord = paises.getCoordenadasPais();


            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(lstCoord[4], lstCoord[5]), 5);
            mMap.moveCamera(camUpd1);

            PolylineOptions lineas = new PolylineOptions()
                    .add(new LatLng(lstCoord[2], lstCoord[0])) //NOR OESTE
                    .add(new LatLng(lstCoord[2], lstCoord[1])) //NOR ESTE
                    .add(new LatLng(lstCoord[3], lstCoord[1]))  //SUR ESTE
                    .add(new LatLng(lstCoord[3], lstCoord[0]))  // SUR OESTE
                    .add(new LatLng(lstCoord[2], lstCoord[0]));  // NOR OESTE
            lineas.width(8);
            lineas.color(Color.RED);
            mMap.addPolyline(lineas);
            txtPais.setText(paises.getNombrePais());
            Glide.with(this.getApplicationContext()).load(paises.getUrl_Pais()).into(imgPais);
        }
    }
}