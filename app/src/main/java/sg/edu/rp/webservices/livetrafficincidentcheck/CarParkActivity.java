package sg.edu.rp.webservices.livetrafficincidentcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CarParkActivity extends AppCompatActivity {
    private ListView lvCarPark;
    private ArrayList<CarPark> alCarPark;
    private ArrayAdapter<CarPark> aaCarPark;
    private AsyncHttpClient client;
    SearchView searchViewCarPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark);

        lvCarPark = (ListView) findViewById(R.id.lvCarPark);
        searchViewCarPark = (SearchView) findViewById(R.id.searchViewCarPark);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();

        searchViewCarPark.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<CarPark> queryList = new ArrayList<CarPark>();

                for (CarPark query : alCarPark) {
                    if (query.getDevelopment().toLowerCase().contains(s.toLowerCase())) {
                        queryList.add(query);
                    }
                }
                aaCarPark = new CarParkAdapter(getApplicationContext(), R.layout.row3, queryList);
                lvCarPark.setAdapter(aaCarPark);
                aaCarPark.notifyDataSetChanged();

                lvCarPark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CarPark selectedCarPark = queryList.get(position);
                        Intent i = new Intent(CarParkActivity.this, MapCarPark.class);
                        i.putExtra("development", selectedCarPark.getDevelopment());
                        i.putExtra("availableLot", selectedCarPark.getAvailableLots());
                        i.putExtra("location", selectedCarPark.getLocation());
                        startActivity(i);
                    }
                }); //end onSuccess

                return true;
            }

        });

        alCarPark = new ArrayList<CarPark>();
        client.addHeader("AccountKey", "SDWn7IqOSryTM/h6AOHDcw==");
        client.get("http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());
                    JSONArray jsonArray = response.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        String development = jsonObj.getString("Development");
                        String lotType = jsonObj.getString("LotType");
                        String location = jsonObj.getString("Location");
                        String availableLots = jsonObj.getString("AvailableLots");

                        CarPark carPark = new CarPark(development, lotType, location, availableLots);
                        alCarPark.add(carPark);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aaCarPark = new CarParkAdapter(getApplicationContext(), R.layout.row3, alCarPark);
                lvCarPark.setAdapter(aaCarPark);
                aaCarPark.notifyDataSetChanged();

                lvCarPark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CarPark selectedCarPark = alCarPark.get(position);
                        Intent i = new Intent(CarParkActivity.this, MapCarPark.class);
                        i.putExtra("development", selectedCarPark.getDevelopment());
                        i.putExtra("availableLot", selectedCarPark.getAvailableLots());
                        i.putExtra("location", selectedCarPark.getLocation());
                        startActivity(i);
                    }
                }); //end onSuccess
            }
        });
    }//end onResume

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.idViewMap) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.idViewIncident) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.idViewCarPark) {
            Intent intent = new Intent(getApplicationContext(), CarParkActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), ViewCameraImageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}