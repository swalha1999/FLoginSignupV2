package com.example.floginsignup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class WelcomeFragment extends Fragment {
    private TextView tvSubtitle;
    private Button btnLogout;
    private FirebaseServices fbs;

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbs = FirebaseServices.getInstance();
        tvSubtitle = view.findViewById(R.id.tvWelcomeSubtitle);
        btnLogout = view.findViewById(R.id.btnLogout);

        FirebaseUser user = fbs.getAuth().getCurrentUser();
        String email = user != null && user.getEmail() != null ? user.getEmail() : "";
        tvSubtitle.setText(getString(R.string.welcome_subtitle, email));

        btnLogout.setOnClickListener(v -> {
            fbs.getAuth().signOut();
            if (getActivity() != null) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.frameLayoutMain, new LoginFragment())
                        .commit();
            }
        });
    }
}
