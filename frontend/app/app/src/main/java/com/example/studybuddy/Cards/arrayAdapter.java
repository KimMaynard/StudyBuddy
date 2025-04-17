package com.example.studybuddy.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studybuddy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    private final Context context;  // FIX: Store context to use it later
    private final int resourceId;

    public arrayAdapter(Context context, int resourceId, List<cards> items) {
        super(context, resourceId, items);
        this.context = context;         // assign context here
        this.resourceId = resourceId;   // assign layout resource
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView university = convertView.findViewById(R.id.university);
        ImageView imageView = convertView.findViewById(R.id.profileImage);


        cards card = getItem(position);

        if (card != null) {
            // Set name and university
            if (name != null) {
                name.setText(card.getName());
            }
            if (university != null) {
                university.setText("University: " + card.getUniversity());
            }

            // Load profile image with fallback
            if (imageView != null) {
                loadProfileImage(card.getProfileImageUrl(), imageView);
            }


        }

        return convertView;
    }


    private void loadProfileImage(String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.profile);
        }
    }
}