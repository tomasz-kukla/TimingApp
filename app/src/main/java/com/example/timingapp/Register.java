package com.example.timingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editUsername, editPassword, editEmail, editPhone;
    private TextView registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        configureLink();

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.register_btn);
        registerUser.setOnClickListener(this);

        editUsername = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);
        editEmail = (EditText) findViewById(R.id.email);
        editPhone = (EditText) findViewById(R.id.phone);

    }

    private void configureLink() {
        TextView textView = (TextView) findViewById(R.id.activity_login_link);
        textView.setOnClickListener(v -> finish());
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

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                User user = new User(username,email,phone);
                sendCredentials(email, password);
                Toast.makeText(Register.this, "Authentication confirmed.",
                        Toast.LENGTH_SHORT).show();
                updateUI();
            } else {
                Toast.makeText(Register.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        finish();
    }

    private void sendCredentials(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8082/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                Users users = new Users(userID);
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<Users> call = jsonPlaceHolderApi.createUser(users);

                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(Register.this, "Database User Creation Completed",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {

                    }
                });
            }
        });

        FirebaseAuth.getInstance().signOut();
    }
}
