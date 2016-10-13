package co.broccli.broccli.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import co.broccli.broccli.R;
import co.broccli.logic.model.Joind;


public class JoindAdapter extends UltimateViewAdapter<JoindAdapter.JoindViewHolder>{

    List <Joind> JoinList;

    public JoindAdapter(List<Joind> JoinList) {
        this.JoinList = JoinList;
    }


    @Override
    public JoindViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.joind_card, viewGroup, false);
        JoindViewHolder joindViewHolder = new JoindViewHolder(v);
        return joindViewHolder;
    }

    @Override
    public JoindViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public JoindViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public JoindViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(JoindViewHolder joindViewHolder, int i) {
        joindViewHolder.name.setText(JoinList.get(i).name);
        //joindViewHolder.img.setImageURI(JoinList.get(i).img);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return JoinList.size();
    }

    @Override
    public int getAdapterItemCount() {
        return JoinList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class JoindViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView name;
        ImageView img;

        public JoindViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.joind_card);
            name = (TextView) itemView.findViewById(R.id.space_name);
            img = (ImageView) itemView.findViewById(R.id.space_photo);
        }
    }
}
