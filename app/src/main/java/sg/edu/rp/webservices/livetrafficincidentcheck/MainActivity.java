package sg.edu.rp.webservices.livetrafficincidentcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvIncident = (ListView) findViewById(R.id.lvIncident);
        client = new AsyncHttpClient();

    }

    //refresh with latest contact data whenever this activity resumes
    @Override
    protected void onResume() {
        super.onResume();

        //TODO: call getListOfContacts.php to retrieve all contact details
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
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                aaIncident = new IncidentAdapter(getApplicationContext(), R.layout.row, alIncident);
                lvIncident.setAdapter(aaIncident);

                lvIncident.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Incident selectedContact = alIncident.get(position);
                        Intent i = new Intent(getBaseContext(), MapsActivity.class);
                        i.putExtra("type", selectedContact.getType());
                        i.putExtra("message", selectedContact.getMessage());
                        i.putExtra("latitude", selectedContact.getLatitude());
                        i.putExtra("longitude", selectedContact.getLongitude());
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
            return true;
        }
        else {

        }
        return super.onOptionsItemSelected(item);
    }
}