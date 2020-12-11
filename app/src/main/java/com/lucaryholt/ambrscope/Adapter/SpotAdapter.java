package com.lucaryholt.ambrscope.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucaryholt.ambrscope.Model.Spot;
import com.lucaryholt.ambrscope.MyPage;
import com.lucaryholt.ambrscope.R;
import com.lucaryholt.ambrscope.Repo.Repo;
import com.lucaryholt.ambrscope.SpotDetailView;

public class SpotAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final Context context;

    public SpotAdapter(Context context) {
        this.context = context;
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

        ImageView trashImage = view.findViewById(R.id.spotRowTrashImageView);
        trashImage.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setMessage("Delete '" + spot.getDescription()+ "' spot?")
                    .setPositiveButton("Okay", (dialog, id) -> Repo.r().deleteSpot(spot.getId()))
                    .setNegativeButton("Cancel", (dialog, id) -> {});
            builder.create().show();
        });

        TextView spotDescriptionTextView = view.findViewById(R.id.spotDescriptionTextView);
        spotDescriptionTextView.setText(spot.getDescription());

        return view;
    }
}
