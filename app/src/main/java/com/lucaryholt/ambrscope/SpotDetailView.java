package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

public class SpotDetailView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ImageView image;
    private TextView chance;
    private TextView finderMethod;
    private TextView time;
    private TextView precise;
    private TextView description;
    private TextView amount;
    private TextView additionalInfo;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail_view);

        description = findViewById(R.id.spotDetailViewDescriptionTextView);
        image = findViewById(R.id.spotDetailViewImageView);
        chance = findViewById(R.id.spotDetailViewChanceValueTextView);
        finderMethod = findViewById(R.id.spotDetailViewFinderMethodValueTextView);
        time = findViewById(R.id.spotDetailViewTimeValueTextView);
        precise = findViewById(R.id.spotDetailViewPreciseValueTextView);
        amount = findViewById(R.id.spotDetailViewAmountValueTextView);
        additionalInfo = findViewById(R.id.spotDetailViewAdditionalInfoValueTextView);

        String id = getIntent().getStringExtra("id");
        Spot spot = Repo.r().getSpot(id);

        if(spot == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            latitude = spot.getLatitude();
            longitude = spot.getLongitude();

            if(spot.getBitmap() == null) {
                Repo.r().downloadBitmap(spot, image);
            } else {
                image.setImageBitmap(spot.getBitmap());
            }
            chance.setText(spot.getChance());
            finderMethod.setText(spot.getFinderMethod());
            time.setText(spot.getTime());
            precise.setText(spot.isPrecise() + "");
            description.setText(spot.getDescription());
            amount.setText(spot.getAmount());
            additionalInfo.setText(spot.getAdditionalInfo());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(latitude, longitude), 12)));
    }
}