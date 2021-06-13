package com.example.timingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment {

    TextView show_name;
    TextView show_id;
    Button watch_list;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance(String param1, String param2) {
        ShowFragment fragment = new ShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        show_name = view.findViewById(R.id.show_name);
        show_id = view.findViewById(R.id.show_id);
        watch_list = view.findViewById(R.id.watch_list_btn);

        String name = getArguments().getString("name");
        String showId = getArguments().getString("id");

        show_name.setText(name);
        show_id.setText(showId);

        watch_list.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();

            watch_list.setEnabled(false);
            Toast.makeText(getActivity(), "Movie Added To Watch List",Toast.LENGTH_SHORT).show();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8082/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            Users_Shows users_shows = new Users_Shows(userId, showId);
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Users_Shows> call = jsonPlaceHolderApi.createUserShow(userId, showId, users_shows);

            call.enqueue(new Callback<Users_Shows>() {
                @Override
                public void onResponse(Call<Users_Shows> call, Response<Users_Shows> response) {

                }

                @Override
                public void onFailure(Call<Users_Shows> call, Throwable t) {

                }
            });

        });





        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        return view;
    }


}