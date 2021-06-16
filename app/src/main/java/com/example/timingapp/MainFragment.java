package com.example.timingapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    GridView gridView;
    public static List<Series> seriesList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = view.findViewById(R.id.gridViewMain);

        seriesList = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Series>> call = jsonPlaceHolderApi.getShows();

        call.enqueue(new Callback<List<Series>>() {
            @Override
            public void onResponse(Call<List<Series>> call, Response<List<Series>> response) {
                if(!response.isSuccessful()){;}

                seriesList = response.body();
                Toast.makeText(getActivity(), "Extracted: " + seriesList ,Toast.LENGTH_SHORT).show();

                gridView.setAdapter(new ShowAdapter(response.body(),getActivity().getApplicationContext()));
                gridView.setOnItemClickListener((parent, view1, position, id) -> {

                    DetailShowFragment showFragment = new DetailShowFragment();
                    Bundle args = new Bundle();
                    args.putString("name", seriesList.get(position).getName());
                    args.putString("id", seriesList.get(position).getId());
//                    Toast.makeText(getActivity(), "Name: " +seriesList.get(position).getId() ,Toast.LENGTH_SHORT).show();
                    showFragment.setArguments(args);
                    //Navigate To ShowFragment to display detailed info about show
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, showFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

            }

            @Override
            public void onFailure(Call<List<Series>> call, Throwable t) {
            }
        });

        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        return view;
    }

    public class ShowAdapter extends BaseAdapter{
        public List<Series> seriesList;
        public Context context;

        public ShowAdapter(List<Series> seriesList, Context context) {
            this.seriesList = seriesList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return seriesList.size();
        }
        @Override
        public Object getItem(int position) { return null; }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_data, null);
            TextView name = view.findViewById(R.id.showTitle);
            name.setText(seriesList.get(position).getName());
            return view;
        }

    }

}