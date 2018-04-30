package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private String LOCATION_SEPARATOR = " of ";
    String primaryLocation;
    String locationOffset;
    String place;

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquake) {
        super(context, 0, earthquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);
        Date dateObject = new Date(currentEarthquake.getDate());

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.textView_magnitude);
        magnitudeTextView.setText(String.valueOf(currentEarthquake.getMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.textView_date);
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.textView_hour);
        String formattedTime = formatTime(dateObject);
        timeTextView.setText(formattedTime);

        place = currentEarthquake.getPlace();

        if (place.contains(LOCATION_SEPARATOR)) {
            String[] parts = place.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }  else {
            locationOffset = getContext().getString(R.string.near_of);
            primaryLocation = place;
        }

        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.textView_offset);
        locationOffsetTextView.setText(locationOffset);

        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.textView_place);
        primaryLocationTextView.setText(primaryLocation);

        return listItemView;

    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat =  new SimpleDateFormat("MMM dd, YYYY");
        return dateFormat.format(dateObject);
    }

    private String formatTime (Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude (double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor (double magnitude) {
        int magnitudeColor;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;

            case 2:
                magnitudeColor = R.color.magnitude2;
                break;

            case 3:
                magnitudeColor = R.color.magnitude3;
                break;

            case 4:
                magnitudeColor = R.color.magnitude4;
                break;

            case 5:
                magnitudeColor = R.color.magnitude5;
                break;

            case 6:
                magnitudeColor = R.color.magnitude6;
                break;

            case 7:
                magnitudeColor = R.color.magnitude7;
                break;

            case 8:
                magnitudeColor = R.color.magnitude8;
                break;

            case 9:
                magnitudeColor = R.color.magnitude9;
                break;

            default:
                magnitudeColor = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColor);
    }


}
