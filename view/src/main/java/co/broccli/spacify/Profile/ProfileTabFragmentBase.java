package co.broccli.spacify.Profile;


import android.animation.ValueAnimator;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import co.broccli.spacify.R;

public class ProfileTabFragmentBase extends Fragment implements ObservableScrollViewCallbacks {

    private LinearLayout profileHeader;
    private LinearLayout profileTabLayout;


    public void setProfileHeader(LinearLayout profileHeader) {
        this.profileHeader = profileHeader;
    }


    public void setProfileTabLayout(LinearLayout profileTabLayout) {
        this.profileTabLayout = profileTabLayout;
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
            if (profileHeaderIsShown()) {
                hideProfileHeader();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (profileHeaderIsHidden()) {
                showProfileHeader();
            }
        }
    }

    private boolean profileHeaderIsShown() {
        // Toolbar is 0 in Y-axis, so we can say it's shown.
        return profileHeader.getTranslationY() == 0;
    }

    private boolean profileHeaderIsHidden() {
        // Toolbar is outside of the screen and absolute Y matches the height of it.
        // So we can say it's hidden.
        return profileHeader.getTranslationY() == -profileHeader.getHeight();
    }

    private void showProfileHeader() {
        moveProfileHeader(0);
    }

    private void hideProfileHeader() {
        moveProfileHeader(-profileHeader.getHeight());
    }

    private void moveProfileHeader(float toTranslationY) {
        // Check the current translationY
        if (profileHeader.getTranslationY() == toTranslationY) {
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(profileHeader.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                profileHeader.setTranslationY(translationY);
                profileTabLayout.setTranslationY(translationY);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) profileTabLayout.getLayoutParams();
                lp.height = getContainerHeight();
                profileTabLayout.setLayoutParams(lp);
                profileTabLayout.requestLayout();
            }
        });
        animator.start();
    }

    private int getContainerHeight() {
        return (getActivity().findViewById(R.id.fragment_container)).getHeight();
    }
}
