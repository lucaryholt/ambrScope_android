package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class AddSpotSelect extends AppCompatActivity implements OnMapReadyCallback {

    // To have a Google Maps fragment implemented in an activity, there are some things, that must be followed.
    // 1. Implement OnMapReadyCallback (with onMapReady(Googlemap))
    // 2. In onCreate() you need to add this to the map (with the SupportMapFragment below)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot_select);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void helpButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_spot_select_help_message)
                .setNeutralButton(R.string.add_spot_select_help_dismiss_button, (dialog, id) -> {});

        builder.create().show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLongClickListener(latLng -> {
            Intent intent = new Intent(this, AddSpotDetails.class);
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            startActivity(intent);
        });

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(56.085556, 10.8746833), 6.5f)));
    }
}