package co.broccli.spacify.Space;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro2;
import com.google.android.gms.maps.model.LatLng;
import javax.annotation.Nullable;
import co.broccli.spacify.R;

public class CreateSpaceActivity extends AppIntro2 {

    private LatLng SpaceLocation;
    private Uri SpacePhoto;
    private String SpaceName;
    private int SpaceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWizardMode(true);

        addSlide(CreateSpaceStep1Fragment.newInstance());
        addSlide(CreateSpaceStep2Fragment.newInstance());

        setBackButtonVisibilityWithDone(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        if (oldFragment != null) {
            if (oldFragment instanceof CreateSpaceStep1Fragment) {
                SpaceLocation = ((CreateSpaceStep1Fragment) oldFragment).currentLocation;
            }
            if (oldFragment instanceof CreateSpaceStep2Fragment) {
                SpaceName = ((CreateSpaceStep2Fragment) oldFragment).SpaceName;
                SpaceType = ((CreateSpaceStep2Fragment) oldFragment).SpaceType;
                SpacePhoto = ((CreateSpaceStep2Fragment) oldFragment).SpacePhoto;
            }
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        if (SpaceLocation != null && SpaceType > 0 && SpaceName != null){
            final ProgressDialog progressDialog = new ProgressDialog(CreateSpaceActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.create_space_wait_message));
            progressDialog.show();
        }
    }
}
