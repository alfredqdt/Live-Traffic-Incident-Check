package sg.edu.rp.webservices.livetrafficincidentcheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CarParkAdapter extends ArrayAdapter<CarPark> {
    private ArrayList<CarPark> alCarPark;
    private Context context;
    private TextView tvDevelopment, tvAvailableLots;
    int resource;

    public CarParkAdapter(Context context, int resource, ArrayList<CarPark> objects){
        super(context, resource, objects);
        alCarPark = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row3, parent, false);

        tvDevelopment = rowView.findViewById(R.id.tvDevelopment);
        tvAvailableLots = rowView.findViewById(R.id.tvAvailableLots);

        CarPark currentCarPark = alCarPark.get(position);

        tvDevelopment.setText(currentCarPark.getDevelopment());
        tvAvailableLots.setText("Available Lots: " + currentCarPark.getAvailableLots());

        return rowView;
    }
}
