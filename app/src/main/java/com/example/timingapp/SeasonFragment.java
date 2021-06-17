package com.example.timingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
 * Use the {@link SeasonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonFragment extends Fragment {

    TextView showName;  // show_name_season
    TextView showId;    // show_id_season
    TextView seasonId;  // season_no
    GridView gridView;

    public static List<EpisodeList> episodeLists;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeasonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeasonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeasonFragment newInstance(String param1, String param2) {
        SeasonFragment fragment = new SeasonFragment();
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
        View view = inflater.inflate(R.layout.fragment_season, container, false);

        showName = view.findViewById(R.id.show_name_season);
        showId = view.findViewById(R.id.show_id_season);
        seasonId = view.findViewById(R.id.season_no);
        gridView = view.findViewById(R.id.gridViewEpisodeDetail);

        episodeLists = new ArrayList<>();

        String show_id = getArguments().getString("idShow");
        String show_name = getArguments().getString("nameShow");
        String season_id = getArguments().getString("idSeason");
        String season_no = getArguments().getString("noSeason");

        showName.setText(show_name);
        showId.setText(show_id);
        seasonId.setText(season_id);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<SeasonDetail> call = jsonPlaceHolderApi.getSeasonDetail(show_id, season_id);

        call.enqueue(new Callback<SeasonDetail>() {
            @Override
            public void onResponse(Call<SeasonDetail> call, Response<SeasonDetail> response) {
                if(!response.isSuccessful()){;}

                episodeLists = response.body().getEpisodes();
                gridView.setAdapter(new DetailSeasonAdapter(episodeLists,getActivity().getApplicationContext()));

                gridView.setOnItemClickListener((parent, view1, position, id) -> {

                    EpisodeFragment episodeFragment = new EpisodeFragment();
                    Bundle args = new Bundle();
                    args.putString("nameShow", show_name);
                    args.putString("idShow", show_id);
                    args.putString("noSeason", Integer.toString(episodeLists.get(position).getNoOfEpisode()));
                    args.putString("titleSeason", episodeLists.get(position).getTitle());
                    args.putString("idSeason", season_id);
                    args.putString("idEpisode", episodeLists.get(position).getId());
                    args.putString("nameEpisode", Integer.toString(episodeLists.get(position).getNoOfEpisode()));
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

            @Override
            public void onFailure(Call<SeasonDetail> call, Throwable t) {

            }
        });

        return view;
    }


    public class DetailSeasonAdapter extends BaseAdapter {
        public List<EpisodeList> episodeList;
        public Context context;

        public DetailSeasonAdapter(List<EpisodeList> episodeList, Context context) {
            this.episodeList = episodeList;
            this.context = context;
        }
        @Override
        public int getCount() {
            return episodeList.size();
        }
        @Override
        public Object getItem(int position) { return null; }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_data_season, null);
            TextView name = view.findViewById(R.id.episodeTitle);
            name.setText(Integer.toString(episodeList.get(position).getNoOfEpisode()) + " : " + episodeList.get(position).getTitle());
            return view;
        }

    }


}