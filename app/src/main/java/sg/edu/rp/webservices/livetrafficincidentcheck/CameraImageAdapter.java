package sg.edu.rp.webservices.livetrafficincidentcheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CameraImageAdapter extends ArrayAdapter<CameraImage> {
    private ArrayList<CameraImage> alCameraImage;
    private Context context;
    private TextView tvCameraID;
    ImageView tvImageLink;
    int resource;

    public CameraImageAdapter(Context context, int resource, ArrayList<CameraImage> objects) {
        super(context, resource, objects);
        alCameraImage = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row2, parent, false);

        tvCameraID = rowView.findViewById(R.id.tvCameraID);
        tvImageLink = rowView.findViewById(R.id.tvImageLink);

        CameraImage selectedCameraImage = alCameraImage.get(position);

        tvCameraID.setText("Camera ID: " + selectedCameraImage.getCameraID());

        Picasso.with(context).load(selectedCameraImage.getImageLink()).into(tvImageLink);

        return rowView;
    }
}