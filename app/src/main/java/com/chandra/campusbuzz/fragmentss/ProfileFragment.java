package com.chandra.campusbuzz.fragmentss;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandra.campusbuzz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView pUsername,pEmail,pFullName,pAbout;
    ImageView pProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        pUsername = view.findViewById(R.id.pUsername);
        pEmail=view.findViewById(R.id.pEmail);
        pFullName = view.findViewById(R.id.pFullName);
        pAbout = view.findViewById(R.id.pAbout);
        pProfileImage = view.findViewById(R.id.pProfileImage);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    String username = ""+ds.child("username").getValue();
                    String email = ""+ds.child("email").getValue();
                    String about = ""+ds.child("about").getValue();
                    String profile = ""+ds.child("profileImage").getValue();

                    pUsername.setText(username);
                    pFullName.setText(name);
                    pEmail.setText(email);
                    pAbout.setText(about);
                    try {
                        Picasso.get().load(profile).into(pProfileImage);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.baseline_add_a_photo_24).centerCrop().into(pProfileImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}