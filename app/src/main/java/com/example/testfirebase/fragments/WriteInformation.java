package com.example.testfirebase.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.example.testfirebase.model.Greetings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class WriteInformation extends Fragment {

    EditText editGreeting;
    EditText editName;
    EditText editDate;
    NavController navController;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_information, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editGreeting = view.findViewById(R.id.editText_Greetings);
        editName = view.findViewById(R.id.editText_name);
        editDate = view.findViewById(R.id.editText_number);
        navController = Navigation.findNavController(view);
        Button writeButton = view.findViewById(R.id.button_info_write);
        writeButton.setOnClickListener(v -> onClick());
    }
    public void onClick(){
        Greetings myGreeting = new Greetings(
                editGreeting.getText().toString(),
                editName.getText().toString(),
                Integer.parseInt(editDate.getText().toString()) );
        db.collection("greetings").
                add(myGreeting).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        WriteInformationDirections.ActionWriteInformationToDisplayAllInformation action = WriteInformationDirections.actionWriteInformationToDisplayAllInformation();
                        action.setId(documentReference.getId());
                        navController.navigate(action);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(getContext(),"Error Loading the data",Toast.LENGTH_SHORT).show();
            }
        });

    }
}