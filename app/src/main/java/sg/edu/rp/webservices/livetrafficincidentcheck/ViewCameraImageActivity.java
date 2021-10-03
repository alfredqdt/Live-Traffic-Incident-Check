package sg.edu.rp.webservices.livetrafficincidentcheck;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
//Hello World
public class ViewCameraImageActivity extends AppCompatActivity {
    private ListView lvCameraImage;
    private ArrayList<CameraImage> alCameraImage;
    private ArrayAdapter<CameraImage> aaCameraImage;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camera_image);

        lvCameraImage = (ListView) findViewById(R.id.lvCameraImage);
        client = new AsyncHttpClient();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Expressway Images</font>"));

    }

    //refresh with latest contact data whenever this activity resumes
    @Override
    protected void onResume() {
        super.onResume();

        //TODO: call getListOfContacts.php to retrieve all contact details
        alCameraImage = new ArrayList<CameraImage>();
        client.addHeader("AccountKey", "SDWn7IqOSryTM/h6AOHDcw==");
        client.get("http://datamall2.mytransport.sg/ltaodataservice/Traffic-Imagesv2", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());
                    JSONArray jsonArray = response.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        String cameraID = jsonObj.getString("CameraID");
                        double latitude = jsonObj.getDouble("Latitude");
                        double longitude = jsonObj.getDouble("Longitude");
                        String imageLink = jsonObj.getString("ImageLink");

                        CameraImage cameraImage = new CameraImage(cameraID, imageLink, latitude, longitude);
                        alCameraImage.add(cameraImage);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aaCameraImage = new CameraImageAdapter(getApplicationContext(), R.layout.row2, alCameraImage);
                lvCameraImage.setAdapter(aaCameraImage);
                aaCameraImage.notifyDataSetChanged();

                lvCameraImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CameraImage selectedImage = alCameraImage.get(position);
                        Intent i = new Intent(getBaseContext(), MapViewImage.class);
                        i.putExtra("cameraID", selectedImage.getCameraID());
                        i.putExtra("imageLink", selectedImage.getImageLink());
                        i.putExtra("latitude", selectedImage.getLatitude());
                        i.putExtra("longitude", selectedImage.getLongitude());
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