package co.broccli.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JoindResponse {

    @SerializedName("data")
    private List<Joind> data;


    public List<Joind> getData () {
        return data;
    }

}