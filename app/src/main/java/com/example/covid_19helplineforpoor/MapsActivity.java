package com.example.covid_19helplineforpoor;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {



    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng address = new LatLng(28.632429, 77.218788); //default=delhi
        Intent intent = getIntent();
        if (intent.getStringExtra("latitude") != null && intent.getStringExtra("longitude") != null) {
            address=new LatLng(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));
            float zoomLevel = (float) 15.0;
            mMap.addMarker(new MarkerOptions().position(address).title("Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, zoomLevel));
        } else {
            String strAddress = intent.getExtras().getString("address values");
            if (strAddress.length()>0) {
                address = getLocationFromAddress(this, strAddress);
            }
            float zoomLevel = (float) 15.0;
            mMap.addMarker(new MarkerOptions().position(address).title("Drag marker to set location").draggable(true));
            // mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, zoomLevel));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng pos = marker.getPosition();
                    final double lat = pos.latitude;
                    final double lng = pos.longitude;
                    System.out.println("1latitude: " + lat + ", longitude: " + lng);

                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(MapsActivity.this);
                    builder.setMessage("Confirm the chosen location or move the marker again to a different location");
                    builder.setTitle("Confirm location");
                    builder.setCancelable(false);
                    builder
                            .setPositiveButton(
                                    "Confirm",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent i = new Intent(getApplicationContext(), getInfo.class);
                                            System.out.println("2latitude: " + Double.toString(lat) + ", longitude: " + Double.toString(lng));
                                            i.putExtra("latitude", lat);
                                            i.putExtra("longitude", lng);
                                            setResult(1000, i);
                                            finish();
                                        }
                                    });
                    builder
                            .setNegativeButton(
                                    "Select another location",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // If user click no
                                            // then dialog box is canceled.
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            });
        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if (address==null)
                return null;
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
}
