package co.broccli.spacify.Profile;

import android.animation.ValueAnimator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import co.broccli.spacify.R;

public class ProfileTabFragmentBase extends Fragment implements ObservableScrollViewCallbacks {

    private LinearLayout profileHeader;
    private LinearLayout profileTabLayout;
    private boolean profileTabLayoutIsExtended = false;
    private LinearLayout.LayoutParams defaultProfileTapLayoutParms;

    private LinearLayoutManager linearLayoutManager;

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setProfileHeader(LinearLayout profileHeader) {
        this.profileHeader = profileHeader;
    }

    public void setProfileTabLayout(LinearLayout profileTabLayout) {
        this.profileTabLayout = profileTabLayout;
        this.defaultProfileTapLayoutParms = (LinearLayout.LayoutParams) profileTabLayout.getLayoutParams();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (profileTabLayout.getTranslationY() == 0) {
                moveProfileHeader(-profileHeader.getHeight());
                if (!profileTabLayoutIsExtended) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) profileTabLayout.getLayoutParams();
                    lp.height = getContainerHeight();
                    profileTabLayout.setLayoutParams(lp);
                    profileTabLayout.requestLayout();
                    profileTabLayoutIsExtended = true;
                }
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                moveProfileHeader(0);
                if (profileTabLayoutIsExtended) {
                    profileTabLayout.setLayoutParams(defaultProfileTapLayoutParms);
                    profileTabLayout.requestLayout();
                    profileTabLayoutIsExtended = false;
                }
            }
        }
    }

    private void moveProfileHeader(float toTranslationY) {
        // Check the current translationY
        if (profileTabLayout.getTranslationY() == toTranslationY) {
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(profileTabLayout.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                profileTabLayout.setTranslationY(translationY);
            }
        });
        animator.start();
    }


    private int getContainerHeight() {
        return (getActivity().findViewById(R.id.fragment_container)).getHeight();
    }
}
