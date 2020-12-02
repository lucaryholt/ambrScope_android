package com.lucaryholt.ambrscope;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.Repo.Repo;

import java.io.InputStream;
import java.util.Date;

public class AddSpot extends AppCompatActivity {

    private double latitude;
    private double longitude;

    private Bitmap currentBitmap;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        TextView location = findViewById(R.id.addSpotLocationText);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        location.setText(latitude + "," + longitude);

        imageView = findViewById(R.id.addSpotImageView);
    }

    public void save(View view) {
        Spot newSpot = new Spot();
        String id = newSpot.getId();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newSpot.setUserID(user.getUid());

        newSpot.setTimeStamp(new Date().getTime() + "");

        newSpot.setLatitude(latitude);
        newSpot.setLongitude(longitude);

        Repo.r().uploadImage(id, currentBitmap);

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        imageView = findViewById(R.id.addSpotImageView);
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
}