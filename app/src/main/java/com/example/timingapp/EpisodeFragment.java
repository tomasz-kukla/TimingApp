package com.example.timingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpisodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpisodeFragment extends Fragment {

    TextView showNameView;
    TextView seasonNoView;
    TextView seasonTitle;
    TextView titleView;
    TextView descriptionView;
    TextView noEpisode;
    Button watchedBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EpisodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EpisodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EpisodeFragment newInstance(String param1, String param2) {
        EpisodeFragment fragment = new EpisodeFragment();
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
        View view = inflater.inflate(R.layout.fragment_episode, container, false);

        showNameView = view.findViewById(R.id.show_name_episode);
        seasonTitle = view.findViewById(R.id.episode_title_episode);
        seasonNoView = view.findViewById(R.id.season_no_episode);

        descriptionView = view.findViewById(R.id.episode_description);
        noEpisode = view.findViewById(R.id.episode_no);

        watchedBtn = view.findViewById(R.id.watched_btn);


        String show_id = getArguments().getString("idShow");
        String show_name = getArguments().getString("nameShow");
        String season_id = getArguments().getString("idSeason");
        String season_no = getArguments().getString("noSeason");
        String season_title = getArguments().getString("titleSeason");
        String episode_id= getArguments().getString("idEpisode");

        showNameView.setText(show_name);
        seasonTitle.setText(season_title);
        seasonNoView.setText(season_no);



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Episode> call = jsonPlaceHolderApi.getEpisode(show_id, season_id, episode_id);

        call.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Call<Episode> call, Response<Episode> response) {
                if(!response.isSuccessful()){;}

                descriptionView.setText(response.body().getDescription());
                noEpisode.setText(Integer.toString(response.body().getNoOfEpisode()));

            }

            @Override
            public void onFailure(Call<Episode> call, Throwable t) {

            }
        });


        return view;
    }
}