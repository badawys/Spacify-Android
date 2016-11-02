package co.broccli.spacify.Profile.MySpaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.broccli.spacify.R;

public class MySpacesItem extends AbstractItem<MySpacesItem, MySpacesItem.ViewHolder> {

    protected String imageURL;
    protected String name;

    public MySpacesItem withImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public MySpacesItem withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.my_spaces_recyclerview_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        viewHolder.spaceName.setText(name);
    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

    }


    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        protected View view;

        @BindView(R.id.space_photo)
        protected SimpleDraweeView spacePhoto;

        @BindView(R.id.space_name)
        protected TextView spaceName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}
