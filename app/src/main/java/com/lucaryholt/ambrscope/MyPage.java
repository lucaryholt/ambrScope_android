package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.lucaryholt.ambrscope.Adapter.SpotAdapter;
import com.lucaryholt.ambrscope.Interface.Updateable;
import com.lucaryholt.ambrscope.Repo.Repo;

public class MyPage extends AppCompatActivity implements Updateable {

    private SpotAdapter spotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ListView listView = findViewById(R.id.myPageSpotListView);

        spotAdapter = new SpotAdapter(this);
        listView.setAdapter(spotAdapter);

        // Does not work ATM. I believe that the button on the 'spotrow',
        // may have overwritten this clickListener.
        listView.setOnItemClickListener((_listView, liniarLayout, adapterPos, arrPos) -> {
            Log.i("MyPageInfo", "Item clicked! " + arrPos);
            Intent intent = new Intent(this, SpotDetailView.class);
            intent.putExtra("id", Repo.r().getUserSpots().get((int) arrPos).getId());
            startActivity(intent);
        });

        Repo.r().startUserSpotsListener();
        Repo.r().setMyPageUpdateable(this);
    }

    @Override
    public void update() {
        Log.i("MyPageInfo", "Data updated!");
        if(Repo.r().getUserSpots().size() == 0) {
            Toast.makeText(this, "No spots.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            spotAdapter.notifyDataSetChanged();
        }
    }
}