package co.broccli.logic.model.space;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSpace {
    @SerializedName("space")
    @Expose
    private Space space;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

}
