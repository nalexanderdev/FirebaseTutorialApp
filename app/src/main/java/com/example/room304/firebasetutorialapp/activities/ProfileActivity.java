package com.example.room304.firebasetutorialapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.room304.firebasetutorialapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends BaseActivity {

    EditText emailF, nameF;
    Button cancelBtn, updateBtn;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailF = (EditText) findViewById(R.id.emailField);
        nameF = (EditText) findViewById(R.id.nameField);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        emailF.setEnabled(false);
        nameF.setEnabled(false);
        updateBtn.setVisibility(View.INVISIBLE);
        cancelBtn.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null){
                    //
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            nameF.setText(user.getDisplayName());
            emailF.setText(user.getEmail());
        }



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameF.setEnabled(false);
                updateBtn.setVisibility(View.INVISIBLE);
                cancelBtn.setVisibility(View.INVISIBLE);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameF.getText().toString().isEmpty()){
                    nameF.setError("Required!");
                }else if(nameF.getText().toString().equals(user.getDisplayName())){
                    showToast("No new changes!");
                    nameF.setEnabled(false);
                    updateBtn.setVisibility(View.INVISIBLE);
                    cancelBtn.setVisibility(View.INVISIBLE);
                }else{
                    updateName(nameF.getText().toString());
                }

            }
        });


    }

    void updateName(String name){
        showMessageDialog("Updating Name...");
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressDialog();
                        if(!task.isSuccessful()){
                            showToast("Failed to update Profile Name!");
                        }else{
                            nameF.setEnabled(false);
                            updateBtn.setVisibility(View.INVISIBLE);
                            cancelBtn.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }else if(id == R.id.action_edit_profile){
            nameF.setEnabled(true);
            updateBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            return true;
        }
        else if(id == R.id.action_posts){
            startActivity(new Intent(this, PostActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            auth.removeAuthStateListener(listener);
        }
    }
}
