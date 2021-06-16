package com.example.timingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment {

    TextView show_name;
    TextView show_id;
    Button watch_list;
    ListView list_view;

    public static List<SeriesDetail> seasons_list;

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

//        seasons_list = new ArrayList<>();
//
//        show_name = view.findViewById(R.id.show_name);
//        show_id = view.findViewById(R.id.show_id);
//        watch_list = view.findViewById(R.id.watch_list_btn);
//        list_view = view.findViewById(R.id.show_list);
//
//        String name = getArguments().getString("name");
//        String showId = getArguments().getString("id");
//
//        show_name.setText(name);
//        show_id.setText(showId);
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8082/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        JsonPlaceHolderApi jsonPlaceHolderApi1 = retrofit.create(JsonPlaceHolderApi.class);
//        Call<List<SeriesDetail> call1 = jsonPlaceHolderApi1.getShowDetail(showId);
//
//
//        call1.enqueue(new Callback<List<SeriesDetail>>() {
//            @Override
//            public void onResponse(Call<List<SeriesDetail>> call, Response<List<SeriesDetail>> response) {
//                if(!response.isSuccessful()){Toast.makeText(getActivity(), "Rsp: " +response ,Toast.LENGTH_SHORT).show();}
//                seasons_list = response.body();
//                Toast.makeText(getActivity(), ": " +response ,Toast.LENGTH_SHORT).show();
//
//                list_view.setAdapter(new ShowFragment.SeasonAdapter(response.body(),getActivity().getApplicationContext()));
//
//
//            }
//            @Override
//            public void onFailure(Call<List<SeriesDetail>> call, Throwable t) { }
//        });
//
//
//
//
//        watch_list.setOnClickListener(v -> {
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseUser user = mAuth.getCurrentUser();
//            String userId = user.getUid();
//
//            watch_list.setEnabled(false);
//            Toast.makeText(getActivity(), "Movie Added To Watch List",Toast.LENGTH_SHORT).show();
//
//            Users_Shows users_shows = new Users_Shows(userId, showId);
//            JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi.class);
//            Call<Users_Shows> call2 = jsonPlaceHolderApi2.createUserShow(userId, showId, users_shows);
//
//            call2.enqueue(new Callback<Users_Shows>() {
//                @Override
//                public void onResponse(Call<Users_Shows> call, Response<Users_Shows> response) {
//                }
//                @Override
//                public void onFailure(Call<Users_Shows> call, Throwable t) {
//                }
//            });
//
//        });
//

        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        return view;
    }

    public class SeasonAdapter extends BaseAdapter {
        public List<SeriesDetail> series_list;
        public Context context;

        public SeasonAdapter(List<SeriesDetail> series_list, Context context) {
            this.series_list = series_list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return series_list.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_lists, null);
            TextView name = view.findViewById(R.id.seasonTitle);
//            name.setText(series_list.get(position).getSeasonList().getId());
            return view;
        }
    }



}