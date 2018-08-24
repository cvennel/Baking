package com.example.chris.baking.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipeList = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;



    static class RecipeViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        LinearLayout indexCardEffect;
        ImageView imageView;

        RecipeViewHolder(View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textView = itemView.findViewById(R.id.recipe_title);
            imageView = itemView.findViewById(R.id.recipe_image);
            indexCardEffect = itemView.findViewById(R.id.index_card_effect);

        }

    }

    RecipeCardAdapter(Context context, Activity activity){
        mContext = context;
        mActivity = activity;
    }

    @NonNull
    @Override
    public RecipeCardAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_recipie_card, viewGroup, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        viewGroup.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder viewHolder, final int i) {

        final Recipe currentRecipe = mRecipeList.get(viewHolder.getAdapterPosition());

        viewHolder.textView.setText(currentRecipe.getName());

        String imageLocation = currentRecipe.getImage();

        if (imageLocation != null) {
            ImageView imageView = viewHolder.imageView;
            if (imageLocation.equals("")) {
                imageView.setVisibility(View.GONE);
                viewHolder.indexCardEffect.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(imageLocation).into(imageView);

                viewHolder.indexCardEffect.setVisibility(View.GONE);
            }

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mActivity, RecipeActivity.class);
                    intent.putExtra(mContext.getString(R.string.intent_extra_recipe), currentRecipe);

                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount(){
        return mRecipeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void setRecipeList(List<Recipe> recipeList){
        mRecipeList = recipeList;
    }

    public List<Recipe> getRecipeList(){
        return mRecipeList;
    }
}

