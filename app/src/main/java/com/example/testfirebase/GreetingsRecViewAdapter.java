package com.example.testfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.model.Greetings;

import java.util.ArrayList;

public class GreetingsRecViewAdapter extends RecyclerView.Adapter<GreetingsRecViewAdapter.ViewHolder>{

    private ArrayList<Greetings> greetings= new ArrayList<>();
    View view;

    public GreetingsRecViewAdapter(View view) {
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.greeting_layout_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  GreetingsRecViewAdapter.ViewHolder holder, int position) {
        holder.textGreeting.setText(greetings.get(position).getGreetings());
        NavController navController = Navigation.findNavController(view);


    }

    @Override
    public int getItemCount() {
        return greetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textGreeting;
        private ConstraintLayout parent;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.recycler_parent);
            textGreeting = itemView.findViewById(R.id.textView_item);

        }
    }

    public void setGreetings(ArrayList<Greetings> greetings) {
        this.greetings = greetings;
        notifyDataSetChanged();
    }
}
