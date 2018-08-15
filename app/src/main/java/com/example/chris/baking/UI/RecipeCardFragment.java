package com.example.chris.baking.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;
import com.squareup.picasso.Picasso;

public class RecipeCardFragment extends Fragment {

    Recipe mRecipe;
    String mTitle;
    String mImageLocation;

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "You clicked on the " + mRecipe.getName() + " recipe", Toast.LENGTH_LONG).show();
        }
    };

    public RecipeCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipie_card, container, false);

        TextView textView = rootView.findViewById(R.id.recipe_title);

        rootView.setOnClickListener(listener);

        if (mTitle != null) {
            textView.setText(mTitle);
        }

        if (mImageLocation != null) {
            ImageView imageView = rootView.findViewById(R.id.recipe_image);
            if (mImageLocation.equals("")) {
// TODO remove line when found beter idea
// mImageLocation = "https://images.pexels.com/photos/291767/pexels-photo-291767.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260";
                imageView.setVisibility(View.GONE);
            } else {
                Picasso.with(getContext()).load(mImageLocation).into(imageView);
            }

        }
        return rootView;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setImage(String imageLocation) {
        mImageLocation = imageLocation;
    }

    public void setmRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}
