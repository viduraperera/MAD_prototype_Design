package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class my_tips_page extends AppCompatActivity
{
    private RecyclerView my_Tips_List;

    private FloatingActionButton addTipBtn;

    private DatabaseReference tips_DB;

    private DatabaseReference tips_DB_CurrentUser;

    private Query tips_Query_CurrentUser;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tips_page);

        mAuth = FirebaseAuth.getInstance();

        addTipBtn = (FloatingActionButton) findViewById(R.id.add_tip_btn);
        addTipBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                open_Add_Tips();
            }
        });

        tips_DB = FirebaseDatabase.getInstance().getReference().child("Tip");

        String currentUserId = mAuth.getCurrentUser().getUid();

        tips_DB_CurrentUser = FirebaseDatabase.getInstance().getReference().child("Tip");

        tips_Query_CurrentUser = tips_DB_CurrentUser.orderByChild("uid").equalTo(currentUserId);

        my_Tips_List = (RecyclerView) findViewById(R.id.tip_list);
        my_Tips_List.setHasFixedSize(true);
        my_Tips_List.setLayoutManager(new LinearLayoutManager(this));


    }

    private void open_Add_Tips()
    {
        startActivity(new Intent(my_tips_page.this, add_cooking_tips.class));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<tips, TipsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<tips, TipsViewHolder>
        (
                tips.class,
                R.layout.tips_row,
                TipsViewHolder.class,
                tips_Query_CurrentUser
        )
        {
            @Override
            protected void populateViewHolder(TipsViewHolder tipsViewHolder, tips tips, int position)
            {
                final String tip_ID = getRef(position).getKey();

                tipsViewHolder.setCaption(tips.getCaption());
                tipsViewHolder.setCategory(tips.getCategory());
                //tipsViewHolder.setDescription(tips.getDescription());
                tipsViewHolder.setTip_Image(getApplicationContext(), tips.getTip_Image());

                tipsViewHolder.tip_view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //Toast.makeText(my_tips_page.this, tip_ID, Toast.LENGTH_LONG).show();
                        Intent tipViewIntent = new Intent(my_tips_page.this, TipDetailedView.class);
                        tipViewIntent.putExtra("tip_id", tip_ID);
                        startActivity(tipViewIntent);

                    }
                });

            }
        };

        my_Tips_List.setAdapter(firebaseRecyclerAdapter);

    }

    /*Swipe to DELETE*/
//    public void removeTip(int position)
//    {
//
////        getSnapshots().getSnapshot(position).getReference.de
//    }


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

//        public void  setDescription(String description)
//        {
//            TextView tip_description = (TextView) tip_view.findViewById(R.id.tip_description);
//            tip_description.setText(description);
//        }

        public void setTip_Image(Context ctx, String tip_Image)
        {
            ImageView tip_image = (ImageView) tip_view.findViewById(R.id.tip_image);
            Picasso.get().load(tip_Image).into(tip_image);
        }

    }

    /*This is for temporary use (from line 143 to )*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId() == R.id.add_item){
            startActivity(new Intent(my_tips_page.this, createrecipe1.class));
        }

        if(item.getItemId() == R.id.log_out){

            logout();

        }
        if(item.getItemId() == R.id.user_account){

            startActivity(new Intent(my_tips_page.this, userAccountPage.class));
        }

        /*Siri add tip_menu*/
        if(item.getItemId() == R.id.add_tip){

            startActivity(new Intent(my_tips_page.this, add_cooking_tips.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }
}