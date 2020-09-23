package com.example.prototype_design_mad;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

////public class createrecipelist extends ArrayAdapter<recipe> {
////    private recipe context;
////    List<recipe> CreateRecipeList;
////
////    public createrecipelist(recipe context, List<recipe> CreateRecipeList){
////        super(context, R.layout.createrecipeview,CreateRecipeList);
////        this.context = context;
////        this.CreateRecipeList = CreateRecipeList;
////    }
////
////    @Override
////    public View getView(int position, @Nullable View convertView) {
////        LayoutInflater inflater = context.getLayoutInflater();
////
////        View recipeView = inflater.inflate(R.layout.createrecipeview,null,true);
////
////        TextView textingredients = (TextView) recipeView.findViewById(R.id.textingredients);
////        TextView textprocedure= (TextView) recipeView.findViewById(R.id.textprocedure);
////        TextView texttitle = (TextView) recipeView.findViewById(R.id.texttitle);
////        TextView textdescription = (TextView) recipeView.findViewById(R.id.textdescription);
////
////        recipe CreateRecipeList = CreateRecipeList.get(position);
////
////
////    }
//}
