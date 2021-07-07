package com.example.testfirebase.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.example.testfirebase.SignInGoogle;
import com.example.testfirebase.activities.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends Fragment {

    private SignInButton signInButton;
    private TextView statusText;
    private final int RC_SIGN_IN = 9001;
    private final String TAG = "SignInActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SignInGoogle.INSTANCE.mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.server_client_id)).
                requestEmail().
                build();


        SignInGoogle.INSTANCE.mGoogleSignInClient =  GoogleSignIn.getClient(getContext(), gso);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        activity.hideLogOut(true);
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ///Buttons
        statusText = view.findViewById(R.id.text_signIn);
        signInButton= view.findViewById(R.id.btn_sign_in_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(v->signIn());
        navController = Navigation.findNavController(view);
    }

    ///// Sign in process///////////////
    private void signIn() {
        Intent signInIntent =  SignInGoogle.INSTANCE.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
            // Signed in successfully, show authenticated UI.
            // updateAfterSignedIn(account);//maybe I can pass fireBase user

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        SignInGoogle.INSTANCE.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user =  SignInGoogle.INSTANCE.mAuth.getCurrentUser();
                            checkIfUserRegistered(user, getContext());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void updateAfterSignedIn(FirebaseUser account){
        if (account !=null ){
            navController.navigate(R.id.action_logIn_to_displayAllGreetings);
        }
    }
    ///// Sign out process///////////////
    private void signOut() {
       navController.navigate(R.id.logIn);
        FirebaseAuth.getInstance().signOut();
    }

    ///// validate for registration and authorization ///////////////
    private void regInFireStore(FirebaseUser currentUser){
        if (currentUser !=null ) {
            DocumentReference docRef = db.collection("stakeholders").document(currentUser.getUid());
            Map<String, Object> stakeholder = new HashMap<>();
            if (currentUser.getDisplayName() != (null))
                stakeholder.put("name", currentUser.getDisplayName());
            if (currentUser.getPhoneNumber() != (null))
                stakeholder.put("phone", currentUser.getPhoneNumber());
            stakeholder.put("email", currentUser.getEmail());
            stakeholder.put("authorized", false);


            db.collection("stakeholders").document(currentUser.getUid())
                    .set(stakeholder)
                    .addOnSuccessListener(success -> Toast.makeText(getContext(), getText(R.string.succesful_registration), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(noSuccess -> Toast.makeText(getContext(), getText(R.string.unsuccesful_registration), Toast.LENGTH_SHORT).show());
        }
    }

    private void checkIfUserRegistered(FirebaseUser currentUser, Context currentContext){
        DocumentReference docRef =  db.collection("stakeholders").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("authorized")!=null && (boolean)document.get("authorized")) {
                            updateAfterSignedIn(currentUser);
                        }
                        else{
                            signOut();
                            Toast.makeText(currentContext,   getText(R.string.Access_denied)+" "+currentUser.getEmail(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        regInFireStore(currentUser);
                        //signOut();
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                    //signOut();
                }
            }
        });
    }
}