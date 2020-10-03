//package com.example.prototype_design_mad.ViewHolder;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.prototype_design_mad.Interface.ItemClickListener;
//import com.example.prototype_design_mad.R;
//
//public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//    public TextView recipe, date, time, ingredients;
//    public ImageView imageView;
//    public ItemClickListener listener;
//
//    public RecipeViewHolder(@NonNull View itemView) {
//        super(itemView);
//
//        imageView = (ImageView)itemView.findViewById(R.id.foodimage);
//        recipe = (TextView)itemView.findViewById(R.id.tvRecipe);
//        date = (TextView)itemView.findViewById(R.id.tvDate);
//        time = (TextView)itemView.findViewById(R.id.tvTime);
//        ingredients = (TextView)itemView.findViewById(R.id.tvIngredients);
//
//    }
//    public void setItemClickListner(ItemClickListener listner){
//
//        this.listener = listner;
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        listener.onClick(view, getAdapterPosition(),false);
//    }
//}
