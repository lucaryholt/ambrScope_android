package com.lucaryholt.ambrscope.Repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lucaryholt.ambrscope.Interface.Toastable;
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

    private final String USER_ID = "userID";
    private final String TIMESTAMP = "timestamp";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String CHANCE = "chance";
    private final String TIME = "time";
    private final String FINDER_METHOD = "finderMethod";
    private final String PRECISE = "precise";
    private final String DESCRIPTION = "description";
    private final String AMOUNT = "amount";
    private final String ADDITIONAL_INFO = "additionalInfo";

    private final ArrayList<Spot> spots = new ArrayList<>();
    private final ArrayList<Spot> userSpots = new ArrayList<>();

    private Updateable myPageUpdateable;
    private Updateable mainUpdateable;
    private Toastable toastable;

    public static Repo r() {
        return instance;
    }

    private Repo(){}

    public ArrayList<Spot> getSpots() {
        return spots;
    }

    public ArrayList<Spot> getUserSpots() {
        return userSpots;
    }

    public void setMyPageUpdateable(Updateable myPageUpdateable) {
        this.myPageUpdateable = myPageUpdateable;
    }

    public void setMainUpdateable(Updateable mainUpdateable) {
        this.mainUpdateable = mainUpdateable;
    }

    public void setToastable(Toastable toastable) {
        this.toastable = toastable;
    }

    public void addSpot(Spot spot) {
        // Upload spot item
        DocumentReference ref = col.document(spot.getId());
        Map<String, String> map = new HashMap<>();
        map.put("id", spot.getId());
        map.put(USER_ID, spot.getUserID());
        map.put(TIMESTAMP, spot.getTimeStamp());
        map.put(LATITUDE, spot.getLatitude() + "");
        map.put(LONGITUDE, spot.getLongitude() + "");
        map.put(CHANCE, spot.getChance());
        map.put(TIME, spot.getTime());
        map.put(FINDER_METHOD, spot.getFinderMethod());
        map.put(PRECISE, spot.isPrecise() + "");
        map.put(DESCRIPTION, spot.getDescription());
        map.put(AMOUNT, spot.getAmount());
        map.put(ADDITIONAL_INFO, spot.getAdditionalInfo());

        ref.set(map).addOnSuccessListener(task -> {
            Log.i("RepoInfo", "Spot " + spot.getId() + " saved.");
            toastable.showToast("Spot has been saved!");
        });
    }

    public void uploadImage(String id, Bitmap bitmap) {
        StorageReference ref = storage.getReference().child(id);

        // As to not fill up Firebase Storage with unnecessarily large images, we scale them down here.
        int desiredHeight = 1000;
        int scale = bitmap.getHeight() / desiredHeight;

        Bitmap bM;
        if(scale != 0) {
            int width = bitmap.getWidth() / scale;
            bM = Bitmap.createScaledBitmap(bitmap, width, desiredHeight, false);
        } else {
            bM = bitmap;
        }

        // Then we compress them to JPEG format using ByteArrayOutputStream and ByteArrayInputStream
        // And use that InputStream to upload to Storage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bM.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        ref.putStream(is).addOnSuccessListener(task -> Log.i("RepoInfo", "Image " + id + " has been uploaded."));
    }

    public void downloadBitmap(Spot spot, ImageView imageview) {
        // We download images by calling getBytes with a max bytes argument on a Storage reference
        StorageReference ref = storage.getReference(spot.getId());
        int max = 10*1024*1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            spot.setBitmap(bitmap);
            // If we have passed an ImageView to this function we set the bitmap on that view
            if(imageview != null){
                imageview.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(exception -> Log.i("RepoInfo", "Error in download. Id: " + spot.getId()));
    }

    public Spot getSpot(String id) {
        try {
            return spots.get(spots.indexOf(new Spot(id)));
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i("RepoInfo", "Could not get spot. Probably deleted. Id: " + id);
            return null;
        }
    }

    public void deleteSpot(String id) {
        col.document(id).delete();
        storage.getReference().child(id).delete();

        toastable.showToast("Spot deleted");
    }

    public void startSpotsListener() {
        spotsListener = col.addSnapshotListener((values, error) -> {
             spots.clear();
             assert values != null;
             for(DocumentSnapshot snap : values.getDocuments()) {
                 Spot spot = spotFromSnap(snap);
                 spots.add(spot);
                 downloadBitmap(spot, null);
             }
             mainUpdateable.update();
        });
    }

    public void startUserSpotsListener() {
        String ids = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        userSpotsListener = col.whereEqualTo(USER_ID, ids).addSnapshotListener((values, error) -> {
            userSpots.clear();
            assert values != null;
            for(DocumentSnapshot snap : values.getDocuments()) {
                Spot spot = spotFromSnap(snap);
                userSpots.add(spot);
                downloadBitmap(spot, null);
            }
            if(myPageUpdateable != null) {
                myPageUpdateable.update();
            }
        });
    }

    private Spot spotFromSnap(DocumentSnapshot snap) {
        return new Spot(
                snap.getId(),
                (String) snap.get(USER_ID),
                (String) snap.get(TIMESTAMP),
                Double.parseDouble((String) Objects.requireNonNull(snap.get(LATITUDE))),
                Double.parseDouble((String) Objects.requireNonNull(snap.get(LONGITUDE))),
                (String) snap.get(CHANCE),
                (String) snap.get(TIME),
                (String) snap.get(FINDER_METHOD),
                Boolean.parseBoolean((String) snap.get(PRECISE)),
                (String) snap.get(DESCRIPTION),
                (String) snap.get(AMOUNT),
                (String) snap.get(ADDITIONAL_INFO)
        );
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
