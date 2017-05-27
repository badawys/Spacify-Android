package co.broccli.logic.model.space;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePost {

    @SerializedName("post")
    @Expose
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}