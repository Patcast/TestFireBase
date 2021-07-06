package com.example.testfirebase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testfirebase.R;
import com.example.testfirebase.SignInGoogle;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
Button logOutButton;
ConstraintLayout bottomLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomLogOut = findViewById(R.id.bottom_layout);
        logOutButton = findViewById(R.id.button_log_out);
        logOutButton.setOnClickListener(v->signOut());
    }

    ///// Sign out process///////////////
    private void signOut() {
        SignInGoogle.INSTANCE.mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> FirebaseAuth.getInstance().signOut());
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.logIn);
    }

    public void hideLogOut(boolean invisible){
        if(invisible) {bottomLogOut.setVisibility(View.INVISIBLE);}
        else bottomLogOut.setVisibility(View.VISIBLE);
    }

}