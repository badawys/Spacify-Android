package co.broccli.spacify.Feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.ButterKnife;
import co.broccli.spacify.R;

public class FeedPostsItem extends AbstractItem<FeedPostsItem, FeedPostsItem.ViewHolder> {

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.feed_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(FeedPostsItem.ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);
    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(FeedPostsItem.ViewHolder holder) {
        super.unbindView(holder);

    }


    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    static class ViewHolder extends RecyclerView.ViewHolder {

        protected View view;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}
