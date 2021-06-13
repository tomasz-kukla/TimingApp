package com.example.timingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    EditText etUsername, etPassword, etEmail, etPhone;
    Button logoutBtn, changeBtn, deleteBtn;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etUsername = view.findViewById(R.id.profile_username);
        etPassword = view.findViewById(R.id.profile_password);
        etEmail = view.findViewById(R.id.profile_email);
        etPhone = view.findViewById(R.id.profile_phone);

        logoutBtn = view.findViewById(R.id.logout_btn);
        changeBtn = view.findViewById(R.id.change_btn);
        deleteBtn =view.findViewById(R.id.delete_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String email = user.getEmail();
        String uid = user.getUid();
        String phone = user.getPhoneNumber();

        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        etEmail.setText(settings.getString(email, email));
        etUsername.setText(settings.getString(uid, uid)); //TODO change -> uid username
        etPhone.setText(settings.getString(phone, phone));

        changeBtn.setOnClickListener(v -> {
            String emailIO = etEmail.getText().toString().trim();
            String passwordIO = etPassword.getText().toString().trim();

            if (emailIO.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailIO).matches()) {
                etEmail.setError("Please set valid email.");
                etEmail.requestFocus();
                return;
            }

            user.updateEmail(emailIO)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Changes applied",Toast.LENGTH_SHORT).show();

                        }
                    });
        if (passwordIO.length() != 0) {
            user.updatePassword(passwordIO)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Changes applied", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        });

        deleteBtn.setOnClickListener(v -> {
            user.delete();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8082/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<Void> call =  jsonPlaceHolderApi.deleteUser(uid);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getActivity(),"User deleted successfully!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);

        });

        return view;
    }

}