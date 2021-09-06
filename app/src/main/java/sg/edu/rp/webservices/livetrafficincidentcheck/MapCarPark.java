package sg.edu.rp.webservices.livetrafficincidentcheck;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sg.edu.rp.webservices.livetrafficincidentcheck.databinding.ActivityMapCarParkBinding;

public class MapCarPark extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapCarParkBinding binding;
    String development, availableLot, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapCarParkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        development = intent.getStringExtra("development");
        availableLot = intent.getStringExtra("availableLot");
        location = intent.getStringExtra("location");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiCompass = mMap.getUiSettings();
        uiCompass.setCompassEnabled(true);

        UiSettings uiControlZoom = mMap.getUiSettings();
        uiControlZoom.setZoomControlsEnabled(true);


        String[] arrOfStr = location.split(" ");

        String strLat = "";
        String strLng = "";

        for (String a : arrOfStr)
            strLat = arrOfStr[0];
            strLng = arrOfStr[1];


        Double doubleLat = Double.parseDouble(strLat);
        Double doubleLng = Double.parseDouble(strLng);


        LatLng singapore = new LatLng(doubleLat, doubleLng);
        mMap.addMarker(new MarkerOptions().position(singapore).title(development).snippet("Available Lots: " + availableLot));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,
                15));
    }
}