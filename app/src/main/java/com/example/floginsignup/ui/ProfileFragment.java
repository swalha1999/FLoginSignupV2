package com.example.floginsignup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.floginsignup.FirebaseServices;
import com.example.floginsignup.MainActivity;
import com.example.floginsignup.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name = view.findViewById(R.id.tvProfileName);
        TextView email = view.findViewById(R.id.tvProfileEmail);
        MaterialButton signOut = view.findViewById(R.id.btnSignOut);

        FirebaseUser user = FirebaseServices.getInstance().getAuth().getCurrentUser();
        String userEmail = user != null && user.getEmail() != null ? user.getEmail() : "Guest";
        name.setText("Signed in");
        email.setText(userEmail);

        signOut.setOnClickListener(v -> {
            FirebaseServices.getInstance().getAuth().signOut();
            if (getActivity() != null) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
