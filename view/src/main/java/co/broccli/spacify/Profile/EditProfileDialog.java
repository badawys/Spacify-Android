package co.broccli.spacify.Profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.victor.loading.rotate.RotateLoading;

import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.profile.User;
import co.broccli.spacify.R;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import mehdi.sakout.fancybuttons.FancyButton;


public class EditProfileDialog extends SupportBlurDialogFragment {

    private LinearLayout dialogContent;
    private RotateLoading rotateLoading;
    private SimpleDraweeView profilePhoto;
    private EditText userName;
    private EditText email;
    private FancyButton cancelButton;
    private FancyButton okButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_profile, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialogContent = (LinearLayout) v.findViewById(R.id.dialog_content);
        rotateLoading = (RotateLoading) v.findViewById(R.id.rotateloading);
        profilePhoto = (SimpleDraweeView) v.findViewById(R.id.user_pic_view);
        userName = (EditText) v.findViewById(R.id.input_name);
        email = (EditText) v.findViewById(R.id.input_email);
        cancelButton = (FancyButton) v.findViewById(R.id.cancelButton);
        okButton = (FancyButton) v.findViewById(R.id.okButton);

        dialogContent.setVisibility(View.INVISIBLE);
        rotateLoading.start();

        SpacifyApi.profile().getUserProfile(getContext(), new Callback<User>() {
            @Override
            public void onResult(User user) {
                setProfilePhoto (user.getPhoto());
                userName.setText(user.getName());
                email.setText(user.getEmail());
                rotateLoading.stop();
                dialogContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }


    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 5;
    }

    @Override
    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 7;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return true;
    }
    /**
     * ToDo: Add Description
     */
    private void setProfilePhoto (String url) {

        Uri uri = Uri.parse("http://spacify.s3.amazonaws.com/" + url);

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();

        PipelineDraweeController photoController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(profilePhoto.getController())
                        .build();

        profilePhoto.setController(photoController);
    }
}
