package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucaryholt.ambrscope.Interface.Toastable;
import com.lucaryholt.ambrscope.Interface.Updateable;
import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Toastable, OnMapReadyCallback, Updateable {

    private final int RC_SIGN_IN = 100;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checks if a user is logged in, if not we start the login flow
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            login();
        }

        // Enable the repo to show Toasts on this activity and notify for changes in Firestore
        Repo.r().setToastable(this);
        Repo.r().setMainUpdateable(this);
        Repo.r().startSpotsListener();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Only show 'My Page' button to non-anonymous users
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Button myPageButton = findViewById(R.id.myPageButton);
        if(user != null) {
            if(user.isAnonymous()) {
                myPageButton.setVisibility(View.INVISIBLE);
            } else {
                myPageButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addSpotButton(View view) {
        Intent intent = new Intent(this, AddSpotSelect.class);
        startActivity(intent);
    }

    public void logoutButton(View view) {
        // Log out of firebase and stop listeners in Repo
        FirebaseAuth.getInstance().signOut();
        Repo.r().logoutEvent();
        showToast("Logged out!");
        // Then we start this Activity again, to "relaunch" the app.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void myPageButton(View view) {
        Intent intent = new Intent(this, MyPage.class);
        startActivity(intent);
    }

    private void login() {
        // We use the login UI that FirebaseAuth provides
        // First choose which login methods to offer
        Log.i("AuthInfo", "Not logged in.");
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

        // Then we start the SignInIntent, set the methods and custom logo. We also pass a request code.
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.icon300titleunder)
                            .build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showToast("Logged in!");

                assert user != null;
                Log.i("AuthInfo", "User " + user.getUid() + " has logged in.");
            } else {
                Log.i("AuthInfo", "Login failed.");
                assert response != null;
                Log.i("AuthInfo", Objects.requireNonNull(response.getError()).getErrorCode() + "");
                login();
            }
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnInfoWindowClickListener((Marker marker) -> {
            Intent intent = new Intent(this, SpotDetailView.class);
            intent.putExtra("id", marker.getSnippet());
            startActivity(intent);
        });

        update();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(56.085556, 10.8746833), 6.5f)));
    }

    public void update() {
        /*
            This method is called when Repo gets a change from Firestore.
            It clears the markers on the map and sets them with the new data.
            We check if mMap is not null, as this method also gets called
            when the activity is not in the foreground and mMap is only
            initialised when the activity is in the foreground.

            Could be more optimised - only clear the markers that were changed.
         */
        ArrayList<Spot> spots = Repo.r().getSpots();
        if(mMap != null) {
            mMap.clear();
            if(spots.size() != 0) {
                for (Spot spot : spots) {
                    mMap.addMarker(new MarkerOptions()
                            .snippet(spot.getId())
                            .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                            .title(spot.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }
        }
    }
}