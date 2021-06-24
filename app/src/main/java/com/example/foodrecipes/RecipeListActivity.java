package com.example.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.foodrecipes.adapters.OnRecipeListener;
import com.example.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApi;
import com.example.foodrecipes.requests.ServiceGenerator;
import com.example.foodrecipes.requests.response.RecipeResponse;
import com.example.foodrecipes.requests.response.RecipeSearchResponse;
import com.example.foodrecipes.util.Constants;
import com.example.foodrecipes.util.Testing;
import com.example.foodrecipes.util.VerticalSpacingItemDecorator;
import com.example.foodrecipes.viewmodel.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    //ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list);
        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.isViewingRecipes()){
            //display search categories
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

    }

    private void initRecyclerView() {
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){
                    // search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });

    }

    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes!=null) {
                    if (mRecipeListViewModel.isViewingRecipes()) {
                        Testing.printRecipes(recipes, TAG);
                        mAdapter.setRecipes(recipes);
                    }
                }

            }
        });
    }
    public void searchRecipesApi(String query, int pageNumber){

        mRecipeListViewModel.searchRecipesApi(query,pageNumber);
    }

    @Override
    public void onRecipeClick(int position) {
        Log.d(TAG, "onRecipeClick: clicked. " + position);
        Intent intent = new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        searchRecipesApi(category,1);

    }


    private void initSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();

    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }
        else{
            displaySearchCategories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}