package com.lucaryholt.ambrscope;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

import java.io.InputStream;
import java.util.Date;

public class AddSpotDetails extends AppCompatActivity implements OnMapReadyCallback {

    private double latitude;
    private double longitude;

    private Bitmap currentBitmap;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot_details);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        imageView = findViewById(R.id.addSpotDetailsImageView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void save(View view) {
        RadioGroup chance = findViewById(R.id.addSpotDetailsChanceRadioGroup);
        RadioGroup finderMethod = findViewById(R.id.addSpotDetailsFinderMethodRadioGroup);
        RadioGroup precise = findViewById(R.id.addSpotDetailsPreciseRadioGroup);
        EditText time = findViewById(R.id.addSpotDetailsTimeEditText);
        EditText description = findViewById(R.id.addSpotDetailsDescriptionEditText);
        EditText amount = findViewById(R.id.addSpotDetailsAmountEditText);
        EditText additionalInfo = findViewById(R.id.addSpotDetailsAdditionalInfoEditText);

        String chanceString;
        if (chance.getCheckedRadioButtonId() != -1) {
            RadioButton chanceChoice = findViewById(chance.getCheckedRadioButtonId());
            chanceString = chanceChoice.getText().toString();
        } else chanceString = "Not specified.";

        String finderMethodString;
        if (finderMethod.getCheckedRadioButtonId() != -1) {
            RadioButton finderMethodChoice = findViewById(finderMethod.getCheckedRadioButtonId());
            finderMethodString = finderMethodChoice.getText().toString();
        } else finderMethodString = "Not specified.";

        boolean preciseBool;
        if (precise.getCheckedRadioButtonId() != -1) {
            RadioButton preciseChoice = findViewById(precise.getCheckedRadioButtonId());
            preciseBool = preciseChoice.getText().toString().equals("Precise");
        } else preciseBool = false;

        if(description.getText().toString().equals("")){
            Log.i("AddSpotInfo", "Needs description.");
            Toast.makeText(this, "Needs a description!", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;

            Spot newSpot = new Spot(
                    user.getUid(),
                    new Date().getTime() + "",
                    latitude,
                    longitude,
                    chanceString,
                    time.getText().toString(),
                    finderMethodString,
                    preciseBool,
                    description.getText().toString(),
                    amount.getText().toString(),
                    additionalInfo.getText().toString()
            );

            if(currentBitmap != null) {
                Repo.r().uploadImage(newSpot.getId(), currentBitmap);
            }

            Repo.r().addSpot(newSpot);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void galleryBtnPressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void cameraBtnPressen(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = findViewById(R.id.addSpotDetailsImageView);
        if(requestCode == 1){
            backFromGallery(data);
        }
        if(requestCode == 2){
            backFromCamera(data);
        }
    }

    private void backFromGallery(@Nullable Intent data){
        assert data != null;
        Uri imageUri = data.getData();
        try {
            InputStream is = getContentResolver().openInputStream(imageUri);
            currentBitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(currentBitmap);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private void backFromCamera(@Nullable Intent data){
        try {
            assert data != null;
            currentBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(currentBitmap);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(latitude, longitude), 11)));
    }
}