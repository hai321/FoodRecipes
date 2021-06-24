package com.example.foodrecipes.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;
    private RecipeRepository mRecipeRepository;
    public RecipeListViewModel() {
        mIsViewingRecipes = false;
        mRecipeRepository = RecipeRepository.getInstance();
    }
    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }
    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes){
            mRecipeRepository.searchNextPage();
        }
    }
    public void searchRecipesApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipesApi(query,pageNumber);
    }
    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        this.mIsViewingRecipes = isViewingRecipes;
    }

    public boolean onBackPressed(){
        if(mIsViewingRecipes){
            mIsViewingRecipes=false;
            return false;
        }
        return true;
    }
}
