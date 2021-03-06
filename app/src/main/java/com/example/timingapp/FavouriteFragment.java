package com.example.timingapp;

import android.content.Context;
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
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    GridView gridView;
    public static List<Users_List> users_lists;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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

        users_lists = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Users_List>> call = jsonPlaceHolderApi.getFavourites(userId);

        call.enqueue(new Callback<List<Users_List>>() {
            @Override
            public void onResponse(Call<List<Users_List>> call, Response<List<Users_List>> response) {
                if(!response.isSuccessful()){;}
                users_lists = response.body();
//                Toast.makeText(getActivity(), "Extracted: " + users_lists ,Toast.LENGTH_SHORT).show();
                gridView.setAdapter(new ShowAdapter(response.body(),getActivity().getApplicationContext()));

                gridView.setOnItemClickListener((parent, view1, position, id) -> {

                    DetailShowFragment showFragment = new DetailShowFragment();
                    Bundle args = new Bundle();
                    args.putString("name", users_lists.get(position).getShowDAO().getName());
                    args.putString("id", users_lists.get(position).getShowDAO().getId());

//                    Toast.makeText(getActivity(), "Name: " +users_lists.get(position).getShowDAO().getName() ,Toast.LENGTH_SHORT).show();

                    showFragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, showFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                gridView.setOnItemLongClickListener((parent, view12, position, id) -> {

                    JsonPlaceHolderApi jsonPlaceHolderApiDelete = retrofit.create(JsonPlaceHolderApi.class);
                    Call<Void> callDelete = jsonPlaceHolderApiDelete.deleteFavourite(userId,users_lists.get(position).getShowDAO().getId());
                    callDelete.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call1, Response<Void> response1) {
                            Toast.makeText(getActivity(), "Deleted show from WatchList: " + users_lists.get(position).getShowDAO().getName(),Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<Void> call1, Throwable t) { ; }
                    });

                    return false;
                });


            }
            @Override
            public void onFailure(Call<List<Users_List>> call, Throwable t) {

            }
        });

        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
        return view;

    }

    public class ShowAdapter extends BaseAdapter {
        public List<Users_List> users_lists;
        public Context context;

        public ShowAdapter(List<Users_List> users_lists, Context context) {
            this.users_lists = users_lists;
            this.context = context;
        }

        @Override
        public int getCount() {
            return users_lists.size();
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
            View view = LayoutInflater.from(context).inflate(R.layout.row_data, null);
            TextView name = view.findViewById(R.id.showTitle);
            name.setText(users_lists.get(position).getShowDAO().getName());
            return view;
        }

    }
}