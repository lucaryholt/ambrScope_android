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
        // 1. DetailView for Spots
        // 2. Decide either to have spot added at the location of device or new MapsActivity for choosing spot (with long press)
        // 3. MapsActivity with spots integrated on MainActivity
        // 4. Photo implemented - both choosing/taking and upload
        // 5. Better solution for updating markers on map
    }

    public void test0Button(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void test1Button(View view) {
        Intent intent = new Intent(this, AddSpot.class);
        startActivity(intent);
    }
}