package com.example.prototype_design_mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddingReview extends AppCompatActivity {

    RecyclerView commentsList;
    ImageButton postCommentBtn;
    EditText commentInputText;

    DatabaseReference UserRef, mDatabase;

    FirebaseAuth mAuth;

    String Post_key, currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_review);


        Post_key = getIntent().getExtras().get("Recipe_id").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipe").child(Post_key).child("reviews");

        commentsList = (RecyclerView) findViewById(R.id.recycleViewOfComments);
        commentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentsList.setLayoutManager(linearLayoutManager);

        postCommentBtn = (ImageButton) findViewById(R.id.comment_btn);
        commentInputText = (EditText) findViewById(R.id.commnet_text);

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String userName = dataSnapshot.child("name").getValue().toString();

                            validateComment(userName);

                            commentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Review, reviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Review, reviewViewHolder>(
                Review.class,
                R.layout.desgin_review,
                reviewViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(reviewViewHolder reviewViewHolder, Review addingReview, int i) {

                reviewViewHolder.setUsername(addingReview.getUsername());
                reviewViewHolder.setDate(addingReview.getDate());
                reviewViewHolder.setReview(addingReview.getReview());
                reviewViewHolder.setTime(addingReview.getTime());


            }
        };
        commentsList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class reviewViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public reviewViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setUsername(String username){
            TextView myUserName = (TextView) mView.findViewById(R.id.reviewUserName);
            myUserName.setText("@" + username + "   ");

        }
        public void setReview(String review){
            TextView myReview = (TextView) mView.findViewById(R.id.review_text);
            myReview.setText(review);
        }
        public void setDate(String date){
            TextView myDate = (TextView) mView.findViewById(R.id.reviewDate);
            myDate.setText("  Date: " + date);
        }
        public void setTime(String time){
            TextView myTime = (TextView) mView.findViewById(R.id.review_time);
            myTime.setText("  Time: "+ time);

        }
    }

    private void validateComment(String userName) {

        String commentText = commentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText)){
            Toast.makeText(AddingReview.this,"please write Text to comment...", Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
            final String saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calForTime.getTime());

            final String randomKey = currentUserId + saveCurrentDate + saveCurrentTime;

            HashMap commentMap = new HashMap();
            commentMap.put("uid", currentUserId);
            commentMap.put("review", commentText);
            commentMap.put("date", saveCurrentDate);
            commentMap.put("time", saveCurrentTime);
            commentMap.put("username", userName);

            mDatabase.child(randomKey).updateChildren(commentMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                Toast.makeText(AddingReview.this,"you have commented", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AddingReview.this,"Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
}