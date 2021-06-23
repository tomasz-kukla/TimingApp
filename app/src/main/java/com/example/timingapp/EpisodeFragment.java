package com.example.timingapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        //Get userID from Firerbase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //GET - info if is Watched
        JsonPlaceHolderApi jsonPlaceHolderApiGET= retrofit.create(JsonPlaceHolderApi.class);
        Call<EpisodeFav> callPOST = jsonPlaceHolderApiGET.getWatched(userId, show_id, season_id, episode_id);
        callPOST.enqueue(new Callback<EpisodeFav>() {
            @Override
            public void onResponse(Call<EpisodeFav> call, Response<EpisodeFav> response) {
                if(response.isSuccessful()){
                    watchedBtn.setBackgroundColor(Color.WHITE);
                    watchedBtn.setTextColor(Color.BLACK);
                    watchedBtn.setText("Mark as not seen");
                    watchedBtn.setOnClickListener(v -> {
                        JsonPlaceHolderApi jsonPlaceHolderApiDELETE= retrofit.create(JsonPlaceHolderApi.class);
                        Call<Void> callDELETE = jsonPlaceHolderApiDELETE.deleteWatched(userId, show_id, season_id, episode_id);
                        callDELETE.enqueue(new Callback<Void>() {

                            @Override
                            public void onResponse(Call<Void> call2, Response<Void> response2) {
                                Toast.makeText(getActivity(), "Marked as not seen.",Toast.LENGTH_SHORT).show();

                                EpisodeFragment episodeFragment = new EpisodeFragment();
                                Bundle args = new Bundle();
                                args.putString("nameShow", show_name);
                                args.putString("idShow", show_id);
                                args.putString("noSeason", season_no);
                                args.putString("titleSeason", season_title);
                                args.putString("idSeason", season_id);
                                args.putString("idEpisode", episode_id);
                                args.putString("nameEpisode", season_title);
//                    Toast.makeText(getActivity(), "Name: " +seriesList.get(position).getId() ,Toast.LENGTH_SHORT).show();
                                episodeFragment.setArguments(args);
                                //Navigate To ShowFragment to display detailed info about show
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.fragment_container, episodeFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                            @Override
                            public void onFailure(Call<Void> call2, Throwable t) {

                            }
                        });
                    });
                } else {

                    //POST add episode as watched
                    watchedBtn.setOnClickListener(v -> {
                        Toast.makeText(getActivity(), season_title + " marked as watched!",Toast.LENGTH_SHORT).show();
                        EpisodeFav episodeFav = new EpisodeFav(true);
                        JsonPlaceHolderApi jsonPlaceHolderApiPOST = retrofit.create(JsonPlaceHolderApi.class);
                        Call<EpisodeFav> callPOST = jsonPlaceHolderApiPOST.addWatched(userId, show_id, season_id, episode_id, episodeFav);
                        callPOST.enqueue(new Callback<EpisodeFav>() {
                            @Override
                            public void onResponse(Call<EpisodeFav> call1, Response<EpisodeFav> response1) {}
                            @Override
                            public void onFailure(Call<EpisodeFav> call1, Throwable t) {}
                        });
                        EpisodeFragment episodeFragment = new EpisodeFragment();
                        Bundle args = new Bundle();
                        args.putString("nameShow", show_name);
                        args.putString("idShow", show_id);
                        args.putString("noSeason", season_no);
                        args.putString("titleSeason", season_title);
                        args.putString("idSeason", season_id);
                        args.putString("idEpisode", episode_id);
                        args.putString("nameEpisode", season_title);
//                    Toast.makeText(getActivity(), "Name: " +seriesList.get(position).getId() ,Toast.LENGTH_SHORT).show();
                        episodeFragment.setArguments(args);
                        //Navigate To ShowFragment to display detailed info about show
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.fragment_container, episodeFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    });
                }
            }
            @Override
            public void onFailure(Call<EpisodeFav> call, Throwable t) { ; }
        });



        //GET - episode info
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
            public void onFailure(Call<Episode> call, Throwable t) {}
        });

        return view;
    }
}