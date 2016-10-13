package co.broccli.logic.model;

import com.google.gson.annotations.SerializedName;

public class Joind {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("img")
    public String img;

    public Joind (String id, String name, String img) {

        this.id = id;
        this.name = name;
        this.img = img;

    }

    public String getSpaceId () {
        return id;
    }

    public String getSpaceName () {
        return name;
    }

    public String getSpaceImg () {
        return img;
    }
    }