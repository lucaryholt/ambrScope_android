package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lucaryholt.ambrscope.Repo.Repo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Repo.r().startListener();

        // TODO
        // 1. DetailView for Spots - Done!
        // 2. Decide either to have spot added at the location of device or new MapsActivity for choosing spot (with long press) - Done!
        // 3. Photo implemented - both choosing/taking and upload - Done!
        // 4. Better solution for updating markers on map
        // 5. Login
        // 6. MapsActivity with spots integrated on MainActivity
        // 7. Styling
        // 8. Other marker icon, maybe small amber icon
        // 9. App icon
    }

    public void test0Button(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void test1Button(View view) {
        Intent intent = new Intent(this, AddSpotMapsActivity.class);
        startActivity(intent);
    }
}