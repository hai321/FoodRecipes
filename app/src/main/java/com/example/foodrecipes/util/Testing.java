package com.example.foodrecipes.util;

import android.util.Log;

import com.example.foodrecipes.models.Recipe;

import java.util.List;

public class Testing {
    public static void printRecipes(List<Recipe>list,String Tag){
        for(Recipe recipe: list){
            Log.d(Tag, "printRecipes: "+ recipe.getTitle());
        }
    }
}
