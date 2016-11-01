package co.broccli.spacify.Profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import co.broccli.spacify.R;


public class MySpacesFragment extends ProfileTabFragmentBase {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_spaces, container, false);

        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(android.R.id.list);
        scrollView.setScrollViewCallbacks(this);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setProfileHeader((LinearLayout) getActivity().findViewById(R.id.profile_header));
        this.setProfileTabLayout((LinearLayout) getActivity().findViewById(R.id.profile_tab_layout));

    }

}
