package com.example.timingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editUsername, editPassword, editEmail, editPhone;
    private TextView registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        configurateLink();

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.register_btn);
        registerUser.setOnClickListener(this);

        editUsername = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);
        editEmail = (EditText) findViewById(R.id.email);
        editPhone = (EditText) findViewById(R.id.phone);

    }

    private void configurateLink() {
        TextView textView = (TextView) findViewById(R.id.activity_login_link);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (username.isEmpty()) {
            editUsername.setError("Username is required");
            editUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editPhone.setError("Phone is required");
            editPhone.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please set valid email.");
            editEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Password is too short. Please set minimum 6 characters password.");
            editPassword.requestFocus();
            return;
        }
    }
}