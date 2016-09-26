package co.broccli.sdk.rest;

import co.broccli.sdk.model.JoindResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("joind.json")
    Call<JoindResponse> getJoindSpaces();
}