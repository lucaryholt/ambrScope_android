package com.lucaryholt.ambrscope.Repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucaryholt.ambrscope.Model.Spot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Repo {

    private static final Repo instance = new Repo();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference col = db.collection("spots");

    private final String TIMESTAMP = "timestamp";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String CHANCE = "chance";
    private final String TIME = "time";
    private final String FINDERMETHOD = "finderMethod";
    private final String PRECISE = "precise";
    private final String DESCRIPTION = "description";

    private final ArrayList<Spot> spots = new ArrayList<>();

    public static Repo r() {
        return instance;
    }

    public ArrayList<Spot> getSpots() {
        return spots;
    }

    public void addSpot(Spot spot) {
        // Upload spot item
        DocumentReference ref = col.document(spot.getId());
        Map<String, String> map = new HashMap<>();
        map.put(TIMESTAMP, spot.getTimeStamp());
        map.put(LATITUDE, spot.getLatitude() + "");
        map.put(LONGITUDE, spot.getLongitude() + "");
        map.put(CHANCE, spot.getChance());
        map.put(TIME, spot.getTime());
        map.put(FINDERMETHOD, spot.getFinderMethod());
        map.put(PRECISE, spot.isPrecise() + "");
        map.put(DESCRIPTION, spot.getDescription());

        ref.set(map).addOnSuccessListener(task -> Log.i("RepoInfo", "Spot " + spot.getId() + " saved."));
    }

    public void uploadImage(String id, Bitmap bitmap) {
        StorageReference ref = storage.getReference().child(id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        ref.putStream(is).addOnSuccessListener(task -> Log.i("RepoInfo", "Image " + id + " has been uploaded."));
    }

    public void downloadBitmap(Spot spot) {
        StorageReference ref = storage.getReference(spot.getId());
        int max = 1024*1024*1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            spot.setBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            Log.i("RepoInfo", "Error in download " + exception);
        });
    }

    public Spot getSpot(String id) {
        return spots.get(spots.indexOf(new Spot(id)));
    }

    public void startListener() {
        col.addSnapshotListener((values, error) -> {
             spots.clear();
             assert values != null;
             for(DocumentSnapshot snap : values.getDocuments()) {
                 Spot spot = new Spot(
                         snap.getId(),
                         (String) snap.get(TIMESTAMP),
                         Double.parseDouble((String) snap.get(LATITUDE)),
                         Double.parseDouble((String) snap.get(LONGITUDE)),
                         (String) snap.get(CHANCE),
                         (String) snap.get(TIME),
                         (String) snap.get(FINDERMETHOD),
                         Boolean.parseBoolean((String) snap.get(PRECISE)),
                         (String) snap.get(DESCRIPTION)
                 );
                 spots.add(spot);
                 downloadBitmap(spot);
             }
        });
    }

}
