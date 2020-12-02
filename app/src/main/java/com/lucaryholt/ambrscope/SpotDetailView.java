package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

public class SpotDetailView extends AppCompatActivity {

    private TextView location;
    private ImageView image;
    private TextView chance;
    private TextView finderMethod;
    private TextView time;
    private TextView precise;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail_view);

        location = findViewById(R.id.spotDetailViewLocationTextView);
        image = findViewById(R.id.spotDetailViewImageView);
        chance = findViewById(R.id.spotDetailViewChanceTextView);
        finderMethod = findViewById(R.id.spotDetailViewFinderMethodTextView);
        time = findViewById(R.id.spotDetailViewTimeTextView);
        precise = findViewById(R.id.spotDetailViewPreciseTextView);
        description = findViewById(R.id.spotDetailViewDescriptionTextView);

        String id = getIntent().getStringExtra("id");
        Spot spot = Repo.r().getSpot(id);

        location.setText(spot.getLatitude() + "," + spot.getLongitude());
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
    }
}