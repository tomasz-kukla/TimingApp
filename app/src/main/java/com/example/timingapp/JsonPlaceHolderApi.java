package com.example.timingapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("shows")
    Call<List<Series>> getShows();

    @POST("users")
    Call<Users> createUser(@Body Users users);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @POST("users/{userId}/shows/{showId}")
    Call<Users_Shows> createUserShow(@Path("userId") String userId, @Path("showId") String showId, @Body Users_Shows users_shows);

    @GET("users/{userId}/shows")
    Call<List<Users_List>> getFavourites(@Path("userId") String userId);

    @GET("shows/details/{id}")
    Call<SeriesDetail> getShowDetail(@Path("id") String id);

    @GET("shows/details/{showId}/seasons/{seasonId}")
    Call<SeasonDetail> getSeasonDetail(@Path("showId") String showId, @Path("seasonId") String seasonId);

    @GET("shows/details/{showId}/seasons/{seasonId}/episodes/{episodeId}")
    Call<Episode> getEpisode(@Path("showId") String showId, @Path("seasonId") String seasonId, @Path("episodeId") String episodeId);



    
}
