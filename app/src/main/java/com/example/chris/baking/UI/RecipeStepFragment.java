package com.example.chris.baking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeStepFragment extends Fragment {

    private RecipeStep mRecipeStep;
    private int recipeStepID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        if (savedInstanceState != null){
            mRecipeStep = savedInstanceState.getParcelable(getString(R.string.recipe_step_key));
            recipeStepID = savedInstanceState.getInt(getString(R.string.recipe_id_key));
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_RECIPE, RecipeActivity.SELECTED_RECIPE);
                intent.putExtra(DetailActivity.EXTRA_STEP,recipeStepID);

                startActivity(intent);
            }
        };

        rootView.setOnClickListener(listener);

        if (mRecipeStep != null) {
            TextView textView = rootView.findViewById(R.id.recipe_step_fragment_tv);
            textView.setText(mRecipeStep.getShortDescription());

            //allows future json to include video thumbnail
            String thumbnailURL = mRecipeStep.getThumbnailURL();
        }


        return rootView;
    }



    public void setRecipeInfo(RecipeStep recipeStep, int recipeStepNum) {
        mRecipeStep = recipeStep;
        recipeStepID = recipeStepNum;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe_step_key), mRecipeStep);
        outState.putInt(getString(R.string.recipe_id_key), recipeStepID);
    }
}
