package com.lucaryholt.ambrscope.Repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucaryholt.ambrscope.Interface.Updateable;
import com.lucaryholt.ambrscope.Model.Spot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Repo {

    private static final Repo instance = new Repo();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference col = db.collection("spots");

    private ListenerRegistration spotsListener;
    private ListenerRegistration userSpotsListener;

    private final String USERID = "userID";
    private final String TIMESTAMP = "timestamp";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String CHANCE = "chance";
    private final String TIME = "time";
    private final String FINDERMETHOD = "finderMethod";
    private final String PRECISE = "precise";
    private final String DESCRIPTION = "description";

    private final ArrayList<Spot> spots = new ArrayList<>();
    private final ArrayList<Spot> userSpots = new ArrayList<>();

    private Updateable myPageUpdateable;

    public static Repo r() {
        return instance;
    }

    public ArrayList<Spot> getSpots() {
        return spots;
    }

    public ArrayList<Spot> getUserSpots() {
        return userSpots;
    }

    public void setMyPageUpdateable(Updateable myPageUpdateable) {
        this.myPageUpdateable = myPageUpdateable;
    }

    public void addSpot(Spot spot) {
        // Upload spot item
        DocumentReference ref = col.document(spot.getId());
        Map<String, String> map = new HashMap<>();
        map.put(USERID, spot.getUserID());
        map.put(TIMESTAMP, spot.getTimeStamp());
        map.put(LATITUDE, spot.getLatitude() + "");
        map.put(LONGITUDE, spot.getLongitude() + "");
        map.put(CHANCE, spot.getChance());
        map.put(TIME, spot.getTime());
        map.put(FINDERMETHOD, spot.getFinderMethod());
        map.put(PRECISE, spot.isPrecise() + "");
        map.put(DESCRIPTION, spot.getDescription());

        ref.set(map).addOnSuccessListener(task -> {
            Log.i("RepoInfo", "Spot " + spot.getId() + " saved.");
            //TODO Toast
        });
    }

    public void uploadImage(String id, Bitmap bitmap) {
        StorageReference ref = storage.getReference().child(id);

        int scale = bitmap.getHeight() / 300;

        Bitmap bM;
        if(scale != 0) {
            int width = bitmap.getWidth() / scale;
            bM = Bitmap.createScaledBitmap(bitmap, width, 300, false);
        } else {
          bM = bitmap;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bM.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        ref.putStream(is).addOnSuccessListener(task -> Log.i("RepoInfo", "Image " + id + " has been uploaded."));
    }

    public void downloadBitmap(Spot spot, ImageView imageview) {
        StorageReference ref = storage.getReference(spot.getId());
        int max = 1024*1024*1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            spot.setBitmap(bitmap);
            if(imageview != null){
                imageview.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(exception -> {
            Log.i("RepoInfo", "Error in download " + exception);
        });
    }

    public Spot getSpot(String id) {
        return spots.get(spots.indexOf(new Spot(id)));
    }

    public void startSpotsListener() {
        spotsListener = col.addSnapshotListener((values, error) -> {
             spots.clear();
             assert values != null;
             for(DocumentSnapshot snap : values.getDocuments()) {
                 Spot spot = new Spot(
                         snap.getId(),
                         (String) snap.get(USERID),
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
                 downloadBitmap(spot, null);
             }
        });
    }

    public void startUserSpotsListener() {
        String ids = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        userSpotsListener = col.whereEqualTo(USERID, ids).addSnapshotListener((values, error) -> {
            userSpots.clear();
            assert values != null;
            for(DocumentSnapshot snap : values.getDocuments()) {
                Spot spot = new Spot(
                        snap.getId(),
                        (String) snap.get(USERID),
                        (String) snap.get(TIMESTAMP),
                        Double.parseDouble((String) snap.get(LATITUDE)),
                        Double.parseDouble((String) snap.get(LONGITUDE)),
                        (String) snap.get(CHANCE),
                        (String) snap.get(TIME),
                        (String) snap.get(FINDERMETHOD),
                        Boolean.parseBoolean((String) snap.get(PRECISE)),
                        (String) snap.get(DESCRIPTION)
                );
                userSpots.add(spot);
                downloadBitmap(spot, null);
                if(myPageUpdateable != null) {
                    myPageUpdateable.update();
                }
            }
        });
    }

    public void logoutEvent() {
        spots.clear();
        spotsListener.remove();
        if(userSpotsListener != null) {
            userSpots.clear();
            myPageUpdateable.update();
            userSpotsListener.remove();
        }
    }

}
