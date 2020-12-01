package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lucaryholt.ambrscope.Enum.Chance;
import com.lucaryholt.ambrscope.LocationHandler.LocationHandler;
import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

import java.util.Date;

public class AddSpot extends AppCompatActivity {

    private TextView location;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        location = findViewById(R.id.addSpotLocationText);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        location.setText(latitude + "," + longitude);
    }

    public void save(View view) {
        Spot newSpot = new Spot();
        String id = newSpot.getId();

        newSpot.setTimeStamp(new Date().getTime() + "");

        newSpot.setLatitude(latitude);
        newSpot.setLongitude(longitude);

        ImageView imageView = findViewById(R.id.addSpotImageView);
        Bitmap bitmap = imageView.getDrawingCache();
        // TODO send to repo and upload to storage with id

        RadioGroup chance = findViewById(R.id.addSpotChanceRadioGroup);
        if (chance.getCheckedRadioButtonId() != -1) {
            RadioButton chanceChoice = findViewById(chance.getCheckedRadioButtonId());
            newSpot.setChance(chanceChoice.getText().toString());
        } else newSpot.setChance("Not specified.");

        RadioGroup finderMethod = findViewById(R.id.addSpotFinderMethodRadioGroup);
        if (finderMethod.getCheckedRadioButtonId() != -1) {
            RadioButton finderMethodChoice = findViewById(finderMethod.getCheckedRadioButtonId());
            newSpot.setFinderMethod(finderMethodChoice.getText().toString());
        } else newSpot.setFinderMethod("Not specified.");

        EditText time = findViewById(R.id.addSpotTimeEditText);
        newSpot.setTime(time.getText().toString());

        RadioGroup precise = findViewById(R.id.addSpotPreciseRadioGroup);
        if (precise.getCheckedRadioButtonId() != -1) {
            RadioButton preciseChoice = findViewById(precise.getCheckedRadioButtonId());
            newSpot.setPrecise(preciseChoice.getText().toString().equals("Precise"));
        } else newSpot.setPrecise(false);

        EditText description = findViewById(R.id.addSpotDescriptionEditText);
        newSpot.setDescription(description.getText().toString());

        Repo.r().addSpot(newSpot);
    }
}