package com.example.chris.baking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.baking.DataTypes.RecipeStep;
import com.example.chris.baking.R;

public class RecipeStepFragment extends Fragment {

    private RecipeStep mRecipeStep;
    private int mRecipeStepID;
    Boolean mIsTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        if (savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(getString(R.string.recipe_step_key));
            mRecipeStepID = savedInstanceState.getInt(getString(R.string.recipe_id_key));
            mIsTwoPane = savedInstanceState.getBoolean(getString(R.string.is_two_pane_key));
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mIsTwoPane) {
                    FragmentManager manager = getFragmentManager();

                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setFragmentRecipeInfo(RecipeActivity.SELECTED_RECIPE, mRecipeStepID);

                    manager.beginTransaction().replace(R.id.detail_fragment_container, detailFragment, "DetailFragment").commit();
                } else {

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(getString(R.string.intent_extra_recipe), RecipeActivity.SELECTED_RECIPE);
                    intent.putExtra(DetailActivity.EXTRA_STEP, mRecipeStepID);
                    intent.putExtra(DetailActivity.EXTRA_TWO_PANE, mIsTwoPane);

                    startActivity(intent);
                }
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


    public void setRecipeInfo(RecipeStep recipeStep, int recipeStepNum, Boolean isTwoPane) {
        mRecipeStep = recipeStep;
        mRecipeStepID = recipeStepNum;
        mIsTwoPane = isTwoPane;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe_step_key), mRecipeStep);
        outState.putInt(getString(R.string.recipe_id_key), mRecipeStepID);
        outState.putBoolean(getString(R.string.is_two_pane_key), mIsTwoPane);
    }
}
