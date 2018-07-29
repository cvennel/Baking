package com.example.chris.baking;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.nio.file.Path;

public class RecipeCardFragment extends Fragment {

    String mTitle;
    String mImageLocation;

    public RecipeCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipie_card, container, false);

        TextView textView = rootView.findViewById(R.id.recipe_title);

        if(mTitle != null){
            textView.setText(mTitle);
        }

        if(mImageLocation != null){
            ImageView imageView = rootView.findViewById(R.id.recipe_image);
            if(mImageLocation.equals("")){
                mImageLocation = "https://images.pexels.com/photos/291767/pexels-photo-291767.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260";
            }

            Picasso.with(getContext()).load(mImageLocation).into(imageView);


        }
        return rootView;
    }

    public void setTitle(String title){
        mTitle = title;
    }
    public void setImage(String imageLocation){
        mImageLocation = imageLocation;
    }

}
