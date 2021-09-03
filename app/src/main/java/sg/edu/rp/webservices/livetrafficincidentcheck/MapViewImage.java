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

import sg.edu.rp.webservices.livetrafficincidentcheck.databinding.ActivityMapViewImageBinding;

public class MapViewImage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapViewImageBinding binding;
    String cameraID, imageLink;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        cameraID = intent.getStringExtra("cameraID");
        imageLink = intent.getStringExtra("imageLink");
        lat = intent.getDoubleExtra("latitude", 0.0);
        lng = intent.getDoubleExtra("longitude", 0.0);

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

        LatLng singapore = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(singapore).title(cameraID).snippet(lat + ", " + lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,
                15));
    }
}