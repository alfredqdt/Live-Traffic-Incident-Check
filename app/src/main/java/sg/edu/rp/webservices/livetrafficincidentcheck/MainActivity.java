package sg.edu.rp.webservices.livetrafficincidentcheck;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ListView lvIncident;
    private ArrayList<Incident> alIncident;
    private ArrayAdapter<Incident> aaIncident;
    private AsyncHttpClient client;
    SearchView searchView;
    TextView tvIncidentNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvIncident = (ListView) findViewById(R.id.lvIncident);
        searchView = (SearchView) findViewById(R.id.searchView);
        tvIncidentNumbers = (TextView) findViewById(R.id.tvIncidentNumbers);
        client = new AsyncHttpClient();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Incident List</font>"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Incident> queryList = new ArrayList<>();

                for (Incident query : alIncident) {
                    if (query.getMessage().toLowerCase().contains(s.toLowerCase())) {
                        queryList.add(query);
                    }
                }
                aaIncident = new IncidentAdapter(getApplicationContext(), R.layout.row, queryList);
                lvIncident.setAdapter(aaIncident);
                aaIncident.notifyDataSetChanged();

                lvIncident.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Incident selectedIncident = queryList.get(position);
                        Intent i = new Intent(getBaseContext(), MapsActivity.class);
                        i.putExtra("type", selectedIncident.getType());
                        i.putExtra("message", selectedIncident.getMessage());
                        i.putExtra("latitude", selectedIncident.getLatitude());
                        i.putExtra("longitude", selectedIncident.getLongitude());
                        startActivity(i);
                    }
                }); //end onSuccess

                return true;
            }
        });

        alIncident = new ArrayList<Incident>();
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

                        Incident incident = new Incident(type, message, latitude, longitude);
                        alIncident.add(incident);

                        if (i >= 0) {
                            tvIncidentNumbers.setText(" Total Current Incident:  " + (i + 1) + " ");
                        } else {
                            tvIncidentNumbers.setText(" Total Current Incident:  0 ");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aaIncident = new IncidentAdapter(getApplicationContext(), R.layout.row, alIncident);
                lvIncident.setAdapter(aaIncident);
                aaIncident.notifyDataSetChanged();

                lvIncident.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Incident selectedIncident = alIncident.get(position);
                        Intent i = new Intent(getBaseContext(), MapsActivity.class);
                        i.putExtra("type", selectedIncident.getType());
                        i.putExtra("message", selectedIncident.getMessage());
                        i.putExtra("latitude", selectedIncident.getLatitude());
                        i.putExtra("longitude", selectedIncident.getLongitude());
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