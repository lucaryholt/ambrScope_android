package com.lucaryholt.ambrscope.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.MyPage;
import com.lucaryholt.ambrscope.R;
import com.lucaryholt.ambrscope.Repo.Repo;
import com.lucaryholt.ambrscope.SpotDetailView;

public class SpotAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    public SpotAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Repo.r().getUserSpots().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null){
            view = layoutInflater.inflate(R.layout.spotrow, null);
        }
        Spot spot = Repo.r().getUserSpots().get(position);

        Button deleteButton = view.findViewById(R.id.spotRowDeleteButton);
        deleteButton.setOnClickListener((v) -> {
            Repo.r().deleteSpot(spot.getId());
        });

        TextView spotDescriptionTextView = view.findViewById(R.id.spotDescriptionTextView);
        spotDescriptionTextView.setText(spot.getDescription());

        return view;
    }
}
