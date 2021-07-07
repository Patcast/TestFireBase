package com.example.testfirebase.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testfirebase.GreetingsRecViewAdapter;
import com.example.testfirebase.R;
import com.example.testfirebase.model.Greetings;

import java.util.ArrayList;


public class DisplayAllGreetings extends Fragment {

    RecyclerView recyclerGreetings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_all_greetings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerGreetings = view.findViewById(R.id.recyclerView_greetings);
        ArrayList <Greetings> greetings = new ArrayList<>();
        greetings.add(new Greetings("Hello","Pat",2000));
        greetings.add(new Greetings("Hello","Pat",2000));
        greetings.add(new Greetings("Hello","Pat",2000));
        greetings.add(new Greetings("Hello","Pat",2000));
        greetings.add(new Greetings("Hello","Pat",2000));

        GreetingsRecViewAdapter adapter = new GreetingsRecViewAdapter(view);
        adapter.setGreetings(greetings);

        recyclerGreetings.setAdapter(adapter);
        recyclerGreetings.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}