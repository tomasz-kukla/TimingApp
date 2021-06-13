package com.example.timingapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("shows")
    Call<List<Series>> getShows();

    @POST("users")
    Call<Users> createUser(@Body Users users);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id);



}
