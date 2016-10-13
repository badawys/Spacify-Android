package co.broccli.logic.rest;

import co.broccli.logic.model.JoindResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("joind.json")
    Call<JoindResponse> getJoindSpaces();
}