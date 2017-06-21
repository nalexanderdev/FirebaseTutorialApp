package com.example.room304.firebasetutorialapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by room3.04 on 14/06/2017.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    public void showMessageDialog(String message){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);

        }
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void showMessageDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);

        }
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    public void hideProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public String getUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
