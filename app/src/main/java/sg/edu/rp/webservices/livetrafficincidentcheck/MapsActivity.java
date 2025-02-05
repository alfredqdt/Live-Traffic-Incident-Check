package sg.edu.rp.webservices.livetrafficincidentcheck;


import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import sg.edu.rp.webservices.livetrafficincidentcheck.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private AsyncHttpClient client;
    String type, message;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        message = intent.getStringExtra("message");
        lat = intent.getDoubleExtra("latitude", 0.0);
        lng = intent.getDoubleExtra("longitude", 0.0);

        client = new AsyncHttpClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (lat != 0.0 && lng != 0.0) {

            UiSettings uiCompass = mMap.getUiSettings();
            uiCompass.setCompassEnabled(true);

            UiSettings uiControlZoom = mMap.getUiSettings();
            uiControlZoom.setZoomControlsEnabled(true);

            LatLng singapore = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(singapore).title(type));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,
                    15));
        } else {
            client.addHeader("AccountKey", "SDWn7IqOSryTM/h6AOHDcw==");
            client.get("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.i("JSON Results: ", response.toString());
                        JSONArray jsonArray = response.getJSONArray("value");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String type = jsonObj.getString("Type");
                            double latitude = jsonObj.getDouble("Latitude");
                            double longitude = jsonObj.getDouble("Longitude");
                            String message = jsonObj.getString("Message");

                            UiSettings uiCompass = mMap.getUiSettings();
                            uiCompass.setCompassEnabled(true);

                            UiSettings uiControlZoom = mMap.getUiSettings();
                            uiControlZoom.setZoomControlsEnabled(true);

                            LatLng singapore = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(singapore).title(type).snippet(message));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,
                                    11));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}