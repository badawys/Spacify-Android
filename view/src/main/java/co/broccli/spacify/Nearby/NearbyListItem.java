package co.broccli.spacify.Nearby;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.broccli.logic.model.space.SpaceData;
import co.broccli.spacify.R;

class NearbyListItem extends AbstractItem<NearbyListItem, NearbyListItem.ViewHolder> {

    protected SpaceData spaceData;

    NearbyListItem withSpaceData(SpaceData spaceData) {
        this.spaceData = spaceData;
        return this;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.nearby_spaces_recyclerview_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(NearbyListItem.ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        viewHolder.spaceName.setText(spaceData.getName());
        viewHolder.spacePhoto.setImageURI("https://spacify.s3-accelerate.amazonaws.com/" + spaceData.getPhoto());
    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(NearbyListItem.ViewHolder holder) {
        super.unbindView(holder);

    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    static class ViewHolder extends RecyclerView.ViewHolder {

        protected View view;

        @BindView(R.id.space_photo)
        SimpleDraweeView spacePhoto;

        @BindView(R.id.space_name)
        TextView spaceName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}
