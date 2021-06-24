package com.example.foodrecipes.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipes.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView categoryImage;
    TextView categoryTitle;
    OnRecipeListener listener;
    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener listener) {
        super(itemView);
        this.listener = listener;
        categoryTitle = itemView.findViewById(R.id.category_title);
        categoryImage = itemView.findViewById(R.id.category_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
