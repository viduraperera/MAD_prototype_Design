package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class my_tips_page extends AppCompatActivity
{
    private RecyclerView my_Tips_List;

    private DatabaseReference tips_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tips_page);

        tips_DB = FirebaseDatabase.getInstance().getReference().child("Tip");

        my_Tips_List = (RecyclerView) findViewById(R.id.tip_list);
        my_Tips_List.setHasFixedSize(true);
        my_Tips_List.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<tips, TipsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<tips, TipsViewHolder>
        (
                tips.class,
                R.layout.tips_row,
                TipsViewHolder.class,
                tips_DB
        )
        {
            @Override
            protected void populateViewHolder(TipsViewHolder tipsViewHolder, tips tips, int i)
            {
                tipsViewHolder.setCaption(tips.getCaption());
                tipsViewHolder.setCategory(tips.getCategory());
                tipsViewHolder.setDescription(tips.getDescription());
                tipsViewHolder.setTip_Image(getApplicationContext(), tips.getTip_Image());

            }
        };

        my_Tips_List.setAdapter(firebaseRecyclerAdapter);

    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder
    {
        View tip_view;

        public TipsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tip_view = itemView;
        }

        public void  setCaption(String caption)
        {
            TextView tip_caption = (TextView) tip_view.findViewById(R.id.tip_caption);
            tip_caption.setText(caption);
        }

        public void  setCategory(String category)
        {
            TextView tip_category = (TextView) tip_view.findViewById(R.id.tip_category);
            tip_category.setText(category);
        }

        public void  setDescription(String description)
        {
            TextView tip_description = (TextView) tip_view.findViewById(R.id.tip_description);
            tip_description.setText(description);
        }

        public void setTip_Image(Context ctx, String tip_Image)
        {
            ImageView tip_image = (ImageView) tip_view.findViewById(R.id.tip_image);
            Picasso.get().load(tip_Image).into(tip_image);
        }

    }
}