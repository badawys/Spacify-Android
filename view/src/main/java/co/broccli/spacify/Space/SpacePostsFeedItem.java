package co.broccli.spacify.Space;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.broccli.logic.model.space.Post;
import co.broccli.logic.model.space.TextPostData;
import co.broccli.spacify.R;

public class SpacePostsFeedItem extends AbstractItem<SpacePostsFeedItem, SpacePostsFeedItem.ViewHolder> {

    protected Post postData;

    SpacePostsFeedItem withPostData(Post postData) {
        this.postData = postData;
        return this;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.space_feed_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(SpacePostsFeedItem.ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        Gson gson = new Gson();
        TextPostData textPostData = gson.fromJson(postData.getData(), TextPostData.class);

        viewHolder.postText.setText(textPostData.getText());
        viewHolder.userPhoto.setImageURI("https://spacify.s3-accelerate.amazonaws.com/" +
                postData.getUser().getPhoto());
        viewHolder.postUserName.setText(postData.getUser().getName());
        viewHolder.postDate.setText(postData.getCreatedAt());
    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(SpacePostsFeedItem.ViewHolder holder) {
        super.unbindView(holder);

    }


    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    static class ViewHolder extends RecyclerView.ViewHolder {

        protected View view;

        @BindView(R.id.user_photo)
        SimpleDraweeView userPhoto;

        @BindView(R.id.post_user_name)
        TextView postUserName;

        @BindView(R.id.post_created_at)
        TextView postDate;

        @BindView(R.id.post_text)
        TextView postText;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}
