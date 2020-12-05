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

public class MainActivity extends AppCompatActivity implements Toastable, OnMapReadyCallback, Updateable {

    private final int RC_SIGN_IN = 100;
    private GoogleMap mMap;

    private ArrayList<Spot> spots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            login();
        }

        Repo.r().setToastable(this);
        Repo.r().setMainUpdateable(this);
        Repo.r().startSpotsListener();

        // TODO
        // 1. DetailView for Spots - Done!
        // 2. Decide either to have spot added at the location of device or new MapsActivity for choosing spot (with long press) - Done!
        // 3. Photo implemented - both choosing/taking and upload - Done!
        // 4. Better solution for updating markers on map - Not needed.
        // 5. Login - Done!
        // 6. My page with own added markers in a list - Done!
        // 7. Toasts - Done!
        // 8. MapsActivity with spots integrated on MainActivity - Done!
        // 9. Convert AddSpotMapsActivity to normal activity with Maps - Done!
        // 10. Scrollable activity for detailview and addspot - Done!
        // 11. Small map on addspot and detailview - Done!
        // 12. Show dialog box with help for various pages
        // 13. Delete spots from mypage
        // 14. Better image quality when using camera
        // 15. Styling - logo in corner of every screen and on login screen
        // 16. Other marker icon, maybe small amber icon
        // 17. App icon

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        FirebaseAuth.getInstance().signOut();
        Repo.r().logoutEvent();
        showToast("Logged out!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void myPageButton(View view) {
        Intent intent = new Intent(this, MyPage.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showToast("Logged in!");

                Log.i("AuthInfo", "User " + user.getUid() + " has logged in.");
            } else {
                Log.i("AuthInfo", "Login failed.");
                Log.i("AuthInfo", response.getError().getErrorCode() + "");
                login();
            }
        }
    }

    private void login() {
        Log.i("AuthInfo", "Not logged in.");
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
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
        spots = Repo.r().getSpots();
        //drawMarkers();
        if(mMap != null) {
            if(spots.size() != 0) {
                for (Spot spot : spots) {
                    mMap.addMarker(new MarkerOptions()
                            .snippet(spot.getId())
                            .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                            .title(spot.getDescription()));
                }
            }
        }
    }
}