package com.example.chris.baking.DataTypes;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private float quantity;
    private String measure;
    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[0];
        }
    };

    Ingredient(Parcel parcel){
        quantity = parcel.readFloat();
        measure = parcel.readString();
        ingredient = parcel.readString();
    }


    public static String ingredientToString(Ingredient ingredient){
        return "\u25CF " + ingredient.quantity + " " + ingredient.measure + " " + ingredient.ingredient + System.lineSeparator();
    }
}
