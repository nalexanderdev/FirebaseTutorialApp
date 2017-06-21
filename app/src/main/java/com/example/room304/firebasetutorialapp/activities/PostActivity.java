package com.example.room304.firebasetutorialapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.room304.firebasetutorialapp.R;
import com.example.room304.firebasetutorialapp.adapters.PostListViewAdapter;
import com.example.room304.firebasetutorialapp.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends BaseActivity {

    ListView listView;
    PostListViewAdapter adapter;

    List<Post> postList;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseUser user;

    private DatabaseReference postReference;
    private ValueEventListener eventListener;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    //
                    startActivity(new Intent(PostActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        postReference = FirebaseDatabase.getInstance().getReference().child("posts");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {

        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        postList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new PostListViewAdapter(this, R.layout.post_item, postList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(listener);

        ValueEventListener postEvents = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.d("child", child.getValue(Post.class).toMap().toString());

                    Post p = child.getValue(Post.class);
                    Log.d("P", p.getPost());
                    postList.add(p);
                }
                adapter.updateList(postList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              showToast("Failed to load post.");
            }
        };

        postReference.addValueEventListener(postEvents);

        eventListener = postEvents;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                String title = p.getUsername() + " has added a new post";
                String content = p.getPost();
                //NewMessageNotification.notify(ViewPostActivity.this,p.getUsername(), title,content , 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        postReference.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            auth.removeAuthStateListener(listener);
        }
        if(eventListener != null){
            postReference.removeEventListener(eventListener);
        }
        if(mChildEventListener != null){
            postReference.removeEventListener(mChildEventListener);
        }
    }
}
