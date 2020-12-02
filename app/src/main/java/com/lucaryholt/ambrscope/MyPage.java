package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

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

        listView.setOnItemClickListener((_listView, liniarLayout, adapterPos, arrPos) -> {
            Intent intent = new Intent(this, SpotDetailView.class);
            intent.putExtra("id", Repo.r().getUserSpots().get((int) arrPos).getId());
            startActivity(intent);
        });

        Repo.r().startUserSpotsListener();
        Repo.r().setMyPageUpdateable(this);
    }

    @Override
    public void update() {
        spotAdapter.notifyDataSetChanged();
    }
}