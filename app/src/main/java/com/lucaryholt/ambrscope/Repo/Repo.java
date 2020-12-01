package com.lucaryholt.ambrscope.Repo;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.lucaryholt.ambrscope.Model.Spot;

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
    private final String PICTUREID = "pictureID";
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
        map.put(PICTUREID, spot.getPictureID());
        map.put(CHANCE, spot.getChance());
        map.put(TIME, spot.getTime());
        map.put(FINDERMETHOD, spot.getFinderMethod());
        map.put(PRECISE, spot.isPrecise() + "");
        map.put(DESCRIPTION, spot.getDescription());

        ref.set(map).addOnSuccessListener(task -> System.out.println("Spot " + spot.getId() + " saved."));
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
                         (String) snap.get(PICTUREID),
                         (String) snap.get(CHANCE),
                         (String) snap.get(TIME),
                         (String) snap.get(FINDERMETHOD),
                         Boolean.parseBoolean((String) snap.get(PRECISE)),
                         (String) snap.get(DESCRIPTION)
                 );
                 spots.add(spot);
             }
        });
    }

}
