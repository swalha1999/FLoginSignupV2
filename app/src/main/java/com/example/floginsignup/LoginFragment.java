package com.example.floginsignup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    private EditText etEmail, etPassword;
    private TextView tvSignupLink;
    private TextView tvForgotPasswordLink;
    private Button btnLogin;
    private FirebaseServices fbs;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbs = FirebaseServices.getInstance();
        etEmail = view.findViewById(R.id.etUsernameLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvForgotPasswordLink = view.findViewById(R.id.tvForgotPasswordLinkLogin);
        tvSignupLink = view.findViewById(R.id.tvSignupLinkLogin);

        tvSignupLink.setOnClickListener(v -> gotoSignupFragment());
        tvForgotPasswordLink.setOnClickListener(v -> gotoForgotPasswordFragment());

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            fbs.getAuth().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> gotoWelcomeFragment())
                    .addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });
    }

    private void gotoSignupFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new SignupFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoForgotPasswordFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new ForgotPasswordFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoWelcomeFragment() {
        if (getActivity() == null) return;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.beginTransaction()
                .replace(R.id.frameLayoutMain, new WelcomeFragment())
                .commit();
    }
}
