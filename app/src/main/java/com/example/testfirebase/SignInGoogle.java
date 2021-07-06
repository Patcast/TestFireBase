package com.example.testfirebase;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public enum SignInGoogle {
    INSTANCE;
    public GoogleSignInClient mGoogleSignInClient;
    public FirebaseAuth mAuth;

}
