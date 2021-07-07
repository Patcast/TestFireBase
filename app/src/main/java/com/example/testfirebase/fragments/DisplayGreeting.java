package com.example.testfirebase.fragments;

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

import com.example.testfirebase.R;
import com.example.testfirebase.activities.MainActivity;
import com.example.testfirebase.model.Greetings;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class DisplayGreeting extends Fragment {
    private static final String TAG = "DisplayGreeting";
    TextView textGreetings;
    TextView textSender;
    TextView textDate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.hideLogOut(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_display_greeting, container, false);
        textGreetings = view.findViewById(R.id.textView_Greetings);
        textSender = view.findViewById(R.id.textView_name);
        textDate = view.findViewById(R.id.textView_number);
        setText();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        Button displayButton = view.findViewById(R.id.button_info_display);
        displayButton.setOnClickListener(v -> navController.navigate(R.id.action_displayAllInformation_to_writeInformation));
        Button allGreetingsButton = view.findViewById(R.id.button_allGreetings);
        allGreetingsButton.setOnClickListener(v -> navController.navigate(R.id.action_displayAllInformation_to_displayAllGreetings));
    }

    private void setText() {
        if (getArguments() != null) {
            DisplayGreetingArgs args = DisplayGreetingArgs.fromBundle(getArguments());
            final DocumentReference docRef = db.collection("Test").document(args.getId());
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        //get data and make an object immediately.
                        Greetings myGreeting = snapshot.toObject(Greetings.class);
               /*     String greetings = snapshot.getString("greetings");
                    String sender = snapshot.getString("sender");
                    Double date = snapshot.getDouble("date");

                    textGreetings.setText(greetings);
                    textSender.setText(sender);
                    textDate.setText(String.valueOf(date));*/

                        textGreetings.setText(myGreeting.getGreetings());
                        textSender.setText(myGreeting.getSender());
                        textDate.setText(String.valueOf(myGreeting.getDate()));
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });


        }
    }
}