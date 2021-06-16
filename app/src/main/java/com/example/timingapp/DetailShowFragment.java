package com.example.timingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailShowFragment extends Fragment {

    TextView show_name;
    TextView show_id;
    Button watch_list;
    GridView gridView;
    public static List<Season> seriesList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailShowFragment newInstance(String param1, String param2) {
        DetailShowFragment fragment = new DetailShowFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_show, container, false);

        show_name = view.findViewById(R.id.show_name);
        show_id = view.findViewById(R.id.show_id);
        watch_list = view.findViewById(R.id.watch_list_btn);
        gridView = view.findViewById(R.id.gridViewShowDetail);

        seriesList = new ArrayList<>();

        String showId = getArguments().getString("id");
        String showName = getArguments().getString("name");

        show_id.setText(showId);
        show_name.setText(showName);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<SeriesDetail> call = jsonPlaceHolderApi.getShowDetail(showId);

        call.enqueue(new Callback<SeriesDetail>() {
            @Override
            public void onResponse(Call<SeriesDetail> call, Response<SeriesDetail> response) {
                if(!response.isSuccessful()){
                }
                seriesList = response.body().getSeasonList();
//                Toast.makeText(getActivity(), "Extracted: " +seriesList.toString(),Toast.LENGTH_SHORT).show();
                gridView.setAdapter(new DetailShowAdapter(seriesList,getActivity().getApplicationContext()));

                gridView.setOnItemClickListener((parent, view1, position, id) -> {

                    SeasonFragment seasonFragment = new SeasonFragment();
                    Bundle args = new Bundle();
                    args.putString("nameShow", showName);
                    args.putString("idShow", showId);
                    args.putString("noSeason", Integer.toString(seriesList.get(position).getNoOfSeason()));
                    args.putString("idSeason", seriesList.get(position).getId());
//                    Toast.makeText(getActivity(), "Name: " +seriesList.get(position).getId() ,Toast.LENGTH_SHORT).show();
                    seasonFragment.setArguments(args);
                    //Navigate To ShowFragment to display detailed info about show
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, seasonFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
            }

            @Override
            public void onFailure(Call<SeriesDetail> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG,Log.getStackTraceString(t));
                Toast.makeText(getActivity(), "Failure: " ,Toast.LENGTH_SHORT).show();
            }
        });

        watch_list.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();

            watch_list.setEnabled(false);
            Toast.makeText(getActivity(), "Movie Added To Watch List",Toast.LENGTH_SHORT).show();

            Users_Shows users_shows = new Users_Shows(userId, showId);
            JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi.class);
            Call<Users_Shows> call2 = jsonPlaceHolderApi2.createUserShow(userId, showId, users_shows);

            call2.enqueue(new Callback<Users_Shows>() {
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

    public class DetailShowAdapter extends BaseAdapter {
        public List<Season> seriesList;
        public Context context;

        public DetailShowAdapter(List<Season> seriesList, Context context) {
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
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_lists, null);
            TextView name = view.findViewById(R.id.seasonTitle);
            int seasonNo = seriesList.get(position).getNoOfSeason();
            name.setText(Integer.toString(seasonNo));
            return view;
        }

    }


}