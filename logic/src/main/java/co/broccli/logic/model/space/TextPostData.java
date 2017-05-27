package co.broccli.logic.model.space;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextPostData {
    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = text;
    }
}
